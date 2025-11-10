/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author cymaniatico
 */
public abstract class AjustesObjetos {

    
    
    public static void ajustarImagen(JLabel label, String ruta) {
    ImageIcon iconoOriginal = new ImageIcon(ruta);
    Image imagenOriginal = iconoOriginal.getImage();

    int labelWidth = label.getWidth();
    int labelHeight = label.getHeight();

    if (labelWidth == 0 || labelHeight == 0) {
        // Si el label aún no tiene tamaño (por ejemplo, si no se ha mostrado todavía)
        labelWidth = 100;
        labelHeight = 100;
    }

    // Calcula la relación de aspecto original
    double ratioOriginal = (double) imagenOriginal.getWidth(null) / imagenOriginal.getHeight(null);
    double ratioLabel = (double) labelWidth / labelHeight;

    int nuevoAncho;
    int nuevoAlto;

    if (ratioOriginal > ratioLabel) {
        // Imagen más ancha que el label: limitar por ancho
        nuevoAncho = labelWidth;
        nuevoAlto = (int) (labelWidth / ratioOriginal);
    } else {
        // Imagen más alta que el label: limitar por alto
        nuevoAlto = labelHeight;
        nuevoAncho = (int) (labelHeight * ratioOriginal);
    }

    Image imagenEscalada = imagenOriginal.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
    ImageIcon iconoFinal = new ImageIcon(imagenEscalada);

    // Centrar dentro del label
    label.setIcon(iconoFinal);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setVerticalAlignment(JLabel.CENTER);
    label.setText(null); // quita texto si lo tiene
    label.repaint();
}

    public static void ajustarTabla(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = columnModel.getColumn(column);
            int preferredWidth = Math.max(tableColumn.getMinWidth(), table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(null, tableColumn.getHeaderValue(), false, false, 0, 0).getPreferredSize().width);
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                // No permitir que el ancho exceda el máximo
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }
            tableColumn.setPreferredWidth(preferredWidth);
        }
    }
    
}