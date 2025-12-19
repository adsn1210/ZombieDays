package ucjc.ev2;

import ucjc.ev2.controller.Buclegame;
import ucjc.ev2.controller.Controladorteclado;
import ucjc.ev2.model.Laberinto;
import ucjc.ev2.model.Zombie;
import ucjc.ev2.view.Panelgame;
import ucjc.ev2.view.PantallaInicio;
import ucjc.ev2.view.Ventanaprincipal;


import javax.swing.*;
import java.awt.*;

public class ZombieDay {

    public static void main(String[] args) {

        /* La forma correcta de Ejecutar el main
               ***  public static void main(String[] args) {
               ***   SwingUtilities.invokeLater(() -> {
               ***   new PantallaInicio(ZombieDay::iniciarJuego);
            ***   });
         ***    }
*/
            new PantallaInicio(ZombieDay::iniciarJuego);
    }


    public static void iniciarJuego(){
        Laberinto laberinto = new Laberinto(15,21);
        int celda = laberinto.getTamanoCelda();
        Point entrada = laberinto.getEntradaEnPixeles();
        Zombie zombie = new Zombie(entrada.x, entrada.y,4.0,celda,celda,(Image) null);

        Panelgame panel = new Panelgame(laberinto, zombie);
        Controladorteclado control = new Controladorteclado();
        Buclegame bucle = new Buclegame(zombie,control,panel,laberinto);
        Image[] frames = new Image[] {
                new ImageIcon(ZombieDay.class.getResource("/sprites/player/male/Walk(1).png")).getImage(),
                new ImageIcon(ZombieDay.class.getResource("/sprites/player/male/Walk (2).png")).getImage(),
                new ImageIcon(ZombieDay.class.getResource("/sprites/player/male/Walk (3).png")).getImage(),
                new ImageIcon(ZombieDay.class.getResource("/sprites/player/male/Walk (4).png")).getImage(),
                new ImageIcon(ZombieDay.class.getResource("/sprites/player/male/Walk (5).png")).getImage()
        };

        zombie.setWalkFrames(frames);

        /*Con esta prueba comprobamos que el TIMER no deberia estar en el constructor del Bucle ya que si el juego
        tarda en iniciarse , el timer restara ese tiempo porque el SI! se ha iniciado, y en vez de 20 segundos seran -eltiempo que pasamos esperando -_-
         */
        /**System.out.println("Esperando 10 segundos antes de arrancar...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }**/
        Ventanaprincipal ventana = new Ventanaprincipal(panel);
        ventana.setVisible(true);

        // Captura de teclas tanto en la ventana como en el panel para asegurar el enfoque en WASD/cursores
        ventana.addKeyListener(control);
        panel.addKeyListener(control);
        panel.requestFocusInWindow();
        SwingUtilities.invokeLater(panel::requestFocusInWindow);

        bucle.iniciar();
    }
}
