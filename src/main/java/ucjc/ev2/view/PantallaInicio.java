package ucjc.ev2.view;

import javax.swing.*;
import java.awt.*;
import ucjc.ev2.audio.SoundPlayer;

public class PantallaInicio extends JFrame {

    private static final String RUTA_ICONO = "/icono/iconoMaze.png";

    public PantallaInicio(Runnable onStartGame) {
        setTitle("Zombie Maze");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        SoundPlayer.reproducir("/sounds/horror.wav");
        establecerIcono();

        FondoInicio panel = new FondoInicio();

        //Layout absoluto
        panel.setLayout(null);

        // =========================
        // BOTÓN INICIAR (normal + hover)
        // =========================
        ImageIcon iconoNormal = new ImageIcon(getClass().getResource("/boton/botonIniciar.png"));
        ImageIcon iconoHover  = new ImageIcon(getClass().getResource("/boton/botonHover.png"));

        JButton botonIniciar = new JButton(iconoNormal);

        // Hover reemplaza el icono
        botonIniciar.setRolloverEnabled(true);
        botonIniciar.setRolloverIcon(iconoHover);

        // Tamaño fijo
        int wBtn = 230;
        int hBtn = 64;

        // Posición
        int xBtnIniciar = 300;
        int yBtnIniciar = 315;

        botonIniciar.setBounds(xBtnIniciar, yBtnIniciar, wBtn, hBtn);

        // Estilo
        configurarBoton(botonIniciar);

        botonIniciar.addActionListener(e -> {
            dispose();
            onStartGame.run();
        });

        // =========================
        // BOTÓN ACERCA DE
        // =========================
        ImageIcon iconoAcerca = new ImageIcon(getClass().getResource("/boton/botonAcerca.png"));
        JButton botonAcerca = new JButton(iconoAcerca);

        int xBtnAcerca = 300;
        int yBtnAcerca = 360;

        botonAcerca.setBounds(xBtnAcerca, yBtnAcerca, wBtn, hBtn);
        configurarBoton(botonAcerca);

        botonAcerca.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "PROYECTO DESARROLLADO POR ADRIAN SALAZAR",
                    "ACERCA DE",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        // Añadir botones al panel
        panel.add(botonIniciar);
        panel.add(botonAcerca);

        add(panel);

        pack();                 // usa el preferredSize del FondoInicio
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void configurarBoton(JButton boton) {
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Para que el rollover funcione bien
        boton.setRolloverEnabled(true);
    }

    private void establecerIcono() {
        try {
            Image iconImage = Toolkit.getDefaultToolkit()
                    .getImage(getClass().getResource(RUTA_ICONO));
            setIconImage(iconImage);
        } catch (Exception e) {
            System.err.println("NO SE HA CARGADO EL ICONO");
            e.printStackTrace();
        }
    }
}
