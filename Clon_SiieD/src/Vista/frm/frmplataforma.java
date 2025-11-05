package Vista.frm;

import Clases.AjustesObjetos;
import Clases.EfectoHoverPanel;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import Clases.Base_De_Datos;
import Clases.ConexionBD;
import static java.awt.Component.LEFT_ALIGNMENT;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import Vista.frm.Panel.*;


public class frmplataforma extends javax.swing.JFrame {
       
    private String Cargos;
    private Integer click;
    private final Base_De_Datos baseDatos;
    private String usuarioActual; 
    
    public frmplataforma(Base_De_Datos basedato,String Cargo,String usuario) {

    miinitComponents();
         
    this.usuarioActual = usuario;
    btnSalir.setVisible(false);
    this.baseDatos = basedato;
    Cargos=Cargo;
    setTitle("Siie administrativo");
    setSize(1224, 717);
    setResizable(false);
    setLocationRelativeTo(null);
    this.getContentPane().setBackground(new Color(240, 244, 248));

    new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(DListadosAlumnos);
    new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(DNotasAlumnos);
    new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(DReportes);
    new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(Ddesempeño);
    
    new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(AAsignar_QuitarMateria);
    new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(ACrearCarrera);
    new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(ACrearMaterias);
    new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(AReporteAcademico);
    
    new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(RCListadoUsuarios);
    new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(RCRegistro);
    new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(RCModificarDatos);
    
     // ---------- hover sobre LABELS de los paneles ----------
    aplicarHoverLabel(lbANotas);
    aplicarHoverLabel(lbListado);
    aplicarHoverLabel(lbListado2);
    aplicarHoverLabel(lbReporteAD);

    aplicarHoverLabel(lbCrearMateria);
    aplicarHoverLabel(lbCrearCarrera);
    aplicarHoverLabel(lbQuitarMateria);
    aplicarHoverLabel(lbReporteAA);

    aplicarHoverLabel(lbListadoUsuarios);
    aplicarHoverLabel(lbModificarDatos);
    aplicarHoverLabel(lbListado2);   

    AjustesObjetos.ajustarImagen(lbicono, "src\\imagenes\\foto.png");
    AjustesObjetos.ajustarImagen(lbFOTO,"src\\imagenes\\Listados.png" );
    AjustesObjetos.ajustarImagen(lbCbn, "src\\imagenes\\Corporacion-Bolivariana-del-Norte-CBN.png");
    AjustesObjetos.ajustarImagen(lbNotas, "src\\imagenes\\Notas.png");

    AjustesObjetos.ajustarImagen(lbReportes, "src\\imagenes\\Reportes.png");
    AjustesObjetos.ajustarImagen(lbDesempeño, "src\\imagenes\\desempeño.png");
    AjustesObjetos.ajustarImagen(lbCrearMateriaf, "src\\imagenes\\56640216-college-education-outlined-and-colored-icons.png");
    AjustesObjetos.ajustarImagen(lbCrearcarreraF, "src\\imagenes\\carrera.png");
    AjustesObjetos.ajustarImagen(lbQuitarMateriaf, "src\\imagenes\\remover.png");
    AjustesObjetos.ajustarImagen(lbReportesA, "src\\imagenes\\Dc.png");
    AjustesObjetos.ajustarImagen(lbModificarDatosf, "src\\imagenes\\system-user-administrator-icon-set-600nw-376451899.png");
    AjustesObjetos.ajustarImagen(lbRegistros, "src\\imagenes\\funcion 2.png");
    AjustesObjetos.ajustarImagen(lblistausuariosf, "src\\imagenes\\icono-funciones-utiles-conjunto-rueda-dentada-simbolo-logotipo-vector-investigacion-util-estilo-negro-lleno-delineado-signo-negocio-multiples-ruedas-dentadas_268104-6873.png");
    
  
    // ===== Separadores para menuDocente =====
    JPanel sepDoc1 = crearSeparador(new Color(192,4,29), 40);
    JPanel sepDoc2 = crearSeparador(new Color(192,4,29), 40);
    JPanel sepDoc3 = crearSeparador(new Color(192,4,29), 40);
    JPanel sepDoc4 = crearSeparador(new Color(192,4,29), 40);
    JPanel sepDoc5 = crearSeparador(new Color(192,4,29), 40);

    // ===== Separadores para menuregistroControl =====
    JPanel sepReg1 = crearSeparador(new Color(192,4,29), 40);
    JPanel sepReg2 = crearSeparador(new Color(192,4,29), 40);
    JPanel sepReg3 = crearSeparador(new Color(192,4,29), 40);
    JPanel sepReg5 = crearSeparador(new Color(192,4,29), 40);
    
    // ===== Separadores para menuAdmin =====
    JPanel sepAdm1 = crearSeparador(new Color(192,4,29), 40);
    JPanel sepAdm2 = crearSeparador(new Color(192,4,29), 40);
    JPanel sepAdm3 = crearSeparador(new Color(192,4,29), 40);
    JPanel sepAdm4 = crearSeparador(new Color(192,4,29), 40);
    JPanel sepAdm5 = crearSeparador(new Color(192,4,29), 40);

        
    // Usar CardLayout
    JpanelMenu.setLayout(new java.awt.CardLayout());

    
    DListadosAlumnos.setMaximumSize(new Dimension(379, 55));    
    Ddesempeño.setMaximumSize(new Dimension(379, 55));
    DNotasAlumnos.setMaximumSize(new Dimension(379, 55));
    DReportes.setMaximumSize(new Dimension(379, 55));

    
    AAsignar_QuitarMateria.setMaximumSize(new Dimension(379, 55));
    ACrearCarrera.setMaximumSize(new Dimension(379, 55));
    ACrearMaterias.setMaximumSize(new Dimension(379, 55));
    AReporteAcademico.setMaximumSize(new Dimension(379, 55));
    
    RCListadoUsuarios.setMaximumSize(new Dimension(379, 55));
       RCRegistro.setMaximumSize(new Dimension(379, 55));
    RCModificarDatos.setMaximumSize(new Dimension(379, 55));
    RCModificarDatos.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        RCModificarDatosMouseClicked(evt);
    }
});
    

    // Panel de menú ADMIN
    JPanel menuAdmin = new JPanel();
    
    menuAdmin.setBackground(new Color	(192,4,29));
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
    
    // Panel de menú Registro control
    
    JPanel menuRegistroC = new JPanel();
    
    menuRegistroC.setBackground(new Color	(192,4,29));
    menuRegistroC.setLayout(new BoxLayout(menuRegistroC, BoxLayout.Y_AXIS));
    
    menuRegistroC.add(sepReg1); 
    menuRegistroC.add(RCListadoUsuarios);
    
    menuRegistroC.add(sepReg2); 
    menuRegistroC.add(RCRegistro);
    
    menuRegistroC.add(sepReg3); 
    menuRegistroC.add(RCModificarDatos);
    
    menuRegistroC.add(sepReg5); 
  
    
    
    // Panel de menú DOCENTE
    JPanel menuDocente = new JPanel();
    menuDocente.setLayout(new BoxLayout(menuDocente, BoxLayout.Y_AXIS));

    menuDocente.setBackground(new Color(192,4,29));
    menuDocente.add(sepDoc1);
    menuDocente.add(DListadosAlumnos);

    menuDocente.add(sepDoc2);
    menuDocente.add(DNotasAlumnos);

    

    menuDocente.add(sepDoc4);
    menuDocente.add(DReportes);

    menuDocente.add(sepDoc5);
    menuDocente.add(Ddesempeño);
    
    
    // Agregar ambos al CardLayout
    JpanelMenu.setLayout(new CardLayout());
    JpanelMenu.add(menuAdmin, "ADMIN");
    JpanelMenu.add(menuDocente, "DOCENTE");
    JpanelMenu.add(menuRegistroC, "registroc");
    
    
    
    // Mostrar el menú según el rol
    
    CardLayout cl = (CardLayout) JpanelMenu.getLayout();

    if (Cargos.equals("administrador")) {
        cl.show(JpanelMenu, "ADMIN");
    } else if(Cargos.equals("docente")){
        cl.show(JpanelMenu, "DOCENTE");
    }else if(Cargos.equals("registro y control")){
        cl.show(JpanelMenu, "registroc");
        
    }
    
    
   
    
    
  }
    
    
    private void aplicarHoverLabel(JLabel label) {
    label.addMouseListener(new java.awt.event.MouseAdapter() {
        private final Color originalFg = label.getForeground();
        private final Color originalBg = label.getParent().getBackground();
        private final Color hoverFg = Color.BLACK; // color del texto en hover
        private final Color hoverBg = new Color(250,217,124); // color del panel en hover

        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            label.setForeground(hoverFg);
            label.getParent().setBackground(hoverBg);
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            label.setForeground(originalFg);
            label.getParent().setBackground(originalBg);
        }
    });
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void miinitComponents() {

        JpanelMenu = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lbicono = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        jtPestañas = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        lbCbn = new javax.swing.JLabel();
        DNotasAlumnos = new javax.swing.JPanel();
        lbANotas = new javax.swing.JLabel();
        lbNotas = new javax.swing.JLabel();
        Ddesempeño = new javax.swing.JPanel();
        lbDesempeño = new javax.swing.JLabel();
        lbListado2 = new javax.swing.JLabel();
        DListadosAlumnos = new javax.swing.JPanel();
        lbListado = new javax.swing.JLabel();
        lbFOTO = new javax.swing.JLabel();
        DReportes = new javax.swing.JPanel();
        lbReporteAD = new javax.swing.JLabel();
        lbReportes = new javax.swing.JLabel();
        RCModificarDatos = new javax.swing.JPanel();
        
        lbModificarDatos = new javax.swing.JLabel();
        lbModificarDatos.setBounds(0, 0, 201, 38); // ocupar todo el panel
        lbModificarDatosf = new javax.swing.JLabel();
        RCListadoUsuarios = new javax.swing.JPanel();
        lbListadoUsuarios = new javax.swing.JLabel();
        lblistausuariosf = new javax.swing.JLabel();
        RCRegistro = new javax.swing.JPanel();
        lbRegistro = new javax.swing.JLabel(); // Cambiado de lbListado2 a lbRegistro
        lbRegistros = new javax.swing.JLabel();
        ACrearMaterias = new javax.swing.JPanel();
        lbCrearMateriaf = new javax.swing.JLabel();
        lbCrearMateria = new javax.swing.JLabel();
        ACrearCarrera = new javax.swing.JPanel();
        lbCrearCarrera = new javax.swing.JLabel();
        lbCrearcarreraF = new javax.swing.JLabel();
        AAsignar_QuitarMateria = new javax.swing.JPanel();
        lbQuitarMateria = new javax.swing.JLabel();
        lbQuitarMateriaf = new javax.swing.JLabel();
        AReporteAcademico = new javax.swing.JPanel();
        lbReporteAA = new javax.swing.JLabel();
        lbReportesA = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JpanelMenu.setBackground(new java.awt.Color(192, 4, 29));
        JpanelMenu.setMinimumSize(new java.awt.Dimension(200, 465));
        JpanelMenu.setLayout(null);
        getContentPane().add(JpanelMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 190, 580));

        jPanel3.setBackground(new java.awt.Color(192, 4, 29));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbicono.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbFoto(evt);
            }
        });
        jPanel3.add(lbicono, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 0, 110, 100));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Corporacion Bolibariana del Norte");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, -1, -1));

        btnSalir.setText("Cerrar Seccion");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel3.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, -1, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1020, 100));

        jtPestañas.setBackground(new java.awt.Color(204, 204, 204));
        jtPestañas.setForeground(new java.awt.Color(153, 153, 153));
        jtPestañas.setPreferredSize(new java.awt.Dimension(1000, 600));
        jtPestañas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtPestañasMouseClicked(evt);
            }
        });
        getContentPane().add(jtPestañas, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 1020, 580));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCbn.setText("jLabel1");
        jPanel2.add(lbCbn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 100));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 100));

        DNotasAlumnos.setBackground(new java.awt.Color(192, 4, 29));
        DNotasAlumnos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DNotasAlumnosMouseClicked(evt);
            }
        });
        DNotasAlumnos.setLayout(null);

        lbANotas.setBackground(new java.awt.Color(192, 4, 29));
        lbANotas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbANotas.setForeground(new java.awt.Color(255, 255, 255));
        lbANotas.setText("Asignar Notas");
        lbANotas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbANotasMouseClicked(evt);
            }
        });
        DNotasAlumnos.add(lbANotas);
        lbANotas.setBounds(50, 20, 100, 20);

        lbNotas.setText("jLabel3");
        DNotasAlumnos.add(lbNotas);
        lbNotas.setBounds(0, 10, 50, 40);

        getContentPane().add(DNotasAlumnos, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 140, 201, 38));

        Ddesempeño.setBackground(new java.awt.Color(192, 4, 29));
        Ddesempeño.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DdesempeñoMouseClicked(evt);
            }
        });
        Ddesempeño.setLayout(null);

        lbDesempeño.setText("jLabel4");
        Ddesempeño.add(lbDesempeño);
        lbDesempeño.setBounds(0, 10, 50, 40);

        lbListado2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbListado2.setForeground(new java.awt.Color(255, 255, 255));
        lbListado2.setText("Desempeño");
        Ddesempeño.add(lbListado2);
        lbListado2.setBounds(50, 20, 150, 20);

        getContentPane().add(Ddesempeño, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, 201, 38));

        DListadosAlumnos.setBackground(new java.awt.Color(192, 4, 29));
        DListadosAlumnos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DListadosAlumnosMouseClicked(evt);
            }
        });
        DListadosAlumnos.setLayout(null);

        lbListado.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbListado.setForeground(new java.awt.Color(255, 255, 255));
        lbListado.setText("Listado de alumnos");
        lbListado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbListadoMouseClicked(evt);
            }
        });
        DListadosAlumnos.add(lbListado);
        lbListado.setBounds(50, 20, 140, 20);

        lbFOTO.setText("jLabel2");
        DListadosAlumnos.add(lbFOTO);
        lbFOTO.setBounds(0, 10, 50, 40);

        getContentPane().add(DListadosAlumnos, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, 201, 38));

        DReportes.setBackground(new java.awt.Color(192, 4, 29));
        DReportes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DReportesMouseClicked(evt);
            }
        });
        DReportes.setLayout(null);

        lbReporteAD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbReporteAD.setForeground(new java.awt.Color(255, 255, 255));
        lbReporteAD.setText("Reportes Academico");
        lbReporteAD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbReporteADMouseClicked(evt);
            }
        });
        DReportes.add(lbReporteAD);
        lbReporteAD.setBounds(50, 20, 150, 20);

        lbReportes.setText("jLabel4");
        DReportes.add(lbReportes);
        lbReportes.setBounds(0, 10, 50, 40);

        getContentPane().add(DReportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 290, 201, 38));

        RCModificarDatos.setBackground(new java.awt.Color(192, 4, 29));
        RCModificarDatos.setLayout(null);

        lbModificarDatos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbModificarDatos.setForeground(new java.awt.Color(255, 255, 255));
        lbModificarDatos.setText("Modificar Datos");
        RCModificarDatos.add(lbModificarDatos);
        lbModificarDatos.setBounds(50, 20, 150, 20);

        lbModificarDatosf.setText("jLabel4");
        RCModificarDatos.add(lbModificarDatosf);
        lbModificarDatosf.setBounds(0, 10, 50, 40);

        getContentPane().add(RCModificarDatos, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 240, 201, 38));

        RCListadoUsuarios.setBackground(new java.awt.Color(192, 4, 29));
        RCListadoUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RCListadoUsuariosMouseClicked(evt);
            }
        });
        RCListadoUsuarios.setLayout(null);

        lbListadoUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbListadoUsuarios.setForeground(new java.awt.Color(255, 255, 255));
        lbListadoUsuarios.setText("Listado de Usuarios");
        RCListadoUsuarios.add(lbListadoUsuarios);
        lbListadoUsuarios.setBounds(50, 20, 150, 20);

        lblistausuariosf.setText("jLabel4");
        RCListadoUsuarios.add(lblistausuariosf);
        lblistausuariosf.setBounds(0, 10, 50, 40);

        getContentPane().add(RCListadoUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 140, 201, 38));

        RCRegistro.setBackground(new java.awt.Color(192, 4, 29));
        RCRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RCRegistroMouseClicked(evt);
            }
        });
        RCRegistro.setLayout(null);

        lbRegistro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbRegistro.setForeground(new java.awt.Color(255, 255, 255));
        lbRegistro.setText("Registro y control");
        lbRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbRegistroMouseClicked(evt);
            }
        });
        RCRegistro.add(lbRegistro);
        lbRegistro.setBounds(50, 20, 150, 20);
        RCRegistro.add(lbRegistros);
        lbRegistros.setBounds(0, 10, 50, 40);
        getContentPane().add(RCRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 190, 201, 38));

        ACrearMaterias.setBackground(new java.awt.Color(192, 4, 29));
        ACrearMaterias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ACrearMateriasMouseClicked(evt);
            }
        });
        ACrearMaterias.setLayout(null);

        lbCrearMateriaf.setText("jLabel4");
        ACrearMaterias.add(lbCrearMateriaf);
        lbCrearMateriaf.setBounds(0, 10, 50, 40);

        lbCrearMateria.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbCrearMateria.setForeground(new java.awt.Color(255, 255, 255));
        lbCrearMateria.setText("Crear Materia");
        lbCrearMateria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbCrearMateriaMouseClicked(evt);
            }
        });
        ACrearMaterias.add(lbCrearMateria);
        lbCrearMateria.setBounds(60, 20, 150, 20);

        getContentPane().add(ACrearMaterias, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 190, 201, 38));

        ACrearCarrera.setBackground(new java.awt.Color(192, 4, 29));
        ACrearCarrera.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ACrearCarreraMouseClicked(evt);
            }
        });
        ACrearCarrera.setLayout(null);

        lbCrearCarrera.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbCrearCarrera.setForeground(new java.awt.Color(255, 255, 255));
        lbCrearCarrera.setText("Crear Carrera");
        lbCrearCarrera.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbCrearCarreraMouseClicked(evt);
            }
        });
        ACrearCarrera.add(lbCrearCarrera);
        lbCrearCarrera.setBounds(60, 20, 150, 20);

        lbCrearcarreraF.setText("jLabel4");
        ACrearCarrera.add(lbCrearcarreraF);
        lbCrearcarreraF.setBounds(0, 10, 50, 40);

        getContentPane().add(ACrearCarrera, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 140, 201, 38));

        AAsignar_QuitarMateria.setBackground(new java.awt.Color(192, 4, 29));
        AAsignar_QuitarMateria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AAsignar_QuitarMateriaMouseClicked(evt);
            }
        });
        AAsignar_QuitarMateria.setLayout(null);

        lbQuitarMateria.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbQuitarMateria.setForeground(new java.awt.Color(255, 255, 255));
        lbQuitarMateria.setText("Quitar Materia");
        lbQuitarMateria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbQuitarMateriaMouseClicked(evt);
            }
        });
        AAsignar_QuitarMateria.add(lbQuitarMateria);
        lbQuitarMateria.setBounds(50, 10, 140, 30);

        lbQuitarMateriaf.setText("jLabel4");
        AAsignar_QuitarMateria.add(lbQuitarMateriaf);
        lbQuitarMateriaf.setBounds(-10, 0, 60, 50);

        getContentPane().add(AAsignar_QuitarMateria, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 290, 201, 38));

        AReporteAcademico.setBackground(new java.awt.Color(192, 4, 29));
        AReporteAcademico.setRequestFocusEnabled(false);
        AReporteAcademico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AReporteAcademicoMouseClicked(evt);
            }
        });
        AReporteAcademico.setLayout(null);

        lbReporteAA.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbReporteAA.setForeground(new java.awt.Color(255, 255, 255));
        lbReporteAA.setText("Reportes Academicos");
        lbReporteAA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbReporteAAMouseClicked(evt);
            }
        });
        AReporteAcademico.add(lbReporteAA);
        lbReporteAA.setBounds(50, 20, 150, 20);

        lbReportesA.setText("jLabel4");
        AReporteAcademico.add(lbReportesA);
        lbReportesA.setBounds(0, 10, 50, 40);

        getContentPane().add(AReporteAcademico, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 240, 201, 38));

        pack();
    }// </editor-fold>                        

    private void lbFoto(java.awt.event.MouseEvent evt) {                        
        // TODO add your handling code here:
        boolean estafo=btnSalir.isVisible();    
        
        if(estafo){
            btnSalir.setVisible(false);
        }else{
            btnSalir.setVisible(true);
        }
        
        
    }                       

    private void abrirListadoEstudiantes() {
    String materiaAsignada = obtenerMateriaDelDocente(usuarioActual);
    if (materiaAsignada == null || materiaAsignada.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No tienes materias asignadas.");
        return;
    }

    PanelListaEstudiantes panel = new PanelListaEstudiantes(baseDatos, usuarioActual);

    jtPestañas.addTab("Listado", panel);
    jtPestañas.setSelectedComponent(panel);
}
    
    private String obtenerMateriaDelDocente(String usuarioDocente) {
    String materia = null;
    String sql = "CALL obtener_materia_docente(?)";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, usuarioDocente);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            materia = rs.getString("materia");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return materia;
}

    private void lbANotasMouseClicked(java.awt.event.MouseEvent evt) {                                      
     PanelAsignarNotasPorMateria panel=new PanelAsignarNotasPorMateria(usuarioActual);
        jtPestañas.addTab("Notas",panel);
    }                                     

    private void lbListadoMouseClicked(java.awt.event.MouseEvent evt) {                                       
        // TODO add your handling code here:
         abrirListadoEstudiantes();
        System.out.println("si ");
    }                                      

    private void DListadosAlumnosMouseClicked(java.awt.event.MouseEvent evt) {                                              
        // TODO add your handling code here:
        abrirListadoEstudiantes();
    }                                             

    private void DNotasAlumnosMouseClicked(java.awt.event.MouseEvent evt) {                                           
        // TODO add your handling code here:
        lbANotasMouseClicked(evt);
    }                                          

    private void DdesempeñoMouseClicked(java.awt.event.MouseEvent evt) {                                        
        // TODO add your handling code here:
        
     PanelPromediosD desempeño = new PanelPromediosD(usuarioActual);
    jtPestañas.addTab("Promedios", desempeño);
    jtPestañas.setSelectedComponent(desempeño);
    }                                       

    private void lbReporteADMouseClicked(java.awt.event.MouseEvent evt) {                                         
        // TODO add your handling code here:
        PanelReportesD panel = new PanelReportesD(baseDatos, usuarioActual);

        // 👉 tamaño del área visible del JTabbedPane
        Dimension size = jtPestañas.getSize();
        panel.setPreferredSize(size);

        jtPestañas.addTab("Reportes", panel);
        jtPestañas.setSelectedComponent(panel);
    }                                        

    private void jtPestañasMouseClicked(java.awt.event.MouseEvent evt) {                                        
        // TODO add your handling code here:

        if (evt.getClickCount() == 2) { // doble clic
                int index = jtPestañas.getSelectedIndex();
                if (index != -1) {
                    jtPestañas.removeTabAt(index); // cierra la pestaña
                }
            }

        
    }                                       

    private void lbCrearMateriaMouseClicked(java.awt.event.MouseEvent evt) {                                            
        PanelGestionMaterias panel = new PanelGestionMaterias();
        jtPestañas.addTab("materias",panel);
    }                                           

    private void lbCrearCarreraMouseClicked(java.awt.event.MouseEvent evt) {                                            
        // TODO add your handling code here:
        PanelCrearCarreraConMaterias panel = new PanelCrearCarreraConMaterias();
        jtPestañas.addTab("Crear Carreras",panel);
    }                                           

    private void DReportesMouseClicked(java.awt.event.MouseEvent evt) {                                       
        // TODO add your handling code here:
            lbReporteADMouseClicked(evt);
    }                                      

    private void RCListadoUsuariosMouseClicked(java.awt.event.MouseEvent evt) {                                               
        // TODO add your handling code here:
        PanelListaUsuarios panel=new PanelListaUsuarios(baseDatos, usuarioActual);
        
        jtPestañas.addTab("listado",panel);        
    }                                              

    private void ACrearCarreraMouseClicked(java.awt.event.MouseEvent evt) {                                           
        // TODO add your handling code here:
        lbCrearCarreraMouseClicked(evt);
    }                                          

    private void ACrearMateriasMouseClicked(java.awt.event.MouseEvent evt) {                                            
        // TODO add your handling code here:
        lbCrearMateriaMouseClicked(evt);
    }                                           

    private void AReporteAcademicoMouseClicked(java.awt.event.MouseEvent evt) {                                               
        // TODO add your handling code here:
        lbReporteAAMouseClicked(evt);
    }                                              

    private void lbReporteAAMouseClicked(java.awt.event.MouseEvent evt) {                                         
        // TODO add your handling code here:
    PanelAlumnosPorCarrera panel=new PanelAlumnosPorCarrera();
    JScrollPane scroll = new JScrollPane(panel);
    scroll.setPreferredSize(new Dimension(950, 500));
    jtPestañas.addTab("Reportes y desempeño", scroll);
    }                                        

    private void AAsignar_QuitarMateriaMouseClicked(java.awt.event.MouseEvent evt) {                                                    
        // TODO add your handling code here:
    }                                                   

    private void lbQuitarMateriaMouseClicked(java.awt.event.MouseEvent evt) {                                             
        // TODO add your handling code here:
        PanelRetirarMateriaADocente panel=new PanelRetirarMateriaADocente();
        jtPestañas.addTab("Quitar materia", panel);
    }                                            

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        frmLogin login=new frmLogin(baseDatos);
        login.setVisible(true);
        this.dispose();
        
        
    }                                        

    /**
     * @param args the command line arguments
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lbCbn = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lbicono = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        JpanelMenu = new javax.swing.JPanel();
        jtPestañas = new javax.swing.JTabbedPane();
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
        lbModificarDatos = new javax.swing.JLabel();
        lbModificarDatosf = new javax.swing.JLabel();
        RCListadoUsuarios = new javax.swing.JPanel();
        lbListadoUsuarios = new javax.swing.JLabel();
        lblistausuariosf = new javax.swing.JLabel();
        RCRegistro = new javax.swing.JPanel();
        lbRegistro = new javax.swing.JLabel();
        lbRegistros = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCbn.setText("jLabel1");
        jPanel2.add(lbCbn, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 190, 100));

        jPanel3.setBackground(new java.awt.Color(192, 4, 29));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbicono.setText("jose");
        lbicono.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbiconolbFoto(evt);
            }
        });
        jPanel3.add(lbicono, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 0, 110, 100));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Corporacion Bolibariana del Norte");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, -1, -1));

        btnSalir.setText("Cerrar Seccion");
        jPanel3.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, -1, -1));

        JpanelMenu.setBackground(new java.awt.Color(192, 4, 29));
        JpanelMenu.setMinimumSize(new java.awt.Dimension(200, 465));
        JpanelMenu.setLayout(null);

        jtPestañas.setPreferredSize(new java.awt.Dimension(1000, 600));

        jPanel5.setPreferredSize(new java.awt.Dimension(1000, 600));

        DListadosAlumnos.setBackground(new java.awt.Color(192, 4, 29));
        DListadosAlumnos.setLayout(null);

        lbListado.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbListado.setForeground(new java.awt.Color(255, 255, 255));
        lbListado.setText("Listado de alumnos");
        DListadosAlumnos.add(lbListado);
        lbListado.setBounds(50, 20, 140, 20);

        lbFOTO.setText("jLabel2");
        DListadosAlumnos.add(lbFOTO);
        lbFOTO.setBounds(0, 10, 50, 40);

        DNotasAlumnos.setBackground(new java.awt.Color(192, 4, 29));
        DNotasAlumnos.setLayout(null);

        lbANotas.setBackground(new java.awt.Color(192, 4, 29));
        lbANotas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbANotas.setForeground(new java.awt.Color(255, 255, 255));
        lbANotas.setText("Asignar Notas");
        DNotasAlumnos.add(lbANotas);
        lbANotas.setBounds(50, 20, 100, 20);

        lbNotas.setText("jLabel3");
        DNotasAlumnos.add(lbNotas);
        lbNotas.setBounds(0, 10, 50, 40);

        Dpromedio.setBackground(new java.awt.Color(192, 4, 29));
        Dpromedio.setLayout(null);

        lbPromedioG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbPromedioG.setForeground(new java.awt.Color(255, 255, 255));
        lbPromedioG.setText("Promedios");
        Dpromedio.add(lbPromedioG);
        lbPromedioG.setBounds(50, 20, 80, 20);

        lbPromedio.setText("jLabel4");
        Dpromedio.add(lbPromedio);
        lbPromedio.setBounds(0, 10, 48, 40);

        DReportes.setBackground(new java.awt.Color(192, 4, 29));
        DReportes.setLayout(null);

        lbReporteAD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbReporteAD.setForeground(new java.awt.Color(255, 255, 255));
        lbReporteAD.setText("Reportes Academico");
        DReportes.add(lbReporteAD);
        lbReporteAD.setBounds(50, 20, 150, 20);

        lbReportes.setText("jLabel4");
        DReportes.add(lbReportes);
        lbReportes.setBounds(0, 10, 50, 40);

        Ddesempeño.setBackground(new java.awt.Color(192, 4, 29));
        Ddesempeño.setLayout(null);

        lbDesempeño.setText("jLabel4");
        Ddesempeño.add(lbDesempeño);
        lbDesempeño.setBounds(0, 10, 50, 40);

        lbListado2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbListado2.setForeground(new java.awt.Color(255, 255, 255));
        lbListado2.setText("Desempeño");
        Ddesempeño.add(lbListado2);
        lbListado2.setBounds(50, 20, 150, 20);

        ACrearMaterias.setBackground(new java.awt.Color(192, 4, 29));
        ACrearMaterias.setLayout(null);

        lbCrearMateriaf.setText("jLabel4");
        ACrearMaterias.add(lbCrearMateriaf);
        lbCrearMateriaf.setBounds(0, 10, 50, 40);

        lbCrearMateria.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbCrearMateria.setForeground(new java.awt.Color(255, 255, 255));
        lbCrearMateria.setText("Crear Materia");
        ACrearMaterias.add(lbCrearMateria);
        lbCrearMateria.setBounds(60, 20, 150, 20);

        ACrearCarrera.setBackground(new java.awt.Color(192, 4, 29));
        ACrearCarrera.setLayout(null);

        lbCrearCarrera.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbCrearCarrera.setForeground(new java.awt.Color(255, 255, 255));
        lbCrearCarrera.setText("Crear Carrera");
        ACrearCarrera.add(lbCrearCarrera);
        lbCrearCarrera.setBounds(60, 20, 150, 20);

        lbCrearcarreraF.setText("jLabel4");
        ACrearCarrera.add(lbCrearcarreraF);
        lbCrearcarreraF.setBounds(0, 10, 50, 40);

        AReporteAcademico.setBackground(new java.awt.Color(192, 4, 29));
        AReporteAcademico.setRequestFocusEnabled(false);
        AReporteAcademico.setLayout(null);

        lbReporteAA.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbReporteAA.setForeground(new java.awt.Color(255, 255, 255));
        lbReporteAA.setText("Reportes Academicos");
        AReporteAcademico.add(lbReporteAA);
        lbReporteAA.setBounds(50, 20, 150, 20);

        lbReportesA.setText("jLabel4");
        AReporteAcademico.add(lbReportesA);
        lbReportesA.setBounds(0, 10, 50, 40);

        AAsignar_QuitarMateria.setBackground(new java.awt.Color(192, 4, 29));
        AAsignar_QuitarMateria.setLayout(null);

        lbQuitarMateria.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbQuitarMateria.setForeground(new java.awt.Color(255, 255, 255));
        lbQuitarMateria.setText("Quitar Materia");
        AAsignar_QuitarMateria.add(lbQuitarMateria);
        lbQuitarMateria.setBounds(50, 10, 140, 30);

        lbQuitarMateriaf.setText("jLabel4");
        AAsignar_QuitarMateria.add(lbQuitarMateriaf);
        lbQuitarMateriaf.setBounds(-10, 0, 60, 50);

        RCModificarDatos.setBackground(new java.awt.Color(192, 4, 29));
        RCModificarDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RCModificarDatosMouseClicked(evt);
            }
        });
        RCModificarDatos.setLayout(null);

        lbModificarDatos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbModificarDatos.setForeground(new java.awt.Color(255, 255, 255));
        lbModificarDatos.setText("Modificar Datos");
        RCModificarDatos.add(lbModificarDatos);
        lbModificarDatos.setBounds(50, 20, 150, 20);

        lbModificarDatosf.setText("jLabel4");
        RCModificarDatos.add(lbModificarDatosf);
        lbModificarDatosf.setBounds(0, 10, 50, 40);

        RCListadoUsuarios.setBackground(new java.awt.Color(192, 4, 29));
        RCListadoUsuarios.setLayout(null);

        lbListadoUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbListadoUsuarios.setForeground(new java.awt.Color(255, 255, 255));
        lbListadoUsuarios.setText("Listado de Usuarios");
        lbListadoUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbListadoUsuariosMouseClicked(evt);
            }
        });
        RCListadoUsuarios.add(lbListadoUsuarios);
        lbListadoUsuarios.setBounds(50, 20, 150, 20);

        lblistausuariosf.setText("jLabel4");
        RCListadoUsuarios.add(lblistausuariosf);
        lblistausuariosf.setBounds(0, 10, 50, 40);

        RCRegistro.setBackground(new java.awt.Color(192, 4, 29));
        RCRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RCRegistroMouseClicked(evt);
            }
        });
        RCRegistro.setLayout(null);

        lbRegistro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbRegistro.setForeground(new java.awt.Color(255, 255, 255));
        lbRegistro.setText("Registro y control");
        lbRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbRegistroMouseClicked(evt);
            }
        });
        RCRegistro.add(lbRegistro);
        lbRegistro.setBounds(50, 20, 150, 20);

        lbRegistros.setText("jLabel4");
        RCRegistro.add(lbRegistros);
        lbRegistros.setBounds(0, 10, 50, 40);

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
                            .addComponent(RCRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(343, 511, Short.MAX_VALUE))
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
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DListadosAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(DReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RCRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        jtPestañas.addTab("tab2", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1020, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(JpanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtPestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 1020, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JpanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtPestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lbiconolbFoto(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbiconolbFoto
        // TODO add your handling code here:
        boolean estafo=btnSalir.isVisible();

        if(estafo){
            btnSalir.setVisible(false);
        }else{
            btnSalir.setVisible(true);
        }

    }//GEN-LAST:event_lbiconolbFoto

    private void lbListadoUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbListadoUsuariosMouseClicked
        // TODO add your handling code here:
        PanelListaUsuarios panel=new PanelListaUsuarios(baseDatos, usuarioActual);
        jtPestañas.addTab("listado",panel);
    }//GEN-LAST:event_lbListadoUsuariosMouseClicked

    private void lbRegistroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbRegistroMouseClicked
        // TODO add your handling code here:
        PanelRegistroPersona panel=new PanelRegistroPersona(baseDatos);
        jtPestañas.addTab("Registros Y control",panel);
    }//GEN-LAST:event_lbRegistroMouseClicked

    private void RCRegistroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RCRegistroMouseClicked
        // TODO add your handling code here:
        PanelRegistroPersona panel=new PanelRegistroPersona(baseDatos);
        jtPestañas.addTab("Registros Y control",panel);

    }//GEN-LAST:event_RCRegistroMouseClicked

    private void RCModificarDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RCModificarDatosMouseClicked
        // TODO add your handling code here:
        PanelMatriculaAlumno panel=new PanelMatriculaAlumno();
        jtPestañas.addTab("matriculas", panel); 
        PanelGestionUsuario pane=new PanelGestionUsuario();
        jtPestañas.addTab("control", pane); 
    }//GEN-LAST:event_RCModificarDatosMouseClicked

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
    private javax.swing.JPanel RCModificarDatos;
    private javax.swing.JPanel RCRegistro;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jtPestañas;
    private javax.swing.JLabel lbANotas;
    private javax.swing.JLabel lbCbn;
    private javax.swing.JLabel lbCrearCarrera;
    private javax.swing.JLabel lbCrearMateria;
    private javax.swing.JLabel lbCrearMateriaf;
    private javax.swing.JLabel lbCrearcarreraF;
    private javax.swing.JLabel lbDesempeño;
    private javax.swing.JLabel lbFOTO;
    private javax.swing.JLabel lbListado;
    private javax.swing.JLabel lbListado2;
    private javax.swing.JLabel lbListadoUsuarios;
    private javax.swing.JLabel lbModificarDatos;
    private javax.swing.JLabel lbModificarDatosf;
    private javax.swing.JLabel lbNotas;
    private javax.swing.JLabel lbPromedio;
    private javax.swing.JLabel lbPromedioG;
    private javax.swing.JLabel lbQuitarMateria;
    private javax.swing.JLabel lbQuitarMateriaf;
    private javax.swing.JLabel lbRegistro;
    private javax.swing.JLabel lbRegistros;
    private javax.swing.JLabel lbReporteAA;
    private javax.swing.JLabel lbReporteAD;
    private javax.swing.JLabel lbReportes;
    private javax.swing.JLabel lbReportesA;
    private javax.swing.JLabel lbicono;
    private javax.swing.JLabel lblistausuariosf;
    // End of variables declaration//GEN-END:variables
}
