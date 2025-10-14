package clases;

import java.sql.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Base_De_Datos {
    
    private static final String URL = "jdbc:mysql://localhost:3306/CBN?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";   
    private static final String PASS = "316484"; 
    private Integer corte = 1;
    
    public String login(String usuario, String contrasena) {
        String sql = "call obtener_cargo_usuario(?,?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
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

    public String obtenerSexoAlumno(String nombreUsuario) {
        String sexo = null;
        String sql = "SELECT sexo FROM usuarios WHERE user_ = ?"; 
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

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
    
}
