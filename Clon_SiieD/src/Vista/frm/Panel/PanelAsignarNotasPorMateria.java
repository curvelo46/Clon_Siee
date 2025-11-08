package Vista.frm.Panel;

import Clases.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.DocumentFilter;
import javax.swing.text.BadLocationException;

public class PanelAsignarNotasPorMateria extends JPanel {

    private static final Color FONDO = new Color(255, 254, 214);

    private final String profesor;
    private String materiaSeleccionada = null;
    private int materiaId;
    private int carreraId;
    private int corteSeleccionado = 1;
    private int docenteMateriaId = -1;
    private final Set<Integer> filasEditadas = new HashSet<>();

    private final JComboBox<String> comboMaterias = new JComboBox<>();
    private final JButton btnGuardar = new JButton("Guardar");
    private final JLabel lbMateria = new JLabel("Materia: Seleccione una materia");
    private final JLabel lbPromedio = new JLabel("Promedio del grupo: 0.0");

    private final JTable tabla = new JTable();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Estudiante", "Corte 1", "Corte 2", "Corte 3", "Promedio", "C1_Edit", "C2_Edit", "C3_Edit"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            if (column >= 2 && column <= 4) {
                int corteColumna = column - 1;
                int editCol = column + 4;
                boolean esCorteActual = (corteColumna == corteSeleccionado);
                boolean noEditado = "0".equals(getValueAt(row, editCol).toString());
                return esCorteActual && noEditado;
            }
            return false;
        }
    };

    public PanelAsignarNotasPorMateria(String profesor) {
        this.profesor = profesor;
        tabla.setModel(modelo);
        initUI();
        cargarMateriasEnCombo();
        configurarCombo();
        configurarBotonGuardar();
        configurarEditorDeNotas(); // ✅ NUEVO: Configura validación de entrada
        ocultarColumnasDesdeInicio();
        modelo.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col >= 2 && col <= 4) filasEditadas.add(row);
            }
        });
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(FONDO);

        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.setBackground(FONDO);
        norte.add(new JLabel("Materia:"));
        norte.add(comboMaterias);
        norte.add(btnGuardar);
        norte.add(lbMateria);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sur.setBackground(FONDO);
        sur.add(lbPromedio);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBackground(FONDO);

        add(norte, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);
    }

    private void cargarMateriasEnCombo() {
        comboMaterias.removeAllItems();
        comboMaterias.addItem("Seleccione materia");
        String sql = "CALL obtener_materias_docente(?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, profesor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) comboMaterias.addItem(rs.getString("nombre"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar materias: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarCombo() {
        comboMaterias.addActionListener(e -> {
            String sel = (String) comboMaterias.getSelectedItem();
            if (sel == null || sel.equals("Seleccione materia")) {
                materiaSeleccionada = null;
                lbMateria.setText("Materia: Seleccione una materia");
                modelo.setRowCount(0);
                return;
            }
            materiaSeleccionada = sel;
            materiaId = obtenerIdMateria(sel);
            carreraId = obtenerCarreraIdPorMateria(materiaId);
            corteSeleccionado = 1;
            filasEditadas.clear();
            cargarTablaConEdicion();
        });
    }

    private void configurarBotonGuardar() {
        btnGuardar.addActionListener(e -> guardarNotas());
    }

    private int obtenerIdMateria(String nombre) {
        int id = -1;
        String sql = "SELECT id FROM Materias WHERE nombre = ? LIMIT 1";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) id = rs.getInt("id");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    private int obtenerCarreraIdPorMateria(int materiaId) {
        int id = 0;
        String sql = "{CALL obtener_carrera_de_materia(?)}";
        try (Connection conn = ConexionBD.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, materiaId);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) id = rs.getInt("carrera_id");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    private int obtenerDocenteMateriaId() {
        int id = -1;
        String sql = "CALL get_docente_materia_id(?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, profesor);
            ps.setString(2, materiaSeleccionada);
            ps.setInt(3, carreraId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) id = rs.getInt("id");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    private void cargarTablaConEdicion() {
        modelo.setRowCount(0);
        if (materiaSeleccionada == null) return;

        docenteMateriaId = obtenerDocenteMateriaId();
        double suma = 0;
        int cnt = 0;

        String sql = "CALL listar_notas_docente_materia(?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, profesor);
            ps.setString(2, materiaSeleccionada);
            ps.setInt(3, carreraId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int alumnoId = rs.getInt("alumno_id");
                String estudiante = rs.getString("estudiante");
                double c1 = rs.getDouble("corte1");
                double c2 = rs.getDouble("corte2");
                double c3 = rs.getDouble("corte3");
                int c1e = rs.getInt("corte1_edit");
                int c2e = rs.getInt("corte2_edit");
                int c3e = rs.getInt("corte3_edit");
                double prom = (c1 + c2 + c3) / 3.0;

                modelo.addRow(new Object[]{alumnoId, estudiante, c1, c2, c3, String.format("%.1f", prom), c1e, c2e, c3e});
                suma += prom;
                cnt++;
            }

            lbPromedio.setText(cnt > 0 ? String.format("Promedio del grupo: %.1f", suma / cnt) : "0.0");
            ocultarColumnas(0, 6, 7, 8);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar notas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void guardarNotas() {
        if (filasEditadas.isEmpty()) return;
        if (tabla.isEditing()) tabla.getCellEditor().stopCellEditing();

        int dmId = obtenerDocenteMateriaId();
        if (dmId == -1) return;

        // ✅ VALIDACIÓN ANTES DE GUARDAR
        for (Integer row : filasEditadas) {
            try {
                String valor = modelo.getValueAt(row, corteSeleccionado + 1).toString();
                if (!valor.isEmpty()) {
                    double nota = Double.parseDouble(valor);
                    if (nota < 0.0 || nota > 5.0) {
                        JOptionPane.showMessageDialog(this, 
                            "❌ La nota debe estar entre 0.0 y 5.0\n\n" +
                            "Alumno: " + modelo.getValueAt(row, 1) + "\n" +
                            "Corte: " + corteSeleccionado + "\n" +
                            "Nota ingresada: " + nota, 
                            "Error de validación", JOptionPane.ERROR_MESSAGE);
                        return; // No guardar si hay nota inválida
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Valor numérico inválido en la fila " + (row + 1), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String sql = "CALL actualizar_nota(?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            for (Integer row : filasEditadas) {
                int alumnoId = (int) modelo.getValueAt(row, 0);
                String valor = modelo.getValueAt(row, corteSeleccionado + 1).toString();
                if (valor.isEmpty()) continue; // Ignorar vacío
                
                double nota = Double.parseDouble(valor);
                int editCol = corteSeleccionado + 5;
                if (!"0".equals(modelo.getValueAt(row, editCol).toString())) continue;

                cs.setInt(1, alumnoId);
                cs.setInt(2, corteSeleccionado);
                cs.setInt(3, dmId);
                cs.setDouble(4, nota);
                cs.addBatch();
            }
            cs.executeBatch();
            filasEditadas.clear();
            JOptionPane.showMessageDialog(this, "✅ Notas guardadas (Corte " + corteSeleccionado + ")");
            corteSeleccionado++;
            if (corteSeleccionado > 3) {
                JOptionPane.showMessageDialog(this, "Todos los cortes han sido guardados.");
            }
            cargarTablaConEdicion();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ NUEVO: Configura validación en tiempo real para las celdas de notas
    private void configurarEditorDeNotas() {
        TableCellEditor notaEditor = new DefaultCellEditor(createNotaTextField()) {
            @Override
            public boolean stopCellEditing() {
                String valor = (String) getCellEditorValue();
                if (valor != null && !valor.isEmpty()) {
                    try {
                        double nota = Double.parseDouble(valor);
                        if (nota < 0.0 || nota > 5.0) {
                            JOptionPane.showMessageDialog(PanelAsignarNotasPorMateria.this,
                                "La nota debe estar entre 0.0 y 5.0", "Error", JOptionPane.ERROR_MESSAGE);
                            return false; // No permitir terminar edición
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(PanelAsignarNotasPorMateria.this,
                            "Ingrese un valor numérico válido", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                return super.stopCellEditing();
            }
        };
        
        // Asignar a las columnas de notas (2, 3, 4)
        tabla.getColumnModel().getColumn(2).setCellEditor(notaEditor);
        tabla.getColumnModel().getColumn(3).setCellEditor(notaEditor);
        tabla.getColumnModel().getColumn(4).setCellEditor(notaEditor);
    }

    // ✅ NUEVO: Crea JTextField con filtro para solo permitir 0.0-5.0
    private JTextField createNotaTextField() {
        JTextField textField = new JTextField();
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String nuevoTexto = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                if (esInputValido(nuevoTexto)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                String nuevoTexto = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (esInputValido(nuevoTexto)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean esInputValido(String input) {
                if (input.isEmpty()) return true;
                // Permite: vacío, dígitos, punto decimal, máximo "5" o "4.9", "4.99"
                if (input.matches("\\d{1}(\\.\\d{0,2})?")) {
                    try {
                        double val = Double.parseDouble(input);
                        return val >= 0.0 && val <= 5.0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
                return false;
            }
        });
        return textField;
    }

    private void ocultarColumnasDesdeInicio() {
        ocultarColumnas(0, 6, 7, 8);
    }

    private void ocultarColumnas(int... cols) {
        for (int c : cols) {
            tabla.getColumnModel().getColumn(c).setMinWidth(0);
            tabla.getColumnModel().getColumn(c).setMaxWidth(0);
            tabla.getColumnModel().getColumn(c).setPreferredWidth(0);
        }
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated*/
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaNotas = new javax.swing.JTable();

        tablaNotas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaNotas);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaNotas;
    // End of variables declaration//GEN-END:variables
}
