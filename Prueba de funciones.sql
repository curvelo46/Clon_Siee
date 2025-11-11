-- =========================================================
-- 📘 SCRIPT DE PRUEBAS - PROCESOS, FUNCIONES Y TRIGGERS
-- SISTEMA CBN
-- Autor: Equipo de Desarrollo CBN
-- Fecha: CURRENT_DATE()
-- =========================================================

USE cbn;

-- =========================================================
-- 🔍 SECCIÓN 1: CONSULTAS GENERALES Y LISTADOS
-- =========================================================
CALL listar_docentes();
CALL listado_docentes();
CALL Listar_Alumnos_sistema();
CALL carreras();
CALL Cargos();
CALL Listado_Materias();
CALL listar_carreras();
CALL obtener_roles_usuarios();
CALL listar_usuarios_por_rol('alumno');	
CALL listar_alumnos_por_carrera('Ingeniería de Sistemas');
CALL listar_notas_docente_materia('laura@mail.com', 'Bases de Datos', 1);
CALL listar_notas_por_alumno_carrera(1, 1);

-- =========================================================
-- ⚙️ SECCIÓN 2: ACTUALIZACIÓN DE USUARIOS SEGÚN ROL
-- =========================================================
CALL actualizar_estudiante(1, '123456789', 'Carlos', NULL, 'Ramírez', NULL, 21, '3009999999', 'carlos_update@mail.com', 'Cra 20 #30-40');
CALL actualizar_docente(3, '987654321', 'Laura', NULL, 'Gómez', NULL, 36, '3018888888', 'laura_update@mail.com', 'Calle 80 #20-15');
CALL actualizar_administrador(4, '112233445', 'Mario', NULL, 'Sánchez', NULL, 41, '3021111111', 'mario_update@mail.com', 'Av 7 #12-90');
CALL actualizar_registro_control(5, '112245', 'Mandres', NULL, 'Sánchez', NULL, 41, '3029999999', 'mandres_update@mail.com', 'Av 6 #15-100');

-- =========================================================
-- 🧾 SECCIÓN 3: INSERCIÓN Y REGISTROS NUEVOS
-- =========================================================
CALL registrar_personal(
    'Daniel', NULL, 'Cruz', NULL, 20, '3007776666', 'daniel@mail.com', 
    'Calle 25 #18-50', '55667788', 'masculino', 'alumno', 'Ingeniería de Sistemas'
);

CALL registrar_personal(
    'María', NULL, 'Pérez', NULL, 32, '3005554444', 'maria@mail.com', 
    'Calle 12 #10-10', '99887766', 'femenino', 'docente', NULL
);

SELECT * FROM Usuarios ORDER BY id DESC;

-- =========================================================
-- 📚 SECCIÓN 4: MATERIAS Y CARRERAS
-- =========================================================
CALL crear_materia('Matemáticas Avanzadas', 1);
CALL crear_carrera_con_materias('Arquitectura', 'Diseño, Planificación, Materiales');
CALL id_carrera_por_nombre('Ingeniería de Sistemas');
CALL id_materia('Bases de Datos', 1);
CALL id_materia_por_carrera('Programación I', 'Ingeniería de Sistemas');
CALL carrera_id_d_materia('Contabilidad');
CALL obtener_carrera_de_materia(1);
CALL obtener_carreras_con_materias_docente('laura@mail.com');
CALL obtener_carreras_del_alumno('carlos@mail.com');
CALL materias_por_carrera('Administración de Empresas');

