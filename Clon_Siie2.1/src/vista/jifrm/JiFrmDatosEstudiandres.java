/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package vista.jifrm;

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
    
      private String getValorCelda(DefaultTableModel modelo, int fila, int columna) {
        Object val = modelo.getValueAt(fila, columna);
        return (val == null) ? "" : val.toString().trim();
    }
    
    

    private void cargarEstudiantes() {
      DefaultTableModel modelo = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
          return column != 9;
      }
    };
      
      modelo.addColumn("CC");
        modelo.addColumn("Nombre");
        modelo.addColumn("Segundo Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Segundo Apellido");
        modelo.addColumn("Edad");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Correo");
        modelo.addColumn("Dirección");
        modelo.addColumn("ID");

        tablaEstudiantes.setModel(modelo);

        String sql = "CALL listar_alumnos()";

        try (Connection conn = baseDatos.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
                    rs.getInt("id")
                };
                modelo.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando estudiantes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Ocultar columna ID
        tablaEstudiantes.getColumnModel().getColumn(9).setMinWidth(0);
        tablaEstudiantes.getColumnModel().getColumn(9).setMaxWidth(0);
        tablaEstudiantes.getColumnModel().getColumn(9).setPreferredWidth(0);

        modelo.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int fila = e.getFirstRow();
                if (fila >= 0) {
                    try {
                        Object idObj = modelo.getValueAt(fila, 9);
                        if (idObj == null) return;

                        int id = Integer.parseInt(idObj.toString());

                        String nuevaCc = getValorCelda(modelo, fila, 0);
                        String nombre = getValorCelda(modelo, fila, 1);
                        String segundoNombre = getValorCelda(modelo, fila, 2);
                        String apellido = getValorCelda(modelo, fila, 3);
                        String segundoApellido = getValorCelda(modelo, fila, 4);

                        String edadStr = getValorCelda(modelo, fila, 5);
                        if (edadStr.isBlank()) {
                            JOptionPane.showMessageDialog(this, "La edad no puede estar vacía.", "Error", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        int edad = Integer.parseInt(edadStr);

                        String telefono = getValorCelda(modelo, fila, 6);
                        String correo = getValorCelda(modelo, fila, 7);
                        String direccion = getValorCelda(modelo, fila, 8);

                        String updateSQL = "CALL actualizar_estudiante(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                        try (Connection conn = baseDatos.getConnection();
                             PreparedStatement ps = conn.prepareStatement(updateSQL)) {

                            ps.setInt(1, id);
                            ps.setString(2, nuevaCc);
                            ps.setString(3, nombre);
                            ps.setString(4, segundoNombre);
                            ps.setString(5, apellido);
                            ps.setString(6, segundoApellido);
                            ps.setInt(7, edad);
                            ps.setString(8, telefono);
                            ps.setString(9, correo);
                            ps.setString(10, direccion);

                            ps.executeUpdate();
                            JOptionPane.showMessageDialog(this, "✅ Estudiante actualizado correctamente");

                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this, "Error actualizando estudiante: " + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Edad inválida. Debe ser un número.", "Error", JOptionPane.WARNING_MESSAGE);
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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
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
