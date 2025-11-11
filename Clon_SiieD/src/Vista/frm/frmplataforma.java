package Vista.frm;

import Clases.AjustesObjetos;
import Clases.EfectoHoverPanel;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import Clases.Base_De_Datos;
import static java.awt.Component.LEFT_ALIGNMENT;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import Vista.frm.Panel.PanelMatriculaAlumno;
import Vista.frm.Panel.PanelGestionMaterias;
import Vista.frm.Panel.*;
import java.awt.BorderLayout;

public class frmplataforma extends javax.swing.JFrame {
       
    private String Cargos;
    private Integer click;
    private final Base_De_Datos baseDatos;
    private String usuarioActual; 
    
public frmplataforma(Base_De_Datos basedato, String Cargo, String usuario) {
    miinitComponents();
    
    this.usuarioActual = usuario;
    btnSalir.setVisible(false);
    this.baseDatos = basedato;
    Cargos = Cargo;
    
    // LIMPIAR y reconfigurar JpanelMenu
    JpanelMenu.removeAll();
    JpanelMenu.setLayout(new CardLayout());
    
    setTitle("Siie administrativo");
    setSize(1224, 717);
    setResizable(false);
    setLocationRelativeTo(null);
    this.getContentPane().setBackground(new Color(240, 244, 248));
    
    // Reconfigurar todos los paneles de menú para que usen BorderLayout
    reconfigurarPanelMenu(AMaterias, lbCrearMateriaf, lbCrearMateria);
    reconfigurarPanelMenu(ACrearCarrera, lbCrearcarreraF, lbCrearCarrera);
    reconfigurarPanelMenu(AReporteAcademico, lbReportesA, lbReporteAA);
    reconfigurarPanelMenu(AQuitar, lbQuitarMateriaf, lbQuitarMateria);
    reconfigurarPanelMenu(RCListadoUsuarios, lblistausuariosf, lbListadoUsuarios);
    reconfigurarPanelMenu(RCRegistro, lbRegistros, lbRegistro);
    reconfigurarPanelMenu(RCModificarDatos, lbModificarDatosf, lbModificarDatos);
    reconfigurarPanelMenu(DListadosAlumnos, lbFOTO, lbListado);
    reconfigurarPanelMenu(DNotasAlumnos, lbNotas, lbANotas);
    reconfigurarPanelMenu(DReportes, lbReportes, lbReporteAD);
    reconfigurarPanelMenu(Ddesempeño, lbDesempeño, lbListado2);
    
    // Aplicar efectos hover a paneles
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(DListadosAlumnos);
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(DNotasAlumnos);
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(DReportes);
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(Ddesempeño);
        
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(AQuitar);
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(ACrearCarrera);
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(AMaterias);
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(AReporteAcademico);
        
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(RCListadoUsuarios);
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(RCRegistro);
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(RCModificarDatos);
        
        // Aplicar hover sobre LABELS de los paneles
        aplicarHoverLabel(lbANotas);
        aplicarHoverLabel(lbListado);
        aplicarHoverLabel(lbListado2);
        aplicarHoverLabel(lbReporteAD);

        aplicarHoverLabel(lbCrearMateria);
        aplicarHoverLabel(lbCrearCarrera);
        aplicarHoverLabel(lbQuitarMateria);
        aplicarHoverLabel(lbReporteAA);
        
        aplicarHoverLabel(lbRegistro);

        aplicarHoverLabel(lbListadoUsuarios);
        aplicarHoverLabel(lbModificarDatos);
        aplicarHoverLabel(lbListado2);   

        // ====== NUEVO: Actualizar foto de perfil según género ======
        actualizarFotoPerfilPorGenero();

        // ====== NUEVO: Aplicar hover a labels de estudiante ======
        // (Se aplicará después de crear los paneles de estudiante)

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
                
        // Separadores...
        JPanel sepDoc1 = crearSeparador(new Color(192,4,29), 40);
        JPanel sepDoc2 = crearSeparador(new Color(192,4,29), 40);
        JPanel sepDoc3 = crearSeparador(new Color(192,4,29), 40);
        JPanel sepDoc4 = crearSeparador(new Color(192,4,29), 40);
        JPanel sepDoc5 = crearSeparador(new Color(192,4,29), 40);

        JPanel sepReg1 = crearSeparador(new Color(192,4,29), 40);
        JPanel sepReg2 = crearSeparador(new Color(192,4,29), 40);
        JPanel sepReg3 = crearSeparador(new Color(192,4,29), 40);
        JPanel sepReg5 = crearSeparador(new Color(192,4,29), 40);
        
        JPanel sepAdm1 = crearSeparador(new Color(192,4,29), 40);
        JPanel sepAdm2 = crearSeparador(new Color(192,4,29), 40);
        JPanel sepAdm3 = crearSeparador(new Color(192,4,29), 40);
        JPanel sepAdm4 = crearSeparador(new Color(192,4,29), 40);
        JPanel sepAdm5 = crearSeparador(new Color(192,4,29), 40);

        // Panel de menú ADMIN
        JPanel menuAdmin = new JPanel();
        menuAdmin.setBackground(new Color(192,4,29));
        menuAdmin.setLayout(new BoxLayout(menuAdmin, BoxLayout.Y_AXIS));
        menuAdmin.setPreferredSize(new Dimension(190, 580));
        menuAdmin.setMaximumSize(new Dimension(190, 580));
        
        // Aplicar tamaños máximos a cada componente para evitar que se estiren
        sepAdm1.setMaximumSize(new Dimension(190, 60)); 
        menuAdmin.add(sepAdm1);
        AMaterias.setMaximumSize(new Dimension(190, 60));
        menuAdmin.add(AMaterias);
        
        sepAdm2.setMaximumSize(new Dimension(190, 60));
        menuAdmin.add(sepAdm2); 
        ACrearCarrera.setMaximumSize(new Dimension(190, 60));
        menuAdmin.add(ACrearCarrera);
        
        sepAdm3.setMaximumSize(new Dimension(190, 60));
        menuAdmin.add(sepAdm3); 
        AReporteAcademico.setMaximumSize(new Dimension(190, 60));
        menuAdmin.add(AReporteAcademico);
        
        sepAdm4.setMaximumSize(new Dimension(190, 60));
        menuAdmin.add(sepAdm4); 
        AQuitar.setMaximumSize(new Dimension(190, 60));
        menuAdmin.add(AQuitar);
        
        sepAdm5.setMaximumSize(new Dimension(190, 60));
        menuAdmin.add(sepAdm5); 

        // Panel de menú Registro control
        JPanel menuRegistroC = new JPanel();
        menuRegistroC.setBackground(new Color(192,4,29));
        menuRegistroC.setLayout(new BoxLayout(menuRegistroC, BoxLayout.Y_AXIS));
        menuRegistroC.setPreferredSize(new Dimension(190, 580));
        menuRegistroC.setMaximumSize(new Dimension(190, 580));
        
        sepReg1.setMaximumSize(new Dimension(190, 60));
        menuRegistroC.add(sepReg1); 
        RCListadoUsuarios.setMaximumSize(new Dimension(190, 60));
        menuRegistroC.add(RCListadoUsuarios);
        
        sepReg2.setMaximumSize(new Dimension(190, 60));
        menuRegistroC.add(sepReg2); 
        RCRegistro.setMaximumSize(new Dimension(190, 60));
        menuRegistroC.add(RCRegistro);
        
        sepReg3.setMaximumSize(new Dimension(190, 60));
        menuRegistroC.add(sepReg3); 
        RCModificarDatos.setMaximumSize(new Dimension(190, 60));
        menuRegistroC.add(RCModificarDatos);
        
        sepReg5.setMaximumSize(new Dimension(190, 60));
        menuRegistroC.add(sepReg5); 

        // Panel de menú DOCENTE
        JPanel menuDocente = new JPanel();
        menuDocente.setLayout(new BoxLayout(menuDocente, BoxLayout.Y_AXIS));
        menuDocente.setBackground(new Color(192,4,29));
        menuDocente.setPreferredSize(new Dimension(190, 580));
        menuDocente.setMaximumSize(new Dimension(190, 580));
        
        sepDoc1.setMaximumSize(new Dimension(190, 60));
        menuDocente.add(sepDoc1);
        DListadosAlumnos.setMaximumSize(new Dimension(190, 60));
        menuDocente.add(DListadosAlumnos);
        
        sepDoc2.setMaximumSize(new Dimension(190, 60));
        menuDocente.add(sepDoc2);
        DNotasAlumnos.setMaximumSize(new Dimension(190, 60));
        menuDocente.add(DNotasAlumnos);
        
        sepDoc4.setMaximumSize(new Dimension(190, 60));
        menuDocente.add(sepDoc4);
        DReportes.setMaximumSize(new Dimension(190, 60));
        menuDocente.add(DReportes);
        
        sepDoc5.setMaximumSize(new Dimension(190, 60));
        menuDocente.add(sepDoc5);
        Ddesempeño.setMaximumSize(new Dimension(190, 60));
        menuDocente.add(Ddesempeño);

        // Panel de menú ALUMNOS
        JPanel menuAlumnos = new JPanel();
        menuAlumnos.setLayout(new BoxLayout(menuAlumnos, BoxLayout.Y_AXIS));
        menuAlumnos.setBackground(new Color(192,4,29));
        menuAlumnos.setPreferredSize(new Dimension(190, 580));
        menuAlumnos.setMaximumSize(new Dimension(190, 580));
        
        // Crear opciones para alumnos
        JPanel panelMisNotas = new JPanel();
        panelMisNotas.setBackground(new Color(192,4,29));
        panelMisNotas.setMaximumSize(new Dimension(190, 60));
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(panelMisNotas);
        panelMisNotas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PanelMisNotas panel = new PanelMisNotas(usuarioActual, baseDatos);
                jtPestañas.addTab("Mis Notas", panel);
            }
        });
        
        JLabel lbMisNotas = new JLabel("Notas");
        lbMisNotas.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lbMisNotas.setForeground(Color.WHITE);
        lbMisNotas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PanelMisNotas panel = new PanelMisNotas(usuarioActual, baseDatos);
                jtPestañas.addTab("Mis Notas", panel);
            }
        });
        panelMisNotas.add(lbMisNotas);
        
        JPanel panelMiDesempeno = new JPanel();
        panelMiDesempeno.setBackground(new Color(192,4,29));
        panelMiDesempeno.setMaximumSize(new Dimension(190, 60));
        new EfectoHoverPanel(new Color(250,217,124), Color.black).aplicarEfecto(panelMiDesempeno);
        panelMiDesempeno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PanelReportesAlumno panel = new PanelReportesAlumno(usuarioActual);
                jtPestañas.addTab("reportes", panel);
            }
        });
        
        JLabel lbMiDesempeno = new JLabel("Reportes Academicos");
        lbMiDesempeno.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lbMiDesempeno.setForeground(Color.WHITE);
        lbMiDesempeno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PanelReportesAlumno panel = new PanelReportesAlumno(usuarioActual);
                jtPestañas.addTab("reportes", panel);
            }
        });
        panelMiDesempeno.add(lbMiDesempeno);
        
        // Aplicar hover a los nuevos labels de estudiante
        aplicarHoverLabel(lbMisNotas);
        aplicarHoverLabel(lbMiDesempeno);
        
        // Agregar separadores y paneles al menú de alumnos
        menuAlumnos.add(crearSeparador(new Color(192,4,29), 40));
        menuAlumnos.add(panelMisNotas);
        menuAlumnos.add(crearSeparador(new Color(192,4,29), 40));
        menuAlumnos.add(panelMiDesempeno);
        
        JPanel menuError = new JPanel();
        menuError.setLayout(new BoxLayout(menuError, BoxLayout.Y_AXIS));
        menuError.setBackground(new Color(192,4,29));
        menuError.setPreferredSize(new Dimension(190, 580));
        menuError.setMaximumSize(new Dimension(190, 580));
        
        // Agregar al CardLayout
        JpanelMenu.setLayout(new CardLayout());
        JpanelMenu.add(menuAdmin, "ADMIN");
        JpanelMenu.add(menuDocente, "DOCENTE");
        JpanelMenu.add(menuRegistroC, "registroc");
        JpanelMenu.add(menuAlumnos, "ALUMNOS");
        JpanelMenu.add(menuError, "Sin_cargo");
        
        // Mostrar el menú según el rol
        CardLayout cl = (CardLayout) JpanelMenu.getLayout();

        if (Cargos.equals("administrador")) {
            cl.show(JpanelMenu, "ADMIN");
        } else if(Cargos.equals("docente")){
            cl.show(JpanelMenu, "DOCENTE");
        }else if(Cargos.equals("registro y control")){
            cl.show(JpanelMenu, "registroc");
        }else if (Cargos.equals("alumno")){
            cl.show(JpanelMenu, "ALUMNOS");
        }else{        
            cl.show(JpanelMenu, "Sin_cargo");
        }
    }
    
    // ====== MÉTODO NUEVO: Actualizar foto según género ======
    private void actualizarFotoPerfilPorGenero() {
        String genero = baseDatos.obtenerSexoAlumno(usuarioActual);
        String rutaImagen;
        
        if ("masculino".equalsIgnoreCase(genero)) {
            rutaImagen = "src\\imagenes\\Imagen3.png";
        } else if ("femenino".equalsIgnoreCase(genero)) {
            rutaImagen = "src\\imagenes\\foto.png";
        } else {
            rutaImagen = "src\\imagenes\\OIP.PNG";
        }
        
        AjustesObjetos.ajustarImagen(lbicono, rutaImagen);
    }

    
    private void reconfigurarPanelMenu(JPanel panel, JLabel icono, JLabel texto) {
    // Quitar el layout null y usar BorderLayout
    panel.setLayout(new BorderLayout());
    
    // Configurar tamaño fijo y alineación
    panel.setPreferredSize(new Dimension(190, 60));
    panel.setMinimumSize(new Dimension(190, 60));
    panel.setMaximumSize(new Dimension(190, 60));
    panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    
    // Limpiar componentes existentes y reañadirlos con BorderLayout
    panel.removeAll();
    icono.setPreferredSize(new Dimension(60, 60));
    panel.add(icono, BorderLayout.WEST);
    panel.add(texto, BorderLayout.CENTER);
    
    // Asegurar que el fondo sea consistente
    panel.setBackground(new Color(192, 4, 29));
}
    
    
    private void aplicarHoverLabel(JLabel label) {
        final Color originalFg = label.getForeground();
        final Color originalBg = label.getParent().getBackground();
        final Color hoverFg = Color.BLACK;
        final Color hoverBg = new Color(250, 217, 124);

        label.addMouseListener(new java.awt.event.MouseAdapter() {
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
        sep.setPreferredSize(new Dimension(190, alto));
        sep.setMaximumSize(new Dimension(190, alto));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        return sep;
    }

    // ... resto del código ...

        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void miinitComponents() {

        JpanelMenu = new javax.swing.JPanel();
        jpanelMenuSuperior = new javax.swing.JPanel();
        lbicono = new javax.swing.JLabel();
        lbCorporacion = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        jtPestañas = new javax.swing.JTabbedPane();
        jpanelLogo = new javax.swing.JPanel();
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
        lbModificarDatos.setBounds(0, 0, 201, 38); 
        lbModificarDatosf = new javax.swing.JLabel();
        RCListadoUsuarios = new javax.swing.JPanel();
        lbListadoUsuarios = new javax.swing.JLabel();
        lblistausuariosf = new javax.swing.JLabel();
        RCRegistro = new javax.swing.JPanel();
        lbRegistro = new javax.swing.JLabel(); 
        lbRegistros = new javax.swing.JLabel();
        AMaterias = new javax.swing.JPanel();
        lbCrearMateriaf = new javax.swing.JLabel();
        lbCrearMateria = new javax.swing.JLabel();
        ACrearCarrera = new javax.swing.JPanel();
        lbCrearCarrera = new javax.swing.JLabel();
        lbCrearcarreraF = new javax.swing.JLabel();
        AQuitar = new javax.swing.JPanel();
        lbQuitarMateria = new javax.swing.JLabel();
        lbQuitarMateriaf = new javax.swing.JLabel();
        AReporteAcademico = new javax.swing.JPanel();
        lbReporteAA = new javax.swing.JLabel();
        lbReportesA = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JpanelMenu.setBackground(new java.awt.Color(192, 4, 29));
        JpanelMenu.setMinimumSize(new java.awt.Dimension(225, 465));
        JpanelMenu.setLayout(null);
        getContentPane().add(JpanelMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 190, 580));

        jpanelMenuSuperior.setBackground(new java.awt.Color(192, 4, 29));
        jpanelMenuSuperior.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbicono.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbFoto(evt);
            }
        });
        jpanelMenuSuperior.add(lbicono, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 0, 110, 100));

        lbCorporacion.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbCorporacion.setForeground(new java.awt.Color(255, 255, 255));
        lbCorporacion.setText("Corporacion Bolibariana del Norte");
        jpanelMenuSuperior.add(lbCorporacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, -1, -1));

        btnSalir.setText("Cerrar Seccion");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jpanelMenuSuperior.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, -1, -1));

        getContentPane().add(jpanelMenuSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 1020, 100));

        jtPestañas.setBackground(new java.awt.Color(204, 204, 204));
        jtPestañas.setForeground(new java.awt.Color(153, 153, 153));
        jtPestañas.setPreferredSize(new java.awt.Dimension(1000, 600));
        jtPestañas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtPestañasMouseClicked(evt);
            }
        });
        getContentPane().add(jtPestañas, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 1020, 580));

        jpanelLogo.setBackground(new java.awt.Color(255, 255, 255));
        jpanelLogo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpanelLogo.add(lbCbn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 100));

        getContentPane().add(jpanelLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 100));

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
        lbListado2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbListado2MouseClicked(evt);
            }
        });
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
        lbReporteAD.setText("Reportes");
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
        RCModificarDatos.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            RCModificarDatosMouseClicked(evt);
            }
        });
        lbModificarDatos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbModificarDatos.setForeground(new java.awt.Color(255, 255, 255));
        lbModificarDatos.setText("Modificar Datos");
        RCModificarDatos.add(lbModificarDatos);
        lbModificarDatos.setBounds(50, 20, 150, 20);
        lbModificarDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbModificarDatosMouseClicked(evt);
            }
        });

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
        lbListadoUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
           public void mouseClicked(java.awt.event.MouseEvent evt) {
               lbListadoUsuariosMouseClicked(evt);
           }
        });               
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

        AMaterias.setBackground(new java.awt.Color(192, 4, 29));
        AMaterias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ACrearMateriasMouseClicked(evt);
            }
        });
        AMaterias.setLayout(null);

        lbCrearMateriaf.setText("jLabel4");
        AMaterias.add(lbCrearMateriaf);
        lbCrearMateriaf.setBounds(0, 10, 50, 40);

        lbCrearMateria.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbCrearMateria.setForeground(new java.awt.Color(255, 255, 255));
        lbCrearMateria.setText("C/asig Materias");
        lbCrearMateria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbCrearMateriaMouseClicked(evt);
            }
        });
        AMaterias.add(lbCrearMateria);
        lbCrearMateria.setBounds(60, 20, 150, 20);

        getContentPane().add(AMaterias, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 190, 201, 38));

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

        AQuitar.setBackground(new java.awt.Color(192, 4, 29));
        AQuitar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AAsignar_QuitarMateriaMouseClicked(evt);
            }
        });
        AQuitar.setLayout(null);

        lbQuitarMateria.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbQuitarMateria.setForeground(new java.awt.Color(255, 255, 255));
        lbQuitarMateria.setText("Quitar Materia");
        lbQuitarMateria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbQuitarMateriaMouseClicked(evt);
            }
        });
        AQuitar.add(lbQuitarMateria);
        lbQuitarMateria.setBounds(50, 10, 140, 30);

        lbQuitarMateriaf.setText("jLabel4");
        AQuitar.add(lbQuitarMateriaf);
        lbQuitarMateriaf.setBounds(-10, 0, 60, 50);

        getContentPane().add(AQuitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 290, 201, 38));

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
        lbReporteAA.setText("Reportes");
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
        String materiaAsignada = baseDatos.obtenerMateriaDelDocente(usuarioActual);
        if (materiaAsignada == null || materiaAsignada.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tienes materias asignadas.");
            return;
        }

        PanelListaEstudiantes panel = new PanelListaEstudiantes(baseDatos, usuarioActual);

        jtPestañas.addTab("Listado", panel);
        jtPestañas.setSelectedComponent(panel);
    }

    private void lbANotasMouseClicked(java.awt.event.MouseEvent evt) {                                      
     PanelAsignarNotasPorMateria panel = new PanelAsignarNotasPorMateria(usuarioActual, baseDatos);
    jtPestañas.addTab("Notas", panel);
    }                                     

    private void lbListadoMouseClicked(java.awt.event.MouseEvent evt) {                                       
        // TODO add your handling code here:
        abrirListadoEstudiantes();
        
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
        Dimension size = jtPestañas.getSize();
        panel.setPreferredSize(size);

        jtPestañas.addTab("Reportes", panel);
        jtPestañas.setSelectedComponent(panel);
    }                                        

    private void jtPestañasMouseClicked(java.awt.event.MouseEvent evt) {                                        

        if (evt.getClickCount() == 2) { 
            int index = jtPestañas.getSelectedIndex();
            if (index != -1) {
                jtPestañas.removeTabAt(index); 
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
        PanelRetirarMateriaADocente panel=new PanelRetirarMateriaADocente();
        jtPestañas.addTab("Quitar materia", panel);
    }                                                   

    private void lbQuitarMateriaMouseClicked(java.awt.event.MouseEvent evt) {                                             
        // TODO add your handling code here:
        PanelRetirarMateriaADocente panel=new PanelRetirarMateriaADocente();
        jtPestañas.addTab("Quitar materia", panel);
    }                                            

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        frmLogin login=new frmLogin();
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

        jpanelLogo = new javax.swing.JPanel();
        lbCbn = new javax.swing.JLabel();
        jpanelMenuSuperior = new javax.swing.JPanel();
        lbicono = new javax.swing.JLabel();
        lbCorporacion = new javax.swing.JLabel();
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
        AMaterias = new javax.swing.JPanel();
        lbCrearMateriaf = new javax.swing.JLabel();
        lbCrearMateria = new javax.swing.JLabel();
        ACrearCarrera = new javax.swing.JPanel();
        lbCrearCarrera = new javax.swing.JLabel();
        lbCrearcarreraF = new javax.swing.JLabel();
        AReporteAcademico = new javax.swing.JPanel();
        lbReporteAA = new javax.swing.JLabel();
        lbReportesA = new javax.swing.JLabel();
        AQuitar = new javax.swing.JPanel();
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

        jpanelLogo.setBackground(new java.awt.Color(255, 255, 255));
        jpanelLogo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jpanelLogo.add(lbCbn, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 190, 100));

        jpanelMenuSuperior.setBackground(new java.awt.Color(192, 4, 29));
        jpanelMenuSuperior.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbicono.setText("jose");
        lbicono.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbiconolbFoto(evt);
            }
        });
        jpanelMenuSuperior.add(lbicono, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 0, 110, 100));

        lbCorporacion.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbCorporacion.setForeground(new java.awt.Color(255, 255, 255));
        lbCorporacion.setText("Corporacion Bolibariana del Norte");
        jpanelMenuSuperior.add(lbCorporacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, -1, -1));

        btnSalir.setText("Cerrar Seccion");
        jpanelMenuSuperior.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, -1, -1));

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
        lbListado2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbListado2MouseClicked(evt);
            }
        });
        Ddesempeño.add(lbListado2);
        lbListado2.setBounds(50, 20, 150, 20);

        AMaterias.setBackground(new java.awt.Color(192, 4, 29));
        AMaterias.setLayout(null);

        lbCrearMateriaf.setText("jLabel4");
        AMaterias.add(lbCrearMateriaf);
        lbCrearMateriaf.setBounds(0, 10, 50, 40);

        lbCrearMateria.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbCrearMateria.setForeground(new java.awt.Color(255, 255, 255));
        lbCrearMateria.setText("C /asignar Materia");
        AMaterias.add(lbCrearMateria);
        lbCrearMateria.setBounds(40, 20, 160, 20);

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

        AQuitar.setBackground(new java.awt.Color(192, 4, 29));
        AQuitar.setLayout(null);

        lbQuitarMateria.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbQuitarMateria.setForeground(new java.awt.Color(255, 255, 255));
        lbQuitarMateria.setText("Quitar Materia");
        AQuitar.add(lbQuitarMateria);
        lbQuitarMateria.setBounds(50, 10, 140, 30);

        lbQuitarMateriaf.setText("jLabel4");
        AQuitar.add(lbQuitarMateriaf);
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
        lbModificarDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbModificarDatosMouseClicked(evt);
            }
        });
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
                        .addComponent(AMaterias, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(AQuitar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(DReportes, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addComponent(Dpromedio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DListadosAlumnos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Ddesempeño, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addComponent(DNotasAlumnos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(RCModificarDatos, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(DNotasAlumnos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(RCRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Ddesempeño, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(AMaterias, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ACrearCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(jpanelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpanelMenuSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 1020, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(JpanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtPestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 1020, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpanelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpanelMenuSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jtPestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(JpanelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
        jtPestañas.addTab("Registro",panel);
        
        PanelMatriculaAlumno panel2=new PanelMatriculaAlumno();
        jtPestañas.addTab("matriculas", panel2); 
    }//GEN-LAST:event_lbRegistroMouseClicked

    private void RCRegistroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RCRegistroMouseClicked
        // TODO add your handling code here:
        PanelRegistroPersona panel=new PanelRegistroPersona(baseDatos);
        jtPestañas.addTab("Registro",panel);
        
        PanelMatriculaAlumno panel2=new PanelMatriculaAlumno();
        jtPestañas.addTab("matriculas", panel2); 

    }//GEN-LAST:event_RCRegistroMouseClicked

    private void RCModificarDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RCModificarDatosMouseClicked
        // TODO add your handling code here:
        PanelGestionUsuario pane=new PanelGestionUsuario();
        jtPestañas.addTab("control", pane); 
    }//GEN-LAST:event_RCModificarDatosMouseClicked

    private void lbModificarDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbModificarDatosMouseClicked
        // TODO add your handling code here:
        PanelGestionUsuario pane=new PanelGestionUsuario();
        jtPestañas.addTab("control", pane); 
        
    }//GEN-LAST:event_lbModificarDatosMouseClicked

    private void lbListado2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbListado2MouseClicked
        PanelPromediosD desempeño = new PanelPromediosD(usuarioActual);
        jtPestañas.addTab("Promedios", desempeño);
        jtPestañas.setSelectedComponent(desempeño);
    }//GEN-LAST:event_lbListado2MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ACrearCarrera;
    private javax.swing.JPanel AMaterias;
    private javax.swing.JPanel AQuitar;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jpanelLogo;
    private javax.swing.JPanel jpanelMenuSuperior;
    private javax.swing.JTabbedPane jtPestañas;
    private javax.swing.JLabel lbANotas;
    private javax.swing.JLabel lbCbn;
    private javax.swing.JLabel lbCorporacion;
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
