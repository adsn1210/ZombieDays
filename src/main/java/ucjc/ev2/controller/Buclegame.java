package ucjc.ev2.controller;

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

    public Buclegame(Zombie zombie, Controladorteclado controladorteclado, Panelgame panel, Laberinto laberinto) {
        this.zombie = zombie;
        this.controladorteclado = controladorteclado;
        this.panel = panel;
        this.laberinto = laberinto;
        this.velocidadBase = zombie.getVelocidad();
        int delay = 1000 / FPS;
        this.timer = new Timer(delay, e -> actualizar());
    }

    private void actualizar() {
        zombie.setVelocidad(velocidadBase * controladorteclado.multiplicadorVelocidad());

        Controladorteclado.Direccion direccion = controladorteclado.direccionActual();
        double deltaX = 0;
        double deltaY = 0;

        switch (direccion) {
            case ARRIBA -> deltaY = -1;
            case ABAJO -> deltaY = 1;//Positivo porque en los juegos el EJE Y para abajo es POSITIVOO
            case DERECHA -> deltaX = 1;
            case IZQUIERDA -> deltaX = -1;
            default -> {
            }
        }
        if (direccion != Controladorteclado.Direccion.NINGUNA) {
            zombie.moverConColisiones(deltaX, deltaY, laberinto);
            panel.repaint();
        }
    }

    public void iniciar() {
        timer.start();
    }


}
