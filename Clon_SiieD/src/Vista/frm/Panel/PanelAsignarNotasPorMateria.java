package Vista.frm.Panel;

import Clases.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
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
    private Base_De_Datos basedatos;
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
                return corteColumna == corteSeleccionado; 
            }
            return false;
        }
    };

    public PanelAsignarNotasPorMateria(String profesor, Base_De_Datos basedatos) {
        this.profesor = profesor;
        this.basedatos = basedatos;
        AjustesObjetos.ajustarTabla(tabla);
        tabla.setModel(modelo);
        initUI();
        cargarMateriasEnCombo();
        configurarCombo();
        configurarBotonGuardar();
        configurarEditorDeNotas();
        ocultarColumnasDesdeInicio();
        AjustesObjetos.ajustarTabla(tabla);
        modelo.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() >= 2 && e.getColumn() <= 4) {
                filasEditadas.add(e.getFirstRow());
            }
        });
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(FONDO);
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setSelectionBackground(new Color(184, 207, 229));
        
        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.setBackground(FONDO);
        norte.add(new JLabel("Materia:"));
        norte.add(comboMaterias);
        norte.add(btnGuardar);
        norte.add(lbMateria);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sur.setBackground(FONDO);
        sur.add(lbPromedio);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);
    }

    private void cargarMateriasEnCombo() {
        comboMaterias.removeAllItems();
        comboMaterias.addItem("Seleccione materia");
        
        try {
            List<String> materias = basedatos.obtenerMateriasDelDocente(profesor);
            for (String materia : materias) {
                comboMaterias.addItem(materia);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar materias: " + e.getMessage());
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
            int materiaId = basedatos.obtenerIdMateriaPorDocente(sel, profesor);
            carreraId = basedatos.obtenerCarreraIdPorMateria(materiaId);
            corteSeleccionado = 1;
            filasEditadas.clear();
            cargarTablaConEdicion();
        });
    }

    private void configurarBotonGuardar() {
        btnGuardar.addActionListener(e -> guardarNotas());
    }

    private void cargarTablaConEdicion() {
        modelo.setRowCount(0);
        if (materiaSeleccionada == null) return;

        docenteMateriaId = basedatos.obtenerDocenteMateriaId(profesor, materiaSeleccionada, carreraId);
        double suma = 0;
        int cnt = 0;

        try {
            List<Object[]> notas = basedatos.listarNotasDocenteMateria(profesor, materiaSeleccionada, carreraId);
            
            for (Object[] fila : notas) {
                int alumnoId = (int) fila[0];
                String estudiante = (String) fila[1];
                double c1 = (double) fila[2];
                double c2 = (double) fila[3];
                double c3 = (double) fila[4];
                int c1e = (int) fila[5];
                int c2e = (int) fila[6];
                int c3e = (int) fila[7];
                double prom = (c1 + c2 + c3) / 3.0;

                modelo.addRow(new Object[]{alumnoId, estudiante, c1, c2, c3, String.format("%.1f", prom), c1e, c2e, c3e});
                suma += prom;
                cnt++;
            }

            lbPromedio.setText(cnt > 0 ? String.format("Promedio del grupo: %.1f", suma / cnt) : "0.0");
            ocultarColumnas(0, 6, 7, 8);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar notas: " + e.getMessage());
        }
    }

    private void guardarNotas() {
        if (filasEditadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay notas nuevas para guardar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (tabla.isEditing()) tabla.getCellEditor().stopCellEditing();

        int dmId = basedatos.obtenerDocenteMateriaId(profesor, materiaSeleccionada, carreraId);
        if (dmId == -1) {
            JOptionPane.showMessageDialog(this, "❌ ERROR: No se pudo obtener el ID del docente-materia", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // VALIDACIÓN DE RANGO ANTES DE GUARDAR
        for (Integer row : filasEditadas) {
            try {
                String valor = modelo.getValueAt(row, corteSeleccionado + 1).toString().trim();
                if (!valor.isEmpty()) {
                    double nota = Double.parseDouble(valor);
                    if (nota < 0.0 || nota > 5.0) {
                        String estudiante = modelo.getValueAt(row, 1).toString();
                        JOptionPane.showMessageDialog(this, 
                            "❌ NOTA FUERA DE RANGO PERMITIDO\n\nEstudiante: " + estudiante + 
                            "\nCorte: " + corteSeleccionado + "\nNota: " + nota + 
                            "\n\nLas notas deben estar entre 0.0 y 5.0", 
                            "Error de Validación", JOptionPane.ERROR_MESSAGE);
                        tabla.setRowSelectionInterval(row, row);
                        tabla.setColumnSelectionInterval(corteSeleccionado + 1, corteSeleccionado + 1);
                        return;
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ VALOR INVÁLIDO EN LA FILA " + (row + 1) + "\nIngrese solo números decimales (ej: 3.5)", 
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Preparar datos para batch
        List<Object[]> batchData = new ArrayList<>();
        for (Integer row : filasEditadas) {
            int alumnoId = (int) modelo.getValueAt(row, 0);
            String valor = modelo.getValueAt(row, corteSeleccionado + 1).toString().trim();
            
            if (valor.isEmpty()) continue;
            
            double nota = Double.parseDouble(valor);
            batchData.add(new Object[]{alumnoId, corteSeleccionado, dmId, nota});
        }
        
        if (!batchData.isEmpty()) {
            try {
                basedatos.actualizarNotasBatch(batchData);
                filasEditadas.clear();
                
                JOptionPane.showMessageDialog(this, 
                    "✅ NOTAS GUARDADAS EXITOSAMENTE\nCorte: " + corteSeleccionado);
                
                corteSeleccionado++;
                if (corteSeleccionado > 3) {
                    JOptionPane.showMessageDialog(this, 
                        "🎉 TODOS LOS CORTES COMPLETADOS\nYa no puede editar más notas.", 
                        "Proceso Completo", JOptionPane.INFORMATION_MESSAGE);
                }
                
                // Recarga la tabla para mostrar los datos actualizados
                cargarTablaConEdicion();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "❌ ERROR AL GUARDAR: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "No se encontraron notas válidas para guardar.", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void configurarEditorDeNotas() {
        TableCellEditor notaEditor = new DefaultCellEditor(createNotaTextField()) {
            @Override
            public boolean stopCellEditing() {
                String valor = (String) getCellEditorValue();
                if (valor != null && !valor.trim().isEmpty()) {
                    try {
                        double nota = Double.parseDouble(valor.trim());
                        if (nota < 0.0 || nota > 5.0) {
                            JOptionPane.showMessageDialog(PanelAsignarNotasPorMateria.this,
                                "❌ LA NOTA DEBE ESTAR ENTRE 0.0 Y 5.0\nValor ingresado: " + nota, 
                                "Error de Rango", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(PanelAsignarNotasPorMateria.this,
                            "❌ VALOR NUMÉRICO INVÁLIDO\nIngrese solo números (ej: 3.5)", 
                            "Error de Formato", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                return super.stopCellEditing();
            }
        };
        
        for (int i = 2; i <= 4; i++) {
            tabla.getColumnModel().getColumn(i).setCellEditor(notaEditor);
        }
    }

    private JTextField createNotaTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.BOLD, 18));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                StringBuilder nuevoTexto = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                nuevoTexto.insert(offset, string);
                if (esInputValido(nuevoTexto.toString())) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                StringBuilder nuevoTexto = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                nuevoTexto.replace(offset, offset + length, text);
                if (esInputValido(nuevoTexto.toString())) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean esInputValido(String input) {
                if (input.isEmpty()) return true;
                if (input.matches("^(5\\.?0?|[0-4]?\\.?\\d{0,2})$") || input.matches("^[0-4](\\.\\d{0,2})?$")) {
                    try {
                        if (input.endsWith(".") || input.equals("5") || input.equals("0")) return true;
                        double val = Double.parseDouble(input);
                        return val >= 0.0 && val <= 5.0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
                return false;
            }
        });
        
        textField.addActionListener(e -> {
            int row = tabla.getEditingRow();
            int col = tabla.getEditingColumn();
            SwingUtilities.invokeLater(() -> moveToNextEditableCell(row, col));
        });
        
        return textField;
    }

    private void moveToNextEditableCell(int currentRow, int currentCol) {
        if (currentRow == -1 || currentCol == -1) return;
        
        for (int row = currentRow + 1; row < modelo.getRowCount(); row++) {
            if (modelo.isCellEditable(row, currentCol)) {
                tabla.editCellAt(row, currentCol);
                tabla.setRowSelectionInterval(row, row);
                tabla.scrollRectToVisible(tabla.getCellRect(row, currentCol, true));
                return;
            }
        }
        
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Terminó de ingresar notas del Corte " + corteSeleccionado + "?\n\n" +
            "Presione 'Sí' para guardar o 'No' para continuar.", 
            "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            btnGuardar.doClick();
        }
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
