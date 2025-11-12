INSERT INTO Usuarios(nombre, segundo_nombre, apellido, segundo_apellido,edad, cc, sexo, cargo,telefono, correo, direccion)
VALUES
('Carlos', NULL, 'Ramírez', NULL, 20, 123456789, 'masculino', 'alumno',
 '3001234567', 'carlos@mail.com', 'Cra 10 #20-30'),

('Pedro',  NULL, 'Ruiz',    NULL, 21, 123456790, 'masculino', 'alumno',
 '3001234568', 'pedro@mail.com',  'Cra 11 #21-31'),

('Laura',  NULL, 'Gómez',   NULL, 35, 987654321, 'femenino',  'docente',
 '3012345678', 'laura@mail.com',  'Calle 45 #12-34'),

('Mario',  NULL, 'Sánchez', NULL, 40, 112233445, 'masculino', 'administrador',
 '3023456789', 'mario@mail.com',  'Av 6 #15-89'),
 
 ('Mandres',  NULL, 'Sánchez', NULL, 40, 112245, 'masculino', 'registro y control',
 '3023456789', 'mario@mail.com',  'Av 6 #15-89');

-- =============================
-- ROLES
-- =============================

INSERT INTO Alumnos (id) VALUES (1), (2);
INSERT INTO Docentes (id) VALUES (3);
INSERT INTO Administradores (id) VALUES (4);
INSERT INTO RegistroYControl (id) VALUES (4);



-- =============================
-- CARRERAS
-- =============================

INSERT INTO Carreras (nombre) VALUES
('Ingeniería de Sistemas'),
('Administración de Empresas');

-- =============================
-- MATERIAS
-- =============================

INSERT INTO Materias (nombre, carrera_id) VALUES
('Programación I', 1),
('Bases de Datos', 1),
('Contabilidad', 2),
('Economía', 2);





-- =============================
-- DOCENTE - MATERIAS
-- =============================

-- La docente da Programación I y Bases de Datos

INSERT INTO Docente_Materias (docente_id, materia_id) VALUES
(3, 1), (3, 2), (3, 3), (3, 4);

INSERT INTO Alumno_Materias (alumno_id, docente_materia_id, corte1) VALUES
(1, 1, 0.0),
(2, 1, 0.0);


select*from docente_materias;
select*from alumno_materias;
select* from docentes;
select*from materias;
select*from alumnos;
select*from Reportes;
select*from usuarios;	
select*from carreras;
select*from administradores;
select*from RegistroYControl;


 
 
 
 
 
