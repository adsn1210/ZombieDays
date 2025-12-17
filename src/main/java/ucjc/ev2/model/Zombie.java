package ucjc.ev2.model;

import javax.swing.*;
import java.awt.*;

//Clase del Personaje (Zombie)
public class Zombie {
    //Clase base
    /*Se usa el tipo de dato Double, para tener un movimiento mas fluido*/
    private double posicionX;
    private double posicionY;
    //Velocidad en PixelesXSegundo
    private double velocidad = 1; //tambien double para darle suavidad

    private static final int TAMANO_SRITE = 32;
    private static final double FACTOR_HITBOX = 0.80;//La hitbox ocupa el 80% para que pueda girar el PJ

    private final int ANCHO_ZOMBIE;
    private final int ALTO_ZOMBIE;
    private Image zombie;

    //Constructor
    //overloading, se sustituye anchoxalto y se iguala a TAMANO_SPRITE ya que sus medidas seran 32x32px
    public Zombie(double posicionX, double posicionY, double velocidad, Image zombie) {
        this(posicionX, posicionY, velocidad, TAMANO_SRITE, TAMANO_SRITE, zombie);
    }


    //Constructor chaining
    public Zombie(double posicionX, double posicionY, double velocidad, int ANCHO_ZOMBIE, int ALTO_ZOMBIE, Image zombie) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.velocidad = velocidad;
        this.ANCHO_ZOMBIE = ANCHO_ZOMBIE;
        this.ALTO_ZOMBIE = ALTO_ZOMBIE;
        this.zombie = cargarImagen("/sprites/player/zombieSoldier.png"); //CAMBIARLO LUEGO PARA IMAGEN EN SPRITE
    }

    //GETTER Y SETTERS
    public int getANCHO_ZOMBIE() {
        return ANCHO_ZOMBIE;
    }

    public int getALTO_ZOMBIE() {
        return ALTO_ZOMBIE;
    }

    public double getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(double posicionX) {
        this.posicionX = posicionX;
    }

    public double getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(double posicionY) {
        this.posicionY = posicionY;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public Image getZombie() {
        return zombie;
    }

    public void setZombie(Image zombie) {
        this.zombie = zombie;
    }


    // ========METODOS======

    /*
     * Este metodo mueve al personaje
     * Se define como double para tener mas suavidad
     * en Sistemas de Juegos el eje Y positivo apunta hacia abajo
     * Se añade velocidad para decir cuantos píxeles se mueve en cada movimiento
     * Este metodo podria servir para momentos donde no hay colisiones a si no
     * habria sobrecarga de calculo por la hitbox
     */

    public void mover(double deltaX, double deltaY) {
        this.posicionX += deltaX * velocidad;
        this.posicionY += deltaY * velocidad;
    }

    //Mueve el personaje pero antes verifica posibles colisiones con los muros del Maze,Se usan posicion Tentativas.
    public void moverConColisiones(double deltaX, double deltaY, Laberinto laberinto) {

        double proximaX = posicionX + deltaX * velocidad;
        double proximaY = posicionY + deltaY * velocidad;

        Rectangle hitboxTentativa = getHitboxEn(proximaX, proximaY);

        if (laberinto == null || !laberinto.esPared(hitboxTentativa)) {
            this.posicionX = proximaX;
            this.posicionY = proximaY;
        }
    }
    public Rectangle getHitbox(){
        return getHitboxEn(posicionX,posicionY);
    }

    public Rectangle getHitboxEn(double x,double y){
        int anchoHitbox = (int) Math.round(ANCHO_ZOMBIE * FACTOR_HITBOX);
        int altoHitbox = (int) Math.round(ALTO_ZOMBIE * FACTOR_HITBOX);
        int offsetX = (int) Math.round((ANCHO_ZOMBIE - anchoHitbox) / 2.0);
        int offsetY = (int) Math.round((ALTO_ZOMBIE - altoHitbox) / 2.0);

        return new Rectangle(
                (int) Math.round(x) + offsetX,
                (int) Math.round(y) + offsetY,
                anchoHitbox,
                altoHitbox);
    }


    public void dibujar(Graphics2D g){
        if (zombie != null){
            g.drawImage(
                    zombie,
                    (int) Math.round(posicionX),
                    (int) Math.round(posicionY),
                    ANCHO_ZOMBIE,
                    ALTO_ZOMBIE,
                    null
            );
        } else {
            // DIBUJO TEMPORAL por si no hay imagen
            g.setColor(Color.GREEN);
            g.fillRect(
                    (int) Math.round(posicionX),
                    (int) Math.round(posicionY),
                    ANCHO_ZOMBIE,
                    ALTO_ZOMBIE
            );
        }
    }
    private Image cargarImagen(String ruta) {
        var url = getClass().getResource(ruta);
        if (url == null) {
            System.err.println("Imagen no encontrada: " + ruta);
            return null;
        }
        return new ImageIcon(url).getImage();
    }


}
