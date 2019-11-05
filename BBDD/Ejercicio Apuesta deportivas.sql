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
	contraseña varchar(25) not null
)

CREATE TABLE Partidos(
	id smallint identity not null constraint pk_id_partidos primary key,
	--------------------------------------------------------------------------
	golLocal tinyint not null
		constraint ck_partidos_golLocal check (golLocal >= 0),
	golVisitante tinyint not null
		constraint ck_partidos_golVisitante check (golVisitante >= 0),
	fechaInicio DATETIME not null,
	fechaFin DATETIME not null,
	nombreLocal varchar(20) not null,
	nombreVisitante varchar(20) not null,
)

CREATE TABLE Ingresos(
	id smallint identity not null constraint pk_id_ingresos primary key,
	--------------------------------------------------------------------------
	cantidad int not null,
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
	cantidad int not null,
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
	apuestasMáximas money not null
		constraint ck_partidos_tipo2_apuestasMáximas check (apuestasMáximas > 0),
	golLocal tinyint null
		constraint ck_Apuestas_tipo1_golLocal check(golLocal >= 0),
	golVisitante tinyint null
		constraint ck_Apuestas_tipo1_golVisitante check(golVisitante >= 0),
	constraint fk_Apuestas_tipo1 foreign key (id) references Apuestas(id)
)

CREATE TABLE Apuestas_tipo2(
	id smallint not null constraint pk_id_apuestas_tipo2 primary key,
	--------------------------------------------------------------------------
	apuestasMáximas money not null
		constraint ck_Apuesta_tipo2_apuestasMáximas check (apuestasMáximas > 0),
	gol tinyint not null
		constraint ck_Apuestas_tipo2_gol check(gol >= 0),
	puja char(1) not null
		constraint ck_Apuestas_tipo2_puja check(puja in ('1','2')),
	constraint fk_Apuestas_tipo2 foreign key (id) references Apuestas(id)
)

CREATE TABLE Apuestas_tipo3(
	id smallint not null constraint pk_id_apuestas_tipo3 primary key,
	--------------------------------------------------------------------------
	apuestasMáximas money not null
		constraint ck_partidos_tipo3_apuestasMáximas check (apuestasMáximas > 0),
	puja char(1) not null
		constraint ck_Apuestas_tipo3_puja check(puja in ('1','x','2'))
	constraint fk_Apuestas_tipo3 foreign key (id) references Apuestas(id)
)
