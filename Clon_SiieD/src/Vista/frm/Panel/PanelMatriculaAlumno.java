package Vista.frm.Panel;

import Clases.Base_De_Datos;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.text.Normalizer; 

public class PanelMatriculaAlumno extends JPanel {

    private final Base_De_Datos baseDatos = new Base_De_Datos();
    private final JComboBox<String> comboAlumnos = new JComboBox<>();
    private final JComboBox<String> comboCarreras = new JComboBox<>();
    private final JPanel panelMaterias = new JPanel();
    private final JCheckBox checkTodas = new JCheckBox("Seleccionar todas las materias");
    private final List<JCheckBox> checkMaterias = new ArrayList<>();
    private final JButton btnMatricular = new JButton("Matricular");
    private final JButton btnRetirar = new JButton("Retirar");
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JLabel lblInfo = new JLabel("Seleccione alumno, carrera y materia(s)");
    private final JTextField txtBuscarAlumno = new JTextField(15);
    private final JButton btnBuscarAlumno = new JButton("Buscar");
    
    public PanelMatriculaAlumno() {
        inicializarComponentes();
        configurarEventos();
        cargarDatosIniciales();
        
    }

    // ===== INICIALIZACIÓN =====
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Matricular / Retirar Alumno"));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelMaterias(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelSuperior() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
    // Fila Alumno
    JPanel filaAlumno = new JPanel(new FlowLayout(FlowLayout.LEFT));
    filaAlumno.add(new JLabel("Alumno:"));
    filaAlumno.add(comboAlumnos);
    panel.add(filaAlumno);
    
    // Fila Búsqueda
    JPanel filaBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
    filaBusqueda.add(new JLabel("Buscar alumno:"));
    filaBusqueda.add(txtBuscarAlumno);
    filaBusqueda.add(btnBuscarAlumno);
    panel.add(filaBusqueda);
    
    // Fila Carrera
    JPanel filaCarrera = new JPanel(new FlowLayout(FlowLayout.LEFT));
    filaCarrera.add(new JLabel("Carrera:"));
    filaCarrera.add(comboCarreras);
    panel.add(filaCarrera);
    
    return panel;
}

    private JScrollPane crearPanelMaterias() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBorder(BorderFactory.createTitledBorder("Materias"));
        
        contenedor.add(checkTodas, BorderLayout.NORTH);
        contenedor.add(panelMaterias, BorderLayout.CENTER);
        
        panelMaterias.setLayout(new BoxLayout(panelMaterias, BoxLayout.Y_AXIS));
        
        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setPreferredSize(new Dimension(400, 200));
        return scroll;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(btnMatricular);
        panel.add(btnRetirar);
        panel.add(btnActualizar);
        panel.add(lblInfo);
        return panel;
    }

    private void configurarEventos() {
        comboCarreras.addActionListener(e -> cargarMateriasPorCarrera());
        comboAlumnos.addActionListener(e -> lblInfo.setText("Seleccione acción"));
        btnMatricular.addActionListener(e -> matricularAlumno());
        btnRetirar.addActionListener(e -> retirarAlumno());
        btnActualizar.addActionListener(e -> cargarDatosIniciales());
        checkTodas.addActionListener(e -> seleccionarTodasLasMaterias());

        // **NUEVO**: Eventos de búsqueda
        btnBuscarAlumno.addActionListener(e -> buscarAlumnos());
        txtBuscarAlumno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    buscarAlumnos();
                }
            }
        });
    }
    
    
    private String normalizarTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) return "";
        // Descompone caracteres en base + diacrítico
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD);
        // Elimina los diacríticos (tildes)
        sinTildes = sinTildes.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        // Convierte a minúsculas para comparación case-insensitive
        return sinTildes.toLowerCase();
    }
    
    private void cargarAlumnosConFiltro(String filtro) {
    limpiarCombo(comboAlumnos);
    try {
        List<String> alumnos = baseDatos.listarAlumnosSistema();
        
        
        String filtroNormalizado = normalizarTexto(filtro);
        
        for (String alumno : alumnos) {
            
            String alumnoNormalizado = normalizarTexto(alumno);
            
            
            if (filtro == null || filtro.trim().isEmpty() || 
                alumnoNormalizado.contains(filtroNormalizado)) {
                comboAlumnos.addItem(alumno);
            }
        }
        
        
        if (comboAlumnos.getItemCount() == 0) {
            lblInfo.setText(filtro != null && !filtro.trim().isEmpty() 
                ? "No se encontraron coincidencias para '" + filtro + "'" 
                : "No hay alumnos disponibles");
        }
        
    } catch (Exception ex) {
        mostrarError("Error al cargar alumnos: " + ex.getMessage());
    }
}
    
    // ===== CARGA DE DATOS =====
    private void cargarDatosIniciales() {
        cargarAlumnos();
        cargarCarreras();
        cargarMateriasPorCarrera();
    }

    private void cargarAlumnos() {
    cargarAlumnosConFiltro(null); 
}



