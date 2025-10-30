/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package vista.jifrm;

import clases.Base_De_Datos;
import clases.ConexionBD;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class JiFrmNotasCurso extends javax.swing.JInternalFrame {
    private final Base_De_Datos baseDatos;
    private final String profesor;
    private final String materia;
    private int corteSeleccionado;
    private int materiaId; 
    private int nota2;
    private int i=0;
    public JiFrmNotasCurso(Base_De_Datos basedatos, String profesor, String materia) {
        initComponents();
        this.baseDatos = basedatos;
        this.profesor = profesor;
        this.materia = materia;
        
        this.getContentPane().setBackground(new Color(255, 254, 214));
        DefaultTableModel modelo = (DefaultTableModel) tablaNotas.getModel();
        

        this.materiaId = obtenerIdMateria(); 

        if (this.materiaId != -1) {
            cargarTabla();
            this.corteSeleccionado = baseDatos.cortenuevo();
            
        } else {
            jScrollPane1.setVisible(false);
            btnGuardar.setVisible(false);
            lbMateria.setText("Materia: No asignada");
        }
    }

    private int obtenerIdMateria() {
        int id = -1;
        String sql = "call id_materia(?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, materia);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    private void cargarTabla() {



    jMenuBar1.add(Box.createHorizontalGlue());
    jMenuBar1.add(btnGuardar);

    // Creamos modelo con control de edición según corteX_edit
    DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Estudiante", "Corte 1", "Corte 2", "Corte 3", "Corte1_Edit", "Corte2_Edit", "Corte3_Edit"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            if (column >= 2 && column <= 4) { // Corte 1,2,3
                int editCol = column + 3; // columna respectiva de corteX_edit
                Object valorEdit = getValueAt(row, editCol);
                if (valorEdit != null && valorEdit.toString().equals("1")) {
                    return false; // No editable si corteX_edit es 1
                }    
            }
            return true; // resto de columnas no editable
        }

    };

    String sql = "CALL listar_notas_docente_materia(?, ?)";

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, profesor);
        stmt.setString(2, materia);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int idAlumno = rs.getInt("alumno_id");
            String estudiante = rs.getString("estudiante") + " " + rs.getString("apellido");
            double c1 = rs.getDouble("corte1");
            double c2 = rs.getDouble("corte2");
            double c3 = rs.getDouble("corte3");
            int c1Edit = rs.getInt("corte1_edit");
            int c2Edit = rs.getInt("corte2_edit");
            int c3Edit = rs.getInt("corte3_edit");

            modelo.addRow(new Object[]{idAlumno, estudiante, c1, c2, c3, c1Edit, c2Edit, c3Edit});
        }

        tablaNotas.setModel(modelo);

        // Ocultamos columnas edit
        tablaNotas.getColumnModel().getColumn(5).setMinWidth(0);
        tablaNotas.getColumnModel().getColumn(5).setMaxWidth(0);
        tablaNotas.getColumnModel().getColumn(5).setWidth(0);

        tablaNotas.getColumnModel().getColumn(6).setMinWidth(0);
        tablaNotas.getColumnModel().getColumn(6).setMaxWidth(0);
        tablaNotas.getColumnModel().getColumn(6).setWidth(0);

        tablaNotas.getColumnModel().getColumn(7).setMinWidth(0);
        tablaNotas.getColumnModel().getColumn(7).setMaxWidth(0);
        tablaNotas.getColumnModel().getColumn(7).setWidth(0);

        // Ocultamos columna ID
        tablaNotas.getColumnModel().getColumn(0).setMinWidth(0);
        tablaNotas.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaNotas.getColumnModel().getColumn(0).setWidth(0);

        lbMateria.setText("Materia: " + materia);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "❌ Error al cargar alumnos/notas: " + e.getMessage());
        e.printStackTrace();
    }
}


    private void guardarNotas() {
    if (materiaId == -1) return;

    // Obtener docente_materia_id
    int docenteMateriaId = -1;
    String sqlGetDmId = "CALL get_docente_materia_id(?, ?)";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement psDm = conn.prepareStatement(sqlGetDmId)) {
        psDm.setString(1, profesor);
        psDm.setString(2, materia);
        ResultSet rsDm = psDm.executeQuery();
        if (rsDm.next()) {
            docenteMateriaId = rsDm.getInt("id");
        } else {
            JOptionPane.showMessageDialog(this, "❌ No se encontró la materia asignada a este docente.");
            return;
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "❌ Error al obtener docente_materia_id: " + e.getMessage());
        e.printStackTrace();
        return;
    }



    String sql = "CALL actualizar_nota(?, ?, ?, ?)";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
                DefaultTableModel modelo = (DefaultTableModel) tablaNotas.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            int alumnoId = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            double nota;
            try {
                nota = Double.parseDouble(modelo.getValueAt(i, corteSeleccionado + 1).toString());
                
            } catch (NumberFormatException e) {
                nota = 0.0;
            }

            stmt.setInt(1, alumnoId);
            stmt.setInt(2, corteSeleccionado);
            stmt.setInt(3, docenteMateriaId);
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


    // Recargar la tabla para reflejar los cambios
    cargarTabla();
    }//GEN-LAST:event_btnGuardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu lbMateria;
    private javax.swing.JTable tablaNotas;
    // End of variables declaration//GEN-END:variables
}
