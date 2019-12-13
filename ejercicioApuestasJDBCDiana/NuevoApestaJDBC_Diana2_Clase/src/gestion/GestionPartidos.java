package gestion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import clases.PartidoImpl;

import conexion.ConexionJDBC;
import utilidad.Utilidad;
import validacion.Validar;

public class GestionPartidos 
{
	/*
	prototipo: public ArrayList<PartidoImpl> VerPartidosDisponibles()
	comentarios: este metodo nos muestra los partidos disponibles para apostar
					se puede apostra si la fecha de la apuesta estÃ¡ entre dos 
					dÃ­as antes de la fecha del partido y diez minutos antes del final del partido
	precondiciones: no hay
	entradas: no hay
	salidas: ArrayList<PartidoImpl> listadoPartidos
	entradas/salidas: no hay 
	postcondiciones: AN devuelve el array con los partidos en los que se puede hacer apuestas
	*/
	
	public ArrayList<PartidoImpl> VerPartidosDisponibles() 
	{
		ConexionJDBC conexionJDBC = new ConexionJDBC();
		Connection connection = conexionJDBC.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<PartidoImpl> listadoPartidos = new ArrayList<>();
		PartidoImpl partido;
		GregorianCalendar fechaInicio;
		GregorianCalendar fechaFin;

		String miSelect = "select * from Partidos where isPeriodoApuestasAbierto=?";
		try {
			//Preparo el statement
			preparedStatement = connection.prepareStatement(miSelect);
			preparedStatement.setBoolean(1, true);
			//Ejecuto
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) 
			{
				partido = new PartidoImpl();
				fechaInicio = new GregorianCalendar();
				fechaFin = new GregorianCalendar();
				
				
				partido.setId(resultSet.getInt("id"));
				partido.setPeriodoApuestasAbierto(resultSet.getBoolean("isPeriodoApuestasAbierto"));
				partido.setGolesLocal(resultSet.getInt("golLocal"));
				partido.setGolesVisitante(resultSet.getInt("golVisitante"));
				
				if(resultSet.getDate("fechaInicio") != null)
				{
					fechaInicio.setTime(resultSet.getDate("fechaInicio"));
				}
				else
				{
					fechaFin = null;
				}
				partido.setFechaInicio(fechaInicio);
				if (resultSet.getDate("fechaFin") != null) 
				{
					fechaFin.setTime(resultSet.getDate("fechaFin"));
				} 
				else 
				{
					fechaFin = null;
				}
				partido.setFechaFin(fechaFin);
				partido.setNombreLocal(resultSet.getString("nombreLocal"));
				partido.setNombreVisitante(resultSet.getString("nombreVisitante"));

				listadoPartidos.add(partido);
			}


			preparedStatement.close();
			conexionJDBC.closeConnection(connection);
		}catch (SQLException e){
			e.getStackTrace();
		}
		return listadoPartidos;
	}
	
	/*
	prototipo: public void DineroApostadoPorUnPosibleResultadoDeUnPartido(int idPartido)
	comentarios: este metodo calcula el total del dinero apostado por cada resultado de un partido
	precondiciones: el id tiene que existir
	entradas: entero id de un partido
	salidas: no hay
	entradas/salidas: no hay 
	postcondiciones: solo pintara en pantalla el dinero apostado por cada resultado mde un partido
	*/
	
	public void DineroApostadoPorUnPosibleResultadoDeUnPartido(int idPartido)
	{
		ConexionJDBC conexionJDBC = new ConexionJDBC();
		Connection connection = conexionJDBC.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int golLocal=0,golVisitante=0,gol=0;
		String pujaTipo2=" ",pujaTipo3=" ";
		double cantidadApostada=0.;

		String miSelect = "select AT1.golLocal,AT1.golVisitante,AT2.gol,AT2.puja as PujaTipo2,AT3.puja as PujaTipo3,SUM(A.cantidad) as CantidatTotal from Apuestas as A\r\n" + 
				"	left join Apuestas_tipo1 as AT1 on A.id=AT1.id\r\n" + 
				"	left join Apuestas_tipo2 as AT2 on A.id=AT2.id\r\n" + 
				"	left join Apuestas_tipo3 as AT3 on A.id=AT3.id\r\n" + 
				"	where A.id_partido=?\r\n" + 
				"	group by AT1.golLocal,AT1.golVisitante,AT2.gol,AT2.puja,AT3.puja";
		try {
			//Preparo el statement
			preparedStatement = connection.prepareStatement(miSelect);
			preparedStatement.setBoolean(1, true);
			//Ejecuto
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) 
			{
				golLocal=resultSet.getInt("golLocal");
				golVisitante=resultSet.getInt("golVisitante");
				gol=resultSet.getInt("gol");
				pujaTipo2=resultSet.getString("PujaTipo2");
				pujaTipo3=resultSet.getString("PujaTipo3");
				cantidadApostada=resultSet.getDouble("CantidatTotal");
				
				System.out.println(golLocal+", "+golVisitante+", "+gol+", "+pujaTipo2+", "+pujaTipo3+", "+cantidadApostada);
			}
			preparedStatement.close();
			conexionJDBC.closeConnection(connection);
		}catch (SQLException e)
		{
			e.getStackTrace();
		}
	}
	
	/*
	prototipo: public void DineroApostadoPorUnPosibleResultadoDeUnPartido(int idPartido)
	comentarios: este metodo sirve para ver si un partido existe
	precondiciones: array lleno
	entradas: entero id de un partido, arrayList de partidos
	salidas: boolean
	entradas/salidas: no hay 
	postcondiciones: AN devuelve true si existe y false si no
	*/
	
	public boolean ComprobarExistenciaDelPartidoPorId(int idPartido,ArrayList<PartidoImpl> partodo)
	{
		boolean existe=false;
		
		for(int i=0;i<partodo.size() && existe==false;i++)
		{
			if(partodo.get(i).getId()==idPartido)
			{
				existe=true;
			}
		}
		return existe;
	}

	/*
	 * Signatura: public ArrayList<PartidoImpl> obtenerListadoPartidosAnteriorAHoy()
	 * Comentario: Este mÃ©todo consulta a la BBDD y devuelve el listado de todos los partidos que existan
	 * 			cuya fecha de fin sea anterior o igual a la actual
	 * Precondiciones:
	 * Entradas:
	 * Salidas: ArrayList de Partidos
	 * Postcondiciones: Asociado al nombre se devolverÃ¡ un arraylist con todos los partidos existentes
	 * 					cuya fecha de fin sea anterior o igual a la fecha actual en
	 * 					la BBDD. Si no hay partidos, se devolverÃ¡ un arraylist vacÃ­o.
	 * */
	public ArrayList<PartidoImpl> obtenerListadoPartidosAnteriorAHoy(){
		ConexionJDBC conexionJDBC = new ConexionJDBC();
		Connection connection = conexionJDBC.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<PartidoImpl> listadoPartidos = new ArrayList<>();
		PartidoImpl partido;
		GregorianCalendar fechaInicio;
		GregorianCalendar fechaFin;

		String miSelect = "select * from Partidos\n" +
				"where fechaFin <= CURRENT_TIMESTAMP";
		try {
			//Preparo el statement
			preparedStatement = connection.prepareStatement(miSelect);
			//Ejecuto
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				partido = new PartidoImpl();
				fechaInicio = new GregorianCalendar();
				fechaFin = new GregorianCalendar();
				//Le pongo los datos al objeto partido
				partido.setId(resultSet.getInt("id"));
				partido.setPeriodoApuestasAbierto(resultSet.getBoolean("isPeriodoApuestasAbierto"));
				partido.setGolesLocal(resultSet.getInt("golLocal"));
				partido.setGolesVisitante(resultSet.getInt("golVisitante"));
				if(resultSet.getDate("fechaInicio") != null){
					fechaInicio.setTime(resultSet.getDate("fechaInicio"));
				}else{
					fechaFin = null;
				}
				partido.setFechaInicio(fechaInicio);
				if (resultSet.getDate("fechaFin") != null) {
					fechaFin.setTime(resultSet.getDate("fechaFin"));
				} else {
					fechaFin = null;
				}
				partido.setFechaFin(fechaFin);
				partido.setNombreLocal(resultSet.getString("nombreLocal"));
				partido.setNombreVisitante(resultSet.getString("nombreVisitante"));

				//aÃ±ado el partido al arraylist
				listadoPartidos.add(partido);
			}


			preparedStatement.close();
			conexionJDBC.closeConnection(connection);
		}catch (SQLException e){
			e.getStackTrace();
		}
		return listadoPartidos;
	}

	/*
	* Signatura: public boolean modificarAperturaPeriodoApuestasDePartido(PartidoImpl partido, boolean isAbierto)
	* Comentario: Este método modifica si el partido tiene el período de apuestas abierto o cerrado
	* Precondiciones:
	* Entradas: objeto partido y boolean que indica si abriremos o cerraremos el periodo de apuestas
	* 			-boolean isAbierto: true -> abrimos el período de apuestas
	* 			-boolean isAbierto: false -> cerramos el período de apuestas
	* Salidas: boolean que indica si la operación se realizó con éxito o no
	* Postcondiciones: asociado al nombre se devolverá el boolean que indicará si la operación
	* 				se realizó con éxito (true) o no (false).
	* 				Se considerará un éxito si el numero de filas afectadas es igual a 1.
	* 				El período de apuestas del partido quedará modificado si la operación es
	* 				realizada con éxito.
	*
	* */
	public boolean modificarAperturaPeriodoApuestasDePartido(PartidoImpl partido, boolean isAbierto){
		ConexionJDBC conexionJDBC = new ConexionJDBC();
		Connection connection = conexionJDBC.getConnection();
		PreparedStatement preparedStatement = null;
		boolean exito = false;

		String miSelect = "update \n" +
				"Partidos\n" +
				"set \n" +
				"isPeriodoApuestasAbierto = ?\n" +
				"where id = ?";
		try {
			//Preparo el statement
			preparedStatement = connection.prepareStatement(miSelect);
			preparedStatement.setInt(2, partido.getId());
			preparedStatement.setBoolean(1, isAbierto);
			//Ejecuto

			if(preparedStatement.executeUpdate() == 1){
				exito = true;
			}
			//Cierro

			preparedStatement.close();
			conexionJDBC.closeConnection(connection);
		}catch (SQLException e){
			//e.getStackTrace();
			System.out.println("Hubo un problema");
		}
		return exito;
	}

	/*
	 * Signatura: public ArrayList<PartidoImpl> obtenerListadoPartidos()
	 * Comentario: Este método consulta a la BBDD y devuelve el listado de todos los partidos que existan
	 * 			cuya fecha de fin sea posterior a la actual
	 * Precondiciones:
	 * Entradas:
	 * Salidas: ArrayList de Partidos
	 * Postcondiciones: Asociado al nombre se devolverá un arraylist con todos los partidos existentes
	 * 					cuya fecha de fin sea posterior a la fecha actual en
	 * 					la BBDD. Si no hay partidos, se devolverá un arraylist vacío.
	 * */
	public ArrayList<PartidoImpl> obtenerListadoPartidos(){
		ConexionJDBC conexionJDBC = new ConexionJDBC();
		Connection connection = conexionJDBC.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<PartidoImpl> listadoPartidos = new ArrayList<>();
		PartidoImpl partido;
		GregorianCalendar fechaInicio;
		GregorianCalendar fechaFin;

		String miSelect = "select * from Partidos\n" +
				"where fechaFin > CURRENT_TIMESTAMP";
		try {
			//Preparo el statement
			preparedStatement = connection.prepareStatement(miSelect);
			//Ejecuto
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				partido = new PartidoImpl();
				fechaInicio = new GregorianCalendar();
				fechaFin = new GregorianCalendar();
				//Le pongo los datos al objeto partido
				partido.setId(resultSet.getInt("id"));
				partido.setPeriodoApuestasAbierto(resultSet.getBoolean("isPeriodoApuestasAbierto"));
				partido.setGolesLocal(resultSet.getInt("golLocal"));
				partido.setGolesVisitante(resultSet.getInt("golVisitante"));
				if(resultSet.getDate("fechaInicio") != null){
					fechaInicio.setTime(resultSet.getDate("fechaInicio"));
				}else{
					fechaFin = null;
				}
				partido.setFechaInicio(fechaInicio);
				if (resultSet.getDate("fechaFin") != null) {
					fechaFin.setTime(resultSet.getDate("fechaFin"));
				} else {
					fechaFin = null;
				}
				partido.setFechaFin(fechaFin);
				partido.setNombreLocal(resultSet.getString("nombreLocal"));
				partido.setNombreVisitante(resultSet.getString("nombreVisitante"));

				//añado el partido al arraylist
				listadoPartidos.add(partido);
			}


			preparedStatement.close();
			conexionJDBC.closeConnection(connection);
		}catch (SQLException e){
			e.getStackTrace();
		}
		return listadoPartidos;
	}

	//Parte de Diana:
	//Crear partido:

	/*
	 * Signatura: public boolean insertarPartido(PartidoImpl partidoNuevo)
	 * Comentario: Este método recibe un objeto nuevo de partido y lo inserta en la base de datos
	 * Precondiciones:
	 * Entradas:
	 * Salidas: Boolean que indica si la operación se realizó con éxito o no.
	 * Postcondiciones: Asociado al nombre se devolverá un buleano que indicará si la operación se realizó con éxito (true) o no (false).
	 * 					Se considerará éxito si el número de filas afectadas es igual a 1.
	 * */
