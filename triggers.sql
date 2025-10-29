DELIMITER $$

-- Eliminar usuario si se elimina un alumno
CREATE TRIGGER trg_alumno_delete
AFTER DELETE ON Alumnos
FOR EACH ROW
BEGIN
    DELETE FROM Usuarios WHERE id = OLD.id;
END$$

-- Eliminar usuario si se elimina un docente
CREATE TRIGGER trg_docente_delete
AFTER DELETE ON Docentes
FOR EACH ROW
BEGIN
    DELETE FROM Usuarios WHERE id = OLD.id;
END$$

-- Eliminar usuario si se elimina un administrador
CREATE TRIGGER trg_admin_delete
AFTER DELETE ON Administradores
FOR EACH ROW
BEGIN
    DELETE FROM Usuarios WHERE id = OLD.id;
END$$






CREATE TRIGGER trg_crear_user_contrasena
BEFORE INSERT ON Usuarios
FOR EACH ROW
BEGIN
    DECLARE primer_nombre   VARCHAR(125);
    DECLARE primer_apellido VARCHAR(125);
    DECLARE inicial_nom     CHAR(1);
    DECLARE inicial_ape     CHAR(1);

    SET primer_nombre   = TRIM(SUBSTRING_INDEX(NEW.nombre,   ' ', 1));
    SET primer_apellido = TRIM(SUBSTRING_INDEX(NEW.apellido, ' ', 1));

    SET inicial_nom = SUBSTRING(primer_nombre,   1, 1);
    SET inicial_ape = SUBSTRING(primer_apellido, 1, 1);

    -- user_: nombre + inicial apellido + últimos 4 dígitos del cc
    IF NEW.user_ IS NULL OR NEW.user_ = '' THEN
        SET NEW.user_ = LOWER(CONCAT(
            primer_nombre,
            inicial_ape,
            NEW.cc % 10000
        ));
    END IF;

    -- contraseña: cc + inicial nombre + inicial apellido
    IF NEW.contrasena_hash IS NULL OR NEW.contrasena_hash = '' THEN
        SET NEW.contrasena_hash = CONCAT(NEW.cc, inicial_nom, inicial_ape);
    END IF;
END$$













DELIMITER ;
