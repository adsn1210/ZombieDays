package ucjc.ev2.model;

import java.awt.*;

//Esta clase va dirigida a ser la Padre de Pumpkin(Obstaculo) y Pocima (Power-Ups)
//Sera una clase Abstacta que contendra los atributos y metodos que tienen en Comun estos 2 objetos
public abstract class Objetos {
    //Se usa protected para que las clases hijas puedan acceder a ellas, sin necesidad de usar gettes Y setters
    protected double x,y; //Pixeles
    protected int w,h; //size
    protected Image img;
    protected boolean activo=true; //Nadie puede modificar el valor (protected) pero pueden leerlo (public)

    //Solo las clases hijas pueden llamar a este constructor
    protected Objetos(double x, double y, int w,int h, Image img) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.img = img;

    }
    public boolean isActivo() {
        return activo;
    }
    public void desactivar(){
        activo=false;
    }

    //La clase Rectangle viene con un metodo inteligente como **.intersects** para saber cuando 2 Rectangles estan colisionando
    public Rectangle getHitbox(){
        //Como rectangle solo acepta enteros (int) ya que x,y son double para suavizar
    return new Rectangle((int)Math.round(x),(int)Math.round(y),w,h);
    }
    public void dibujar(Graphics2D g2d, Component observer) {
        if (!activo) return;
        if (img != null) {
            g2d.drawImage(img, (int)Math.round(x), (int)Math.round(y), w, h, observer);
        } else {
            g2d.setColor(Color.MAGENTA);//Es un colo muy llamativo
            g2d.fillRect((int)Math.round(x), (int)Math.round(y), w, h);
        }
    }

    // Para objetos que se mueven (pumpkin). Por defecto no hace nada.
    public void update(Laberinto laberinto) {}
}

