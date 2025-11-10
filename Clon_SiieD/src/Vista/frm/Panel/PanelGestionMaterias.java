package Vista.frm.Panel;

import Clases.Base_De_Datos;
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
    private Base_De_Datos basedatos = new Base_De_Datos();  // ← INSTANCIA

    public PanelGestionMaterias() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de materias"));

        add(crearPanelCrear(), BorderLayout.NORTH);
        add(crearPanelAsignar(), BorderLayout.CENTER);
        add(crearPanelCarreras(), BorderLayout.SOUTH);

        cargarDocentes();
        cargarMaterias();
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
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar docentes", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMaterias() {    
        comboMaterias.removeAllItems();
        comboMaterias.addItem("Seleccione materia");
        
        String carrera = getCarreraSeleccionada();
        if (carrera == null) return;

        try {
            List<String> materias = basedatos.listarMateriasPorCarrera(carrera);
            for (String materia : materias) {
                comboMaterias.addItem(materia);
            }
            comboMaterias.setSelectedIndex(0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar materias", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
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
            }
            if (grupoCarreras.getButtonCount() > 0) {
                ((JRadioButton) grupoCarreras.getElements().nextElement()).setSelected(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar carreras", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Actualización instantánea al cambiar de carrera
        for (Enumeration<AbstractButton> buttons = grupoCarreras.getElements(); buttons.hasMoreElements();) {
            AbstractButton btn = buttons.nextElement();
            btn.addActionListener(e -> {
                cargarMaterias();
                comboMaterias.setSelectedIndex(-1);
            });
        }
    }

    private void crearMateria() {
        String nombre = txtNombreMateria.getText().trim();
        String carrera = getCarreraSeleccionada();

        if (nombre.isEmpty() || carrera == null) {
            JOptionPane.showMessageDialog(this, "Escribe un nombre y selecciona una carrera");
            return;
        }

        try {
            basedatos.crearMateria(nombre, carrera);
            txtNombreMateria.setText("");
            cargarMaterias();
        } catch (Exception ex) {
            // El mensaje ya se muestra en Base_De_Datos
        }
    }

    private void asignarMateria() {
        String materia = (String) comboMaterias.getSelectedItem();
        String docente = (String) comboDocentes.getSelectedItem();
        String carrera = getCarreraSeleccionada();

        if (materia == null || docente == null || carrera == null) return;

        int idDocente = Integer.parseInt(docente.split(" - ")[0]);

        try {
            int idMateria = basedatos.obtenerIdMateriaPorCarrera(materia, carrera);
            if (idMateria == 0) {
                JOptionPane.showMessageDialog(this, "Materia no encontrada en la carrera seleccionada", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            basedatos.asignarMateriaADocente(idDocente, idMateria);
        } catch (Exception ex) {
            // El mensaje ya se muestra en Base_De_Datos
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
