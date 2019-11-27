package gestion;

import clases.Apuesta;
import clases.ApuestaTipo1;
import clases.ApuestaTipo2;
import clases.ApuestaTipo3;
import conexion.ConexionJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        String miSelect = "";
        ConexionJDBC conexionJDBC = new ConexionJDBC();
        Connection connection = conexionJDBC.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double total = 0.0;

        switch (tipoApuesta){
            case 1:
                apuesta = (ApuestaTipo1) apuesta;
                miSelect = "\tSELECT SUM(cantidad) AS TOTAL\n" +
                        "\tFROM Apuestas AS A\n" +
                        "\tINNER JOIN Apuestas_tipo1 AS AT1\n" +
                        "\tON A.id = AT1.id\n" +
                        "\tWHERE AT1.golLocal = ?\n" +
                        "\tAND\n" +
                        "\tAT1.golVisitante = ?";
                break;
            case 2:
                apuesta = (ApuestaTipo2) apuesta;
                miSelect = "\tSELECT SUM(cantidad) AS TOTAL\n" +
                        "\tFROM Apuestas AS A\n" +
                        "\tINNER JOIN Apuestas_tipo2 AS AT2\n" +
                        "\tON A.id = AT2.id\n" +
                        "\tWHERE AT2.gol = ?\n" +
                        "\tAND AT2.puja = ?";
                break;
            case 3:
                apuesta = (ApuestaTipo3) apuesta;
                miSelect = "\tSELECT SUM(cantidad) AS TOTAL\n" +
                        "\tFROM Apuestas AS A\n" +
                        "\tINNER JOIN Apuestas_tipo3 AS AT3\n" +
                        "\tON A.id = AT3.id\n" +
                        "\tWHERE AT3.puja = ?";
                break;
        }

        try {
            //Preparo el statement

            preparedStatement = connection.prepareStatement(miSelect);
            if(apuesta instanceof ApuestaTipo1){
                preparedStatement.setInt(1, ((ApuestaTipo1) apuesta).getGolesLocal());
                preparedStatement.setInt(2, ((ApuestaTipo1) apuesta).getGolesVisitante());
            }else if(apuesta instanceof ApuestaTipo2){
                preparedStatement.setInt(1, ((ApuestaTipo2) apuesta).getCantidadGoles());
                preparedStatement.setString(2, String.valueOf(((ApuestaTipo2) apuesta).getEquipo()));
            }else if(apuesta instanceof  ApuestaTipo3){
                preparedStatement.setString(1, String.valueOf(((ApuestaTipo3) apuesta).getEquipo()));
            }

            //Ejecuto
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                total = resultSet.getDouble("TOTAL");
            }
            preparedStatement.close();
            conexionJDBC.closeConnection(connection);
        }catch (SQLException e){
            e.getStackTrace();
        }
        return total;
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
        //TODO testea estos metodos
        double cantidadTotalApostadaEnApuestasIguales = calcularCantidadTotalApostadaEnApuestasIguales(apuesta, tipoApuesta);
        double cantidadTotalApostadaEnApuestasDeEseTipo = calcularCantidadTotalApostadaEnApuestasDeEseTipo(apuesta, tipoApuesta);

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

}
