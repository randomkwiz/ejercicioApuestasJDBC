package gestion;

import java.util.ArrayList;

public class GestionApuestas {

    /*
     * Signatura: public void realizarApuestaTipo1
     * Comentario: método que inserta datos en tabla
     * Precondiciones:
     * Entradas:
     * Salidas:
     * Estrada/Salidas:
     * Postcondiciones:*/
    public void menuTiposApuestas(){ //debe ir en validaciones
        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        int opcionTipoApuesta = pedirValidarMenuTiposApuestas();
            /*TODO: falta método para pedir los datos a insertar al usuario, que incluirán
                todos los datos correspondientes a la elección del usuario*/
        Validar objValidar = new Validar();
        //PedirValidarTipoApuesta

        objValidar.pedirValidarTipoApuesta();
        //PedirValidarCantidadApuesta
        //CalcularCuota
        switch (opcionTipoApuesta){
            case 1:
                //Tipo1
                realizarApuestaNormal(apuesta);
                realizarApuestaTipo1(tipo1);
                break;
            case 2:
                //Tipo2
                realizarApuestaNormal(apuesta);
                realizarApuestaTipo2(tipo2);
                break;
            case 3:
                //Tipo3
                realizarApuestaNormal(apuesta);
                realizarApuestaTipo3(tipo3);
                break;
        }
    }

    //TODO: Error en menú, revisar

    public int pedirValidarMenuTiposApuestas(){
        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        do{
            pintarMenuTiposApuestas();
            opcion = sc.nextInt();
        } while (opcion < 0 || opcion > 4);

        return opcion;
    }

    /*
     * Signatura: public void pintarMenuTiposApuestas
     * Comentario: método que inserta datos en tabla
     * Precondiciones: No hay
     * Entradas: No hay
     * Salidas: No hay
     * Estrada/Salidas: No hay
     * Postcondiciones: No hay*/
    public void pintarMenuTiposApuestas(){
        System.out.println("Elija un tipo de apuesta:" +
                "\n1- Apuesta Tipo 1: por goles equipo local y goles Equipo Visitante" +
                "\n2- Apuesta Tipo 2: por Cantidad de goles y nombre de Equipo" +
                "\n3- Apuesta Tipo 3: por Nombre de Equipo");
    }

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
        //String sentenciaSql = "INSERT INTO Apuestas VALUES(2.3, 10, 2, '2019-01-11 12:00', 2, 3)";
        String sentenciaSql = "INSERT INTO Apuestas VALUES(?, ?, ?, ?, ?, ?)";
        try{
            conexion = objConexion.getConnection();
            if(!conexion.isClosed()){
                preparedStatement = conexion.prepareStatement(sentenciaSql);
                preparedStatement.setInt(1, apuesta.getId());
                preparedStatement.setDouble(2, apuesta.getCuota());
                preparedStatement.setInt(3, apuesta.getCantidad());
                preparedStatement.setDate(4, apuesta.getFechaHora());
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
