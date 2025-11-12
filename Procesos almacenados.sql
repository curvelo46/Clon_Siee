DELIMITER //
	
CREATE PROCEDURE actualizar_estudiante(
    IN p_id INT, IN p_cc VARCHAR(20), IN p_nombre VARCHAR(125),
    IN p_segundo_nombre VARCHAR(125), IN p_apellido VARCHAR(125),
    IN p_segundo_apellido VARCHAR(125), IN p_edad INT,
    IN p_telefono VARCHAR(50), IN p_correo VARCHAR(125), IN p_direccion VARCHAR(150)
) BEGIN
    UPDATE Usuarios SET
        cc = CAST(p_cc AS UNSIGNED), nombre = p_nombre,
        segundo_nombre = p_segundo_nombre, apellido = p_apellido,
        segundo_apellido = p_segundo_apellido, edad = p_edad,
        telefono = p_telefono, correo = p_correo, direccion = p_direccion
    WHERE id = p_id AND cargo = 'alumno';
END //


CREATE PROCEDURE actualizar_docente(
    IN p_id INT, IN p_cc VARCHAR(20), IN p_nombre VARCHAR(125),
    IN p_segundo_nombre VARCHAR(125), IN p_apellido VARCHAR(125),
    IN p_segundo_apellido VARCHAR(125), IN p_edad INT,
    IN p_telefono VARCHAR(50), IN p_correo VARCHAR(125), IN p_direccion VARCHAR(150)
) BEGIN
    UPDATE Usuarios SET
        cc = CAST(p_cc AS UNSIGNED), nombre = p_nombre,
        segundo_nombre = p_segundo_nombre, apellido = p_apellido,
        segundo_apellido = p_segundo_apellido, edad = p_edad,
        telefono = p_telefono, correo = p_correo, direccion = p_direccion
    WHERE id = p_id AND cargo = 'docente';
END //


CREATE PROCEDURE id_materia_por_carrera(
    IN p_nombre_materia VARCHAR(125),
    IN p_carrera_nombre VARCHAR(125)
)
begin
SELECT m.id
FROM Materias m
JOIN Carreras c ON c.id = m.carrera_id
WHERE m.nombre = p_nombre_materia
  AND c.nombre = p_carrera_nombre
LIMIT 1;
end//


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

CREATE PROCEDURE obtener_sexo_usuario(
		IN p_nombre_usuario VARCHAR(50)
	)
BEGIN
		SELECT sexo 
		FROM Usuarios 
		WHERE user_ = p_nombre_usuario;
	END //	




CREATE PROCEDURE Cargos()
BEGIN
    SELECT DISTINCT cargo FROM Usuarios;
END //





	
CREATE PROCEDURE obtener_id_alumno_por_user(IN p_user VARCHAR(50))
BEGIN
    SELECT u.id
    FROM Usuarios u
    JOIN Alumnos a ON u.id = a.id
    WHERE u.user_ = p_user;
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




CREATE PROCEDURE actualizar_usuario_con_rol(
    IN p_id INT, 
    IN p_cc VARCHAR(20), 
    IN p_nombre VARCHAR(125),
    IN p_segundo_nombre VARCHAR(125), 
    IN p_apellido VARCHAR(125),
    IN p_segundo_apellido VARCHAR(125), 
    IN p_edad INT,
    IN p_telefono VARCHAR(50), 
    IN p_correo VARCHAR(125), 
    IN p_direccion VARCHAR(150),
    IN p_cargo VARCHAR(50)
)
BEGIN
    DECLARE v_cargo_actual VARCHAR(50);
    
    -- Obtener cargo actual del usuario
    SELECT cargo INTO v_cargo_actual FROM Usuarios WHERE id = p_id;
    
    -- Actualizar datos básicos y nuevo cargo
    UPDATE Usuarios SET
        cc = CAST(p_cc AS UNSIGNED), 
        nombre = p_nombre,
        segundo_nombre = p_segundo_nombre, 
        apellido = p_apellido,
        segundo_apellido = p_segundo_apellido, 
        edad = p_edad,
        telefono = p_telefono, 
        correo = p_correo, 
        direccion = p_direccion,
        cargo = p_cargo
    WHERE id = p_id;
    
    -- Si cambió el cargo, actualizar tablas de roles
    IF v_cargo_actual != p_cargo THEN
        -- Eliminar de tabla anterior
        CASE v_cargo_actual
            WHEN 'alumno' THEN DELETE FROM Alumnos WHERE id = p_id;
            WHEN 'docente' THEN DELETE FROM Docentes WHERE id = p_id;
            WHEN 'administrador' THEN DELETE FROM Administradores WHERE id = p_id;
            WHEN 'registro y control' THEN DELETE FROM RegistroYControl WHERE id = p_id;
        END CASE;
        
        -- Insertar en nueva tabla de rol
        CASE p_cargo
            WHEN 'alumno' THEN INSERT INTO Alumnos (id) VALUES (p_id);
            WHEN 'docente' THEN INSERT INTO Docentes (id) VALUES (p_id);
            WHEN 'administrador' THEN INSERT INTO Administradores (id) VALUES (p_id);
            WHEN 'registro y control' THEN INSERT INTO RegistroYControl (id) VALUES (p_id);
        END CASE;
    END IF;
