package ucjc.ev2.view;

import javax.swing.*;
import java.awt.*;

public class Ventanaprincipal extends JFrame {
    private static final String RUTA_ICONO = "/icono/iconoMaze.png";
    public Ventanaprincipal(Panelgame panelgame) {
        super("ZOMBIE DAYS 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        establecerIcono();
        setLayout(new BorderLayout());
        add(panelgame, BorderLayout.CENTER);
        pack(); //setSize() pero actualizado
        setLocationRelativeTo(null);
        setResizable(false);
    }
    private void establecerIcono() {
        try {
            Image iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(RUTA_ICONO));
        this.setIconImage(iconImage);
        }catch (Exception e){
            System.err.println("NO SE A CARGADO EL ICONO");
        }
    }
}
