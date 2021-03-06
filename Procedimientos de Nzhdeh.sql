set dateformat 'ymd'

--prototipo: procedure PartidosDisponibles
--comentarios: un procidimeinto para ver los partidos que estan disponibles para apostar
--precondiciones: no hay
--entradas: no hay
--salidas: no hay
--entradas/salidas: no hay 
--postcondiciones: no hay, solo se muestran los partidos disponibles para apostar
go
create procedure PartidosDisponibles as
begin
	select nombreLocal,nombreVisitante,fechaInicio from Partidos
	where (GETDATE() between DATEADD(day, -2, fechaInicio) and DATEADD(minute, -10, fechaFin)) and isPeriodoApuestasAbierto=0
end
go

--prototipo: procedure RetirarDinero(@idUsuario as int,@Cantidad as money,@Descripcion as varchar(100))
--comentarios: un procidimeinto para hacer un ingreso
--precondiciones: el id de usuario tiene que ser correcto y la cantidad un numero positivo
--entradas: idUsuario entero, dinero a retirar money, descripion cadena
--salidas: no hay
--entradas/salidas: no hay
--postcondiciones: la tabla de ingresos queda con una fila mas
go
create procedure RetirarDinero(@idUsuario as int,@Cantidad as money,@Descripcion as varchar(100)) as
begin
	insert into Ingresos(cantidad,descripcion,id_usuario)
	values (@idUsuario,@Cantidad,@Descripcion)
end
go

--prototipo: procedure Movimientos
--comentarios: un procidimeinto para obtener todos los movimientos del usuario incluyendo las apuestas realizadas y ganadas
--precondiciones: el id de usuario tiene que ser correcto
--entradas: idUsuario entero
--salidas: no hay
--entradas/salidas: no hay
--postcondiciones: no hay, solo muestra la lista de los movimientos de una cuenta de un usuario
create procedure Movimientos(@idUsuario as int) as
begin
	select * from Ingresos
	where id_usuario=@idUsuario
end
go

--select GETDATE()
--SELECT DATEDIFF(minute,      '2005-12-31 23:59:59.9999999', '2006-01-01 00:00:00.0000000');

--SELECT DATEADD(minute, -10, '2011-05-10 11:00:23.927') AS dt 
--SELECT DATEADD(day, -2, '2011-05-10 11:00:23.927') AS dt 
select * from Usuarios
select * from Ingresos

--consultar las apuestas de un partido, indicando la cantidad de dinero apostado a cada posible resultado
--en teoria son 3 consultas
--tipo 1 agrupar por los dos resultados
--tipo 2 agrupar por el resultado
--tipo 3 agrupar por el resultado

--necesito una funcion por cada tipo que me devuelva la cantidad apostada por cada resulatado posible

--go
--create procedure PR_DineroApostadoPorUnPosibleResultadoDeUnPartido(@IDPartido int)
--as
--begin
--	select distinct A.id_partido,AT1.CantidadTotalTipo1,AT2.CantidadTotalTipo2,AT3.CantidadTotalTipo3 from Apuestas as A
--	left join --por si no hay apuestas del tipo 1
--	(
--		select id_partido,SUM(cantidad) as CantidadTotalTipo1 from Apuestas
--		where tipo=1 and id_partido=@IDPartido
--		group by id_partido
--	) as AT1 on A.id_partido=AT1.id_partido
--	left join --por si no hay apuestas del tipo 2
--	(
--		select id_partido,SUM(cantidad) as CantidadTotalTipo2 from Apuestas
--		where tipo=2 and id_partido=@IDPartido
--		group by id_partido
--	) as AT2 on A.id_partido=AT2.id_partido
--	left join --por si no hay apuestas del tipo 3
--	(
--		select id_partido,SUM(cantidad) as CantidadTotalTipo3 from Apuestas
--		where tipo=3 and id_partido=@IDPartido
--		group by id_partido
--	) as AT3 on A.id_partido=AT3.id_partido
--	where A.id_partido=@IDPartido
--end
--go

