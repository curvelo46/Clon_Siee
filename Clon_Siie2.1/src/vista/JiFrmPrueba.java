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
    private final String materia;

    public JiFrmPrueba(Base_De_Datos basedatos, String profesor, String materia) {
        initComponents();
        this.baseDatos = basedatos;
        this.profesor = profesor;
        this.materia = materia;
        this.getContentPane().setBackground(new Color(255, 254, 214));
        this.corteSeleccionado = basedatos.corte();

        cargarTabla();

            this.tablaNotasMateria = obtenerTablaNotasMateria();

            if (this.tablaNotasMateria != null) {
                cargarTabla();
            } else {
                jScrollPane1.setVisible(false);
                btnGuardar.setVisible(false);
                lbMateria.setText("Materia: No asignada");
            }
    }

    
    private void cargarTabla() {
        if (tablaNotasMateria == null) return;

        txtCorte.setText("Corte actual: " + corteSeleccionado);

        jMenuBar1.add(Box.createHorizontalGlue());
        jMenuBar1.add(btnGuardar);

        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Estudiante", "Corte 1", "Corte 2", "Corte 3"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == corteSeleccionado;
            }
        };

        String sql = "CALL listar_notas_docente_materia(?, ?)"; // debes tener este SP en MySQL
try (Connection conn = ConexionBD.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql)) {

    stmt.setString(1, profesor);
    stmt.setString(2, materia);
    ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String estudiante = rs.getString("estudiante");
                double c1 = rs.getDouble("corte1");
                double c2 = rs.getDouble("corte2");
                double c3 = rs.getDouble("corte3");
                modelo.addRow(new Object[]{estudiante, c1, c2, c3});
            }

            tablaNotas.setModel(modelo);
            lbMateria.setText("Materia: " + tablaNotasMateria);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al cargar alumnos/notas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la materia asignada al docente.
     * Si no tiene, muestra alerta y retorna null.
     */
 private String obtenerTablaNotasMateria() {
        String materia = null;
        String sql = "CALL obtener_materia_docente(?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, profesor);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                materia = rs.getString("nombre_materia");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (materia == null || materia.equalsIgnoreCase("sin asignatura")) {
            JOptionPane.showMessageDialog(this, "❌ El docente no tiene materia asignada.");
            return null;
        }

        return materia;
    }

    // 🔹 Guardar notas usando procedimiento almacenado
    private void guardarNotas() {
        if (tablaNotasMateria == null) return;

        DefaultTableModel modelo = (DefaultTableModel) tablaNotas.getModel();

        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "CALL actualizar_nota_alumno(?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < modelo.getRowCount(); i++) {
                int colIndex = corteSeleccionado;
                double nota;
                try {
                    nota = Double.parseDouble(modelo.getValueAt(i, colIndex).toString());
                } catch (NumberFormatException e) {
                    nota = 0.0;
                }

                String estudiante = modelo.getValueAt(i, 0).toString();

                stmt.setString(1, profesor);
                stmt.setString(2, estudiante);
                stmt.setInt(3, corteSeleccionado);
                stmt.setDouble(4, nota);
                stmt.addBatch();
            }

            stmt.executeBatch();

            JOptionPane.showMessageDialog(this, "✅ Notas guardadas correctamente (Corte " + corteSeleccionado + ")");
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

        btnGuardar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaNotas = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        lbMateria = new javax.swing.JMenu();
        txtCorte = new javax.swing.JMenu();

        btnGuardar.setText("guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

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

        lbMateria.setText("File");
        jMenuBar1.add(lbMateria);

        txtCorte.setText("jMenu1");
        jMenuBar1.add(txtCorte);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
       guardarNotas();
    this.corteSeleccionado = baseDatos.cortenuevo();

    // ✅ Actualizamos inmediatamente el menú del corte
    txtCorte.setText("Corte actual: " + corteSeleccionado);

    // Recargar la tabla para reflejar los cambios
    cargarTabla();
    }//GEN-LAST:event_btnGuardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu lbMateria;
    private javax.swing.JTable tablaNotas;
    private javax.swing.JMenu txtCorte;
    // End of variables declaration//GEN-END:variables
}
