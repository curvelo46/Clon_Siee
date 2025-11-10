package Vista.frm.Panel;

import Clases.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PanelMisNotas extends JPanel {
    private JComboBox<String> comboCarreras;
    private JTable tablaNotas;
    private DefaultTableModel modeloTabla;
    private String usuarioActual;
    private Map<String, Integer> carreraMap;
    private Base_De_Datos baseDatos;
    private JLabel lbPromedioGeneral;
    
    public PanelMisNotas(String usuarioActual, Base_De_Datos baseDatos) {
        this.usuarioActual = usuarioActual;
        this.baseDatos = baseDatos;
        this.carreraMap = new HashMap<>();
        
        initComponentes();
        cargarCarreras();
    }
    
    private void initComponentes() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 244, 248));
        
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(240, 244, 248));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));
        
        JLabel lblTitulo = new JLabel("Mis Notas Académicas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(192, 4, 29));
        
        panelSuperior.add(lblTitulo);
        panelSuperior.add(Box.createHorizontalGlue());
        
        JLabel lblCarrera = new JLabel("Carrera:");
        lblCarrera.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelSuperior.add(lblCarrera);
        panelSuperior.add(Box.createRigidArea(new Dimension(10, 0)));
        
        comboCarreras = new JComboBox<>();
        comboCarreras.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboCarreras.setPreferredSize(new Dimension(300, 30));
        comboCarreras.setMaximumSize(new Dimension(300, 30));
        comboCarreras.addActionListener(e -> cargarNotas());
        panelSuperior.add(comboCarreras);
        
        String[] columnas = {"Materia", "Corte 1", "Corte 2", "Corte 3", "Promedio Final"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaNotas = new JTable(modeloTabla);
        tablaNotas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaNotas.setRowHeight(25);
        tablaNotas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaNotas.getTableHeader().setBackground(new Color(192, 4, 29));
        tablaNotas.getTableHeader().setForeground(Color.BLACK);
        tablaNotas.setGridColor(new Color(221, 221, 221));
        AjustesObjetos.ajustarTabla(tablaNotas);
        
        JScrollPane scrollTabla = new JScrollPane(tablaNotas);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSur.setBackground(new Color(240, 244, 248));
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        lbPromedioGeneral = new JLabel("Promedio General del Estudiante: 0.0");
        lbPromedioGeneral.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbPromedioGeneral.setForeground(new Color(192, 4, 29));
        panelSur.add(lbPromedioGeneral);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);
    }
    
    private void cargarCarreras() {
        comboCarreras.removeAllItems();
        carreraMap.clear();
        
        try {
            List<Object[]> carreras = baseDatos.obtenerCarrerasDelAlumno(usuarioActual);
            
            if (carreras.isEmpty()) {
                comboCarreras.addItem("No tiene carreras matriculadas");
                comboCarreras.setEnabled(false);
                return;
            }
            
            for (Object[] carrera : carreras) {
                int id = (Integer) carrera[0];
                String nombre = (String) carrera[1];
                carreraMap.put(nombre, id);
                comboCarreras.addItem(nombre);
            }
            
            comboCarreras.setEnabled(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar carreras", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            comboCarreras.addItem("Error al cargar carreras");
            comboCarreras.setEnabled(false);
        }
    }
    
    private void cargarNotas() {
    modeloTabla.setRowCount(0);
    
    if (!comboCarreras.isEnabled() || comboCarreras.getItemCount() == 0) {
        lbPromedioGeneral.setText("Promedio General del Estudiante: 0.0");
        return;
    }
    
    String carreraSeleccionada = (String) comboCarreras.getSelectedItem();
    if (carreraSeleccionada == null || carreraSeleccionada.isEmpty()) {
        lbPromedioGeneral.setText("Promedio General del Estudiante: 0.0");
        return;
    }
    
    Integer carreraId = carreraMap.get(carreraSeleccionada);
    if (carreraId == null) {
        JOptionPane.showMessageDialog(this, "Error de sistema: No se encontró el ID de la carrera", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
        lbPromedioGeneral.setText("Promedio General del Estudiante: 0.0");
        return;
    }
    
    int alumnoId = baseDatos.obtenerIdAlumnoPorUsername(usuarioActual);
    if (alumnoId == -1) {
        JOptionPane.showMessageDialog(this, "Error de sistema: No se pudo identificar al alumno", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
        lbPromedioGeneral.setText("Promedio General del Estudiante: 0.0");
        return;
    }
    
    try {
        List<Object[]> notas = baseDatos.listarNotasPorAlumnoCarrera(alumnoId, carreraId);
        
        if (notas.isEmpty()) {
            modeloTabla.addRow(new Object[]{"No hay registros de notas", "-", "-", "-", "-"});
            lbPromedioGeneral.setText("Promedio General del Estudiante: 0.0");
            return;
        }
        
        double sumaPromedios = 0;
        int cantidadMaterias = 0;
        
        for (Object[] nota : notas) {
            String materia = (String) nota[0];
            String c1 = (String) nota[1];
            String c2 = (String) nota[2];
            String c3 = (String) nota[3];
            String promedio = (String) nota[4];
            
            modeloTabla.addRow(new Object[]{materia, c1, c2, c3, promedio});
            
            // Parsear usando Double.parseDouble (ahora siempre con punto)
            sumaPromedios += Double.parseDouble(promedio);
            cantidadMaterias++;
        }
        
        double promedioGeneral = cantidadMaterias > 0 ? sumaPromedios / cantidadMaterias : 0;
        lbPromedioGeneral.setText(String.format(Locale.US, "Promedio General del Estudiante: %.1f", promedioGeneral));
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar notas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        lbPromedioGeneral.setText("Promedio General del Estudiante: 0.0");
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
