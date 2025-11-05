/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vista.frm.Panel;

import Clases.ConexionBD;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Enumeration;

public class PanelGestionMaterias extends JPanel {

    private JComboBox<String> comboDocentes = new JComboBox<>();
    private JComboBox<String> comboMaterias = new JComboBox<>();
    private JTextField txtNombreMateria = new JTextField(20);
    private JButton btnCrearMateria = new JButton("Crear materia");
    private JButton btnAsignar = new JButton("Asignar materia");
    private ButtonGroup grupoCarreras = new ButtonGroup();
    private JPanel panelRadios = new JPanel(new GridLayout(0, 1));

    public PanelGestionMaterias() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de materias"));

        add(crearPanelCrear(), BorderLayout.NORTH);
        add(crearPanelAsignar(), BorderLayout.CENTER);
        add(crearPanelCarreras(), BorderLayout.SOUTH);

        cargarDocentes();
        cargarMaterias();
        cargarCarreras();
    }

    private JPanel crearPanelCrear() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("Nombre nueva materia:"));
        p.add(txtNombreMateria);
        p.add(btnCrearMateria);
        btnCrearMateria.addActionListener(e -> crearMateria());
        return p;
    }

    private JPanel crearPanelAsignar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("Materia:"));
        p.add(comboMaterias);
        p.add(new JLabel("Docente:"));
        p.add(comboDocentes);
        p.add(btnAsignar);
        btnAsignar.addActionListener(e -> asignarMateria());
        return p;
    }

    private JPanel crearPanelCarreras() {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createTitledBorder("Carreras"));
        p.add(panelRadios);
        return p;
    }

    private void cargarDocentes() {
        comboDocentes.removeAllItems();
        String sql = "CALL listado_docentes()";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comboDocentes.addItem(rs.getInt("id") + " - " +
                                      rs.getString("nombre") + " " +
                                      rs.getString("apellido"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error docentes: " + ex.getMessage());
        }
    }

    private void cargarMaterias() {    
    comboMaterias.removeAllItems();
    comboMaterias.addItem("Seleccione materia"); // ← texto en blanco
    String carrera = getCarreraSeleccionada();
    if (carrera == null) return;

    String sql = "CALL materias_por_carrera(?)";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, carrera);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            comboMaterias.addItem(rs.getString("nombre"));
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error materias: " + ex.getMessage());
    }
    comboMaterias.setSelectedIndex(0); // deja el texto como placeholder
    ((JLabel) comboMaterias.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER); // centrado (opcional)
}

    private void cargarCarreras() {
        panelRadios.removeAll();
        grupoCarreras = new ButtonGroup();
        String sql = "SELECT nombre FROM Carreras";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                JRadioButton rb = new JRadioButton(rs.getString("nombre"));
                grupoCarreras.add(rb);
                panelRadios.add(rb);
            }
            if (grupoCarreras.getButtonCount() > 0) {
                ((JRadioButton) grupoCarreras.getElements().nextElement()).setSelected(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error carreras: " + ex.getMessage());
        }
        
                // 👇 Actualización instantánea al cambiar de carrera
        for (Enumeration<AbstractButton> buttons = grupoCarreras.getElements(); buttons.hasMoreElements();) {
            AbstractButton btn = buttons.nextElement();
            btn.addActionListener(e -> {
                cargarMaterias();          // recarga el combo
                comboMaterias.setSelectedIndex(-1); // (opcional) limpia selección previa
            });
        }
    }

    private void crearMateria() {
    String nombre = txtNombreMateria.getText().trim();
    String carrera = getCarreraSeleccionada();

    if (nombre.isEmpty() || carrera == null) {
        JOptionPane.showMessageDialog(this, "Escribe un nombre y selecciona una carrera");
        return;
    }

    try (Connection conn = ConexionBD.getConnection()) {
        conn.setAutoCommit(false);

        // 1. Obtener id de la carrera seleccionada
        int idCarrera = 0;
        String sqlCarrera = "SELECT id FROM Carreras WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlCarrera)) {
            ps.setString(1, carrera);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) idCarrera = rs.getInt("id");
        }

        if (idCarrera == 0) {
            JOptionPane.showMessageDialog(this, "Carrera no encontrada");
            return;
        }

        // 2. Crear materia (ya con carrera_id)
        String sqlCrear = "CALL crear_materia(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sqlCrear)) {
            ps.setString(1, nombre);
            ps.setInt(2, idCarrera);
            ps.executeUpdate();
        }

        conn.commit();
        JOptionPane.showMessageDialog(this, "Materia creada y asociada a " + carrera);
        txtNombreMateria.setText("");
        cargarMaterias();

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al crear: " + ex.getMessage());
    }
}

    private void asignarMateria() {
    String materia = (String) comboMaterias.getSelectedItem();
    String docente = (String) comboDocentes.getSelectedItem();
    String carrera = getCarreraSeleccionada();

    if (materia == null || docente == null || carrera == null) return;

    int idDocente = Integer.parseInt(docente.split(" - ")[0]);
    int idMateria = 0;

    // obtener id materia **perteneciente a la carrera seleccionada**
    String sqlId = "CALL id_materia_por_carrera(?, ?)";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sqlId)) {
        ps.setString(1, materia);
        ps.setString(2, carrera);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) idMateria = rs.getInt("id");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al obtener id de materia: " + ex.getMessage());
        return;
    }

    if (idMateria == 0) {
        JOptionPane.showMessageDialog(this, "No se encontró la materia en la carrera seleccionada");
        return;
    }

    // asignar docente – materia
    String sqlAsig = "CALL asignar_m_a_p(?, ?)";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sqlAsig)) {
        ps.setInt(1, idDocente);
        ps.setInt(2, idMateria);
        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "Materia asignada al docente");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al asignar: " + ex.getMessage());
    }
}

    public String getCarreraSeleccionada() {
        for (Enumeration<AbstractButton> buttons = grupoCarreras.getElements(); buttons.hasMoreElements();) {
            AbstractButton btn = buttons.nextElement();
            if (btn.isSelected()) return btn.getText();
        }
        return null;
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
