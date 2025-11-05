package Vista.frm.Panel;

import Clases.ConexionBD;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PanelRetirarMateriaADocente extends JPanel {

    private final JComboBox<String> comboDocentes = new JComboBox<>();
    private final JComboBox<String> comboMaterias = new JComboBox<>();
    private final JButton btnRetirar = new JButton("Retirar materia");
    private final JTextField txtBuscar = new JTextField(15);
    private final JButton btnBuscar = new JButton("Buscar");

    private final Map<String, Integer> docenteIdMap = new HashMap<>();
    private String docenteSeleccionado = null;

    public PanelRetirarMateriaADocente() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 254, 214));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initUI();
        cargarDocentes();
        configurarBusqueda();
        configurarBotonRetirar();
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

    /* -------------------- CARGAR DOCENTES -------------------- */
    private void cargarDocentes() {
        comboDocentes.removeAllItems();
        docenteIdMap.clear();

        String sql = "CALL listar_docentes()";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String display = nombre + " " + apellido;
                comboDocentes.addItem(display);
                docenteIdMap.put(display, id);
            }

            if (comboDocentes.getItemCount() > 0) {
                comboDocentes.setSelectedIndex(0);
                docenteSeleccionado = (String) comboDocentes.getSelectedItem();
                cargarMateriasDelDocente();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar docentes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* -------------------- CARGAR MATERIAS DEL DOCENTE -------------------- */
    private void cargarMateriasDelDocente() {
        comboMaterias.removeAllItems();
        String docente = (String) comboDocentes.getSelectedItem();
        if (docente == null) return;

        int idDocente = docenteIdMap.get(docente);

        String sql = "CALL obtener_materias_docente_por_id(?)"; // Ver procedimiento abajo
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comboMaterias.addItem(rs.getString("materia"));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar materias: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* -------------------- BUSCAR DOCENTES -------------------- */
    private void configurarBusqueda() {
        btnBuscar.addActionListener(e -> buscarDocentesPorNombre());
    }

    private void buscarDocentesPorNombre() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            cargarDocentes();
            return;
        }

        comboDocentes.removeAllItems();
        docenteIdMap.clear();

        String sql = "CALL buscar_docentes_por_nombre(?)"; // Ver procedimiento abajo
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, texto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String display = nombre + " " + apellido;
                    comboDocentes.addItem(display);
                    docenteIdMap.put(display, id);
                }
            }

            if (comboDocentes.getItemCount() > 0) {
                comboDocentes.setSelectedIndex(0);
                cargarMateriasDelDocente();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar docentes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* -------------------- RETIRAR MATERIA -------------------- */
    private void configurarBotonRetirar() {
        btnRetirar.addActionListener(e -> retirarMateria());
    }

    private void retirarMateria() {
        String materia = (String) comboMaterias.getSelectedItem();
        String docente = (String) comboDocentes.getSelectedItem();

        if (materia == null || docente == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un docente y una materia.");
            return;
        }

        int idDocente = docenteIdMap.get(docente);

        // Llamar al procedimiento para eliminar la asignación
        String sql = "CALL eliminar_asignacion_docente_materia(?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idDocente);
            cs.setString(2, materia);
            cs.execute();

            JOptionPane.showMessageDialog(this, "✅ Materia retirada correctamente.");
            comboMaterias.removeItem(materia); // Quitar del combo

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al retirar materia: " + e.getMessage());
            e.printStackTrace();
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