END //




CREATE PROCEDURE eliminar_asignacion_docente_materia(
    IN p_docente_id INT,
    IN p_materia_nombre VARCHAR(125)
)
BEGIN
    DECLARE v_docente_materia_id INT;
    DECLARE v_materia_id INT;
    
    -- Obtener IDs
    SELECT dm.id, dm.materia_id INTO v_docente_materia_id, v_materia_id
    FROM Docente_Materias dm
    JOIN Materias m ON m.id = dm.materia_id
    WHERE dm.docente_id = p_docente_id
      AND m.nombre = p_materia_nombre
    LIMIT 1;
    
    IF v_docente_materia_id IS NULL THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'No se encontró la asignación de esta materia al docente';
    END IF;
    
    -- ELIMINAR EN ORDEN CORRECTO (lo más importante primero)
    DELETE FROM Reportes WHERE docente_materia_id = v_docente_materia_id;
    DELETE FROM Alumno_Materias WHERE docente_materia_id = v_docente_materia_id;
    DELETE FROM Docente_Materias WHERE id = v_docente_materia_id;
    
END //





CREATE PROCEDURE buscar_docentes_por_nombre(IN p_nombre VARCHAR(100))
BEGIN
    SELECT u.id, u.nombre, u.apellido
    FROM Usuarios u
    JOIN Docentes d ON d.id = u.id
    WHERE u.nombre LIKE CONCAT('%', p_nombre, '%')
       OR u.apellido LIKE CONCAT('%', p_nombre, '%')
    ORDER BY u.apellido, u.nombre;
END //


CREATE PROCEDURE obtener_materias_docente_por_id(IN p_docente_id INT)
BEGIN
    SELECT m.nombre AS materia
    FROM Docente_Materias dm
    JOIN Materias m ON m.id = dm.materia_id
    WHERE dm.docente_id = p_docente_id;
END //





CREATE PROCEDURE listar_estudiantes_por_docente_materia(
    IN p_docente_user VARCHAR(100),
    IN p_materia_nombre VARCHAR(125),
    IN p_id_carrera INT
)
BEGIN
    SELECT u.cc, u.nombre, u.apellido, u.edad, u.telefono, u.correo, u.id
    FROM Alumno_Materias am
    JOIN Alumnos a ON a.id = am.alumno_id
    JOIN Usuarios u ON u.id = a.id
    JOIN Docente_Materias dm ON dm.id = am.docente_materia_id
    JOIN Materias m ON m.id = dm.materia_id
    JOIN Docentes d ON d.id = dm.docente_id
    JOIN Usuarios ud ON ud.id = d.id
    WHERE ud.user_ = p_docente_user
      AND m.nombre = p_materia_nombre
      AND m.carrera_id = p_id_carrera;
END;
 
 
 
 

CREATE PROCEDURE actualizar_nota(
    IN p_alumno_id INT,
    IN p_corte TINYINT,
    IN p_docente_materia_id INT,
    IN p_nota DECIMAL(5,2))
BEGIN
    CASE p_corte
        WHEN 1 THEN UPDATE Alumno_Materias SET corte1=p_nota, corte1_edit=1
             WHERE alumno_id=p_alumno_id AND docente_materia_id=p_docente_materia_id;
        WHEN 2 THEN UPDATE Alumno_Materias SET corte2=p_nota, corte2_edit=1
             WHERE alumno_id=p_alumno_id AND docente_materia_id=p_docente_materia_id;
        WHEN 3 THEN UPDATE Alumno_Materias SET corte3=p_nota, corte3_edit=1
             WHERE alumno_id=p_alumno_id AND docente_materia_id=p_docente_materia_id;
    END CASE;
END//


CREATE PROCEDURE listado_docentes()
BEGIN
		SELECT u.id, u.nombre, u.apellido
		FROM Usuarios u
		JOIN Docentes d ON u.id = d.id;
	END //




CREATE PROCEDURE id_materia(IN p_nombre VARCHAR(125), IN p_carrera_id INT)
BEGIN
    SELECT id FROM Materias WHERE nombre = p_nombre AND carrera_id = p_carrera_id LIMIT 1;
