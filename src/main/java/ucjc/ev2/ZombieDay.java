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


    private static void iniciarJuego(){
        Laberinto laberinto = new Laberinto(15,21);
        int celda = laberinto.getTamanoCelda();
        Point entrada = laberinto.getEntradaEnPixeles();
        Zombie zombie = new Zombie(entrada.x, entrada.y,4.0,celda,celda,(Image) null);

        Panelgame panel = new Panelgame(laberinto, zombie);
        Controladorteclado control = new Controladorteclado();
        Buclegame bucle = new Buclegame(zombie,control,panel,laberinto);

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
