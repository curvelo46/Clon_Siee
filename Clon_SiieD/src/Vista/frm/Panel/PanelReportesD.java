package Vista.frm.Panel;

import Clases.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


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
    
    
    private final DefaultTableModel modeloTablaReportes = new DefaultTableModel(
        new Object[]{"Fecha", "Estudiante", "Materia", "Reporte"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaReportes = new JTable(modeloTablaReportes);
    private final JScrollPane scrollTablaReportes = new JScrollPane(tablaReportes);
    private TableRowSorter<DefaultTableModel> sorter;
    
    
    private JPanel jpCarreras = new JPanel();
    private ButtonGroup grupoCarreras = new ButtonGroup();
    private Map<String, Integer> carreraIdMap = new HashMap<>();
    
    private static final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
    
    public PanelReportesD(Base_De_Datos baseDatos, String profesor) {
        this.baseDatos = baseDatos;
        this.profesor = profesor;
        txtIdAlumno.setEditable(false);

        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(new Color(255, 254, 214));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Norte
        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.add(new JLabel("Estudiante:"));
        norte.add(comboAlumnos);

        jpCarreras.setLayout(new BoxLayout(jpCarreras, BoxLayout.Y_AXIS));
        jpCarreras.setBorder(BorderFactory.createTitledBorder("Carreras"));
        JScrollPane scrollCarreras = new JScrollPane(jpCarreras);
        scrollCarreras.setPreferredSize(new Dimension(300, 100));
        
        // Centro
        jpMaterias.setLayout(new BoxLayout(jpMaterias, BoxLayout.Y_AXIS));
        jpMaterias.setLayout(new BoxLayout(jpMaterias, BoxLayout.Y_AXIS));
        jpMaterias.setBorder(BorderFactory.createTitledBorder("Materias"));

        JScrollPane scrollMaterias = new JScrollPane(jpMaterias);
        
        // Panel para materias y tabla de reportes
        JPanel centro = new JPanel(new BorderLayout());
        centro.add(scrollCarreras, BorderLayout.NORTH);
        centro.add(scrollMaterias, BorderLayout.CENTER);

        scrollTablaReportes.setBorder(BorderFactory.createTitledBorder("Reportes de la Materia"));
        scrollTablaReportes.setPreferredSize(new Dimension(600, 200));
        centro.add(scrollTablaReportes, BorderLayout.SOUTH);
        
        scrollMaterias.setPreferredSize(new Dimension(300, 100));

        
        
        // Sur
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

        this.add(norte, BorderLayout.NORTH);
        this.add(centro, BorderLayout.CENTER);
        this.add(sur, BorderLayout.SOUTH);

        sorter = new TableRowSorter<>(modeloTablaReportes);
        tablaReportes.setRowSorter(sorter);

        cargarCarrerasConMateriasDocente();
        configurarBusquedaDeAlumnos();
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

    private void cargarReportesDeMateria() {
        modeloTablaReportes.setRowCount(0); 
        
        String materia = getMateriaSeleccionada();
        String carrera = getCarreraSeleccionada();
        
        if (materia == null || carrera == null) return;
        
        int idCarrera = carreraIdMap.get(carrera);
        int docenteMateriaId = baseDatos.obtenerDocenteMateriaId(profesor, materia, idCarrera);
        
        if (docenteMateriaId == -1) return;
        
        String sql = "call Reportes_para_Docente(?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, docenteMateriaId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Timestamp fecha = rs.getTimestamp("fecha");
                String estudiante = rs.getString("estudiante");
                String nombreMateria = rs.getString("materia");
                String reporte = rs.getString("reporte");
                
                modeloTablaReportes.addRow(new Object[]{
                    fecha != null ? sdf.format(fecha) : "",
                    estudiante,
                    nombreMateria,
                    reporte
                });
            }
            
            aplicarFiltroAlumno();
            
        } catch (Exception e) {
            System.err.println("Error al cargar reportes de materia: " + e.getMessage());
        }
    }

    
    private void aplicarFiltroAlumno() {
        String alumnoSeleccionado = (String) comboAlumnos.getSelectedItem();
        
        if (alumnoSeleccionado == null || alumnoSeleccionado.equals("Sin coincidencias")) {
            sorter.setRowFilter(null); 
        } else {
            // Filtrar por la columna "Estudiante" (índice 1)
            RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("(?i)" + alumnoSeleccionado, 1);
            sorter.setRowFilter(rf);
        }
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

    private void cargarAlumnos(String carrera, String filtro, boolean mostrarAlertas) {
        String materia = getMateriaSeleccionada();
        if (materia == null) return;

        int idCarrera = baseDatos.obtenerCarreraIdDesdeMateria(materia);
        if (idCarrera == 0) {
            if (mostrarAlertas) {
                JOptionPane.showMessageDialog(this, "Error al obtener carrera de la materia.");
            }
            return;
        }

        comboAlumnos.removeAllItems();
        alumnoIdMap.clear();

        if (filtro == null || filtro.trim().isEmpty()) {
            List<Object[]> estudiantes = baseDatos.listarEstudiantesPorDocenteMateria(profesor, materia, idCarrera);
            
            if (estudiantes.isEmpty()) {
                if (mostrarAlertas) {
                    JOptionPane.showMessageDialog(this, "No hay estudiantes registrados en esta materia.");
                }
            } else {
                for (Object[] fila : estudiantes) {
                    String nombreCompleto = fila[1] + " " + fila[2];
                    int idAlumno = (Integer) fila[6];
                    comboAlumnos.addItem(nombreCompleto);
                    alumnoIdMap.put(nombreCompleto, idAlumno);
                }
            }
        } else {
            Map<String, Integer> estudiantes = baseDatos.buscarAlumnosPorNombreCarrera(carrera, filtro);
            
            if (estudiantes.isEmpty()) {
                if (mostrarAlertas) {
                    JOptionPane.showMessageDialog(this, "No se encontraron estudiantes con el criterio de búsqueda.");
                }
            } else {
                for (Map.Entry<String, Integer> entry : estudiantes.entrySet()) {
                    comboAlumnos.addItem(entry.getKey());
                    alumnoIdMap.put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (comboAlumnos.getItemCount() > 0) {
            comboAlumnos.setSelectedIndex(0);
            actualizarCedulaAlumno();
        }
    }

     
    private void cargarAlumnos() {
        String carrera = getCarreraSeleccionada();
        cargarAlumnos(carrera, null, false); 
    }

     private void cargarMateriasPorCarrera(int idCarrera) {
        jpMaterias.removeAll();
        ButtonGroup grupoMaterias = new ButtonGroup();

        List<String> materias = baseDatos.obtenerMateriasDocentePorCarrera(profesor, idCarrera);
        
        for (String materia : materias) {
            JRadioButton radio = new JRadioButton(materia);
            grupoMaterias.add(radio);
            jpMaterias.add(radio);
            radio.addActionListener(e -> {
                cargarAlumnos(); 
                cargarReportesDeMateria(); 
            });
        }
        
        if (grupoMaterias.getButtonCount() > 0) {
            grupoMaterias.getElements().nextElement().setSelected(true);
            cargarAlumnos();
            cargarReportesDeMateria(); 
        }
        
        jpMaterias.revalidate();
        jpMaterias.repaint();
    }

    private void cargarCarrerasConMateriasDocente() {
        jpCarreras.removeAll();
        grupoCarreras.clearSelection();
        carreraIdMap.clear();

        Map<String, Integer> carreras = baseDatos.obtenerCarrerasConMateriasDocente(profesor);
        
        for (Map.Entry<String, Integer> entry : carreras.entrySet()) {
            String nombre = entry.getKey();
            int id = entry.getValue();
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
        
        jpCarreras.revalidate();
        jpCarreras.repaint();
    }

    private void configurarComboAlumnos() {
        comboAlumnos.addActionListener(e -> {
            actualizarCedulaAlumno();
            aplicarFiltroAlumno();
        });
    }

    private void actualizarCedulaAlumno() {
        String seleccion = (String) comboAlumnos.getSelectedItem();
        if (seleccion != null && alumnoIdMap.containsKey(seleccion)) {
            int id = alumnoIdMap.get(seleccion);
            String cedula = baseDatos.obtenerCedulaDesdeId(id);
            txtIdAlumno.setText(cedula != null ? cedula : "");
        } else {
            txtIdAlumno.setText("");
        }
    }

    private void configurarBotonSubir() {
        btnSubir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reporte = txtReporte.getText().trim();
                String alumno = (String) comboAlumnos.getSelectedItem();
                
                if (reporte.isEmpty()) {
                    mostrarAdvertencia("El reporte no puede estar vacío");
                    return;
                }
                
                if (alumno == null || !alumnoIdMap.containsKey(alumno)) {
                    mostrarAdvertencia("Seleccione un alumno válido");
                    return;
                }
                
                try {
                    int idAlumno = alumnoIdMap.get(alumno);
                    int idDocente = baseDatos.obtenerIdDocenteDesdeUsuario(profesor);
                    
                    String carrera = getCarreraSeleccionada();
                    String materia = getMateriaSeleccionada();
                    
                    if (carrera == null || materia == null) {
                        mostrarAdvertencia("Seleccione carrera y materia");
                        return;
                    }
                    
                    int idCarrera = carreraIdMap.get(carrera);
                    int docenteMateriaId = baseDatos.obtenerDocenteMateriaId(profesor, materia, idCarrera);
                    
                    if (docenteMateriaId == -1) {
                        mostrarError("No se pudo obtener la relación docente-materia");
                        return;
                    }
                    
                    if (!baseDatos.alumnoExisteEnTablaAlumnos(idAlumno)) {
                        mostrarError("El alumno no está registrado correctamente");
                        return;
                    }
                    
                    boolean exito = baseDatos.insertarReporte(idAlumno, reporte, idDocente, docenteMateriaId);
                    
                    if (exito) {
                        mostrarInformacion("✅ Reporte guardado correctamente");
                        txtReporte.setText("");
                        cargarReportesDeMateria();
                    } else {
                        mostrarError("❌ Error al guardar el reporte");
                    }
                    
                } catch (Exception r ) {
                    mostrarError("Error al procesar reporte: " + r.getMessage());
                }
            }
        });
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    
    private void mostrarInformacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
    
    
    private void configurarBusquedaDeAlumnos() {
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar alumno:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

        JPanel norteOriginal = (JPanel) this.getComponent(0);
        norteOriginal.add(panelBusqueda, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarAlumnoPorNombre());
    }
 
   private void buscarAlumnoPorNombre() {
    String texto = txtBuscar.getText().trim();
    if (texto.isEmpty()) {
        mostrarAdvertencia("Ingrese un nombre o apellido para buscar.");
        return;
    }
    
    // ✅ VALIDAR QUE HAYA MATERIA SELECCIONADA
    String materia = getMateriaSeleccionada();
    if (materia == null) {
        mostrarAdvertencia("Seleccione una materia primero.");
        return;
    }
    
    // ✅ OBTENER LA CARRERA SELECCIONADA
    String carrera = getCarreraSeleccionada();
    if (carrera == null) {
        mostrarAdvertencia("Seleccione una carrera primero.");
        return;
    }
    
    int idCarrera = carreraIdMap.get(carrera);
    
    Map<String, Integer> estudiantes = baseDatos.buscarAlumnosPorMateriaCarreraDocente(profesor, materia, idCarrera, texto);
    
    comboAlumnos.removeAllItems();
    alumnoIdMap.clear();
    
    if (estudiantes.isEmpty()) {
        mostrarAdvertencia("No se encontraron estudiantes en '" + materia + "' con: " + texto);
        return;
    }
    
    for (Map.Entry<String, Integer> entry : estudiantes.entrySet()) {
        comboAlumnos.addItem(entry.getKey());
        alumnoIdMap.put(entry.getKey(), entry.getValue());
    }
    
    comboAlumnos.setSelectedIndex(0);
}

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
