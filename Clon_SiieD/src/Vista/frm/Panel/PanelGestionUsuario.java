package Vista.frm.Panel;

import Clases.Base_De_Datos;
import javax.swing.*;
import java.awt.*;

public class PanelGestionUsuario extends JPanel {
    // === 1. DECLARACIÓN DE VARIABLES (componentes) ===
    private JTextField txtCedula, txtNombre, txtSegundoNombre, txtApellido, txtSegundoApellido;
    private JTextField txtEdad, txtTelefono, txtCorreo, txtDireccion;
    private JButton btnBuscar, btnGuardar;
    private JLabel lblCargo;
    private int usuarioId;
    private String cargo;
    private Base_De_Datos baseDatos = new Base_De_Datos();

    public PanelGestionUsuario() {
        // === 2. INICIALIZAR COMPONENTES ===
        miinitComponents();
        
        // === 3. CONFIGURAR LISTENERS (después de inicializar) ===
        btnBuscar.addActionListener(e -> buscarUsuario());
        btnGuardar.addActionListener(e -> guardarCambios());
    }

    /**
     * Método que inicializa TODOS los componentes y el layout
     */
    private void miinitComponents() {
        // Configurar panel principal
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        
        // Inicializar GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // === INICIALIZAR TODOS LOS COMPONENTES ===
        txtCedula = new JTextField(15);
        txtNombre = new JTextField(15);
        txtSegundoNombre = new JTextField(15);
        txtApellido = new JTextField(15);
        txtSegundoApellido = new JTextField(15);
        txtEdad = new JTextField(15);
        txtTelefono = new JTextField(15);
        txtCorreo = new JTextField(15);
        txtDireccion = new JTextField(15);
        
        btnBuscar = new JButton("Buscar");
        btnGuardar = new JButton("Guardar Cambios");
        
        lblCargo = new JLabel("Cargo: -");

        // === CREAR FORMULARIO ===
        int fila = 0;
        
        // Fila 0: Cédula + Buscar
        gbc.gridx = 0; gbc.gridy = fila;
        add(new JLabel("Cédula:"), gbc);
        
        gbc.gridx = 1;
        add(txtCedula, gbc);
        
        gbc.gridx = 2;
        add(btnBuscar, gbc);
        fila++;

        // Fila 1: Nombre
        gbc.gridx = 0; gbc.gridy = fila;
        add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(txtNombre, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Fila 2: Segundo Nombre
        gbc.gridx = 0; gbc.gridy = fila;
        add(new JLabel("Segundo Nombre:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(txtSegundoNombre, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Fila 3: Apellido
        gbc.gridx = 0; gbc.gridy = fila;
        add(new JLabel("Apellido:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(txtApellido, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Fila 4: Segundo Apellido
        gbc.gridx = 0; gbc.gridy = fila;
        add(new JLabel("Segundo Apellido:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(txtSegundoApellido, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Fila 5: Edad
        gbc.gridx = 0; gbc.gridy = fila;
        add(new JLabel("Edad:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(txtEdad, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Fila 6: Teléfono
        gbc.gridx = 0; gbc.gridy = fila;
        add(new JLabel("Teléfono:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(txtTelefono, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Fila 7: Correo
        gbc.gridx = 0; gbc.gridy = fila;
        add(new JLabel("Correo:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(txtCorreo, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Fila 8: Dirección
        gbc.gridx = 0; gbc.gridy = fila;
        add(new JLabel("Dirección:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(txtDireccion, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Fila 9: Cargo (no editable)
        gbc.gridx = 0; gbc.gridy = fila;
        add(new JLabel("Cargo Actual:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        lblCargo.setFont(new Font("Arial", Font.BOLD, 14));
        lblCargo.setForeground(Color.BLUE);
        add(lblCargo, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Fila 10: Botón Guardar
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnGuardar, gbc);
    }

    // Resto de métodos (buscarUsuario, guardarCambios) igual que antes
    private void buscarUsuario() {
        String cedula = txtCedula.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula.");
            return;
        }

        Object[] usuario = baseDatos.buscarUsuarioPorCedula(cedula);
        if (usuario != null) {
            usuarioId = (int) usuario[0];
            cargo = (String) usuario[1];
            txtNombre.setText((String) usuario[2]);
            txtSegundoNombre.setText((String) usuario[3]);
            txtApellido.setText((String) usuario[4]);
            txtSegundoApellido.setText((String) usuario[5]);
            txtEdad.setText(String.valueOf(usuario[6]));
            txtTelefono.setText((String) usuario[7]);
            txtCorreo.setText((String) usuario[8]);
            txtDireccion.setText((String) usuario[9]);
            lblCargo.setText(cargo);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
        }
    }

    private void guardarCambios() {
        if (usuarioId == 0) {
            JOptionPane.showMessageDialog(this, "Primero busque un usuario.");
            return;
        }

        try {
            int edad = Integer.parseInt(txtEdad.getText().trim());
            baseDatos.actualizarUsuario(cargo, usuarioId, txtCedula.getText().trim(),
                txtNombre.getText().trim(), txtSegundoNombre.getText().trim(),
                txtApellido.getText().trim(), txtSegundoApellido.getText().trim(),
                edad, txtTelefono.getText().trim(), txtCorreo.getText().trim(),
                txtDireccion.getText().trim());
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una edad válida.", 
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
