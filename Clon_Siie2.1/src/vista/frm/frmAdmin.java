package vista.frm;

import clases.AjustesObjetos;
import clases.EfectoHoverPanel;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;


public class frmAdmin extends javax.swing.JFrame {
        
        
  public frmAdmin() {
    initComponents();
    btnSalir.setVisible(false);
    setTitle("Siie administrativo");
    setSize(1208, 680);
    setResizable(false);
    setLocationRelativeTo(null);
    this.getContentPane().setBackground(new Color(155, 234, 234));

    new EfectoHoverPanel(new Color(155, 134, 120)).aplicarEfecto(DListadosAlumnos);
    new EfectoHoverPanel(new Color(155, 134, 120)).aplicarEfecto(DNotasAlumnos);
    new EfectoHoverPanel(new Color(155, 134, 120)).aplicarEfecto(Dpromedio);
    new EfectoHoverPanel(new Color(155, 134, 120)).aplicarEfecto(DReportes);
    new EfectoHoverPanel(new Color(155, 134, 120)).aplicarEfecto(Ddesempeño);
        

    AjustesObjetos.ajustarImagen(lbicono, "src\\imagenes\\foto.png");
    AjustesObjetos.ajustarImagen(lbFOTO,"src\\imagenes\\Listados.png" );
    AjustesObjetos.ajustarImagen(lbCbn, "src\\imagenes\\Corporacion-Bolivariana-del-Norte-CBN.png");
    AjustesObjetos.ajustarImagen(lbNotas, "src\\imagenes\\Notas.png");
    AjustesObjetos.ajustarImagen(lbPromedio, "src\\imagenes\\Promedio.png");
    AjustesObjetos.ajustarImagen(lbReportes, "src\\imagenes\\Reportes.png");
    AjustesObjetos.ajustarImagen(lbDesempeño, "src\\imagenes\\desempeño.png");
  
    // ===== Separadores para menuDocente =====
    JPanel sepDoc1 = crearSeparador(new Color(255, 255, 0), 40);
    JPanel sepDoc2 = crearSeparador(new Color(255, 255, 0), 40);
    JPanel sepDoc3 = crearSeparador(new Color(255, 255, 0), 40);
    JPanel sepDoc4 = crearSeparador(new Color(255, 255, 0), 40);
    JPanel sepDoc5 = crearSeparador(new Color(255, 255, 0), 40);

    // ===== Separadores para menuDocente =====
    JPanel sepReg1 = crearSeparador(new Color(255, 255, 0), 40);
    JPanel sepReg2 = crearSeparador(new Color(255, 255, 0), 40);
    JPanel sepReg3 = crearSeparador(new Color(255, 255, 0), 40);
    JPanel sepReg4 = crearSeparador(new Color(255, 255, 0), 40);
    JPanel sepReg5 = crearSeparador(new Color(255, 255, 0), 40);
    
    // ===== Separadores para menuAdmin =====
    JPanel sepAdm1 = crearSeparador(new Color(255, 255, 0), 40);
    JPanel sepAdm2 = crearSeparador(new Color(255, 255, 0), 40);
    JPanel sepAdm3 = crearSeparador(new Color(255, 255, 0), 40);
    JPanel sepAdm4 = crearSeparador(new Color(255, 255, 0), 40);
    JPanel sepAdm5 = crearSeparador(new Color(255, 255, 0), 40);

        
    // Usar CardLayout
    JpanelMenu.setLayout(new java.awt.CardLayout());

    DListadosAlumnos.setMaximumSize(new Dimension(379, 55));    
    Ddesempeño.setMaximumSize(new Dimension(379, 55));
    DNotasAlumnos.setMaximumSize(new Dimension(379, 55));
    DReportes.setMaximumSize(new Dimension(379, 55));
    Dpromedio.setMaximumSize(new Dimension(379, 55));
    AAsignar_QuitarMateria.setMaximumSize(new Dimension(379, 55));
    ACrearCarrera.setMaximumSize(new Dimension(379, 55));
    ACrearMaterias.setMaximumSize(new Dimension(379, 55));
    AReporteAcademico.setMaximumSize(new Dimension(379, 55));

    // Panel de menú ADMIN
    JPanel menuAdmin = new JPanel();
    
    menuAdmin.setBackground(new Color(255, 255, 0));
    menuAdmin.setLayout(new BoxLayout(menuAdmin, BoxLayout.Y_AXIS));
    
    menuAdmin.add(sepAdm1); 
    menuAdmin.add(ACrearMaterias);
    
    menuAdmin.add(sepAdm2); 
    menuAdmin.add(ACrearCarrera);
    
    menuAdmin.add(sepAdm3); 
    menuAdmin.add(AReporteAcademico);
    
    menuAdmin.add(sepAdm4); 
    menuAdmin.add(AAsignar_QuitarMateria);
    menuAdmin.add(sepAdm5); 
    
    
    // Panel de menú DOCENTE
    JPanel menuDocente = new JPanel();
    menuDocente.setLayout(new BoxLayout(menuDocente, BoxLayout.Y_AXIS));

    menuDocente.setBackground(new Color(255, 255, 0));
    menuDocente.add(sepDoc1);
    menuDocente.add(DListadosAlumnos);

    menuDocente.add(sepDoc2);
    menuDocente.add(DNotasAlumnos);

    menuDocente.add(sepDoc3);
    menuDocente.add(Dpromedio);

    menuDocente.add(sepDoc4);
    menuDocente.add(DReportes);

    menuDocente.add(sepDoc5);
    menuDocente.add(Ddesempeño);
    
    
    // Agregar ambos al CardLayout
    JpanelMenu.setLayout(new CardLayout());
    JpanelMenu.add(menuAdmin, "ADMIN");
    JpanelMenu.add(menuDocente, "DOCENTE");

    // Mostrar el menú según el rol
    String jose = "akl";
    CardLayout cl = (CardLayout) JpanelMenu.getLayout();

    if (jose.equals("admin")) {
        cl.show(JpanelMenu, "ADMIN");
    } else {
        cl.show(JpanelMenu, "DOCENTE");
    }
  }
    
