package ucjc.ev2.view;

import javax.swing.*;
import java.awt.*;

public class FondoImagen extends JPanel {

    private final Image fondo;

    private final int ancho;
    private final int alto;

    public FondoImagen(String rutaImagen, int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;

        Image temp = null;
        try {
            temp = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen: " + rutaImagen);
        }
        fondo = temp;

        setPreferredSize(new Dimension(ancho, alto));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
