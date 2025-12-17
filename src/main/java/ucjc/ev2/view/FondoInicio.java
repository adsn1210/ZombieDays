package ucjc.ev2.view;

import javax.swing.*;
import java.awt.*;

public class FondoInicio extends JPanel {

    private final Image fondo;

    // Tamaño fijo de la pantalla de inicio
    private static final int ANCHO_PANTALLA = 800;
    private static final int ALTO_PANTALLA  = 690;

    public FondoInicio() {
        Image temp = null;

        try {
            temp = new ImageIcon(
                    getClass().getResource("/pantallaInicio/mazeInit.png")
            ).getImage();
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen de inicio");
        }

        fondo = temp;

        // Size
        setPreferredSize(new Dimension(ANCHO_PANTALLA, ALTO_PANTALLA));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (fondo != null) {
            // Escala la imagen al tamaño del panel
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
