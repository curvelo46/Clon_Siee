-- TRIGGERS
    

DELIMITER $$

-- Alumno crea usuario
CREATE TRIGGER trg_alumno_usuario
BEFORE INSERT ON Alumnos
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM Usuarios WHERE user_ = NEW.nombre AND contrasena = NEW.cc
    ) THEN
        INSERT INTO Usuarios (user_, contrasena, cargo)
        VALUES (NEW.nombre, NEW.cc, 'alumno');
        SET NEW.id = LAST_INSERT_ID();
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: Usuario con el mismo nombre y contraseña ya existe.';
    END IF;
END$$

-- Alumno elimina usuario
CREATE TRIGGER trg_alumno_delete
AFTER DELETE ON Alumnos
FOR EACH ROW
BEGIN
    DELETE FROM Usuarios WHERE id = OLD.id;
END$$

-- Docente crea usuario
CREATE TRIGGER trg_docente_usuario
BEFORE INSERT ON Docentes
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM Usuarios WHERE user_ = NEW.nombre AND contrasena = NEW.cc
    ) THEN
        INSERT INTO Usuarios (user_, contrasena, cargo)
        VALUES (NEW.nombre, NEW.cc, 'docente');
        SET NEW.id = LAST_INSERT_ID();
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: Usuario con el mismo nombre y contraseña ya existe.';
    END IF;
END$$

-- Docente elimina usuario y materias asociadas
CREATE TRIGGER trg_docente_delete
AFTER DELETE ON Docentes
FOR EACH ROW
BEGIN
    DELETE FROM Usuarios WHERE id = OLD.id;
    UPDATE Materias SET docente_id = NULL, estado = 'libre'
    WHERE docente_id = OLD.id;
END$$

-- Administrador crea usuario
CREATE TRIGGER trg_admin_usuario
BEFORE INSERT ON Administradores
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM Usuarios WHERE user_ = NEW.nombre AND contrasena = NEW.cc
    ) THEN
        INSERT INTO Usuarios (user_, contrasena, cargo)
        VALUES (NEW.nombre, NEW.cc, 'administrador');
        SET NEW.id = LAST_INSERT_ID();
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: Usuario con el mismo nombre y contraseña ya existe.';
    END IF;
END$$

-- Administrador elimina usuario
CREATE TRIGGER trg_admin_delete
AFTER DELETE ON Administradores
FOR EACH ROW
BEGIN
    DELETE FROM Usuarios WHERE id = OLD.id;
END$$

DELIMITER ;
