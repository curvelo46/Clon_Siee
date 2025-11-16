    package Clases;

    import java.awt.Component;
    import java.util.Locale;
    import java.sql.*;
    import java.text.SimpleDateFormat;
    import java.util.*;
    import javax.swing.JOptionPane;

    public class Base_De_Datos {

        private Integer corte = 1;

        // Método utilitario para manejar excepciones
        public static void manejarExcepcion(Component parent, Exception e, String mensajeContexto) {
            String tipoError = e.getClass().getSimpleName();
            String mensaje = String.format("%s%n%nError de sistema: %s%s", 
                mensajeContexto, 
                tipoError, 
                e.getMessage() != null ? "%nDetalle: " + e.getMessage() : "");
            //JOptionPane.showMessageDialog(parent, mensaje, "Error del Sistema", JOptionPane.ERROR_MESSAGE);
        }

        
        public String login(String usuario, String contrasena) {
            String sql = "CALL obtener_cargo_usuario(?,?)";

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
                manejarExcepcion(null, e, "Error al validar credenciales");
            }
            return null;
        }    

        
        public List<String> obtenerCargosPermitidos() {
            List<String> cargos = new ArrayList<>();
            String sql = "{CALL Cargos()}";

            try (Connection conn = ConexionBD.getConnection();
                CallableStatement cs = conn.prepareCall(sql);
                ResultSet rs = cs.executeQuery()) {

                while (rs.next()) {
                    cargos.add(rs.getString("cargo"));
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener cargos permitidos");
            }

            return new ArrayList<>(new LinkedHashSet<>(cargos));
        }    

        
        public String obtenerSexoAlumno(String nombreUsuario) {
            String sql = "CALL obtener_sexo_usuario(?)"; 

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nombreUsuario);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("sexo");
                    }
                }
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener genero del alumno");
            }
            return null;
        }   


        public String obtenerMateriaDelDocente(String usuarioDocente) {
            String materia = null;
            String sql = "CALL obtener_materia_docente(?)";

            try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, usuarioDocente);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    materia = rs.getString("materia");
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener materia del docente");
            }
            return materia;
        }


        public int obtenerIdMateriaPorDocente(String nombreMateria, String usernameDocente) {
        String sql = "CALL id_materia_por_docente(?, ?)";
        int idMateria = 0;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombreMateria);
            ps.setString(2, usernameDocente); 
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idMateria = rs.getInt("id");
            }
        } catch (SQLException e) {
            manejarExcepcion(null, e, "Error al obtener materia por docente");
        }
        return idMateria;
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
                manejarExcepcion(null, e, "Error al listar carreras");
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
                manejarExcepcion(null, e, "Error al listar alumnos por carrera");
            }

            return alumnos;
        }


        public int obtenerIdAlumnoPorNombre(String nombre, String apellido) {
        String sql = "CALL obtener_id_alumno_por_nombre_apellido(?, ?, ?)"; 

        try (Connection conn = ConexionBD.getConnection();
            CallableStatement cs = conn.prepareCall(sql)) {         

            cs.setString(1, nombre);
            cs.setString(2, apellido);
            cs.registerOutParameter(3, Types.INTEGER);  
            cs.execute();  

            return cs.getInt(3);

        } catch (SQLException e) {
            manejarExcepcion(null, e, "Error al obtener ID del alumno");
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
                manejarExcepcion(null, e, "Error al obtener ID de carrera");
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
                            String.format(Locale.US, "%.1f", c1),
                            String.format(Locale.US, "%.1f", c2),
                            String.format(Locale.US, "%.1f", c3),
                            String.format(Locale.US, "%.1f", prom)
                        });
                    }
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al listar notas por alumno y carrera");
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
                manejarExcepcion(null, e, "Error al obtener reportes con docente");
            }

            return reportes;
        }    


        public List<String> obtenerMateriasDelDocente(String usuarioDocente) {
            List<String> materias = new ArrayList<>();
            String sql = "CALL obtener_materias_docente(?)";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, usuarioDocente);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    materias.add(rs.getString("nombre"));
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener materias del docente");
            }
            return materias;
        }    


        public int obtenerCarreraIdPorMateria(int materiaId) {
            String sql = "{CALL obtener_carrera_de_materia(?)}";
            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {
                cs.setInt(1, materiaId);
                ResultSet rs = cs.executeQuery();
                if (rs.next()) {
                    return rs.getInt("carrera_id");
                }
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener carrera de materia");
            }
            return 0;
        }


        public int obtenerDocenteMateriaId(String profesor, String materia, int carreraId) {
            String sql = "CALL get_docente_materia_id(?, ?, ?)";
            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, profesor);
                ps.setString(2, materia);
                ps.setInt(3, carreraId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener docente materia ID");
            }
            return -1;
        }


        public List<Object[]> listarNotasDocenteMateria(String profesor, String materia, int carreraId) {
            List<Object[]> resultado = new ArrayList<>();
            String sql = "{CALL listar_notas_docente_materia(?, ?, ?)}";
    
            try (Connection cs = ConexionBD.getConnection();
                 PreparedStatement ps = cs.prepareStatement(sql)) {
                ps.setString(1, profesor);
                ps.setString(2, materia);
                ps.setInt(3, carreraId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Object[] fila = new Object[5];
                    fila[0] = rs.getInt("alumno_id");
                    fila[1] = rs.getString("estudiante");
                    fila[2] = rs.getDouble("corte1");
                    fila[3] = rs.getDouble("corte2");
                    fila[4] = rs.getDouble("corte3");
                    resultado.add(fila);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return resultado;
        }


        public void actualizarNotasBatch(List<Object[]> batchData) throws SQLException {
            String sql = "CALL actualizar_nota(?, ?, ?, ?)";
            Connection conn = null;
            CallableStatement cs = null;

            try {
                conn = ConexionBD.getConnection();
                cs = conn.prepareCall(sql);

                for (Object[] data : batchData) {
                    cs.setInt(1, (Integer) data[0]); 
                    cs.setInt(2, (Integer) data[1]); 
                    cs.setInt(3, (Integer) data[2]); 
                    cs.setDouble(4, (Double) data[3]); 
                    cs.addBatch();
                }

                cs.executeBatch();
            } catch (SQLException e) {
                throw e;
            } finally {
                if (cs != null) try { cs.close(); } catch (SQLException e) { 
                    manejarExcepcion(null, e, "Error al cerrar CallableStatement"); 
                }
                if (conn != null) try { conn.close(); } catch (SQLException e) { 
                    manejarExcepcion(null, e, "Error al cerrar conexión"); 
                }
            }
        }


        public List<String> listarTodasLasMaterias() {
            List<String> materias = new ArrayList<>();
            String sql = "call Listado_Materias()";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    materias.add(rs.getString("nombre"));
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al listar materias");
            }

            return materias;
        }


        public void crearCarreraConMaterias(String nombreCarrera, List<String> materiasSeleccionadas) throws SQLException {
            String sql = "CALL crear_carrera_con_materias(?, ?)";

            try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, nombreCarrera);
                ps.setString(2, String.join(",", materiasSeleccionadas));
                ps.executeUpdate();

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al crear carrera con materias");
                throw e; 
            }
        }


        public List<String[]> listarDocentes() {
            List<String[]> docentes = new ArrayList<>();
            String sql = "CALL listado_docentes()";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String[] docente = {
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nombre"),
                        rs.getString("apellido")
                    };
                    docentes.add(docente);
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al listar docentes");
            }

            return docentes;
        }


        public String obtenerUsernamePorIdAlumno(int idAlumno) {
        String sql = "CALL obtener_username_por_id_alumno(?)"; // Nuevo nombre
        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAlumno);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("user_");
                }
            }
        } catch (SQLException e) {
            manejarExcepcion(null, e, "Error al obtener username del alumno");
        }
        return null;
    }


        public List<String> listarMateriasPorCarrera(String carrera) {
            List<String> materias = new ArrayList<>();
            String sql = "CALL listar_nombres_materias_por_carrera_nombres(?)"; 

            try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, carrera);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    materias.add(rs.getString("materia_nombre"));
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al cargar materias");
            }
            return materias;
        }


        public int obtenerIdMateriaPorCarrera(String nombreMateria, String nombreCarrera) {
            String sql = "CALL id_materia_por_carrera(?, ?)";

            try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nombreMateria);
                ps.setString(2, nombreCarrera);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getInt("id");
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener ID de materia por carrera");
            }
            return 0;
        }


        public void crearMateria(String nombre, String carrera) throws Exception {
            Connection conn = null;

            try {
                conn = ConexionBD.getConnection();
                conn.setAutoCommit(false);

                int idCarrera = obtenerIdCarreraPorNombre(carrera);
                if (idCarrera == 0) {
                    throw new Exception("Carrera no encontrada");
                }

                String sql = "CALL crear_materia(?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, nombre);
                    ps.setInt(2, idCarrera);
                    ps.executeUpdate();
                }

                conn.commit();
                JOptionPane.showMessageDialog(null, "Materia creada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        manejarExcepcion(null, ex, "Error al hacer rollback");
                    }
                }
                manejarExcepcion(null, e, "Error al crear materia");
                throw e;
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        manejarExcepcion(null, e, "Error al cerrar conexión");
                    }
                }
            }
        }


        public void asignarMateriaADocente(int idDocente, int idMateria) throws Exception {
            String sql = "CALL asignar_m_a_p(?, ?)";

            try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idDocente);
                ps.setInt(2, idMateria);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(null, "Materia asignada al docente correctamente","Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al asignar materia a docente");
                throw e;
            }
        }


        public List<String> obtenerMateriasDocente(String usuarioDocente) {
            List<String> materias = new ArrayList<>();
            String sql = "CALL obtener_materias_docente(?)";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, usuarioDocente);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    materias.add(rs.getString("nombre"));
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener materias del docente");
            }

            return materias;
        }


        public int obtenerCarreraIdPorDocenteMateria(String usuarioDocente, String nombreMateria) {
            String sql = "CALL obtener_carrera_id_por_docente_materia(?, ?)";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, usuarioDocente);
                ps.setString(2, nombreMateria);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getInt("carrera_id");
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener carrera por docente y materia");
            }

            return 0;
        }


        public List<Object[]> listarEstudiantesPorDocenteMateria(String profesor, String materia, int idCarrera) {
        List<Object[]> estudiantes = new ArrayList<>();
        String sql = "CALL listar_estudiantes_por_docente_materia(?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, profesor);
            ps.setString(2, materia);
            ps.setInt(3, idCarrera);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] estudiante = {
                    rs.getString("cc"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("edad"),
                    rs.getString("telefono"),
                    rs.getString("correo"),
                    rs.getInt("id")
                };
                estudiantes.add(estudiante);
            }
        } catch (SQLException e) {
            manejarExcepcion(null, e, "Error al listar estudiantes por docente y materia");
        }
        return estudiantes;
    }


        public Object[] buscarUsuarioPorCedula(String cedula) {
            String sql = "call buscar_usuario(?)";
            Object[] usuario = null;

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, cedula);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    usuario = new Object[]{
                        rs.getInt("id"),
                        rs.getString("cargo"),
                        rs.getString("nombre"),
                        rs.getString("segundo_nombre"),
                        rs.getString("apellido"),
                        rs.getString("segundo_apellido"),
                        rs.getInt("edad"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("direccion")
                    };
                }
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al buscar usuario por cédula");
            }
            return usuario;
        }


        public void actualizarUsuario(String cargo, int usuarioId, String cedula, String nombre, 
        String segundoNombre, String apellido, String segundoApellido, 
        int edad, String telefono, String correo, String direccion) {

            String procedimiento = null;
            switch (cargo) {
                case "alumno":
                    procedimiento = "{call actualizar_estudiante(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                    break;
                case "docente":
                    procedimiento = "{call actualizar_docente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                    break;
                case "administrador":
                    procedimiento = "{call actualizar_administrador(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                    break;
                case "registro y control":
                    procedimiento = "{call actualizar_registro_control(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "No se puede editar este tipo de usuario","Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            Connection conn = null;
            try {
                conn = ConexionBD.getConnection();
                conn.setAutoCommit(false);

                try (CallableStatement cs = conn.prepareCall(procedimiento)) {
                    cs.setInt(1, usuarioId);
                    cs.setString(2, cedula);
                    cs.setString(3, nombre);
                    cs.setString(4, segundoNombre);
                    cs.setString(5, apellido);
                    cs.setString(6, segundoApellido);
                    cs.setInt(7, edad);
                    cs.setString(8, telefono);
                    cs.setString(9, correo);
                    cs.setString(10, direccion);

                    cs.executeUpdate();
                    conn.commit();

                    JOptionPane.showMessageDialog(null, "Usuario actualizado correctamente","Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        manejarExcepcion(null, ex, "Error al hacer rollback");
                    }
                }
                manejarExcepcion(null, e, "Error al actualizar usuario");
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        manejarExcepcion(null, e, "Error al cerrar conexión");
                    }
                }
            }
        }


        public List<String> obtenerRolesUsuarios() {
            List<String> roles = new ArrayList<>();
            String sql = "{CALL obtener_roles_usuarios()}";

            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement cs = conn.prepareCall(sql);
                 ResultSet rs = cs.executeQuery()) {

                while (rs.next()) {
                    roles.add(rs.getString("rol"));
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener roles de usuarios");
            }

            return roles;
        }


        public List<Object[]> listarUsuariosPorRol(String rol) {
            List<Object[]> usuarios = new ArrayList<>();
            String sql = "{CALL listar_usuarios_por_rol(?)}";

            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {

                cs.setString(1, rol);
                ResultSet rs = cs.executeQuery();

                while (rs.next()) {
                    Object[] usuario = {
                        rs.getString("cc"),
                        rs.getString("nombre"),
                        rs.getString("segundo_nombre"),
                        rs.getString("apellido"),
                        rs.getString("segundo_apellido"),
                        rs.getString("edad"),
                        rs.getString("sexo"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("correo")
                    };
                    usuarios.add(usuario);
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al listar usuarios por rol");
            }

            return usuarios;
        }


        public List<String> listarAlumnosSistema() {
            List<String> alumnos = new ArrayList<>();
            String sql = "CALL Listar_Alumnos_sistema()";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    alumnos.add(rs.getString("nombre_completo"));
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al listar alumnos del sistema");
            }

            return alumnos;
        }


        public List<Object[]> listarMateriasPorCarreraConDocente(String nombreCarrera) {
        List<Object[]> materias = new ArrayList<>();
        String sql = "CALL listar_materias_por_carrera_con_docente(?)"; 

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombreCarrera);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String docenteNombre = rs.getString("docente_nombre");
                if (docenteNombre == null) {
                    docenteNombre = "Sin docente asignado";
                }

                Object[] materia = {
                    rs.getInt("docente_materia_id"),
                    rs.getString("materia_nombre"),
                    docenteNombre
                };
                materias.add(materia);
            }

        } catch (SQLException e) {
            manejarExcepcion(null, e, "Error al cargar materias con docentes");
        }

        return materias;
    }


        public int obtenerIdAlumnoPorNombreCompleto(String nombreCompleto) {
            int idAlumno = 0;
            String sql = "CALL obtener_id_alumno_por_nombre_completo(?, ?)";

            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {

                cs.setString(1, nombreCompleto);
                cs.registerOutParameter(2, Types.INTEGER);
                cs.execute();

                idAlumno = cs.getInt(2);

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener ID de alumno por nombre completo");
            }

            return idAlumno;
        }


        public int matricularAlumnoEnMateria(int idAlumno, int idDocenteMateria) {
            int resultado = -1;
            String sql = "CALL matricular_alumno_individual(?, ?, ?)";

            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {

                cs.setInt(1, idAlumno);
                cs.setInt(2, idDocenteMateria);
                cs.registerOutParameter(3, Types.TINYINT);

                cs.execute();
                resultado = cs.getInt(3);

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al matricular alumno en materia");
            }

            return resultado;
        }


        public int retirarAlumnoDeMateria(int idAlumno, int idDocenteMateria) {
            int resultado = 0;
            String sql = "CALL retirar_alumno_individual(?, ?, ?)";

            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {

                cs.setInt(1, idAlumno);
                cs.setInt(2, idDocenteMateria);
                cs.registerOutParameter(3, Types.TINYINT);

                cs.execute();
                resultado = cs.getInt(3);

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al retirar alumno de materia");
            }

            return resultado;
        }


        public int retirarAlumnoDeCarrera(int idAlumno, String nombreCarrera) {
            int cantidad = 0;
            String sql = "CALL retirar_alumno_carrera(?, ?, ?)";

            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {

                cs.setInt(1, idAlumno);
                cs.setString(2, nombreCarrera);
                cs.registerOutParameter(3, Types.INTEGER);

                cs.execute();
                cantidad = cs.getInt(3);

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al retirar alumno de carrera");
            }

            return cantidad;
        }


        public List<Object[]> obtenerCarrerasDelAlumno(String username) {
            List<Object[]> carreras = new ArrayList<>();
            String sql = "{CALL obtener_carreras_del_alumno(?)}";

            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {

                cs.setString(1, username);
                ResultSet rs = cs.executeQuery();

                while (rs.next()) {
                    Object[] carrera = {
                        rs.getInt("id"),
                        rs.getString("nombre")
                    };
                    carreras.add(carrera);
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener carreras del alumno");
            }

            return carreras;
        }


        public int obtenerIdAlumnoPorUsername(String username) {
            String sql = "{CALL obtener_id_alumno_por_user(?)}";
            int idAlumno = -1;

            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {

                cs.setString(1, username);
                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    idAlumno = rs.getInt("id");
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener ID de alumno por username");
            }

            return idAlumno;
        }


        public List<Object[]> listarNotasPromedioDocenteMateria(String docente, String materia) {
            List<Object[]> notas = new ArrayList<>();
            String sql = "CALL listar_notas_docente_materia(?, ?, NULL)";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, docente);
                ps.setString(2, materia);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Object[] nota = {
                        rs.getString("estudiante"),
                        rs.getDouble("corte1"),
                        rs.getDouble("corte2"),
                        rs.getDouble("corte3")
                    };
                    notas.add(nota);
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al listar notas promedio docente materia");
            }

            return notas;
        }


        public List<String> cargarCarrerasRegistro() {
            List<String> carreras = new ArrayList<>();
            String sql = "{CALL carreras()}";

            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement cs = conn.prepareCall(sql);
                 ResultSet rs = cs.executeQuery()) {

                while (rs.next()) {
                    carreras.add(rs.getString("nombre"));
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al cargar carreras para registro");
            }

            return carreras;
        }


        public boolean registrarPersonal(String nom, String segNom, String ape, String segApe, 
        int edad, String tel, String mail, String dir, 
        String ced, String gen, String cargo, String carrera) {
            String sql = "{CALL registrar_personal(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            Connection conn = null;
            CallableStatement cs = null;

            try {
                conn = ConexionBD.getConnection();
                conn.setAutoCommit(false);

                cs = conn.prepareCall(sql);

                cs.setString(1, nom);
                cs.setString(2, segNom.isEmpty() ? null : segNom);
                cs.setString(3, ape);
                cs.setString(4, segApe.isEmpty() ? null : segApe);
                cs.setInt(5, edad);
                cs.setString(6, tel.isEmpty() ? null : tel);
                cs.setString(7, mail.isEmpty() ? null : mail);
                cs.setString(8, dir.isEmpty() ? null : dir);
                cs.setString(9, ced);
                cs.setString(10, gen);
                cs.setString(11, cargo);
                cs.setString(12, carrera); 

                cs.execute();
                conn.commit(); 

                return true;

            } catch (SQLException ex) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException e) {
                        manejarExcepcion(null, e, "Error al hacer rollback");
                    }
                }
                manejarExcepcion(null, ex, "Error al registrar personal");
                return false;
            } finally {
                if (cs != null) {
                    try {
                        cs.close();
                    } catch (SQLException e) {
                        manejarExcepcion(null, e, "Error al cerrar CallableStatement");
                    }
                }
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        manejarExcepcion(null, e, "Error al cerrar conexión");
                    }
                }
            }
        }


        public List<Object[]> obtenerReportesAlumnoCompletos(String usuarioActual) {
        List<Object[]> reportes = new ArrayList<>();
        String sql = "{CALL obtener_reportes_alumno_completos(?)}";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, usuarioActual);
            ResultSet rs = cs.executeQuery();

            boolean tieneReportes = false;
            while (rs.next()) {
                tieneReportes = true;
                Timestamp fecha = rs.getTimestamp("fecha");
                String docente = rs.getString("docente");
                String materia = rs.getString("materias");
                String reporte = rs.getString("reporte");

                Object[] fila = {
                    fecha != null ? new SimpleDateFormat("dd/MM/yyyy").format(fecha) : "", 
                    docente != null ? docente : "N/A",
                    materia != null ? materia : "N/A",
                    reporte != null ? reporte : ""
                };
                reportes.add(fila);
            }

            if (!tieneReportes) {
                reportes.add(new Object[]{"No hay reportes registrados", "", "", ""});
            }

        } catch (SQLException e) {
            manejarExcepcion(null, e, "Error al obtener reportes completos del alumno");
        }

        return reportes;
    }


        public int obtenerCarreraIdDesdeMateria(String materia) {
            String sql = "call carrera_id_d_materia(?)";
            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, materia);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener carrera desde materia");
            }
            return 0;
        }


        public java.util.List<String> obtenerMateriasDocentePorCarrera(String profesor, int idCarrera) {
        java.util.List<String> materias = new java.util.ArrayList<>();
        String sql = "CALL obtener_materias_docente_por_carrera(?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, profesor);
            ps.setInt(2, idCarrera);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    materias.add(rs.getString("materia"));
                }
            }
        } catch (SQLException e) {
            manejarExcepcion(null, e, "Error al obtener materias por carrera");
        }

        return materias;
    }


        public Map<String, Integer> obtenerCarrerasConMateriasDocente(String profesor) {
            Map<String, Integer> carreras = new HashMap<>();
            String sql = "CALL obtener_carreras_con_materias_docente(?)";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, profesor);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nombre = rs.getString("nombre");
                        carreras.put(nombre, id);
                    }
                }
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener carreras con materias");
            }

            return carreras;
        }


        public boolean alumnoExisteEnTablaAlumnos(int idAlumno) {
            String sql = "call  alumno_existente(?)";
            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idAlumno);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al verificar existencia de alumno");
            }
            return false;
        }


        public int obtenerIdDocenteDesdeUsuario(String usuario) {
            String sql = "CALL obtener_id_docente_por_user(?)";
            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, usuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener ID de docente");
            }
            return 0;
        }


        public String obtenerCedulaDesdeId(int idAlumno) {
            String sql = "CALL obtener_cedula(?)";
            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setInt(1, idAlumno);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("cc");
                    }
                }
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener cédula");
            }
            return "";
        }


        public Map<String, Integer> buscarAlumnosPorMateriaCarreraDocente(
            String usuarioDocente, String materia, int idCarrera, String filtro) {
            Map<String, Integer> alumnos = new HashMap<>();
            String sql = "CALL buscar_alumnos_por_materia_carrera_docente(?, ?, ?, ?)";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, usuarioDocente);
                ps.setString(2, materia);
                ps.setInt(3, idCarrera);
                ps.setString(4, filtro);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("alumno_id");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String display = nombre + " " + apellido;
                    alumnos.put(display, id);
                }
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al buscar alumnos específicos");
            }

            return alumnos;
        }


        public boolean insertarReporte(int idAlumno, String reporte, int idDocente, int docenteMateriaId) {
            String sql = "CALL insertar_reporte(?, ?, ?, ?)";
            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setInt(1, idAlumno);
                stmt.setString(2, reporte);
                stmt.setInt(3, idDocente);
                stmt.setInt(4, docenteMateriaId);
                stmt.execute();
                return true;
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al insertar reporte");
                return false;
            }
        }


        public Map<String, Integer> buscarAlumnosPorNombreCarrera(String carrera, String filtro) {
            Map<String, Integer> alumnos = new HashMap<>();
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
                        int id = rs.getInt("alumno_id");
                        String nombre = rs.getString("nombre");
                        String apellido = rs.getString("apellido");
                        String display = nombre + " " + apellido;
                        alumnos.put(display, id);
                    }
                }
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al buscar alumnos");
            }

            return alumnos;
        }


        // Métodos para panel retirar materia a docente
        public List<String> obtenerMateriasDocentePorId(int idDocente) {
            List<String> materias = new ArrayList<>();
            String sql = "CALL obtener_materias_docente_por_id(?)";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idDocente);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        materias.add(rs.getString("materia"));
                    }
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener materias del docente");
            }

            return materias;
        }


        public Map<String, Integer> buscarDocentesPorNombre(String texto) {
            Map<String, Integer> docentes = new HashMap<>();
            String sql = "CALL buscar_docentes_por_nombre(?)";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, texto);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nombre = rs.getString("nombre");
                        String apellido = rs.getString("apellido");
                        String display = nombre + " " + apellido;
                        docentes.put(display, id);
                    }
                }

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al buscar docentes");
            }

            return docentes;
        }


        public boolean eliminarAsignacionDocenteMateria(int idDocente, String materia) {
            String sql = "{CALL eliminar_asignacion_docente_materia(?, ?)}";
            Connection conn = null;
            CallableStatement cs = null;

            try {
                conn = ConexionBD.getConnection();
                conn.setAutoCommit(false);

                cs = conn.prepareCall(sql);
                cs.setInt(1, idDocente);
                cs.setString(2, materia);
                cs.execute();

                conn.commit();
                return true;

            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        manejarExcepcion(null, ex, "Error al hacer rollback");
                    }
                }
                manejarExcepcion(null, e, "Error al eliminar asignación docente materia");
                return false;

            } finally {
                if (cs != null) try { cs.close(); } catch (SQLException e) { 
                    manejarExcepcion(null, e, "Error al cerrar CallableStatement"); 
                }
                if (conn != null) try { conn.close(); } catch (SQLException e) { 
                    manejarExcepcion(null, e, "Error al cerrar conexión"); 
                }
            }
        }


        public List<Object[]> obtenerMateriasDocenteConCarrera(int idDocente) {
            List<Object[]> materias = new ArrayList<>();
            String sql = "CALL obtener_materias_docente_con_carrera(?)";

            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idDocente);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Object[] materia = {
                        rs.getString("materia_nombre"),
                        rs.getString("carrera_nombre"),
                        rs.getInt("docente_materia_id")
                    };
                    materias.add(materia);
                }
            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al obtener materias con carrera");
            }

            return materias;
        }


        public boolean reemplazarDocenteEnMateria(int docenteMateriaId, int nuevoDocenteId) {
            String sql = "CALL reemplazar_docente_en_materia(?, ?)";
            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {

                cs.setInt(1, docenteMateriaId);
                cs.setInt(2, nuevoDocenteId);
                cs.execute();
                return true;

            } catch (SQLException e) {
                manejarExcepcion(null, e, "Error al reemplazar docente en materia");
                return false;
            }
        }


        public void actualizarUsuarioConRol(int usuarioId, String cedula, String nombre, 
            String segundoNombre, String apellido, String segundoApellido, 
            int edad, String telefono, String correo, String direccion, String cargo) {

            String sql = "{CALL actualizar_usuario_con_rol(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

            try (Connection conn = ConexionBD.getConnection();
                 CallableStatement cs = conn.prepareCall(sql)) {

                cs.setInt(1, usuarioId);
                cs.setString(2, cedula);
                cs.setString(3, nombre);
                cs.setString(4, segundoNombre);
                cs.setString(5, apellido);
                cs.setString(6, segundoApellido);
                cs.setInt(7, edad);
                cs.setString(8, telefono);
                cs.setString(9, correo);
                cs.setString(10, direccion);
                cs.setString(11, cargo);

                cs.execute();

            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar usuario con rol: " + e.getMessage(), e);
            }
        }

    public List<Object[]> obtenerReportesAlumnoConId(String username) {
        List<Object[]> reportes = new ArrayList<>();
        String sql = "{CALL obtener_reportes_alumno_con_id(?)}"; 

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, username);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                reportes.add(new Object[]{
                    rs.getInt("id_reporte"),     
                    rs.getTimestamp("fecha") != null ? 
                        new SimpleDateFormat("dd/MM/yyyy").format(rs.getTimestamp("fecha")) : "",
                    rs.getString("docente") != null ? rs.getString("docente") : "N/A",
                    rs.getString("materia") != null ? rs.getString("materia") : "N/A",
                    rs.getString("reporte") != null ? rs.getString("reporte") : ""
                });
            }

        } catch (SQLException e) {
            manejarExcepcion(null, e, "Error al obtener reportes con ID");
        }

        return reportes;
    }

    // 2. Método para eliminar reporte
    public boolean eliminarReportePorId(int idReporte) {
        String sql = "{CALL eliminar_reporte_por_id(?)}";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idReporte);
            cs.execute();
            return true;

        } catch (SQLException e) {
            manejarExcepcion(null, e, "Error al eliminar reporte");
            return false;
        }
    }


    public void resetearTodasLasNotas() throws SQLException {
        String sql = "CALL resetear_todas_las_notas()";

        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); 

            cs = conn.prepareCall(sql);
            cs.execute();

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    manejarExcepcion(null, ex, "Error al hacer rollback en reseteo de notas");
                }
            }
            manejarExcepcion(null, e, "Error al resetear todas las notas del sistema");
            throw e; 

        } finally {
            if (cs != null) try { cs.close(); } catch (SQLException e) { 
                manejarExcepcion(null, e, "Error al cerrar CallableStatement"); 
            }
            if (conn != null) try { conn.close(); } catch (SQLException e) { 
                manejarExcepcion(null, e, "Error al cerrar conexión"); 
            }
        }
    }

    
   
public int obtenerCorteActual(int docenteMateriaId) throws SQLException {
    String sql = "call corte_actual_porMateria(?);";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, docenteMateriaId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("corte_actual");
        }
    }
    return 1; 
}

public void actualizarCorteActual(int docenteMateriaId, int corte) throws SQLException {
    String sql = "call actualizar_corte_actual_porMateria(?,?);";
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, docenteMateriaId);
        ps.setInt(2, corte);
        ps.executeUpdate();
    }
}

    public int corte_montado(){
        this.corte+=1;
            return 0;
    }
    
    public int corte_actual(){
            return this.corte;
    }
    
    
    
}