    private JPanel crearSeparador(Color color, int alto) {
    JPanel sep = new JPanel();
    sep.setBackground(color);
    sep.setPreferredSize(new Dimension(150, alto));
    sep.setMaximumSize(new Dimension(150, alto));
    sep.setAlignmentX(LEFT_ALIGNMENT);
    return sep;
}

    
  
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JpanelMenu = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lbicono = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        DListadosAlumnos = new javax.swing.JPanel();
        lbListado = new javax.swing.JLabel();
        lbFOTO = new javax.swing.JLabel();
        DNotasAlumnos = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lbNotas = new javax.swing.JLabel();
        Dpromedio = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lbPromedio = new javax.swing.JLabel();
        DReportes = new javax.swing.JPanel();
        lbListado1 = new javax.swing.JLabel();
        lbReportes = new javax.swing.JLabel();
        Ddesempeño = new javax.swing.JPanel();
        lbDesempeño = new javax.swing.JLabel();
        lbListado2 = new javax.swing.JLabel();
        ACrearMaterias = new javax.swing.JPanel();
        ACrearCarrera = new javax.swing.JPanel();
        AReporteAcademico = new javax.swing.JPanel();
        AAsignar_QuitarMateria = new javax.swing.JPanel();
        RCModificarDatos = new javax.swing.JPanel();
        RCMatricular = new javax.swing.JPanel();
        RCListadoUsuarios = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lbCbn = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JpanelMenu.setBackground(new java.awt.Color(255, 255, 0));
        JpanelMenu.setMinimumSize(new java.awt.Dimension(200, 465));
        JpanelMenu.setLayout(null);
        getContentPane().add(JpanelMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 190, 580));

        jPanel3.setBackground(new java.awt.Color(0, 255, 153));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbicono.setText("jose");
        lbicono.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbFoto(evt);
            }
        });
        jPanel3.add(lbicono, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 0, 110, 100));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Corporacion Bolibariana del Norte");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, -1, -1));

        btnSalir.setText("Cerrar Seccion");
        jPanel3.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, -1, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1020, 100));

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1000, 600));

        jPanel4.setPreferredSize(new java.awt.Dimension(100, 500));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1020, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 545, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("contabilidad", jPanel4);

        jPanel5.setPreferredSize(new java.awt.Dimension(1000, 600));

        DListadosAlumnos.setLayout(null);

        lbListado.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbListado.setForeground(new java.awt.Color(0, 0, 0));
        lbListado.setText("Listado de alumnos");
        DListadosAlumnos.add(lbListado);
        lbListado.setBounds(50, 20, 140, 20);

        lbFOTO.setText("jLabel2");
        DListadosAlumnos.add(lbFOTO);
        lbFOTO.setBounds(0, 10, 50, 40);

        DNotasAlumnos.setBackground(new java.awt.Color(204, 204, 0));
        DNotasAlumnos.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Asignar Notas");
        DNotasAlumnos.add(jLabel2);
        jLabel2.setBounds(50, 20, 100, 20);

        lbNotas.setText("jLabel3");
        DNotasAlumnos.add(lbNotas);
        lbNotas.setBounds(0, 10, 50, 40);

        Dpromedio.setBackground(new java.awt.Color(0, 255, 102));
        Dpromedio.setLayout(null);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Promedios");
        Dpromedio.add(jLabel3);
        jLabel3.setBounds(50, 20, 80, 20);

        lbPromedio.setText("jLabel4");
        Dpromedio.add(lbPromedio);
        lbPromedio.setBounds(0, 10, 48, 40);

        DReportes.setBackground(new java.awt.Color(0, 51, 51));
        DReportes.setLayout(null);

        lbListado1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbListado1.setForeground(new java.awt.Color(0, 0, 0));
        lbListado1.setText("Reportes Academico");
        DReportes.add(lbListado1);
        lbListado1.setBounds(50, 20, 150, 20);

        lbReportes.setText("jLabel4");
        DReportes.add(lbReportes);
        lbReportes.setBounds(0, 10, 50, 40);

        Ddesempeño.setBackground(new java.awt.Color(255, 204, 51));
        Ddesempeño.setLayout(null);

        lbDesempeño.setText("jLabel4");
        Ddesempeño.add(lbDesempeño);
        lbDesempeño.setBounds(0, 10, 50, 40);

        lbListado2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbListado2.setForeground(new java.awt.Color(0, 0, 0));
        lbListado2.setText("Desempeño");
        Ddesempeño.add(lbListado2);
        lbListado2.setBounds(50, 20, 150, 20);

        ACrearMaterias.setBackground(new java.awt.Color(0, 204, 153));
        ACrearMaterias.setLayout(null);

        ACrearCarrera.setBackground(new java.awt.Color(204, 0, 51));
        ACrearCarrera.setLayout(null);

        AReporteAcademico.setBackground(new java.awt.Color(102, 0, 102));
        AReporteAcademico.setLayout(null);

        AAsignar_QuitarMateria.setBackground(new java.awt.Color(0, 255, 255));
        AAsignar_QuitarMateria.setLayout(null);

        RCModificarDatos.setBackground(new java.awt.Color(0, 153, 153));
        RCModificarDatos.setLayout(null);

        RCMatricular.setBackground(new java.awt.Color(204, 0, 204));
        RCMatricular.setLayout(null);

        RCListadoUsuarios.setBackground(new java.awt.Color(0, 0, 0));
        RCListadoUsuarios.setLayout(null);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(RCListadoUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(334, 334, 334))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(AReporteAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Ddesempeño, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(67, 67, 67)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(199, 199, 199)
                                .addComponent(RCMatricular, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(ACrearMaterias, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ACrearCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(AAsignar_QuitarMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60)
                                .addComponent(RCModificarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(DReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(DNotasAlumnos, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addComponent(Dpromedio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DListadosAlumnos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(377, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DNotasAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(Dpromedio, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DListadosAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Ddesempeño, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(127, 127, 127)
                        .addComponent(AReporteAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(ACrearMaterias, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(ACrearCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(AAsignar_QuitarMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(RCModificarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(RCMatricular, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)))
                        .addComponent(RCListadoUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(112, 112, 112))))
        );

        jTabbedPane1.addTab("tab2", jPanel5);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 1020, 580));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCbn.setText("jLabel1");
        jPanel2.add(lbCbn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 100));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 100));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lbFoto(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbFoto
        // TODO add your handling code here:
        boolean estafo=btnSalir.isVisible();
        
        if(estafo){
            btnSalir.setVisible(false);
        }else{
            btnSalir.setVisible(true);
        }
        
        
    }//GEN-LAST:event_lbFoto

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AAsignar_QuitarMateria;
    private javax.swing.JPanel ACrearCarrera;
    private javax.swing.JPanel ACrearMaterias;
    private javax.swing.JPanel AReporteAcademico;
    private javax.swing.JPanel DListadosAlumnos;
    private javax.swing.JPanel DNotasAlumnos;
    private javax.swing.JPanel DReportes;
    private javax.swing.JPanel Ddesempeño;
    private javax.swing.JPanel Dpromedio;
    private javax.swing.JPanel JpanelMenu;
    private javax.swing.JPanel RCListadoUsuarios;
    private javax.swing.JPanel RCMatricular;
    private javax.swing.JPanel RCModificarDatos;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbCbn;
    private javax.swing.JLabel lbDesempeño;
    private javax.swing.JLabel lbFOTO;
    private javax.swing.JLabel lbListado;
    private javax.swing.JLabel lbListado1;
    private javax.swing.JLabel lbListado2;
    private javax.swing.JLabel lbNotas;
    private javax.swing.JLabel lbPromedio;
    private javax.swing.JLabel lbReportes;
    private javax.swing.JLabel lbicono;
    // End of variables declaration//GEN-END:variables
}
