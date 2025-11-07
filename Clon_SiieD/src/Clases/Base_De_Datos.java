package Clases;

import java.sql.*;

public class Base_De_Datos {
    
    private Integer corte = 0;
    
    public String login(String usuario, String contrasena) {
        String sql = "call obtener_cargo_usuario(?,?)";

        try (Connection con = ConexionBD.getConnection();
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
        String sql = "call obtener_sexo_usuario(?)"; 
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
}
