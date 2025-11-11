package Vista.frm.Panel;

import Clases.AjustesObjetos;
import Clases.Base_De_Datos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PanelReportesAlumno extends JPanel {
    
    // ✅ INSTANCIA ESTÁTICA - No requiere pasar Base_De_Datos por constructor
    private static final Base_De_Datos baseDatos = new Base_De_Datos();
    
    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;
    private final String usuarioActual;
    
    // ✅ CONSTRUCTOR SIN Base_De_Datos - Compatible con frmplataforma
    public PanelReportesAlumno(String usuarioActual) {
        this.usuarioActual = usuarioActual;
        initComponentes();  
        cargarReportes();
        AjustesObjetos.ajustarTabla(tablaReportes);
    }
    
    private void initComponentes() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 244, 248));
        
        // Título
        JLabel lblTitulo = new JLabel("Reportes Académicos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(192, 4, 29));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);
        
        // Tabla
        String[] columnas = {"Fecha", "Docente", "Materia(s)", "Reporte"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };
        
        tablaReportes = new JTable(modeloTabla);
        tablaReportes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaReportes.setRowHeight(25);
        tablaReportes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaReportes.getTableHeader().setBackground(new Color(192, 4, 29));
        tablaReportes.getTableHeader().setForeground(Color.BLACK);
        
        // Configurar ancho de columnas
        tablaReportes.getColumnModel().getColumn(0).setPreferredWidth(150);
        tablaReportes.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaReportes.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaReportes.getColumnModel().getColumn(3).setPreferredWidth(400);
        
        // ScrollPane
        JScrollPane scrollTabla = new JScrollPane(tablaReportes);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        add(scrollTabla, BorderLayout.CENTER);
        // AjustesObjetos.ajustarTabla(tablaReportes);
    }
    
    private void cargarReportes() {
        modeloTabla.setRowCount(0);
        
        try {
            // ✅ LLAMADA AL MÉTODO ESTÁTICO
            List<Object[]> reportes = baseDatos.obtenerReportesAlumnoCompletos(usuarioActual);
            
            // Llenar la tabla con los datos obtenidos
            for (Object[] fila : reportes) {
                modeloTabla.addRow(fila);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error al cargar reportes: " + e.getMessage(), 
                "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }




    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
