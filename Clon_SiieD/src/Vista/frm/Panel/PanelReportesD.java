package Vista.frm.Panel;

import Clases.Base_De_Datos;
import Clases.ConexionBD;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PanelReportesD extends JPanel {
    private final Base_De_Datos baseDatos;
    private final String profesor;

    private JComboBox<String> comboAlumnos = new JComboBox<>();
    private JPanel jpMaterias = new JPanel();
    private JTextField txtReporte = new JTextField(20);
    private JTextField txtIdAlumno = new JTextField(10);
    private JButton btnSubir = new JButton("Subir reporte");
    private final JTextField txtBuscar = new JTextField(15);
    private final JButton btnBuscar = new JButton("Buscar");
    private Map<String, Integer> alumnoIdMap = new HashMap<>();
    
    private JPanel jpCarreras = new JPanel();
    private ButtonGroup grupoCarreras = new ButtonGroup();
    private Map<String, Integer> carreraIdMap = new HashMap<>();
    
    public PanelReportesD(Base_De_Datos basedatos, String profesor) {
        this.baseDatos = basedatos;
        this.profesor = profesor;
        txtIdAlumno.setEditable(false);

        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(new Color(255, 254, 214));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Norte: alumno
        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.add(new JLabel("Estudiante:"));
        norte.add(comboAlumnos);

        jpCarreras.setLayout(new BoxLayout(jpCarreras, BoxLayout.Y_AXIS));
        jpCarreras.setBorder(BorderFactory.createTitledBorder("Carreras"));
        JScrollPane scrollCarreras = new JScrollPane(jpCarreras);
        scrollCarreras.setPreferredSize(new Dimension(300, 100));
        
        // Centro: materias
        jpMaterias.setLayout(new BoxLayout(jpMaterias, BoxLayout.Y_AXIS));
        jpMaterias.setBorder(BorderFactory.createTitledBorder("Materias"));

        JScrollPane scrollMaterias = new JScrollPane(jpMaterias);
        JPanel centro = new JPanel(new BorderLayout());
        centro.add(scrollCarreras, BorderLayout.NORTH);  // primero carreras
        centro.add(scrollMaterias, BorderLayout.CENTER);
        scrollMaterias.setPreferredSize(new Dimension(300, 100));
        

        // Sur: ID, reporte y botón
        JPanel sur = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        sur.add(new JLabel("Cc Alumno:"), gbc);

        gbc.gridx = 1;
        sur.add(txtIdAlumno, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        sur.add(new JLabel("Reporte:"), gbc);

        gbc.gridx = 1;
        sur.add(txtReporte, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        sur.add(btnSubir, gbc);

        // Agregar todo
        this.add(norte, BorderLayout.NORTH);
        this.add(centro, BorderLayout.CENTER);  // <-- cambiado
        this.add(sur, BorderLayout.SOUTH);

        cargarCarrerasConMateriasDocente();  // <-- nuevo
        configurarBusquedaDeAlumnos();
        cargarMateriasComoRadioButtons();    // se llena tras elegir carrera
        configurarComboAlumnos();
        configurarBotonSubir();
    }

    /* -------------------- MÉTODOS -------------------- */
    private String getMateriaSeleccionada() {
        for (Component comp : jpMaterias.getComponents()) {
            if (comp instanceof JRadioButton) {
                JRadioButton radio = (JRadioButton) comp;
                if (radio.isSelected()) {
                    return radio.getText();
                }
            }
        }
        return null;
    }

    private void cargarAlumnos() {
        comboAlumnos.removeAllItems();
        alumnoIdMap.clear();

        String materia = getMateriaSeleccionada();
        if (materia == null) return;

        String sql = "CALL listar_estudiantes_por_docente_materia(?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, profesor);
            stmt.setString(2, materia);

            // Obtener id_carrera desde la materia
            int idCarrera = obtenerIdCarreraDesdeMateria(materia);
            stmt.setInt(3, idCarrera);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String display = nombre + " " + apellido;
                    comboAlumnos.addItem(display);
                    alumnoIdMap.put(display, id);
                }
            }

            if (comboAlumnos.getItemCount() > 0) {
                comboAlumnos.setSelectedIndex(0);
                actualizarCedulaAlumno();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar alumnos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int obtenerIdCarreraDesdeMateria(String materia) {
        int idCarrera = 0;
        String sql = "SELECT c.id FROM Materias m JOIN Carreras c ON c.id = m.carrera_id WHERE m.nombre = ? LIMIT 1";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, materia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) idCarrera = rs.getInt("id");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener carrera: " + ex.getMessage());
        }
        return idCarrera;
    }
    
    private void cargarMateriasPorCarrera(int idCarrera) {
    jpMaterias.removeAll();
    ButtonGroup grupoMaterias = new ButtonGroup();

    String sql = "CALL obtener_materias_docente_por_carrera(?, ?)";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, profesor);
        ps.setInt(2, idCarrera);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String materia = rs.getString("materia");
                JRadioButton radio = new JRadioButton(materia);
                grupoMaterias.add(radio);
                jpMaterias.add(radio);
                radio.addActionListener(e -> cargarAlumnos());
            }
            if (grupoMaterias.getButtonCount() > 0) {
                grupoMaterias.getElements().nextElement().setSelected(true);
                cargarAlumnos();
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar materias: " + e.getMessage());
        e.printStackTrace();
    }
    jpMaterias.revalidate();
    jpMaterias.repaint();
}
    
    private void cargarCarrerasConMateriasDocente() {
    jpCarreras.removeAll();
    grupoCarreras.clearSelection();
    carreraIdMap.clear();

    String sql = "CALL obtener_carreras_con_materias_docente(?)";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, profesor);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                JRadioButton rb = new JRadioButton(nombre);
                grupoCarreras.add(rb);
                jpCarreras.add(rb);
                carreraIdMap.put(nombre, id);
                rb.addActionListener(e -> cargarMateriasPorCarrera(id));
            }
            if (grupoCarreras.getButtonCount() > 0) {
                grupoCarreras.getElements().nextElement().setSelected(true);
                int primero = carreraIdMap.values().iterator().next();
                cargarMateriasPorCarrera(primero);
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + e.getMessage());
        e.printStackTrace();
    }
    jpCarreras.revalidate();
    jpCarreras.repaint();
}
    
    private void cargarMateriasComoRadioButtons() {
        jpMaterias.removeAll();

        String sql = "CALL obtener_materias_docente(?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, profesor);
            try (ResultSet rs = stmt.executeQuery()) {
                ButtonGroup grupo = new ButtonGroup();
                while (rs.next()) {
                    String materia = rs.getString("nombre");
                    JRadioButton radio = new JRadioButton(materia);
                    radio.setActionCommand(materia);
                    grupo.add(radio);
                    jpMaterias.add(radio);
                    radio.addActionListener(e -> cargarAlumnos());
                }

                if (grupo.getButtonCount() > 0) {
                    grupo.getElements().nextElement().setSelected(true);
                    cargarAlumnos();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar materias: " + e.getMessage());
            e.printStackTrace();
        }

        jpMaterias.revalidate();
        jpMaterias.repaint();
    }
    
    private boolean alumnoExisteEnTablaAlumnos(int idAlumno) {
    String sql = "SELECT 1 FROM Alumnos WHERE id = ? LIMIT 1";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idAlumno);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
    
    private void configurarComboAlumnos() {
        comboAlumnos.addActionListener(e -> actualizarCedulaAlumno());
    }

    private void actualizarCedulaAlumno() {
        String seleccion = (String) comboAlumnos.getSelectedItem();
        if (seleccion != null && alumnoIdMap.containsKey(seleccion)) {
            int id = alumnoIdMap.get(seleccion);
            String cedula = obtenerCedulaDesdeId(id);
            txtIdAlumno.setText(cedula);
        } else {
            txtIdAlumno.setText("");
        }
    }

    private void configurarBotonSubir() {
        btnSubir.addActionListener(e -> {
            String idText = txtIdAlumno.getText().trim();
            String reporte = txtReporte.getText().trim();

            if (idText.isEmpty() || reporte.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }

            try {
                int idAlumno = Integer.parseInt("1");
                int idDocente = obtenerIdDocenteDesdeUsuario(profesor);

                
                if (!alumnoExisteEnTablaAlumnos(idAlumno)) {
                    JOptionPane.showMessageDialog(this, "El alumno no está registrado en la tabla Alumnos.");
                    return;
}
                
                
                
                String sql = "CALL insertar_reporte(?, ?, ?)";
                try (Connection conn = ConexionBD.getConnection();
                     CallableStatement stmt = conn.prepareCall(sql)) {
                    stmt.setInt(1, idAlumno);
                    stmt.setString(2, reporte);
                    stmt.setInt(3, idDocente);
                    stmt.execute();
                    JOptionPane.showMessageDialog(this, "✅ Reporte guardado correctamente.");
                    txtReporte.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error al guardar reporte: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    private int obtenerIdDocenteDesdeUsuario(String usuario) {
        int idDocente = 0;
        String sql = "CALL obtener_id_docente_por_user(?)";
        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, usuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) idDocente = rs.getInt("id");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener ID del docente: " + ex.getMessage());
        }
        return idDocente;
    }

    private String obtenerCedulaDesdeId(int idAlumno) {
        String cedula = "";
        String sql = "CALL obtener_cedula(?)";
        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAlumno);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) cedula = rs.getString("cc");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cedula;
    }
    
    private void configurarBusquedaDeAlumnos() {
    JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelBusqueda.add(new JLabel("Buscar alumno:"));
    panelBusqueda.add(txtBuscar);
    panelBusqueda.add(btnBuscar);

    // Insertar al norte, antes del combo de alumnos
    JPanel norteOriginal = (JPanel) this.getComponent(0); // Norte
    norteOriginal.add(panelBusqueda, BorderLayout.SOUTH);

    btnBuscar.addActionListener(e -> buscarAlumnoPorNombre());
}
 
    
    private String getCarreraSeleccionada() {
    for (Component comp : jpCarreras.getComponents()) {
        if (comp instanceof JRadioButton) {
            JRadioButton radio = (JRadioButton) comp;
            if (radio.isSelected()) {
                return radio.getText();
            }
        }
    }
    return null;
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
    
    private void cargarAlumnosPorCarrera(String carrera, String filtro) {
    comboAlumnos.removeAllItems(); // ← Limpia antes de llenar
    alumnoIdMap.clear();           // ← Limpia el mapa también

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
                int id = rs.getInt("alumno_id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String display = nombre + " " + apellido;
                comboAlumnos.addItem(display);
                alumnoIdMap.put(display, id);
            }
        }

        if (comboAlumnos.getItemCount() > 0) {
            comboAlumnos.setSelectedIndex(0);
            actualizarCedulaAlumno();
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al cargar alumnos: " + ex.getMessage());
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

        JpMaterias = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        javax.swing.GroupLayout JpMateriasLayout = new javax.swing.GroupLayout(JpMaterias);
        JpMaterias.setLayout(JpMateriasLayout);
        JpMateriasLayout.setHorizontalGroup(
            JpMateriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
        );
        JpMateriasLayout.setVerticalGroup(
            JpMateriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel1.setText("Reporte");

        jLabel2.setText("Cc Alumno");

        jButton1.setText("subir reporte");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 123, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(JpMaterias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(25, 74, Short.MAX_VALUE))
            .addComponent(JpMaterias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JpMaterias;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
