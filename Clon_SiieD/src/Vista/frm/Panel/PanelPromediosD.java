package Vista.frm.Panel;

import Clases.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanelPromediosD extends javax.swing.JPanel {

    private final String docente;
    private String materiaSeleccionada = null;

    private JRadioButton rbAlto, rbMedio, rbBajo;
    private ButtonGroup bgDesempeno;
    private JTable tablaNotas;
    private JLabel lbPromedio;
    private JPanel jpanelGrupos;

    public PanelPromediosD(String docente) {
        this.docente = docente;
        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(214, 245, 255));
        initComponentes();
        cargarMateriasDelDocente();
        agregarFiltrosDesempeno();
    }

    /* -------------------- INICIALIZAR COMPONENTES -------------------- */
    private void initComponentes() {
        // NORTE
        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.setBackground(new Color(214, 245, 255));
        norte.add(new JLabel("Promedio del grupo:", SwingConstants.LEFT));
        lbPromedio = new JLabel("0.0");
        lbPromedio.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        norte.add(lbPromedio);
        add(norte, BorderLayout.NORTH);

        // CENTRO – tabla
        tablaNotas = new JTable();
        tablaNotas.setModel(new DefaultTableModel(
                new Object[]{"Estudiante", "Corte 1", "Corte 2", "Corte 3", "Promedio"}, 0));
        add(new JScrollPane(tablaNotas), BorderLayout.CENTER);

        // ESTE – filtros
        jpanelGrupos = new JPanel();
        jpanelGrupos.setLayout(new BoxLayout(jpanelGrupos, BoxLayout.Y_AXIS));
        jpanelGrupos.setBackground(new Color(240, 244, 248));

        JPanel panelEste = new JPanel(new BorderLayout());
        panelEste.setPreferredSize(new Dimension(220, 0));
        panelEste.setBackground(new Color(240, 244, 248));
        panelEste.add(jpanelGrupos, BorderLayout.NORTH);
        add(panelEste, BorderLayout.EAST);
    }

    /* -------------------- CARGAR MATERIAS -------------------- */
    private void cargarMateriasDelDocente() {
        jpanelGrupos.removeAll();
        String sql = "CALL obtener_materias_docente(?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, docente);
            ResultSet rs = ps.executeQuery();
            ButtonGroup grupo = new ButtonGroup();
            while (rs.next()) {
                final String materiaNombre = rs.getString("nombre");
                JRadioButton rb = new JRadioButton(materiaNombre);
                rb.setAlignmentX(Component.LEFT_ALIGNMENT);
                rb.addActionListener(e -> {
                    materiaSeleccionada = materiaNombre;
                    cargarTablaConPromedio();
                });
                grupo.add(rb);
                jpanelGrupos.add(rb);
            }
            if (grupo.getButtonCount() > 0) {
                ((JRadioButton) grupo.getElements().nextElement()).setSelected(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar materias: " + e.getMessage());
            e.printStackTrace();
        }

        jpanelGrupos.add(new JSeparator());
        jpanelGrupos.revalidate();
        jpanelGrupos.repaint();
    }

    /* -------------------- FILTROS DESEMPAÑO -------------------- */
    private void agregarFiltrosDesempeno() {
        jpanelGrupos.add(Box.createVerticalStrut(10));
        jpanelGrupos.add(new JLabel("Filtrar por:"));
        bgDesempeno = new ButtonGroup();

        rbAlto = new JRadioButton("Alto (4.0 - 5.0)");
        rbMedio = new JRadioButton("Medio (3.0 - 3.9)");
        rbBajo = new JRadioButton("Bajo (0.0 - 2.9)");

        rbAlto.addActionListener(e -> cargarTablaConPromedio());
        rbMedio.addActionListener(e -> cargarTablaConPromedio());
        rbBajo.addActionListener(e -> cargarTablaConPromedio());

        bgDesempeno.add(rbAlto);
        bgDesempeno.add(rbMedio);
        bgDesempeno.add(rbBajo);

        jpanelGrupos.add(rbAlto);
        jpanelGrupos.add(rbMedio);
        jpanelGrupos.add(rbBajo);
        jpanelGrupos.add(Box.createVerticalGlue());

        rbAlto.setSelected(true);
    }

    /* -------------------- CARGAR TABLA CON PROMEDIO -------------------- */
    private void cargarTablaConPromedio() {
        if (materiaSeleccionada == null) return;

        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Estudiante", "Corte 1", "Corte 2", "Corte 3", "Promedio"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        double suma = 0;
        int cnt = 0;

        String sql = "CALL listar_notas_docente_materia(?, ?, NULL)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, docente);
            ps.setString(2, materiaSeleccionada);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String estudiante = rs.getString("estudiante");
                double c1 = rs.getDouble("corte1");
                double c2 = rs.getDouble("corte2");
                double c3 = rs.getDouble("corte3");
                double promedio = (c1 + c2 + c3) / 3.0;

                boolean incluir = true;
                if (rbAlto.isSelected())  incluir = (promedio >= 4.0 && promedio <= 5.0);
                if (rbMedio.isSelected()) incluir = (promedio >= 3.0 && promedio <= 3.9);
                if (rbBajo.isSelected())  incluir = (promedio >= 0.0 && promedio <= 2.9);
                if (!incluir) continue;

                modelo.addRow(new Object[]{
                        estudiante,
                        String.format("%.1f", c1),
                        String.format("%.1f", c2),
                        String.format("%.1f", c3),
                        String.format("%.1f", promedio)
                });
                suma += promedio;
                cnt++;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tabla: " + e.getMessage());
            e.printStackTrace();
        }

        tablaNotas.setModel(modelo);
        lbPromedio.setText(cnt > 0 ? String.format("%.1f", suma / cnt) : "0.0");
    }

    /* -------------------- GENERATED -------------------- */
         
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
