package gestion;

import clases.*;
import conexion.ConexionJDBC;
import validacion.Validar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

public class GestionApuestas {

//    /*Los métodos que antes estaban override en Apuestas*/
//
//
//    /*
//     * Signatura: public void consultarResultadoApuestaTipo1(int idApuesta)
//     * Comentario: muestra los resultados de una apuesta anterior del tipo1
//     * Precondiciones: los datos de la apuesta deberán existir en la BBDD. El idApuesta lo habrá consultado el usuario
//     *                 previamente para saber qué Apuesta concreta debe consultar.
//     * Entradas: entero idApuesta
//     * Salidas:
//     * Postcondiciones:
//     * */
//
//    public void consultarResultadoApuestaTipo1(int idApuesta) {
//        ConexionJDBC objConexion = new ConexionJDBC();
//        Connection conexion = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultset = null;
//        ApuestaTipo1 apuestaTipo1 = new ApuestaTipo1();
//        String sentenciaSql = "SELECT * FROM Apuestas_tipo1 WHERE id = ?";
//
//        try{
//            conexion = objConexion.getConnection();
//            preparedStatement = conexion.prepareStatement(sentenciaSql);
//            preparedStatement.setInt(1, idApuesta);
//            resultset = preparedStatement.executeQuery();
//
//            while (resultset.next()){
//                apuestaTipo1.setId(resultset.getInt("id"));
//
//                apuestaTipo1.setGolesLocal(resultset.getInt("golLocal"));
//                apuestaTipo1.setGolesVisitante(resultset.getInt("golVisitante"));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                resultset.close();
//                preparedStatement.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            objConexion.closeConnection(conexion);
//        }
//        //TODO ver si esto podemos sacarlo fuera
//        System.out.println("Id: " + apuestaTipo1.getId()  + " Goles Local: " + apuestaTipo1.getGolesLocal() + " Goles Visitante: " + apuestaTipo1.getGolesVisitante());
//    }
//
//    /*
//     * Signatura: public void consultarResultadoApuestaTipo2(int idApuesta)
//     * Comentario: los datos de la apuesta deberán existir en la BBDD. El idApuesta lo habrá consultado el usuario
//     *             previamente para saber qué Apuesta concreta debe consultar.
//     * Precondiciones: los datos de la apuesta deberán existir en la BBDD
//     * Entradas: entero idApuesta
//     * Salidas:
//     * Postcondiciones:
//     * */
//
//    public void consultarResultadoApuestaTipo2(int idApuesta) {
//        ConexionJDBC objConexion = new ConexionJDBC();
//        Connection conexion = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultset = null;
//        ApuestaTipo2 apuestaTipo2 = new ApuestaTipo2();
//        String sentenciaSql = "SELECT * FROM Apuestas_tipo2 WHERE id = ?";
//
//        try{
//            conexion = objConexion.getConnection();
//            preparedStatement = conexion.prepareStatement(sentenciaSql);
//            preparedStatement.setInt(1, idApuesta);
//            resultset = preparedStatement.executeQuery();
//
//            while (resultset.next()){
//                apuestaTipo2.setId(resultset.getInt("id"));
//
//                apuestaTipo2.setCantidadGoles(resultset.getInt("gol"));
//                apuestaTipo2.setEquipo(resultset.getString("puja").charAt(0));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                resultset.close();
//                preparedStatement.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            objConexion.closeConnection(conexion);
//        }
//        //TODO ver si esto podemos sacarlo fuera
//        System.out.println("Id: " + apuestaTipo2.getId()  + " Goles : " + apuestaTipo2.getCantidadGoles() + " Puja: " + apuestaTipo2.getEquipo());
//
//    }
//
//    /*
//     * Signatura: public void consultarResultadoApuestaTipo3(int idApuesta)
//     * Comentario: muestra los resultados de una apuesta anterior del tipo3
//     * Precondiciones: los datos de la apuesta deberán existir en la BBDD. El idApuesta lo habrá consultado el usuario
//     *                 previamente para saber qué Apuesta concreta debe consultar.
//     * Entradas: entero idApuesta
//     * Salidas:
//     * Postcondiciones:
//     * */
//    public void consultarResultadoApuestaTipo3(int idApuesta) {
//        ConexionJDBC objConexion = new ConexionJDBC();
//        Connection conexion = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultset = null;
//        ApuestaTipo3 apuestaTipo3 = new ApuestaTipo3();
//        String sentenciaSql = "SELECT * FROM Apuestas_tipo3 WHERE id = ?";
//
//        try{
//            conexion = objConexion.getConnection();
//            preparedStatement = conexion.prepareStatement(sentenciaSql);
//            preparedStatement.setInt(1, idApuesta);
//            resultset = preparedStatement.executeQuery();
//
//            while (resultset.next()){
//                apuestaTipo3.setId(resultset.getInt("id"));
//
//                apuestaTipo3.setEquipo(resultset.getString("puja").charAt(0));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                resultset.close();
//                preparedStatement.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            objConexion.closeConnection(conexion);
//        }
//        //TODO ver si esto se puede sacar fuera
//        System.out.println("Id: " + apuestaTipo3.getId()  + " Puja: " + apuestaTipo3.getEquipo());
//    }

