package Vista.frm.Panel;

import Clases.ConexionBD;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Enumeration;

public class PanelAlumnosPorCarrera extends JPanel {

    private final ButtonGroup grupoCarreras = new ButtonGroup();
    private final JPanel panelRadios = new JPanel();
    private final JComboBox<String> comboAlumnos = new JComboBox<>();
    private final JTextField txtBuscar = new JTextField(15);
    private final JButton btnBuscar = new JButton("Buscar");

    /* ---------- Tablas y modelos ---------- */
    private final JTable tablaNotas = new JTable();
   private final DefaultTableModel modeloNotas = new DefaultTableModel(
        new Object[]{"Materia", "Corte 1", "Corte 2", "Corte 3", "Promedio"}, 0) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // 🔒 No editable
    }
};

    private final JTable tablaReportes = new JTable();
    
    private final DefaultTableModel modeloReportes = new DefaultTableModel(
        new Object[]{"Reporte", "Docente", "Fecha"}, 0) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
};

    private final JLabel lblPromedioGeneral = new JLabel("Promedio general: --");

    public PanelAlumnosPorCarrera() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Seleccione una carrera y alumno"));

        /* ---------- Panel izquierdo: carreras ---------- */
        panelRadios.setLayout(new BoxLayout(panelRadios, BoxLayout.Y_AXIS));
        panelRadios.setBorder(BorderFactory.createTitledBorder("Carreras"));
        JScrollPane scrollRadios = new JScrollPane(panelRadios);
        scrollRadios.setPreferredSize(new Dimension(250, 200));

        /* ---------- Panel superior: búsqueda y combo ---------- */
        JPanel panelAlumnos = new JPanel(new BorderLayout());
        panelAlumnos.setBorder(BorderFactory.createTitledBorder("Alumnos"));

        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscar.add(new JLabel("Buscar:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);

        JPanel panelCombo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCombo.add(new JLabel("Alumno:"));
        panelCombo.add(comboAlumnos);

        JPanel norte = new JPanel(new GridLayout(2, 1));
        norte.add(panelBuscar);
        norte.add(panelCombo);
        panelAlumnos.add(norte, BorderLayout.NORTH);

       /* ---------- Centro: notas + reportes ---------- */
        tablaNotas.setModel(modeloNotas);
        JScrollPane scrollNotas = new JScrollPane(tablaNotas);
        scrollNotas.setBorder(BorderFactory.createTitledBorder("Notas por materia"));

        /* --- nuevo contenedor para la tabla y su etiqueta --- */
        JPanel panelNotasConPromedio = new JPanel(new BorderLayout());
         panelNotasConPromedio.add(scrollNotas, BorderLayout.CENTER);
         panelNotasConPromedio.add(lblPromedioGeneral, BorderLayout.SOUTH);
        
        tablaReportes.setModel(modeloReportes);
        JScrollPane scrollReportes = new JScrollPane(tablaReportes);
        scrollReportes.setBorder(BorderFactory.createTitledBorder("Reportes del alumno"));

        JSplitPane splitCentral = new JSplitPane(JSplitPane.VERTICAL_SPLIT,panelNotasConPromedio,scrollReportes);
        
        splitCentral.setDividerLocation(250);
        splitCentral.setResizeWeight(0.5);
        
        add(splitCentral, BorderLayout.CENTER);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(splitCentral, BorderLayout.CENTER);


        /* ---------- ensamblado ---------- */
        add(scrollRadios, BorderLayout.WEST);
        add(panelAlumnos, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> buscarAlumnoPorNombre());
        comboAlumnos.addActionListener(e -> {
            cargarNotasYPromedio();
            cargarReportesDelAlumno();
        });

        cargarCarreras();
    }

    /* -------------------- CARRERAS -------------------- */
    private void cargarCarreras() {
        panelRadios.removeAll();
        grupoCarreras.clearSelection();

        String sql = "CALL listar_carreras()";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                JRadioButton rb = new JRadioButton(rs.getString("nombre"));
                rb.setAlignmentX(Component.LEFT_ALIGNMENT);
                rb.setOpaque(false);
                grupoCarreras.add(rb);
                panelRadios.add(rb);

                rb.addActionListener(e -> {
                    String carrera = rb.getText();
                    cargarAlumnosPorCarrera(carrera, null);
                });
            }

            if (grupoCarreras.getButtonCount() > 0) {
                JRadioButton primero = (JRadioButton) grupoCarreras.getElements().nextElement();
                primero.setSelected(true);
                cargarAlumnosPorCarrera(primero.getText(), null);
            }

            panelRadios.revalidate();
            panelRadios.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + ex.getMessage());
        }
    }

    /* -------------------- ALUMNOS -------------------- */
    private void cargarAlumnosPorCarrera(String carrera, String filtro) {
        comboAlumnos.removeAllItems();

        String sql = (filtro == null || filtro.trim().isEmpty())
                ? "CALL listar_alumnos_por_carrera(?)"
                : "CALL buscar_alumnos_por_nombre_carrera(?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, carrera);
            if (filtro != null && !filtro.trim().isEmpty()) {
                ps.setString(2, filtro);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    comboAlumnos.addItem(nombre + " " + apellido);
                }
            }

            if (comboAlumnos.getItemCount() == 0) {
                comboAlumnos.addItem("Sin coincidencias");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar alumnos: " + ex.getMessage());
        }
    }

    private void buscarAlumnoPorNombre() {
        String carrera = getCarreraSeleccionada();
        if (carrera == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una carrera primero.");
            return;
        }
        String texto = txtBuscar.getText().trim();
        cargarAlumnosPorCarrera(carrera, texto);
    }

    /* -------------------- NOTAS -------------------- */
    private void cargarNotasYPromedio() {
        modeloNotas.setRowCount(0);
        lblPromedioGeneral.setText("Promedio general: --");

        String alumno = (String) comboAlumnos.getSelectedItem();
        String carreraNombre = getCarreraSeleccionada();

        if (alumno == null || !alumno.contains(" ") || carreraNombre == null) return;

        String nombre = alumno.split(" ")[0];
        String apellido = alumno.split(" ")[1];

        int idAlumno = 0;
        int idCarrera = 0;

        try (Connection conn = ConexionBD.getConnection()) {

            // 1. ID alumno
            String sqlAlumno = "CALL obtener_id_alumno_por_nombre_apellido(?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlAlumno)) {
                ps.setString(1, nombre);
                ps.setString(2, apellido);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) idAlumno = rs.getInt("id");
                }
            }
            if (idAlumno == 0) return;

            // 2. ID carrera
            String sqlCarrera = "CALL id_carrera_por_nombre(?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlCarrera)) {
                ps.setString(1, carreraNombre);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) idCarrera = rs.getInt("id");
                }
            }
            if (idCarrera == 0) return;

            // 3. Notas
            String sqlNotas = "CALL listar_notas_por_alumno_carrera(?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlNotas)) {
                ps.setInt(1, idAlumno);
                ps.setInt(2, idCarrera);
                try (ResultSet rs = ps.executeQuery()) {
                    double sumaTotal = 0;
                    int materias = 0;

                    while (rs.next()) {
                        String materia = rs.getString("nombre_materia");
                        double c1 = rs.getDouble("corte1");
                        double c2 = rs.getDouble("corte2");
                        double c3 = rs.getDouble("corte3");
                        double prom = (c1 + c2 + c3) / 3.0;

                        modeloNotas.addRow(new Object[]{
                                materia,
                                String.format("%.1f", c1),
                                String.format("%.1f", c2),
                                String.format("%.1f", c3),
                                String.format("%.1f", prom)
                        });

                        sumaTotal += prom;
                        materias++;
                    }

                    if (materias > 0) {
                        double promedioGeneral = sumaTotal / materias;
                        lblPromedioGeneral.setText("Promedio general: " + String.format("%.1f", promedioGeneral));
                    }
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar notas: " + ex.getMessage());
        }
    }

    /* -------------------- REPORTES -------------------- */
    private void cargarReportesDelAlumno() {
    modeloReportes.setRowCount(0);

    String alumno = (String) comboAlumnos.getSelectedItem();
    if (alumno == null || !alumno.contains(" ")) return;

    String nombre = alumno.split(" ")[0];
    String apellido = alumno.split(" ")[1];

    int idAlumno = 0;

    try (Connection conn = ConexionBD.getConnection()) {

        // 1. Obtener ID del alumno
        String sqlAlumno = "CALL obtener_id_alumno_por_nombre_apellido(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sqlAlumno)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) idAlumno = rs.getInt("id");
            }
        }
        if (idAlumno == 0) return;

        // 2. Cargar reportes con docente y fecha
        String sqlReportes = "CALL obtener_reportes_con_docente(?)";
        try (PreparedStatement ps = conn.prepareStatement(sqlReportes)) {
            ps.setInt(1, idAlumno);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String reporte = rs.getString("reporte");
                    String docente = rs.getString("docente");
                    Timestamp fecha = rs.getTimestamp("fecha");

                    modeloReportes.addRow(new Object[]{
                            reporte,
                            docente,
                            fecha.toString()
                    });
                }
            }
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al cargar reportes: " + ex.getMessage());
    }
}

    /* -------------------- UTILS -------------------- */
    public String getAlumnoSeleccionado() {
        return (String) comboAlumnos.getSelectedItem();
    }

    public String getCarreraSeleccionada() {
        Enumeration<AbstractButton> buttons = grupoCarreras.getElements();
        while (buttons.hasMoreElements()) {
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
