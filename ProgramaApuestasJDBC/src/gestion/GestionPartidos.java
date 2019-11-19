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
	prototipo: public void VerPartidosDisponibles()
	comentarios: este método nos muestra los partidos disponibles para apostar
					se puede apostra si la fecha de la apuesta está entre dos 
					días antes de la fecha del partido y diez minutos antes del final del partido
	precondiciones: no hay
	entradas: no hay
	salidas: no hay
	entradas/salidas: no hay 
	postcondiciones: no hya, solo muestra una lista de los partidos en los que se puede apostar
	*/
	
	public void VerPartidosDisponibles() 
	{
		 //ArrayList<PartidoImpl> partidosQueSePuedeApostar =new ArrayList<>(); 
		 ConexionJDBC conexionJDBC = new ConexionJDBC();
	     Connection connection = conexionJDBC.getConnection();
	     ResultSet rs=null;
	     try 
	     {
			CallableStatement cst = connection.prepareCall("{call PartidosDisponibles}");
			cst.execute();
			
			rs=cst.getResultSet();
			
			while(rs.next()) {
				cst.registerOutParameter(1, java.sql.Types.VARCHAR);
				cst.registerOutParameter(2, java.sql.Types.VARCHAR);
				cst.registerOutParameter(3, java.sql.Types.DATE);
				
				String nombreLocal = cst.getString(1);
				String nombreVisitante = cst.getString(2);
				Date fechaIni = cst.getDate(3); 

				System.out.println("Local: "+nombreLocal+" Visitante: "+nombreVisitante+" Fecha del Partido"+fechaIni);
			}
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
		//System.out.println("En resguardo");
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


}