-- =========================================================
-- 🧑‍🏫 SECCIÓN 5: DOCENTES Y ASIGNACIONES
-- =========================================================
CALL asignar_m_a_p(3, 1); -- Laura enseña Programación I
CALL asignar_m_a_p(3, 2); -- Laura enseña Bases de Datos
CALL obtener_materias_docente_por_id(3);
CALL obtener_materias_docente('laura@mail.com');
CALL obtener_materia_docente('laura@mail.com');
CALL eliminar_asignacion_docente_materia(3, 'Contabilidad');
CALL existe_asignacion(3, 1);
CALL obtener_id_docente_por_user('laura@mail.com');
CALL get_docente_materia_id('laura@mail.com', 'Bases de Datos', 1);
CALL obtener_carrera_id_por_docente_materia('laura@mail.com', 'Programación I');

-- =========================================================
-- 🎓 SECCIÓN 6: ALUMNOS, MATRÍCULAS Y NOTAS
-- =========================================================
CALL matricular_alumno_en_carrera(1, 'Ingeniería de Sistemas');
CALL matricular_alumno_individual(1, 1, @resultado);
SELECT @resultado AS matricula_individual;
CALL verificar_matricula_existente(1, 1, @existe);
SELECT @existe AS matricula_existente;
CALL retirar_alumno_individual(1, 1, @res_retiro);
SELECT @res_retiro AS retiro_individual;
CALL retirar_alumno_carrera(1, 'Ingeniería de Sistemas', @cant_retiros);
SELECT @cant_retiros AS materias_retiradas;

CALL actualizar_nota(1, 1, 1, 4.5);
CALL actualizar_nota(1, 2, 1, 4.0);
CALL actualizar_nota(1, 3, 1, 4.8);
SELECT * FROM Alumno_Materias WHERE alumno_id = 1;

CALL listar_estudiantes_por_docente_materia('laura@mail.com', 'Bases de Datos', 1);
CALL obtener_id_alumno_por_user('carlos@mail.com');
CALL obtener_id_alumno_por_nombre_apellido('Carlos', 'Ramírez', @id_alumno);
SELECT @id_alumno AS id_por_nombre_apellido;
CALL obtener_id_alumno_por_nombre_completo('Carlos Ramírez', @id_completo);
SELECT @id_completo AS id_por_nombre_completo;

-- =========================================================
-- 🧾 SECCIÓN 7: REPORTES Y CONTROL
-- =========================================================
CALL insertar_reporte(1, 'Buen desempeño en clase', 3);
CALL obtener_reportes_con_docente(1);
CALL obtener_reportes_alumno_completos('carlos@mail.com');

-- =========================================================
-- 🧩 SECCIÓN 8: BÚSQUEDAS, FILTROS Y FUNCIONES
-- =========================================================
CALL buscar_docentes_por_nombre('Laura');
CALL buscar_alumnos_por_nombre_carrera('Ingeniería de Sistemas', 'Carlos');
CALL buscar_usuario(123456789);
CALL obtener_cargo_usuario('carlos@mail.com', '12345');
CALL obtener_sexo_usuario('carlos@mail.com');
CALL alumno_existente(1);
CALL obtener_cedula(1);

-- =========================================================
-- 🧮 SECCIÓN 9: FUNCIONES Y VERIFICACIONES
-- =========================================================
SELECT existe_asignacion(3, 1) AS AsignacionExiste;
SELECT existe_asignacion(3, 99) AS AsignacionNoExiste;

-- =========================================================
-- ✅ SECCIÓN 10: TRIGGERS (Verificación indirecta)
-- =========================================================
-- Estos se activan automáticamente al insertar nuevos usuarios
INSERT INTO Usuarios(nombre, apellido, edad, cc, sexo, cargo, telefono, correo, direccion)
VALUES ('Sofía', 'Jiménez', 24, 777555333, 'femenino', 'alumno', '3012223344', 'sofia@mail.com', 'Calle 100 #30-40');

SELECT user_, contrasena_hash FROM Usuarios WHERE nombre = 'Sofía';
SELECT * FROM Alumnos WHERE id = (SELECT id FROM Usuarios WHERE nombre = 'Sofía');

-- =========================================================
-- 🏁 FIN DE PRUEBAS
-- =========================================================
