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
	select * from Partidos
	where GETDATE() between DATEADD(day, -2, fechaInicio) and DATEADD(minute, -10, fechaFin)
end
go

--prototipo: procedure PartidosDisponibles
--comentarios: un procidimeinto para ver los partidos que estan disponibles para apostar
--precondiciones: no hay
--entradas: no hay
--salidas: no hay
--entradas/salidas: no hay 
--postcondiciones: no hay, solo se muestran los partidos disponibles para apostar
go
create procedure RetirarDinero(@idUsuario as int,) as
begin
	insert into ()
end
--select GETDATE()
--SELECT DATEDIFF(minute,      '2005-12-31 23:59:59.9999999', '2006-01-01 00:00:00.0000000');

--SELECT DATEADD(minute, -10, '2011-05-10 11:00:23.927') AS dt 
--SELECT DATEADD(day, -2, '2011-05-10 11:00:23.927') AS dt 
select * from Usuarios
select * from Ingresos