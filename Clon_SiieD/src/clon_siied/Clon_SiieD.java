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

            frmLogin jose=new frmLogin();
            jose.setVisible(true);

        
    }
}
