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
    AjustesObjetos.ajustarImagen(lbCrearMateriaf, "C:\\Users\\PC\\OneDrive\\Escritorio\\tareas\\git\\Clon_Siee\\Clon_Siie2.1\\src\\imagenes\\56640216-college-education-outlined-and-colored-icons.png");
    AjustesObjetos.ajustarImagen(lbCrearcarreraF, "C:\\Users\\PC\\OneDrive\\Escritorio\\tareas\\git\\Clon_Siee\\Clon_Siie2.1\\src\\imagenes\\carrera.png");
    AjustesObjetos.ajustarImagen(lbQuitarMateriaf, "C:\\Users\\PC\\OneDrive\\Escritorio\\tareas\\git\\Clon_Siee\\Clon_Siie2.1\\src\\imagenes\\remover.png");
    AjustesObjetos.ajustarImagen(lbReportesA, "C:\\Users\\PC\\OneDrive\\Escritorio\\tareas\\git\\Clon_Siee\\Clon_Siie2.1\\src\\imagenes\\Dc.png");
  
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
    String jose = "admin";
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
        lbANotas = new javax.swing.JLabel();
        lbNotas = new javax.swing.JLabel();
        Dpromedio = new javax.swing.JPanel();
        lbPromedioG = new javax.swing.JLabel();
        lbPromedio = new javax.swing.JLabel();
        DReportes = new javax.swing.JPanel();
        lbReporteAD = new javax.swing.JLabel();
        lbReportes = new javax.swing.JLabel();
        Ddesempeño = new javax.swing.JPanel();
        lbDesempeño = new javax.swing.JLabel();
        lbListado2 = new javax.swing.JLabel();
        ACrearMaterias = new javax.swing.JPanel();
        lbCrearMateriaf = new javax.swing.JLabel();
        lbCrearMateria = new javax.swing.JLabel();
        ACrearCarrera = new javax.swing.JPanel();
        lbCrearCarrera = new javax.swing.JLabel();
        lbCrearcarreraF = new javax.swing.JLabel();
        AReporteAcademico = new javax.swing.JPanel();
        lbReporteAA = new javax.swing.JLabel();
        lbReportesA = new javax.swing.JLabel();
        AAsignar_QuitarMateria = new javax.swing.JPanel();
        lbQuitarMateria = new javax.swing.JLabel();
        lbQuitarMateriaf = new javax.swing.JLabel();
        RCModificarDatos = new javax.swing.JPanel();
        lbCrearMateria1 = new javax.swing.JLabel();
        lbModificarDatosf = new javax.swing.JLabel();
        RCMatricular = new javax.swing.JPanel();
        lbCrearMateria3 = new javax.swing.JLabel();
        lbingresaruserf = new javax.swing.JLabel();
        RCListadoUsuarios = new javax.swing.JPanel();
        lbListadoUsuarios = new javax.swing.JLabel();
        lblistausuariosf = new javax.swing.JLabel();
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

        lbANotas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbANotas.setForeground(new java.awt.Color(0, 0, 0));
        lbANotas.setText("Asignar Notas");
        DNotasAlumnos.add(lbANotas);
        lbANotas.setBounds(50, 20, 100, 20);

        lbNotas.setText("jLabel3");
        DNotasAlumnos.add(lbNotas);
        lbNotas.setBounds(0, 10, 50, 40);

        Dpromedio.setBackground(new java.awt.Color(0, 255, 102));
        Dpromedio.setLayout(null);

        lbPromedioG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbPromedioG.setForeground(new java.awt.Color(0, 0, 0));
        lbPromedioG.setText("Promedios");
        Dpromedio.add(lbPromedioG);
        lbPromedioG.setBounds(50, 20, 80, 20);

        lbPromedio.setText("jLabel4");
        Dpromedio.add(lbPromedio);
        lbPromedio.setBounds(0, 10, 48, 40);

        DReportes.setBackground(new java.awt.Color(0, 51, 51));
        DReportes.setLayout(null);

        lbReporteAD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbReporteAD.setForeground(new java.awt.Color(0, 0, 0));
        lbReporteAD.setText("Reportes Academico");
        DReportes.add(lbReporteAD);
        lbReporteAD.setBounds(50, 20, 150, 20);

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

        lbCrearMateriaf.setText("jLabel4");
        ACrearMaterias.add(lbCrearMateriaf);
        lbCrearMateriaf.setBounds(0, 10, 50, 40);

        lbCrearMateria.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbCrearMateria.setForeground(new java.awt.Color(0, 0, 0));
        lbCrearMateria.setText("Crear Materia");
        ACrearMaterias.add(lbCrearMateria);
        lbCrearMateria.setBounds(60, 20, 150, 20);

        ACrearCarrera.setBackground(new java.awt.Color(204, 0, 51));
        ACrearCarrera.setLayout(null);

        lbCrearCarrera.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbCrearCarrera.setForeground(new java.awt.Color(0, 0, 0));
        lbCrearCarrera.setText("Crear Carrera");
        ACrearCarrera.add(lbCrearCarrera);
        lbCrearCarrera.setBounds(60, 20, 150, 20);

        lbCrearcarreraF.setText("jLabel4");
        ACrearCarrera.add(lbCrearcarreraF);
        lbCrearcarreraF.setBounds(0, 10, 50, 40);

        AReporteAcademico.setBackground(new java.awt.Color(102, 0, 102));
        AReporteAcademico.setRequestFocusEnabled(false);
        AReporteAcademico.setLayout(null);

        lbReporteAA.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbReporteAA.setForeground(new java.awt.Color(0, 0, 0));
        lbReporteAA.setText("Reportes Academicos");
        AReporteAcademico.add(lbReporteAA);
        lbReporteAA.setBounds(50, 20, 150, 20);

        lbReportesA.setText("jLabel4");
        AReporteAcademico.add(lbReportesA);
        lbReportesA.setBounds(0, 10, 50, 40);

        AAsignar_QuitarMateria.setBackground(new java.awt.Color(0, 255, 255));
        AAsignar_QuitarMateria.setLayout(null);

        lbQuitarMateria.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbQuitarMateria.setForeground(new java.awt.Color(0, 0, 0));
        lbQuitarMateria.setText("Quitar Materia");
        AAsignar_QuitarMateria.add(lbQuitarMateria);
        lbQuitarMateria.setBounds(50, 10, 140, 30);

        lbQuitarMateriaf.setText("jLabel4");
        AAsignar_QuitarMateria.add(lbQuitarMateriaf);
        lbQuitarMateriaf.setBounds(-10, 0, 60, 50);

        RCModificarDatos.setBackground(new java.awt.Color(0, 153, 153));
        RCModificarDatos.setLayout(null);

        lbCrearMateria1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbCrearMateria1.setForeground(new java.awt.Color(0, 0, 0));
        lbCrearMateria1.setText("Modificar Datos");
        RCModificarDatos.add(lbCrearMateria1);
        lbCrearMateria1.setBounds(50, 20, 150, 20);

        lbModificarDatosf.setText("jLabel4");
        RCModificarDatos.add(lbModificarDatosf);
        lbModificarDatosf.setBounds(0, 10, 50, 40);

        RCMatricular.setBackground(new java.awt.Color(204, 0, 204));
        RCMatricular.setLayout(null);

        lbCrearMateria3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbCrearMateria3.setForeground(new java.awt.Color(0, 0, 0));
        lbCrearMateria3.setText("ingresar usuario");
        RCMatricular.add(lbCrearMateria3);
        lbCrearMateria3.setBounds(50, 20, 150, 20);
        RCMatricular.add(lbingresaruserf);
        lbingresaruserf.setBounds(0, 10, 50, 40);

        RCListadoUsuarios.setBackground(new java.awt.Color(0, 0, 0));
        RCListadoUsuarios.setLayout(null);

        lbListadoUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbListadoUsuarios.setForeground(new java.awt.Color(0, 0, 0));
        lbListadoUsuarios.setText("Listado de Usuarios");
        RCListadoUsuarios.add(lbListadoUsuarios);
        lbListadoUsuarios.setBounds(50, 20, 150, 20);

        lblistausuariosf.setText("jLabel4");
        RCListadoUsuarios.add(lblistausuariosf);
        lblistausuariosf.setBounds(0, 10, 50, 40);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(AReporteAcademico, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(ACrearCarrera, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addComponent(ACrearMaterias, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(AAsignar_QuitarMateria, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(DReportes, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addComponent(DNotasAlumnos, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addComponent(Dpromedio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DListadosAlumnos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Ddesempeño, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                        .addGap(92, 92, 92)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RCListadoUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(RCModificarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(RCMatricular, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(304, 511, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DNotasAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RCModificarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(Dpromedio, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RCListadoUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(DListadosAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(DReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(RCMatricular, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Ddesempeño, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(ACrearMaterias, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ACrearCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AAsignar_QuitarMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AReporteAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbANotas;
    private javax.swing.JLabel lbCbn;
    private javax.swing.JLabel lbCrearCarrera;
    private javax.swing.JLabel lbCrearMateria;
    private javax.swing.JLabel lbCrearMateria1;
    private javax.swing.JLabel lbCrearMateria3;
    private javax.swing.JLabel lbCrearMateriaf;
    private javax.swing.JLabel lbCrearcarreraF;
    private javax.swing.JLabel lbDesempeño;
    private javax.swing.JLabel lbFOTO;
    private javax.swing.JLabel lbListado;
    private javax.swing.JLabel lbListado2;
    private javax.swing.JLabel lbListadoUsuarios;
    private javax.swing.JLabel lbModificarDatosf;
    private javax.swing.JLabel lbNotas;
    private javax.swing.JLabel lbPromedio;
    private javax.swing.JLabel lbPromedioG;
    private javax.swing.JLabel lbQuitarMateria;
    private javax.swing.JLabel lbQuitarMateriaf;
    private javax.swing.JLabel lbReporteAA;
    private javax.swing.JLabel lbReporteAD;
    private javax.swing.JLabel lbReportes;
    private javax.swing.JLabel lbReportesA;
    private javax.swing.JLabel lbicono;
    private javax.swing.JLabel lbingresaruserf;
    private javax.swing.JLabel lblistausuariosf;
    // End of variables declaration//GEN-END:variables
}
