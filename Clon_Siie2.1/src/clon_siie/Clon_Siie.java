package clon_siie;

import clases.Base_De_Datos;
import vista.frmLogin;

public class Clon_Siie {
    
    public static void main(String[] args) {
        Base_De_Datos baseDatos = new Base_De_Datos();
        frmLogin Login=new frmLogin(baseDatos);
        Login.setVisible(true);
        
    }
    
}
