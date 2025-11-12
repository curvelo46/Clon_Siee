package Vista.frm.Panel;

import Clases.Base_De_Datos;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class PanelGestionUsuario extends JPanel {
    // Componentes
    private JTextField txtCedula, txtNombre, txtSegundoNombre, txtApellido, txtSegundoApellido;
    private JTextField txtEdad, txtTelefono, txtCorreo, txtDireccion;
    private JComboBox<String> comboCargo;
    private JButton btnBuscar, btnGuardar;
    private int usuarioId;
    private Base_De_Datos baseDatos = new Base_De_Datos();

    public PanelGestionUsuario() {
        initComponentes();
        cargarCargos();
        configurarListeners();
        configurarFiltros();
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // === PANEL SUPERIOR (Búsqueda) ===
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBackground(new Color(230, 230, 255));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda"));
        
        txtCedula = new JTextField(15);
        btnBuscar = new JButton("🔍 Buscar");
        panelBusqueda.add(new JLabel("Cédula:"));
        panelBusqueda.add(txtCedula);
        panelBusqueda.add(btnBuscar);

        // === PANEL CENTRAL (Datos) ===
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar componentes
        txtNombre = new JTextField(20);
        txtSegundoNombre = new JTextField(20);
        txtApellido = new JTextField(20);
        txtSegundoApellido = new JTextField(20);
        txtEdad = new JTextField(20);
        txtTelefono = new JTextField(20);
        txtCorreo = new JTextField(25);
        txtDireccion = new JTextField(30);
        comboCargo = new JComboBox<>();
        comboCargo.setPreferredSize(new Dimension(200, 30));
        
        // Estilo de campos
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        txtCedula.setFont(fieldFont);
        txtNombre.setFont(fieldFont);
        txtSegundoNombre.setFont(fieldFont);
        txtApellido.setFont(fieldFont);
        txtSegundoApellido.setFont(fieldFont);
        txtEdad.setFont(fieldFont);
        txtTelefono.setFont(fieldFont);
        txtCorreo.setFont(fieldFont);
        txtDireccion.setFont(fieldFont);
        comboCargo.setFont(fieldFont);

        // === DISEÑO HORIZONTAL (2 columnas) ===
        int fila = 0;
        
        // Fila 0: Nombre y Segundo Nombre
        gbc.gridx = 0; gbc.gridy = fila;
        panelDatos.add(crearLabel("Nombre*:"), gbc);
        
        gbc.gridx = 1;
        panelDatos.add(txtNombre, gbc);
        
        gbc.gridx = 2;
        panelDatos.add(crearLabel("Segundo Nombre:"), gbc);
        
        gbc.gridx = 3;
        panelDatos.add(txtSegundoNombre, gbc);
        fila++;

        // Fila 1: Apellidos
        gbc.gridx = 0; gbc.gridy = fila;
        panelDatos.add(crearLabel("Apellido*:"), gbc);
        
        gbc.gridx = 1;
        panelDatos.add(txtApellido, gbc);
        
        gbc.gridx = 2;
        panelDatos.add(crearLabel("Segundo Apellido:"), gbc);
        
        gbc.gridx = 3;
        panelDatos.add(txtSegundoApellido, gbc);
        fila++;

        // Fila 2: Edad y Teléfono
        gbc.gridx = 0; gbc.gridy = fila;
        panelDatos.add(crearLabel("Edad*:"), gbc);
        
        gbc.gridx = 1;
        panelDatos.add(txtEdad, gbc);
        
        gbc.gridx = 2;
        panelDatos.add(crearLabel("Teléfono:"), gbc);
        
        gbc.gridx = 3;
        panelDatos.add(txtTelefono, gbc);
        fila++;

        // Fila 3: Correo (ocupa 3 columnas)
        gbc.gridx = 0; gbc.gridy = fila;
        panelDatos.add(crearLabel("Correo*:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        panelDatos.add(txtCorreo, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Fila 4: Dirección
        gbc.gridx = 0; gbc.gridy = fila;
        panelDatos.add(crearLabel("Dirección:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        panelDatos.add(txtDireccion, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Fila 5: Cargo
        gbc.gridx = 0; gbc.gridy = fila;
        panelDatos.add(crearLabel("Cargo*:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        panelDatos.add(comboCargo, gbc);
        gbc.gridwidth = 1;
        fila++;

        // === PANEL INFERIOR (Botón) ===
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBoton.setBackground(new Color(245, 245, 245));
        
        btnGuardar = new JButton("💾 Guardar Cambios");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(70, 130, 180));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setPreferredSize(new Dimension(200, 40));
        panelBoton.add(btnGuardar);

        // === ENSAMBLAR PANEL PRINCIPAL ===
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelDatos, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private void configurarListeners() {
        btnBuscar.addActionListener(e -> buscarUsuario());
        btnGuardar.addActionListener(e -> guardarCambios());
    }

    private void cargarCargos() {
        comboCargo.removeAllItems();
        List<String> cargos = baseDatos.obtenerCargosPermitidos();
        for (String cargo : cargos) {
            comboCargo.addItem(cargo);
        }
    }

    private void configurarFiltros() {
        // Solo números en cédula y edad
        ((AbstractDocument) txtCedula.getDocument()).setDocumentFilter(new NumericFilter());
        ((AbstractDocument) txtEdad.getDocument()).setDocumentFilter(new NumericFilter());
        
        // Solo letras en nombres
        ((AbstractDocument) txtNombre.getDocument()).setDocumentFilter(new LetterFilter());
        ((AbstractDocument) txtSegundoNombre.getDocument()).setDocumentFilter(new LetterFilter());
        ((AbstractDocument) txtApellido.getDocument()).setDocumentFilter(new LetterFilter());
        ((AbstractDocument) txtSegundoApellido.getDocument()).setDocumentFilter(new LetterFilter());
        
        // Teléfono: números y símbolos básicos
        ((AbstractDocument) txtTelefono.getDocument()).setDocumentFilter(new PhoneFilter());
    }

    // ================== VALIDACIONES ==================
    private boolean validarCedula(String cedula) {
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cédula no puede estar vacía", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!cedula.matches("\\d{6,10}")) {
            JOptionPane.showMessageDialog(this, "Cédula inválida: debe contener 6-10 dígitos numéricos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarNombre(String nombre, String campo) {
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, campo + " no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s']+")) {
            JOptionPane.showMessageDialog(this, campo + " contiene caracteres inválidos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (nombre.length() < 2) {
            JOptionPane.showMessageDialog(this, campo + " debe tener al menos 2 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarEdad(String edadStr) {
        if (edadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La edad no puede estar vacía", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int edad = Integer.parseInt(edadStr);
            if (edad < 18 || edad > 100) {
                JOptionPane.showMessageDialog(this, "Edad debe estar entre 18 y 100 años", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Edad debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean validarTelefono(String telefono) {
        if (telefono.isEmpty()) return true; // Opcional
        if (!telefono.matches("[\\d\\s\\-\\+\\(\\)]{7,15}")) {
            JOptionPane.showMessageDialog(this, "Teléfono inválido: use solo números y símbolos (-, +, )", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarCorreo(String correo) {
        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El correo no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!Pattern.matches(emailRegex, correo)) {
            JOptionPane.showMessageDialog(this, "Formato de correo inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarDireccion(String direccion) {
        if (direccion.isEmpty()) return true; // Opcional
        if (direccion.length() < 5) {
            JOptionPane.showMessageDialog(this, "Dirección debe tener al menos 5 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!direccion.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s'#\\-,.]+")) {
            JOptionPane.showMessageDialog(this, "Dirección contiene caracteres inválidos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarFormulario() {
        return validarCedula(txtCedula.getText().trim()) &&
               validarNombre(txtNombre.getText().trim(), "Nombre") &&
               validarNombre(txtApellido.getText().trim(), "Apellido") &&
               validarEdad(txtEdad.getText().trim()) &&
               validarTelefono(txtTelefono.getText().trim()) &&
               validarCorreo(txtCorreo.getText().trim()) &&
               validarDireccion(txtDireccion.getText().trim());
    }

    // ================== MÉTODOS DE ACCIÓN ==================
    private void buscarUsuario() {
        String cedula = txtCedula.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula para buscar", "Aviso", JOptionPane.WARNING_MESSAGE);
            txtCedula.requestFocus();
            return;
        }

        Object[] usuario = baseDatos.buscarUsuarioPorCedula(cedula);
        if (usuario != null) {
            usuarioId = (int) usuario[0];
            String cargoActual = (String) usuario[1];
            
            txtNombre.setText((String) usuario[2]);
            txtSegundoNombre.setText((String) usuario[3]);
            txtApellido.setText((String) usuario[4]);
            txtSegundoApellido.setText((String) usuario[5]);
            txtEdad.setText(String.valueOf(usuario[6]));
            txtTelefono.setText((String) usuario[7]);
            txtCorreo.setText((String) usuario[8]);
            txtDireccion.setText((String) usuario[9]);
            
            comboCargo.setSelectedItem(cargoActual);
            btnGuardar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado con la cédula: " + cedula, "Error", JOptionPane.ERROR_MESSAGE);
            limpiarCampos();
        }
    }

    private void guardarCambios() {
        if (usuarioId == 0) {
            JOptionPane.showMessageDialog(this, "Primero debe buscar un usuario", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarFormulario()) {
            return; 
        }

        try {
            String nuevoCargo = (String) comboCargo.getSelectedItem();
            
            baseDatos.actualizarUsuarioConRol(
                usuarioId,
                txtCedula.getText().trim(),
                txtNombre.getText().trim(),
                txtSegundoNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtSegundoApellido.getText().trim(),
                Integer.parseInt(txtEdad.getText().trim()),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                txtDireccion.getText().trim(),
                nuevoCargo
            );
            
            JOptionPane.showMessageDialog(this, "✅ Usuario actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al actualizar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtSegundoNombre.setText("");
        txtApellido.setText("");
        txtSegundoApellido.setText("");
        txtEdad.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        comboCargo.setSelectedIndex(-1);
        usuarioId = 0;
        btnGuardar.setEnabled(false);
    }

    // ================== FILTROS DE DOCUMENTO ==================
    private static class NumericFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    private static class LetterFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s']*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s']*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    private static class PhoneFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[\\d\\s\\-\\+\\(\\)]*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("[\\d\\s\\-\\+\\(\\)]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
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
