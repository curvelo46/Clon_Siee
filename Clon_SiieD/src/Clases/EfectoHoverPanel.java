package Clases;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class EfectoHoverPanel {

    private Color colorHoverFondo;
    private Color colorHoverTexto;

    public EfectoHoverPanel(Color colorHoverFondo, Color colorHoverTexto) {
        this.colorHoverFondo = colorHoverFondo;
        this.colorHoverTexto = colorHoverTexto;
    }

    public void aplicarEfecto(JPanel panel) {
        final Color colorOriginalFondo = panel.getBackground();
        final JLabel[] labelsOriginales = new JLabel[panel.getComponentCount()];

        // Guardar colores originales de los labels
        for (int i = 0; i < panel.getComponentCount(); i++) {
            if (panel.getComponent(i) instanceof JLabel) {
                labelsOriginales[i] = (JLabel) panel.getComponent(i);
            }
        }

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(colorHoverFondo);
                for (JLabel label : labelsOriginales) {
                    if (label != null) {
                        label.setForeground(colorHoverTexto);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(colorOriginalFondo);
                for (JLabel label : labelsOriginales) {
                    if (label != null) {
                        label.setForeground(Color.WHITE); // o el color original que uses
                    }
                }
            }
        });
    }
}