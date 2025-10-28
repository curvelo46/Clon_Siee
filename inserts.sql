-- =============================
-- USUARIOS
-- =============================

-- Alumno
INSERT INTO Usuarios (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, sexo, user_, contrasena_hash, cargo)
VALUES ('Carlos', 'Andrés', 'Ramírez', 'Pérez', 20, '3001234567', 'carlos@email.com', 'Cra 10 #20-30', 123456789, 'masculino', 'carlosr', 'hashed_pass_1', 'alumno');

INSERT INTO Usuarios (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, sexo, user_, contrasena_hash, cargo)
VALUES ('los', 'And', 'rez', 'Pér', 20, '3001234567', 'carlos@email.com', 'Cra 10 #20-30', 123489, 'masculino', 'osr', 'hashed_pass_1', 'alumno');

-- Docente
INSERT INTO Usuarios (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, sexo, user_, contrasena_hash, cargo)
VALUES ('Laura', 'María', 'Gómez', 'Lozano', 35, '3012345678', 'laura@email.com', 'Calle 45 #12-34', 987654321, 'femenino', 'laurag', 'hashed_pass_2', 'docente');

INSERT INTO Usuarios (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, sexo, user_, contrasena_hash, cargo)
VALUES ('Lara', 'ría', 'mez', 'Loza', 35, '30123458', 'laura@email.com', 'Calle 45 #12-34', 987321, 'femenino', 'larag', 'hashed_pass_2', 'docente');

-- Administrador
INSERT INTO Usuarios (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, sexo, user_, contrasena_hash, cargo)
VALUES ('Mario', NULL, 'Sánchez', 'Torres', 40, '3023456789', 'mario@email.com', 'Av 6 #15-89', 112233445, 'masculino', 'marios', 'hashed_pass_3', 'administrador');

-- =============================
-- ROLES
-- =============================

-- Vincular usuario como alumno (id = 1)
INSERT INTO Alumnos (id) VALUES (1);
INSERT INTO Alumnos (id) VALUES (2);

-- Vincular usuario como docente (id = 2)
INSERT INTO Docentes (id) VALUES (3);
INSERT INTO Docentes (id) VALUES (4);

-- Vincular usuario como administrador (id = 3)
INSERT INTO Administradores (id) VALUES (5);

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

INSERT INTO Docente_Materias (docente_id, materia_id) VALUES (3, 1); -- Laura - Programación I
INSERT INTO Docente_Materias (docente_id, materia_id) VALUES (3, 2); -- Laura - Programación I
INSERT INTO Docente_Materias (docente_id, materia_id) VALUES (4, 1); -- Lara - Programación I



-- =============================
-- ALUMNO - MATERIAS (Con notas)
-- =============================

-- Carlos está con Laura
INSERT INTO Alumno_Materias (alumno_id, docente_materia_id, corte1) VALUES (1, 1, 4.5);

-- Pedro está con Lara
INSERT INTO Alumno_Materias (alumno_id, docente_materia_id, corte1) VALUES (2, 2, 3.8);


select*from alumno_materias;
select*from usuarios;

