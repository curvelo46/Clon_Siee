package clases;

import java.util.*;

public class Base_De_Datos {

    // Listas organizadas por tipo de usuario
    private final List<Estudiante> estudiantes = new ArrayList<>();
    private final List<Profesor> profesores = new ArrayList<>();
    private final List<Administrador> administradores = new ArrayList<>();
    private final List<String> asignaturas = new ArrayList<>();

    // Mapa para relacionar estudiante con su nota
    private final Map<String, String> calificaciones = new HashMap<>();

    public Base_De_Datos() {
        // ---------- Estudiantes ----------
        estudiantes.add(new Estudiante("andres", "123"));
        estudiantes.add(new Estudiante("jose", "1234"));
        estudiantes.add(new Estudiante("maria", "12345"));
        estudiantes.add(new Estudiante("lucia", "pass2024"));
        estudiantes.add(new Estudiante("carlos", "clave567"));
        estudiantes.add(new Estudiante("sofia", "abc12345"));
        estudiantes.add(new Estudiante("diego", "seguro789"));
        estudiantes.add(new Estudiante("valeria", "valpass1"));
        estudiantes.add(new Estudiante("juan", "juan2024"));
        estudiantes.add(new Estudiante("laura", "laura!234"));

        // ---------- Profesores ----------
        profesores.add(new Profesor("luis", "qwe"));
        profesores.add(new Profesor("tatiana", "asd"));
        profesores.add(new Profesor("carlos", "zxc"));

        // ---------- Administradores ----------
        administradores.add(new Administrador("seraira", "fgh"));
        administradores.add(new Administrador("carlos quintero", "jkl"));
        administradores.add(new Administrador("paula andrea", "bnm"));

        // ---------- Asignaturas ----------
        asignaturas.add("Diseño Web");
        asignaturas.add("POO");
        asignaturas.add("Desarrollo humano");
    }

    // ----- Login -----
    public boolean Login1(String usuario, String contraseña) {
        return estudiantes.stream()
                .anyMatch(e -> e.getNombre().equals(usuario) && e.getContraseña().equals(contraseña));
    }

    public boolean Login2(String usuario, String contraseña) {
        return profesores.stream()
                .anyMatch(p -> p.getNombre().equals(usuario) && p.getContraseña().equals(contraseña));
    }

    public boolean Login3(String usuario, String contraseña) {
        return administradores.stream()
                .anyMatch(a -> a.getNombre().equals(usuario) && a.getContraseña().equals(contraseña));
    }

    // ----- Notas -----
    public boolean cargarNota(String estudiante, String nota) {
         if (estudiante == null || nota == null || estudiante.isEmpty() || nota.isEmpty()) {
        return false; // No guardar si datos inválidos
    }
    calificaciones.put(estudiante, nota);
    return true;
         
    }

    public String obtenerNota(String estudiante) {
        return calificaciones.get(estudiante);
    }

    // ----- Listados -----
    public List<String> listaAlumnos() {
        List<String> nombres = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            nombres.add(e.getNombre());
        }
        return nombres;
    }

    public List<String> listaNotas() {
        List<String> notas = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            notas.add(obtenerNota(e.getNombre()));
        }
        return notas;
    }

    public List<String> listaAsignaturas() {
        return asignaturas;
    }

   
}

// ----- Clases de apoyo -----
class Usuario {
    private String nombre;
    private String contraseña;

    public Usuario(String nombre, String contraseña) {
        this.nombre = nombre;
        this.contraseña = contraseña;
    }

    public String getNombre() { return nombre; }
    public String getContraseña() { return contraseña; }
}

class Estudiante extends Usuario {
    public Estudiante(String nombre, String contraseña) { super(nombre, contraseña); }
}

class Profesor extends Usuario {
    public Profesor(String nombre, String contraseña) { super(nombre, contraseña); }
}

class Administrador extends Usuario {
    public Administrador(String nombre, String contraseña) { super(nombre, contraseña); }
}
