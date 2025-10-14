DROP DATABASE IF EXISTS CBN;
CREATE DATABASE CBN;
USE CBN;

-- =============================
-- USUARIOS (Base de todo)
-- =============================
CREATE TABLE Usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(125) NOT NULL,
    segundo_nombre VARCHAR(125),
    apellido VARCHAR(125) NOT NULL,
    segundo_apellido VARCHAR(125),
    edad INT NOT NULL,
    telefono VARCHAR(50),
    correo VARCHAR(125),
    direccion VARCHAR(150),
    cc BIGINT NOT NULL UNIQUE,
    sexo VARCHAR(25),
    user_ VARCHAR(50) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL, -- Aquí debe ir el hash
    cargo ENUM('alumno','docente','administrador','master') NOT NULL
);

-- =============================
-- ALUMNOS
-- =============================
CREATE TABLE Alumnos (
    id INT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- =============================
-- DOCENTES
-- =============================
CREATE TABLE Docentes (
    id INT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- =============================
-- ADMINISTRADORES
-- =============================
CREATE TABLE Administradores (
    id INT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- =============================
-- CARRERAS
-- =============================
CREATE TABLE Carreras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(125) NOT NULL UNIQUE
);

-- =============================
-- MATERIAS
-- =============================
CREATE TABLE Materias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(125) NOT NULL
);

-- =============================
-- RELACIÓN CARRERA-MATERIAS
-- =============================
CREATE TABLE Carrera_Materias (
    carrera_id INT,
    materia_id INT,
    PRIMARY KEY (carrera_id, materia_id),
    FOREIGN KEY (carrera_id) REFERENCES Carreras(id) ON DELETE CASCADE,
    FOREIGN KEY (materia_id) REFERENCES Materias(id) ON DELETE CASCADE
);

-- =============================
-- RELACIÓN DOCENTES-MATERIAS
-- =============================
CREATE TABLE Docente_Materias (
    docente_id INT,
    materia_id INT,
    PRIMARY KEY (docente_id, materia_id),
    FOREIGN KEY (docente_id) REFERENCES Docentes(id) ON DELETE CASCADE,
    FOREIGN KEY (materia_id) REFERENCES Materias(id) ON DELETE CASCADE
);

-- =============================
-- RELACIÓN ALUMNOS-MATERIAS (con calificaciones)
-- =============================
CREATE TABLE Alumno_Materias (
    alumno_id INT,
    materia_id INT,
    corte1 DECIMAL(5,2) DEFAULT 0,
    corte2 DECIMAL(5,2) DEFAULT 0,
    corte3 DECIMAL(5,2) DEFAULT 0,
    PRIMARY KEY (alumno_id, materia_id),
    FOREIGN KEY (alumno_id) REFERENCES Alumnos(id) ON DELETE CASCADE,
    FOREIGN KEY (materia_id) REFERENCES Materias(id) ON DELETE CASCADE
);

SELECT m.nombre AS Materia, c.nombre AS Carrera
FROM Carrera_Materias cm
JOIN Materias m ON cm.materia_id = m.id
JOIN Carreras c ON cm.carrera_id = c.id
WHERE c.nombre = 'Ingeniería de Sistemas';

select*from carrera_materias
