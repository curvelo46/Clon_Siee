create database CBN;
use CBN;

create table Alumnos (id int primary key,nombre varchar(25) not null,segundo_nombre varchar(25),apellido varchar(25)not null,segundo_apellido varchar(25)
,edad varchar(2) not null,telefono VARCHAR(20),correo VARCHAR(100),direccion VARCHAR(150),cc VARCHAR(15) NOT NULL UNIQUE,sexo varchar(25));

create table Docentes (id int primary key,nombre varchar(25) not null,segundo_nombre varchar(25),apellido varchar(25)not null,segundo_apellido varchar(25)
,edad varchar(2) not null,materia varchar(25),telefono VARCHAR(20),correo VARCHAR(100),direccion VARCHAR(150),cc VARCHAR(15) NOT NULL UNIQUE);

create table Administradores (id int primary key,nombre varchar(25) not null,segundo_nombre varchar(25),apellido varchar(25)not null,segundo_apellido varchar(25)
,edad varchar(2) not null,telefono VARCHAR(20),correo VARCHAR(100),direccion VARCHAR(150),cc VARCHAR(15) NOT NULL UNIQUE);

create table usuarios(id int primary key, user_ varchar(25),contraseña varchar(50),cargo varchar(125));

create table java(nombre varchar(25),corte int, nota double);
create table poo(nombre varchar(25),corte int, nota double);
create table materias_net(nombre varchar(25),corte int, nota double);

create table materias(nombre varchar(125), materia varchar(125) primary key);

insert into materias values ('carlos','java');
insert into materias values ('tatiana','materias_net');
insert into materias values ('maria','poo');

insert  into Alumnos values(1,'Andres','camilo','curvelo','diaz','18','3243582032','andrescurvelodiaz@gmail.com','calle29 el resto no','1084451641',"hombre");
insert  into Alumnos values(2,'jose','billero','granado','martines','20','3945768123','','la livertador','4900224',"hombre");
insert  into Alumnos values(3,'hilary','fontalvo','ospina','ospina','18','12321323','andrescamilo@gmail.com','11000232','4234',"mujer");
insert  into Alumnos values(4,'andrea','martines','curvelo','jose','10','1232323','andrescamilo@gmail.com','11000232','434',"mujer");
insert  into Alumnos values(5,'milagro','la doña','peleona',' de la esquina','70','1232323','andrescamilo@gmail.com','11000232','4',"mujer");
insert  into Alumnos values(11,'to','la doña','peleona',' de la esquina','70','1232323','andrescamilo@gmail.com','11000232','008',"hombre");


insert  into Docentes values(1,'luiz','','carlos','','40','12321323','andres@gmail.com','11000232','3434','12123');
insert  into Docentes values(2,'doctora',' ','tatiana',' ','48','123223','camila@gmail.com','11000','sdf','121231');
insert  into Docentes values(3,'profesora',' ','maria',' ','28','123223','camila@gmail.com','11000','sdf','12131');

insert  into Administradores values(1,'jose','','curvelo','','18','12321323','andrescamilo@gmail.com','11000232','34');
insert  into Administradores values(2,'fernanda','','fernanda','','18','123223','camila@gmail.com','11000','4545');


insert into usuarios values(6,'carlos','123','docente');
insert into usuarios values(7,'tatiana','123','docente');
insert into usuarios values(8,'maria','123','docente');
insert into usuarios values(9,'yo','007','administrador');

DELIMITER $$

CREATE TRIGGER after_insert_alumno
AFTER INSERT ON Alumnos
FOR EACH ROW
BEGIN
      INSERT INTO java (nombre, corte, nota) VALUES (NEW.nombre, 1, 0.0);
    INSERT INTO  materias_net(nombre, corte, nota) VALUES (NEW.nombre, 1, 0.0);
    INSERT INTO poo (nombre, corte, nota) VALUES (NEW.nombre, 1, 0.0);
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER usuarios
AFTER INSERT ON Alumnos
FOR EACH ROW
BEGIN
    INSERT INTO usuarios (id, user_, contraseña, cargo)
    VALUES (NEW.id, NEW.nombre, NEW.cc, 'alumno');
END$$

DELIMITER ;


DELIMITER $$

CREATE TRIGGER sincronisacion
AFTER DELETE ON Alumnos
FOR EACH ROW
BEGIN
    DELETE FROM java WHERE nombre = OLD.nombre;
END$$

DELIMITER ;



