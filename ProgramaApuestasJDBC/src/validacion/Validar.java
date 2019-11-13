    package validacion;

    import clases.IngresoImpl;
    import clases.UsuarioImpl;
    import conexion.ConexionJDBC;

    import java.io.Console;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.InputMismatchException;
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
        public String pedirValidarMenuDeseaSalir() {
            Scanner sc = new Scanner(System.in);
            String respuesta = "";
            try {
                do {

                    System.out.println("¿Desea ejecutar el programa? SI/NO");
                    respuesta = sc.next().toLowerCase();

                } while ((!respuesta.equals("si") && !respuesta.equals("no")));
            } catch (InputMismatchException e) {
                System.out.println("Introduce un formato valido");
                respuesta = pedirValidarMenuDeseaSalir();
            }
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
        public int pedirValidarCrearCuentaOLogin() {
            Scanner sc = new Scanner(System.in);
            int respuesta = 0;
            try {
                do {
                    System.out.println("0. Salir");
                    System.out.println("1. Iniciar sesión");
                    System.out.println("2. Crear cuenta");
                    respuesta = sc.nextInt();
                } while (respuesta < 0 || respuesta > 2);
            } catch (InputMismatchException e) {
                System.out.println("Introduce un formato valido");
                respuesta = pedirValidarCrearCuentaOLogin();
            }
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
        public UsuarioImpl pedirValidarCredenciales() {
            Scanner sc = new Scanner(System.in);
            UsuarioImpl usuario = new UsuarioImpl();
            String correo = "";
            char[] pw;
            String password = "";
            Console console = System.console();
            boolean existe = false;
            try {
                do {

                    System.out.println("Introduce tu correo electrónico: ");
                    correo = sc.next();
                    System.out.println("Introduce tu contraseña: ");
                    if (console == null) {
                        password = sc.next();
                    } else {
                        pw = console.readPassword();   //con esto hacemos que no se vea en pantalla al escribir
                        password = String.valueOf(pw);
                    }

                } while (!isValidUser(correo, password));
                
            } catch (Exception e) {
                System.out.println("Introduce un formato valido");
                usuario = pedirValidarCredenciales();
            }
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
        public boolean isValidUser(String correo, String password) {
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
                if (!connection.isClosed()) {
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
                } else {
                    System.out.println("La conexion esta cerrada: " + connection.isClosed());
                }
            } catch (SQLException e) {
                e.getStackTrace();
            }
            return exito;
        }

        /*
         * MENU USUARIO ESTANDAR
         * */
        public int pedirValidarMenuUsuarioEstandar() {
            Scanner sc = new Scanner(System.in);
            int opcion = -1;
            try {
                do {
                    pintarMenuUsuario();
                    opcion = sc.nextInt();
                } while (opcion < 0 || opcion > 6);
            } catch (InputMismatchException e) {
                System.out.println("Introduce un formato valido");
                opcion = pedirValidarMenuUsuarioEstandar();
            }
            return opcion;
        }

        /*
         * MENU USUARIO ADMINISTRADOR
         * */
        public int pedirValidarMenuAdministrador() {
            Scanner sc = new Scanner(System.in);
            int opcion = -1;
            try {
                do {
                    pintarMenuUsuario();
                    pintarOpcionesAdministrador();
                    opcion = sc.nextInt();
                } while (opcion < 0 || opcion > 11);
            } catch (InputMismatchException e) {
                System.out.println("Introduce un formato valido");
                opcion = pedirValidarMenuAdministrador();
            }
            return opcion;
        }

        /*
         * Menu usuario
         * */
        private void pintarMenuUsuario() {
            System.out.println("0. Salir");
            System.out.println("1. Realizar apuesta");
            System.out.println("2. Ver los partidos disponibles para apostar");
            System.out.println("3. Comprobar el resultado de una apuesta anterior");
            System.out.println("4. Hacer un ingreso en la cuenta");
            System.out.println("5. Hacer una retirada de dinero");
            System.out.println("6. Ver movimientos de la cuenta");
        }

        /*
         * Menu administrador
         * */
        private void pintarOpcionesAdministrador() {
            System.out.println("7. Crear un nuevo partido");
            System.out.println("8. Abrir un partido para que acepte apuestas");
            System.out.println("9. Cerrar un partido para que no se pueda apostar");
            System.out.println("10. Consultar las apuestas de un partido");
            System.out.println("11. Pagar las apuestas ganadoras de un partido finalizado");
        }

        /*
         * Signatura: public double pedirValidarCantidadIngresoDinero()
         * Comentario: pide y valida una cantidad de dinero para INGRESAR en la cuenta
         * Precondiciones:
         * Entradas:
         * Salidas: entero que sera la cantidad de dinero a ingresar
         * Entrada/Salida:
         * Postcondiciones: Asociado al nombre se devuelve un double que sera la cantidad a ingresar. La cantidad sera superior a 0.0
         * */

        public double pedirValidarCantidadIngresoDinero() {
            Scanner sc = new Scanner(System.in);
            double cantidad = 0.0;
            try {
                do {

                    System.out.println("Introduzca la cantidad que desea ingresar: ");
                    cantidad = sc.nextDouble();

                } while (cantidad <= 0.0);
            } catch (InputMismatchException e) {
                System.out.println("Introduce una cantidad en formato 0.00");
                cantidad = pedirValidarCantidadIngresoDinero();
            }
            return cantidad;
        }

        /*
         * Signatura: public String pedirValidarDescripcionMovimientoDinero()
         * Comentario: pide y valida una descripcion para un movimiento de saldo
         * Precondiciones:
         * Entradas:
         * Salidas: String que sera la descripcion del movimiento
         * Entrada/Salida:
         * Postcondiciones: Asociado al nombre se devuelve un String que sera la descripcion del movimiento.
         *                  No puede ser una cadena vacia.
         * */
        public String pedirValidarDescripcionMovimientoDinero() {
            Scanner sc = new Scanner(System.in);
            String descripcion = "";
            try {
                do {

                    System.out.println("Introduzca la descripcion: ");
                    descripcion = sc.nextLine();
                } while (descripcion.equals(""));
            } catch (InputMismatchException e) {
                System.out.println("Introduce un formato valido");
                descripcion = pedirValidarDescripcionMovimientoDinero();
            }
            return descripcion;
        }

        /*
         * Signatura: public boolean pedirValidarEstaSeguroDeseaRealizarMovimiento(IngresoImpl movimiento)
         * Comentario: pide y valida si el usuario esta seguro de realizar un movimiento de saldo
         * Precondiciones:
         * Entradas: objeto ingreso que es el movimiento de saldo
         * Salidas: boolean que indica si el usuario da el visto bueno o no
         * Entrada/Salida:
         * Postcondiciones: Asociado al nombre se devuelve un boolean que sera true si el usuario esta de acuerdo y false si no.
         * */
        public boolean pedirValidarEstaSeguroDeseaRealizarMovimiento(IngresoImpl movimiento){
            Scanner sc = new Scanner(System.in);
            boolean estaSeguro = false;
            String respuesta = "";
            try {
                do {
                    System.out.println("Movimiento: ");
                    System.out.println("Cantidad: "+movimiento.getCantidad());
                    System.out.println("Descripcion: "+ movimiento.getDescripcion());
                    System.out.println("¿Esta seguro de que desea realizar el movimiento? SI/NO");
                    respuesta = sc.next().toLowerCase();
                } while (!respuesta.equals("si") && !respuesta.equals("no"));
                if(respuesta.equals("si")){
                    estaSeguro = true;
                }
            }catch (InputMismatchException e){
                estaSeguro = pedirValidarEstaSeguroDeseaRealizarMovimiento(movimiento);
            }
            return estaSeguro;
        }
    }