END//




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
    DECLARE ya_asignada TINYINT;

    -- Verificar si ya existe la asignación usando la función
    SET ya_asignada = existe_asignacion(p_docente_id, p_materia_id);

    IF ya_asignada = 1 THEN
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'El docente ya tiene asignada esta materia';
    ELSE
        -- Insertar la asignación
        INSERT IGNORE INTO Docente_Materias (docente_id, materia_id)
        VALUES (p_docente_id, p_materia_id);
    END IF;
END //







CREATE PROCEDURE registrar_personal(
    IN p_nombre VARCHAR(50),
    IN p_segundoNombre VARCHAR(50),
    IN p_apellido VARCHAR(50),
    IN p_segundoApellido VARCHAR(50),
    IN p_edad INT,
    IN p_telefono VARCHAR(15),
    IN p_correo VARCHAR(50),
    IN p_direccion VARCHAR(100),
    IN p_cedula VARCHAR(20),
    IN p_genero VARCHAR(10),
    IN p_cargo VARCHAR(20),
    IN p_carrera_nombre VARCHAR(125)
)
BEGIN
    DECLARE v_usuario_id INT;

    -- Validar duplicados
    IF EXISTS (SELECT 1 FROM Usuarios WHERE cc = p_cedula) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: La cédula ya está registrada';
    END IF;

    IF p_correo IS NOT NULL AND p_correo != '' THEN
        IF EXISTS (SELECT 1 FROM Usuarios WHERE correo = p_correo) THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'Error: El correo ya está registrado';
        END IF;
    END IF;

    -- Insertar usuario (sin credenciales, el trigger las genera)
    INSERT INTO usuarios (
        nombre, segundo_Nombre, apellido, segundo_Apellido, 
        edad, telefono, correo, direccion, cc, sexo, cargo        
    ) VALUES (
        p_nombre, NULLIF(p_segundoNombre, ''), p_apellido, NULLIF(p_segundoApellido, ''),
        p_edad, NULLIF(p_telefono, ''), NULLIF(p_correo, ''), NULLIF(p_direccion, ''),
        p_cedula, p_genero, p_cargo 
    );

    SET v_usuario_id = LAST_INSERT_ID();

    -- Insertar en tabla de rol
    IF p_cargo = 'alumno' THEN
        INSERT INTO Alumnos (id) VALUES (v_usuario_id);
        
        -- Matricular en carrera si se seleccionó
        IF p_carrera_nombre IS NOT NULL AND p_carrera_nombre != '' THEN
            CALL matricular_alumno_en_carrera(v_usuario_id, p_carrera_nombre);
        END IF;
        
    ELSEIF p_cargo = 'docente' THEN
        INSERT INTO Docentes (id) VALUES (v_usuario_id);
        
    ELSEIF p_cargo = 'administrador' THEN
        INSERT INTO Administradores (id) VALUES (v_usuario_id);
        
    ELSEIF p_cargo = 'registro y control' THEN
        INSERT INTO RegistroYControl (id) VALUES (v_usuario_id);
    END IF;

END //




-- Procedimiento de matrícula corregido
CREATE PROCEDURE matricular_alumno_en_carrera(
    IN p_alumno_id INT,
    IN p_carrera_nombre VARCHAR(125)
)
BEGIN
    DECLARE v_carrera_id INT;
    DECLARE v_docente_materia_id INT;
    DECLARE done INT DEFAULT 0;

    -- Cursor para materias de la carrera con docentes asignados
    DECLARE curMaterias CURSOR FOR
        SELECT dm.id
        FROM Materias m
        JOIN Docente_Materias dm ON dm.materia_id = m.id
        WHERE m.carrera_id = v_carrera_id;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    -- Obtener ID de carrera
    SELECT id INTO v_carrera_id
    FROM Carreras
    WHERE nombre = p_carrera_nombre;

    IF v_carrera_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La carrera especificada no existe';
    END IF;

    OPEN curMaterias;

    loop_materias: LOOP
        FETCH curMaterias INTO v_docente_materia_id;
        IF done THEN LEAVE loop_materias; END IF;

        INSERT INTO Alumno_Materias (alumno_id, docente_materia_id, corte1, corte2, corte3)
        VALUES (p_alumno_id, v_docente_materia_id, 0, 0, 0);
    END LOOP;

    CLOSE curMaterias;
END //









CREATE PROCEDURE crear_materia(IN p_nombre VARCHAR(125), IN p_carrera_id INT)
begin
	INSERT INTO Materias (nombre, carrera_id) VALUES (p_nombre, p_carrera_id);
end//







create procedure carreras()
begin
	SELECT nombre FROM Carreras;

end//



