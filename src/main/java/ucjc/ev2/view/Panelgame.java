package ucjc.ev2.view;

// Aqui se pinta -> Laberinto -> Personaje -> Debug

import ucjc.ev2.model.Laberinto;
import ucjc.ev2.model.Zombie;
import ucjc.ev2.model.Pocima;
import ucjc.ev2.model.Pumpkin;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class Panelgame extends JPanel {

    private final Laberinto laberinto;
    private final Zombie zombie;

    // Imágenes (tiles)
    private final Image[] paredes;
    private final int[][] paredElegida;
    private final Image[] suelos;
    private final int[][] sueloElegido;
    private final Image imgEntrada;
    private final Image imgSalida;
    private int tiempoRestante = 20;
    private List<Pumpkin> pumpkins;
    private Pocima pocima;

    public void setPumpkins(List<Pumpkin> pumpkins) {
        this.pumpkins = pumpkins;
    }

    public void setPocima(Pocima pocima) {
        this.pocima = pocima;
    }

    public Panelgame(Laberinto laberinto, Zombie zombie) {
        this.laberinto = laberinto;
        this.zombie = zombie;

        // Cargar imágenes
        paredes = new Image[] {
                new ImageIcon(getClass().getResource("/tiles/pared1.png")).getImage(),
                new ImageIcon(getClass().getResource("/tiles/pared2.png")).getImage(),
                new ImageIcon(getClass().getResource("/tiles/pared3.png")).getImage()
        };
        int filas = laberinto.getMapa().length;
        int columnas = laberinto.getMapa()[0].length;

        paredElegida = new int[filas][columnas];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (laberinto.getMapa()[f][c] == 1) {
                    paredElegida[f][c] = (int)(Math.random() * paredes.length);
                }
            }
        }
        suelos= new Image[] {
                new ImageIcon(getClass().getResource("/tiles/sueloNieve.png")).getImage(),
                new ImageIcon(getClass().getResource("/tiles/sueloNieve2.png")).getImage(),
        };
        // === Asignar suelo aleatorio por celda (UNA VEZ) ===
        sueloElegido = new int[filas][columnas];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (laberinto.getMapa()[f][c] == 0) {
                    sueloElegido[f][c] = (int) (Math.random() * suelos.length);
                }
            }
        }


        imgEntrada = new ImageIcon(getClass().getResource("/tiles/entrada.png")).getImage();
        imgSalida = new ImageIcon(getClass().getResource("/tiles/salida.png")).getImage();

        int ancho = laberinto.getMapa()[0].length * laberinto.getTamanoCelda();
        int alto  = laberinto.getMapa().length * laberinto.getTamanoCelda();

        setPreferredSize(new Dimension(ancho, alto));
        setDoubleBuffered(true);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        try {
            dibujarLaberinto(g2d);
            zombie.dibujar(g2d);
            if (pumpkins != null) {
                for (Pumpkin p : pumpkins) {
                    p.dibujar(g2d, this);
                }
            }
            if (pocima != null) {
                pocima.dibujar(g2d, this);
            }
            dibujarHUD(g2d);
        } finally {
            g2d.dispose();
        }
    }

    /*Ya que nuestro Juego se redibuja 60veces x segundo no podemos usar un Math.random para generar la aletoriedad de los muros
    si no estarian parpadean constantemente.
    Solucion crear una Memoria con = paredElegida
        Creamos un Catalogo con 3 imagenes (un array)

    */
    private void dibujarLaberinto(Graphics2D g2d) {
        int[][] mapa = laberinto.getMapa();
        int celda = laberinto.getTamanoCelda();

        // Suelo = 0,
        for (int fila = 0; fila < mapa.length; fila++) {
            for (int columna = 0; columna < mapa[0].length; columna++) {

                int x = columna * celda;
                int y = fila * celda;

                //No ponemos un IF para que haya suelo siempre como Base y aseguramos un Background
                int idxSuelo = sueloElegido[fila][columna];
                g2d.drawImage(suelos[idxSuelo], x, y, celda, celda, this);

                // Pared = 1
                if (mapa[fila][columna] == 1) {
                    int idx = paredElegida[fila][columna];
                    g2d.drawImage(paredes[idx], x, y, celda, celda, this);
                }
            }
        }

        // Entrada y salida
        Point entrada = laberinto.getEntradaEnPixeles();
        Point salida  = laberinto.getSalidaEnPixeles();

        if (imgEntrada != null) {
            g2d.drawImage(imgEntrada, entrada.x, entrada.y, celda, celda, this);
        }

        if (imgSalida != null) {
            g2d.drawImage(imgSalida, salida.x, salida.y, celda, celda, this);
        }
    }

    //PARA PRUEBAS, Dibujamos un cuadrado alrededor del personaje... Sirve para darle el % correcto de size al PJ
    private void dibujarHitbox(Graphics2D g2d) {
        Rectangle hitbox = zombie.getHitbox();
        g2d.setColor(new Color(255, 255, 255, 120));
        g2d.draw(hitbox);
    }
    public void setTiempoRestante(int tiempo) {
        this.tiempoRestante = Math.max(0, tiempo);
    }

    // provisional
    public void mostrarGameOver() {
        System.out.println("GAME OVER");
    }
    private void dibujarHUD(Graphics2D g2d) {
        // Fondo del HUD (barra inferior)
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, getHeight() - 40, getWidth(), 40);

        // Texto del tiempo
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Tiempo: " + tiempoRestante + "s", 10, getHeight() - 14);
    }
}
