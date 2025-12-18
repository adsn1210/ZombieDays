package ucjc.ev2.controller;

import ucjc.ev2.model.EstadoJuego;
import ucjc.ev2.model.Laberinto;
import ucjc.ev2.model.Zombie;
import ucjc.ev2.view.Panelgame;
import ucjc.ev2.model.Pocima;
import ucjc.ev2.model.Pumpkin;
import ucjc.ev2.model.Objetos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.*;

public class Buclegame {

    private static final int FPS = 60;
    private final Timer timer;
    private final Zombie zombie;
    private final Controladorteclado controladorteclado;
    private final Panelgame panel;
    private final Laberinto laberinto;
    private static final int DURACION_PARTIDA = 30;
    private long tiempoInicio; //Cuando se llame cogera el Tiempo ABSOLUTO (el del sistema)
    private EstadoJuego estado;

    private final List<Pumpkin> pumpkins = new ArrayList<>();
    private Pocima pocima;
    private final Random rand = new Random();

    public Buclegame(Zombie zombie, Controladorteclado controladorteclado, Panelgame panel, Laberinto laberinto) {
        this.zombie = zombie;
        this.controladorteclado = controladorteclado;
        this.panel = panel;
        this.laberinto = laberinto;
        int celda = laberinto.getTamanoCelda();
        int delay = 1000 / FPS;

        this.timer = new Timer(delay, e -> actualizar()); //Aqui le decimos al TIMER que hacer
        //cada vez que pase el tiempo del delay, ejecuta el metodo actualizar()".

        // 1. Crear 5 Pumpkins en sitios aleatorios
        for (int i = 0; i < 5; i++) {
            java.awt.Point p = laberinto.celdaCaminoAleatoria(rand);
            double x = p.x * celda;
            double y = p.y * celda;

            Image imgP = new ImageIcon(getClass().getResource("/sprites/items/pumpkin.png")).getImage();
            pumpkins.add(new Pumpkin(x, y, celda, celda, imgP));
        }

        // 2. Crear 1 Pócima
        java.awt.Point pPocion = laberinto.celdaCaminoAleatoria(rand);
        Image imgPocima = new ImageIcon(getClass().getResource("/sprites/items/pocima.png")).getImage();
        this.pocima = new Pocima(pPocion.x * celda, pPocion.y * celda, celda, celda, imgPocima);

        // 3. Informar al panel para que sepa qué dibujar
        panel.setPumpkins(pumpkins);
        panel.setPocima(pocima);
    }

    private void actualizar() {

        if (estado != EstadoJuego.EN_JUEGO) { //EVITAMOS QUE EL JUEGO SIGA CORRIENDO CUANDO ACABE
            timer.stop();
            return;
        }
        actualizarTiempo();

        double multTeclado = controladorteclado.multiplicadorVelocidad();

        Controladorteclado.Direccion direccion = controladorteclado.direccionActual();
        double deltaX = 0;
        double deltaY = 0;

        switch (direccion) {
            case ARRIBA -> deltaY = -1;
            case ABAJO -> deltaY = 1;//Positivo porque en los juegos el EJE Y para abajo es POSITIVOO
            //Y te preguntaras porque es POSITIVO? :( pues porque el 0,0 EMPIEZA en la esquina izquierda superior...XK? nolose
            case DERECHA -> deltaX = 1;
            case IZQUIERDA -> deltaX = -1;
            default -> {
            }
        }

        // --- MOVIMIENTO DEL ZOMBIE ---
        if (direccion != Controladorteclado.Direccion.NINGUNA) {
            zombie.moverConColisiones(deltaX, deltaY,multTeclado, laberinto);
        }
        boolean seMueve = (direccion != Controladorteclado.Direccion.NINGUNA);
        zombie.setMoviendose(seMueve);
        zombie.actualizarAnimacion();


        // --- LÓGICA DE OBJETOS (FUERA DEL BLOQUE DE DIRECCIÓN PARA QUE SE MUEVAN SIEMPRE) ---

        // A) Mover calabazas
        for (Pumpkin p : pumpkins) {
            p.update(laberinto);
        }

        // B) Colisión con Calabazas (PENALIZACIÓN)
        for (Pumpkin p : pumpkins) {
            if (p.isActivo() && zombie.getHitbox().intersects(p.getHitbox())) {
                zombie.setVelocidad(zombie.getVelocidad() * 0.5);

                p.desactivar(); // Desaparece al tocarla
                System.out.println("¡Calabaza! Velocidad reducida.");

                // --- NUEVA LÓGICA: RESTAURACIÓN TRAS 3 SEGUNDOS ---
                // Creamos un Timer que no bloquea el juego (asíncrono)
                Timer timerRestaurar = new Timer(3000, e -> {
                    zombie.setVelocidad(zombie.getVelocidad() / 0.5); // Divide por 0.5 para volver al valor original
                    System.out.println("¡Efecto de calabaza terminado! Velocidad normal.");
                });
                timerRestaurar.setRepeats(false); // Solo se ejecuta una vez
                timerRestaurar.start();
            }
        }

        // C) Colisión con Pócima (BENEFICIO)
        if (pocima != null && pocima.isActivo() && zombie.getHitbox().intersects(pocima.getHitbox())) {
            zombie.setVelocidad(zombie.getVelocidad() * 1.2);
            pocima.desactivar(); // Desaparece
            System.out.println("¡Pócima mágica! Velocidad aumentada.");
        }

        panel.repaint(); //Lo sacamos para que el tiempo tambien se vea como va bajando
    }

    /*
     * TIMER
     * Necesita 2 cosas:
     * EL delay()Cuanto tiempo esperar entre cada tic en milisegundos
     * El ActionListener que debe ejecutarse
     * .start()
     * .stop()
     * .restart()
     * *
     * Usar System.currentTimeMillis() es mas seguro porque coge el tiempo del sistema
     *
     * **/
    private void actualizarTiempo() {
        long ahora = System.currentTimeMillis();
        int segundosPasados = (int) ((ahora - tiempoInicio) / 1000);
        //Resta el momento actual - desde cuando empezo = a cuanto ha pasado y como es en milisegundos lo /1000 con el (int) lo truncamos para que de un numero entero
        int restantes = DURACION_PARTIDA - segundosPasados; //No usamos tiempo-- por fallos de lag

        //Se dibujara
        panel.setTiempoRestante(restantes);
            /*Una duda que puede surgir es...? si la pantalla se refreca cada 16.6ms (1000/FPS (1000/60 = 16.6)) y la pantalla
            si tiene esa tasa de refresco porque el timer no se vuelve loco ya que estariamos pasando esa misma variable
            al Timer -> Actualizar->ActualizarTiempo() cada 16milisegundos no cada 1seg o 1000milisegundos;
            Pues no se vuelve loco por el Truncamiento de (int)
            */
        if (restantes <= 0) {
            estado = EstadoJuego.GAME_OVER;
            timer.stop();
            panel.mostrarGameOver();
        }
    }

    public void iniciar() {
        this.tiempoInicio = System.currentTimeMillis();
        this.estado = EstadoJuego.EN_JUEGO;//enum
        timer.start();
    }
}