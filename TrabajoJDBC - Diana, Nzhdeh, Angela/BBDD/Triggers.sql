USE ApuestasDeportivas
--USE master
--En la tabla ingresos cuando se hace un insert hay que hacer un trigger que aumente el saldo del usuario 
--(Cuando se retira también funcionaria igual).
--
GO
CREATE TRIGGER modificarSaldo ON Ingresos
AFTER INSERT AS
	BEGIN
		UPDATE Usuarios
		SET saldo = U.saldo + I.cantidad
		FROM Usuarios AS U
		INNER JOIN inserted AS I ON U.id = I.id_usuario
		WHERE U.id = I.id_usuario
	END
GO

-- Cuando una apuesta está en la BBDD, no se puede eliminar ni modificar. Un trigger ayudaría
GO
CREATE TRIGGER noModificarApuestas ON Apuestas
INSTEAD OF DELETE, UPDATE AS
	--BEGIN
		THROW 51000, 'La apuesta no se puede ni modificar ni eliminar cuando ya se ha realizado', 1
		ROLLBACK
	--END
GO

-- Para poder apostar, el tiempo del partido debe estar abierto (que la fecha de la apuesta este entre fechaHoraInicio y 
--fechaHoraFin del partido), trigger o procedimiento almacenado (no estoy seguro)
--CREATE TRIGGER partidoAbiertoApuesta ON Apuestas
--AFTER INSERT AS 
--	BEGIN
--		IF EXISTS (SELECT * FROM inserted AS I
--		INNER JOIN Partidos AS P ON I.id_partido = P.id
--		WHERE isPeriodoApuestasAbierto=0)
--		--I.fechaHora NOT BETWEEN DATEADD(DAY, -2, P.fechaInicio) AND DATEADD(MINUTE, -10, P.fechaFin)) 
--		BEGIN
--			RAISERROR ('La apuesta para ese partido no ha empezado o se ha cerrado', 16,1)
--			ROLLBACK
--		END
--	END


--Procedimiento que comprueba que una apuesta es ganada o no
GO
CREATE PROCEDURE comprobarApuestaAcertada @idApuesta INT, @tipo CHAR(1), @acertada BIT OUTPUT
AS
BEGIN
--DECLARE @acertada BIT
SET @acertada = 0
	IF(@tipo = 1)
	BEGIN
		IF EXISTS (SELECT * FROM Apuestas AS A
		INNER JOIN Partidos AS P ON A.id_partido = P.id
		INNER JOIN Apuestas_tipo1 AS A1 ON  A1.id = A.id
		WHERE A.id = @idApuesta AND A1.golLocal = P.golLocal AND A1.golVisitante = P.golVisitante)
		BEGIN
			SET @acertada = 1
		END
	END

	IF(@tipo = 2)
	BEGIN
		IF EXISTS (SELECT * FROM Apuestas AS A
		INNER JOIN Partidos AS P ON A.id_partido = P.id
		INNER JOIN Apuestas_tipo2 AS A2 ON  A2.id = A.id
		WHERE A.id = @idApuesta AND (A2.puja = '1' AND A2.gol = P.golLocal) OR (A2.puja = '2' AND A2.gol = P.golVisitante))
		BEGIN
			SET @acertada = 1
		END
	END

	IF(@tipo = 3)
	BEGIN
		IF EXISTS (SELECT * FROM Apuestas AS A
		INNER JOIN Partidos AS P ON A.id_partido = P.id
		INNER JOIN Apuestas_tipo3 AS A3 ON  A3.id = A.id
		WHERE A.id = @idApuesta AND (A3.puja = '1' AND P.golLocal > P.golVisitante) OR (A3.puja = '2' AND P.golLocal < P.golVisitante) OR (A3.puja = 'x' AND P.golLocal = P.golVisitante))
		BEGIN
			SET @acertada = 1
		END
	END
	RETURN @acertada
END
GO

/*
ESTAN COMENTADOS PORQUE EN EL TRIGGER APUESTA ABIERTA LO CONTROLAMOS TODO
*/
/*
Trigger que no se pueda modificar despues de concluir la finalizacion del partido

GO
CREATE OR ALTER TRIGGER partidoFinalizado ON Partidos
AFTER UPDATE AS 
	BEGIN
		IF EXISTS (SELECT * FROM inserted AS I
		INNER JOIN Partidos AS P ON I.id = P.id
		--INNER JOIN deleted AS D ON P.id = D.id
		WHERE GETDATE() > DATEADD(MINUTE, -10, P.fechaFin)) 
		BEGIN
			RAISERROR ('El partido se ha cerrado y no se permite mas modificaciones', 16,1)
			ROLLBACK
		END
	END
GO

/*
	Trigger que no se pueda antes de empezar el partido modificar el marcador
*/
GO
CREATE OR ALTER TRIGGER modificarAntesMarcador ON Partidos
AFTER UPDATE AS 
	BEGIN
		IF EXISTS (SELECT * FROM inserted AS I
		INNER JOIN Partidos AS P ON I.id = P.id
		--INNER JOIN deleted AS D ON P.id = D.id
		WHERE (GETDATE() NOT BETWEEN DATEADD(DAY, -2, P.fechaInicio) AND P.fechaInicio)) --AND I.golLocal = 0 AND I.golVisitante = 0)
		BEGIN
			RAISERROR ('La apuesta del partido no ha empezado o los goles deben ser 0 a 0', 16,1)
			ROLLBACK
		END
	END
