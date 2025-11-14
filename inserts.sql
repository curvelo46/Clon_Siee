USE CBN;

-- =============================
-- CARRERAS
-- =============================
INSERT INTO Carreras (nombre) VALUES
('Ingeniería de Sistemas'),
('Administración de Empresas'),
('Contabilidad');

-- =============================
-- MATERIAS
-- =============================

-- Ingeniería de Sistemas
INSERT INTO Materias (nombre, carrera_id) VALUES
('Programación I', 1),
('Bases de Datos', 1),
('Redes I', 1);

-- Administración de Empresas
INSERT INTO Materias (nombre, carrera_id) VALUES
('Economía', 2),
('Mercadeo', 2),
('Finanzas I', 2);

-- Contabilidad
INSERT INTO Materias (nombre, carrera_id) VALUES
('Contabilidad I', 3),
('Matemáticas Financieras', 3),
('Auditoría', 3);

-- =============================
-- USUARIOS ADMINISTRATIVOS
-- =============================
INSERT INTO Usuarios (nombre, apellido, edad, cc, sexo, cargo, telefono, correo, direccion)
VALUES
('Carlos', 'Gómez', 40, 1000188888, 'Masculino', 'administrador', '3001000001', 'carlosg@cbn.edu', 'Cra 10 #20-10'),
('Marta', 'López', 42, 10002344, 'Femenino', 'administrador', '3001000002', 'martal@cbn.edu', 'Cra 11 #21-11'),
('Juan', 'Pérez', 38, 1000345, 'Masculino', 'administrador', '3001000003', 'juanp@cbn.edu', 'Cra 12 #22-12'),

('Ana', 'Ramírez', 35, 2000145, 'Femenino', 'registro y control', '3012000001', 'anar@cbn.edu', 'Av 5 #10-20'),
('Mario', 'Suárez', 37, 2000245, 'Masculino', 'registro y control', '3012000002', 'marios@cbn.edu', 'Av 6 #11-21'),
('Julia', 'Torres', 39, 2000345, 'Femenino', 'registro y control', '3012000003', 'juliat@cbn.edu', 'Av 7 #12-22'),

('Laura', 'Santos', 34, 3000145, 'Femenino', 'docente', '3023000001', 'lauras@cbn.edu', 'Calle 8 #15-10'),
('Pedro', 'Ruiz', 36, 3000245, 'Masculino', 'docente', '3023000002', 'pedror@cbn.edu', 'Calle 9 #16-11'),
('Luis', 'Fernández', 41, 4530003, 'Masculino', 'docente', '3023000003', 'luisf@cbn.edu', 'Calle 10 #17-12');

-- Vincular a sus roles
INSERT INTO Administradores (id) 
SELECT id FROM Usuarios WHERE cargo='administrador';
INSERT INTO RegistroYControl (id) 
SELECT id FROM Usuarios WHERE cargo='registro y control';
INSERT INTO Docentes (id) 
SELECT id FROM Usuarios WHERE cargo='docente';

-- =============================
-- ASIGNAR MATERIAS A DOCENTES
-- =============================
-- Laura enseña Ingeniería de Sistemas
INSERT INTO Docente_Materias (docente_id, materia_id) VALUES
((SELECT id FROM Usuarios WHERE nombre='Laura'), 1),
((SELECT id FROM Usuarios WHERE nombre='Laura'), 2),
((SELECT id FROM Usuarios WHERE nombre='Laura'), 3);

-- Pedro enseña Administración
INSERT INTO Docente_Materias (docente_id, materia_id) VALUES
((SELECT id FROM Usuarios WHERE nombre='Pedro'), 4),
((SELECT id FROM Usuarios WHERE nombre='Pedro'), 5),
((SELECT id FROM Usuarios WHERE nombre='Pedro'), 6);

-- Luis enseña Contabilidad
INSERT INTO Docente_Materias (docente_id, materia_id) VALUES
((SELECT id FROM Usuarios WHERE nombre='Luis'), 7),
((SELECT id FROM Usuarios WHERE nombre='Luis'), 8),
((SELECT id FROM Usuarios WHERE nombre='Luis'), 9);

