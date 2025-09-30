package clon_siie;

import clases.Base_De_Datos;
import clases.ConexionBD;
import vista.frmLogin;

public class Clon_Siie {
    
    public static void main(String[] args) {

        Base_De_Datos baseDatos = new Base_De_Datos();
        String teta = baseDatos.ViajarAlFuturo(2);
        
        System.out.println(teta);
       
        
        frmLogin Login=new frmLogin(baseDatos);
        Login.setVisible(true);
        
          if (ConexionBD.getConnection() != null) {
            System.out.println(" Conexión exitosa a la BD");
        } else {
            System.out.println(" Error en la conexión");
        }
            
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  
          
    }
}
