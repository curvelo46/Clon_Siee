DELIMITER //

CREATE PROCEDURE listado_alumnos()
BEGIN
    select*from alumnos;
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


