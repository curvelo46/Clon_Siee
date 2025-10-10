DELIMITER //

CREATE PROCEDURE listado_alumnos()
BEGIN
    SELECT nombre, apellido FROM Alumnos;
END //

CREATE PROCEDURE id_alumno(in nombres varchar(125))
BEGIN
    SELECT id FROM Alumnos WHERE nombre = Nombres;
END //

CREATE PROCEDURE listar_promedios_por_docente_materia(IN p_docente VARCHAR(100), IN p_materia VARCHAR(100))
BEGIN
    SELECT 
        a.nombre,
        a.apellido,
        m.corte1,
        m.corte2,
        m.corte3,
        ROUND((m.corte1 + m.corte2 + m.corte3) / 3, 2) AS promedio
    FROM Materias m
    INNER JOIN Alumnos a ON m.alumno_id = a.id
    INNER JOIN Docentes d ON m.docente_id = d.id
    WHERE d.nombre = p_docente
      AND m.nombre_materia = p_materia;
END //

CREATE PROCEDURE obtener_materia_docente(IN p_docente_nombre VARCHAR(100))
BEGIN
    SELECT m.nombre_materia
    FROM Materias m
    INNER JOIN Docentes d ON m.docente_id = d.id
    WHERE d.nombre = p_docente_nombre
    LIMIT 1;
END //


CREATE PROCEDURE listar_notas_docente_materia(IN p_docente VARCHAR(100), IN p_materia VARCHAR(100))
BEGIN
    SELECT 
        a.nombre AS estudiante,
        a.apellido,
        m.corte1,
        m.corte2,
        m.corte3
    FROM Materias m
    INNER JOIN Alumnos a ON m.alumno_id = a.id
    INNER JOIN Docentes d ON m.docente_id = d.id
    WHERE d.nombre = p_docente
      AND m.nombre_materia = p_materia;
END //


CREATE PROCEDURE actualizar_nota_alumno(
    IN p_docente_nombre VARCHAR(100),
    IN p_estudiante_nombre VARCHAR(100),
    IN p_corte INT,
    IN p_nota DOUBLE
)
BEGIN
    IF p_corte = 1 THEN
        UPDATE Materias m
        INNER JOIN Alumnos a ON m.alumno_id = a.id
        INNER JOIN Docentes d ON m.docente_id = d.id
        SET m.corte1 = p_nota
        WHERE d.nombre = p_docente_nombre
        AND CONCAT(a.nombre, ' ', a.apellido) = p_estudiante_nombre;
    ELSEIF p_corte = 2 THEN
        UPDATE Materias m
        INNER JOIN Alumnos a ON m.alumno_id = a.id
        INNER JOIN Docentes d ON m.docente_id = d.id
        SET m.corte2 = p_nota
        WHERE d.nombre = p_docente_nombre
        AND CONCAT(a.nombre, ' ', a.apellido) = p_estudiante_nombre;
    ELSEIF p_corte = 3 THEN
        UPDATE Materias m
        INNER JOIN Alumnos a ON m.alumno_id = a.id
        INNER JOIN Docentes d ON m.docente_id = d.id
        SET m.corte3 = p_nota
        WHERE d.nombre = p_docente_nombre
        AND CONCAT(a.nombre, ' ', a.apellido) = p_estudiante_nombre;
    END IF;
END //


CREATE PROCEDURE quitar_materia(IN p_nombre_materia VARCHAR(100))
BEGIN
    DECLARE v_docente_id INT;

    -- 1️⃣ Buscar el docente asignado a la materia
    SELECT docente_id INTO v_docente_id
    FROM Materias
    WHERE nombre_materia = p_nombre_materia
    LIMIT 1;

    -- Si no existe o está sin docente, generar error
    IF v_docente_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La materia no tiene un docente asignado o no existe.';
    END IF;

    -- 2️⃣ Actualizar al docente → dejarlo sin asignatura e id_materia = 'x'
    UPDATE Docentes
    SET materia = 'sin asignatura',
        id_materia = 'x'
    WHERE id = v_docente_id;

    -- 3️⃣ Actualizar la materia → dejarla libre, sin docente y con id_asignatura = 'x'
    UPDATE Materias
    SET docente_id = NULL,
        id_asignatura = 'x'
    WHERE nombre_materia = p_nombre_materia;
END //


CREATE PROCEDURE listar_notas_por_alumno(IN p_alumno_id INT)
BEGIN
    SELECT 
        m.nombre_materia,
        m.corte1,
        m.corte2,
        m.corte3
    FROM Materias m
    WHERE m.alumno_id = p_alumno_id;
END //

CREATE PROCEDURE listar_estudiantes()
BEGIN
    SELECT 
        id, 
        cc, 
        nombre, 
        segundo_nombre, 
        apellido, 
        segundo_apellido, 
        edad, 
        telefono, 
        correo, 
        direccion
    FROM Alumnos;
END //

CREATE PROCEDURE listar_docentes()
BEGIN
    SELECT 
        cc,
        nombre,
        segundo_nombre,
        apellido,
        segundo_apellido,
        edad,
        telefono,
        correo,
        direccion,
        materia
    FROM Docentes;
