-- =============================
-- USUARIOS
-- =============================

-- Alumno
INSERT INTO Usuarios (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, sexo, user_, contrasena_hash, cargo)
VALUES ('Carlos', 'Andrés', 'Ramírez', 'Pérez', 20, '3001234567', 'carlos@email.com', 'Cra 10 #20-30', 123456789, 'masculino', 'carlosr', 'hashed_pass_1', 'alumno');

-- Docente
INSERT INTO Usuarios (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, sexo, user_, contrasena_hash, cargo)
VALUES ('Laura', 'María', 'Gómez', 'Lozano', 35, '3012345678', 'laura@email.com', 'Calle 45 #12-34', 987654321, 'femenino', 'laurag', 'hashed_pass_2', 'docente');

-- Administrador
INSERT INTO Usuarios (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, sexo, user_, contrasena_hash, cargo)
VALUES ('Mario', NULL, 'Sánchez', 'Torres', 40, '3023456789', 'mario@email.com', 'Av 6 #15-89', 112233445, 'masculino', 'marios', 'hashed_pass_3', 'administrador');

-- =============================
-- ROLES
-- =============================

-- Vincular usuario como alumno (id = 1)
INSERT INTO Alumnos (id) VALUES (1);

-- Vincular usuario como docente (id = 2)
INSERT INTO Docentes (id) VALUES (2);

-- Vincular usuario como administrador (id = 3)
INSERT INTO Administradores (id) VALUES (3);

-- =============================
-- CARRERAS
-- =============================

INSERT INTO Carreras (nombre) VALUES 
('Ingeniería de Sistemas'),
('Administración de Empresas');

-- =============================
-- MATERIAS
-- =============================

INSERT INTO Materias (nombre) VALUES 
('Programación I'),
('Bases de Datos'),
('Contabilidad'),
('Economía');

-- =============================
-- CARRERA - MATERIAS
-- =============================

-- Ingeniería de Sistemas: Programación I y Bases de Datos
INSERT INTO Carrera_Materias (carrera_id, materia_id) VALUES 
(1, 1),
(1, 2);

-- Administración de Empresas: Contabilidad y Economía
INSERT INTO Carrera_Materias (carrera_id, materia_id) VALUES 
(2, 3),
(2, 4);

-- =============================
-- DOCENTE - MATERIAS
-- =============================

-- La docente da Programación I y Bases de Datos
INSERT INTO Docente_Materias (docente_id, materia_id) VALUES 
(2, 1),
(2, 2);

-- =============================
-- ALUMNO - MATERIAS (Con notas)
-- =============================

-- Carlos está inscrito en Programación I y Bases de Datos
INSERT INTO Alumno_Materias (alumno_id, materia_id, corte1, corte2, corte3) VALUES 
(1, 1, 4.5, 4.0, 3.8),
(1, 2, 4.2, 3.9, 4.0);


select*from usuarios