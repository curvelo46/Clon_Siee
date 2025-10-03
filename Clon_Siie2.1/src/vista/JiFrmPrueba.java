/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package vista;

import clases.Base_De_Datos;
import clases.ConexionBD;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cymaniatico
 */
   public class JiFrmPrueba extends javax.swing.JInternalFrame {
    private final Base_De_Datos baseDatos;
    private final String profesor;
    private int corteSeleccionado; 
    private String tablaNotasMateria; 
    
    public JiFrmPrueba(Base_De_Datos basedatos, String profesor) {
        initComponents();
        this.baseDatos = basedatos;
        this.getContentPane().setBackground(new Color(255, 254, 214));
        this.profesor = profesor;
        this.corteSeleccionado = basedatos.corte();

        // Detectar qué tabla de notas usar según la materia del docente
        this.tablaNotasMateria = obtenerTablaNotasMateria();
        cargarTabla();
    }

    
    private String obtenerTablaNotasMateria() {
        String materia = null;
        String sql = "SELECT m.materia FROM Materias m "
                   + "INNER JOIN Docentes d ON m.docente_id = d.id "
                   + "WHERE d.nombre = ? LIMIT 1";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, profesor);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                materia = rs.getString("materia");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (materia == null || materia.equalsIgnoreCase("sin asignatura")) {
            JOptionPane.showMessageDialog(this, "❌ Materia no asignada");
            return null;
        }

        // Mapear nombre de materia → tabla de notas
        return materia;
    }

    /**
     * Carga lista de alumnos con sus notas (si existen) en la tabla de la materia
     */
    private void cargarTabla() {
        if (tablaNotasMateria == null) return; 

        jMenuBar1.add(Box.createHorizontalGlue());
        jMenuBar1.add(btnGuardar);

        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Estudiante", "Nota"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // solo la columna Nota editable
            }
        };

        String sql = "SELECT a.id, a.nombre, a.apellido, n.nota "
                   + "FROM Alumnos a "
                   + "LEFT JOIN " + tablaNotasMateria + " n "
                   + "ON a.id = n.alumno_id AND n.corte = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, corteSeleccionado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String estudiante = rs.getString("nombre") + " " + rs.getString("apellido");
                double nota = rs.getDouble("nota");
                if (rs.wasNull()) nota = 0.0; 
                modelo.addRow(new Object[]{estudiante, nota});
            }

            tablaNotas.setModel(modelo);
            lbMateria.setText("Materia: " + tablaNotasMateria + " | Corte: " + corteSeleccionado);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error al cargar alumnos/notas: " + e.getMessage());
        }
    }

    /**
     * Guarda las notas editadas en la tabla de la materia
     */
    private void guardarNotas() {
        if (tablaNotasMateria == null) return; // sin materia

        DefaultTableModel modelo = (DefaultTableModel) tablaNotas.getModel();

        try (Connection conn = ConexionBD.getConnection()) {
            String sqlUpdate = "INSERT INTO " + tablaNotasMateria + " (alumno_id, corte, nota) "
                             + "VALUES ((SELECT id FROM Alumnos WHERE CONCAT(nombre,' ',apellido) = ?), ?, ?) "
                             + "ON DUPLICATE KEY UPDATE nota = VALUES(nota)";

            PreparedStatement stmt = conn.prepareStatement(sqlUpdate);

            for (int i = 0; i < modelo.getRowCount(); i++) {
                String estudiante = modelo.getValueAt(i, 0).toString();
                double nota = Double.parseDouble(modelo.getValueAt(i, 1).toString());

                stmt.setString(1, estudiante);
                stmt.setInt(2, corteSeleccionado);
                stmt.setDouble(3, nota);
                stmt.addBatch();
            }

            stmt.executeBatch();
            JOptionPane.showMessageDialog(this, "✅ Notas guardadas en " + tablaNotasMateria);
            cargarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al guardar notas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
   


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaNotas = new javax.swing.JTable();
        btnGuardar = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        lbMateria = new javax.swing.JMenu();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);

        tablaNotas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaNotas);

        btnGuardar.setText("guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        lbMateria.setText("File");
        jMenuBar1.add(lbMateria);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnGuardar))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        guardarNotas();
        this.corteSeleccionado=baseDatos.cortenuevo();
        
    }//GEN-LAST:event_btnGuardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu lbMateria;
    private javax.swing.JTable tablaNotas;
    // End of variables declaration//GEN-END:variables
}
