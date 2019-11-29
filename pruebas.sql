
EXEC dbo.insertarApuesta 
4,1,3,1,3,null,null,null,'1'

PROCEDURE insertarApuesta 
@cuota decimal(5,2),
@cantidad MONEY, 
@tipo CHAR(1),
@IDUsuario SMALLINT, 
@IDPartido smallint,
@golLocal tinyint,
@golVisitante tinyint,
@gol tinyint,
@puja char(1)


select * from Usuarios
select * from Partidos
select * from Apuestas_tipo3
select * from Apuestas