CREATE PROCEDURE get_docente_materia_id(
    IN p_user_docente VARCHAR(100),
    IN p_materia_nombre VARCHAR(125),
    IN p_carrera_id   INT)
BEGIN
    SELECT dm.id
    FROM   Docente_Materias dm
    JOIN   Docentes  d ON d.id = dm.docente_id
    JOIN   Usuarios  u ON u.id = d.id
    JOIN   Materias  m ON m.id = dm.materia_id
    WHERE  u.user_      = p_user_docente
      AND  m.nombre     = p_materia_nombre
      AND  m.carrera_id = p_carrera_id
    LIMIT 1;
END//






CREATE PROCEDURE obtener_cedula(IN p_id INT)
begin
	SELECT cc FROM Usuarios WHERE id = p_id;
end//



CREATE PROCEDURE listar_nombres_materias_por_carrera_nombres(IN carrera_nombre VARCHAR(125))
BEGIN
    SELECT DISTINCT m.nombre AS materia_nombre
    FROM Materias m
    JOIN Carreras c ON c.id = m.carrera_id
    WHERE c.nombre = carrera_nombre
    ORDER BY m.nombre;
END //

CREATE PROCEDURE listar_materias_por_carrera_con_docente(
    IN p_carrera_nombre VARCHAR(125)
)
BEGIN
    SELECT 
        dm.id AS docente_materia_id,
        m.nombre AS materia_nombre,
        CONCAT(u.nombre, ' ', u.apellido) AS docente_nombre
    FROM Materias m
    JOIN Carreras c ON c.id = m.carrera_id
    LEFT JOIN Docente_Materias dm ON dm.materia_id = m.id
    LEFT JOIN Docentes d ON d.id = dm.docente_id
    LEFT JOIN Usuarios u ON u.id = d.id
    WHERE c.nombre = p_carrera_nombre
    ORDER BY m.nombre, u.apellido, u.nombre;
END //





CREATE PROCEDURE obtener_carrera_id_por_docente_materia(
    IN p_user_docente VARCHAR(100),
    IN p_materia_nombre VARCHAR(125)
)
BEGIN
    SELECT m.carrera_id
    FROM Materias m
    JOIN Docente_Materias dm ON dm.materia_id = m.id
    JOIN Docentes d ON d.id = dm.docente_id
    JOIN Usuarios u ON u.id = d.id
    WHERE u.user_ = p_user_docente
      AND m.nombre = p_materia_nombre
    LIMIT 1;
END //



CREATE PROCEDURE obtener_materias_docente(IN p_user_docente VARCHAR(100))
begin
	SELECT DISTINCT m.nombre
	FROM Docente_Materias dm
	JOIN Docentes d ON d.id = dm.docente_id
	JOIN Usuarios u ON u.id = d.id
	JOIN Materias m ON m.id = dm.materia_id
	WHERE u.user_ = p_user_docente
	ORDER BY m.nombre;
END //



CREATE PROCEDURE listar_notas_docente_materia(
    IN p_docente_user VARCHAR(100),
    IN p_materia_nombre VARCHAR(125),
    IN p_id_carrera INT
)
BEGIN
    SELECT
        CONCAT(u.nombre, ' ', u.apellido) AS estudiante,
        am.corte1,
        am.corte2,
        am.corte3,
        am.alumno_id,
        am.corte1_edit,
        am.corte2_edit,
        am.corte3_edit
    FROM Alumno_Materias am
    JOIN Alumnos a ON a.id = am.alumno_id
    JOIN Usuarios u ON u.id = a.id
    JOIN Docente_Materias dm ON dm.id = am.docente_materia_id
    JOIN Materias m ON m.id = dm.materia_id
    JOIN Docentes d ON d.id = dm.docente_id
    JOIN Usuarios ud ON ud.id = d.id
    WHERE ud.user_ = p_docente_user
      AND m.nombre = p_materia_nombre
      AND (p_id_carrera IS NULL OR m.carrera_id = p_id_carrera);
END //


CREATE PROCEDURE crear_carrera_con_materias(
    IN p_nombre_carrera VARCHAR(125),
    IN p_materias TEXT)   -- separadas por coma
BEGIN
    DECLARE v_carrera_id INT;
    DECLARE v_nombre VARCHAR(125);
    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
        SELECT DISTINCT nombre
        FROM Materias
        WHERE FIND_IN_SET(nombre, p_materias);
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    INSERT INTO Carreras (nombre) VALUES (p_nombre_carrera);
    SET v_carrera_id = LAST_INSERT_ID();

    OPEN cur;
    loop_materias: LOOP
        FETCH cur INTO v_nombre;
        IF done THEN LEAVE loop_materias; END IF;
        INSERT INTO Materias (nombre, carrera_id) VALUES (v_nombre, v_carrera_id);
    END LOOP;
    CLOSE cur;
