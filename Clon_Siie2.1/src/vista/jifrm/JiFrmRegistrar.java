/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package vista.jifrm;

import clases.ConexionBD;

import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author cymaniatico
 */
public class JiFrmRegistrar extends javax.swing.JInternalFrame {
        
    /**
     * Creates new form JiFrmPrueba
     */

    public JiFrmRegistrar() {
        initComponents();
        this.getContentPane().setBackground(new Color(214, 245, 255));
        cargarcargos();
        cargarCarrerasDesdeBD();
        carrera.setVisible(false);
        Cargo.addActionListener(e -> {
        String seleccion = (String) Cargo.getSelectedItem();
        if ("alumno".equalsIgnoreCase(seleccion)) {
            carrera.setVisible(true);
            cargarCarrerasDesdeBD(); // Solo cuando es alumno
        } else {
            carrera.setVisible(false);
        }
    });
        
    }
    
    private void cargarcargos() {
        Cargo.removeAllItems();
        Cargo.addItem("administrador");
        Cargo.addItem("docente");
        Cargo.addItem("alumno");
  
    }

    private void cargarCarrerasDesdeBD() {
    carrera.removeAllItems(); // Limpiar primero

    String sql = "SELECT nombre FROM Carreras"; // Asegúrate de que esta tabla y columna existan

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            carrera.addItem(rs.getString("nombre"));
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + e.getMessage());
    }
}
    
private void MatricularAlumnos() {
    // 1️⃣ Recoger datos del formulario
    String nombre = txtNombre.getText();
    String segundoNombre = txtSegundoNombr.getText();
    String apellido = txtApellido.getText();
    String segundoApellido = txtSegundoApellido.getText();
    String edadStr = txtEdad.getText();
    String telefono = txtTelefono.getText();
    String correo = txtCorreo.getText();
    String direccion = txtDireccion.getText();
    String cedula = txtCedula.getText();
    String genero = txtGenero.getText();
    String cargo = (String) Cargo.getSelectedItem();
    String carreraSeleccionada = (String) carrera.getSelectedItem();

    // Validar edad
    int edad = 0;
    try {
        edad = Integer.parseInt(edadStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Edad inválida. Debe ser un número.");
        return;
    }

    String sqlRegistrarUsuario = "{CALL registrar_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    try (Connection conn = ConexionBD.getConnection()) {
        conn.setAutoCommit(false); // Inicio de transacción

        // 2️⃣ Registrar usuario
        try (CallableStatement stmt = conn.prepareCall(sqlRegistrarUsuario)) {
            stmt.setString(1, nombre);
            stmt.setString(2, segundoNombre);
            stmt.setString(3, apellido);
            stmt.setString(4, segundoApellido);
            stmt.setInt(5, edad);
            stmt.setString(6, telefono);
            stmt.setString(7, correo);
            stmt.setString(8, direccion);
            stmt.setString(9, cedula); // <- Cédula como String
            stmt.setString(10, genero);
            stmt.setString(11, cargo);

            // Carrera solo si es alumno
            if ("alumno".equalsIgnoreCase(cargo)) {
                stmt.setString(12, carreraSeleccionada);
            } else {
                stmt.setNull(12, java.sql.Types.VARCHAR);
            }

            stmt.execute();
        }

        // 3️⃣ Matricular en materias si es alumno
        if ("alumno".equalsIgnoreCase(cargo)) {
            String sqlMaterias = "SELECT idMateria FROM Materias WHERE carrera = ?";
            try (PreparedStatement psMaterias = conn.prepareStatement(sqlMaterias)) {
                psMaterias.setString(1, carreraSeleccionada);
                try (ResultSet rs = psMaterias.executeQuery()) {
                    String sqlMatricular = "{CALL matricular_alumno_en_materia(?, ?)}";
                    while (rs.next()) {
                        int idMateria = rs.getInt("idMateria");
                        try (CallableStatement csMatricula = conn.prepareCall(sqlMatricular)) {
                            csMatricula.setString(1, cedula); // id o cedula del alumno
                            csMatricula.setInt(2, idMateria);
                            csMatricula.execute();
                        }
                    }
                }
            }
        }

        conn.commit(); // Confirmar transacción
        JOptionPane.showMessageDialog(this, "✅ Usuario registrado correctamente.");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "❌ Error al registrar usuario: " + e.getMessage());
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

        jLabel1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        btnGuerdarAlumno = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSegundoNombr = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtSegundoApellido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtEdad = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtCedula = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtGenero = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        Cargo = new javax.swing.JComboBox<>();
        carrera = new javax.swing.JComboBox<>();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);

        jLabel1.setText("Primer  Nombre ");

        btnGuerdarAlumno.setText("Registrar Alumno");
        btnGuerdarAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuerdarAlumnoActionPerformed(evt);
            }
        });

        jLabel3.setText("segundo Nombre");

        jLabel4.setText("Primer apellido ");

        jLabel5.setText("Segundo apellido");

        jLabel6.setText("Edad");

        jLabel7.setText("Telefono");

        jLabel8.setText("Correo");

        jLabel9.setText("Direccion");

        jLabel10.setText("CC");

        jLabel11.setText("Genero");

        jButton1.setText("Limpiar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Cargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        carrera.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtCedula, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtEdad, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel10))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtCorreo, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSegundoNombr, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtApellido))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(txtDireccion))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(21, 21, 21)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9)
                                        .addComponent(txtSegundoApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Cargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(carrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel11)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGuerdarAlumno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSegundoNombr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSegundoApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(Cargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEdad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(carrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuerdarAlumno)
                    .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuerdarAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuerdarAlumnoActionPerformed
        // TODO add your handling code here:
        
        MatricularAlumnos();

    }//GEN-LAST:event_btnGuerdarAlumnoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        txtNombre.setText("");
        txtSegundoNombr.setText("");
        txtApellido.setText("");
        txtSegundoApellido.setText("");
        txtEdad.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtCedula.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Cargo;
    private javax.swing.JButton btnGuerdarAlumno;
    private javax.swing.JComboBox<String> carrera;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtEdad;
    private javax.swing.JTextField txtGenero;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtSegundoApellido;
    private javax.swing.JTextField txtSegundoNombr;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
