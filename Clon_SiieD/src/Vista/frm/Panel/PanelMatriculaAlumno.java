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
    
    // Limpiar grupo de botones
    Enumeration<AbstractButton> buttons = grupoMaterias.getElements();
    while (buttons.hasMoreElements()) {
        grupoMaterias.remove(buttons.nextElement());
    }

    String carrera = (String) comboCarreras.getSelectedItem();
    if (carrera == null) return;

    // ✅ Nuevo query: incluye docente y docente_materia_id
    String sql = "SELECT dm.id AS docente_materia_id, \n" +
"               m.nombre AS materia_nombre,\n" +
"               CONCAT(u.nombre, ' ', u.apellido) AS docente_nombre\n" +
"        FROM Docente_Materias dm\n" +
"        JOIN Materias m ON dm.materia_id = m.id\n" +
"        JOIN Carreras c ON m.carrera_id = c.id\n" +
"        JOIN Docentes d ON d.id = dm.docente_id\n" +
"        JOIN Usuarios u ON u.id = d.id\n" +
"        WHERE c.nombre = ?\n" +
"        ORDER BY m.nombre, u.apellido, u.nombre";

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, carrera);
        try (ResultSet rs = ps.executeQuery()) {
            
            JRadioButton rbTodas = new JRadioButton("Todas las materias");
            rbTodas.setActionCommand("TODAS"); // Especial command
            grupoMaterias.add(rbTodas);
            panelMaterias.add(rbTodas);

            while (rs.next()) {
                int dmId = rs.getInt("docente_materia_id");
                String materia = rs.getString("materia_nombre");
                String docente = rs.getString("docente_nombre");
                
                // ✅ Mostrar "Materia - Profesor"
                String displayText = materia + " - Prof. " + docente;
                JRadioButton rb = new JRadioButton(displayText);
                rb.setActionCommand(String.valueOf(dmId)); // ✅ Guardar ID directo!
                grupoMaterias.add(rb);
                panelMaterias.add(rb);
            }
        }
        
        if (grupoMaterias.getButtonCount() > 0) {
            ((JRadioButton)grupoMaterias.getElements().nextElement()).setSelected(true);
        }

        panelMaterias.revalidate();
        panelMaterias.repaint();

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al cargar materias: " + ex.getMessage());
    }
}

    private void matricularAlumno() {
    String alumno = (String) comboAlumnos.getSelectedItem();
    String seleccion = getSelectedMateria(); // Ahora retorna un String que es el ID o "TODAS"

    if (alumno == null || seleccion == null) {
        JOptionPane.showMessageDialog(this, "Seleccione alumno y materia.");
        return;
    }

    try (Connection conn = ConexionBD.getConnection()) {
        conn.setAutoCommit(false);
        int idAlumno = getAlumnoId(conn, alumno);

        if ("TODAS".equals(seleccion)) {
            // Matricular en todas las materias visible en el panel
            int count = 0;
            for (Component c : panelMaterias.getComponents()) {
                if (c instanceof JRadioButton) {
                    JRadioButton rb = (JRadioButton) c;
                    String cmd = rb.getActionCommand();
                    if (!"TODAS".equals(cmd) && rb.isSelected()) { // Todas seleccionadas
                        int dmId = Integer.parseInt(cmd);
                        matricularEnMateria(conn, idAlumno, dmId);
                        count++;
                    }
                }
            }
            lblInfo.setText("✅ Matriculado en " + count + " materias.");
        } else {
            // Matricular en una sola materia específica
            int dmId = Integer.parseInt(seleccion);
            matricularEnMateria(conn, idAlumno, dmId);
            lblInfo.setText("✅ Matriculado en la materia seleccionada.");
        }

        conn.commit();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "❌ Error al matricular: " + ex.getMessage());
        ex.printStackTrace();
    }
}


    private void retirarAlumno() {
    String alumno = (String) comboAlumnos.getSelectedItem();
    String seleccion = getSelectedMateria();

    if (alumno == null || seleccion == null) {
        JOptionPane.showMessageDialog(this, "Seleccione alumno y materia.");
        return;
    }

    try (Connection conn = ConexionBD.getConnection()) {
        conn.setAutoCommit(false);
        int idAlumno = getAlumnoId(conn, alumno);

        if ("TODAS".equals(seleccion)) {
            // Retirar de todas
            String sql = "DELETE FROM Alumno_Materias WHERE alumno_id = ? AND docente_materia_id IN " +
                         "(SELECT dm.id FROM Docente_Materias dm " +
                         " JOIN Materias m ON dm.materia_id = m.id " +
                         " JOIN Carreras c ON m.carrera_id = c.id " +
                         " WHERE c.nombre = ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idAlumno);
                ps.setString(2, (String) comboCarreras.getSelectedItem());
                int rows = ps.executeUpdate();
                lblInfo.setText("🗑️ Retirado de " + rows + " materias.");
            }
        } else {
            // Retirar de una específica
            int dmId = Integer.parseInt(seleccion);
            String sql = "DELETE FROM Alumno_Materias WHERE alumno_id = ? AND docente_materia_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idAlumno);
                ps.setInt(2, dmId);
                ps.executeUpdate();
                lblInfo.setText("🗑️ Retirado de la materia.");
            }
        }

        conn.commit();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "❌ Error al retirar: " + ex.getMessage());
        ex.printStackTrace();
    }
}
    
    private String getSelectedMateria() {
    for (Component c : panelMaterias.getComponents()) {
        if (c instanceof JRadioButton) {
            JRadioButton rb = (JRadioButton) c;
            if (rb.isSelected()) {
                return rb.getActionCommand(); // Retorna "TODAS" o el ID como String
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
