package clases;

import java.sql.*;
import java.util.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Base_De_Datos {
    
    private static final String URL = "jdbc:mysql://localhost:3306/CBN?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";   
    private static final String PASS = "cbn2016"; 
    private Integer corte = 1;

    
    // LOGIN
    public String login(String usuario, String contraseña) {
        String sql = "SELECT cargo FROM Usuarios WHERE user_ = ? AND contraseña = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, contraseña);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("cargo"); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

   
    // LISTAR ALUMNOS
    public List<String> listaAlumnos() {
        List<String> alumnos = new ArrayList<>();
        String sql = "SELECT nombre, apellido FROM Alumnos";
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                alumnos.add(rs.getString("nombre") + " " + rs.getString("apellido"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alumnos;
    }


    // INSERTAR ALUMNO
    public void insertarAlumno(String nombre, String segundoNombre, String apellido, String segundoApellido,
                               String edad, String telefono, String correo, String direccion, 
                               String cc, String sexo) {
        String sql = "INSERT INTO Alumnos (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, sexo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, segundoNombre);
            ps.setString(3, apellido);
            ps.setString(4, segundoApellido);
            ps.setString(5, edad);
            ps.setString(6, telefono);
            ps.setString(7, correo);
            ps.setString(8, direccion);
            ps.setString(9, cc);
            ps.setString(10, sexo);

            ps.executeUpdate();
            System.out.println(" Alumno insertado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // INSERTAR DOCENTE
    public void insertarDocente(String nombre, String segundoNombre, String apellido, String segundoApellido,
                                String edad, String telefono, String correo, String direccion, String cc) {
        String sql = "INSERT INTO Docentes (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, segundoNombre);
            ps.setString(3, apellido);
            ps.setString(4, segundoApellido);
            ps.setString(5, edad);
            ps.setString(6, telefono);
            ps.setString(7, correo);
            ps.setString(8, direccion);
            ps.setString(9, cc);

            ps.executeUpdate();
            System.out.println(" Docente insertado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  
    // INSERTAR NOTA 
    public void agregarNotaJava(int alumnoId, int corte, double nota) {
        String sql = "INSERT INTO Java (alumno_id, corte, nota) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, alumnoId);
            ps.setInt(2, corte);
            ps.setDouble(3, nota);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // OBTENER NOTAS 
    public List<Object[]> obtenerNotasJava() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT a.nombre, j.corte, j.nota " +
                     "FROM Java j " +
                     "JOIN Alumnos a ON j.alumno_id = a.id";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int corte = rs.getInt("corte");
                double nota = rs.getDouble("nota");
                lista.add(new Object[]{nombre, corte, nota});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ACTUALIZAR NOTA 

    public boolean actualizarNotaJava(int alumnoId, int corte, double nota) {
        String sql = "UPDATE Java SET nota = ? WHERE alumno_id = ? AND corte = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, nota);
            ps.setInt(2, alumnoId);
            ps.setInt(3, corte);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Error al actualizar nota: " + e.getMessage());
            return false;
        }
    }


    // OBTENER MATERIA DEL DOCENTE
 public int obtenerIdMateriaDocente(String profesor) {
    String sql = "SELECT m.id " +
                 "FROM materias m " +
                 "INNER JOIN docentes d ON m.id_docente = d.id " +
                 "WHERE d.nombre = ?";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, profesor);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return -1; 
}


    // OBTENER SEXO DEL ALUMNO
    public String obtenerSexoAlumno(String nombreUsuario) {
    String sexo = null;
    String sql = "SELECT sexo FROM alumnos WHERE nombre = ?"; 
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, nombreUsuario); 
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                sexo = rs.getString("sexo");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return sexo;
}


    
    // REINICIAR NOTAS
    public void reiniciarNotas() {
        String[] tablas = {"Java", "Poo", "Materias_net"};
        try (Connection con = getConnection()) {
            for (String tabla : tablas) {
                String sql = "UPDATE " + tabla + " SET nota = 0";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.executeUpdate();
                    System.out.println(" Notas reiniciadas en tabla: " + tabla);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al reiniciar notas: " + e.getMessage());
        }
    }

    
    // CORTES
    public Integer cortenuevo() {
        if (this.corte == 3) {
            this.corte = 0;
        }
        this.corte += 1;
        return corte;
    }

    public Integer corte() {
        return corte;
    }

    // VIAJAR EN EL TIEMPO 
    public String ViajarAlFuturo(int Corte) {      
        LocalDateTime fechaActual = LocalDateTime.now();
        LocalDateTime fechaAjustada = fechaActual;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        LocalDateTime fechaEstablecida;
        switch (Corte) {
            case 1:
                fechaEstablecida = LocalDateTime.of(2025, 10, 10 , 7, 1);
                break;
            case 2:
                fechaEstablecida = LocalDateTime.of(2025, 10, 14 , 7, 1);
                break;
            case 3:
                fechaEstablecida = LocalDateTime.of(2025, 10, 18 , 7, 1);
                break;
            default:
                throw new AssertionError();
        }

        if (fechaActual.isBefore(fechaEstablecida)) {
            long minutosDiferencia = Duration.between(fechaActual, fechaEstablecida).toMinutes();
            fechaAjustada = fechaActual.plusMinutes(minutosDiferencia);
        }

        return fechaAjustada.format(fmt);
    }

    
    // CREAR TABLA DE MATERIA DINÁMICAMENTE
    public void crearTablaMateria(String nombreTabla) {
    // Validar que el nombre no contenga caracteres peligrosos
    if (!nombreTabla.matches("^[a-zA-Z0-9_]+$")) {
        throw new IllegalArgumentException("Nombre de tabla inválido: " + nombreTabla);
    }

    // Plantilla de creación de tabla
    String sql = "CREATE TABLE IF NOT EXISTS " + nombreTabla + " (" +
                 "id INT AUTO_INCREMENT PRIMARY KEY, " +
                 "alumno_id INT, " +
                 "corte INT, " +
                 "nota DOUBLE, " +
                 "CONSTRAINT fk_" + nombreTabla + "_alumno FOREIGN KEY (alumno_id) " +
                 "REFERENCES Alumnos(id) ON DELETE CASCADE" +
                 ")";

    try (Connection con = getConnection();
         Statement stmt = con.createStatement()) {
        stmt.executeUpdate(sql);
        System.out.println("✅ Tabla creada correctamente: " + nombreTabla);
    } catch (SQLException e) {
        System.err.println("❌ Error al crear la tabla " + nombreTabla + ": " + e.getMessage());
    }
}

    // CONEXIÓN
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

  

 
}
