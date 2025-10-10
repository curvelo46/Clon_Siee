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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cymaniatico
 */
public class JiFrmDatosProfesores extends javax.swing.JInternalFrame {
    private final Base_De_Datos baseDatos;
    private final String profesor;
    /**
     * Creates new form JiFrmPrueba
     */

 public JiFrmDatosProfesores(Base_De_Datos basedatos, String profesor) {
        initComponents();
        this.baseDatos = basedatos;
        this.profesor = profesor;
        this.getContentPane().setBackground(new Color(255, 254, 214));
        cargarDocentes();
    }
    
    private void cargarDocentes() {
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{ 
                "CC", "Nombre", "Segundo Nombre", "Apellido", "Segundo Apellido", 
                "Edad", "Teléfono", "Correo", "Dirección", "Materia" 
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // No se permite editar CC ni Materia
                return column != 0 && column != 9;
            }
        };

        // 🔹 Ahora usamos el procedimiento almacenado
        String sql = "CALL listar_docentes()";

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
                    rs.getString("materia")
                };
                modelo.addRow(fila);
            }

            tablaDocentes.setModel(modelo);

            // 🔥 Detectar cambios en las celdas y guardar con el SP
            modelo.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int fila = e.getFirstRow();

                        String cc = (String) modelo.getValueAt(fila, 0);
                        String nombre = (String) modelo.getValueAt(fila, 1);
                        String segundoNombre = (String) modelo.getValueAt(fila, 2);
                        String apellido = (String) modelo.getValueAt(fila, 3);
                        String segundoApellido = (String) modelo.getValueAt(fila, 4);
                        String edad = (String) modelo.getValueAt(fila, 5);
                        String telefono = (String) modelo.getValueAt(fila, 6);
                        String correo = (String) modelo.getValueAt(fila, 7);
                        String direccion = (String) modelo.getValueAt(fila, 8);

                        actualizarDocente(cc, nombre, segundoNombre, apellido, segundoApellido, edad, telefono, correo, direccion);
                    }
                }
            });

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error cargando docentes: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


  private void actualizarDocente(String cc, String nombre, String segundoNombre, String apellido,
                                   String segundoApellido, String edad, String telefono, String correo, String direccion) {

        String sql = "CALL actualizar_docente(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = baseDatos.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(cc));
            ps.setString(2, nombre);
            ps.setString(3, segundoNombre);
            ps.setString(4, apellido);
            ps.setString(5, segundoApellido);
            ps.setInt(6, Integer.parseInt(edad));
            ps.setString(7, telefono);
            ps.setString(8, correo);
            ps.setString(9, direccion);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, 
                "✅ Datos del docente " + nombre +" "+ apellido + " actualizados correctamente.");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error actualizando docente: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error en el formato numérico de CC o edad.",
                "Error", JOptionPane.ERROR_MESSAGE);
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

        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDocentes = new javax.swing.JTable();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);

        tablaDocentes.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tablaDocentes);

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
    private javax.swing.JTable tablaDocentes;
    // End of variables declaration//GEN-END:variables
}
