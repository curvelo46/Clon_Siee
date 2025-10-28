	DELIMITER //
	
CREATE PROCEDURE actualizar_estudiante(
    IN p_id INT,
    IN p_cc VARCHAR(20),
    IN p_nombre VARCHAR(125),
    IN p_segundo_nombre VARCHAR(125),
    IN p_apellido VARCHAR(125),
    IN p_segundo_apellido VARCHAR(125),
    IN p_edad INT,
    IN p_telefono VARCHAR(50),
    IN p_correo VARCHAR(125),
    IN p_direccion VARCHAR(150)
)
BEGIN
    UPDATE Usuarios
    SET
        cc = CAST(p_cc AS UNSIGNED),
        nombre = p_nombre,
        segundo_nombre = p_segundo_nombre,
        apellido = p_apellido,
        segundo_apellido = p_segundo_apellido,
        edad = p_edad,
        telefono = p_telefono,
        correo = p_correo,
        direccion = p_direccion
    WHERE id = p_id AND cargo = 'docente';
END //

CREATE PROCEDURE actualizar_docente(
    IN p_id INT,
    IN p_cc VARCHAR(20),
    IN p_nombre VARCHAR(125),
    IN p_segundo_nombre VARCHAR(125),
    IN p_apellido VARCHAR(125),
    IN p_segundo_apellido VARCHAR(125),
    IN p_edad INT,
    IN p_telefono VARCHAR(50),
    IN p_correo VARCHAR(125),
    IN p_direccion VARCHAR(150)
)
BEGIN
    UPDATE Usuarios
    SET
        cc = CAST(p_cc AS UNSIGNED),
        nombre = p_nombre,
        segundo_nombre = p_segundo_nombre,
        apellido = p_apellido,
        segundo_apellido = p_segundo_apellido,
        edad = p_edad,
        telefono = p_telefono,
        correo = p_correo,
        direccion = p_direccion
    WHERE id = p_id AND cargo = 'docente';
END //


CREATE PROCEDURE actualizar_nota_alumno(
    IN alumno_id INT,
    IN materia_id INT,
    IN corte INT,
    IN nota DECIMAL(4,2)
)
BEGIN
    DECLARE id_docente_materia INT;

    SELECT id INTO id_docente_materia
    FROM Docente_Materias
    WHERE materia_id = materia_id
    LIMIT 1;

    IF id_docente_materia IS NOT NULL THEN
        CASE corte
            WHEN 1 THEN
                UPDATE Alumno_Materias 
                SET corte1 = nota
                WHERE alumno_id = alumno_id 
                AND docente_materia_id = id_docente_materia;
            WHEN 2 THEN
                UPDATE Alumno_Materias 
                SET corte2 = nota
                WHERE alumno_id = alumno_id 
                AND docente_materia_id = id_docente_materia;
            WHEN 3 THEN
                UPDATE Alumno_Materias 
                SET corte3 = nota
                WHERE alumno_id = alumno_id 
                AND docente_materia_id = id_docente_materia;
        END CASE;
    END IF;
END //





	CREATE PROCEDURE listar_alumnos()
BEGIN
    SELECT 
        u.id,
        u.cc,
        u.nombre,
        u.segundo_nombre,
        u.apellido,
        u.segundo_apellido,
        u.edad,
        u.telefono,
        u.correo,
        u.direccion
    FROM Alumnos a
    JOIN Usuarios u ON a.id = u.id;
END //

CREATE PROCEDURE listar_docentes()
BEGIN
    SELECT 
        u.id,
        u.cc,
        u.nombre,
        u.segundo_nombre,
        u.apellido,
        u.segundo_apellido,
        u.edad,
        u.telefono,
        u.correo,
        u.direccion
    FROM docentes a
    JOIN Usuarios u ON a.id = u.id;
END //

	CREATE PROCEDURE obtener_cargo_usuario(
		IN p_usuario VARCHAR(50),
		IN p_contrasena VARCHAR(255)
	)
	BEGIN
		SELECT cargo 
		FROM Usuarios 
		WHERE user_ = p_usuario AND contrasena_hash = p_contrasena;
	END //

	CREATE PROCEDURE obtener_sexo_alumno(
		IN p_nombre_usuario VARCHAR(50)
	)
	BEGIN
		SELECT sexo 
		FROM Usuarios 
		WHERE user_ = p_nombre_usuario;
	END //


	CREATE PROCEDURE obtener_id_alumno_por_user(
		IN p_user VARCHAR(50)
	)
	BEGIN
		SELECT u.id
		FROM Usuarios u
		JOIN Alumnos a ON u.id = a.id
		WHERE u.user_ = p_user;
	END //

	CREATE PROCEDURE listar_notas_por_alumno(
		IN p_alumno_id INT
	)
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



CREATE PROCEDURE obtener_materia_docente(IN nombre_docente VARCHAR(100))
BEGIN
    SELECT m.nombre AS materia
    FROM Usuarios u
    JOIN Docentes d ON d.id = u.id
    JOIN Docente_Materias dm ON dm.docente_id = d.id
    JOIN Materias m ON m.id = dm.materia_id
    WHERE u.user_ = nombre_docente;
END //



