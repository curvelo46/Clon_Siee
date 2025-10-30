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
    WHERE id = p_id AND cargo = 'alumno';
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
    

CREATE PROCEDURE obtener_user_por_cc(IN p_cc VARCHAR(100))
BEGIN
    SELECT user_ FROM Usuarios WHERE cc = p_cc;
END //



	CREATE PROCEDURE obtener_id_por_cc(
		IN cedula VARCHAR(100)
	)
	BEGIN
		SELECT id FROM Usuarios WHERE cc = cedula;
	END //

	
CREATE PROCEDURE obtener_id_alumno_por_user(IN p_user VARCHAR(50))
BEGIN
    SELECT u.id
    FROM Usuarios u
    JOIN Alumnos a ON u.id = a.id
    WHERE u.user_ = p_user;
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
        am.corte3,
        am.corte1_edit,
        am.corte2_edit,
        am.corte3_edit
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
    JOIN Docente_Materias dm ON am.docente_materia_id = dm.id
    JOIN Materias m ON dm.materia_id = m.id
    WHERE am.alumno_id = p_alumno_id;
END //



CREATE PROCEDURE actualizar_nota
(
    IN p_alumno_id        INT,
    IN p_corte            TINYINT,   -- 1, 2 o 3
    IN p_docente_materia_id INT,
    IN p_nota             DECIMAL(5,2)
)
BEGIN
    /* validamos el número de corte */
    IF p_corte NOT IN (1,2,3) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El corte debe ser 1, 2 o 3';
    END IF;

    /* construimos dinámicamente la asignación al campo correcto */
    CASE p_corte
        WHEN 1 THEN
            UPDATE Alumno_Materias
               SET corte1 = p_nota, corte1_edit=1
             WHERE alumno_id = p_alumno_id
               AND docente_materia_id = p_docente_materia_id;
        WHEN 2 THEN
            UPDATE Alumno_Materias
               SET corte2 = p_nota, corte2_edit=1
             WHERE alumno_id = p_alumno_id
               AND docente_materia_id = p_docente_materia_id;
        WHEN 3 THEN
            UPDATE Alumno_Materias
               SET corte3 = p_nota,corte3_edit=1
             WHERE alumno_id = p_alumno_id
               AND docente_materia_id = p_docente_materia_id;
    END CASE;
END//


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
    IN p_cargo VARCHAR(20)
)
BEGIN
	
    INSERT INTO usuarios
    (nombre, segundo_Nombre, apellido, segundo_Apellido, edad, telefono, correo, direccion, cc, sexo, cargo)
    VALUES
    (p_nombre, p_segundoNombre, p_apellido, p_segundoApellido, p_edad, p_telefono, p_correo, p_direccion, p_cedula, p_genero, p_cargo );
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


create procedure insertar_alumno(in ida int)
begin
	INSERT INTO Alumnos (id) VALUES (ida);
end//


CREATE PROCEDURE matricular_alumno_en_carrera(
    IN p_user VARCHAR(50),
    IN p_carrera_nombre VARCHAR(125)
)
BEGIN
    DECLARE v_alumno_id INT;
    DECLARE v_carrera_id INT;
    DECLARE v_docente_materia_id INT;
    DECLARE done INT DEFAULT 0;

    -- Cursor para recorrer todas las materias de la carrera
    DECLARE curMaterias CURSOR FOR
        SELECT dm.id
        FROM Carrera_Materias cm
        JOIN Materias m ON cm.materia_id = m.id
        JOIN Docente_Materias dm ON dm.materia_id = m.id
        WHERE cm.carrera_id = v_carrera_id;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    -- 1️⃣ Obtener el ID del alumno
    SELECT u.id INTO v_alumno_id
    FROM Usuarios u
    WHERE u.user_ = p_user AND u.cargo = 'alumno';

    -- 2️⃣ Obtener el ID de la carrera
    SELECT id INTO v_carrera_id
    FROM Carreras
    WHERE nombre = p_carrera_nombre;

    -- 3️⃣ Verificar que ambos existan
    IF v_alumno_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El usuario no existe o no es un alumno';
    END IF;

    IF v_carrera_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La carrera especificada no existe';
    END IF;

    -- 4️⃣ Recorrer las materias de la carrera y matricular al alumno
    OPEN curMaterias;

    loop_materias: LOOP
        FETCH curMaterias INTO v_docente_materia_id;
        IF done THEN
            LEAVE loop_materias;
        END IF;

        -- Insertar la relación alumno - materia
        INSERT INTO Alumno_Materias (alumno_id, docente_materia_id, corte1, corte2, corte3)
        VALUES (v_alumno_id, v_docente_materia_id, 0, 0, 0);
    END LOOP;

    CLOSE curMaterias;
END //


create procedure carreras()
begin
	SELECT nombre FROM Carreras;

end//


CREATE PROCEDURE registrar_docente_por_cedula(IN p_cc BIGINT)
BEGIN
    DECLARE v_id INT;

    /* 1. Obtener el id del usuario con esa cédula y que sea docente */
    SELECT id
      INTO v_id
      FROM Usuarios
     WHERE cc = p_cc
       AND cargo = 'docente';

    /* 2. Si no existe => error */
    IF v_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'No existe un usuario docente con esa cédula';
    ELSE
        /* 3. Insertar en la tabla Docentes (si ya existe no hará nada por la PK) */
        INSERT IGNORE INTO Docentes (id) VALUES (v_id);
    END IF;
END //





CREATE PROCEDURE get_docente_materia_id(
    IN p_user_docente VARCHAR(100),
    IN p_materia_nombre VARCHAR(100)
)
BEGIN
    SELECT dm.id
    FROM Docente_Materias dm
    JOIN Docentes d ON d.id = dm.docente_id
    JOIN Usuarios u ON u.id = d.id
    JOIN Materias m ON m.id = dm.materia_id
    WHERE u.user_ = p_user_docente
      AND m.nombre = p_materia_nombre
    LIMIT 1;
END//

