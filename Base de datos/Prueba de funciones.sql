USE CBN;

-- =====================================================
-- 🔹 SECCIÓN 1: VALIDACIÓN DEL TRIGGER
-- =====================================================

-- Inserta un nuevo usuario sin user_ ni contraseña (el trigger los genera)
INSERT INTO Usuarios (nombre, apellido, edad, cc, sexo, cargo)
VALUES ('Trigger', 'Test', 22, 99998, 'Masculino', 'alumno');

SELECT id, nombre, user_, contrasena_hash
FROM Usuarios
WHERE nombre = 'Trigger';

-- =====================================================
-- 🔹 SECCIÓN 2: USUARIOS Y ROLES
-- =====================================================

-- 2.1 Registrar nuevo personal con rol de docente
CALL registrar_personal(
    'Carlos', 'Eduardo', 'Morales', 'Rojas', 45, '3105000001', 'carlosm@cbn.edu',
    'Calle 90 #10-20', '88888', 'Masculino', 'docente', 'Ingeniería de Sistemas'
);

-- 2.2 Actualizar usuario con cambio de rol
CALL actualizar_usuario_con_rol(
    (SELECT id FROM Usuarios WHERE nombre='Carlos' AND apellido='Vega'),
    '40003', 'Carlos', NULL, 'Vega', NULL, 22, '3004000003',
    'carlosv@cbn.edu', 'Nueva Dir #123', 'docente'
);

-- 2.3 Consultar roles disponibles
CALL obtener_roles_usuarios();

-- 2.4 Buscar usuario por cédula
CALL buscar_usuario(40005);

-- 2.5 Listar usuarios por rol
CALL listar_usuarios_por_rol('administrador');

-- =====================================================
-- 🔹 SECCIÓN 3: CARRERAS Y MATERIAS
-- =====================================================

-- 3.1 Crear una nueva materia
CALL crear_materia('Programación II', 1);

-- 3.2 Crear una carrera con materias
CALL crear_carrera_con_materias('Ingeniería Industrial', 'Física,Química,Matemáticas');

-- 3.3 Listar carreras existentes
CALL listar_carreras();

-- 3.4 Obtener ID de carrera por nombre
CALL id_carrera_por_nombre('Ingeniería de Sistemas');

-- 3.5 Listar materias de una carrera
CALL listar_nombres_materias_por_carrera_nombres('Contabilidad');

-- 3.6 Listar materias con docente asignado
CALL listar_materias_por_carrera_con_docente('Ingeniería de Sistemas');

-- =====================================================
-- 🔹 SECCIÓN 4: DOCENTES Y ASIGNACIONES
-- =====================================================

-- 4.1 Listar docentes
CALL listar_docentes();

-- 4.2 Buscar docentes por nombre
CALL buscar_docentes_por_nombre('Laura');

-- 4.3 Asignar materia a docente
CALL asignar_m_a_p(
    (SELECT id FROM Usuarios WHERE nombre='Laura' and cargo="docente"),
    (SELECT id FROM Materias WHERE nombre='Redes I')
);

-- 4.4 Verificar si la asignación existe (usa función)
SELECT existe_asignacion(
    (SELECT id FROM Usuarios WHERE nombre='Laura'and cargo="docente"),
    (SELECT id FROM Materias WHERE nombre='Redes I')
) AS existe;

-- 4.5 Reemplazar docente en una materia
CALL reemplazar_docente_en_materia(
    (SELECT id FROM Docente_Materias WHERE materia_id=1 LIMIT 1),
    (SELECT id FROM Usuarios WHERE nombre='Pedro')
);

-- 4.6 Eliminar asignación docente-materia
CALL eliminar_asignacion_docente_materia(
    (SELECT id FROM Usuarios WHERE nombre='Pedro'and cargo="docente"),
    'Economía'
);

-- =====================================================
-- 🔹 SECCIÓN 5: MATRÍCULA Y NOTAS
-- =====================================================

-- 5.1 Matricular alumno automáticamente en carrera
CALL matricular_alumno_en_carrera(
    (SELECT id FROM Usuarios WHERE nombre='Beatriz' and cargo="alumno"),
    'Ingeniería de Sistemas'
);

