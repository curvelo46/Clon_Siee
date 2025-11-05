/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vista.frm.Panel;
import Clases.ConexionBD;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PanelGestionUsuario extends JPanel {
    private JTextField txtCedula, txtNombre, txtSegundoNombre, txtApellido, txtSegundoApellido;
    private JTextField txtEdad, txtTelefono, txtCorreo, txtDireccion;
    private JButton btnBuscar, btnGuardar;
    private JLabel lblCargo;
    private int usuarioId;
    private String cargo;

    public PanelGestionUsuario() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Cédula
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Cédula:"), gbc);
        txtCedula = new JTextField(15);
        gbc.gridx = 1;
        add(txtCedula, gbc);
        btnBuscar = new JButton("Buscar");
        gbc.gridx = 2;
        add(btnBuscar, gbc);

        // Fila 1: Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(20);
        gbc.gridx = 1;
        add(txtNombre, gbc);

        // Fila 2: Segundo Nombre
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Segundo Nombre:"), gbc);
        txtSegundoNombre = new JTextField(20);
        gbc.gridx = 1;
        add(txtSegundoNombre, gbc);

        // Fila 3: Apellido
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Apellido:"), gbc);
        txtApellido = new JTextField(20);
        gbc.gridx = 1;
        add(txtApellido, gbc);

        // Fila 4: Segundo Apellido
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Segundo Apellido:"), gbc);
        txtSegundoApellido = new JTextField(20);
        gbc.gridx = 1;
        add(txtSegundoApellido, gbc);

        // Fila 5: Edad
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Edad:"), gbc);
        txtEdad = new JTextField(5);
        gbc.gridx = 1;
        add(txtEdad, gbc);

        // Fila 6: Teléfono
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Teléfono:"), gbc);
        txtTelefono = new JTextField(20);
        gbc.gridx = 1;
        add(txtTelefono, gbc);

        // Fila 7: Correo
        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel("Correo:"), gbc);
        txtCorreo = new JTextField(20);
        gbc.gridx = 1;
        add(txtCorreo, gbc);

        // Fila 8: Dirección
        gbc.gridx = 0; gbc.gridy = 8;
        add(new JLabel("Dirección:"), gbc);
        txtDireccion = new JTextField(20);
        gbc.gridx = 1;
        add(txtDireccion, gbc);

        // Fila 9: Cargo (solo lectura)
        gbc.gridx = 0; gbc.gridy = 9;
        add(new JLabel("Cargo:"), gbc);
        lblCargo = new JLabel("-");
        gbc.gridx = 1;
        add(lblCargo, gbc);

        // Fila 10: Botón Guardar
        btnGuardar = new JButton("Guardar Cambios");
        gbc.gridx = 1; gbc.gridy = 10;
        add(btnGuardar, gbc);

        // Eventos
        btnBuscar.addActionListener(e -> buscarUsuario());
        btnGuardar.addActionListener(e -> guardarCambios());
    }

    private void buscarUsuario() {
        String cedula = txtCedula.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula.");
            return;
        }

        String sql = "SELECT id, nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cargo " +
                     "FROM Usuarios WHERE cc = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuarioId = rs.getInt("id");
                cargo = rs.getString("cargo");
                txtNombre.setText(rs.getString("nombre"));
                txtSegundoNombre.setText(rs.getString("segundo_nombre"));
                txtApellido.setText(rs.getString("apellido"));
                txtSegundoApellido.setText(rs.getString("segundo_apellido"));
                txtEdad.setText(String.valueOf(rs.getInt("edad")));
                txtTelefono.setText(rs.getString("telefono"));
                txtCorreo.setText(rs.getString("correo"));
                txtDireccion.setText(rs.getString("direccion"));
                lblCargo.setText(cargo);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar usuario.");
        }
    }

    private void guardarCambios() {
    if (usuarioId == 0) {
        JOptionPane.showMessageDialog(this, "Primero busque un usuario.");
        return;
    }

    String procedimiento = null;
    if ("alumno".equals(cargo)) {
        procedimiento = "{call actualizar_estudiante(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    } else if ("docente".equals(cargo)) {
        procedimiento = "{call actualizar_docente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    } else if ("administrador".equals(cargo)) {
        procedimiento = "{call actualizar_administrador(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    } else if ("registro y control".equals(cargo)) {
        procedimiento = "{call actualizar_registro_control(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    } else {
        JOptionPane.showMessageDialog(this, "No se puede editar este tipo de usuario.");
        return;
    }

    try (Connection conn = ConexionBD.getConnection();
         CallableStatement cs = conn.prepareCall(procedimiento)) {

        cs.setInt(1, usuarioId);
        cs.setString(2, txtCedula.getText().trim());
        cs.setString(3, txtNombre.getText().trim());
        cs.setString(4, txtSegundoNombre.getText().trim());
        cs.setString(5, txtApellido.getText().trim());
        cs.setString(6, txtSegundoApellido.getText().trim());
        cs.setInt(7, Integer.parseInt(txtEdad.getText().trim()));
        cs.setString(8, txtTelefono.getText().trim());
        cs.setString(9, txtCorreo.getText().trim());
        cs.setString(10, txtDireccion.getText().trim());

        cs.executeUpdate();
        JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.");

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al actualizar usuario.");
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