END//

-- 12. Obtener ID de alumno por nombre y apellido

CREATE PROCEDURE obtener_id_alumno_por_nombre_apellido(
    IN p_nombre VARCHAR(125), 
    IN p_apellido VARCHAR(125),
    OUT p_id INT
)
BEGIN
    SELECT u.id INTO p_id
    FROM Usuarios u
    JOIN Alumnos a ON a.id = u.id
    WHERE u.nombre = p_nombre AND u.apellido = p_apellido
    LIMIT 1;
END //



-- Reportear a un alumno
CREATE PROCEDURE insertar_reporte(
    IN p_id_alumno INT, 
    IN p_reporte TEXT, 
    IN p_id_docente INT,
    IN p_docente_materia_id INT  
)
BEGIN
    INSERT INTO Reportes (id_alumno, id_docente, reporte, fecha, docente_materia_id)
    VALUES (p_id_alumno, p_id_docente, p_reporte, NOW(), p_docente_materia_id);
END //



CREATE PROCEDURE obtener_materias_docente_por_carrera(
    IN p_user_docente VARCHAR(100), IN p_id_carrera INT
) BEGIN
    SELECT DISTINCT m.nombre AS materia
    FROM Materias m
    JOIN Docente_Materias dm ON dm.materia_id = m.id
    JOIN Docentes d ON d.id = dm.docente_id
    JOIN Usuarios u ON u.id = d.id
    WHERE u.user_ = p_user_docente AND m.carrera_id = p_id_carrera
    ORDER BY m.nombre;
END //

-- Obtener ID de docente por usuario
DELIMITER //
CREATE PROCEDURE obtener_id_docente_por_user(IN p_user VARCHAR(50))
BEGIN
    SELECT d.id FROM Docentes d JOIN Usuarios u ON u.id = d.id WHERE u.user_ = p_user LIMIT 1;
END //


-- 1. Listar alumnos por carrera


CREATE PROCEDURE listar_alumnos_por_carrera(IN p_carrera_nombre VARCHAR(125))
BEGIN
    SELECT DISTINCT u.id AS alumno_id, u.nombre, u.apellido
    FROM Usuarios u
    JOIN Alumnos a ON a.id = u.id
    JOIN Alumno_Materias am ON am.alumno_id = a.id
    JOIN Docente_Materias dm ON dm.id = am.docente_materia_id
    JOIN Materias m ON m.id = dm.materia_id
    JOIN Carreras c ON c.id = m.carrera_id
    WHERE c.nombre = p_carrera_nombre
    ORDER BY u.apellido, u.nombre;
END //

CREATE PROCEDURE obtener_carreras_con_materias_docente(IN p_user_docente VARCHAR(100))
BEGIN
    SELECT DISTINCT c.id, c.nombre
    FROM Carreras c
    JOIN Materias m ON m.carrera_id = c.id
    JOIN Docente_Materias dm ON dm.materia_id = m.id
    JOIN Docentes d ON d.id = dm.docente_id
    JOIN Usuarios u ON u.id = d.id
    WHERE u.user_ = p_user_docente
    ORDER BY c.nombre;
END //



-- 2. Buscar alumnos por nombre y carrera

CREATE PROCEDURE buscar_alumnos_por_nombre_carrera(
    IN p_carrera_nombre VARCHAR(125), IN p_nombre VARCHAR(100)
) BEGIN
    SELECT DISTINCT u.id AS alumno_id, u.nombre, u.apellido
    FROM Usuarios u
    JOIN Alumnos a ON a.id = u.id
    JOIN Alumno_Materias am ON am.alumno_id = a.id
    JOIN Docente_Materias dm ON dm.id = am.docente_materia_id
    JOIN Materias m ON m.id = dm.materia_id
    JOIN Carreras c ON c.id = m.carrera_id
    WHERE c.nombre = p_carrera_nombre
      AND (u.nombre LIKE CONCAT('%', p_nombre, '%') OR u.apellido LIKE CONCAT('%', p_nombre, '%'))
    ORDER BY u.apellido, u.nombre;
END //

-- 3. Obtener promedio de alumno en carrera




CREATE PROCEDURE obtener_carrera_de_materia(IN p_materia_id INT)
begin
SELECT carrera_id FROM Materias WHERE id = p_materia_id LIMIT 1;
END //

-- 4. Listar carreras
CREATE PROCEDURE listar_carreras()
BEGIN
    SELECT id, nombre FROM Carreras ORDER BY nombre;
END //



CREATE PROCEDURE id_carrera_por_nombre(IN p_nombre VARCHAR(125))
BEGIN
    SELECT id FROM Carreras WHERE nombre = p_nombre LIMIT 1;
