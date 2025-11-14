package Clases;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public abstract class AjustesObjetos {

    public static void ajustarImagen(JLabel label, String ruta) {
        ImageIcon iconoOriginal = new ImageIcon(ruta);
        Image imagenOriginal = iconoOriginal.getImage();

        int labelWidth = label.getWidth();
        int labelHeight = label.getHeight();

        if (labelWidth == 0 || labelHeight == 0) {
            labelWidth = 100;
            labelHeight = 100;
        }

        double ratioOriginal = (double) imagenOriginal.getWidth(null) / imagenOriginal.getHeight(null);
        double ratioLabel = (double) labelWidth / labelHeight;

        int nuevoAncho;
        int nuevoAlto;

        if (ratioOriginal > ratioLabel) {
            nuevoAncho = labelWidth;
            nuevoAlto = (int) (labelWidth / ratioOriginal);
            
        } else {
            nuevoAlto = labelHeight;
            nuevoAncho = (int) (labelHeight * ratioOriginal);
            
        }

        Image imagenEscalada = imagenOriginal.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
        ImageIcon iconoFinal = new ImageIcon(imagenEscalada);

        label.setIcon(iconoFinal);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setText(null); 
        label.repaint();
    }

    public static void ajustarTabla(JTable table) {
        
        if (table.getRowCount() == 0) {
            TableColumnModel columnModel = table.getColumnModel();
            for (int column = 0; column < table.getColumnCount(); column++) {
                TableColumn tableColumn = columnModel.getColumn(column);
                int headerWidth = table.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(null, tableColumn.getHeaderValue(), 
                        false, false, 0, 0).getPreferredSize().width;
                tableColumn.setPreferredWidth(headerWidth + 10);
            }
            return;
        }
        
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

                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }
            tableColumn.setPreferredWidth(preferredWidth);
        }
    }
    
}