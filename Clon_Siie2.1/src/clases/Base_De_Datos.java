package clases;

import java.util.*;

public class Base_De_Datos {

       // Listas organizadas por tipo de usuario
    private final List<Estudiante> estudiantes = new ArrayList<>();
    private final List<Profesor> profesores = new ArrayList<>();
    private final List<Administrador> administradores = new ArrayList<>();
    private final List<String> asignaturas = new ArrayList<>();

    // Relación profesor → materia
    private final Map<String, String> materiasPorProfesor = new HashMap<>();

    // Relación estudiante + materia → nota
    private final Map<String, Map<String, String>> calificaciones = new HashMap<>();

    public Base_De_Datos() {
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
        Profesor p1 = new Profesor("luis", "qwe");
        Profesor p2 = new Profesor("tatiana", "asd");
        Profesor p3 = new Profesor("carlos", "zxc");

        profesores.add(p1);
        profesores.add(p2);
        profesores.add(p3);

        // ---------- Administradores ----------
        administradores.add(new Administrador("seraira", "fgh"));
        administradores.add(new Administrador("carlos quintero", "jkl"));
        administradores.add(new Administrador("paula andrea", "bnm"));

        // ---------- Asignaturas ----------
        asignaturas.add("Diseño Web");
        asignaturas.add("POO");
        asignaturas.add("Desarrollo humano");

        // Relacionamos profesores con materias
        materiasPorProfesor.put("luis", "POO");
        materiasPorProfesor.put("tatiana", "Diseño Web");
        materiasPorProfesor.put("carlos", "Desarrollo humano");
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
    public boolean cargarNota(String estudiante, String profesor, String nota) {
        if (estudiante == null || profesor == null || nota == null ||
            estudiante.isEmpty() || profesor.isEmpty() || nota.isEmpty()) {
            return false;
        }

        String materia = materiasPorProfesor.get(profesor);
        if (materia == null) return false;

        calificaciones.putIfAbsent(estudiante, new HashMap<>());
        calificaciones.get(estudiante).put(materia, nota);

        return true;
    }

    public String obtenerNota(String estudiante, String profesor) {
        String materia = materiasPorProfesor.get(profesor);
        if (materia == null) return null;

        return calificaciones.getOrDefault(estudiante, new HashMap<>()).get(materia);
    }

    // ----- Listados -----
     public List<String> listaAlumnos() {
        List<String> nombres = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            nombres.add(e.getNombre());
        }
        return nombres;
    }

    public List<String> listaNotas(String profesor) {
        String materia = materiasPorProfesor.get(profesor);
        List<String> notas = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            Map<String, String> notasEst = calificaciones.get(e.getNombre());
            String nota = (notasEst != null) ? notasEst.getOrDefault(materia, "") : "";
            notas.add(nota);
        }
        return notas;
    }

    public String obtenerMateriaProfesor(String profesor) {
        return materiasPorProfesor.get(profesor);
    }

    public Map<String, String> obtenerNotasEstudiante(String estudiante) {
    Map<String, String> notas = new LinkedHashMap<>();
    for (Map.Entry<String, String> entry : materiasPorProfesor.entrySet()) {
        String materia = entry.getValue();
        String nota = calificaciones
                .getOrDefault(estudiante, new HashMap<>())
                .getOrDefault(materia, "0.0"); // por defecto 0.0
        notas.put(materia, nota);
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
