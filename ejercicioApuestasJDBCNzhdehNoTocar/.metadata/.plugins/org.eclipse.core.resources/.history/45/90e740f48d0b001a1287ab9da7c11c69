package gestion;

import clases.IngresoImpl;
import clases.UsuarioImpl;
import conexion.ConexionJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionSaldo {
    /*
     * Signatura: public boolean realizarIngresoEnCuentaUsuario(UsuarioImpl usuario, IngresoImpl ingreso)
     * Comentario: Realiza un ingreso de dinero en la cuenta del usuario.
     * Precondiciones: El objeto usuario recibido debe existir en la BBDD
     * Entradas: objeto usuario y objeto ingreso que sera el ingreso a introducir en la cuenta del usuario
     * Salidas: boolean que indicara el exito de la operacion
     * Entrada/Salida:
     * Postcondiciones: Modifica el objeto usuario actualizando su saldo actual. Devuelve asociado al nombre
     *                  un boolean que sera true si la operacion se realizo correctamente y false si hubo algun problema.
     *                  La base de datos quedará actualizada con un ingreso económico.
     * */
    public boolean realizarIngresoEnCuentaUsuario(UsuarioImpl usuario, IngresoImpl ingreso){
        ConexionJDBC conexionJDBC = new ConexionJDBC();
        Connection connection = conexionJDBC.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean exito = false;

        String miSelect = "INSERT INTO Ingresos(cantidad, descripcion,id_usuario)\n" +
                "VALUES(?,?,?)";
        try {
            //Preparo el statement
            preparedStatement = connection.prepareStatement(miSelect);
            preparedStatement.setDouble(1,ingreso.getCantidad());
            preparedStatement.setString(2, ingreso.getDescripcion());
            preparedStatement.setInt(3, usuario.getId());
            //Ejecuto
            preparedStatement.executeQuery();
            exito = true;

            //Cierro
            preparedStatement.close();
            conexionJDBC.closeConnection(connection);
        }catch (SQLException e){
            e.getStackTrace();
        }
        return exito;
    }
}
