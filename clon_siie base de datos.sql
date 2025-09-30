	CREATE DATABASE CBN;
	USE CBN;


	-- TABLA USUARIOS
	CREATE TABLE Usuarios (
		id INT AUTO_INCREMENT PRIMARY KEY,
		user_ VARCHAR(50) NOT NULL,
		contraseña VARCHAR(50) NOT NULL,
		cargo ENUM('alumno','docente','administrador') NOT NULL,
		UNIQUE KEY unq_user_pass (user_, contraseña)  -- 🔹 No permite mismo nombre con misma contraseña
	);


	-- TABLA ALUMNOS
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


	-- TABLA DOCENTES
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
		CONSTRAINT fk_docente_usuario FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
	);


	-- TABLA ADMINISTRADORES
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


	-- TABLA MATERIAS
	CREATE TABLE Materias (
		id INT AUTO_INCREMENT PRIMARY KEY,
		docente_id INT,
		nombre VARCHAR(125) NOT NULL,
		materia VARCHAR(125) DEFAULT 'sin asignatura',
		CONSTRAINT fk_materia_docente FOREIGN KEY (docente_id) REFERENCES Docentes(id) ON DELETE CASCADE
	);
	
    create table materias_existentes(nombre varchar(125) primary key,estado varchar(25));
    

    DELIMITER $$

CREATE PROCEDURE sp_borrar_materia(IN materia_nombre VARCHAR(125))
BEGIN
    -- 1. Eliminar el registro de la tabla Materias_existentes
    DELETE FROM Materias_existentes WHERE nombre = materia_nombre;

    -- 2. Construir y ejecutar SQL dinámico para borrar la tabla
    SET @sql = CONCAT('DROP TABLE IF EXISTS `', materia_nombre, '`');

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;
    
	-- TRIGGERS
    
	DELIMITER $$


	CREATE TRIGGER trg_alumno_usuario
	BEFORE INSERT ON Alumnos
	FOR EACH ROW
	BEGIN

		IF NOT EXISTS (
			SELECT 1 FROM Usuarios WHERE user_ = NEW.nombre AND contraseña = NEW.cc
		) THEN
			INSERT INTO Usuarios (user_, contraseña, cargo)
			VALUES (NEW.nombre, NEW.cc, 'alumno');
			SET NEW.id = LAST_INSERT_ID();
		ELSE
			SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Error: Usuario con el mismo nombre y contraseña ya existe.';
		END IF;
	END$$


	CREATE TRIGGER trg_alumno_delete
	AFTER DELETE ON Alumnos
	FOR EACH ROW
	BEGIN
		DELETE FROM Usuarios WHERE id = OLD.id;
	END$$


	CREATE TRIGGER trg_docente_usuario
	BEFORE INSERT ON Docentes
	FOR EACH ROW
	BEGIN
		IF NOT EXISTS (
			SELECT 1 FROM Usuarios WHERE user_ = NEW.nombre AND contraseña = NEW.cc
		) THEN
			INSERT INTO Usuarios (user_, contraseña, cargo)
			VALUES (NEW.nombre, NEW.cc, 'docente');
			SET NEW.id = LAST_INSERT_ID();
		ELSE
			SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Error: Usuario con el mismo nombre y contraseña ya existe.';
		END IF;
	END$$


	CREATE TRIGGER trg_docente_materia
	AFTER INSERT ON Docentes
	FOR EACH ROW
	BEGIN
		INSERT INTO Materias (docente_id, nombre, materia)
		VALUES (NEW.id, CONCAT(NEW.nombre, ' ', IFNULL(NEW.segundo_nombre,'')), 'sin asignatura');
	END$$


	CREATE TRIGGER trg_docente_delete
	AFTER DELETE ON Docentes
	FOR EACH ROW
	BEGIN
		DELETE FROM Usuarios WHERE id = OLD.id;
		DELETE FROM Materias WHERE docente_id = OLD.id;
	END$$


	CREATE TRIGGER trg_admin_usuario
	BEFORE INSERT ON Administradores
	FOR EACH ROW
	BEGIN
		IF NOT EXISTS (
			SELECT 1 FROM Usuarios WHERE user_ = NEW.nombre AND contraseña = NEW.cc
		) THEN
			INSERT INTO Usuarios (user_, contraseña, cargo)
			VALUES (NEW.nombre, NEW.cc, 'administrador');
			SET NEW.id = LAST_INSERT_ID();
		ELSE
			SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Error: Usuario con el mismo nombre y contraseña ya existe.';
		END IF;
	END$$


	CREATE TRIGGER trg_admin_delete
	AFTER DELETE ON Administradores
	FOR EACH ROW
	BEGIN
		DELETE FROM Usuarios WHERE id = OLD.id;
	END$$

	DELIMITER ;
	

	-- INSERTAR DATOS

	INSERT INTO Alumnos (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, sexo)
	VALUES 
	('Andres','camilo','curvelo','diaz','18','3243582032','andrescurvelodiaz@gmail.com','calle29','1084451641',"hombre"),
	('jose','billero','granado','martines','20','3945768123','','la libertador','4900224',"hombre"),
	('hilary','fontalvo','ospina','ospina','18','12321323','andrescamilo@gmail.com','11000232','4234',"mujer"),
	('andrea','martines','curvelo','jose','10','1232323','andrescamilo@gmail.com','11000232','434',"mujer"),
	('milagro','la doña','peleona',' de la esquina','70','1232323','andrescamilo@gmail.com','11000232','4',"mujer"),
	('to','la doña','peleona',' de la esquina','70','1232323','andrescamilo@gmail.com','11000232','008',"hombre");

	INSERT INTO Docentes (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc)
	VALUES
	('luiz','','carlos','','40','123456789','andres@gmail.com','direccion','12123'),
	('doctora','','tatiana','','48','987654321','camila@gmail.com','direccion','121231'),
	('profesora','','maria','','28','456789123','camila@gmail.com','direccion','12131');

	INSERT INTO Administradores (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc)
	VALUES
	('jose','','curvelo','','18','12321323','andrescamilo@gmail.com','direccion','34'),
	('fernanda','','fernanda','','18','123223','camila@gmail.com','direccion','4545');

	insert into materias_existentes values('java','libre');
    select*from materias_existentes;

