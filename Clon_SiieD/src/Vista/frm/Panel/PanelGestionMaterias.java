package Vista.frm.Panel;

import Clases.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.Normalizer;
import java.util.*;
import java.util.List;

public class PanelGestionMaterias extends JPanel {

    private JComboBox<String> comboDocentes = new JComboBox<>();
    private JComboBox<String> comboMaterias = new JComboBox<>();
    private JComboBox<String> comboCarreras = new JComboBox<>(); // NUEVO: Combo para carreras
    private JTextField txtNombreMateria = new JTextField(20);
    private JButton btnCrearMateria = new JButton("Crear materia");
    private JButton btnAsignar = new JButton("Asignar materia");
    private Base_De_Datos basedatos = new Base_De_Datos();
    
    // Para la funcionalidad de búsqueda inteligente
    private List<String> todasLasCarreras = new ArrayList<>();
    private DefaultComboBoxModel<String> carreraModel;

    public PanelGestionMaterias() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de materias"));

        // Panel superior unificado con todos los controles
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.add(crearPanelSuperior());
        
        add(panelSuperior, BorderLayout.NORTH);

        cargarDocentes();
        cargarCarreras();
        configurarBusquedaComboCarreras();
    }

    /**
     * Panel superior con todos los campos organizados en dos filas
     */
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 1: Carrera, Nombre Materia y Botón Crear
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Carrera:"), gbc);
        
        gbc.gridx = 1;
        comboCarreras.setPreferredSize(new Dimension(200, 25));
        panel.add(comboCarreras, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Nombre nueva materia:"), gbc);
        
        gbc.gridx = 3;
        txtNombreMateria.setPreferredSize(new Dimension(150, 25));
        panel.add(txtNombreMateria, gbc);
        
        gbc.gridx = 4;
        panel.add(btnCrearMateria, gbc);
        btnCrearMateria.addActionListener(e -> crearMateria());

        // Fila 2: Materia, Docente y Botón Asignar
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Materia:"), gbc);
        
        gbc.gridx = 1;
        comboMaterias.setPreferredSize(new Dimension(200, 25));
        panel.add(comboMaterias, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Docente:"), gbc);
        
        gbc.gridx = 3;
        comboDocentes.setPreferredSize(new Dimension(200, 25));
        panel.add(comboDocentes, gbc);
        
        gbc.gridx = 4;
        panel.add(btnAsignar, gbc);
        btnAsignar.addActionListener(e -> asignarMateria());

        return panel;
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
        todasLasCarreras.clear();
        try {
            List<String> carreras = basedatos.listarCarreras();
            todasLasCarreras.addAll(carreras);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
        
        carreraModel = new DefaultComboBoxModel<>(todasLasCarreras.toArray(new String[0]));
        comboCarreras.setModel(carreraModel);
        
        // Seleccionar primera carrera si hay disponible
        if (todasLasCarreras.size() > 0) {
            comboCarreras.setSelectedIndex(0);
            cargarMaterias();
        }
    }

    /**
     * Configura el combo de carreras para que sea editable y tenga 
     * funcionalidad de búsqueda con normalización de texto
     */
    private void configurarBusquedaComboCarreras() {
        comboCarreras.setEditable(true);
        JTextField editor = (JTextField) comboCarreras.getEditor().getEditorComponent();
        
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Evitar filtrar con teclas de navegación
                if (e.getKeyCode() == KeyEvent.VK_UP || 
                    e.getKeyCode() == KeyEvent.VK_DOWN || 
                    e.getKeyCode() == KeyEvent.VK_ENTER) {
                    return;
                }
                
                SwingUtilities.invokeLater(() -> {
                    String texto = editor.getText();
                    filtrarCarreras(texto);
                });
            }
        });
        
        // Recargar materias cuando se seleccione una carrera
        comboCarreras.addActionListener(e -> {
            if (comboCarreras.getSelectedItem() != null) {
                cargarMaterias();
            }
        });
    }

    /**
     * Filtra las carreras según el texto ingresado usando comparación normalizada
     */
    private void filtrarCarreras(String textoBusqueda) {
        String textoNormalizado = normalizeText(textoBusqueda);
        List<String> filtradas = new ArrayList<>();
        
        for (String carrera : todasLasCarreras) {
            if (normalizeText(carrera).contains(textoNormalizado)) {
                filtradas.add(carrera);
            }
        }
        
        // Guardar texto actual para restaurarlo
        String textoActual = textoBusqueda;
        
        // Actualizar modelo del combo
        carreraModel.removeAllElements();
        for (String carrera : filtradas) {
            carreraModel.addElement(carrera);
        }
        
        // Restaurar texto en el editor
        comboCarreras.getEditor().setItem(textoActual);
        
        // Mostrar popup si hay resultados
        if (!filtradas.isEmpty()) {
            comboCarreras.setPopupVisible(true);
        }
    }

    /**
     * Normaliza texto eliminando tildes, comillas y convirtiendo a minúsculas
     */
    private String normalizeText(String text) {
        if (text == null) return "";
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}", "");
        normalized = normalized.replaceAll("[\"']", "");
        return normalized.toLowerCase();
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
            System.err.println("Error en crearMateria: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al crear materia: " + ex.getMessage());
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
            JOptionPane.showMessageDialog(this, "Materia asignada exitosamente");
            
        } catch (Exception ex) {
            System.err.println("Error en asignarMateria: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public String getCarreraSeleccionada() {
        return (String) comboCarreras.getSelectedItem();
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
