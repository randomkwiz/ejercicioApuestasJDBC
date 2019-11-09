package validacion;

import clases.UsuarioImpl;
import conexion.ConexionJDBC;

import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Validar {

    /*
     * Signatura: public String pedirValidarMenuDeseaSalir()
     * Comentario: Este método pinta un mensaje que pregunta al usuario si desea salir del programa
     * Precondiciones: No hay
     * Entradas:
     * Salidas: String que indica si el usuario desea salir o no
     * Entrada/Salida: Nada
     * Postcondiciones: Asociado al nombre se devuelve un String que será "si" si el usuario responde que sí quiere salir
     *                  y sera "no" si el usuario no desea salir.
     * */
    public String pedirValidarMenuDeseaSalir(){
        Scanner sc = new Scanner(System.in);
        String respuesta = "";
        do {
            System.out.println("¿Desea ejecutar el programa? SI/NO");
            respuesta = sc.next().toLowerCase();
        }while ((!respuesta.equals("si") && !respuesta.equals("no")));
    return respuesta;
    }

    /*
     * Signatura: public int pedirValidarCrearCuentaOLogin()
     * Comentario: Este metodo pregunta si el usuario desea crear una cuenta (standard) o iniciar sesion
     * Precondiciones: No hay
     * Entradas:
     * Salidas: int que indica la opcion del usuario
     * Entrada/Salida:
     * Postcondiciones: Asociado al nombre se devuelve un entero que indica la opcion del usuario.
     *                  0 si desea salir
     *                  1 si desea iniciar sesion
     *                  2 si desea crear una cuenta
     * */
    public int pedirValidarCrearCuentaOLogin(){
        Scanner sc = new Scanner(System.in);
        int respuesta = -1;
        do {
            System.out.println("0. Salir");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Crear cuenta");
            respuesta = sc.nextInt();
        }while (respuesta<0 || respuesta >2);
        return respuesta;
    }


    /*
     * Signatura: public UsuarioImpl pedirValidarCredenciales()
     * Comentario: Pide y valida unas credenciales de login
     * Precondiciones:
     * Entradas:
     * Salidas: Objeto usuario con correo y contraseña existentes en la BBDD
     * Entrada/Salida:
     * Postcondiciones: Asociado al nombre se devuelve un objeto usuario con correo y contraseña existentes
     * */
    public UsuarioImpl pedirValidarCredenciales(){
        Scanner sc = new Scanner(System.in);
        UsuarioImpl usuario = new UsuarioImpl();
        String correo = "";
        char[] pw;
        String password = "";
        Console console = System.console();
        boolean existe = false;
        do {
            System.out.println("Introduce tu correo electrónico: ");
            correo = sc.next();
            System.out.println("Introduce tu contraseña: ");

            if(console == null){
             password = sc.next();
            }else {
                pw = console.readPassword();   //con esto hacemos que no se vea en pantalla al escribir
                password = String.valueOf(pw);
            }
        }while (!isValidUser(correo,password));

        usuario.setCorreo(correo);
        usuario.setPassword(password);
        return usuario;
    }

    /*
     * Signatura: public boolean isValidUser(String correo, String password)
     * Comentario: Comprueba si unas credenciales son correctas
     * Precondiciones:
     * Entradas: string correo y string password
     * Salidas: boolean
     * Entrada/Salida:
     * Postcondiciones: Asociado al nombre se devuelve un boolean que indica si la combinacion de correo y contraseña
     *                  existe en la BBDD
     * */
    public boolean isValidUser(String correo, String password){
        ConexionJDBC conexionJDBC = new ConexionJDBC();
        Connection connection = conexionJDBC.getConnection();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        boolean exito = false;

        String miSelect = "select * from Usuarios  where correo = ?\n" +
                "        and\n" +
                "                contraseña = ?";
        try {
            //Preparo el statement
            if(!connection.isClosed()){
                preparedStatement = connection.prepareStatement(miSelect);
                preparedStatement.setString(1, correo);
                preparedStatement.setString(2, password);
                //Ejecuto
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    exito = true;
                }
                //Cierro
                resultSet.close();
                preparedStatement.close();
                conexionJDBC.closeConnection(connection);
            }else {
                System.out.println("La conexion esta cerrada: "+ connection.isClosed());
            }
        }catch (SQLException e){
            e.getStackTrace();
        }
        return exito;
    }

    /*
    * MENU USUARIO ESTANDAR
    * */
    public int pedirValidarMenuUsuarioEstandar(){
        Scanner sc = new Scanner(System.in);
        int opcion = -1;
        do{
            pintarMenuUsuario();
            opcion = sc.nextInt();
        }while (opcion < 0 || opcion >6);

        return opcion;
    }

    /*
     * MENU USUARIO ADMINISTRADOR
     * */
    public int pedirValidarMenuAdministrador(){
        Scanner sc = new Scanner(System.in);
        int opcion = -1;
        do{
            pintarMenuUsuario();
            pintarOpcionesAdministrador();
            opcion = sc.nextInt();
        }while (opcion < 0 || opcion >11);

        return opcion;
    }

    private void pintarMenuUsuario(){
        System.out.println("0. Salir");
        System.out.println("1. Realizar apuesta");
        System.out.println("2. Ver los partidos disponibles para apostar");
        System.out.println("3. Comprobar el resultado de una apuesta anterior");
        System.out.println("4. Hacer un ingreso en la cuenta");
        System.out.println("5. Hacer una retirada de dinero");
        System.out.println("6. Ver movimientos de la cuenta");
    }

    private void pintarOpcionesAdministrador(){
        System.out.println("7. Crear un nuevo partido");
        System.out.println("8. Abrir un partido para que acepte apuestas");
        System.out.println("9. Cerrar un partido para que no se pueda apostar");
        System.out.println("10. Consultar las apuestas de un partido");
        System.out.println("11. Pagar las apuestas ganadoras de un partido finalizado");
    }
}
