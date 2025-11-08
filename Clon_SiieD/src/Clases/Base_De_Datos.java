package Clases;

import java.sql.*;
import java.util.*;

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
    
    public List<String> listarCarreras() {
        List<String> carreras = new ArrayList<>();
        String sql = "CALL listar_carreras()";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                carreras.add(rs.getString("nombre"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return carreras;
    }
    
    
    public List<String> listarAlumnosPorCarrera(String carrera, String filtro) {
        List<String> alumnos = new ArrayList<>();
        String sql = (filtro == null || filtro.trim().isEmpty())
                ? "CALL listar_alumnos_por_carrera(?)"
                : "CALL buscar_alumnos_por_nombre_carrera(?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, carrera);
            if (filtro != null && !filtro.trim().isEmpty()) {
                ps.setString(2, filtro);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    alumnos.add(rs.getString("nombre") + " " + rs.getString("apellido"));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return alumnos;
    }
    
   
    public int obtenerIdAlumnoPorNombre(String nombre, String apellido) {
        String sql = "CALL obtener_id_alumno_por_nombre_apellido(?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    
    public int obtenerIdCarreraPorNombre(String nombreCarrera) {
        String sql = "CALL id_carrera_por_nombre(?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nombreCarrera);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    
    public List<Object[]> listarNotasPorAlumnoCarrera(int idAlumno, int idCarrera) {
        List<Object[]> notas = new ArrayList<>();
        String sql = "CALL listar_notas_por_alumno_carrera(?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idAlumno);
            ps.setInt(2, idCarrera);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String materia = rs.getString("nombre_materia");
                    double c1 = rs.getDouble("corte1");
                    double c2 = rs.getDouble("corte2");
                    double c3 = rs.getDouble("corte3");
                    double prom = (c1 + c2 + c3) / 3.0;
                    
                    notas.add(new Object[]{
                        materia,
                        String.format("%.1f", c1),
                        String.format("%.1f", c2),
                        String.format("%.1f", c3),
                        String.format("%.1f", prom)
                    });
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return notas;
    }
    
  
    public List<Object[]> obtenerReportesConDocente(int idAlumno) {
        List<Object[]> reportes = new ArrayList<>();
        String sql = "CALL obtener_reportes_con_docente(?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idAlumno);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reportes.add(new Object[]{
                        rs.getString("reporte"),
                        rs.getString("docente"),
                        rs.getTimestamp("fecha").toString()
                    });
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reportes;
    }
    
    
}