END //

CREATE PROCEDURE listar_estudiantes_por_docente_materia(IN p_docente VARCHAR(100), IN p_materia VARCHAR(100))
BEGIN
    SELECT 
        a.cc,
        a.nombre,
        a.apellido,
        a.edad,
        a.telefono,
        a.correo
    FROM Materias m
    INNER JOIN Alumnos a ON m.alumno_id = a.id
    INNER JOIN Docentes d ON m.docente_id = d.id
    WHERE d.nombre = p_docente
      AND m.nombre_materia = p_materia;
END //



CREATE PROCEDURE actualizar_docente(
    IN p_cc INT,
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
    UPDATE Docentes
    SET 
        nombre = p_nombre,
        segundo_nombre = p_segundo_nombre,
        apellido = p_apellido,
        segundo_apellido = p_segundo_apellido,
        edad = p_edad,
        telefono = p_telefono,
        correo = p_correo,
        direccion = p_direccion
    WHERE cc = p_cc;
END //



CREATE PROCEDURE actualizar_estudiante(
    IN p_id INT,
    IN p_cc VARCHAR(125),
    IN p_nombre VARCHAR(125),
    IN p_segundo_nombre VARCHAR(125),
    IN p_apellido VARCHAR(125),
    IN p_segundo_apellido VARCHAR(125),
    IN p_edad INT,
    IN p_telefono VARCHAR(125),
    IN p_correo VARCHAR(125),
    IN p_direccion VARCHAR(150)
)
BEGIN
    UPDATE Alumnos
    SET 
        cc = p_cc,
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

CREATE PROCEDURE Materias_disponibles()
BEGIN
    SELECT nombre_materia FROM Materias  where Materias.id_asignatura="x";
END //

CREATE PROCEDURE id_materia(in nombre varchar(125))
BEGIN
    SELECT nombre_materia FROM Materias WHERE nombre_materia = nombre AND id_asignatura = 'x';
END //

CREATE PROCEDURE asignar_p_a_m(in id_d int, in id_a varchar(1) , nombre_m varchar(125))
BEGIN
    UPDATE Materias SET docente_id = id_d, id_asignatura = id_a WHERE nombre_materia = nombre_m;
END //

CREATE PROCEDURE asignar_m_a_p(in materias varchar(125), in id_m varchar(1),in id_d int )
BEGIN
    UPDATE Docentes SET materia = materias, id_materia = id_m WHERE id = id_d;
END //

CREATE PROCEDURE listado_docentes_disponibles()
BEGIN
	SELECT id, nombre, apellido FROM Docentes ;
END //

CREATE PROCEDURE Cargos(in usuario varchar(50), in pass varchar(50) )
BEGIN
    SELECT cargo FROM Usuarios WHERE user_ = usuario AND contrasena = pass ;
END //


CREATE PROCEDURE crear_materia(in materia varchar(125))
BEGIN
    insert into materias (nombre_materia) values(materia);
END //


CREATE PROCEDURE mi_procedimiento_ejemplo(
    IN nombre VARCHAR(25),
    IN segundo_nombre VARCHAR(25),
    IN apellido VARCHAR(25),
    IN segundo_apellido VARCHAR(25),
    IN edad VARCHAR(2),
    IN telefono VARCHAR(20),
    IN correo VARCHAR(100),
    IN direccion VARCHAR(150),
    IN cc VARCHAR(15),
    IN sexo VARCHAR(25)
)
BEGIN
    INSERT INTO Alumnos (nombre, segundo_nombre, apellido, segundo_apellido, edad,telefono, correo, direccion, cc, sexo)
    VALUES (nombre, segundo_nombre, apellido, segundo_apellido, edad,telefono, correo, direccion, cc, sexo);
END //

CREATE PROCEDURE Registrar_Docente(
    nombre VARCHAR(125) ,
    segundo_nombre VARCHAR(125),
    apellido VARCHAR(125),
    segundo_apellido VARCHAR(125),
    edad int ,
    telefono varchar(125) ,
    correo VARCHAR(100),
    direccion VARCHAR(150),
    cc int     
)
BEGIN
    INSERT INTO docentes (nombre, segundo_nombre, apellido, segundo_apellido, edad,telefono, correo, direccion, cc)
    VALUES (nombre, segundo_nombre, apellido, segundo_apellido, edad,telefono, correo, direccion, cc);
END //

CREATE PROCEDURE sp_borrar_materia(IN materia_nombre VARCHAR(125))
BEGIN
    DECLARE materiaId INT;

    -- Buscar el id de la materia
    SELECT id_materia INTO materiaId
    FROM Materias
    WHERE nombre_materia = materia_nombre;

    -- Si existe, desvincularla del docente y eliminarla
    IF materiaId IS NOT NULL THEN
        UPDATE Docentes
        SET materia = 'sin asignatura'
        WHERE id = (SELECT docente_id FROM Materias WHERE id_materia = materiaId);

        DELETE FROM Materias WHERE id_materia = materiaId;
    END IF;
END$$

DELIMITER ;


