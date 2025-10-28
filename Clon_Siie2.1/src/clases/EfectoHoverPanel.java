package clases;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class EfectoHoverPanel {

    private Color colorHover;

    public EfectoHoverPanel(Color colorHover) {
        this.colorHover = colorHover;
    }

    public void aplicarEfecto(JPanel panel) {
        Color colorOriginal = panel.getBackground();

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(colorOriginal);
            }
        });
    }
}