private void buscarAlumnos() {
    String textoBusqueda = txtBuscarAlumno.getText().trim();
    cargarAlumnosConFiltro(textoBusqueda);
    
    if (comboAlumnos.getItemCount() == 0 && !textoBusqueda.isEmpty()) {
        lblInfo.setText("❌ No se encontraron alumnos con '" + textoBusqueda + "'");
    } else if (comboAlumnos.getItemCount() > 0) {
        lblInfo.setText("✅ Se encontraron " + comboAlumnos.getItemCount() + " alumno(s)");
    }
}
 
private void cargarCarreras() {
        limpiarCombo(comboCarreras);
        try {
            List<String> carreras = baseDatos.listarCarreras();
            for (String carrera : carreras) {
                comboCarreras.addItem(carrera);
            }
        } catch (Exception ex) {
            mostrarError("Error al cargar carreras: " + ex.getMessage());
        }
    }

    private void limpiarCombo(JComboBox<String> combo) {
        combo.removeAllItems();
        combo.setEnabled(true);
    }

    private void cargarMateriasPorCarrera() {
        limpiarPanelMaterias();
        
        String carrera = getCarreraSeleccionada();
        if (carrera == null) {
            lblInfo.setText("No hay carreras disponibles");
            checkTodas.setEnabled(false);
            return;
        }

        try {
            List<Object[]> materias = baseDatos.listarMateriasPorCarreraConDocente(carrera);
            
            if (materias.isEmpty()) {
                lblInfo.setText("No hay materias disponibles para esta carrera");
                checkTodas.setEnabled(false);
                return;
            }

            for (Object[] materia : materias) {
                agregarCheckboxMateria(materia);
            }

            checkTodas.setEnabled(true);
            panelMaterias.revalidate();
            panelMaterias.repaint();
            
        } catch (Exception ex) {
            mostrarError("Error al cargar materias: " + ex.getMessage());
        }
    }

    private void limpiarPanelMaterias() {
        panelMaterias.removeAll();
        checkMaterias.clear();
        checkTodas.setSelected(false);
    }

    private void agregarCheckboxMateria(Object[] materia) {
        try {
            int dmId = (Integer) materia[0];
            String materiaNombre = (String) materia[1];
            String docenteNombre = (String) materia[2];
            
            String displayText = String.format("%s - Prof. %s", 
                materiaNombre, 
                docenteNombre != null ? docenteNombre : "Sin asignar");
            
            JCheckBox check = new JCheckBox(displayText);
            check.setActionCommand(String.valueOf(dmId));
            checkMaterias.add(check);
            panelMaterias.add(check);
        } catch (Exception e) {
            System.err.println("Error al crear checkbox: " + e.getMessage());
        }
    }

    private void seleccionarTodasLasMaterias() {
        boolean seleccionar = checkTodas.isSelected();
        for (JCheckBox check : checkMaterias) {
            check.setSelected(seleccionar);
        }
    }

    // ===== OPERACIONES =====
    private void matricularAlumno() {
        String alumno = getAlumnoSeleccionado();
        if (alumno == null) {
            mostrarAdvertencia("Seleccione un alumno");
            return;
        }

        List<Integer> materiasIds = getMateriasSeleccionadas();
        if (materiasIds.isEmpty()) {
            mostrarAdvertencia("Seleccione al menos una materia");
            return;
        }

        try {
            int idAlumno = baseDatos.obtenerIdAlumnoPorNombreCompleto(alumno);
            if (idAlumno == 0) {
                mostrarError("Alumno no encontrado");
                return;
            }

            int matriculadas = 0, existentes = 0, errores = 0;
            
            for (int dmId : materiasIds) {
                int resultado = baseDatos.matricularAlumnoEnMateria(idAlumno, dmId);
                switch (resultado) {
                    case 1: matriculadas++; break;
                    case 0: existentes++; break;
                    case -1: errores++; break;
                }
            }

            mostrarResultadoOperacion(matriculadas, existentes, errores);
            
        } catch (Exception ex) {
            mostrarError("Error al matricular: " + ex.getMessage());
        }
    }

    private void retirarAlumno() {
        String alumno = getAlumnoSeleccionado();
        if (alumno == null) {
            mostrarAdvertencia("Seleccione un alumno");
            return;
        }

        List<Integer> materiasIds = getMateriasSeleccionadas();
        if (materiasIds.isEmpty()) {
            mostrarAdvertencia("Seleccione al menos una materia");
            return;
        }

        try {
            int idAlumno = baseDatos.obtenerIdAlumnoPorNombreCompleto(alumno);
            if (idAlumno == 0) {
                mostrarError("Alumno no encontrado");
                return;
            }

            int retiradas = 0, noEncontradas = 0;
            
            for (int dmId : materiasIds) {
                int resultado = baseDatos.retirarAlumnoDeMateria(idAlumno, dmId);
                if (resultado == 1) {
                    retiradas++;
                } else {
                    noEncontradas++;
                }
            }

            String mensaje = String.format("Retirado de materia selecionada(s).", 
                retiradas, 
                noEncontradas > 0 ? " " + noEncontradas + " no estaba matriculado." : "");
            
            mostrarMensaje(mensaje, retiradas > 0 ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
            lblInfo.setText(mensaje);
            
        } catch (Exception ex) {
            mostrarError("Error al retirar: " + ex.getMessage());
        }
    }

    // ===== GETTERS Y VALIDACIONES =====
    private String getAlumnoSeleccionado() {
        return comboAlumnos.getSelectedItem() != null ? 
               comboAlumnos.getSelectedItem().toString() : null;
    }

    private String getCarreraSeleccionada() {
        return comboCarreras.getSelectedItem() != null ? 
               comboCarreras.getSelectedItem().toString() : null;
    }

    private List<Integer> getMateriasSeleccionadas() {
        List<Integer> ids = new ArrayList<>();
        
        if (checkTodas.isSelected()) {
            for (JCheckBox check : checkMaterias) {
                try {
                    ids.add(Integer.parseInt(check.getActionCommand()));
                } catch (NumberFormatException e) {
                    
                }
            }
        } else {
            for (JCheckBox check : checkMaterias) {
                if (check.isSelected()) {
                    try {
                        ids.add(Integer.parseInt(check.getActionCommand()));
                    } catch (NumberFormatException e) {
                        // Ignorar si no es número válido
                        System.out.println("error del sistema: " );
                    }
                }
            }
        }
        
        return ids;
    }

    // ===== MENSAJES =====
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarMensaje(String mensaje, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, "Resultado", tipo);
    }

    private void mostrarResultadoOperacion(int matriculadas, int existentes, int errores) {
        StringBuilder mensaje = new StringBuilder();
        
        if (matriculadas > 0) {
            mensaje.append("✅ Matriculado en ").append(matriculadas).append(" materia(s).\n");
        }
        if (existentes > 0) {
            mensaje.append("⚠️ Ya estaba matriculado en esta materia");
        }
        if (errores > 0) {
            mensaje.append("❌ Error en ").append(errores).append(" materia(s).");
        }
        
        int tipo = errores > 0 ? JOptionPane.ERROR_MESSAGE : 
                   matriculadas > 0 ? JOptionPane.INFORMATION_MESSAGE : 
                   JOptionPane.WARNING_MESSAGE;
        
        mostrarMensaje(mensaje.toString(), tipo);
        lblInfo.setText(mensaje.toString().replace("\n", " | "));
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