//
	public boolean insertarPartido(PartidoImpl partidoNuevo){ //TODO modificar en común 12/12/2019
		Utilidad objUtilidad = new Utilidad();
		//Variables inserción en base de datos
		ConexionJDBC objConexion = new ConexionJDBC();
		Connection conexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultset;
//		String sentenciaSql = "INSERT INTO Partidos VALUES(?, ?, ?, ?, ?, ?, ?)"; //Antigua
		String sentenciaSql = "INSERT INTO Partidos VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; //Modificada
		boolean exito = false;


		try{
			conexion = objConexion.getConnection();
			preparedStatement = conexion.prepareStatement(sentenciaSql);
			if(!conexion.isClosed()){
				preparedStatement.setBoolean(1, partidoNuevo.isPeriodoApuestasAbierto());
				preparedStatement.setInt(2, partidoNuevo.getGolesLocal());
				preparedStatement.setInt(3, partidoNuevo.getGolesVisitante());
				//preparedStatement.setDate(4, (java.sql.Date) partidoNuevo.getFechaInicio().getTime()); //TODO: revisar
				//preparedStatement.setDate(5, (java.sql.Date) partidoNuevo.getFechaFin().getTime()); //TODO: revisar
				preparedStatement.setString(4, objUtilidad.formatearFecha(partidoNuevo.getFechaInicio())); //TODO revisar formato a insertar
				preparedStatement.setString(5, objUtilidad.formatearFecha(partidoNuevo.getFechaFin())); //TODO revisar formato a insertar
//				preparedStatement.setString(4, objUtilidad.formatearFecha(partidoNuevo.getFechaInicio()) + " " + partidoNuevo.getFechaInicio().get(Calendar.HOUR_OF_DAY) + ":" + partidoNuevo.getFechaInicio().get(Calendar.MINUTE)); //TODO ver si funciona
//				preparedStatement.setString(5, objUtilidad.formatearFecha(partidoNuevo.getFechaFin()) + " " + partidoNuevo.getFechaFin().get(Calendar.HOUR_OF_DAY) + ":" + partidoNuevo.getFechaFin().get(Calendar.MINUTE)); //TODO ver si funciona

				preparedStatement.setString(6, partidoNuevo.getNombreLocal());
				preparedStatement.setString(7, partidoNuevo.getNombreVisitante());
				//TODO falta la inserción de  máximo apuestas tipo1 tipo2 y tipo3
				preparedStatement.setDouble(8, partidoNuevo.getApuestasMaximasTipo1());
				preparedStatement.setDouble(9, partidoNuevo.getApuestasMaximasTipo2());
				preparedStatement.setDouble(10, partidoNuevo.getApuestasMaximasTipo3());

//				filasAfectadas = preparedStatement.executeUpdate();

				if(preparedStatement.executeUpdate() == 1){
					exito = true;
				}
			}

		}catch (SQLException e){
			e.printStackTrace();
		}finally {


			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			objConexion.closeConnection(conexion);
		}
		return exito;
	}




	/*
	 * Signatura: public PartidoImpl obtenerPartidoPorFechaApuesta(GregorianCalendar fechaApuestaGregCal)
	 * Comentario: obtiene un partido según una fecha de apuesta
	 * Precondiciones: la fecha de apuesta estará validada
	 * Entradas:
	 * Salidas: Objeto PartidoImpl
	 * Postcondiciones: Asociado al nombre se devolverá un objeto partido según la fecha pasada por parámetro
	 * */
	public ArrayList<PartidoImpl> obtenerPartidoPorFechaApuesta(GregorianCalendar fechaApuestaGregCal){ //necesario para obtener apuesta por fecha, la fecha se insertará en el método de obtener apuestas según fecha
		Utilidad objUtil = new Utilidad();
		ConexionJDBC objConexion = new ConexionJDBC();
		Connection conexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultset = null;
		PartidoImpl partido = new PartidoImpl();
		ArrayList<PartidoImpl> listadoPartidos = new ArrayList<>();

		GregorianCalendar fechaInicio = new GregorianCalendar();
		GregorianCalendar fechaFin = new GregorianCalendar();

		String fechaApuesta = objUtil.formatearFecha(fechaApuestaGregCal);

		//TODO recoger partido en arrylist y  luego (en otro lado) preguntar por el id para obtener el partido en concreto
		//Sentecia que obtiene los datos del partido según la fecha de la apuesta, se tiene en cuenta que la fecha de la apuesta esté entre fecha inicio y fecha final del partido
		String sentenciaSql = "SELECT p.id, p.isPeriodoApuestasAbierto, p.golLocal, p.golVisitante, p.fechaInicio, p.fechaFin, p.nombreLocal, p.nombreVisitante \n" +
				"\t\t\t\tFROM Partidos AS p INNER JOIN Apuestas As a ON a.id_partido = p.id \n" +
				"\t\t\t\tWHERE fechaHora BETWEEN (SELECT fechaInicio FROM Partidos WHERE id = a.id_partido) \n" +
				"\t\t\t\tAND (SELECT fechaFin FROM Partidos WHERE id = a.id_partido) \n" +
				"\t\t\t\tAND CAST(a.fechaHora as date) = ?";

		try{
			conexion = objConexion.getConnection();
			preparedStatement = conexion.prepareStatement(sentenciaSql);
			preparedStatement.setString(1, fechaApuesta);
			resultset = preparedStatement.executeQuery();

			while (resultset.next()){
				partido.setId(resultset.getInt("id"));
				partido.setPeriodoApuestasAbierto(resultset.getBoolean("isPeriodoApuestasAbierto"));
				partido.setGolesLocal(resultset.getInt("golLocal"));
				partido.setGolesVisitante(resultset.getInt("golVisitante"));
				//Fechas //TODO no tengo claro que sea así
				fechaInicio.setTime(resultset.getDate("fechaInicio"));
				partido.setFechaInicio(fechaInicio); //TODO revisar

				fechaFin.setTime(resultset.getDate("fechaFin"));
				partido.setFechaFin(fechaFin);

				partido.setNombreLocal(resultset.getString("nombreLocal"));
				partido.setNombreVisitante(resultset.getString("nombreVisitante"));

				listadoPartidos.add(partido);
			}

		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			try {
				resultset.close();
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			objConexion.closeConnection(conexion);
		}
		return listadoPartidos;
	}
}
