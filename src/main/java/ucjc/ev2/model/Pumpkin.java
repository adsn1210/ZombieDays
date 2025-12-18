package ucjc.ev2.model;
import java.awt.*;
public class Pumpkin extends Objetos {
    private double vx; //Velocidad

    public Pumpkin(double x, double y,int w , int h ,Image img) {
        super(x,y,w,h,img);
        this.vx = 1.2;
    }

    @Override
    public void update(Laberinto laberinto) {
        if (!activo) {
            return;
        }
        double nextX = x + vx;
        Rectangle nextBox = new Rectangle((int)Math.round(nextX), (int)Math.round(y), w, h);// Si choca con pared, invierte dirección
        if (laberinto.esPared(nextBox)) {
            vx = -vx;
            return;
        }

        x = nextX;
    }
}