go
create or alter procedure PR_DineroApostadoPorUnPosibleResultadoDeUnPartido(@IDPartido int)
as
begin
	select AT1.golLocal,AT1.golVisitante,AT2.gol,AT2.puja as PujaTipo2,AT3.puja as PujaTipo3,SUM(A.cantidad) as CantidatTotal from Apuestas as A
	left join Apuestas_tipo1 as AT1 on A.id=AT1.id
	left join Apuestas_tipo2 as AT2 on A.id=AT2.id
	left join Apuestas_tipo3 as AT3 on A.id=AT3.id
	where A.id_partido=@IDPartido
	group by AT1.golLocal,AT1.golVisitante,AT2.gol,AT2.puja,AT3.puja
end
go
--select * from Apuestas
execute dbo.PR_DineroApostadoPorUnPosibleResultadoDeUnPartido 1--probar que sean los resultados iguales pa ver si suma bien
execute dbo.PR_DineroApostadoPorUnPosibleResultadoDeUnPartido 2
execute dbo.PR_DineroApostadoPorUnPosibleResultadoDeUnPartido 3
go
select * from Apuestas_tipo2
select * from Apuestas_tipo1
select * from Apuestas_tipo3
select * from Apuestas
where tipo=1
set dateformat 'ymd'
insert into Apuestas(cuota,cantidad,tipo,fechaHora,id_usuario,id_partido)
values(1.20,35,2,'2019-01-11 12:00:00.000',2,1),
		(1.20,45,3,'2019-01-11 12:00:00.000',2,1)
insert into Apuestas_tipo1(id,apuestasMáximas,golLocal,golVisitante)
values(10,500.0,3,1),
(4,500.0,3,1),
		(7,700.0,2,2)
insert into Apuestas_tipo2(id,apuestasMáximas,gol,puja)
values(10,5000.0,4,1)
insert into Apuestas_tipo3(id,apuestasMáximas,puja)
values(11,5000.0,1)

--select A2.id_partido,SUM(A2.cantidad) as CantidadTotalTipo2,A1.CantidadTotalTipo1 from Apuestas as A2
-- inner join 
-- (
--	select id_partido,SUM(cantidad) as CantidadTotalTipo1 from Apuestas
--	where tipo=1 and id_partido=1
--	group by id_partido) as A1 on A2.id_partido=A1.id_partido
--where tipo=2
--group by A2.id_partido,A1.CantidadTotalTipo1

--select * from Apuestas
----la suma total de las apuestas del tipo 1 de un partido dado
--select id_partido,SUM(cantidad) as CantidadTotalTipo1 from Apuestas
--where tipo=1 and id_partido=1
--group by id_partido

----la suma total de las apuestas del tipo 2 de un partido dado
--select id_partido,SUM(cantidad) as CantidadTotalTipo2 from Apuestas
--where tipo=2 and id_partido=1
--group by id_partido

----la suma total de las apuestas del tipo 2 de un partido dado
--select id_partido,SUM(cantidad) as CantidadTotalTipo2 from Apuestas
--where tipo=3 and id_partido=1
--group by id_partido

---------------------------------------------------------------
--select distinct A.id_partido,AT1.CantidadTotalTipo1,AT2.CantidadTotalTipo2,AT3.CantidadTotalTipo3 from Apuestas as A
--left join --por si no hay apuestas del tipo 1
--(
--select id_partido,SUM(cantidad) as CantidadTotalTipo1 from Apuestas
--where tipo=1 and id_partido=1
--group by id_partido
--) as AT1 on A.id_partido=AT1.id_partido
--left join --por si no hay apuestas del tipo 2
--(
--select id_partido,SUM(cantidad) as CantidadTotalTipo2 from Apuestas
--where tipo=2 and id_partido=1
--group by id_partido
--) as AT2 on A.id_partido=AT2.id_partido
--left join --por si no hay apuestas del tipo 3
--(
--select id_partido,SUM(cantidad) as CantidadTotalTipo3 from Apuestas
--where tipo=3 and id_partido=1
--group by id_partido
--) as AT3 on A.id_partido=AT3.id_partido
--where A.id_partido=1