package Vista.frm.Panel;

import Clases.ConexionBD;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PanelMatriculaAlumno extends JPanel {

    private final JComboBox<String> comboAlumnos = new JComboBox<>();
    private final JComboBox<String> comboCarreras = new JComboBox<>();
    private final JPanel panelMaterias = new JPanel();
    private final ButtonGroup grupoMaterias = new ButtonGroup();
    private final JButton btnMatricular = new JButton("Matricular");
    private final JButton btnRetirar = new JButton("Retirar");
    private final JLabel lblInfo = new JLabel("Seleccione alumno, carrera y materia(s)");

    public PanelMatriculaAlumno() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Matricular / Retirar Alumno"));

        JPanel top = new JPanel(new GridLayout(2, 2, 10, 10));
        top.add(new JLabel("Alumno:"));
        top.add(comboAlumnos);
        top.add(new JLabel("Carrera:"));
        top.add(comboCarreras);

        panelMaterias.setLayout(new BoxLayout(panelMaterias, BoxLayout.Y_AXIS));
        panelMaterias.setBorder(BorderFactory.createTitledBorder("Materias"));
        JScrollPane scroll = new JScrollPane(panelMaterias);
        scroll.setPreferredSize(new Dimension(400, 200));

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(btnMatricular);
        bottom.add(btnRetirar);
        bottom.add(lblInfo);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        comboCarreras.addActionListener(e -> cargarMateriasPorCarrera());
        comboAlumnos.addActionListener(e -> lblInfo.setText("Seleccione acción"));

        btnMatricular.addActionListener(e -> matricularAlumno());
        btnRetirar.addActionListener(e -> retirarAlumno());

        cargarAlumnos();
        cargarCarreras();
    }

    private void cargarAlumnos() {
        comboAlumnos.removeAllItems();
        String sql = "SELECT CONCAT(nombre, ' ', apellido) AS nombre_completo FROM Usuarios WHERE cargo = 'alumno' ORDER BY nombre";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comboAlumnos.addItem(rs.getString("nombre_completo"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar alumnos: " + ex.getMessage());
        }
    }

    private void cargarCarreras() {
        comboCarreras.removeAllItems();
        String sql = "SELECT nombre FROM Carreras ORDER BY nombre";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comboCarreras.addItem(rs.getString("nombre"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + ex.getMessage());
        }
    }

    private void cargarMateriasPorCarrera() {
    panelMaterias.removeAll();

    // ✅ Eliminar todos los botones del grupo manualmente
    Enumeration<AbstractButton> buttons = grupoMaterias.getElements();
    while (buttons.hasMoreElements()) {
        grupoMaterias.remove(buttons.nextElement());
    }

    String carrera = (String) comboCarreras.getSelectedItem();
    if (carrera == null) return;

    List<String> materias = new ArrayList<>();
    String sql = "SELECT m.nombre FROM Materias m JOIN Carreras c ON m.carrera_id = c.id WHERE c.nombre = ? ORDER BY m.nombre";

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, carrera);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                materias.add(rs.getString("nombre"));
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al cargar materias: " + ex.getMessage());
        return;
    }

    JRadioButton rbTodas = new JRadioButton("Todas las materias");
    rbTodas.setActionCommand("TODAS");
    grupoMaterias.add(rbTodas);
    panelMaterias.add(rbTodas);

    for (String m : materias) {
        JRadioButton rb = new JRadioButton(m);
        rb.setActionCommand(m);
        grupoMaterias.add(rb);
        panelMaterias.add(rb);
    }

    if (grupoMaterias.getButtonCount() > 0) {
        rbTodas.setSelected(true);
    }

    panelMaterias.revalidate();
    panelMaterias.repaint();
}

    private void matricularAlumno() {
        String alumno = (String) comboAlumnos.getSelectedItem();
        String carrera = (String) comboCarreras.getSelectedItem();
        String seleccion = getSelectedMateria();

        if (alumno == null || carrera == null || seleccion == null) {
            JOptionPane.showMessageDialog(this, "Seleccione alumno, carrera y materia.");
            return;
        }

        try (Connection conn = ConexionBD.getConnection()) {
            conn.setAutoCommit(false);

            int idAlumno = getAlumnoId(conn, alumno);
            int idCarrera = getCarreraId(conn, carrera);

            if ("TODAS".equals(seleccion)) {
                List<Integer> ids = getDocenteMateriaIds(conn, idCarrera);
                for (int dmId : ids) {
                    matricularEnMateria(conn, idAlumno, dmId);
                }
                lblInfo.setText("Matriculado en " + ids.size() + " materias.");
            } else {
                int dmId = getDocenteMateriaId(conn, idCarrera, seleccion);
                if (dmId != -1) {
                    matricularEnMateria(conn, idAlumno, dmId);
                    lblInfo.setText("Matriculado en " + seleccion);
                }
            }

            conn.commit();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al matricular: " + ex.getMessage());
        }
    }

    private void retirarAlumno() {
        String alumno = (String) comboAlumnos.getSelectedItem();
        String carrera = (String) comboCarreras.getSelectedItem();
        String seleccion = getSelectedMateria();

        if (alumno == null || carrera == null || seleccion == null) {
            JOptionPane.showMessageDialog(this, "Seleccione alumno, carrera y materia.");
            return;
        }

        try (Connection conn = ConexionBD.getConnection()) {
            conn.setAutoCommit(false);

            int idAlumno = getAlumnoId(conn, alumno);
            int idCarrera = getCarreraId(conn, carrera);

            if ("TODAS".equals(seleccion)) {
                String sql = "DELETE FROM Alumno_Materias WHERE alumno_id = ? AND docente_materia_id IN " +
                             "(SELECT dm.id FROM Docente_Materias dm JOIN Materias m ON dm.materia_id = m.id WHERE m.carrera_id = ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, idAlumno);
                    ps.setInt(2, idCarrera);
                    int rows = ps.executeUpdate();
                    lblInfo.setText("Retirado de " + rows + " materias.");
                }
            } else {
                int dmId = getDocenteMateriaId(conn, idCarrera, seleccion);
                if (dmId != -1) {
                    String sql = "DELETE FROM Alumno_Materias WHERE alumno_id = ? AND docente_materia_id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, idAlumno);
                        ps.setInt(2, dmId);
                        ps.executeUpdate();
                        lblInfo.setText("Retirado de " + seleccion);
                    }
                }
            }

            conn.commit();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al retirar: " + ex.getMessage
                    ());
}
}
    
    private String getSelectedMateria() {
    for (Component c : panelMaterias.getComponents()) {
        if (c instanceof JRadioButton) {
            JRadioButton rb = (JRadioButton) c;
            if (rb.isSelected()) {
                return rb.getActionCommand();
            }
        }
    }
    return null;
}

