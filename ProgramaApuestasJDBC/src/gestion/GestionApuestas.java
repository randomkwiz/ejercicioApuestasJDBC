package gestion;

import clases.Apuesta;
import clases.ApuestaTipo1;
import clases.ApuestaTipo2;
import clases.ApuestaTipo3;
import conexion.ConexionJDBC;
import validacion.Validar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class GestionApuestas {

    /*
    * Signatura: public double calcularCantidadTotalApostadaEnApuestasIguales(Apuesta apuesta, int tipoApuesta)
    * Comentario: calcula la cantidad total apostada en apuestas iguales a la recibida
    * Precondiciones:
    * Entradas:
    * Salidas: double cantidad total apostada en apuestas iguales
    * Postcondiciones: asociado al nombre devuelve la cantidad total apostada en apuestas iguales
    * */
    public double calcularCantidadTotalApostadaEnApuestasIguales(Apuesta apuesta, int tipoApuesta){
        //En resguardo
        return 0.0;
    }

    /*
     * Signatura: public double calcularCantidadTotalApostadaEnApuestasDeEseTipo(Apuesta apuesta, int tipoApuesta)
     * Comentario: calcula la Cantidad Total Apostada En Apuestas De Ese Tipo
     * Precondiciones:
     * Entradas:
     * Salidas: double cantidad total apostada en apuestas de ese tipo
     * Postcondiciones: asociado al nombre devuelve la cantidad total apostada en apuestas de ese tipo
     * */
    public double calcularCantidadTotalApostadaEnApuestasDeEseTipo(Apuesta apuesta, int tipoApuesta){
        ConexionJDBC conexionJDBC = new ConexionJDBC();
        Connection connection = conexionJDBC.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double total = 0.0;

        String miSelect = "select sum(?) as 'cantidad'\n" +
                "from Apuestas\n" +
                "where tipo = ?";
        try {
            //Preparo el statement
            preparedStatement = connection.prepareStatement(miSelect);
            preparedStatement.setDouble(1,apuesta.getCantidad());
            preparedStatement.setInt(2,tipoApuesta);
            //Ejecuto
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                total = resultSet.getDouble("cantidad");
            }
            preparedStatement.close();
            conexionJDBC.closeConnection(connection);
        }catch (SQLException e){
            e.getStackTrace();
        }
        return total;
    }

    /*
    * Signatura: public double calcularCuotaApuesta(Apuesta apuesta, int tipoApuesta)
    * Comentario: calcula la cuota correspondiente a una Apuesta
    * Precondiciones: la cantidad debe ser positiva
    * Entradas: double cantidad de dinero apostada
    * Salidas: double cuota
    * Postcondiciones: asociado al nombre se devuelve la cuota correspondiente.
    * */
    public double calcularCuotaApuesta(Apuesta apuesta, int tipoApuesta){
        double cuota = 0.0;
        //TODO hacer los metodos para calcular estas variables xd
        double cantidadTotalApostadaEnApuestasIguales = 0.0;
        double cantidadTotalApostadaEnApuestasDeEseTipo = 0.0;

        if(apuesta.getCantidad() < 40){
            switch (tipoApuesta){
                case 1:
                    cuota = 4;
                    break;
                case 2:
                    cuota = 3;
                    break;
                case 3:
                    cuota = 1.5;
                    break;
            }
        }else{
            cuota = ((cantidadTotalApostadaEnApuestasDeEseTipo/cantidadTotalApostadaEnApuestasIguales)-1)*0.8;
        }

        return cuota;
    }



    //TODO revisar
    /*
     * Signatura: public void realizarApuestaTipo1
     * Comentario: método que inserta datos en tabla para realizar apuestas
     * Precondiciones:
     * Entradas:
     * Salidas:
     * Estrada/Salidas:
     * Postcondiciones:*/
    public void realizarApuestaNormal(Apuesta apuesta){
        ConexionJDBC objConexion = new ConexionJDBC();
        Connection conexion = null;
        PreparedStatement preparedStatement;
        ResultSet resultset;
        String sentenciaSql = "INSERT INTO Apuestas VALUES(?, ?, ?, ?, ?, ?)";
        try{
            conexion = objConexion.getConnection();
            if(!conexion.isClosed()){
                preparedStatement = conexion.prepareStatement(sentenciaSql);
                preparedStatement.setInt(1, apuesta.getId());
                preparedStatement.setDouble(2, apuesta.getCuota());
                preparedStatement.setDouble(3, apuesta.getCantidad());
            //    preparedStatement.setDate(4, apuesta.getFechaHora());
                preparedStatement.setInt(5, apuesta.getUsuario().getId()); //usuario implementado
                preparedStatement.setInt(6, apuesta.getPartido().getId()); //usuario implementado
            }
        }catch(SQLException e){
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

    public void realizarApuestaTipo1(ApuestaTipo1 apuestaTipo1){
        //Instanciación objetos para operaciones con Base de Datos
        ConexionJDBC objConexion = new ConexionJDBC();
        Connection conexion = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        String sentenciaSql = "INSERT INTO Apuestas_tipo1 VALUES (?, ?, ?, ?)";

        try{
            //Preparación Statement
            conexion =  objConexion.getConnection();
            if(!conexion.isClosed()){
                preparedStatement = conexion.prepareStatement(sentenciaSql);
                preparedStatement.setInt(1, apuestaTipo1.getId()); //TODO: ver cómo consigo el id del usuario, según el usuario logueado
                preparedStatement.setDouble(2,  apuestaTipo1.getApuestasMaximas());
                preparedStatement.setInt(3, apuestaTipo1.getGolesLocal());
                preparedStatement.setInt(4, apuestaTipo1.getGolesVisitante());
            }else{
                //Indica que la conexión está cerrada
                System.out.println("La conexión está cerrada");
            }

        }catch (SQLException ex){
            ex.printStackTrace();
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
    /*
     * Signatura: public void realizarApuestaTipo2
     * Comentario: método que inserta datos en tabla
     * Precondiciones:
     * Entradas:
     * Salidas:
     * Estrada/Salidas:
     * Postcondiciones:*/
    public void realizarApuestaTipo2(ApuestaTipo2 apuestaTipo2){
        ConexionJDBC objConexion = new ConexionJDBC();
        Connection conexion = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        String sentenciaSql = "INSERT INTO Apuestas_tipo1 VALUES (?, ?, ?, ?)";
        try{
            //Preparación Statement
            conexion =  objConexion.getConnection();
            if(!conexion.isClosed()){
                preparedStatement = conexion.prepareStatement(sentenciaSql);
                preparedStatement.setInt(1, apuestaTipo2.getId());
                preparedStatement.setDouble(2,  apuestaTipo2.getApuestasMaximas());
                preparedStatement.setInt(3, apuestaTipo2.getCantidadGoles());
                preparedStatement.setInt(4, apuestaTipo2.getEquipo()); //No entiendo por qué no da error si Equipos es un char
            }else{
                //Indica que la conexión está cerrada
                System.out.println("La conexión está cerrada");
            }

        }catch (SQLException ex){
            ex.printStackTrace();
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
    /*
     * Signatura: public void realizarApuestaTipo3
     * Comentario: método que inserta datos en tabla
     * Precondiciones:
     * Entradas:
     * Salidas:
     * Estrada/Salidas:
     * Postcondiciones:*/
    public void realizarApuestaTipo3(ApuestaTipo3 apuestaTipo3){
        ConexionJDBC objConexion = new ConexionJDBC();
        Connection conexion = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        String sentenciaSql = "INSERT INTO Apuestas_tipo1 VALUES (?, ?, ?)";
        try{
            //Preparación Statement
            conexion =  objConexion.getConnection();
            if(!conexion.isClosed()){
                preparedStatement = conexion.prepareStatement(sentenciaSql);
                preparedStatement.setInt(1, apuestaTipo3.getId());
                preparedStatement.setDouble(2,  apuestaTipo3.getApuestasMaximas());
                preparedStatement.setInt(3, apuestaTipo3.getEquipo()); //No entiendo por qué no da error si Equipos es un char
            }else{
                //Indica que la conexión está cerrada
                System.out.println("La conexión está cerrada");
            }

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
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
