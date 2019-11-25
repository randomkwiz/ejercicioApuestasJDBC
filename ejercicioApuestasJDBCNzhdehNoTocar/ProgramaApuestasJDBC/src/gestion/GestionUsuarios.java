package gestion;

import clases.UsuarioImpl;
import conexion.ConexionJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionUsuarios {

    /*
     * Signatura: public UsuarioImpl obtenerObjetoUsuarioCompleto(UsuarioImpl usuario)
     * Comentario: Construye el objeto usuario cogiendo los datos de la BBDD
     * Precondiciones: El objeto usuario recibido debe tener una combinacion de correo y contraseña que debe existir en la BBDD
     * Entradas:
     * Salidas:
     * Entrada/Salida: objeto usuario que primero tendra solo correo y contraseña y luego
     *                  sera un objeto con los datos correspondientes a partir de la BBDD
     * Postcondiciones: Modifica el objeto usuario añadiendo todos los datos que tuviera. Devuelve asociado al nombre
     *                  un boolean que sera true si el usuario se construyo correctamente y false si hubo algun problema.
     * */
    public boolean obtenerObjetoUsuarioCompleto(UsuarioImpl usuario){
        ConexionJDBC conexionJDBC = new ConexionJDBC();
        Connection connection = conexionJDBC.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean exito = false;

        String miSelect = "select * from Usuarios  where correo = ?\n" +
                "        and\n" +
                "                contraseña = ?";
        try {
            //Preparo el statement
            preparedStatement = connection.prepareStatement(miSelect);
            preparedStatement.setString(1, usuario.getCorreo());
            preparedStatement.setString(2, usuario.getPassword());
            //Ejecuto
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //Le pongo los datos al objeto usuario
                usuario.setId(resultSet.getInt("id"));
                usuario.setCantidadActualDinero(resultSet.getDouble("saldo"));
                usuario.setCorreo(resultSet.getString("correo"));
                usuario.setPassword(resultSet.getString("contraseña"));
                usuario.setAdmin(resultSet.getBoolean("isAdmin"));
            }
            exito = true;

            //Cierro
            resultSet.close();
            preparedStatement.close();
            conexionJDBC.closeConnection(connection);
        }catch (SQLException e){
            e.getStackTrace();
        }
        return exito;
    }
	
	 /*
	prototipo: public UsuarioImpl ObtenerUsuarioPorId(int idUsuario)  
	comentarios: sirve para obtener un usuario seguin una id dada
	precondiciones: id correcto
	entradas: entero idUsuario
	salidas: objeto usuario
	entradas/salidas: no hay 
	postcondiciones: AN devuelve un objeto usuario
	*/
	
	public UsuarioImpl ObtenerUsuarioPorId(int idUsuario) 
	{
		ConexionJDBC conexionJDBC = new ConexionJDBC();
		Connection connection = conexionJDBC.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		UsuarioImpl usuario = null;

		String miSelect = "select * from Usuarios where id= ?";//preguntar
		try {
			//Preparo el statement
			preparedStatement = connection.prepareStatement(miSelect);
			preparedStatement.setInt(1,idUsuario);
			//Ejecuto
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) 
			{
				usuario = new UsuarioImpl();
				
				usuario.setId(resultSet.getInt("id"));
                usuario.setCantidadActualDinero(resultSet.getDouble("saldo"));
                usuario.setCorreo(resultSet.getString("correo"));
                usuario.setPassword(resultSet.getString("contraseña"));
                usuario.setAdmin(resultSet.getBoolean("isAdmin"));
			}


			preparedStatement.close();
			conexionJDBC.closeConnection(connection);
		}catch (SQLException e){
			e.getStackTrace();
		}
		return usuario;
	}
}