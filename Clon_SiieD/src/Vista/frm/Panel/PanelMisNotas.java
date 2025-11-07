package Vista.frm.Panel;

import Clases.Base_De_Datos;
import Clases.ConexionBD;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelMisNotas extends JPanel {
    private JComboBox<String> comboCarreras;
    private JTable tablaNotas;
    private DefaultTableModel modeloTabla;
    private String usuarioActual;
    private Map<String, Integer> carreraMap;
    private Base_De_Datos baseDatos;
    
    public PanelMisNotas(String usuarioActual, Base_De_Datos baseDatos) {
        this.usuarioActual = usuarioActual;
        this.baseDatos = baseDatos;
        this.carreraMap = new HashMap<>();
        
        initComponentes();
        cargarCarreras();
    }
    
    private void initComponentes() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 244, 248));
        
        // Panel Superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(240, 244, 248));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));
        
        JLabel lblTitulo = new JLabel("Mis Notas Académicas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(192, 4, 29));
        
        panelSuperior.add(lblTitulo);
        panelSuperior.add(Box.createHorizontalGlue());
        
        JLabel lblCarrera = new JLabel("Carrera:");
        lblCarrera.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelSuperior.add(lblCarrera);
        panelSuperior.add(Box.createRigidArea(new Dimension(10, 0)));
        
        comboCarreras = new JComboBox<>();
        comboCarreras.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboCarreras.setPreferredSize(new Dimension(300, 30));
        comboCarreras.setMaximumSize(new Dimension(300, 30));
        comboCarreras.addActionListener(e -> cargarNotas());
        panelSuperior.add(comboCarreras);
        
        // Tabla
        String[] columnas = {"Materia", "Corte 1", "Corte 2", "Corte 3", "Promedio Final"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaNotas = new JTable(modeloTabla);
        tablaNotas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaNotas.setRowHeight(25);
        tablaNotas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaNotas.getTableHeader().setBackground(new Color(192, 4, 29));
        tablaNotas.getTableHeader().setForeground(Color.WHITE);
        tablaNotas.setGridColor(new Color(221, 221, 221));
        
        JScrollPane scrollTabla = new JScrollPane(tablaNotas);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
    }
    
    private void cargarCarreras() {
        comboCarreras.removeAllItems();
        carreraMap.clear();
        
        String sql = "{CALL obtener_carreras_del_alumno(?)}";
        
        try (Connection conn = ConexionBD.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            
            cs.setString(1, usuarioActual);
            ResultSet rs = cs.executeQuery();
            
            while (rs.next()) {
                int carreraId = rs.getInt("id");
                String carreraNombre = rs.getString("nombre");
                carreraMap.put(carreraNombre, carreraId);
                comboCarreras.addItem(carreraNombre);
            }
            
            if (comboCarreras.getItemCount() == 0) {
                comboCarreras.addItem("No tiene carreras matriculadas");
                comboCarreras.setEnabled(false);
                cargarNotas(); // Limpiar tabla
            } else {
                comboCarreras.setEnabled(true);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error al cargar carreras: " + e.getMessage(), 
                "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarNotas() {
        modeloTabla.setRowCount(0);
        
        if (!comboCarreras.isEnabled() || comboCarreras.getItemCount() == 0) {
            return;
        }
        
        String carreraSeleccionada = (String) comboCarreras.getSelectedItem();
        if (carreraSeleccionada == null || carreraSeleccionada.isEmpty()) {
            return;
        }
        
        Integer carreraId = carreraMap.get(carreraSeleccionada);
        if (carreraId == null) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error: No se encontró el ID de la carrera", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int alumnoId = obtenerIdAlumno();
        if (alumnoId == -1) {
            JOptionPane.showMessageDialog(this, 
                "❌ No se pudo identificar al alumno", 
                "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sql = "{CALL listar_notas_por_alumno_carrera(?, ?)}";
        
        try (Connection conn = ConexionBD.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            
            cs.setInt(1, alumnoId);
            cs.setInt(2, carreraId);
            ResultSet rs = cs.executeQuery();
            
            boolean tieneDatos = false;
            while (rs.next()) {
                tieneDatos = true;
                String materia = rs.getString("nombre_materia");
                double corte1 = rs.getDouble("corte1");
                double corte2 = rs.getDouble("corte2");
                double corte3 = rs.getDouble("corte3");
                double promedio = (corte1 + corte2 + corte3) / 3.0;
                
                Object[] fila = {
                    materia,
                    String.format("%.2f", corte1),
                    String.format("%.2f", corte2),
                    String.format("%.2f", corte3),
                    String.format("%.2f", promedio)
                };
                modeloTabla.addRow(fila);
            }
            
            if (!tieneDatos) {
                modeloTabla.addRow(new Object[]{"No hay registros de notas", "-", "-", "-", "-"});
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error al cargar notas: " + e.getMessage(), 
                "Error de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int obtenerIdAlumno() {
        String sql = "{CALL obtener_id_alumno_por_user(?)}";
        
        try (Connection conn = ConexionBD.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            
            cs.setString(1, usuarioActual);
            ResultSet rs = cs.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1;
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
