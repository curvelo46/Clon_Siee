package clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/CBN";
    private static final String USER = "root";   // usuario de MySQL
    private static final String PASS = "316484";       // sin contraseña

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Cargar el driver (opcional en versiones nuevas de Java)
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(ConexionBD.URL, ConexionBD.USER, ConexionBD.PASS);
            System.out.println("✅ Conexión exitosa a la BD");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ No se encontró el Driver JDBC de MySQL");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Error en la conexión a la BD");
            e.printStackTrace();
        }
        return conn;
    }
}
