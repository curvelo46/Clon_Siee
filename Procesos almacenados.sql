DELIMITER //

CREATE PROCEDURE listar_alumnos()
BEGIN
    SELECT u.id, u.nombre, u.apellido, u.cc
    FROM Alumnos a
    JOIN Usuarios u ON a.id = u.id;
END //

CREATE PROCEDURE obtener_id_alumno_por_user(
    IN p_user VARCHAR(50)
)
BEGIN
    SELECT a.id
    FROM Alumnos a
    INNER JOIN Usuarios u ON a.id = u.id
    WHERE u.user_ = p_user;
END//

CREATE PROCEDURE listar_promedios_por_docente_materia(IN p_docente VARCHAR(100), IN p_materia VARCHAR(100))
BEGIN
    SELECT 
        u.nombre,
        u.apellido,
        am.corte1,
        am.corte2,
        am.corte3,
        ROUND((am.corte1 + am.corte2 + am.corte3) / 3, 2) AS promedio
    FROM Docente_Materias dm
    JOIN Materias m ON dm.materia_id = m.id
    JOIN Alumno_Materias am ON m.id = am.materia_id
    JOIN Alumnos a ON am.alumno_id = a.id
    JOIN Usuarios u ON a.id = u.id
    WHERE dm.docente_id = (SELECT d.id FROM Docentes d JOIN Usuarios ud ON d.id = ud.id WHERE ud.user_ = p_docente LIMIT 1)
      AND m.nombre = p_materia;
END //

CREATE PROCEDURE obtener_materia_docente(IN p_docente_nombre VARCHAR(100))
BEGIN
    SELECT m.nombre
    FROM Materias m
    JOIN Docente_Materias dm ON m.id = dm.materia_id
    JOIN Docentes d ON dm.docente_id = d.id
    JOIN Usuarios u ON d.id = u.id
    WHERE u.user_ = p_docente_nombre;
END //

CREATE PROCEDURE listar_notas_docente_materia(IN p_docente VARCHAR(100), IN p_materia VARCHAR(100))
BEGIN
    SELECT 
        a.id AS alumno_id, -- NUEVO
        u.nombre AS estudiante,
        u.apellido,
        am.corte1,
        am.corte2,
        am.corte3
    FROM Alumno_Materias am
    JOIN Materias m ON am.materia_id = m.id
    JOIN Alumnos a ON am.alumno_id = a.id
    JOIN Usuarios u ON a.id = u.id
    WHERE m.nombre = p_materia
      AND m.id IN (
        SELECT materia_id
        FROM Docente_Materias dm
        JOIN Docentes d ON dm.docente_id = d.id
        JOIN Usuarios ud ON d.id = ud.id
        WHERE ud.user_ = p_docente
      );
END //


CREATE PROCEDURE actualizar_nota_alumno(
    IN p_alumno_id INT,
    IN p_materia_id INT,
    IN p_corte INT,
    IN p_nota DECIMAL(5,2)
)
BEGIN
    IF p_corte = 1 THEN
        UPDATE Alumno_Materias SET corte1 = p_nota
        WHERE alumno_id = p_alumno_id AND materia_id = p_materia_id;
    ELSEIF p_corte = 2 THEN
        UPDATE Alumno_Materias SET corte2 = p_nota
        WHERE alumno_id = p_alumno_id AND materia_id = p_materia_id;
    ELSEIF p_corte = 3 THEN
        UPDATE Alumno_Materias SET corte3 = p_nota
        WHERE alumno_id = p_alumno_id AND materia_id = p_materia_id;
    END IF;
END //

CREATE PROCEDURE actualizar_docente(
    IN p_id INT,
    IN p_nombre VARCHAR(125),
    IN p_segundo_nombre VARCHAR(125),
    IN p_apellido VARCHAR(125),
    IN p_segundo_apellido VARCHAR(125),
    IN p_edad INT,
    IN p_telefono VARCHAR(125),
    IN p_correo VARCHAR(100),
    IN p_direccion VARCHAR(150)
)
BEGIN
    UPDATE Usuarios
    SET 
        nombre = p_nombre,
        segundo_nombre = p_segundo_nombre,
        apellido = p_apellido,
        segundo_apellido = p_segundo_apellido,
        edad = p_edad,
        telefono = p_telefono,
        correo = p_correo,
        direccion = p_direccion
    WHERE id = p_id;
END //

CREATE PROCEDURE listar_estudiantes_por_docente_materia(IN p_docente VARCHAR(100), IN p_materia VARCHAR(100))
BEGIN
    SELECT 
        u.cc,
        u.nombre,
        u.apellido,
        u.edad,
        u.telefono,
        u.correo
    FROM Docente_Materias dm
    JOIN Materias m ON dm.materia_id = m.id
    JOIN Alumno_Materias am ON m.id = am.materia_id
    JOIN Alumnos a ON am.alumno_id = a.id
    JOIN Usuarios u ON a.id = u.id
    WHERE m.nombre = p_materia
      AND dm.docente_id = (
          SELECT d.id FROM Docentes d
          JOIN Usuarios ud ON d.id = ud.id
          WHERE ud.user_ = p_docente
          LIMIT 1
      );
END //

CREATE PROCEDURE obtener_cargo_usuario(
    IN p_user VARCHAR(50),
    IN p_contrasena VARCHAR(255)
)
BEGIN
    SELECT cargo
    FROM Usuarios
    WHERE user_ = p_user
      AND contrasena_hash = p_contrasena
    LIMIT 1;
END //

CREATE PROCEDURE listar_notas_por_alumno(IN p_alumno_id INT)
BEGIN
    SELECT 
        m.nombre AS nombre_materia,
        am.corte1,
        am.corte2,
        am.corte3
    FROM Alumno_Materias am
    JOIN Materias m ON am.materia_id = m.id
    WHERE am.alumno_id = p_alumno_id;
END //
