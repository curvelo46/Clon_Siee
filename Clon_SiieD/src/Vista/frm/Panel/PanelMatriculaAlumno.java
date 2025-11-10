package Vista.frm.Panel;

import Clases.Base_De_Datos;
import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.List;

public class PanelMatriculaAlumno extends JPanel {

    private final Base_De_Datos baseDatos = new Base_De_Datos();
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

    // Cargar alumnos desde Base_De_Datos
    private void cargarAlumnos() {
        comboAlumnos.removeAllItems();
        try {
            List<String> alumnos = baseDatos.listarAlumnosSistema();
            for (String alumno : alumnos) {
                comboAlumnos.addItem(alumno);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar alumnos", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cargar carreras desde Base_De_Datos
    private void cargarCarreras() {
        comboCarreras.removeAllItems();
        try {
            List<String> carreras = baseDatos.listarCarreras();
            for (String carrera : carreras) {
                comboCarreras.addItem(carrera);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar carreras", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cargar materias con docentes desde Base_De_Datos
    private void cargarMateriasPorCarrera() {
        panelMaterias.removeAll();
        
        Enumeration<AbstractButton> buttons = grupoMaterias.getElements();
        while (buttons.hasMoreElements()) {
            grupoMaterias.remove(buttons.nextElement());
        }

        String carrera = (String) comboCarreras.getSelectedItem();
        if (carrera == null) return;

        try {
            List<Object[]> materias = baseDatos.listarMateriasPorCarreraConDocente(carrera);
            
            JRadioButton rbTodas = new JRadioButton("Todas las materias");
            rbTodas.setActionCommand("TODAS");
            grupoMaterias.add(rbTodas);
            panelMaterias.add(rbTodas);

            for (Object[] materia : materias) {
                int dmId = (Integer) materia[0];
                String materiaNombre = (String) materia[1];
                String docenteNombre = (String) materia[2];
                
                String displayText = materiaNombre + " - Prof. " + docenteNombre;
                JRadioButton rb = new JRadioButton(displayText);
                rb.setActionCommand(String.valueOf(dmId));
                grupoMaterias.add(rb);
                panelMaterias.add(rb);
            }

            if (grupoMaterias.getButtonCount() > 0) {
                ((JRadioButton)grupoMaterias.getElements().nextElement()).setSelected(true);
            }

            panelMaterias.revalidate();
            panelMaterias.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar materias", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Matricular alumno usando Base_De_Datos
    private void matricularAlumno() {
        String alumno = (String) comboAlumnos.getSelectedItem();
        String seleccion = getSelectedMateria();

        if (alumno == null || seleccion == null) {
            JOptionPane.showMessageDialog(this, "Seleccione alumno y materia");
            return;
        }

        try {
            int idAlumno = baseDatos.obtenerIdAlumnoPorNombreCompleto(alumno);
            if (idAlumno == 0) {
                JOptionPane.showMessageDialog(this, "Error de sistema: Alumno no encontrado", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder mensaje = new StringBuilder();
            boolean hayExito = false;
            boolean hayErrores = false;

            if ("TODAS".equals(seleccion)) {
                int matriculadas = 0;
                int yaExistentes = 0;
                int errores = 0;

                for (Component c : panelMaterias.getComponents()) {
                    if (c instanceof JRadioButton) {
                        JRadioButton rb = (JRadioButton) c;
                        String cmd = rb.getActionCommand();
                        if (!"TODAS".equals(cmd) && rb.isSelected()) {
                            int dmId = Integer.parseInt(cmd);
                            int resultado = baseDatos.matricularAlumnoEnMateria(idAlumno, dmId);
                            
                            switch (resultado) {
                                case 1: matriculadas++; hayExito = true; break;
                                case 0: yaExistentes++; break;
                                case -1: errores++; hayErrores = true; break;
                            }
                        }
                    }
                }
                
                construirMensajeMatricula(mensaje, matriculadas, yaExistentes, errores);
            } else {
                int dmId = Integer.parseInt(seleccion);
                int resultado = baseDatos.matricularAlumnoEnMateria(idAlumno, dmId);
                construirMensajeMatriculaUnica(mensaje, resultado);
                if (resultado == 1) hayExito = true;
                if (resultado == -1) hayErrores = true;
            }

            mostrarMensajeOperacion(mensaje.toString(), hayExito, hayErrores);
            lblInfo.setText(mensaje.toString().replace("\n", " | "));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de sistema al matricular: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Retirar alumno usando Base_De_Datos
    private void retirarAlumno() {
        String alumno = (String) comboAlumnos.getSelectedItem();
        String seleccion = getSelectedMateria();

        if (alumno == null || seleccion == null) {
            JOptionPane.showMessageDialog(this, "Seleccione alumno y materia");
            return;
        }

        try {
            int idAlumno = baseDatos.obtenerIdAlumnoPorNombreCompleto(alumno);
            if (idAlumno == 0) {
                JOptionPane.showMessageDialog(this, "Error de sistema: Alumno no encontrado", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder mensaje = new StringBuilder();
            boolean hayExito = false;
            boolean hayErrores = false;

            if ("TODAS".equals(seleccion)) {
                String carrera = (String) comboCarreras.getSelectedItem();
                int cantidad = baseDatos.retirarAlumnoDeCarrera(idAlumno, carrera);
                mensaje.append("🗑️ Retirado de ").append(cantidad).append(" materias.");
                hayExito = cantidad > 0;
            } else {
                int dmId = Integer.parseInt(seleccion);
                int resultado = baseDatos.retirarAlumnoDeMateria(idAlumno, dmId);
                if (resultado == 1) {
                    mensaje.append("🗑️ Retirado de la materia exitosamente.");
                    hayExito = true;
                } else {
                    mensaje.append("⚠️ No estaba matriculado en esta materia.");
                    hayErrores = true;
                }
            }

            mostrarMensajeOperacion(mensaje.toString(), hayExito, hayErrores);
            lblInfo.setText(mensaje.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de sistema al retirar: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Métodos auxiliares
    private String getSelectedMateria() {
        for (Component c : panelMaterias.getComponents()) {
            if (c instanceof JRadioButton) {
                JRadioButton rb = (JRadioButton) c;
                if (rb.isSelected()) return rb.getActionCommand();
            }
        }
        return null;
    }

    private void construirMensajeMatricula(StringBuilder mensaje, int matriculadas, int yaExistentes, int errores) {
        if (matriculadas > 0) mensaje.append("✅ Matriculado en ").append(matriculadas).append(" materia(s) exitosamente.\n");
        if (yaExistentes > 0) mensaje.append("⚠️ Ya estaba matriculado en ").append(yaExistentes).append(" materia(s).\n");
        if (errores > 0) mensaje.append("❌ Error al matricular en ").append(errores).append(" materia(s).\n");
    }

    private void construirMensajeMatriculaUnica(StringBuilder mensaje, int resultado) {
        switch (resultado) {
            case 1: mensaje.append("✅ Matriculado exitosamente en la materia seleccionada."); break;
            case 0: mensaje.append("⚠️ El alumno ya está matriculado en esta materia."); break;
            case -1: mensaje.append("❌ Error al matricular en la materia."); break;
        }
    }

    private void mostrarMensajeOperacion(String mensaje, boolean hayExito, boolean hayErrores) {
        if (hayExito && !hayErrores) {
            JOptionPane.showMessageDialog(this, mensaje, "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else if (hayErrores) {
            JOptionPane.showMessageDialog(this, mensaje, "Errores en la Operación", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
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
