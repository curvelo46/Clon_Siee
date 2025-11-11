	DROP DATABASE IF EXISTS CBN;
	CREATE DATABASE CBN CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
	
    USE CBN;

	/* =========================================================
	   TABLAS
	   ========================================================= */

	-- Usuarios (tabla base)
	CREATE TABLE Usuarios (
		id                INT AUTO_INCREMENT PRIMARY KEY,
		nombre            VARCHAR(125) NOT NULL,
		segundo_nombre    VARCHAR(125),
		apellido          VARCHAR(125) NOT NULL,
		segundo_apellido  VARCHAR(125),
		edad              INT NOT NULL,
		telefono          VARCHAR(50),
		correo            VARCHAR(125),
		direccion         VARCHAR(150),
		cc                BIGINT NOT NULL UNIQUE,
		sexo              VARCHAR(25),
		user_             VARCHAR(50) UNIQUE,
		contrasena_hash   VARCHAR(255),
		cargo             ENUM('alumno','docente','administrador','registro y control') NOT NULL
	);

	-- Roles
	CREATE TABLE Alumnos (
		id INT PRIMARY KEY,
		FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
	);

	CREATE TABLE Docentes (
		id INT PRIMARY KEY,
		FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
	);

	CREATE TABLE Administradores (
		id INT PRIMARY KEY,
		FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
	);

	-- Académico
	CREATE TABLE Carreras (
		id     INT AUTO_INCREMENT PRIMARY KEY,
		nombre VARCHAR(125) NOT NULL UNIQUE
	);

	CREATE TABLE Materias (
		id         INT AUTO_INCREMENT PRIMARY KEY,
		nombre     VARCHAR(125) NOT NULL,
		carrera_id INT NOT NULL,
		UNIQUE (nombre, carrera_id),
		FOREIGN KEY (carrera_id) REFERENCES Carreras(id) ON DELETE CASCADE
	);


	CREATE TABLE Docente_Materias (
		id          INT AUTO_INCREMENT PRIMARY KEY,
		docente_id  INT NOT NULL,
		materia_id  INT NOT NULL,
		FOREIGN KEY (docente_id) REFERENCES Docentes(id) ON DELETE CASCADE,
		FOREIGN KEY (materia_id) REFERENCES Materias(id) ON DELETE CASCADE
	);

	CREATE TABLE Alumno_Materias (
		alumno_id          INT,
		docente_materia_id INT,
		corte1             DECIMAL(5,2) DEFAULT 0,
		corte2             DECIMAL(5,2) DEFAULT 0,
		corte3             DECIMAL(5,2) DEFAULT 0,
		corte1_edit		   TINYINT DEFAULT 0,
		corte2_edit		   TINYINT DEFAULT 0,
		corte3_edit		   TINYINT DEFAULT 0,
		PRIMARY KEY (alumno_id, docente_materia_id),
		FOREIGN KEY (alumno_id)       REFERENCES Alumnos(id)          ON DELETE CASCADE,
		FOREIGN KEY (docente_materia_id) REFERENCES Docente_Materias(id) ON DELETE CASCADE
	);


	CREATE TABLE RegistroYControl (
		id INT PRIMARY KEY,        
		FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
	);


	CREATE TABLE Reportes (
		id_alumno  INT NOT NULL,
		id_docente INT NOT NULL,
		reporte    TEXT NOT NULL,
		fecha      DATETIME DEFAULT CURRENT_TIMESTAMP,
		PRIMARY KEY (id_alumno, id_docente, fecha),
        docente_materia_id INT,
		FOREIGN KEY (docente_materia_id) REFERENCES Docente_Materias(id),
		FOREIGN KEY (id_alumno)  REFERENCES Alumnos(id)  ON DELETE CASCADE,
		FOREIGN KEY (id_docente) REFERENCES Docentes(id) ON DELETE CASCADE
	);