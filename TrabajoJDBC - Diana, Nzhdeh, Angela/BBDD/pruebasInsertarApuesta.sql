
use ApuestasDeportivas
go
select * from Partidos

select * from Usuarios

select * from Ingresos

select * from Apuestas_tipo1
select * from Apuestas_tipo2
select * from Apuestas_tipo3

select * from Apuestas where id_partido = 4


set dateformat 'ymd'

select * from Apuestas as a
full join Apuestas_tipo1 as at1
on a.id = at1.id
full join Apuestas_tipo2 as at2
on a.id = at2.id
full join Apuestas_tipo3 as at3
on a.id = at3.id

where DAY(fechaHora) = 11
and
MONTH(fechaHora) = 01
and
YEAR(fechaHora) = 2019
and 
id_usuario = 1



EXEC dbo.insertarApuesta 
@cuota = 4.2,
@cantidad = 50,
@tipo = 3,
@IdUsuario = 1,
@IdPartido  = 4,
@puja = 1,
@golLocal = null,
@golVisitante = null,
@gol = null


select dbo.COMPROBARMAXIMO (4,3)

select * from Partidos where id = 4



						

SELECT maximoApuestaTipo3
					FROM Partidos
					WHERE ID = 4 
				



INSERT Apuestas (cuota, cantidad, tipo, fechaHora, id_usuario, id_partido)
	VALUES (1.5, 25, 3, CURRENT_TIMESTAMP, 1, 3)


	declare @last smallint
	set @last = @@IDENTITY
	print @last
	--INSERT Apuestas_tipo1(id, golLocal, golVisitante)
	--	VALUES (@last, 1, 2)

	--insert Apuestas_tipo2(id, gol, puja)
	--values(@last, 2,1)

	insert Apuestas_tipo3(id, puja)
	values(@last, 'x')


	select * from Partidos