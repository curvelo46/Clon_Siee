/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package vista;

import clases.Base_De_Datos;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class JiFrmDatosEstudiandres extends javax.swing.JInternalFrame {
    private final Base_De_Datos baseDatos;
    private final String profesor;

    public JiFrmDatosEstudiandres(Base_De_Datos basedatos, String profesor) {
        initComponents();
        this.baseDatos = basedatos;
        this.getContentPane().setBackground(new Color(255, 254, 214));
        this.profesor = profesor;
        cargarEstudiantes();
    }
    
   private void cargarEstudiantes() {
        // Modelo editable pero sin permitir editar la columna de CC
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Evitamos que la CC sea editable
                return column != 0;
            }
        };

        // 🔹 Columnas visibles (usamos CC en vez de ID)
        modelo.addColumn("CC");
        modelo.addColumn("Nombre");
        modelo.addColumn("Segundo Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Segundo Apellido");
        modelo.addColumn("Edad");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Correo");
        modelo.addColumn("Dirección");
   

        tablaEstudiantes.setModel(modelo);

        String sql = "SELECT cc, nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion  FROM Alumnos";

        try (Connection conn = baseDatos.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] fila = {
                    rs.getString("cc"),
                    rs.getString("nombre"),
                    rs.getString("segundo_nombre"),
                    rs.getString("apellido"),
                    rs.getString("segundo_apellido"),
                    rs.getString("edad"),
                    rs.getString("telefono"),
                    rs.getString("correo"),
                    rs.getString("direccion"),
               
                };
                modelo.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando estudiantes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        // 🔹 Listener para actualizar cambios en la BD
        modelo.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int fila = e.getFirstRow();

                if (fila >= 0) {
                    String cc = modelo.getValueAt(fila, 0).toString();
                    String nombre = modelo.getValueAt(fila, 1).toString();
                    String segundoNombre = modelo.getValueAt(fila, 2).toString();
                    String apellido = modelo.getValueAt(fila, 3).toString();
                    String segundoApellido = modelo.getValueAt(fila, 4).toString();
                    String edad = modelo.getValueAt(fila, 5).toString();
                    String telefono = modelo.getValueAt(fila, 6).toString();
                    String correo = modelo.getValueAt(fila, 7).toString();
                    String direccion = modelo.getValueAt(fila, 8).toString();
     

                    String updateSQL = "UPDATE Alumnos SET nombre=?, segundo_nombre=?, apellido=?, segundo_apellido=?, edad=?, telefono=?, correo=?, direccion=? WHERE cc=?";

                    try (Connection conn = baseDatos.getConnection();
                         PreparedStatement ps = conn.prepareStatement(updateSQL)) {

                        ps.setString(1, nombre);
                        ps.setString(2, segundoNombre);
                        ps.setString(3, apellido);
                        ps.setString(4, segundoApellido);
                        ps.setString(5, edad);
                        ps.setString(6, telefono);
                        ps.setString(7, correo);
                        ps.setString(8, direccion);
        
                        ps.setString(9, cc);

                        ps.executeUpdate();

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error actualizando estudiante: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }



 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tablaEstudiantes = new javax.swing.JTable();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);

        tablaEstudiantes.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tablaEstudiantes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tablaEstudiantes;
    // End of variables declaration//GEN-END:variables
}
