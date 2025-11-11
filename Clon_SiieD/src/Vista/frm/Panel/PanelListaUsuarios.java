package Vista.frm.Panel;

import Clases.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelListaUsuarios extends JPanel {

    private final Base_De_Datos baseDatos;
    private final String usuarioDocente;
    private final JPanel panelRoles = new JPanel();
    private final ButtonGroup grupoRoles = new ButtonGroup();
    private final JTable tablaUsuarios = new JTable();
    private final DefaultTableModel modeloTabla =
            new DefaultTableModel(
                    new Object[]{
                            "CC", "Nombre", "Segundo Nombre", "Apellido", "Segundo Apellido", 
                            "Edad", "Género", "Dirección", "Teléfono", "Correo"
                    }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    public PanelListaUsuarios(Base_De_Datos baseDatos, String usuarioDocente) {
        this.baseDatos = baseDatos;
        this.usuarioDocente = usuarioDocente;
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponentes();
        cargarRoles();

    }

    private void initComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelRoles.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelRoles.setBorder(BorderFactory.createTitledBorder("Roles"));
        JScrollPane scrollRoles = new JScrollPane(panelRoles);
        scrollRoles.setPreferredSize(new Dimension(600, 80));
        scrollRoles.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollRoles.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(scrollRoles, BorderLayout.WEST);
        add(panelNorte, BorderLayout.NORTH);

        tablaUsuarios.setModel(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.setGridColor(Color.LIGHT_GRAY);
        tablaUsuarios.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Listado de usuarios"));
        add(scrollTabla, BorderLayout.CENTER);
    }

    private void cargarRoles() {
        panelRoles.removeAll();
        grupoRoles.clearSelection();

        try {
            List<String> roles = baseDatos.obtenerRolesUsuarios();
            for (String rol : roles) {
                JRadioButton rb = new JRadioButton(rol);
                grupoRoles.add(rb);
                panelRoles.add(rb);
                rb.addActionListener(e -> cargarUsuariosPorRol(rol));
            }

            if (grupoRoles.getButtonCount() > 0) {
                JRadioButton primero = (JRadioButton) grupoRoles.getElements().nextElement();
                primero.setSelected(true);
            }

            panelRoles.revalidate();
            panelRoles.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar roles", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarUsuariosPorRol(String rol) {
        modeloTabla.setRowCount(0);

        try {
            List<Object[]> usuarios = baseDatos.listarUsuariosPorRol(rol);
            if (usuarios.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay usuarios con el rol: " + rol);
                return;
            }

            for (Object[] usuario : usuarios) {
                modeloTabla.addRow(usuario);
            }
            
            AjustesObjetos.ajustarTabla(tablaUsuarios);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar usuarios", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
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
