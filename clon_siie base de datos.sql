
DROP DATABASE IF EXISTS CBN;
CREATE DATABASE CBN;
USE CBN;

-- =============================
-- TABLA USUARIOS
-- =============================
CREATE TABLE Usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_ VARCHAR(50) NOT NULL,
    contrasena VARCHAR(50) NOT NULL,
    cargo ENUM('alumno','docente','administrador','master') NOT NULL,
    UNIQUE KEY unq_user_pass (user_, contrasena)  
);

-- =============================
-- TABLA ALUMNOS
-- =============================
CREATE TABLE Alumnos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(25) NOT NULL,
    segundo_nombre VARCHAR(25),
    apellido VARCHAR(25) NOT NULL,
    segundo_apellido VARCHAR(25),
    edad VARCHAR(2) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(150),
    cc VARCHAR(15) NOT NULL UNIQUE,
    sexo VARCHAR(25),
    CONSTRAINT fk_alumno_usuario FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- =============================
-- TABLA DOCENTES
-- =============================
CREATE TABLE Docentes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(25) NOT NULL,
    segundo_nombre VARCHAR(25),
    apellido VARCHAR(25) NOT NULL,
    segundo_apellido VARCHAR(25),
    edad VARCHAR(2) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(150),
    cc VARCHAR(15) NOT NULL UNIQUE,
    materia VARCHAR(125) DEFAULT 'sin asignatura',
    CONSTRAINT fk_docente_usuario FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- =============================
-- TABLA ADMINISTRADORES
-- =============================
CREATE TABLE Administradores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(25) NOT NULL,
    segundo_nombre VARCHAR(25),
    apellido VARCHAR(25) NOT NULL,
    segundo_apellido VARCHAR(25),
    edad VARCHAR(2) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(150),
    cc VARCHAR(15) NOT NULL UNIQUE,
    CONSTRAINT fk_admin_usuario FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- =============================
-- TABLA MATERIAS
-- =============================

CREATE TABLE Materias (
    id_materia INT AUTO_INCREMENT PRIMARY KEY,
    nombre_materia VARCHAR(125) NOT NULL,
    docente_id INT NULL,
    alumno_id INT NULL,
    corte1 DECIMAL(5,2) DEFAULT 0,
    corte2 DECIMAL(5,2) DEFAULT 0,
    corte3 DECIMAL(5,2) DEFAULT 0,
    estado ENUM('libre','activa') DEFAULT 'libre',
    CONSTRAINT fk_materia_docente FOREIGN KEY (docente_id) REFERENCES Docentes(id) ON DELETE SET NULL,
    CONSTRAINT fk_materia_alumno FOREIGN KEY (alumno_id) REFERENCES Alumnos(id) ON DELETE CASCADE
);





    
	-- TRIGGERS
    DELIMITER $$

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


INSERT INTO Alumnos (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, sexo)
VALUES 
('Andres','camilo','curvelo','diaz','18','3243582032','andrescurvelodiaz@gmail.com','calle29','1084451641',"hombre"),
('jose','billero','granado','martines','20','3945768123','','la libertador','4900224',"hombre"),
('hilary','fontalvo','ospina','ospina','18','12321323','andrescamilo@gmail.com','11000232','4234',"mujer"),
('andrea','martines','curvelo','jose','10','1232323','andrescamilo@gmail.com','11000232','434',"mujer"),
('milagro','la doña','peleona','de la esquina','70','1232323','andrescamilo@gmail.com','11000232','4',"mujer"),
('to','la doña','peleona','de la esquina','70','1232323','andrescamilo@gmail.com','11000232','008',"hombre");


-- =============================
-- INSERTAR DOCENTES
-- (Todos empiezan con 'sin asignatura')
-- =============================
INSERT INTO Docentes (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, materia)
VALUES
('luiz','','carlos','','40','123456789','andres@gmail.com','direccion','12123','sin asignatura'),
('doctora','','tatiana','','48','987654321','camila@gmail.com','direccion','121231','sin asignatura'),
('profesora','','maria','','28','456789123','camila@gmail.com','direccion','12131','sin asignatura');


-- =============================
-- INSERTAR ADMINISTRADORES
-- =============================
INSERT INTO Administradores (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc)
VALUES
('jose','','curvelo','','18','12321323','andrescamilo@gmail.com','direccion','34'),
('fernanda','','fernanda','','18','123223','camila@gmail.com','direccion','4545');


-- =============================
-- INSERTAR MATERIAS
-- =============================
INSERT INTO Materias (nombre_materia, docente_id, alumno_id, estado)
VALUES ('matematicas', 7, 1, 'activa');










