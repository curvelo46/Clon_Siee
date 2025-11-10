package Vista.frm.Panel;

import Clases.Base_De_Datos;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

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
    
    private static final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
    
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
        jpMaterias.setBorder(BorderFactory.createTitledBorder("Materias"));

        JScrollPane scrollMaterias = new JScrollPane(jpMaterias);
        JPanel centro = new JPanel(new BorderLayout());
        centro.add(scrollCarreras, BorderLayout.NORTH);
        centro.add(scrollMaterias, BorderLayout.CENTER);
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

        cargarCarrerasConMateriasDocente(); // Carga inicial sin alertas
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

    // ✅ NUEVO: Método único con control de alertas
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

    // ✅ Sobrecarga conveniente para llamadas sin filtros (oculta alertas por defecto)
    private void cargarAlumnos(String carrera, String filtro) {
        cargarAlumnos(carrera, filtro, false);
    }

    private void cargarAlumnos() {
        String carrera = getCarreraSeleccionada();
        cargarAlumnos(carrera, null, false); // Sin alertas al inicio
    }

    private void cargarMateriasPorCarrera(int idCarrera) {
        jpMaterias.removeAll();
        ButtonGroup grupoMaterias = new ButtonGroup();

        List<String> materias = baseDatos.obtenerMateriasDocentePorCarrera(profesor, idCarrera);
        
        for (String materia : materias) {
            JRadioButton radio = new JRadioButton(materia);
            grupoMaterias.add(radio);
            jpMaterias.add(radio);
            radio.addActionListener(e -> cargarAlumnos()); // Usa la versión sin alertas
        }
        
        if (grupoMaterias.getButtonCount() > 0) {
            grupoMaterias.getElements().nextElement().setSelected(true);
            cargarAlumnos(); // Sin alertas al cargar materias
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
            rb.addActionListener(e -> cargarMateriasPorCarrera(id)); // Sin alertas al cambiar carrera
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
        comboAlumnos.addActionListener(e -> actualizarCedulaAlumno());
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
        btnSubir.addActionListener(e -> {
            String idText = txtIdAlumno.getText().trim();
            String reporte = txtReporte.getText().trim();

            if (idText.isEmpty() || reporte.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }

            try {
                String seleccion = (String) comboAlumnos.getSelectedItem();
                if (seleccion == null || !alumnoIdMap.containsKey(seleccion)) {
                    JOptionPane.showMessageDialog(this, "Seleccione un alumno válido.");
                    return;
                }
                
                int idAlumno = alumnoIdMap.get(seleccion);
                int idDocente = baseDatos.obtenerIdDocenteDesdeUsuario(profesor);

                if (!baseDatos.alumnoExisteEnTablaAlumnos(idAlumno)) {
                    JOptionPane.showMessageDialog(this, "El alumno no está registrado en la tabla Alumnos.");
                    return;
                }

                boolean exito = baseDatos.insertarReporte(idAlumno, reporte, idDocente);
                
                if (exito) {
                    JOptionPane.showMessageDialog(this, "✅ Reporte guardado correctamente.");
                    txtReporte.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al guardar el reporte.");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error al guardar reporte: " + ex.getMessage());
            }
        });
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
        String carrera = getCarreraSeleccionada();
        if (carrera == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una carrera primero.");
            return;
        }

        String texto = txtBuscar.getText().trim();
        cargarAlumnos(carrera, texto, true); // ✅ Muestra alerta si no encuentra
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