GO
*/
--1er Trigger actualiza el saldo del usuario cuando realiza una apuesta
GO	
--DROP TRIGGER actualizarSaldo
CREATE TRIGGER actualizarSaldo on Apuestas
AFTER INSERT AS
	BEGIN
		DECLARE @saldo money
		DECLARE @cantidad int
		DECLARE @id_usuario smallint 
		SELECT @saldo = U.saldo, @cantidad=I.cantidad, @id_usuario=U.id FROM Usuarios AS U 
		INNER JOIN inserted AS I ON U.id = I.id_usuario

		
		IF(@cantidad > @saldo)
			BEGIN
				RAISERROR('No tiene suficiente saldo',16,1)
				ROLLBACK
			END
		ELSE
			BEGIN
				--UPDATE Usuarios
				--SET saldo -= @cantidad WHERE id=@id_usuario
				--Insertamos el ingreso
				INSERT INTO Ingresos (cantidad, descripcion, id_usuario) VALUES (@cantidad * -1,'Apuesta realizada',@id_usuario)
			END
	END
GO

/*
	Comprobar los beneficios maximos para no dejar pagar la apuesta
*/

-- Se comprueba que en cada apuesta ganada no se supere el maximo beneficio definido en la tabla
-- Si esto ocurre, el pago de la apuesta quedaria anulada
--GO
--CREATE OR ALTER PROCEDURE noSePagaMaximo @IDApuesta SMALLINT, @Tipo CHAR(1), @fallado BIT OUTPUT 
--AS
--BEGIN
--SET @fallado = 0
--	BEGIN TRANSACTION
--	IF(@Tipo = 1)
--	BEGIN
--		IF EXISTS(SELECT * FROM Apuestas AS A
--		INNER JOIN Apuestas_tipo1 AS AT1 ON A.id = AT1.id
--		WHERE AT1.apuestasMáximas < A.cantidad * A.cuota AND A.id = @IDApuesta)
--		BEGIN
--			SET @fallado = 1
--			RAISERROR('Tu apuesta supera el maximo permitido en esta apuesta',16,1)
--			ROLLBACK
--		END
--	END

--	IF(@Tipo = 2)
--	BEGIN
--		IF EXISTS(SELECT * FROM Apuestas AS A
--		INNER JOIN Apuestas_tipo2 AS AT2 ON A.id = AT2.id
--		WHERE AT2.apuestasMáximas < A.cantidad * A.cuota AND A.id = @IDApuesta)
--		BEGIN
--			SET @fallado = 1
--			ROLLBACK
--			RAISERROR('Tu apuesta supera el maximo permitido en esta apuesta',16,1)
			
--		END
--	END

--	IF(@Tipo = 3)
--	BEGIN
--		IF EXISTS(SELECT * FROM Apuestas AS A
--		INNER JOIN Apuestas_tipo3 AS AT3 ON A.id = AT3.id
--		WHERE AT3.apuestasMáximas < A.cantidad * A.cuota AND A.id = @IDApuesta)
--		BEGIN
--			SET @fallado = 1
--			RAISERROR('Tu apuesta supera el maximo permitido en esta apuesta',16,1)
--			ROLLBACK
--		END
--	END
--	IF(@fallado = 0)
--	BEGIN
--		COMMIT
--	END
	
--END
--GO

--Este procedimiento es sumar la apuesta en caso de que esté acertada
GO
CREATE OR ALTER PROCEDURE sumarApuesta 
				 @IDApuest int,
				 @IDUsuario int
