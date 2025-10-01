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
    // 🔹 Modelo que permite edición en todas las columnas menos el id oculto
    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            // Todas las columnas editables excepto la última (id oculto)
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
    modelo.addColumn("ID"); // 🔹 clave primaria oculta

    tablaEstudiantes.setModel(modelo);

    String sql = "SELECT id, cc, nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion FROM Alumnos";

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
                rs.getInt("id") // 🔹 guardamos id para el WHERE
            };
            modelo.addRow(fila);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error cargando estudiantes: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    // 🔹 Ocultar la columna de id
    tablaEstudiantes.getColumnModel().removeColumn(tablaEstudiantes.getColumnModel().getColumn(9));

    // Listener para actualizar
    modelo.addTableModelListener(e -> {
        if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
            int fila = e.getFirstRow();

            if (fila >= 0) {
                String nuevaCc = modelo.getValueAt(fila, 0).toString();
                String nombre = modelo.getValueAt(fila, 1).toString();
                String segundoNombre = modelo.getValueAt(fila, 2).toString();
                String apellido = modelo.getValueAt(fila, 3).toString();
                String segundoApellido = modelo.getValueAt(fila, 4).toString();
                String edad = modelo.getValueAt(fila, 5).toString();
                String telefono = modelo.getValueAt(fila, 6).toString();
                String correo = modelo.getValueAt(fila, 7).toString();
                String direccion = modelo.getValueAt(fila, 8).toString();
                int id = Integer.parseInt(modelo.getValueAt(fila, 9).toString()); // 🔹 id PK

                String updateSQL = "UPDATE Alumnos SET cc=?, nombre=?, segundo_nombre=?, apellido=?, segundo_apellido=?, edad=?, telefono=?, correo=?, direccion=? WHERE id=?";

                try (Connection conn = baseDatos.getConnection();
                     PreparedStatement ps = conn.prepareStatement(updateSQL)) {

                    ps.setString(1, nuevaCc);
                    ps.setString(2, nombre);
                    ps.setString(3, segundoNombre);
                    ps.setString(4, apellido);
                    ps.setString(5, segundoApellido);
                    ps.setString(6, edad);
                    ps.setString(7, telefono);
                    ps.setString(8, correo);
                    ps.setString(9, direccion);
                    ps.setInt(10, id); // 🔹 el margen es id

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
