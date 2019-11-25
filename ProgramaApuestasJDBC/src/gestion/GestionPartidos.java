package gestion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import clases.PartidoImpl;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import conexion.ConexionJDBC;

public class GestionPartidos 
{
	/*
	prototipo: public ArrayList<PartidoImpl> VerPartidosDisponibles()
	comentarios: este mÃ©todo nos muestra los partidos disponibles para apostar
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
	 * Signatura: public void crearObjetoPartido()
	 * Comentario: Este método crea un objeto nuevo de partido y lo inserta en la base de datos
	 * Precondiciones:
	 * Entradas:
	 * Salidas: ArrayList de Partidos
	 * Postcondiciones: Asociado al nombre se devolverá un arraylist con todos los partidos existentes
	 * 					cuya fecha de fin sea posterior a la fecha actual en
	 * 					la BBDD. Si no hay partidos, se devolverá un arraylist vacío.
	 * */
	public void crearObjetoPartido(){
		Scanner sc = new Scanner(System.in);
		//Variables inserción en base de datos
		ConexionJDBC objConexion = new ConexionJDBC();
		Connection conexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultset;
		String sentenciaSql = "INSERT INTO Partidos VALUES(?, ?, ?, ?, ?, ?, ?)";

		//Variables datos
		int golLocal, golVisitante;
		GregorianCalendar fechaComun, fechaInicio, fechaFin;
		String nombreLocal, nombreVisitante;
		boolean fechasCorrectas = false;
		boolean periodoApuestasIsAbierto = false;

		Validar objValidar = new Validar();
		PartidoImpl nuevoPartido = new PartidoImpl();


		System.out.println("Introduzca los Goles de equipo Local");
		golLocal = objValidar.pedirValidarNumeroGoles();

		System.out.println("Introduzca los Goles de equipo Visitante");
		golVisitante = objValidar.pedirValidarNumeroGoles();

		fechaComun = objValidar.pedirValidarFecha(); // Pido la fecha sólo con día, mes y año del partido, que será común al inicio y al final
		//Paso la fecha común a la de inicio y final, para más tarde introducirles la hora
		fechaInicio = new GregorianCalendar(fechaComun.get(Calendar.YEAR),fechaComun.get(Calendar.MONTH),fechaComun.get(Calendar.DAY_OF_MONTH),0,0);
		fechaFin = new GregorianCalendar(fechaComun.get(Calendar.YEAR),fechaComun.get(Calendar.MONTH),fechaComun.get(Calendar.DAY_OF_MONTH),0,0);

        /*Pido introducir tiempo de inicio y final de partido y compruebo que final vaya después de inicio,
        se repita la opreación hasta que el usuasrio lo introduzca correctamente*/
		do{
			System.out.println("Introduzca tiempo inicio Partido");
			fechaInicio = objValidar.introducirTiempoPartido(fechaInicio);
			System.out.println("Introduzca tiempo final partido");
			fechaFin = objValidar.introducirTiempoPartido(fechaFin);
			fechasCorrectas = objValidar.validarFechaFinPosteriorFechaInicio(fechaInicio, fechaFin);
		}while (!fechasCorrectas);


		System.out.println("Introduzca Nombre del equipo Local");
		nombreLocal = sc.nextLine();
		System.out.println("Introduzca Nombre del Equipo Visitante");
		nombreVisitante = sc.nextLine();

		periodoApuestasIsAbierto = objValidar.pedirValidarIsPeriodoApuestasAbierto();


		//Creación de objeto partido
		PartidoImpl partidoNuevo = new PartidoImpl();
		partidoNuevo.setPeriodoApuestasAbierto(periodoApuestasIsAbierto);
		partidoNuevo.setGolesLocal(golLocal);
		partidoNuevo.setGolesVisitante(golVisitante);
		partidoNuevo.setFechaInicio(fechaInicio);
		partidoNuevo.setFechaFin(fechaFin);
		partidoNuevo.setNombreLocal(nombreLocal);
		partidoNuevo.setNombreVisitante(nombreVisitante);


		try{
			conexion = objConexion.getConnection();
			if(!conexion.isClosed()){
				preparedStatement.setBoolean(1, partidoNuevo.isPeriodoApuestasAbierto());
				preparedStatement.setInt(2, partidoNuevo.getGolesLocal());
				preparedStatement.setInt(3, partidoNuevo.getGolesVisitante());
				preparedStatement.setDate(4, (java.sql.Date) partidoNuevo.getFechaInicio().getTime()); //TODO: revisar
				preparedStatement.setDate(5, (java.sql.Date) partidoNuevo.getFechaFin().getTime()); //TODO: revisar
				preparedStatement.setString(6, partidoNuevo.getNombreLocal());
				preparedStatement.setString(7, partidoNuevo.getNombreVisitante());
			}

		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			//Cerrar conexión
			boolean conexionCerrada = objConexion.closeConnection(conexion);

			if(conexionCerrada){
				System.out.println("Conexión cerrada");
			}else{
				System.out.println("Fallo al cerrar la conexión");
			}
		}
	}


}
