package clases;

import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Base_De_Datos {
    
    private static final String URL = "jdbc:mysql://localhost:3306/CBN";
    private static final String USER = "root";   
    private static final String PASS = "316484";    
    private Integer corte=1;
    
    // ----- LOGIN -----
   public String login(String usuario, String contraseña) {
    String sql = "SELECT cargo FROM usuarios WHERE user_ = ? AND contraseña = ?";
    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, usuario);
        ps.setString(2, contraseña);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("cargo"); // devuelve "alumno", "docente" o "administrador"
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // si no encontró nada
}


    // ----- LISTAR ALUMNOS -----
    public List<String> listaAlumnos() {
        List<String> alumnos = new ArrayList<>();
        String sql = "SELECT nombre, apellido FROM Alumnos";
        try (Connection con = ConexionBD.getConnection();
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

    public void Agregar_Notas() throws SQLException{
        String sql="insert into java values('jose','1','4') ";
        Connection conn = ConexionBD.getConnection();
         Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            System.getLogger(Base_De_Datos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
         stmt.executeUpdate(sql);
        
    }
    
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
   public List<Object[]> obtenerNotasJava() {
    List<Object[]> lista = new ArrayList<>();
    String sql = "SELECT nombre, corte, nota FROM java";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String nombre = rs.getString("nombre");
            
            Double nota = rs.getDouble("nota"); 

            lista.add(new Object[]{nombre, nota});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return lista;
}

   public boolean actualizarNotaJava(String nombre, int corte, double nota) {
    String sql = "UPDATE java SET nota = ? WHERE nombre = ? AND corte = ?";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setDouble(1, nota);
        ps.setString(2, nombre);
        ps.setInt(3, corte);

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al actualizar nota: " + e.getMessage());
        return false;
    }
}

  public String obtenerMateriaDocente(String user) {
    String materia = null;
    String sql = "SELECT m.materia " +
                 "FROM usuarios u " +
                 "JOIN materias m ON u.user_ = m.nombre " +
                 "WHERE u.user_ = ? AND u.cargo = 'docente'";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, user);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            materia = rs.getString("materia");
        } else {
            System.out.println("❌ No se encontró materia para el docente: " + user);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return materia;
}

  public String obtenerSexoAlumno(String nombreAlumno) {
    String sexo = null;
    String sql = "SELECT sexo FROM Alumnos WHERE nombre = ?";

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, nombreAlumno);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            sexo = rs.getString("sexo");  // Devuelve "hombre" o "mujer"
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return sexo;
}
  
  
  public String ViajarAlFuturo(int Corte){      
        LocalDateTime fechaActual = LocalDateTime.now();
        LocalDateTime fechaAjustada = fechaActual;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        switch (Corte) {
          case 1:
            LocalDateTime fechaEstablecida = LocalDateTime.of(2025, 10,10 , 7, 1);
            if (fechaActual.isBefore(fechaEstablecida)) {
                long minutosDiferencia = Duration.between(fechaActual, fechaEstablecida).toMinutes();
                fechaAjustada = fechaActual.plusMinutes(minutosDiferencia);
            }
       
            return fechaAjustada.format(fmt);
             
          case 2:
            fechaEstablecida = LocalDateTime.of(2025, 10,14 , 7, 1);
            
            if (fechaActual.isBefore(fechaEstablecida)) {
                long minutosDiferencia = Duration.between(fechaActual, fechaEstablecida).toMinutes();
                fechaAjustada = fechaActual.plusMinutes(minutosDiferencia);
            }
       
            return fechaAjustada.format(fmt);
             
          case 3:
            fechaEstablecida = LocalDateTime.of(2025, 10,18 , 7, 1);
            
            if (fechaActual.isBefore(fechaEstablecida)) {
                long minutosDiferencia = Duration.between(fechaActual, fechaEstablecida).toMinutes();
                fechaAjustada = fechaActual.plusMinutes(minutosDiferencia);
            }
       
            return fechaAjustada.format(fmt);
             
          default:
              throw new AssertionError();
      }
  
       
        
  }
  
  public void reiniciarNotas() {
    String[] tablas = {"java", "poo", "materias_net"};
    try (Connection con = getConnection()) {
        for (String tabla : tablas) {
            String sql = "UPDATE " + tabla + " SET nota = 0";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.executeUpdate();
                System.out.println("✅ Notas reiniciadas en tabla: " + tabla);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al reiniciar notas: " + e.getMessage());
    }
}

  
  public Integer cortenuevo(){
      
       if (this.corte==3){
          this.corte=0;
         
        }
      
      this.corte+=1;
      return corte;
      
     
  }

  public Integer corte(){
      
      return corte;

  }
  
  
  
}