    //TODO añadir comun
    /*
     * Signatura: public void verResultadoApuesta(ArrayList<Apuesta> listaApuestas)
     * Comentario: indica si se ha ganado la apuesta o no
     * Precondiciones: la lista deberá esta correctamente validada
     * Entradas: ArrayList<Apuesta> listaApuestas
     * Salidas:
     * Postcondiciones:
     * */
    public void verResultadoApuesta(ArrayList<Apuesta> listaApuestas){ //Funciona
        Validar validar = new Validar();
        int opcion;
        boolean exito;

        validar.mostrarListaApuestas(listaApuestas);
        opcion = validar.pedirValidarOpcionApuesta();
        exito = ejecutarCallStatement(listaApuestas.get(opcion).getId());  //TODO hacer método y descomentar
        if(exito){
            System.out.println("Ha ganado la apuesta");
        }else{
            System.out.println("Ha perdido la apuesta");
        }
    }

//TODO añadir comun
    /*
     * Signatura: public boolean ejecutarCallStatement(int idApuesta)
     * Comentario: ejecuta el procedimiento comprobarApuestaAcertada de la BBDD que comprueba el resultado de la apuesta
     * Precondiciones: idApuesta estará validado
     * Entradas: Entero idApuesta
     * Salidas: boolean resultadoApuesta
     * Postcondiciones: Asociado al nombre se devuelve un boolean que indica si la apuesta es ganadora (true) o no (false)
     * */
    public boolean ejecutarCallStatement(int idApuesta){ //Funciona
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
        int id = -1;
        ConexionJDBC objConexion = new ConexionJDBC();
        Connection conexion = null;
        CallableStatement callableStatement;
        boolean resultadoApuesta = false;

        try{
            conexion = objConexion.getConnection();
            // Llama al procedimiento almacenado
            callableStatement = conexion.prepareCall("{call comprobarApuestaAcertada(?, ?, ?)}");
            callableStatement.setInt(1, idApuesta);
//            callableStatement.registerOutParameter(2, Types.CHAR); //si falla comprobar: java.sql.Types.CHAR
            callableStatement.setString(2, String.valueOf(Types.CHAR));
            callableStatement.registerOutParameter(3, Types.BIT);
            //Ejecuta procedimiento
            callableStatement.execute();
            //Resultado de callableStatement
            resultadoApuesta = callableStatement.getBoolean(3);

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (conexion != null) {
                objConexion.closeConnection(conexion);
            }
        }
        return resultadoApuesta;
    }


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
                    cstmt.setNull("gol", Types.INTEGER );
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
