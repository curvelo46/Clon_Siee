package Vista.frm.Panel;

import Clases.Base_De_Datos;
import Clases.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;


public class PanelListaEstudiantes extends JPanel {

    private final Base_De_Datos baseDatos;
    private final String usuarioDocente;

    private final JComboBox<String> comboMaterias = new JComboBox<>();
    private final JTable tablaEstudiantes = new JTable();
    private final DefaultTableModel modeloTabla =
            new DefaultTableModel(new Object[]{"CC", "Nombre", "Apellido", "Edad", "Teléfono", "Correo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    public PanelListaEstudiantes(Base_De_Datos baseDatos, String usuarioDocente) {
        this.baseDatos = baseDatos;
        this.usuarioDocente = usuarioDocente;
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponentes();
        cargarMateriasDelDocente();
    }


    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Norte: combo de materias
        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.add(new JLabel("Materia:"));
        norte.add(comboMaterias);
        JButton btnCargar = new JButton("Cargar alumnos");
        btnCargar.addActionListener(e -> cargarAlumnosSP());
        norte.add(btnCargar);
        add(norte, BorderLayout.NORTH);

        // Centro: tabla
        tablaEstudiantes.setModel(modeloTabla);
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEstudiantes.setGridColor(Color.LIGHT_GRAY);
        tablaEstudiantes.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollTabla = new JScrollPane(tablaEstudiantes);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Listado de estudiantes"));
        add(scrollTabla, BorderLayout.CENTER);
    }

    /* -------------------- CARGAR CARRERAS (SP) -------------------- */
    private void cargarMateriasDelDocente() {
        comboMaterias.removeAllItems();
        comboMaterias.addItem("Seleccione materia");

        String sql = "CALL obtener_materias_docente(?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuarioDocente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comboMaterias.addItem(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar materias: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /* -------------------- CARGAR MATERIAS (SP) -------------------- */
    

    /* -------------------- ID CARRERA (SP) -------------------- */
    

    /* -------------------- CARGAR ALUMNOS (SP) -------------------- */
    private void cargarAlumnosSP() {
    modeloTabla.setRowCount(0);
    String materia = (String) comboMaterias.getSelectedItem();
    if (materia == null || materia.equals("Seleccione materia")) return;

    // 🔧 Obtenemos la carrera que tiene asignada esta materia para este docente
    int idCarrera = obtenerCarreraDeMateriaDocente(usuarioDocente, materia);

    String sql = "{CALL listar_estudiantes_por_docente_materia(?, ?, ?)}";
    try (Connection conn = ConexionBD.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {
        stmt.setString(1, usuarioDocente);
        stmt.setString(2, materia);
        stmt.setInt(3, idCarrera);
        ResultSet rs = stmt.executeQuery();
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

   private int obtenerCarreraDeMateriaDocente(String usuarioDocente, String nombreMateria) {
    int idCarrera = 0;
    String sql = "{CALL obtener_carrera_id_por_docente_materia(?, ?)}";
    try (Connection conn = ConexionBD.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {
        stmt.setString(1, usuarioDocente);
        stmt.setString(2, nombreMateria);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) idCarrera = rs.getInt("carrera_id");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return idCarrera;
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
