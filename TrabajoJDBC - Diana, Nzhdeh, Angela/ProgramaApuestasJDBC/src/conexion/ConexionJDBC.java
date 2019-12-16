package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionJDBC {
    //private String sourceURL = "jdbc:sqlserver://localhost";
    private String sourceURL = "jdbc:sqlserver://192.168.0.166";
    private String usuario = "apuestas";
    private String password = "apuestas";

    /*
     * Signatura: public Connection getConnection()
     * Comentario: Devuelve una conexion con la BBDD
     * Precondiciones:
     * Entradas:
     * Salidas: Objeto Connection
     * Entrada/Salida:
     * Postcondiciones: Devuelve un objeto conexion, que es una conexion con la BBDD abierta.
     * */
    public Connection getConnection(){
        Connection conexionBD = null;
        try{
            conexionBD = DriverManager.getConnection(sourceURL, usuario, password);

            //Los buenos tests
/*
            if(!conexionBD.isClosed()){
                System.out.println("La conexion se abrio");
            }else{
                System.out.println("La conexion no se abrio");
            }
*/
        }catch (SQLException e){
            e.getStackTrace();
        }
        return conexionBD;
    }

    /*
     * Signatura: public boolean closeConnection(Connection conexion)
     * Comentario: Cierra la conexion recibida como parametro de entrada
     * Precondiciones:
     * Entradas:
     * Salidas: boolean que indica si la conexion se cerro correctamente
     * Entrada/Salida: Connection conexion
     * Postcondiciones: Asociado al nombre devuelve un boolean que sera true si la conexion recibida se cerro correctamente y false si no.
     *                  La conexion quedara cerrada.
     * */
    public boolean closeConnection(Connection conexion){
        boolean exito = false;
        try{
            conexion.close();
            if(conexion.isClosed()){
                exito = true;
            }
            //Los buenos tests
/*
            if(conexion.isClosed()){
                System.out.println("La conexion se cerro");
            }else{
                System.out.println("La conexion no se cerro");
            }
*/
        }catch (SQLException e){
            e.getStackTrace();
        }
        return exito;
    }
}