END //


-- Reportes de un alumno con docente y fecha
CREATE PROCEDURE obtener_reportes_con_docente(IN p_id_alumno INT)
BEGIN
    SELECT r.reporte, CONCAT(u.nombre, ' ', u.apellido) AS docente, r.fecha
    FROM Reportes r
    JOIN Usuarios u ON u.id = r.id_docente
    WHERE r.id_alumno = p_id_alumno
    ORDER BY r.fecha DESC;
END //



CREATE PROCEDURE listar_usuarios_por_rol(
    IN p_rol VARCHAR(50)
)
BEGIN
    SELECT 
        cc,
        nombre,
        segundo_nombre,
        apellido,
        segundo_apellido,
        edad,
        sexo,
        direccion,
        telefono,
        correo
    FROM Usuarios
    WHERE cargo = p_rol
    ORDER BY apellido, nombre;
END //



CREATE PROCEDURE obtener_roles_usuarios()
BEGIN
    SELECT DISTINCT cargo AS rol
    FROM Usuarios
    WHERE cargo IN ('alumno', 'docente', 'administrador', 'registro y control')
    ORDER BY rol;
END //


CREATE PROCEDURE buscar_alumnos_por_materia_carrera_docente(
    IN p_docente_user VARCHAR(100),
    IN p_materia_nombre VARCHAR(125),
    IN p_carrera_id INT,
    IN p_nombre VARCHAR(100)
)
begin
    SELECT DISTINCT u.id AS alumno_id, u.nombre, u.apellido
    FROM Usuarios u
    JOIN Alumnos a ON a.id = u.id
    JOIN Alumno_Materias am ON am.alumno_id = a.id
    JOIN Docente_Materias dm ON dm.id = am.docente_materia_id
    JOIN Materias m ON m.id = dm.materia_id
    JOIN Docentes d ON d.id = dm.docente_id
    JOIN Usuarios ud ON ud.id = d.id
    JOIN Carreras c ON c.id = m.carrera_id
    WHERE ud.user_ = p_docente_user
      AND m.nombre = p_materia_nombre
      AND c.id = p_carrera_id
      AND (u.nombre LIKE CONCAT('%', p_nombre, '%') OR u.apellido LIKE CONCAT('%', p_nombre, '%'))
    ORDER BY u.apellido, u.nombre;
END //



-- Procedimiento que devuelve la materia de un alumno con un docente específico
CREATE PROCEDURE obtener_materia_alumno_en_carrera(
    IN p_id_alumno INT,
    IN p_docente_user VARCHAR(100),
    IN p_id_carrera INT
)
BEGIN
    SELECT DISTINCT m.nombre
    FROM Alumno_Materias am
    JOIN Alumnos a ON a.id = am.alumno_id
    JOIN Docente_Materias dm ON dm.id = am.docente_materia_id
    JOIN Materias m ON m.id = dm.materia_id
    JOIN Docentes d ON d.id = dm.docente_id
    JOIN Usuarios u ON u.id = d.id
    WHERE a.id = p_id_alumno
      AND u.user_ = p_docente_user
      AND m.carrera_id = p_id_carrera
    LIMIT 1;
END //








CREATE PROCEDURE obtener_reportes_alumno_completos(IN p_username_alumno VARCHAR(50))
BEGIN
    SELECT 
        r.fecha,
        CONCAT(ud.nombre, ' ', ud.apellido) AS docente,
        m.nombre AS materias,  -- AHORA SOLO UNA MATERIA ESPECÍFICA
        r.reporte
    FROM Reportes r
    JOIN Docentes d ON d.id = r.id_docente
    JOIN Usuarios ud ON ud.id = d.id
    JOIN Alumnos a ON a.id = r.id_alumno
    JOIN Usuarios ua ON ua.id = a.id
    JOIN Docente_Materias dm ON dm.id = r.docente_materia_id  -- JOIN DIRECTO
    JOIN Materias m ON m.id = dm.materia_id
    WHERE ua.user_ = p_username_alumno
    ORDER BY r.fecha DESC;
END //

Create Procedure Listado_Materias()
begin
	SELECT DISTINCT nombre FROM Materias ORDER BY nombre;
end//



create procedure buscar_usuario(in cedula int)
begin

SELECT id, nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cargo FROM Usuarios WHERE cc = cedula;
end//


CREATE PROCEDURE obtener_carreras_del_alumno(IN p_username VARCHAR(50))
BEGIN
    SELECT DISTINCT c.id, c.nombre
    FROM Carreras c
    JOIN Materias m ON m.carrera_id = c.id
    JOIN Docente_Materias dm ON dm.materia_id = m.id
    JOIN Alumno_Materias am ON am.docente_materia_id = dm.id
    JOIN Alumnos a ON a.id = am.alumno_id
    JOIN Usuarios u ON u.id = a.id
    WHERE u.user_ = p_username;
