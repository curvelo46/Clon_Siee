package Vista.frm.Panel;

import Clases.Base_De_Datos;
import Clases.ConexionBD;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class PanelRetirarMateriaADocente extends JPanel {
    // Instancia estática de Base_De_Datos
    private static final Base_De_Datos baseDatos = new Base_De_Datos();
    
    private final JComboBox<String> comboDocentes = new JComboBox<>();
    private final JComboBox<String> comboMaterias = new JComboBox<>();
    private final JButton btnRetirar = new JButton("Retirar materia");
    private final JTextField txtBuscar = new JTextField(15);
    private final JButton btnBuscar = new JButton("Buscar");
    private final Map<String, Integer> docenteIdMap = new HashMap<>();
    private String docenteSeleccionado = null;

    // Constructor SIN parámetros - se mantiene igual
    public PanelRetirarMateriaADocente() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 254, 214));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initUI();
        cargarDocentes();
        configurarBusqueda();
        configurarBotonRetirar();
    }

    /* -------------------- MÉTODOS -------------------- */
    private void cargarDocentes() {
    comboDocentes.removeAllItems();
    docenteIdMap.clear(); // ← LIMPIAR mapa antes de llenarlo
    
    List<String[]> docentes = baseDatos.listarDocentes();
    
    for (String[] docente : docentes) {
        int idDocente = Integer.parseInt(docente[0]); // ← OBTENER ID
        String nombreCompleto = docente[1] + " " + docente[2];
        
        comboDocentes.addItem(nombreCompleto);
        docenteIdMap.put(nombreCompleto, idDocente); // ← GUARDAR en mapa
    }
    
    // Cargar materias del primer docente si existe
    if (comboDocentes.getItemCount() > 0) {
        comboDocentes.setSelectedIndex(0);
        cargarMateriasDelDocente();
    }
}

    private void cargarMateriasDelDocente() {
        comboMaterias.removeAllItems();
        String docente = (String) comboDocentes.getSelectedItem();
        if (docente == null) return;

        int idDocente = docenteIdMap.get(docente);
        
        // Llamada al método de Base_De_Datos
        List<String> materias = baseDatos.obtenerMateriasDocentePorId(idDocente);
        
        for (String materia : materias) {
            comboMaterias.addItem(materia);
        }
        
        if (comboMaterias.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Este docente no tiene materias asignadas.");
        }
    }

    private void configurarBusqueda() {
    btnBuscar.addActionListener(e -> buscarDocentesPorNombre());
    
    // ✅ NUEVO: Listener para cuando se seleccione un docente
    comboDocentes.addActionListener(e -> {
        if (comboDocentes.getSelectedItem() != null) {
            cargarMateriasDelDocente();
        }
    });
}

    private void buscarDocentesPorNombre() {
    String texto = txtBuscar.getText().trim();
    comboDocentes.removeAllItems();
    docenteIdMap.clear(); // ← LIMPIAR mapa

    Map<String, Integer> docentes = baseDatos.buscarDocentesPorNombre(texto);
    
    if (docentes.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No se encontraron docentes con ese nombre.");
    } else {
        for (String display : docentes.keySet()) {
            comboDocentes.addItem(display);
        }
        docenteIdMap.putAll(docentes); // ← GUARDAR resultados
    }
}

    private void retirarMateria() {
        String materia = (String) comboMaterias.getSelectedItem();
        String docente = (String) comboDocentes.getSelectedItem();

        if (materia == null || docente == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un docente y una materia.");
            return;
        }

        int idDocente = docenteIdMap.get(docente);
        
        // Llamada al método de Base_De_Datos
        boolean exito = baseDatos.eliminarAsignacionDocenteMateria(idDocente, materia);
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Materia retirada correctamente.");
            comboMaterias.removeItem(materia);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al retirar materia.");
        }
    }

    private void configurarBotonRetirar() {
        btnRetirar.addActionListener(e -> retirarMateria());
    }

    private void initUI() {
        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.add(new JLabel("Docente:"));
        norte.add(comboDocentes);
        norte.add(new JLabel("Buscar:"));
        norte.add(txtBuscar);
        norte.add(btnBuscar);

        JPanel centro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centro.add(new JLabel("Materia:"));
        centro.add(comboMaterias);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(btnRetirar);

        add(norte, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);
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
