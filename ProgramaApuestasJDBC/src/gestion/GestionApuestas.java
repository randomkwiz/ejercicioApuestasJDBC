package gestion;

import clases.Apuesta;
import clases.ApuestaTipo1;
import clases.ApuestaTipo2;
import clases.ApuestaTipo3;
import conexion.ConexionJDBC;

import java.lang.reflect.Type;
import java.sql.*;

public class GestionApuestas {

    /*
    * Signatura: public boolean realizarApuesta(Apuesta apuesta)
    * Comentario: Este método llama al procedure de insertar apuesta de la BBDD y realiza
    * la inserción de la apuesta recibida como parámetro.
    * Precondiciones: La apuesta debe estar correctamente construida.
    * Entradas: objeto apuesta
    * Salidas: boolean que indicará si la operación se realizó con éxito
    * Postcondiciones: Asociado al nombre se devolverá un boolean que será true si la operación finalizó con éxito
    * o no. En caso afirmativo se insertará la apuesta indicada.
    * */
    public boolean realizarApuesta(Apuesta apuesta){
        CallableStatement cstmt = null;
        ResultSet rs = null;
        ConexionJDBC conexionJDBC = new ConexionJDBC();
        Connection connection ;
        int results = 0;
        boolean exito = false;
        try {
            connection = conexionJDBC.getConnection();
            cstmt = connection.prepareCall(
                    "{call insertarApuesta(?,?,?,?,?,?,?,?,?)}"
                    );

            cstmt.setDouble("cuota", apuesta.getCuota());
            cstmt.setDouble("cantidad", apuesta.getCantidad());
            cstmt.setString("tipo", String.valueOf(apuesta.getTipo()));
            cstmt.setInt("IDUsuario", apuesta.getUsuario().getId());
            cstmt.setInt("IDPartido", apuesta.getPartido().getId());

            switch (apuesta.getTipo()){
                case '1':
                    apuesta = (ApuestaTipo1) apuesta;
                    cstmt.setInt("golLocal", ((ApuestaTipo1) apuesta).getGolesLocal());
                    cstmt.setInt("golVisitante", ((ApuestaTipo1) apuesta).getGolesVisitante());


                    //Lo demas lo ponemos a null
                    cstmt.setNull("gol",java.sql.Types.INTEGER );
                    cstmt.setNull("puja", Types.CHAR );
                    break;
                case '2':
                    apuesta = (ApuestaTipo2) apuesta;
                    cstmt.setInt("gol", ((ApuestaTipo2) apuesta).getCantidadGoles());
                    cstmt.setString("puja", String.valueOf(((ApuestaTipo2) apuesta).getEquipo()));


                    //Lo demas lo ponemos a null
                    cstmt.setNull("golLocal", Types.INTEGER);
                    cstmt.setNull("golVisitante", Types.INTEGER);

                    break;
                case '3':
                    apuesta = (ApuestaTipo3) apuesta;
                    cstmt.setString("puja", String.valueOf(((ApuestaTipo3) apuesta).getEquipo()));

                    //Lo demas lo ponemos a null
                    cstmt.setNull("golLocal", Types.INTEGER);
                    cstmt.setNull("golVisitante", Types.INTEGER);
                    cstmt.setNull("gol", Types.INTEGER);
                    break;
            }

            results = cstmt.executeUpdate();
            if(results == 1){
                exito = true;
            }


            cstmt.close();
            conexionJDBC.closeConnection(connection);

        } catch (Exception e) {
            e.getStackTrace();
        }
        return exito;
    }

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
        Connection connection ;
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
            connection = conexionJDBC.getConnection();
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
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double total = 0.0;

        String miSelect = "select sum(?) as 'cantidad'\n" +
                "from Apuestas\n" +
                "where tipo = ?";
        try {
            connection = conexionJDBC.getConnection();
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
            cantidadTotalApostadaEnApuestasIguales = calcularCantidadTotalApostadaEnApuestasIguales(apuesta, tipoApuesta);
            cantidadTotalApostadaEnApuestasDeEseTipo = calcularCantidadTotalApostadaEnApuestasDeEseTipo(apuesta, tipoApuesta);

            cuota = ((cantidadTotalApostadaEnApuestasDeEseTipo/cantidadTotalApostadaEnApuestasIguales)-1)*0.8;
        }

        return cuota;
    }

}
