package clases;
import java.util.*;

public class Base_De_Datos{
    
    private final ArrayList<String> Estudiantes = new ArrayList<>();
    private ArrayList<String> Profesores = new ArrayList<>();
    private ArrayList<String> Administradores = new ArrayList<>();
    private ArrayList<String> Asignaturas = new ArrayList<>();
    private ArrayList<String> Estudiantes_contraseña = new ArrayList<>();
    private ArrayList<String> Profesores_contraseña = new ArrayList<>();
    private ArrayList<String> Administradores_contraseña = new ArrayList<>();
    private ArrayList<Notas> Calificaciones = new ArrayList<>();

    public Base_De_Datos() {
        this.Estudiantes.add("andres");
        this.Estudiantes_contraseña.add("123");

        this.Estudiantes.add("jose");
        this.Estudiantes_contraseña.add("1234");

        this.Estudiantes.add("maria");
        this.Estudiantes_contraseña.add("12345");

        this.Estudiantes.add("lucia");
        this.Estudiantes_contraseña.add("pass2024");

        this.Estudiantes.add("carlos");
        this.Estudiantes_contraseña.add("clave567");

        this.Estudiantes.add("sofia");
        this.Estudiantes_contraseña.add("abc12345");

        this.Estudiantes.add("diego");
        this.Estudiantes_contraseña.add("seguro789");

        this.Estudiantes.add("valeria");
        this.Estudiantes_contraseña.add("valpass1");

        this.Estudiantes.add("juan");
        this.Estudiantes_contraseña.add("juan2024");

        this.Estudiantes.add("laura");
        this.Estudiantes_contraseña.add("laura!234");       
        //-----------------------------------//
        this.Profesores.add("luis");
        this.Profesores_contraseña.add("qwe");
        this.Profesores.add("tatiana");
        this.Profesores_contraseña.add("asd");
        this.Profesores.add("carlos");
        this.Profesores_contraseña.add("zxc");
        //-----------------------------------//
        this.Administradores.add("seraira");
        this.Administradores_contraseña.add("fgh");
        this.Administradores.add("carlos quintero");
        this.Administradores_contraseña.add("jkl");
        this.Administradores.add("paula andrea");
        this.Administradores_contraseña.add("bnm");
        //-----------------------------------//
        this.Asignaturas.add("Diseño Web");
        this.Asignaturas.add("POO");
        this.Asignaturas.add("Desarrollo humano");
        
    }
    
    public boolean Login1(String v_usuario,String v_contraseña){        
        
    int indice = Estudiantes.indexOf(v_usuario);
    if (indice != -1) {
        return Estudiantes_contraseña.get(indice).equals(v_contraseña);
    }
    return false;
        
    }
    public boolean Login2(String v_usuario,String v_contraseña){        
        
    int indice = Profesores.indexOf(v_usuario);
    if (indice != -1) {
        return Profesores_contraseña.get(indice).equals(v_contraseña);
    }
    return false;
        
    }
    
    public boolean Login3(String v_usuario,String v_contraseña){        
        
    int indice = Administradores.indexOf(v_usuario);
    if (indice != -1) {
        return Administradores_contraseña.get(indice).equals(v_contraseña);
    }
    return false;
        
    }
    
    public ArrayList<String> ListaAlumnos(){
    return Estudiantes;
    }
    
}
