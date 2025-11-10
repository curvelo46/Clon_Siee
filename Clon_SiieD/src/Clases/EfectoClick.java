package Clases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EfectoClick {
    
    // Aplica efecto clickeable a un label con acción personalizada
    public static void aplicarLabel(JLabel label, Runnable accion) {
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(Color.BLUE);
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(Color.BLACK);
                label.setFont(label.getFont().deriveFont(Font.PLAIN));
            }
        });
    }
    
    // Aplica efecto clickeable a un panel con acción personalizada
    public static void aplicarPanel(JPanel panel, Runnable accion) {
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(panel.getBackground().darker());
                panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(panel.getBackground().brighter());
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            }
        });
    }
    
    // Versión sobrecargada para panel sin borde inicial
    public static void aplicarPanel(JPanel panel, Runnable accion, Color colorOriginal) {
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(colorOriginal.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(colorOriginal);
            }
        });
    }
}