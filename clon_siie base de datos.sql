
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
    nombre VARCHAR(125) NOT NULL,
    segundo_nombre VARCHAR(125),
    apellido VARCHAR(125) NOT NULL,
    segundo_apellido VARCHAR(125),
    edad int not null,
    telefono varchar(125) not null,
    correo VARCHAR(125),
    direccion VARCHAR(150),
    cc int NOT NULL UNIQUE,
    sexo VARCHAR(25),
    CONSTRAINT fk_alumno_usuario FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- =============================
-- TABLA DOCENTES
-- =============================
CREATE TABLE Docentes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(125) NOT NULL,
    segundo_nombre VARCHAR(125),
    apellido VARCHAR(125) NOT NULL,
    segundo_apellido VARCHAR(125),
    edad int NOT NULL,
    telefono varchar(125) not null,
    correo VARCHAR(100),
    direccion VARCHAR(150),
    cc int NOT NULL UNIQUE,
    materia VARCHAR(125) DEFAULT 'sin asignatura',
    id_materia varchar(1) default 'x',
    CONSTRAINT fk_docente_usuario FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- =============================
-- TABLA ADMINISTRADORES
-- =============================
CREATE TABLE Administradores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(125) NOT NULL,
    segundo_nombre VARCHAR(125),
    apellido VARCHAR(125) NOT NULL,
    segundo_apellido VARCHAR(125),
    edad int NOT NULL,
    telefono varchar(125),
    correo VARCHAR(100),
    direccion VARCHAR(150),
    cc int NOT NULL UNIQUE,
    CONSTRAINT fk_admin_usuario FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- =============================
-- TABLA MATERIAS
-- =============================

CREATE TABLE Materias (
	id_asignatura varchar(1) default'x',
    nombre_materia VARCHAR(125) NOT NULL,
    docente_id INT NULL,
    alumno_id INT NULL,
    corte1 DECIMAL(5,2) DEFAULT 0,
    corte2 DECIMAL(5,2) DEFAULT 0,
    corte3 DECIMAL(5,2) DEFAULT 0,
    CONSTRAINT fk_materia_docente FOREIGN KEY (docente_id) REFERENCES Docentes(id) ON DELETE SET NULL,
    CONSTRAINT fk_materia_alumno FOREIGN KEY (alumno_id) REFERENCES Alumnos(id) ON DELETE CASCADE
);


SELECT nombre_materia FROM Materias  where Materias.id_asignatura="x";
