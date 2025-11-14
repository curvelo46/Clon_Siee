package Vista.frm.Panel;

import Clases.Base_De_Datos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelRetirarMateriaADocente extends JPanel {
    private static final Base_De_Datos baseDatos = new Base_De_Datos();
    
    // Radio buttons para seleccionar modo
    private final JRadioButton rbRemplazar = new JRadioButton("Remplazar docente");
    private final JRadioButton rbEliminar = new JRadioButton("Eliminar materia");
    private final ButtonGroup grupoModo = new ButtonGroup();
    
    // Panel principal con CardLayout
    private final JPanel panelModos = new JPanel(new CardLayout());
    
    // Componentes para modo ELIMINAR (original)
    private final JComboBox<String> comboDocentesEliminar = new JComboBox<>();
    private final JComboBox<String> comboMateriasEliminar = new JComboBox<>();
    private final JTextField txtBuscarDocenteEliminar = new JTextField(15);
    private final JButton btnBuscarDocenteEliminar = new JButton("Buscar");
    private final JButton btnEliminarMateria = new JButton("Eliminar materia");
    private final Map<String, Integer> docenteIdMapEliminar = new HashMap<>();
    
    // Componentes para modo REMPLAZAR
    private final JTextField txtBuscarDocenteActual = new JTextField(15);
    private final JButton btnBuscarDocenteActual = new JButton("Buscar");
    private final JComboBox<String> comboDocenteActual = new JComboBox<>();
    private final Map<String, Integer> docenteActualMap = new HashMap<>();
    
    private JTable tablaMaterias; // Tabla para mostrar materias del docente actual
    private DefaultTableModel modeloTablaMaterias;
    private final Map<Integer, Integer> docenteMateriaIdMap = new HashMap<>(); 
    
    private final JTextField txtBuscarDocenteRemplazo = new JTextField(15);
    private final JButton btnBuscarDocenteRemplazo = new JButton("Buscar");
    private final JComboBox<String> comboDocenteRemplazo = new JComboBox<>();
    private final Map<String, Integer> docenteRemplazoMap = new HashMap<>();
    
    private final JButton btnCambiarMateria = new JButton("Cambiar materia");
    
    // Constructor
    public PanelRetirarMateriaADocente() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 254, 214));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initUI();
        configurarRadioButtons();
        configurarPanelEliminar();
        configurarPanelRemplazar();
        configurarEventos();
        
    }
    
    private void initUI() {
        // Panel superior con radio buttons
        JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOpciones.setBackground(new Color(255, 254, 214));
        grupoModo.add(rbRemplazar);
        grupoModo.add(rbEliminar);
        panelOpciones.add(rbRemplazar);
        panelOpciones.add(rbEliminar);
        
        // Inicializar tabla de materias
        String[] columnas = {"Materia", "Carrera", "ID Asignación"};
        modeloTablaMaterias = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaMaterias = new JTable(modeloTablaMaterias);
        tablaMaterias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tablaMaterias);
        scrollTabla.setPreferredSize(new Dimension(600, 200));
        
        // Crear paneles para cada modo
        JPanel panelEliminar = crearPanelEliminar();
        JPanel panelRemplazar = crearPanelRemplazar(scrollTabla);
        
        panelModos.add(panelEliminar, "ELIMINAR");
        panelModos.add(panelRemplazar, "REMPLAZAR");
        
        add(panelOpciones, BorderLayout.NORTH);
        add(panelModos, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 254, 214));
        
        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.add(new JLabel("Docente:"));
        norte.add(comboDocentesEliminar);
        norte.add(new JLabel("Buscar:"));
        norte.add(txtBuscarDocenteEliminar);
        norte.add(btnBuscarDocenteEliminar);
        
        JPanel centro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centro.add(new JLabel("Materia:"));
        centro.add(comboMateriasEliminar);
        
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(btnEliminarMateria);
        
        panel.add(norte, BorderLayout.NORTH);
        panel.add(centro, BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelRemplazar(JScrollPane scrollTabla) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 254, 214));
        
        // Panel superior: Docente actual y sus materias
        JPanel panelDocenteActual = new JPanel(new BorderLayout(10, 10));
        panelDocenteActual.setBorder(BorderFactory.createTitledBorder("Docente Actual"));
        panelDocenteActual.setBackground(new Color(255, 254, 214));
        
        JPanel busquedaDocenteActual = new JPanel(new FlowLayout(FlowLayout.LEFT));
        busquedaDocenteActual.add(new JLabel("Buscar:"));
        busquedaDocenteActual.add(txtBuscarDocenteActual);
        busquedaDocenteActual.add(btnBuscarDocenteActual);
        busquedaDocenteActual.add(new JLabel("Docente:"));
        busquedaDocenteActual.add(comboDocenteActual);
        
        panelDocenteActual.add(busquedaDocenteActual, BorderLayout.NORTH);
        panelDocenteActual.add(scrollTabla, BorderLayout.CENTER);
        
        // Panel inferior: Docente de reemplazo
        JPanel panelDocenteRemplazo = new JPanel(new BorderLayout(10, 10));
        panelDocenteRemplazo.setBorder(BorderFactory.createTitledBorder("Docente de Remplazo"));
        panelDocenteRemplazo.setBackground(new Color(255, 254, 214));
        
        JPanel busquedaDocenteRemplazo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        busquedaDocenteRemplazo.add(new JLabel("Buscar:"));
        busquedaDocenteRemplazo.add(txtBuscarDocenteRemplazo);
        busquedaDocenteRemplazo.add(btnBuscarDocenteRemplazo);
        busquedaDocenteRemplazo.add(new JLabel("Docente:"));
        busquedaDocenteRemplazo.add(comboDocenteRemplazo);
        
        panelDocenteRemplazo.add(busquedaDocenteRemplazo, BorderLayout.CENTER);
        
        // Panel de botón
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnCambiarMateria);
        
        panel.add(panelDocenteActual, BorderLayout.CENTER);
        panel.add(panelDocenteRemplazo, BorderLayout.SOUTH);
        panel.add(panelBoton, BorderLayout.EAST);
        
        return panel;
    }
    
    private void configurarRadioButtons() {
        rbRemplazar.addActionListener(e -> {
            CardLayout cl = (CardLayout) panelModos.getLayout();
            cl.show(panelModos, "REMPLAZAR");
        });
        
        rbEliminar.addActionListener(e -> {
            CardLayout cl = (CardLayout) panelModos.getLayout();
            cl.show(panelModos, "ELIMINAR");
        });
        
        // Seleccionar eliminar por defecto
        rbEliminar.setSelected(true);
    }
    
    private void configurarPanelEliminar() {
        cargarDocentesEliminar();
        
        btnBuscarDocenteEliminar.addActionListener(e -> buscarDocentesEliminar());
        comboDocentesEliminar.addActionListener(e -> cargarMateriasEliminar());
        btnEliminarMateria.addActionListener(e -> eliminarMateria());
    }
    
   private void configurarPanelRemplazar() {
    // Eventos
    btnBuscarDocenteActual.addActionListener(e -> buscarDocenteActual());
    btnBuscarDocenteRemplazo.addActionListener(e -> buscarDocenteRemplazo());
    comboDocenteActual.addActionListener(e -> {
        // Solo cargar si hay algo seleccionado y el mapa contiene la clave
        String docente = (String) comboDocenteActual.getSelectedItem();
        if (docente != null && docenteActualMap.containsKey(docente)) {
            cargarMateriasDocenteActual();
        }
    });
    btnCambiarMateria.addActionListener(e -> cambiarMateria());
}
    
    private void configurarEventos() {
        // Configuración inicial
        rbEliminar.setSelected(true);
    }
    
    /* ==================== MÉTODOS PARA MODO ELIMINAR ==================== */
    private void cargarDocentesEliminar() {
        comboDocentesEliminar.removeAllItems();
        docenteIdMapEliminar.clear();
        
        List<String[]> docentes = baseDatos.listarDocentes();
        for (String[] docente : docentes) {
            int id = Integer.parseInt(docente[0]);
            String nombreCompleto = docente[1] + " " + docente[2];
            comboDocentesEliminar.addItem(nombreCompleto);
            docenteIdMapEliminar.put(nombreCompleto, id);
        }
        
        if (comboDocentesEliminar.getItemCount() > 0) {
            comboDocentesEliminar.setSelectedIndex(0);
            cargarMateriasEliminar();
        }
    }
    
    private void buscarDocentesEliminar() {
        String texto = txtBuscarDocenteEliminar.getText().trim();
        comboDocentesEliminar.removeAllItems();
        docenteIdMapEliminar.clear();
        
        Map<String, Integer> docentes = baseDatos.buscarDocentesPorNombre(texto);
        if (docentes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron docentes.");
        } else {
            for (String nombre : docentes.keySet()) {
                comboDocentesEliminar.addItem(nombre);
            }
            docenteIdMapEliminar.putAll(docentes);
        }
    }
    
    private void cargarMateriasEliminar() {
        comboMateriasEliminar.removeAllItems();
        String docente = (String) comboDocentesEliminar.getSelectedItem();
        if (docente == null) return;
        
        int idDocente = docenteIdMapEliminar.get(docente);
        List<String> materias = baseDatos.obtenerMateriasDocentePorId(idDocente);
        
        for (String materia : materias) {
            comboMateriasEliminar.addItem(materia);
        }
    }
    
    private void eliminarMateria() {
        String materia = (String) comboMateriasEliminar.getSelectedItem();
        String docente = (String) comboDocentesEliminar.getSelectedItem();
        
        if (materia == null || docente == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un docente y una materia.");
            return;
        }
        
        int idDocente = docenteIdMapEliminar.get(docente);
        boolean exito = baseDatos.eliminarAsignacionDocenteMateria(idDocente, materia);
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Materia eliminada correctamente.");
            comboMateriasEliminar.removeItem(materia);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al eliminar materia.");
        }
    }
    
    /* ==================== MÉTODOS PARA MODO REMPLAZAR ==================== */
    private void buscarDocenteActual() {
    String texto = txtBuscarDocenteActual.getText().trim();
    comboDocenteActual.removeAllItems();
    docenteActualMap.clear();
    
    Map<String, Integer> docentes = baseDatos.buscarDocentesPorNombre(texto);
    
    if (docentes.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No se encontraron docentes.");
        modeloTablaMaterias.setRowCount(0); // Limpiar tabla
    } else {
        // Llenar combo y mapa con los MISMOS strings
        for (Map.Entry<String, Integer> entry : docentes.entrySet()) {
            String nombreDocente = entry.getKey();
            comboDocenteActual.addItem(nombreDocente);
            docenteActualMap.put(nombreDocente, entry.getValue());
        }
        
        // Seleccionar el primero y cargar sus materias
        if (comboDocenteActual.getItemCount() > 0) {
            comboDocenteActual.setSelectedIndex(0);
            // No llamar a cargarMateriasDocenteActual() aquí porque el ActionListener se encargará
        }
    }
}
    
    private void cargarMateriasDocenteActual() {
    // VALIDACIÓN ROBUSTA
    String docente = (String) comboDocenteActual.getSelectedItem();
    
    if (docente == null || docente.trim().isEmpty()) {
        System.out.println("DEBUG: Docente seleccionado es null o vacío");
        modeloTablaMaterias.setRowCount(0);
        return;
    }
    
    // VERIFICAR QUE EL DOCENTE EXISTE EN EL MAPA
    if (!docenteActualMap.containsKey(docente)) {
        System.out.println("ERROR: Docente '" + docente + "' no encontrado en el mapa.");
        System.out.println("Mapa contiene: " + docenteActualMap.keySet());
        JOptionPane.showMessageDialog(this, 
            "Error interno: No se encontró el ID del docente. Busque nuevamente.", 
            "Error", JOptionPane.ERROR_MESSAGE);
        modeloTablaMaterias.setRowCount(0);
        return;
    }
    
    int idDocente = docenteActualMap.get(docente);
    System.out.println("DEBUG: Cargando materias para docente: " + docente + " (ID: " + idDocente + ")");
    
    // Limpiar tabla
    modeloTablaMaterias.setRowCount(0);
    docenteMateriaIdMap.clear();
    
    try {
        List<Object[]> materias = baseDatos.obtenerMateriasDocenteConCarrera(idDocente);
        System.out.println("DEBUG: Obtenidas " + materias.size() + " materias");
        
        for (int i = 0; i < materias.size(); i++) {
            Object[] materia = materias.get(i);
            String nombreMateria = (String) materia[0];
            String nombreCarrera = (String) materia[1];
            int docenteMateriaId = (Integer) materia[2];
            
            modeloTablaMaterias.addRow(new Object[]{nombreMateria, nombreCarrera, docenteMateriaId});
            docenteMateriaIdMap.put(i, docenteMateriaId);
        }
        
        if (materias.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este docente no tiene materias asignadas.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar materias: " + e.getMessage());
    }
}
    
    private void buscarDocenteRemplazo() {
        String texto = txtBuscarDocenteRemplazo.getText().trim();
        comboDocenteRemplazo.removeAllItems();
        docenteRemplazoMap.clear();
        
        Map<String, Integer> docentes = baseDatos.buscarDocentesPorNombre(texto);
        if (docentes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron docentes.");
        } else {
            for (String nombre : docentes.keySet()) {
                comboDocenteRemplazo.addItem(nombre);
            }
            docenteRemplazoMap.putAll(docentes);
        }
    }
    
    private void cambiarMateria() {
        // Validar selección de materia
        int filaSeleccionada = tablaMaterias.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una materia de la tabla.");
            return;
        }
        
        // Validar docente de reemplazo
        String docenteRemplazo = (String) comboDocenteRemplazo.getSelectedItem();
        if (docenteRemplazo == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un docente de reemplazo.");
            return;
        }
        
        int docenteMateriaId = docenteMateriaIdMap.get(filaSeleccionada);
        int idDocenteRemplazo = docenteRemplazoMap.get(docenteRemplazo);
        
        // Confirmar acción
        String materia = (String) modeloTablaMaterias.getValueAt(filaSeleccionada, 0);
        String carrera = (String) modeloTablaMaterias.getValueAt(filaSeleccionada, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de asignar la materia \"" + materia + "\" (" + carrera + ")\n" +
            "al docente \"" + docenteRemplazo + "\"?\n\n" +
            "Los alumnos y sus notas se mantendrán.",
            "Confirmar reemplazo",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirm != JOptionPane.YES_OPTION) return;
        
        // Ejecutar reemplazo
        boolean exito = baseDatos.reemplazarDocenteEnMateria(docenteMateriaId, idDocenteRemplazo);
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Docente reemplazado correctamente.");
            cargarMateriasDocenteActual(); // Recargar tabla
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al reemplazar docente. Verifique que no tenga la materia asignada.");
        }
    }

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