END//


CREATE PROCEDURE listar_notas_por_alumno_carrera(IN p_id_alumno INT, IN p_id_carrera INT)
BEGIN
    SELECT m.nombre AS nombre_materia, am.corte1, am.corte2, am.corte3
    FROM Alumno_Materias am
    JOIN Alumnos a ON a.id = am.alumno_id
    JOIN Docente_Materias dm ON dm.id = am.docente_materia_id
    JOIN Materias m ON m.id = dm.materia_id
    WHERE am.alumno_id = p_id_alumno AND m.carrera_id = p_id_carrera;
END //



CREATE PROCEDURE actualizar_administrador(
    IN p_id INT, IN p_cc VARCHAR(20), IN p_nombre VARCHAR(125),
    IN p_segundo_nombre VARCHAR(125), IN p_apellido VARCHAR(125),
    IN p_segundo_apellido VARCHAR(125), IN p_edad INT,
    IN p_telefono VARCHAR(50), IN p_correo VARCHAR(125), IN p_direccion VARCHAR(150)
)
BEGIN
    UPDATE Usuarios SET
        cc = CAST(p_cc AS UNSIGNED), nombre = p_nombre,
        segundo_nombre = p_segundo_nombre, apellido = p_apellido,
        segundo_apellido = p_segundo_apellido, edad = p_edad,
        telefono = p_telefono, correo = p_correo, direccion = p_direccion
    WHERE id = p_id AND cargo = 'administrador';
END //

CREATE PROCEDURE actualizar_registro_control(
    IN p_id INT, IN p_cc VARCHAR(20), IN p_nombre VARCHAR(125),
    IN p_segundo_nombre VARCHAR(125), IN p_apellido VARCHAR(125),
    IN p_segundo_apellido VARCHAR(125), IN p_edad INT,
    IN p_telefono VARCHAR(50), IN p_correo VARCHAR(125), IN p_direccion VARCHAR(150)
)
BEGIN
    UPDATE Usuarios SET
        cc = CAST(p_cc AS UNSIGNED), nombre = p_nombre,
        segundo_nombre = p_segundo_nombre, apellido = p_apellido,
        segundo_apellido = p_segundo_apellido, edad = p_edad,
        telefono = p_telefono, correo = p_correo, direccion = p_direccion
    WHERE id = p_id AND cargo = 'registro y control';
END //


-- ⬇️ PROCEDIMIENTOS NUEVOS REQUERIDOS ⬇️

-- Verificar si un alumno ya está matriculado en una materia
CREATE PROCEDURE verificar_matricula_existente(
    IN p_alumno_id INT,
    IN p_docente_materia_id INT,
    OUT p_existe TINYINT
)
BEGIN
    SELECT COUNT(*) INTO p_existe
    FROM Alumno_Materias
    WHERE alumno_id = p_alumno_id AND docente_materia_id = p_docente_materia_id;
END //

-- Matricular a un alumno en una materia (devuelve estado)
CREATE PROCEDURE matricular_alumno_individual(
    IN p_alumno_id INT,
    IN p_docente_materia_id INT,
    OUT p_resultado TINYINT
)
BEGIN
    DECLARE ya_matriculado INT;
    
    CALL verificar_matricula_existente(p_alumno_id, p_docente_materia_id, ya_matriculado);
    
    IF ya_matriculado > 0 THEN
        SET p_resultado = 0; -- Ya existente
    ELSE
        INSERT INTO Alumno_Materias (alumno_id, docente_materia_id, corte1, corte2, corte3)
        VALUES (p_alumno_id, p_docente_materia_id, 0, 0, 0);
        
        SET p_resultado = 1; -- Éxito
    END IF;
END //

-- Retirar a un alumno de una materia específica
CREATE PROCEDURE retirar_alumno_individual(
    IN p_alumno_id INT,
    IN p_docente_materia_id INT,
    OUT p_resultado TINYINT
)
BEGIN
    DELETE FROM Alumno_Materias 
    WHERE alumno_id = p_alumno_id AND docente_materia_id = p_docente_materia_id;
    
    IF ROW_COUNT() > 0 THEN
        SET p_resultado = 1; -- Éxito
    ELSE
        SET p_resultado = 0; -- No se encontró
    END IF;
END //

