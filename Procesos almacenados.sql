DELIMITER //
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
END//

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
END//

CREATE PROCEDURE ingresar_usuario(
    IN p_nombre VARCHAR(125),
    IN p_segundo_nombre VARCHAR(125),
    IN p_apellido VARCHAR(125),
    IN p_segundo_apellido VARCHAR(125),
    IN p_edad INT,
    IN p_telefono VARCHAR(50),
    IN p_correo VARCHAR(125),
    IN p_direccion VARCHAR(150),
    IN p_cc BIGINT,
    IN p_sexo VARCHAR(25),
    IN p_cargo VARCHAR(123)
)
BEGIN
    INSERT INTO usuarios (
        nombre, segundo_nombre, apellido, segundo_apellido,
        edad, telefono, correo, direccion, cc, sexo, cargo,
        user_, contrasena_hash
    ) VALUES (
        p_nombre, p_segundo_nombre, p_apellido, p_segundo_apellido,
        p_edad, p_telefono, p_correo, p_direccion, p_cc, p_sexo, p_cargo,
        CONCAT(p_nombre, LEFT(p_apellido, 2)), 
        CONCAT(p_cc, LEFT(p_nombre, 2))  
    );
END//

CREATE  PROCEDURE listar_alumnos()
BEGIN
    SELECT u.id, u.nombre, u.apellido, u.cc
    FROM Alumnos a
    JOIN Usuarios u ON a.id = u.id;
END//

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

CREATE PROCEDURE obtener_materia_docente(
    IN p_user VARCHAR(50)
)
BEGIN
    SELECT m.nombre
    FROM Usuarios u
    JOIN Docentes d ON u.id = d.id
    JOIN Docente_Materias dm ON d.id = dm.docente_id
    JOIN Materias m ON dm.materia_id = m.id
    WHERE u.user_ = p_user;
END //

CREATE PROCEDURE listado_docentes_disponibles()
BEGIN
    SELECT u.id, u.nombre, u.apellido
    FROM Usuarios u
    JOIN Docentes d ON u.id = d.id
    WHERE d.id NOT IN (
        SELECT dm.docente_id FROM Docente_Materias dm
    );
END //

CREATE PROCEDURE Materias_disponibles()
BEGIN
    SELECT m.id, m.nombre AS nombre_materia
    FROM Materias m
    WHERE m.id NOT IN (
        SELECT dm.materia_id FROM Docente_Materias dm
    );
END //


CREATE PROCEDURE id_materia(
    IN p_nombre_materia VARCHAR(125)
)
BEGIN
    SELECT id
    FROM Materias
    WHERE nombre = p_nombre_materia;
END //


CREATE PROCEDURE asignar_m_a_p(
    IN p_materia_id INT,
    IN p_docente_id INT
)
BEGIN
    INSERT INTO Docente_Materias (docente_id, materia_id)
    VALUES (p_docente_id, p_materia_id);
END //



CREATE PROCEDURE listar_promedios_por_docente_materia(
    IN p_docente_nombre VARCHAR(100),
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
    JOIN Alumnos a ON am.alumno_id = a.id
    JOIN Usuarios u ON a.id = u.id
    JOIN Materias m ON am.materia_id = m.id
    JOIN Docente_Materias dm ON dm.materia_id = m.id
    JOIN Docentes d ON dm.docente_id = d.id
    JOIN Usuarios du ON d.id = du.id
    WHERE m.nombre = p_materia_nombre
      AND du.user_ = p_docente_nombre;
END //

CREATE PROCEDURE listar_estudiantes_por_docente_materia(
    IN p_nombre_docente VARCHAR(100),
    IN p_nombre_materia VARCHAR(100)
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
    JOIN Materias m ON am.materia_id = m.id
    JOIN Docente_Materias dm ON dm.materia_id = m.id
    JOIN Docentes d ON dm.docente_id = d.id
    JOIN Usuarios ud ON d.id = ud.id
    WHERE m.nombre = p_nombre_materia
      AND ud.user_ = p_nombre_docente;
END //

CREATE PROCEDURE listar_notas_docente_materia(
    IN p_docente_nombre VARCHAR(100),
    IN p_materia_nombre VARCHAR(100)
)
BEGIN
    SELECT 
        a.id AS alumno_id,
        u.nombre AS estudiante,
        u.apellido,
        n.corte1,
        n.corte2,
        n.corte3
    FROM alumno_materias n
    JOIN Alumnos a ON n.alumno_id = a.id
    JOIN Usuarios u ON a.id = u.id
    JOIN Materias m ON n.materia_id = m.id
    JOIN Docente_Materias dm ON m.id = dm.materia_id
    JOIN Docentes d ON dm.docente_id = d.id
    JOIN Usuarios ud ON d.id = ud.id
    WHERE m.nombre = p_materia_nombre
      AND ud.user_ = p_docente_nombre;
END //

CREATE PROCEDURE ingresar_usuario(
    IN p_nombre VARCHAR(100),
    IN p_segundoNombre VARCHAR(100),
    IN p_apellido VARCHAR(100),
    IN p_segundoApellido VARCHAR(100),
    IN p_edad INT,
    IN p_telefono VARCHAR(20),
    IN p_correo VARCHAR(100),
    IN p_direccion VARCHAR(255),
    IN p_cedula VARCHAR(20),
    IN p_genero VARCHAR(10),
    IN p_username VARCHAR(50)
)
BEGIN
        -- Insertar el nuevo usuario
        INSERT INTO usuarios(nombre, segundoNombre, apellido, segundoApellido, edad, telefono, correo, direccion, cedula, genero, username)
        VALUES (p_nombre, p_segundoNombre, p_apellido, p_segundoApellido, p_edad, p_telefono, p_correo, p_direccion, p_cedula, p_genero, p_username);
    

END//