package ucjc.ev2.view;

import javax.swing.*;
import java.awt.*;

public class PantallaGameOver extends JFrame {

    private static final String RUTA_ICONO = "/icono/iconoMaze.png";

    public PantallaGameOver(Runnable onRetry, Runnable onExit) {
        setTitle("Game Over");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        establecerIcono();

        // Fondo
        FondoImagen panel = new FondoImagen(
                "/PantallaGameOver/GameOver.png",
                800, 690
        );
        panel.setLayout(null); // para setBounds
        setContentPane(panel);

        JButton btnRetry = crearBotonImagen("/boton/retry.png", "/boton/retry.png");
        JButton btnExit  = crearBotonImagen("/boton/exit.png",  "/boton/exitHover.png");

        // Posiciones
        btnRetry.setBounds(245, 260, 300, 100);
        btnExit.setBounds(250, 300, 300, 100);

        btnRetry.addActionListener(e -> {
            dispose();
            onRetry.run();
        });

        btnExit.addActionListener(e -> {
            dispose();
            onExit.run();
        });

        panel.add(btnRetry);
        panel.add(btnExit);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton crearBotonImagen(String normal, String hover) {
        ImageIcon iconNormal = new ImageIcon(getClass().getResource(normal));
        ImageIcon iconHover  = new ImageIcon(getClass().getResource(hover));

        JButton b = new JButton(iconNormal);
        b.setRolloverIcon(iconHover);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void establecerIcono() {
        try {
            Image iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(RUTA_ICONO));
            setIconImage(iconImage);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono en GameOver");
        }
    }
}
