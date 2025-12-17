package ucjc.ev2.view;

// Aqui se pinta -> Laberinto -> Personaje -> Debug

import ucjc.ev2.model.Laberinto;
import ucjc.ev2.model.Zombie;

import javax.swing.*;
import java.awt.*;

public class Panelgame extends JPanel {

    private final Laberinto laberinto;
    private final Zombie zombie;

    // Imágenes (tiles)
    private final Image Pared;
    private final Image Suelo;
    private final Image imgEntrada;
    private final Image imgSalida;

    public Panelgame(Laberinto laberinto, Zombie zombie) {
        this.laberinto = laberinto;
        this.zombie = zombie;

        // Cargar imágenes
        Pared = new ImageIcon(getClass().getResource("/tiles/Pared.png")).getImage();
        Suelo = new ImageIcon(getClass().getResource("/tiles/muroNieve.png")).getImage();
        imgEntrada = new ImageIcon(getClass().getResource("/tiles/entry.png")).getImage();
        imgSalida = new ImageIcon(getClass().getResource("/tiles/exit.png")).getImage();

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
        } finally {
            g2d.dispose();
        }
    }

    private void dibujarLaberinto(Graphics2D g2d) {
        int[][] mapa = laberinto.getMapa();
        int celda = laberinto.getTamanoCelda();

        for (int fila = 0; fila < mapa.length; fila++) {
            for (int columna = 0; columna < mapa[0].length; columna++) {

                int x = columna * celda;
                int y = fila * celda;

                //No ponemos un IF para que haya suelo siempre como Base y aseguramos un Background
                    g2d.drawImage(Suelo, x, y, celda, celda, this);


                // Pared encima si existe
                if (mapa[fila][columna] == 1) {
                    g2d.drawImage(Pared, x, y, celda, celda, this);
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
}
