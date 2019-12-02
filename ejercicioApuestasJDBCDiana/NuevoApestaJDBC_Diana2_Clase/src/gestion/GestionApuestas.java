package gestion;

import clases.*;
import conexion.ConexionJDBC;
import utilidad.Utilidad;
import validacion.Validar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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













    //Para obtener resultado apuestas anterior


    //Consultar apuesta según fecha
    //TODO añadir a proyecto común

    /*
     * Signatura: public ArrayList<Apuesta> obtenerListaApuestasPorFecha(UsuarioImpl usuarioApuesta)
     * Comentario: obtiene un listado de apuestas según la fecha
     * Precondiciones:
     * Entradas:
     * Salidas: ArrayList de apuesta
     * Postcondiciones: asociado al nombre se devuelve la lista de apuestas según la fecha instroudcida por el usuario
     * */
    public ArrayList<Apuesta> obtenerListaApuestasPorFecha(UsuarioImpl usuarioApuesta){  //Usuario que realiza la consulta, no estoy segura de si haría falta
        Validar objValidar = new Validar();
        ConexionJDBC objConexion = new ConexionJDBC();
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Apuesta apuesta = null;
        PartidoImpl partido;
        GestionPartidos gestionPartidos = new GestionPartidos();
        ArrayList<Apuesta> listaApuestasPorFecha = new ArrayList<>();

        //Fecha original
        GregorianCalendar fechaApuesta = objValidar.pedirValidarFechaHora();
        //Fecha formateada para consulta: mes/dia/año
        String fechaFormatoConversion = objValidar.pedirValidarFechaParaFormatoConversion(fechaApuesta); //Convierto la fecha al formato necesario para la sentencia Sql

        String sentenciaSql = "SELECT * FROM  Apuestas WHERE Convert(VARCHAR(10),fechaHora,101) =  Convert(Varchar(10), ?,101) and id_usuario = ?";


        try{
            conexion = objConexion.getConnection();
            preparedStatement = conexion.prepareStatement(sentenciaSql);
            preparedStatement.setString(1, fechaFormatoConversion);
            preparedStatement.setInt(2, usuarioApuesta.getId());
            resultSet = preparedStatement.executeQuery();

            //Partido según fecha apuesta
            partido = gestionPartidos.obtenerPartidoPorFechaApuesta(fechaApuesta);


            while (resultSet.next()){
                apuesta.setId(resultSet.getInt("id"));
                apuesta.setCuota(resultSet.getInt("cuota"));
                apuesta.setCantidad(resultSet.getDouble("cantidad"));
                apuesta.setTipo(resultSet.getString("tipo").charAt(0)); //Para tipo char
//              //Fecha //TODO revisar
                fechaApuesta.setTime(resultSet.getDate("fechaHora"));
                apuesta.setFechaHora(fechaApuesta); //TODO creo que no hace falta
                apuesta.setUsuario(usuarioApuesta); //TODO creo que no hace falta
                apuesta.setPartido(partido);
//                usuarioApuesta.setId(resultSet.getInt("id_usuario"));

                listaApuestasPorFecha.add(apuesta);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                resultSet.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            objConexion.closeConnection(conexion);
        }
        return listaApuestasPorFecha;
    }

    //TODO añadir a proyecto común

    /*
     * Signatura: public void verResultadosApuesta(UsuarioImpl usuarioApuesta, Apuesta tipoApuesta)
     * Comentario: muestra los resultados de una apuesta anterior
     * Precondiciones: los datos de la apuesta deberán existir en la BBDD
     * Entradas:
     * Salidas: ArrayList de apuesta
     * Postcondiciones:
     * */
    public void verResultadosApuesta(UsuarioImpl usuarioApuesta, Apuesta tipoApuesta) {
        Validar objValidar = new Validar();

        ConexionJDBC objConexion = new ConexionJDBC();
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sentenciaSql = "SELECT * FROM Apuestas WHERE id = ?";
        int id = objValidar.pedirValidarIdApuesta(); //Id para realizar la consulta

        Apuesta apuesta = null;

        //Obtengo lista apuestas
        ArrayList<Apuesta> listaApuestas = obtenerListaApuestasPorFecha(usuarioApuesta);
        //Muestro lista apuestas
        objValidar.mostrarListaApuestasPorFecha(listaApuestas);

        try {
            conexion = objConexion.getConnection();
            preparedStatement = conexion.prepareStatement(sentenciaSql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                apuesta.setId(resultSet.getInt("id"));
                apuesta.setCuota(resultSet.getInt("cuota"));
                apuesta.setCantidad(resultSet.getInt("cantidad"));
                apuesta.setTipo(resultSet.getString("tipo").charAt(0));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        switch (apuesta.getTipo()) {
            case 1:
                ApuestaTipo1 tipo1 = new ApuestaTipo1();
                tipo1.consultarResultadoApuesta(apuesta.getId());
                break;
            case 2:
                ApuestaTipo2 tipo2 = new ApuestaTipo2();
                tipo2.consultarResultadoApuesta(apuesta.getId());
                break;
            case 3:
                ApuestaTipo3 tipo3 = new ApuestaTipo3();
                tipo3.consultarResultadoApuesta(apuesta.getId());
                break;
        }


    }






    //TODO: añadir a proyecto común

    //Metodo que muestra listado apuestas por fecha
//    public void mostrarListadoApuestasPorFecha(GregorianCalendar fechaApuesta){
//        Utilidad objUtilidad = new Utilidad();
//        ConexionJDBC objConexion = new ConexionJDBC();
//        Connection conexion = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//        Apuesta objApuesta = null; //Todo comprobar si está bien, ya que la clase es abstracta
//        ArrayList<Apuesta> listadoApuestas = new ArrayList<Apuesta>();
//        String fechaFormatoNormal = objUtilidad.formatearFecha(fechaApuesta);
//
//        String sentenciaSql = "select * from Apuestas where fechaHora = ?";
//
//        System.out.println("Lista Apuestas");
//        try{
//            conexion = objConexion.getConnection();
//            preparedStatement = conexion.prepareStatement(sentenciaSql);
//            preparedStatement.setString(1, fechaFormatoNormal);
//            resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()){
//                objApuesta.setCuota(resultSet.getDouble("cuota"));
//                objApuesta.setCantidad(resultSet.getDouble("cantidad"));
////                objApuesta.setTipo(resultSet.getString("tipo"));
////                objApuesta.setFechaHora(resultSet.getString("fechaHora"));
////                objApuesta.setUsuario(resultSet.getInt("id_usuario"));
////                objApuesta.setUsuario(resultSet.getInt("id_partido"));
//
//                System.out.println("IdApuesta: " + objApuesta.getId() + ", " );
//                listadoApuestas.add(objApuesta);
//            }
//
//        }catch (SQLException e){
//            e.printStackTrace();
//        }finally {
//            try {
//                preparedStatement.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            objConexion.closeConnection(conexion);
//        }
//    }


//
//    //TODO no está bien planteado, revisar
//    public Apuesta leerApuestaSegunID(int idApuesta){
//        Validar objValidar = new Validar();
//        Utilidad objUtilidad = new Utilidad();
//        ConexionJDBC objConexion = new ConexionJDBC();
//        Connection conexion = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//        Apuesta objApuesta = null;
//        ArrayList<Apuesta> objListaApuestas = new ArrayList<Apuesta>();
//        String sentenciaSql = "SELECT * FROM Apuestas WHERE id = ?";
//
//        System.out.println("Introduce la fecha de las apuestas a buscar");
//        GregorianCalendar fechaApuesta = objValidar.pedirValidarFechaHora(); //TODO: no sé si hacer con fecha entera o sólo dia, mes y año
//        //Consultar los datos de apuestas según fecha
//        mostrarListadoApuestasPorFecha(fechaApuesta);
//
//        try{
//            conexion = objConexion.getConnection();
//            preparedStatement = conexion.prepareStatement(sentenciaSql);
//            preparedStatement.setInt(1, idApuesta);
//            resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()){
//                objApuesta.setCuota(resultSet.getDouble("cuota"));
//                objApuesta.setCantidad(resultSet.getDouble("cantidad"));
////                objApuesta.setTipo(resultSet.getString("tipo"));
////                objApuesta.setFechaHora(resultSet.getString("fechaHora"));
////                objApuesta.setUsuario(resultSet.getInt("id_usuario"));
////                objApuesta.setUsuario(resultSet.getInt("id_partido"));
//            }
//
//            System.out.println("Cuota: " + objApuesta.getCuota() + ", Cantidad: " + objApuesta.getCantidad() + ", Tipo: " + objApuesta.getTipo() + ", Fecha y hora: " + objUtilidad.formatearFecha(objApuesta.getFechaHora() ) + ", Id usuario: " + objApuesta.getUsuario().getId() + ", Id Partido: " + objApuesta.getPartido().getId());
//
//            //Construyo objetos del tipo de apuesta que corresponda
//
//
//        }catch (SQLException e){
//            e.printStackTrace();
//        }finally {
//            try {
//                resultSet.close();
//                preparedStatement.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            objConexion.closeConnection(conexion);
//        }
//
//        return objApuesta;
//    }




}
