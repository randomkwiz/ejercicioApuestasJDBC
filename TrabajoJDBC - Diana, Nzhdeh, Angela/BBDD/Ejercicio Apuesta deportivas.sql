--Ejercicio de acceso de datos sobre las apuestas deportivas.
--Los intengrantes del grupo son: Rafa manzano, victor perez, y miguel espigares.
--
--USE master
CREATE DATABASE ApuestasDeportivas
Go
USE ApuestasDeportivas
Go

--DROP DATABASE ApuestasDeportivas
CREATE TABLE Usuarios(
	id smallint identity not null  constraint pk_id_usuarios primary key, 
	--------------------------------------------------------------------------
	saldo money not null
		constraint ck_Usuarios_saldo check(saldo >= 0),
	correo varchar(30) null,
		constraint ck_Usuarios_correo check(correo LIKE '%@%'),
		constraint UQ_Correo unique(correo),--añadido por Nzhdeh
	contraseña varchar(25) not null,
	isAdmin bit not null--añadodo por Angela
)

CREATE TABLE Partidos(
	id smallint identity not null constraint pk_id_partidos primary key,
	--------------------------------------------------------------------------
	isPeriodoApuestasAbierto bit not null,	--añadido por Angela
	--si es 0 NO esta abierto, si es 1 SI esta abierto

	golLocal tinyint not null
		constraint ck_partidos_golLocal check (golLocal >= 0),
	golVisitante tinyint not null
		constraint ck_partidos_golVisitante check (golVisitante >= 0),
	fechaInicio DATETIME not null,
	fechaFin DATETIME not null,
	nombreLocal varchar(20) not null,
	nombreVisitante varchar(20) not null,
	--modificado por angela porque no puedes poner el maximo de apuesta en la entidad ApuestaTipoX
	maximoApuestaTipo1 money not null,
		constraint ck_maxApuestasTipo1 check (maximoApuestaTipo1 > 0),
	maximoApuestaTipo2 money not null,
	constraint ck_maxApuestasTipo2 check (maximoApuestaTipo2 > 0),
	maximoApuestaTipo3 money not null,
	constraint ck_maxApuestasTipo3 check (maximoApuestaTipo3 > 0)
	
)

CREATE TABLE Ingresos(
	id smallint identity not null constraint pk_id_ingresos primary key,
	--------------------------------------------------------------------------
	cantidad money not null,
	descripcion varchar(100) null,
	id_usuario smallint not null,
	constraint fk_id_usuario_Ingresos foreign key (id_usuario) references Usuarios(id)
)

--DROP TABLE Apuestas
CREATE TABLE Apuestas(
	id smallint identity not null constraint pk_id_apuestas primary key,
	--------------------------------------------------------------------------
	cuota decimal(5,2) not null
		constraint ck_Apuestas_cuota check(cuota > 1),
	cantidad money not null,
		--constraint ck_Apuestas_cantidad check(cantidad > 0),
	tipo char(1) not null
		constraint ck_Apuestas_tipo check(tipo in ('1','2','3')),
	fechaHora datetime not null,
	--------------------------------------------------------------------------
	id_usuario smallint not null
	constraint fk_id_usuarios foreign key (id_usuario) references Usuarios(id),
	id_partido smallint not null
	constraint fk_id_partidos foreign key (id_partido) references Partidos(id)
)

CREATE TABLE Apuestas_tipo1(
	id smallint not null constraint pk_id_apuestas_tipo1 primary key,
	--------------------------------------------------------------------------
	
	golLocal tinyint null
		constraint ck_Apuestas_tipo1_golLocal check(golLocal >= 0),
	golVisitante tinyint null
		constraint ck_Apuestas_tipo1_golVisitante check(golVisitante >= 0),
	constraint fk_Apuestas_tipo1 foreign key (id) references Apuestas(id)
	on delete cascade on update cascade
)

CREATE TABLE Apuestas_tipo2(
	id smallint not null constraint pk_id_apuestas_tipo2 primary key,
	--------------------------------------------------------------------------
	
	gol tinyint not null
		constraint ck_Apuestas_tipo2_gol check(gol >= 0),
	puja char(1) not null
		constraint ck_Apuestas_tipo2_puja check(puja in ('1','2')),
	constraint fk_Apuestas_tipo2 foreign key (id) references Apuestas(id)
	on delete cascade on update cascade
)

CREATE TABLE Apuestas_tipo3(
	id smallint not null constraint pk_id_apuestas_tipo3 primary key,
	--------------------------------------------------------------------------
	
	puja char(1) not null
		constraint ck_Apuestas_tipo3_puja check(puja in ('1','x','2'))
	constraint fk_Apuestas_tipo3 foreign key (id) references Apuestas(id)
	on delete cascade on update cascade
)
go


set dateformat 'ymd'
--Inserts
INSERT INTO Usuarios
VALUES (500,'aabb@gmail.com','1234',0),
(5000,'bbb@gmail.com','5678',0),
(8000,'gmasd@gmail.com','9123',1)
go

INSERT INTO Ingresos (cantidad,descripcion,id_usuario)
VALUES (300,'Ingreso',1),(-300,'Reintegro',2),(2000,'Ingreso',3)
go

INSERT INTO Partidos 
VALUES(1,1,2,'2019-01-12 12:00','2019-01-12 13:45','Sevilla','Betis', 5000,1000,6000),
(0,1,5,'2019-01-13 13:00','2019-01-13 14:45','Carmona','Coria',6000,12000,500),
(1,1,2,'2019-03-03 22:00','2019-03-03 23:45','Barcelona','Madrid',6000,9054,6987)
go

INSERT INTO Apuestas
VALUES (1.2,50,1,'2019-01-11 12:00',1,1),
(2.0,20,2,'2019-01-12 12:00',2,2),
(2.50,300,3,'2019-02-03 12:00',3,3),
(1.2,50,1,'2019-01-11 12:00',1,3),
(2.0,20,2,'2019-01-12 12:00',2,2),
(2.50,300,3,'2019-02-03 12:00',3,1)
go

INSERT INTO Apuestas_tipo1
VALUES (1,3,2)
go

INSERT INTO Apuestas_tipo2
VALUES (1,2,'2')
go

INSERT INTO Apuestas_tipo3
VALUES (1,'x')