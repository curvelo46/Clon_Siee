package Vista.frm.Panel;

import Clases.Base_De_Datos;
import Clases.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PanelListaUsuarios extends JPanel {

    private final Base_De_Datos baseDatos;
    private final String usuarioDocente;
private final JPanel panelRoles = new JPanel();
private final ButtonGroup grupoRoles = new ButtonGroup();
    private final JPanel panelCarreras = new JPanel();
    private final ButtonGroup grupoCarreras = new ButtonGroup();
    private final JComboBox<String> comboMaterias = new JComboBox<>();
    private final JTable tablaUsuarios = new JTable();
private final DefaultTableModel modeloTabla =
        new DefaultTableModel(
                new Object[]{
                        "CC",
                        "Nombre",
                        "Segundo Nombre",
                        "Apellido",
                        "Segundo Apellido",
                        "Edad",
                        "Género",
                        "Dirección",
                        "Teléfono",
                        "Correo"
                }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    public PanelListaUsuarios(Base_De_Datos baseDatos, String usuarioDocente) {
        this.baseDatos = baseDatos;
        this.usuarioDocente = usuarioDocente;
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponentes();
        cargarRoles();
    }

    private void initComponentes() {
    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    /* ---------- NORTE: Roles en horizontal ---------- */
    panelRoles.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
    panelRoles.setBorder(BorderFactory.createTitledBorder("Roles"));
    JScrollPane scrollRoles = new JScrollPane(panelRoles);
    scrollRoles.setPreferredSize(new Dimension(600, 80));
    scrollRoles.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollRoles.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

    JPanel panelNorte = new JPanel(new BorderLayout());
    panelNorte.add(scrollRoles, BorderLayout.WEST);

    add(panelNorte, BorderLayout.NORTH);

    /* ---------- CENTRO: Tabla ---------- */
    tablaUsuarios.setModel(modeloTabla);
    tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaUsuarios.setGridColor(Color.LIGHT_GRAY);
    tablaUsuarios.getTableHeader().setReorderingAllowed(false);

    JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);
    scrollTabla.setBorder(BorderFactory.createTitledBorder("Listado de usuarios"));
    add(scrollTabla, BorderLayout.CENTER);

    cargarRoles();
}

    
    
    
    
    
    
    
    /* -------------------- CARGAR ROLES (SP) -------------------- */
private void cargarRoles() {
    panelRoles.removeAll();
    grupoRoles.clearSelection();

    String sql = "{CALL obtener_roles_usuarios()}";
    try (Connection conn = baseDatos.getConnection();
         CallableStatement cs = conn.prepareCall(sql);
         ResultSet rs = cs.executeQuery()) {

        while (rs.next()) {
            String rol = rs.getString("rol");
            JRadioButton rb = new JRadioButton(rol);
            grupoRoles.add(rb);
            panelRoles.add(rb);
            rb.addActionListener(e -> cargarUsuariosPorRol(rol));
        }

        if (grupoRoles.getButtonCount() > 0) {
            JRadioButton primero = (JRadioButton) grupoRoles.getElements().nextElement();
            primero.setSelected(true);
            cargarUsuariosPorRol(primero.getText());
        }

        panelRoles.revalidate();
        panelRoles.repaint();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al cargar roles: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

/* -------------------- CARGAR USUARIOS POR ROL (SP) -------------------- */
private void cargarUsuariosPorRol(String rol) {
    modeloTabla.setRowCount(0);

    String sql = "{CALL listar_usuarios_por_rol(?)}";
    try (Connection conn = baseDatos.getConnection();
         CallableStatement cs = conn.prepareCall(sql)) {
        cs.setString(1, rol);
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            modeloTabla.addRow(new Object[]{
                    rs.getString("cc"),
                    rs.getString("nombre"),
                    rs.getString("segundo_nombre"),
                    rs.getString("apellido"),
                    rs.getString("segundo_apellido"),
                    rs.getString("edad"),
                    rs.getString("sexo"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("correo")
            });
        }
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay usuarios con el rol: " + rol);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    
    
    
    
    
    /* -------------------- ID CARRERA (SP) -------------------- */
    private int obtenerIdCarreraSP(String nombreCarrera) {
        int id = 0;
        String sql = "CALL id_carrera_por_nombre(?)";
        try (Connection conn = baseDatos.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreCarrera);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) id = rs.getInt("id");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }

    /* -------------------- CARGAR ALUMNOS (SP) -------------------- */
    private void cargarAlumnosSP() {
        modeloTabla.setRowCount(0);
        String carrera = getCarreraSeleccionada();
        String materia = (String) comboMaterias.getSelectedItem();
        if (carrera == null || materia == null || materia.startsWith("Sin")) return;

        int idCarrera = obtenerIdCarreraSP(carrera);
        String sql = "CALL listar_estudiantes_por_docente_materia(?, ?, ?)";
        try (Connection conn = baseDatos.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuarioDocente);
            ps.setString(2, materia);
            ps.setInt(3, idCarrera);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getString("cc"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("edad"),
                        rs.getString("telefono"),
                        rs.getString("correo")
                });
            }
            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No hay alumnos matriculados en esta materia.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar alumnos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* -------------------- UTIL -------------------- */
    private String getCarreraSeleccionada() {
    for (Enumeration<AbstractButton> e = grupoCarreras.getElements(); e.hasMoreElements();) {
        AbstractButton b = e.nextElement();
        if (b.isSelected()) return b.getText();
    }
    return null;
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
