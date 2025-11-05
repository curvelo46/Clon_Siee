package clon_siied;


import Clases.Base_De_Datos;
import Vista.frm.*;

public class Clon_SiieD {
    public static void main(String[] args) {

        // ====== Aplicar diseño Nimbus desde el main ======
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("No se pudo aplicar el estilo Nimbus: " + ex);
        }

        // ====== Iniciar base de datos ======
        Base_De_Datos baseDatos = new Base_De_Datos();
        
       

        // ====== Crear y mostrar ventana principal ======
            
            

            frmLogin jose=new frmLogin(baseDatos);
            jose.setVisible(true);

        
    }
}
