package gestion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.GregorianCalendar;

import conexion.ConexionJDBC;

public class GestionPartidos 
{
	public void VerPartidosDisponibles() 
	{
		
		 ConexionJDBC conexionJDBC = new ConexionJDBC();
	     Connection connection = conexionJDBC.getConnection();
	     ResultSet rs=null;
	     try 
	     {
			CallableStatement cst = connection.prepareCall("{call PartidosDisponibles}");
			cst.execute();
			
			rs=cst.getResultSet();
			
			if(rs.next()) {
				cst.registerOutParameter(1, java.sql.Types.VARCHAR);
				cst.registerOutParameter(2, java.sql.Types.VARCHAR);
				cst.registerOutParameter(3, java.sql.Types.DATE);
				
				String nombreLocal = cst.getString(1);
				String nombreVisitante = cst.getString(2);
				Date fechaIni = cst.getDate(3); 

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("En resguardo");
	}
}