CREATE PROCEDURE listar_estudiantes_por_docente_materia(
    IN p_docente_user VARCHAR(100),
    IN p_materia_nombre VARCHAR(100)
)
BEGIN
    SELECT 
        u.cc,
        u.nombre,
        u.apellido,
        u.edad,
        u.telefono,
        u.correo
    FROM Alumno_Materias am
    JOIN Alumnos a ON am.alumno_id = a.id
    JOIN Usuarios u ON a.id = u.id
    JOIN Docente_Materias dm ON am.docente_materia_id = dm.id
    JOIN Materias m ON dm.materia_id = m.id
    JOIN Docentes d ON dm.docente_id = d.id
    JOIN Usuarios ud ON d.id = ud.id
    WHERE m.nombre = p_materia_nombre
      AND ud.user_ = p_docente_user;
END //

    
CREATE PROCEDURE listar_notas_docente_materia(
    IN p_docente_user VARCHAR(100),
    IN p_materia_nombre VARCHAR(100)
)
BEGIN
    SELECT 
        a.id AS alumno_id,
        u.nombre AS estudiante,
        u.apellido,
        am.corte1,
        am.corte2,
        am.corte3
    FROM Alumno_Materias am
    JOIN Alumnos a ON am.alumno_id = a.id
    JOIN Usuarios u ON a.id = u.id
    JOIN Docente_Materias dm ON am.docente_materia_id = dm.id
    JOIN Materias m ON dm.materia_id = m.id
    JOIN Docentes d ON dm.docente_id = d.id
    JOIN Usuarios ud ON d.id = ud.id
    WHERE m.nombre = p_materia_nombre
      AND ud.user_ = p_docente_user;
END //


	CREATE PROCEDURE listado_docentes()
	BEGIN
		SELECT u.id, u.nombre, u.apellido
		FROM Usuarios u
		JOIN Docentes d ON u.id = d.id;
	END //

	CREATE PROCEDURE Materias_disponibles()
	BEGIN
		SELECT m.id, m.nombre AS nombre_materia
		FROM Materias m;
	END //


	CREATE PROCEDURE id_materia(
		IN p_nombre_materia VARCHAR(125)
	)
	BEGIN
		SELECT id
		FROM Materias
		WHERE nombre = p_nombre_materia;
	END //




CREATE FUNCTION existe_asignacion(docenteId INT, materiaId INT)
RETURNS TINYINT
DETERMINISTIC
BEGIN
    DECLARE totalAsignaciones INT;

    SELECT COUNT(*) INTO totalAsignaciones
    FROM Docente_Materias
    WHERE docente_id = docenteId
      AND materia_id = materiaId;

    IF totalAsignaciones > 0 THEN
        RETURN 1;  -- Ya existe
    ELSE
        RETURN 0;  -- No existe
    END IF;
END //










CREATE PROCEDURE asignar_m_a_p(
    IN p_docente_id INT,
    IN p_materia_id INT
)
BEGIN
    DECLARE ya_asignada INT;

    -- Verificar si ya existe la asignación
    SELECT COUNT(*) INTO ya_asignada
    FROM Docente_Materias
    WHERE docente_id = p_docente_id
      AND materia_id = p_materia_id;

    IF ya_asignada > 0 THEN
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'El docente ya tiene asignada esta materia';
    ELSE
        -- Insertar la asignación
        INSERT INTO Docente_Materias (docente_id, materia_id)
        VALUES (p_docente_id, p_materia_id);
    END IF;
END //






CREATE PROCEDURE listar_promedios_por_docente_materia(
    IN p_docente_user VARCHAR(100),
    IN p_materia_nombre VARCHAR(100)
)
BEGIN
    SELECT 
        u.nombre,
        u.apellido,
        am.corte1,
        am.corte2,
        am.corte3
    FROM Alumno_Materias am
    JOIN Alumnos a ON a.id = am.alumno_id
    JOIN Usuarios u ON u.id = a.id
    JOIN Docente_Materias dm ON dm.id = am.docente_materia_id
    JOIN Materias m ON m.id = dm.materia_id
    JOIN Docentes d ON d.id = dm.docente_id
    JOIN Usuarios ud ON ud.id = d.id
    WHERE ud.user_ = p_docente_user
      AND m.nombre = p_materia_nombre;
END //


CREATE PROCEDURE registrar_usuario(
    IN p_nombre VARCHAR(50),
    IN p_segundoNombre VARCHAR(50),
    IN p_apellido VARCHAR(50),
    IN p_segundoApellido VARCHAR(50),
    IN p_edad INT,
    IN p_telefono VARCHAR(15),
    IN p_correo VARCHAR (50),
    IN p_direccion VARCHAR(100),
    IN p_cedula VARCHAR(20),
    IN p_genero VARCHAR(10),
    IN p_cargo VARCHAR(20),
    IN p_carrera VARCHAR(50)
)
BEGIN
    INSERT INTO usuarios
    (nombre, segundo_Nombre, apellido, segundo_Apellido, edad, telefono, correo, direccion, cc, sexo, cargo, carrera)
    VALUES
    (p_nombre, p_segundoNombre, p_apellido, p_segundoApellido, p_edad, p_telefono, p_correo, p_direccion, p_cedula, p_genero, p_cargo, p_carrera);
END//





	





    
   CREATE PROCEDURE reiniciar_notas()
BEGIN
    UPDATE Alumno_Materias
    SET corte1 = 0, corte2 = 0, corte3 = 0;
END // 
    CREATE PROCEDURE crear_materia(
		IN nombre VARCHAR(100)
	
	)
    begin
			INSERT INTO Materias (nombre) VALUES (nombre);
            
	end//