-- Retirar a un alumno de TODAS las materias de una carrera
CREATE PROCEDURE retirar_alumno_carrera(
    IN p_alumno_id INT,
    IN p_carrera_nombre VARCHAR(125),
    OUT p_cantidad INT
)
BEGIN
    DELETE am
    FROM Alumno_Materias am
    JOIN Docente_Materias dm ON dm.id = am.docente_materia_id
    JOIN Materias m ON m.id = dm.materia_id
    JOIN Carreras c ON c.id = m.carrera_id
    WHERE am.alumno_id = p_alumno_id AND c.nombre = p_carrera_nombre;
    
    SET p_cantidad = ROW_COUNT();
END //

-- Obtener ID de alumno por nombre completo (divide nombre y apellido)
CREATE PROCEDURE obtener_id_alumno_por_nombre_completo(
    IN p_nombre_completo VARCHAR(250),
    OUT p_id_alumno INT
)
BEGIN
    DECLARE v_nombre VARCHAR(125);
    DECLARE v_apellido VARCHAR(125);
    
    -- Dividir el nombre completo (formato: "Nombre Apellido")
    SET v_nombre = SUBSTRING_INDEX(p_nombre_completo, ' ', 1);
    SET v_apellido = SUBSTRING_INDEX(p_nombre_completo, ' ', -1);
    
    CALL obtener_id_alumno_por_nombre_apellido(v_nombre, v_apellido, p_id_alumno);
END //

create procedure carrera_id_d_materia(in nombre varchar(200))
begin
	SELECT c.id FROM Materias m JOIN Carreras c ON c.id = m.carrera_id WHERE m.nombre = nombre ;
end//



create procedure alumno_existente(in id int)
begin
SELECT 1 FROM Alumnos WHERE id = id LIMIT 1;
end//


CREATE PROCEDURE id_materia_por_docente(
    IN p_nombre_materia VARCHAR(200),
    IN p_username_docente VARCHAR(100)
)
BEGIN
    SELECT m.id
    FROM Materias m
    JOIN Docente_Materias dm ON dm.materia_id = m.id
    JOIN Docentes d ON d.id = dm.docente_id
    JOIN Usuarios u ON u.id = d.id
    WHERE m.nombre = p_nombre_materia
      AND u.user_ = p_username_docente
    LIMIT 1;
END //

create procedure id_materia_nombre(in nombre varchar(200))
begin 
	SELECT id 
    FROM materias 
    WHERE nombre = nombre
    LIMIT 1;
end//


create procedure Listar_Alumnos_sistema()
begin
SELECT CONCAT(nombre, ' ', apellido) AS nombre_completo FROM Usuarios WHERE cargo = 'alumno' ORDER BY nombre; 
end//

create procedure obtener_alumno_user(in id int)
begin
SELECT u.user_ FROM Usuarios u JOIN Alumnos a ON u.id = a.id WHERE a.id = id;
end//

create procedure Reportes_para_Docente(in id int)
begin
SELECT r.fecha, CONCAT(u.nombre, ' ', u.apellido) as estudiante, m.nombre as materia, r.reporte FROM Reportes r 
JOIN Alumnos a ON a.id = r.id_alumno JOIN Usuarios u ON u.id = a.id 
JOIN Docente_Materias dm ON dm.id = r.docente_materia_id JOIN Materias m ON m.id = dm.materia_id WHERE dm.id = id ORDER BY r.fecha DESC;
end//



-- Obtener materias de un docente con nombre de carrera
DELIMITER //
CREATE PROCEDURE obtener_materias_docente_con_carrera(IN p_docente_id INT)
BEGIN
    SELECT 
        m.nombre AS materia_nombre,
        c.nombre AS carrera_nombre,
        dm.id AS docente_materia_id
    FROM Docente_Materias dm
    JOIN Materias m ON m.id = dm.materia_id
    JOIN Carreras c ON c.id = m.carrera_id
    WHERE dm.docente_id = p_docente_id
    ORDER BY c.nombre, m.nombre;
END //

-- Reemplazar docente en una materia específica (conserva alumnos y notas)
DELIMITER //
CREATE PROCEDURE reemplazar_docente_en_materia(
    IN p_docente_materia_id INT,
    IN p_nuevo_docente_id INT
)
BEGIN
    DECLARE v_materia_id INT;
    DECLARE v_count INT;
    
    -- Obtener materia_id
    SELECT materia_id INTO v_materia_id 
    FROM Docente_Materias 
    WHERE id = p_docente_materia_id;
    
    -- Verificar si el nuevo docente ya tiene esta materia
    SELECT COUNT(*) INTO v_count
    FROM Docente_Materias
    WHERE docente_id = p_nuevo_docente_id 
      AND materia_id = v_materia_id;
    
    IF v_count > 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'El docente ya tiene asignada esta materia';
    ELSE
        UPDATE Docente_Materias
        SET docente_id = p_nuevo_docente_id
        WHERE id = p_docente_materia_id;
    END IF;
END //