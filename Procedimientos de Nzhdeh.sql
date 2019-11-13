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