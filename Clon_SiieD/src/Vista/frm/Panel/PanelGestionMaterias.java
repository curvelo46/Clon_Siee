package Vista.frm.Panel;

import Clases.*;
import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.List;

public class PanelGestionMaterias extends JPanel {

    private JComboBox<String> comboDocentes = new JComboBox<>();
    private JComboBox<String> comboMaterias = new JComboBox<>();
    private JTextField txtNombreMateria = new JTextField(20);
    private JButton btnCrearMateria = new JButton("Crear materia");
    private JButton btnAsignar = new JButton("Asignar materia");
    private ButtonGroup grupoCarreras = new ButtonGroup();
    private JPanel panelRadios = new JPanel(new GridLayout(0, 1));
    private Base_De_Datos basedatos = new Base_De_Datos();

    public PanelGestionMaterias() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de materias"));

        add(crearPanelCrear(), BorderLayout.NORTH);
        add(crearPanelAsignar(), BorderLayout.CENTER);
        add(crearPanelCarreras(), BorderLayout.SOUTH);

        cargarDocentes();
        cargarCarreras();
    }

    private JPanel crearPanelCrear() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("Nombre nueva materia:"));
        p.add(txtNombreMateria);
        p.add(btnCrearMateria);
        btnCrearMateria.addActionListener(e -> crearMateria());
        return p;
    }

    private JPanel crearPanelAsignar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("Materia:"));
        p.add(comboMaterias);
        p.add(new JLabel("Docente:"));
        p.add(comboDocentes);
        p.add(btnAsignar);
        btnAsignar.addActionListener(e -> asignarMateria());
        return p;
    }

    private JPanel crearPanelCarreras() {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createTitledBorder("Carreras"));
        p.add(panelRadios);
        return p;
    }

    private void cargarDocentes() {
        comboDocentes.removeAllItems();
        try {
            List<String[]> docentes = basedatos.listarDocentes();
            for (String[] docente : docentes) {
                comboDocentes.addItem(docente[0] + " - " + docente[1] + " " + docente[2]);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar docentes: " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMaterias() {    
        comboMaterias.removeAllItems();
        comboMaterias.addItem("Seleccione materia");
        comboMaterias.setSelectedIndex(0);
        
        String carrera = getCarreraSeleccionada();
        if (carrera == null) {
            comboMaterias.setEnabled(false);
            return;
        }
        
        comboMaterias.setEnabled(true);

        try {
            List<String> materias = basedatos.listarMateriasPorCarrera(carrera);
            for (String materia : materias) {
                if (materia != null && !materia.trim().isEmpty()) {
                    comboMaterias.addItem(materia);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar materias: " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarCarreras() {
        panelRadios.removeAll();
        grupoCarreras = new ButtonGroup();
        
        try {
            List<String> carreras = basedatos.listarCarreras();
            for (String carrera : carreras) {
                JRadioButton rb = new JRadioButton(carrera);
                grupoCarreras.add(rb);
                panelRadios.add(rb);
                rb.addActionListener(e -> cargarMaterias());
            }
            
            if (grupoCarreras.getButtonCount() > 0) {
                ((JRadioButton) grupoCarreras.getElements().nextElement()).setSelected(true);
                cargarMaterias();
            }
            
            panelRadios.revalidate();
            panelRadios.repaint();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearMateria() {
        String nombre = txtNombreMateria.getText().trim();
        String carrera = getCarreraSeleccionada();

        // ⚠️ ÚNICA ALERTA DE VALIDACIÓN
        if (nombre.isEmpty() || carrera == null) {
            JOptionPane.showMessageDialog(this, "Escribe un nombre y selecciona una carrera");
            return;
        }

        try {
            basedatos.crearMateria(nombre, carrera);
            txtNombreMateria.setText("");
            cargarMaterias();
        } catch (Exception ex) {
            System.err.println("Error en crearMateria: " + ex.getMessage());
        }
    }

    private void asignarMateria() {
        String materia = (String) comboMaterias.getSelectedItem();
        String docente = (String) comboDocentes.getSelectedItem();
        String carrera = getCarreraSeleccionada();

        if (materia == null || materia.equals("Seleccione materia") || 
            docente == null || carrera == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una materia válida, un docente y una carrera");
            return;
        }

        try {
            int idDocente = Integer.parseInt(docente.split(" - ")[0]);
            int idMateria = basedatos.obtenerIdMateriaPorCarrera(materia, carrera);
            
            if (idMateria == 0) {
                JOptionPane.showMessageDialog(this, "Materia no encontrada en la carrera seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            basedatos.asignarMateriaADocente(idDocente, idMateria);
            
        } catch (Exception ex) {
            System.err.println("Error en asignarMateria: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,ex.getMessage());
        }
    }

    public String getCarreraSeleccionada() {
        for (Enumeration<AbstractButton> buttons = grupoCarreras.getElements(); buttons.hasMoreElements();) {
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
