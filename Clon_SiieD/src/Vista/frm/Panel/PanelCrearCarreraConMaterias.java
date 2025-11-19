package Vista.frm.Panel;

import Clases.Base_De_Datos;  
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class PanelCrearCarreraConMaterias extends JPanel {

    private JTextField txtNombreCarrera = new JTextField(20);
    private JTextField txtBuscarMateria = new JTextField(20); 
    private JButton btnCrear = new JButton("Crear carrera");
    private JPanel panelMaterias = new JPanel(new GridLayout(0, 3, 5, 5));
    private List<JCheckBox> checkBoxes = new ArrayList<>();
    private List<String> todasLasMaterias = new ArrayList<>(); 
    private Base_De_Datos basedatos = new Base_De_Datos();  

    public PanelCrearCarreraConMaterias() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Crear carrera y asignar materias"));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(new JScrollPane(panelMaterias), BorderLayout.CENTER);
        add(crearPanelBoton(), BorderLayout.SOUTH);

        cargarTodasLasMaterias();
    }

    /**
     * Panel superior con nombre de carrera y búsqueda de materias EN LA MISMA FILA
     */
    private JPanel crearPanelSuperior() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 0: Nombre de carrera y Buscar materia (juntos)
        gbc.gridx = 0; gbc.gridy = 0;
        p.add(new JLabel("Nombre de la carrera:"), gbc);
        
        gbc.gridx = 1;
        txtNombreCarrera.setPreferredSize(new Dimension(200, 25));
        p.add(txtNombreCarrera, gbc);
        
        // Espaciado entre grupos
        gbc.gridx = 2;
        p.add(Box.createHorizontalStrut(20), gbc);
        
        gbc.gridx = 3;
        p.add(new JLabel("Buscar materia:"), gbc);
        
        gbc.gridx = 4;
        txtBuscarMateria.setPreferredSize(new Dimension(200, 25));
        p.add(txtBuscarMateria, gbc);

        // Configurar listener de búsqueda
        txtBuscarMateria.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Evitar filtrar con teclas de navegación
                if (e.getKeyCode() == KeyEvent.VK_UP || 
                    e.getKeyCode() == KeyEvent.VK_DOWN || 
                    e.getKeyCode() == KeyEvent.VK_ENTER) {
                    return;
                }
                filtrarMaterias();
            }
        });

        return p;
    }

    private JPanel crearPanelBoton() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.add(btnCrear);
        btnCrear.addActionListener(e -> crearCarreraConMaterias());
        return p;
    }

    private void cargarTodasLasMaterias() {
        checkBoxes.clear();
        todasLasMaterias.clear();
        panelMaterias.removeAll();

        try {
            List<String> materias = basedatos.listarTodasLasMaterias();
            todasLasMaterias.addAll(materias);
            renderizarMaterias(todasLasMaterias); // Renderizar todas inicialmente
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar materias: " + ex.getMessage());
        }
        
        panelMaterias.revalidate();
        panelMaterias.repaint();
    }

    /**
     * Filtra y muestra solo las materias que coinciden con la búsqueda
     */
    private void filtrarMaterias() {
        String textoBusqueda = normalizeText(txtBuscarMateria.getText());
        
        List<String> materiasFiltradas = new ArrayList<>();
        for (String materia : todasLasMaterias) {
            if (normalizeText(materia).contains(textoBusqueda)) {
                materiasFiltradas.add(materia);
            }
        }
        
        renderizarMaterias(materiasFiltradas);
    }

    /**
     * Renderiza los checkboxes para la lista de materias proporcionada
     */
    private void renderizarMaterias(List<String> materias) {
        panelMaterias.removeAll();
        checkBoxes.clear();

        for (String materia : materias) {
            JCheckBox cb = new JCheckBox(materia);
            checkBoxes.add(cb);
            panelMaterias.add(cb);
        }

        panelMaterias.revalidate();
        panelMaterias.repaint();
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

    private void crearCarreraConMaterias() {
        String nombreCarrera = txtNombreCarrera.getText().trim();
        if (nombreCarrera.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe un nombre para la carrera");
            return;
        }

        List<String> seleccionadas = new ArrayList<>();
        for (JCheckBox cb : checkBoxes) {
            if (cb.isSelected()) seleccionadas.add(cb.getText());
        }

        if (seleccionadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos una materia");
            return;
        }

        try {
            basedatos.crearCarreraConMaterias(nombreCarrera, seleccionadas);
            JOptionPane.showMessageDialog(this, "✅ Carrera creada exitosamente");
            txtNombreCarrera.setText("");
            txtBuscarMateria.setText("");
            cargarTodasLasMaterias(); // Recargar y resetear filtro
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
