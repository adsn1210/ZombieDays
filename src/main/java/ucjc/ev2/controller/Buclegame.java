package ucjc.ev2.controller;

import ucjc.ev2.model.EstadoJuego;
import ucjc.ev2.model.Laberinto;
import ucjc.ev2.model.Zombie;
import ucjc.ev2.view.Panelgame;

import javax.swing.*;

public class Buclegame {

    private static final int FPS = 60;
    private final Timer timer;
    private final Zombie zombie;
    private final Controladorteclado controladorteclado;
    private final Panelgame panel;
    private final Laberinto laberinto;
    private final double velocidadBase;
    private static final int DURACION_PARTIDA = 20;
    private long tiempoInicio; //Cuando se llame cogera el Tiempo ABSOLUTO (el del sistema)
    private EstadoJuego estado;

    public Buclegame(Zombie zombie, Controladorteclado controladorteclado, Panelgame panel, Laberinto laberinto) {
        this.zombie = zombie;
        this.controladorteclado = controladorteclado;
        this.panel = panel;
        this.laberinto = laberinto;
        this.velocidadBase = zombie.getVelocidad();
        int delay = 1000 / FPS;
        this.timer = new Timer(delay, e -> actualizar()); //Aqui le decimos al TIMER que hacer
        //cada vez que pase el tiempo del delay, ejecuta el metodo actualizar()".
    }

    private void actualizar() {

        if (estado != EstadoJuego.EN_JUEGO) { //EVITAMOS QUE EL JUEGO SIGA CORRIENDO CUANDO ACABE
            timer.stop();
            return;
        }
        actualizarTiempo();

        zombie.setVelocidad(velocidadBase * controladorteclado.multiplicadorVelocidad());

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
        if (direccion != Controladorteclado.Direccion.NINGUNA) {
            zombie.moverConColisiones(deltaX, deltaY, laberinto);
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
