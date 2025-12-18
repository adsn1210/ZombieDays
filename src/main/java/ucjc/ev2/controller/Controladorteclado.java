package ucjc.ev2.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

//Captura las pulsaciones del teclado del jugador

public class Controladorteclado implements KeyListener {

    /*estructuras de datos avanzadas (HashSet y ArrayDeque) para manejar pulsaciones simultáneas
    y determinar la dirección de movimiento prioritaria.*/
    private final Set<Integer> teclasActivas = new HashSet<>();
    /*
     * Almacena las teclas que estan siendo pulsadas actualmente,El <Set> asegura que no haya duplicados.
     * Se usa por ejemplo para almacenar "w" + "Shift"
     * */
    private final Deque<Integer> historialDirecciones = new ArrayDeque<>();
    /*
     *Almacena el historial reciente de las teclas de direccion pulsadas,
     Permite mantener un orden, resuelva la problematica de la diagonal/ultima pulsada.
     */

    @Override
    /*
    * Añade siempre la tecla al Set de activas.
        Si es una tecla de movimiento (WASD o flechas):
        la quita del historial si ya estaba (evita duplicados),
        la mete al final → pasa a ser la más reciente.
    * */

    public void keyPressed(KeyEvent e) {
        teclasActivas.add(e.getKeyCode());
        if (esDireccion(e.getKeyCode())) {
            historialDirecciones.remove(e.getKeyCode());
            historialDirecciones.addLast(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        teclasActivas.remove(e.getKeyCode());
        historialDirecciones.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //No se usa
    }


    public boolean arriba() {
        return teclasActivas.contains(KeyEvent.VK_W) || teclasActivas.contains(KeyEvent.VK_UP);
    }

    public boolean abajo() {
        return teclasActivas.contains(KeyEvent.VK_S) || teclasActivas.contains(KeyEvent.VK_DOWN);
    }

    public boolean izquieda() {
        return teclasActivas.contains(KeyEvent.VK_A) || teclasActivas.contains(KeyEvent.VK_LEFT);
    }

    public boolean derecha() {
        return teclasActivas.contains(KeyEvent.VK_D) || teclasActivas.contains(KeyEvent.VK_RIGHT);
    }

    //Esto debera cambiar en el futuro ya que la velocidad varia por las PUMKIN Y POCION, pero se usara para confirmar el cambio de velocidad
    public double multiplicadorVelocidad() {
        if (teclasActivas.contains(KeyEvent.VK_SHIFT)) {
            return 2.50;
        }
        if (teclasActivas.contains(KeyEvent.VK_CONTROL)) {
            return 0.6;
        }
        return 1.0;
    }

    public Direccion direccionActual() {
        Integer tecla = historialDirecciones.peekLast();
        if (tecla == null) {
            return Direccion.NINGUNA;
        }

        return switch (tecla) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> Direccion.ARRIBA;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> Direccion.ABAJO;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> Direccion.IZQUIERDA;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> Direccion.DERECHA;
            default -> Direccion.NINGUNA;
        };
    }

    private boolean esDireccion(int keyCode) {
        return keyCode == KeyEvent.VK_W
                || keyCode == KeyEvent.VK_A
                || keyCode == KeyEvent.VK_S
                || keyCode == KeyEvent.VK_D
                || keyCode == KeyEvent.VK_UP
                || keyCode == KeyEvent.VK_LEFT
                || keyCode == KeyEvent.VK_DOWN
                || keyCode == KeyEvent.VK_RIGHT;
    }

    public enum Direccion {
        ARRIBA, ABAJO, IZQUIERDA, DERECHA, NINGUNA;
    }

}