-- =============================
-- ALUMNOS
-- =============================
INSERT INTO Usuarios (nombre, apellido, edad, cc, sexo, cargo, telefono, correo, direccion)
VALUES
('Andrés', 'Castro', 20, 4000451, 'Masculino', 'alumno', '3034000001', 'andresc@cbn.edu', 'Calle 1 #1-01'),
('Beatriz', 'Mejía', 19, 4004502, 'Femenino', 'alumno', '3034000002', 'beatriz@cbn.edu', 'Calle 1 #1-02'),
('Carlos', 'Vega', 21, 4000543, 'Masculino', 'alumno', '3034000003', 'carlosv@cbn.edu', 'Calle 1 #1-03'),
('Daniela', 'Gómez', 22, 4000544, 'Femenino', 'alumno', '3034000004', 'danielag@cbn.edu', 'Calle 1 #1-04'),
('Esteban', 'Núñez', 23, 4005405, 'Masculino', 'alumno', '3034000005', 'estebann@cbn.edu', 'Calle 1 #1-05'),
('Fernanda', 'Ruiz', 20, 4005406, 'Femenino', 'alumno', '3034000006', 'fernandar@cbn.edu', 'Calle 1 #1-06'),
('Gabriel', 'Pérez', 21, 4045007, 'Masculino', 'alumno', '3034000007', 'gabrielp@cbn.edu', 'Calle 1 #1-07'),
('Helena', 'Martínez', 19, 4005408, 'Femenino', 'alumno', '3034000008', 'helenam@cbn.edu', 'Calle 1 #1-08'),
('Iván', 'Sánchez', 22, 4000459, 'Masculino', 'alumno', '3034000009', 'ivans@cbn.edu', 'Calle 1 #1-09'),
('Julia', 'Torres', 20, 4001054, 'Femenino', 'alumno', '3034000010', 'juliat2@cbn.edu', 'Calle 1 #1-10'),
('Kevin', 'Mora', 21, 4001145, 'Masculino', 'alumno', '3034000011', 'kevinm@cbn.edu', 'Calle 1 #1-11'),
('Laura', 'Cano', 22, 40012445, 'Femenino', 'alumno', '3034000012', 'laurac@cbn.edu', 'Calle 1 #1-12'),
('Manuel', 'Ríos', 23, 4001453, 'Masculino', 'alumno', '3034000013', 'manuelr@cbn.edu', 'Calle 1 #1-13'),
('Natalia', 'Lozano', 20, 400454514, 'Femenino', 'alumno', '3034000014', 'natalial@cbn.edu', 'Calle 1 #1-14'),
('Oscar', 'Duarte', 21, 4001545, 'Masculino', 'alumno', '3034000015', 'oscard@cbn.edu', 'Calle 1 #1-15'),
('Paula', 'Gómez', 22, 4001456, 'Femenino', 'alumno', '3034000016', 'paulag@cbn.edu', 'Calle 1 #1-16'),
('Ricardo', 'Martín', 23, 4004517, 'Masculino', 'alumno', '3034000017', 'ricardom@cbn.edu', 'Calle 1 #1-17'),
('Sandra', 'Ruiz', 19, 4001458, 'Femenino', 'alumno', '3034000018', 'sandrar@cbn.edu', 'Calle 1 #1-18'),
('Tomás', 'Giraldo', 20, 4004519, 'Masculino', 'alumno', '3034000019', 'tomasg@cbn.edu', 'Calle 1 #1-19'),
('Valeria', 'García', 21, 4450020, 'Femenino', 'alumno', '3034000020', 'valeriag@cbn.edu', 'Calle 1 #1-20');

INSERT INTO Alumnos (id)
SELECT id FROM Usuarios WHERE cargo='alumno';

-- =============================
-- MATRÍCULA AUTOMÁTICA POR CARRERA
-- =============================
CALL matricular_alumno_en_carrera((SELECT id FROM Usuarios WHERE nombre='Andrés' and cargo="alumno"), 'Ingeniería de Sistemas');
CALL matricular_alumno_en_carrera((SELECT id FROM Usuarios WHERE nombre='Beatriz'and cargo="alumno"), 'Ingeniería de Sistemas');
CALL matricular_alumno_en_carrera((SELECT id FROM Usuarios WHERE nombre='Carlos'and cargo="alumno"), 'Ingeniería de Sistemas');
CALL matricular_alumno_en_carrera((SELECT id FROM Usuarios WHERE nombre='Daniela'and cargo="alumno"), 'Administración de Empresas');
CALL matricular_alumno_en_carrera((SELECT id FROM Usuarios WHERE nombre='Esteban'and cargo="alumno"), 'Administración de Empresas');
CALL matricular_alumno_en_carrera((SELECT id FROM Usuarios WHERE nombre='Fernanda'and cargo="alumno"), 'Administración de Empresas');
CALL matricular_alumno_en_carrera((SELECT id FROM Usuarios WHERE nombre='Gabriel'and cargo="alumno"), 'Contabilidad');
CALL matricular_alumno_en_carrera((SELECT id FROM Usuarios WHERE nombre='Helena'and cargo="alumno"), 'Contabilidad');
CALL matricular_alumno_en_carrera((SELECT id FROM Usuarios WHERE nombre='Iván'and cargo="alumno"), 'Contabilidad');

select*from docente_materias;
select*from usuarios;