AS
BEGIN
	declare @salgoGanado int
	declare @acertada bit
	declare @tipo tinyint

	set @tipo = (Select tipo FROM Apuestas WHERE ID = @IDApuest)--añadido and id_usuario=@IDUsuario
	EXECUTE @acertada = comprobarApuestaAcertada @IDApuest,@tipo, @acertada
	
	IF(@acertada = 1)
	BEGIN
		
		SELECT @salgoGanado = saldo + (cantidad*cuota) FROM Apuestas AS A
		INNER JOIN Usuarios AS U
			ON U.id = A.id_usuario
		WHERE @IDApuest = A.id AND @IDUsuario = id_usuario and cast(A.fechaHora as date)=cast(CURRENT_TIMESTAMP as date)
		
		/*UPDATE Usuarios 
		SET saldo = @salgoGanado
		WHERE id = @IDUsuario*/
		if(@salgoGanado>0)
		begin
			INSERT INTO Ingresos (cantidad,descripcion,id_usuario)
			VALUES( @salgoGanado,'apuesta ganada',@IDUsuario)
		end
	END
END

--Este procedimiento es sumar la apuesta en caso de que este acertada
GO
CREATE OR ALTER PROCEDURE sumarApuestaAutomaticamente 
AS
BEGIN
	declare @IDApuest int
	declare  @IDUsuario int

	declare miCursor cursor for select id,id_usuario from Apuestas

	open miCursor

	fetch next from miCursor into @IDApuest,@IDUsuario

	while(@@FETCH_STATUS=0)
	begin
	
		exec sumarApuesta @IDApuest,@IDUsuario
		fetch next from miCursor into @IDApuest,@IDUsuario

	end--fin de while
	close miCursor--cerramos
	deallocate miCursor--liberamos la memoria

END
GO
begin tran
EXECUTE sumarApuestaAutomaticamente
commit
rollback
go



--set dateformat 'ymd'
----Inserts
--INSERT INTO Usuarios
--VALUES (500,'aabb@gmail.com','1234',0),(5000,'bbb@gmail.com','5678',0),(8000,'gmasd@gmail.com','9123',1)

--INSERT INTO Ingresos (cantidad,descripcion,id_usuario)
--VALUES (300,'Ingreso',1),(-300,'Reintegro',2),(2000,'Ingreso',3)

--INSERT INTO Partidos 
--VALUES(3,1,2,'2019-01-12 12:00','2019-01-12 13:45','Sevilla','Betis', 5000,1000,6000),
--(0,1,5,'2019-01-13 13:00','2019-01-13 14:45','Carmona','Coria',6000,12000,500),
--(2,1,2,'2019-03-03 22:00','2019-03-03 23:45','Barcelona','Madrid',6000,9054,6987)

--INSERT INTO Apuestas
--VALUES (1.2,50,1,'2019-01-11 12:00',1,3),
--(2.0,20,2,'2019-01-12 12:00',2,4),
--(2.50,300,3,'2019-02-03 12:00',3,5)

--INSERT INTO Apuestas
--VALUES (1.2,50,1,'2019-01-11 12:00',1,4),(2.0,20,2,'2019-01-12 12:00',2,4),(2.50,300,3,'2019-02-03 12:00',3,4)

--INSERT INTO Apuestas_tipo1
--VALUES (2,3,2)

--INSERT INTO Apuestas_tipo2
--VALUES (3,5,'2')

--INSERT INTO Apuestas_tipo3
--VALUES (4,'x')

/*Actualizado por Angela*/
GO
CREATE or alter PROCEDURE insertarApuesta 
@cuota decimal(5,2),
@cantidad MONEY, 
@tipo CHAR(1),
@IDUsuario SMALLINT, 
@IDPartido smallint,
@golLocal tinyint,
@golVisitante tinyint,
@gol tinyint,
@puja char(1)


AS
BEGIN

	declare @temp bit
	set @temp = dbo.COMPROBARMAXIMO(@IDPartido, @tipo)
	declare @lastID smallint

	begin transaction

	if(@temp = 1)
	begin

			INSERT Apuestas (cuota, cantidad, tipo, fechaHora, id_usuario, id_partido)
			VALUES (@cuota, @cantidad, @tipo, CURRENT_TIMESTAMP, @IDUsuario, @IDPartido)

			set @lastID = @@IDENTITY
			IF(@tipo = '1')
			BEGIN
				INSERT Apuestas_tipo1(id, golLocal, golVisitante)
				VALUES (@lastID, @golLocal, @golVisitante)

				
			END

			ELSE IF(@tipo = '2' )
			BEGIN
				INSERT Apuestas_tipo2(id, gol, puja)
				VALUES (@lastID ,  @gol, @puja)
				
			END
			ELSE
			IF(@tipo = '3')
			BEGIN
				INSERT Apuestas_tipo3(id,  puja)
				VALUES (@lastID , @puja)
				
			END
	COMMIT
	end

	else
	begin
	
			print @temp
			RAISERROR('No se pudo insertar la apuesta porque se ha superado el maximo',16,1)
					ROLLBACK

	end
END
GO




/*Comprueba que no se supere el maximo
Funcion AÑADIDA
*/

