package Vista.frm.Panel;

import Clases.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelListaEstudiantes extends JPanel {

    private final Base_De_Datos baseDatos;
    private final String usuarioDocente;

    private final JComboBox<String> comboMaterias = new JComboBox<>();
    private final JTable tablaEstudiantes = new JTable();
    private final DefaultTableModel modeloTabla =
            new DefaultTableModel(new Object[]{"CC", "Nombre", "Apellido", "Edad", "Teléfono", "Correo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    public PanelListaEstudiantes(Base_De_Datos baseDatos, String usuarioDocente) {
        this.baseDatos = baseDatos;
        this.usuarioDocente = usuarioDocente;
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponentes();
        cargarMateriasDelDocente();
        AjustesObjetos.ajustarTabla(tablaEstudiantes);
    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.add(new JLabel("Materia:"));
        norte.add(comboMaterias);
        JButton btnCargar = new JButton("Cargar alumnos");
        btnCargar.addActionListener(e -> cargarAlumnosSP());
        norte.add(btnCargar);
        add(norte, BorderLayout.NORTH);

        tablaEstudiantes.setModel(modeloTabla);
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEstudiantes.setGridColor(Color.LIGHT_GRAY);
        tablaEstudiantes.getTableHeader().setReorderingAllowed(false);
        
        AjustesObjetos.ajustarTabla(tablaEstudiantes);
        JScrollPane scrollTabla = new JScrollPane(tablaEstudiantes);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Listado de estudiantes"));
        add(scrollTabla, BorderLayout.CENTER);
    }

    private void cargarMateriasDelDocente() {
        comboMaterias.removeAllItems();
        comboMaterias.addItem("Seleccione materia");

        try {
            List<String> materias = baseDatos.obtenerMateriasDocente(usuarioDocente);
            for (String materia : materias) {
                comboMaterias.addItem(materia);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar materias","Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarAlumnosSP() {
        modeloTabla.setRowCount(0);
        String materia = (String) comboMaterias.getSelectedItem();
        if (materia == null || materia.equals("Seleccione materia")) return;

        int idCarrera = baseDatos.obtenerCarreraIdPorDocenteMateria(usuarioDocente, materia);
        if (idCarrera == 0) {
            JOptionPane.showMessageDialog(this, "No se pudo obtener la carrera","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<Object[]> estudiantes = baseDatos.listarEstudiantesPorDocenteMateria(
                usuarioDocente, materia, idCarrera);
            
            if (estudiantes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay alumnos matriculados en esta materia.");
                return;
            }
            
            for (Object[] estudiante : estudiantes) {
                modeloTabla.addRow(estudiante);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar alumnos", "Error", JOptionPane.ERROR_MESSAGE);
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