-- 5.2 Matricular alumno individualmente en materia
CALL matricular_alumno_individual(
    (SELECT id FROM Usuarios WHERE nombre='Carlos'and cargo="alumno"),
    (SELECT id FROM Docente_Materias LIMIT 1),
    @resultado
);
SELECT @resultado AS matricula_resultado;

-- 5.3 Retirar alumno individualmente
CALL retirar_alumno_individual(
    (SELECT id FROM Usuarios WHERE nombre='Carlos'and cargo="docente"),
    (SELECT id FROM Docente_Materias LIMIT 1),
    @retirado
);
SELECT @retirado AS retiro_resultado;

-- 5.4 Retirar alumno de toda una carrera
CALL retirar_alumno_carrera(
    (SELECT id FROM Usuarios WHERE nombre='Fernanda'),
    'Administración de Empresas',
    @cantidad
);
SELECT @cantidad AS cantidad_retirada;

-- 5.5 Actualizar nota de alumno
CALL actualizar_nota(
    (SELECT id FROM Usuarios WHERE nombre='Andrés'),
    1,
    (SELECT id FROM Docente_Materias WHERE materia_id=1 LIMIT 1),
    4.8
);

-- 5.6 Listar notas por alumno y carrera
CALL listar_notas_por_alumno_carrera(
    (SELECT id FROM Usuarios WHERE nombre='Andrés'),
    1
);

-- =====================================================
-- 🔹 SECCIÓN 6: REPORTES
-- =====================================================

-- 6.1 Insertar reporte académico
CALL insertar_reporte(
    (SELECT id FROM Usuarios WHERE nombre='Andrés'and cargo="alumno"),
    'Excelente participación y rendimiento.',
    (SELECT id FROM Usuarios WHERE nombre='Laura'and cargo="docente"),
    (SELECT id FROM Docente_Materias WHERE materia_id=1 LIMIT 1)
);

-- 6.2 Consultar reportes de un alumno
CALL obtener_reportes_con_docente((SELECT id FROM Usuarios WHERE nombre='Andrés'));

-- 6.3 Ver reportes detallados por docente
CALL Reportes_para_Docente((SELECT id FROM Docente_Materias WHERE materia_id=1 LIMIT 1));

-- =====================================================
-- 🔹 SECCIÓN 7: CONSULTAS Y LISTADOS
-- =====================================================

-- 7.1 Listar alumnos por carrera
CALL listar_alumnos_por_carrera('Ingeniería de Sistemas');

-- 7.2 Buscar alumnos por nombre dentro de carrera
CALL buscar_alumnos_por_nombre_carrera('Contabilidad', 'Helena');

-- 7.3 Buscar alumnos por materia y carrera de docente
CALL buscar_alumnos_por_materia_carrera_docente(
    'lauras3001', 'Bases de Datos', 1, 'Andrés'
);

-- 7.4 Obtener materias que enseña un docente
CALL obtener_materias_docente('lauras3001');

-- 7.5 Obtener carreras en las que enseña un docente
CALL obtener_carreras_con_materias_docente('lauras3001');

-- 7.6 Obtener materias de docente con nombre de carrera
CALL obtener_materias_docente_con_carrera((SELECT id FROM Usuarios WHERE nombre='Laura'and cargo="docente"));

-- 7.7 Obtener materias de alumno con docente y carrera
CALL obtener_materia_alumno_en_carrera(
    (SELECT id FROM Usuarios WHERE nombre='Andrés'),
    'lauras3001',
    1
);

-- =====================================================
-- 🔹 SECCIÓN 8: VALIDACIÓN GENERAL
-- =====================================================

-- 8.1 Verificar alumnos creados
SELECT COUNT(*) AS total_alumnos FROM Alumnos;

-- 8.2 Verificar docentes
SELECT COUNT(*) AS total_docentes FROM Docentes;

-- 8.3 Verificar asignaciones
SELECT COUNT(*) AS total_asignaciones FROM Docente_Materias;

-- 8.4 Verificar matrículas
SELECT COUNT(*) AS total_matriculas FROM Alumno_Materias;

-- 8.5 Mostrar algunos usuarios finales
SELECT id, nombre, cargo, user_ FROM Usuarios ORDER BY id LIMIT 10;

-- FIN DEL TEST