private int getAlumnoId(Connection conn, String nombreCompleto) throws SQLException {
    String[] partes = nombreCompleto.split(" ", 2);
    String sql = "CALL obtener_id_alumno_por_nombre_apellido(?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, partes[0]);
        ps.setString(2, partes[1]);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("id");
        }
    }
    throw new SQLException("Alumno no encontrado");
}

private int getCarreraId(Connection conn, String nombre) throws SQLException {
    String sql = "CALL id_carrera_por_nombre(?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, nombre);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("id");
        }
    }
    throw new SQLException("Carrera no encontrada");
}

private int getDocenteMateriaId(Connection conn, int idCarrera, String materia) throws SQLException {
    String sql = "SELECT dm.id FROM Docente_Materias dm JOIN Materias m ON dm.materia_id = m.id WHERE m.carrera_id = ? AND m.nombre = ? LIMIT 1";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idCarrera);
        ps.setString(2, materia);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("id");
        }
    }
    return -1;
}

private List<Integer> getDocenteMateriaIds(Connection conn, int idCarrera) throws SQLException {
    List<Integer> ids = new ArrayList<>();
    String sql = "SELECT dm.id FROM Docente_Materias dm JOIN Materias m ON dm.materia_id = m.id WHERE m.carrera_id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idCarrera);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ids.add(rs.getInt("id"));
        }
    }
    return ids;
}

private void matricularEnMateria(Connection conn, int idAlumno, int idDocenteMateria) throws SQLException {
    String sql = "INSERT IGNORE INTO Alumno_Materias (alumno_id, docente_materia_id, corte1, corte2, corte3) VALUES (?, ?, 0, 0, 0)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idAlumno);
        ps.setInt(2, idDocenteMateria);
        ps.executeUpdate();
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
