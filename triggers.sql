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

DELIMITER ;
