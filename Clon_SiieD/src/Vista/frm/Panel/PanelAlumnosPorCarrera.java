package Vista.frm.Panel;

import Clases.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Enumeration;
import java.util.List;

public class PanelAlumnosPorCarrera extends JPanel {

    private final ButtonGroup grupoCarreras = new ButtonGroup();
    private final JPanel panelRadios = new JPanel();
    private final JComboBox<String> comboAlumnos = new JComboBox<>();
    private final JTextField txtBuscar = new JTextField(15);
    private final JButton btnBuscar = new JButton("Buscar");
    private String usernameAlumno;
    
    /* ---------- Repositorio de datos ---------- */
    private final Base_De_Datos repo = new Base_De_Datos(); 
    private final AjustesObjetos ajustes = new AjustesObjetos(){}; 

    
    /* ---------- Tablas y modelos ---------- */
    private final JTable tablaNotas = new JTable();
    private final DefaultTableModel modeloNotas = new DefaultTableModel(
        new Object[]{"Materia", "Corte 1", "Corte 2", "Corte 3", "Promedio"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; 
        }
    };

    /* ====== TABLA REPORTES CON ID OCULTO ====== */
    private final JTable tablaReportes = new JTable();
    private final DefaultTableModel modeloReportes = new DefaultTableModel(
        new Object[]{"ID", "Fecha", "Docente", "Materia", "Reporte"}, 0) { 
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JLabel lblPromedioGeneral = new JLabel("Promedio general: --");
    
    
    /* ====== BOTÓN ELIMINAR REPORTE ====== */
    private final JButton btnEliminarReporte = new JButton(" Eliminar Reporte");

    public PanelAlumnosPorCarrera() {
        AjustesObjetos.ajustarTabla(tablaNotas);
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

        
        /* --- contenedor para la tabla de notas y su etiqueta de promedio --- */
        JPanel panelNotasConPromedio = new JPanel(new BorderLayout());
        panelNotasConPromedio.add(scrollNotas, BorderLayout.CENTER);
        panelNotasConPromedio.add(lblPromedioGeneral, BorderLayout.SOUTH);
        
        
        // ✅ Configurar tabla de reportes
        tablaReportes.setModel(modeloReportes);
        
        
        // OCULTAR LA COLUMNA ID (primera columna)
        tablaReportes.removeColumn(tablaReportes.getColumnModel().getColumn(0));
        
        
        JScrollPane scrollReportes = new JScrollPane(tablaReportes);
        scrollReportes.setBorder(BorderFactory.createTitledBorder("Reportes del alumno"));

        
        /* ====== PANEL BOTÓN ELIMINAR ====== */
        JPanel panelBotonEliminar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonEliminar.add(btnEliminarReporte);
        
        JSplitPane splitCentral = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelNotasConPromedio, scrollReportes);
        splitCentral.setDividerLocation(250);
        splitCentral.setResizeWeight(0.5);

        scrollNotas.setMaximumSize(new Dimension(900, 200));
        scrollReportes.setMaximumSize(new Dimension(900, 200));
        scrollNotas.setPreferredSize(new Dimension(900, 180));
        scrollReportes.setPreferredSize(new Dimension(900, 180));
        
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(splitCentral, BorderLayout.CENTER);        
        panelCentral.add(panelBotonEliminar, BorderLayout.SOUTH); 

        
        /* ---------- ensamblado ---------- */
        add(scrollRadios, BorderLayout.WEST);
        add(panelAlumnos, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);  

        
        // ✨ Configurar listeners
        btnBuscar.addActionListener(e -> buscarAlumnoPorNombre());
        comboAlumnos.addActionListener(e -> {
            cargarNotasYPromedio();
            String alumno = (String) comboAlumnos.getSelectedItem();
            if (alumno != null && alumno.contains(" ")) {
                String[] partes = alumno.split(" ", 2);
                int idAlumno = repo.obtenerIdAlumnoPorNombre(partes[0], partes[1]);
                if (idAlumno > 0) {
                    cargarReportesDelAlumno(idAlumno);
                }
            }
        });

        
        /* ====== LISTENER BOTÓN ELIMINAR ====== */
        btnEliminarReporte.addActionListener(e -> eliminarReporteSeleccionado());

        cargarCarreras();
        ajustes.ajustarTabla(tablaNotas);
        
        setMaximumSize(new Dimension(950, 500)); // Límite razonable
        setPreferredSize(new Dimension(950, 500));
        
        splitCentral.setMaximumSize(new Dimension(940, 450));
    }

    
    /* -------------------- CARRERAS -------------------- */
    private void cargarCarreras() {
        panelRadios.removeAll();
        grupoCarreras.clearSelection();
        List<String> carreras = repo.listarCarreras();
        
        if (carreras.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron carreras registradas.");
            panelRadios.revalidate();
            panelRadios.repaint();
            return;
        }

        for (String carrera : carreras) {
            JRadioButton rb = new JRadioButton(carrera);
            rb.setAlignmentX(Component.LEFT_ALIGNMENT);
            rb.setOpaque(false);
            grupoCarreras.add(rb);
            panelRadios.add(rb);

            rb.addActionListener(e -> {
                cargarAlumnosPorCarrera(rb.getText(), null);
                txtBuscar.setText(""); // Limpiar búsqueda al cambiar carrera
            });
        }

        if (grupoCarreras.getButtonCount() > 0) {
            JRadioButton primero = (JRadioButton) grupoCarreras.getElements().nextElement();
            primero.setSelected(true);
            cargarAlumnosPorCarrera(primero.getText(), null);
        }

        panelRadios.revalidate();
        panelRadios.repaint();
    }

    
    /* -------------------- ALUMNOS -------------------- */
    private void cargarAlumnosPorCarrera(String carrera, String filtro) {
        comboAlumnos.removeAllItems();
        List<String> alumnos = repo.listarAlumnosPorCarrera(carrera, filtro);
        
        if (alumnos.isEmpty()) {
            comboAlumnos.addItem("Sin coincidencias");
        } else {
            for (String alumno : alumnos) {
                comboAlumnos.addItem(alumno);
            }
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
        modeloReportes.setRowCount(0); 

        String alumno = (String) comboAlumnos.getSelectedItem();
        String carreraNombre = getCarreraSeleccionada();

        if (alumno == null || !alumno.contains(" ") || carreraNombre == null) return;

        String[] partes = alumno.split(" ", 2);
        if (partes.length < 2) return;
        String nombre = partes[0];
        String apellido = partes[1];

        int idAlumno = repo.obtenerIdAlumnoPorNombre(nombre, apellido);
        int idCarrera = repo.obtenerIdCarreraPorNombre(carreraNombre);
        
        if (idAlumno == 0 || idCarrera == 0) return;

        // Cargar notas
        List<Object[]> notas = repo.listarNotasPorAlumnoCarrera(idAlumno, idCarrera);
        double sumaTotal = 0;
        int materias = 0;

        for (Object[] nota : notas) {
            modeloNotas.addRow(nota);
            Object promedioObj = nota[4];
            if (promedioObj != null) {
                try {
                    double promedio = promedioObj instanceof String ? 
                        Double.parseDouble(((String) promedioObj).replace(",", ".")) : 
                        ((Number) promedioObj).doubleValue();
                    sumaTotal += promedio;
                    materias++;
                } catch (Exception ex) {
                    System.err.println("Error al procesar promedio: " + ex.getMessage());
                }
            }
        }

        if (materias > 0) {
            double promedioGeneral = sumaTotal / materias;
            lblPromedioGeneral.setText("Promedio general: " + String.format("%.1f", promedioGeneral));
        }

    
        cargarReportesDelAlumno(idAlumno);
    }
    
    
    private void cargarReportesDelAlumno(int idAlumno) {
        modeloReportes.setRowCount(0); 
        
        usernameAlumno = repo.obtenerUsernamePorIdAlumno(idAlumno);
        if (usernameAlumno == null) {
            modeloReportes.addRow(new Object[]{-1, "Error: No se encontró el usuario", "", "", ""});
            return;
        }
        
        // ====== IMPORTANTE: Método debe devolver ID como primer elemento ======
        List<Object[]> reportes = repo.obtenerReportesAlumnoConId(usernameAlumno); 
        
        if (reportes.isEmpty()) {
            modeloReportes.addRow(new Object[]{-1, "No hay reportes registrados", "", "", ""});
        } else {
            for (Object[] reporte : reportes) {
                modeloReportes.addRow(reporte); 
            }
        }
    }

    
    
    /* ====== MÉTODO ELIMINAR REPORTE ====== */
    private void eliminarReporteSeleccionado() {
        int filaVista = tablaReportes.getSelectedRow();
        
        if (filaVista == -1) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Por favor, seleccione un reporte para eliminar.", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

       
        int filaModelo = tablaReportes.convertRowIndexToModel(filaVista);
        
        
        Object idObj = modeloReportes.getValueAt(filaModelo, 0);
        
        
        if (!(idObj instanceof Integer) || (Integer) idObj <= 0) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ No se puede eliminar esta fila.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int idReporte = (Integer) idObj;

        // ====== CONFIRMACIÓN ======
        String mensaje = String.format(
            "¿Está seguro de eliminar este reporte?\n\n" +
            "Reporte ID: %d\n" +
            "Esta acción no se puede deshacer.", 
            idReporte
        );
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            mensaje,
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return; 
        }

        try {
            boolean exito = repo.eliminarReportePorId(idReporte);
            
            if (exito) {
                modeloReportes.removeRow(filaModelo);
                JOptionPane.showMessageDialog(this, 
                    "✅ Reporte eliminado correctamente.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ No se pudo eliminar el reporte de la base de datos.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error al eliminar reporte: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    /* -------------------- UTILIDADES -------------------- */
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
