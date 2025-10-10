
INSERT INTO Alumnos (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc, sexo)
VALUES 
('Andres','camilo','curvelo','diaz',18,'3243582032','andrescurvelodiaz@gmail.com','calle29',1084451641,"hombre"),
('jose','billero','granado','martines',20,'3243582032','','la libertador',900224,"hombre"),
('hilary','fontalvo','ospina','ospina',18,'3243582032','andrescamilo@gmail.com','11000232',4234,"mujer"),
('andrea','martines','curvelo','jose',10,'3243582032','andrescamilo@gmail.com','11000232',434,"mujer"),
('milagro','la doña','peleona','de la esquina',70,'3243582032','andrescamilo@gmail.com','11000232',4,"mujer"),
('to','la doña','peleona','de la esquina',7,'1232323','andrescamilo@gmail.com','11000232',008,"hombre");


-- =============================
-- INSERTAR DOCENTES
-- (Todos empiezan con 'sin asignatura')
-- =============================
INSERT INTO Docentes (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc)
VALUES
('luiz','','carlos','',40,'123456789','andres@gmail.com','direccion',12123),
('doctora','','tatiana','',48,'987654321','camila@gmail.com','direccion',121231),
('profesora','','maria','',28,'456789123','camila@gmail.com','direccion',12131);


-- =============================
-- INSERTAR ADMINISTRADORES
-- =============================
INSERT INTO Administradores (nombre, segundo_nombre, apellido, segundo_apellido, edad, telefono, correo, direccion, cc)
VALUES
('jose','','curvelo','',18,'12321323','andrescamilo@gmail.com','direccion',34),
('fernanda','','fernanda','',18,'123223','camila@gmail.com','direccion',4545);


-- =============================
-- INSERTAR MATERIAS
-- =============================



select*from materias