CREATE OR ALTER FUNCTION COMPROBARMAXIMO(@IDPARTIDO SMALLINT, @TIPO CHAR(1))
	RETURNS BIT AS
	BEGIN
	DECLARE @RET BIT = 0

	/*Importante faltaban los ISNULL porque si no pillaba null y no sabia comparar eso con un numero*/


		IF(@TIPO = '1')
			BEGIN 

				IF( (SELECT ISNULL(SUM(cantidad * cuota),0 )
					FROM Apuestas
					WHERE id_partido = @IDPARTIDO
					and
					tipo = '1'
					) <
					(SELECT maximoApuestaTipo1
					FROM Partidos
					WHERE ID = @IDPARTIDO) 
				)
						BEGIN
						SET @RET = 1
						END
			END
		

		IF(@TIPO = '2')
			BEGIN 

				IF( (SELECT ISNULL(SUM(cantidad * cuota),0 )
					FROM Apuestas
					WHERE id_partido = @IDPARTIDO
					and
					tipo = '2'
					) <
					(SELECT maximoApuestaTipo2
					FROM Partidos
					WHERE ID = @IDPARTIDO) 
				)
						BEGIN
						SET @RET = 1
						END
			END
		

		IF(@TIPO = '3')
			BEGIN 
			/*Importante faltaban los ISNULL porque si no pillaba null y no sabia comparar eso con un numero*/
				IF( (SELECT ISNULL(SUM(cantidad * cuota),0 )
						FROM Apuestas
						WHERE id_partido = @IDPARTIDO
						and
						tipo = '3'
						) <
					(SELECT maximoApuestaTipo3
					FROM Partidos
					WHERE ID = @IDPARTIDO) 
				)
						BEGIN
						SET @RET = 1
						END
			END
		


		RETURN @RET
	END
	GO


--PRUEBAS 
-- modificarSaldo
--SELECT * FROM Usuarios
--INSERT INTO Ingresos (cantidad,descripcion,id_usuario)
--VALUES (2,'Ingreso',1)

----No se puede modificar las apuestas
--SELECT * FROM Apuestas
--UPDATE Apuestas
--SET cantidad = 3
--WHERE id = 1

--DELETE FROM Apuestas
--WHERE id = 1

----partidoAbiertoApuesta
--SELECT * FROM Partidos
--SELECT * FROM Apuestas

--INSERT INTO Apuestas
--VALUES (1.2,50,1,'1-01-2019 12:00',1,1)

----partidoFinalizado
/*
SELECT * FROM Partidos

INSERT INTO Partidos 
VALUES(0,2,'08-10-2019 12:00','08-10-2019 13:45','Villanueva','Xerez')
UPDATE Partidos
SET golLocal = 5
WHERE id = 4


INSERT INTO Partidos 
VALUES(1,2,'08-10-2019 09:00','08-10-2019 10:45','Sevilla','Lora del Rio')
UPDATE Partidos
SET golLocal = 5
WHERE id = 6

--modificarAntesMarcador
SELECT * FROM Partidos
UPDATE Partidos
SET golLocal = 1
WHERE id = 4

INSERT INTO Partidos 
VALUES(1,2,'18-10-2019 09:00','18-10-2019 10:45','Leon','Salamanca')

UPDATE Partidos
SET golLocal = 19
WHERE id = 7
*/

--actualizarSaldo
SELECT * FROM Apuestas
SELECT * FROM Apuestas_tipo1
SELECT * FROM Apuestas_tipo2
SELECT * FROM Ingresos
SELECT * FROM Usuarios
SELECT * FROM Partidos

INSERT INTO Apuestas
VALUES (1.2,50,2,'08-10-2019 12:00',1,4)

--comprobarApuestaAcertada
--Acertado
--BEGIN TRANSACTION
----DECLARE @acertada BIT
--EXECUTE @acertada = comprobarApuestaAcertada 1,1,@acertada
--PRINT @acertada
----ROLLBACK
----COMMIT

----Fallado
--INSERT INTO Apuestas_tipo1
--VALUES (2,2,0,0)
--BEGIN TRANSACTION
--DECLARE @acertada BIT
--EXECUTE @acertada = comprobarApuestaAcertada 2,1,@acertada
--PRINT @acertada
----ROLLBACK
----COMMIT

----noSePagaMaximo
--INSERT INTO Apuestas
--VALUES (1.8,250,2,'13-01-2019 14:00',1,2)

--INSERT INTO Apuestas_tipo2
--VALUES (4,2,5,2)

--UPDATE Apuestas_tipo2
--SET apuestasMáximas = 2
--WHERE id = 4

----BEGIN TRANSACTION
--EXECUTE noSePagaMaximo 4,2
----ROLLBACK
----COMMIT

--SumarApuesta