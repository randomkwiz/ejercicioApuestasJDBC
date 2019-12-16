    package validacion;

    import clases.*;
    import conexion.ConexionJDBC;
    import gestion.GestionApuestas;
    import gestion.GestionPartidos;
    import utilidad.Utilidad;

    import java.io.Console;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.*;

    public class Validar {


        /*
         * Signatura: public ArrayList<Apuesta> validarListaApuestasPorFecha(UsuarioImpl usuarioApuesta, GregorianCalendar fechaApuesta)
         * Comentario: obtiene un listado de apuestas según la fecha
         * Precondiciones:
         * Entradas:
         * Salidas: ArrayList de apuesta
         * Postcondiciones: asociado al nombre se devuelve la lista de apuestas según la fecha instroudcida por el usuario
         * */
        public ArrayList<Apuesta> validarListaApuestasPorFecha(UsuarioImpl usuarioApuesta, GregorianCalendar fechaApuesta) {  //Usuario que realiza la consulta, no estoy segura de si haría falta

            ConexionJDBC objConexion = new ConexionJDBC();
            Connection conexion = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            Apuesta apuesta = null;
            PartidoImpl partido;
            //ArrayList<PartidoImpl> listadoPartidos = new ArrayList<>();
            GestionPartidos gestionPartidos = new GestionPartidos();
            ArrayList<Apuesta> listaApuestasPorFecha = new ArrayList<>();

            //TODO revisar consulta de clase revisada con Leo
            //String sentenciaSql = "SELECT * FROM  Apuestas WHERE Convert(VARCHAR(10),fechaHora,101) =  ? and id_usuario = ?"; //Hecha con Leo
            String sentenciaSql = "select * from Apuestas as a\n" +
                    "full join Apuestas_tipo1 as at1\n" +
                    "on a.id = at1.id\n" +
                    "full join Apuestas_tipo2 as at2\n" +
                    "on a.id = at2.id\n" +
                    "full join Apuestas_tipo3 as at3\n" +
                    "on a.id = at3.id\n" +
                    "\n" +
                    "where DAY(fechaHora) = ?\n" +
                    "and\n" +
                    "MONTH(fechaHora) = ?\n" +
                    "and\n" +
                    "YEAR(fechaHora) = ?\n" +
                    "and \n" +
                    "id_usuario = ?";

            try {
                conexion = objConexion.getConnection();
                preparedStatement = conexion.prepareStatement(sentenciaSql);

                if (fechaApuesta != null) {
                    preparedStatement.setInt(1, fechaApuesta.get(Calendar.DAY_OF_MONTH));
                    preparedStatement.setInt(2, (fechaApuesta.get(Calendar.MONTH) + 1));  //porque calendar va de 0 a 11
                    preparedStatement.setInt(3, fechaApuesta.get(Calendar.YEAR));
                } else {
                    preparedStatement.setInt(1, 0);
                    preparedStatement.setInt(2, 0);
                    preparedStatement.setInt(3, 0);

                }

                preparedStatement.setInt(4, usuarioApuesta.getId());
                resultSet = preparedStatement.executeQuery();
                String equipo = "";
                while (resultSet.next()) {

                    switch (resultSet.getString("tipo").charAt(0)) {
                        case '1':
                            apuesta = new ApuestaTipo1();
                            ((ApuestaTipo1) apuesta).setGolesLocal(resultSet.getInt("golLocal"));
                            ((ApuestaTipo1) apuesta).setGolesVisitante(resultSet.getInt("golVisitante"));
                            break;
                        case '2':
                            apuesta = new ApuestaTipo2();
                            equipo = resultSet.getString("puja");
                            ((ApuestaTipo2) apuesta).setCantidadGoles(resultSet.getInt("gol"));
                            ((ApuestaTipo2) apuesta).setEquipo(equipo.charAt(0));
                            break;
                        case '3':
                            apuesta = new ApuestaTipo3();
                            equipo = resultSet.getString("puja");
                            ((ApuestaTipo3) apuesta).setEquipo(equipo.charAt(0));
                            break;
                    }

                    apuesta.setId(resultSet.getInt("id"));
                    apuesta.setCuota(resultSet.getDouble("cuota"));
                    apuesta.setCantidad(resultSet.getDouble("cantidad"));
                    apuesta.setTipo(resultSet.getString("tipo").charAt(0)); //Para tipo char

                    fechaApuesta.setTime(resultSet.getDate("fechaHora"));
                    apuesta.setFechaHora(fechaApuesta);
                    apuesta.setUsuario(usuarioApuesta);

                    //TODO hace falta un metodo de obtener partido por ID
                    apuesta.setPartido(new PartidoImpl(resultSet.getInt("id_partido")));    //apaño

                    listaApuestasPorFecha.add(apuesta);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
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


        /*
         * Signatura: public UsuarioImpl pedirValidarDatosUsuario()
         * Comentario: crea un objeto usuario
         * Precondiciones:
         * Entradas:
         * Salidas: Objeto UsuarioImpl
         * Entrada/Salida:
         * Postcondiciones: Asicado al nombre devuelve un objeto UsuarioImpl con los datos validados
         * */
        public UsuarioImpl pedirValidarDatosUsuario() {

            UsuarioImpl nuevoUsuario = new UsuarioImpl();

            String correo = pedirValidarCorreo();
            String password = pedirValidarPassword();
            boolean isAdmin = pedirValidarIsAdministrador();
            double cantidadActualDinero = pedirValidarCantidadDinero(); //TODO 1: poner debajo de validarPassword, modificar en común

            nuevoUsuario.setCantidadActualDinero(cantidadActualDinero);
            nuevoUsuario.setCorreo(correo);
            nuevoUsuario.setPassword(password);
            nuevoUsuario.setAdmin(isAdmin);

            return nuevoUsuario;
        }


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
                    correo = sc.nextLine();
                    System.out.println("Introduce tu contraseña: ");
                    if (console == null) {
                        password = sc.nextLine();
                    } else {
                        pw = console.readPassword();   //con esto hacemos que no se vea en pantalla al escribir
                        password = String.valueOf(pw);
                    }

                } while (!isValidUser(correo, password));

            } catch (Exception e) {
                e.printStackTrace();
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
            Connection connection;
            PreparedStatement preparedStatement;
            ResultSet resultSet;
            boolean exito = false;

            String miSelect = "select * from Usuarios  where correo = ?\n" +
                    "        and\n" +
                    "                contraseña = ?";
            try {
                connection = conexionJDBC.getConnection();
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
         * Signatura: public double pedirValidarCantidadDinero()
         * Comentario: pide y valida una cantidad de dinero para INGRESAR en la cuenta
         * Precondiciones:
         * Entradas:
         * Salidas: entero que sera la cantidad de dinero a ingresar
         * Entrada/Salida:
         * Postcondiciones: Asociado al nombre se devuelve un double que sera la cantidad a ingresar. La cantidad sera superior a 0.0
         * */

        public double pedirValidarCantidadDinero() {
            Scanner sc = new Scanner(System.in);
            double cantidad = 0.0;
            try {
                do {

                    System.out.println("Introduzca la cantidad : ");
                    cantidad = sc.nextDouble();

                } while (cantidad <= 0.0);
            } catch (InputMismatchException e) {
                System.out.println("Introduce una cantidad en formato 0.00");
                cantidad = pedirValidarCantidadDinero();
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
        public boolean pedirValidarEstaSeguroDeseaRealizarMovimiento(IngresoImpl movimiento) {
            Scanner sc = new Scanner(System.in);
            boolean estaSeguro = false;
            String respuesta = "";
            try {
                do {
                    System.out.println("Movimiento: ");
                    System.out.println("Cantidad: " + movimiento.getCantidad());
                    System.out.println("Descripcion: " + movimiento.getDescripcion());
                    System.out.println("¿Esta seguro de que desea realizar el movimiento? SI/NO");
                    respuesta = sc.next().toLowerCase();
                } while (!respuesta.equals("si") && !respuesta.equals("no"));
                if (respuesta.equals("si")) {
                    estaSeguro = true;
                }
            } catch (InputMismatchException e) {
                estaSeguro = pedirValidarEstaSeguroDeseaRealizarMovimiento(movimiento);
            }
            return estaSeguro;
        }


        /*Signatura:public PartidoImpl pedirValidarPartidoDeUnaLista(ArrayList<PartidoImpl> listadoPartidos)
         * Comentario:
         *   A partir de una lista de partidos, los muestra en pantalla y pide al usuario
         *   que escoja uno de ellos. Devuelve el partido seleccionado.
         * Precondiciones:
         * Entradas: lista de partidos
         * Salidas: partido seleccionado
         * Postcondiciones: Si no hay partidos en la lista recibida, se mostrará un mensaje acorde a la situación.
         *                   y se devolverá null.
         *                  Si sí hay partidos, se devolverá el partido seleccionado.
         * */
        public PartidoImpl pedirValidarPartidoDeUnaLista(ArrayList<PartidoImpl> listadoPartidos) {
            int opcion = 0;
            Scanner sc = new Scanner(System.in);
            PartidoImpl partido = null;
            if (listadoPartidos.size() > 0) {
                try {
                    do {
                        mostrarPartidosComoUnMenu(listadoPartidos);
                        System.out.println("Partido Nº: ");
                        opcion = sc.nextInt() - 1;   //porque las opciones empiezan en 1
                    } while (opcion < 0 || opcion >= listadoPartidos.size());
                    partido = listadoPartidos.get(opcion);
                } catch (InputMismatchException e) {
                    partido = pedirValidarPartidoDeUnaLista(listadoPartidos);
                }
            } else {
                System.out.println("No existen partidos disponibles");
            }
            return partido;
        }

        /*Signatura:
            Comentario: muestra en pantalla un listado de partidos como un menu con todos los datos de los partidos.
            Postcondiciones: Si no hay partidos en el arraylist mostrará un mensaje indicando que no hay partidos.
        * */
        private void mostrarPartidosComoUnMenu(ArrayList<PartidoImpl> listadoPartidos) {
            Utilidad utilidad = new Utilidad();
            if (listadoPartidos.size() > 0) {
                for (int i = 0; i < listadoPartidos.size(); i++) {
                    System.out.println("Partido Nº: " + (i + 1));
                    System.out.println("Equipo local: ");
                    System.out.println(listadoPartidos.get(i).getNombreLocal());
                    System.out.println("Goles local: ");
                    System.out.println(listadoPartidos.get(i).getGolesLocal());
                    System.out.println("Equipo visitante: ");
                    System.out.println(listadoPartidos.get(i).getNombreVisitante());
                    System.out.println("Goles visitante: ");
                    System.out.println(listadoPartidos.get(i).getGolesVisitante());
                    System.out.println("Fecha inicio: ");
                    System.out.println(utilidad.formatearFecha(listadoPartidos.get(i).getFechaInicio()));
                    System.out.println("Fecha fin: ");
                    System.out.println(utilidad.formatearFecha(listadoPartidos.get(i).getFechaFin()));
                    System.out.print("Periodo de apuestas: ");
                    if (listadoPartidos.get(i).isPeriodoApuestasAbierto()) {
                        System.out.println("Abierto");
                    } else {
                        System.out.println("Cerrado");
                    }

                }
            } else {
                System.out.println("No existen partidos");
            }
            System.out.println("------------------------------------------");
        }

        /* prototipo: public void MostrarListadoPartidosAApostar(ArrayList<PartidoImpl> listadoPartidos)
         * Comentario: pinta la lista de los partidos disponibles para apostar
         * Precondiciones: array lleno
         * Entradas: lista de partidos
         * Salidas: no hay
         * Entrada/salida: no hay
         * Postcondiciones: no hay, solo pinta una lista de los partidos disponibles para apostar
         * */
        public void MostrarListadoPartidosAApostar(ArrayList<PartidoImpl> listadoPartidos) {
            mostrarPartidosComoUnMenu(listadoPartidos);
        }
        /*-------------------------------------------------------*/

        //VALIDACIONES DIANA
        //Validaciones Ejercicio1: realizar Apuesta

        //PedirValidarTipoApuesta: comprueba que el numero de tipo de apuesta sea 1, 2 o 3
        public int pedirValidarTipoApuesta() {
            Scanner sc = new Scanner(System.in);
            int tipoApuesta = 0;
            try {
                do {
                    pintarMenuTiposApuestas();
                    tipoApuesta = sc.nextInt();
                    if (tipoApuesta < 1 || tipoApuesta > 3) {
                        System.out.println("La opción no es correcta, vuelva a introducir un tipo de apuesta");
                    }
                } while (tipoApuesta < 1 || tipoApuesta > 3);
            } catch (InputMismatchException e) {
                tipoApuesta = pedirValidarTipoApuesta();
            }
            return tipoApuesta;
        }

        //Menú para pedir tipo de apuesta a realizar
        public void pintarMenuTiposApuestas() {
            System.out.println("Elija un tipo de apuesta:" +
                    "\n1- Apuesta Tipo 1: por goles equipo local y goles Equipo Visitante" +
                    "\n2- Apuesta Tipo 2: por Cantidad de goles y nombre de Equipo" +
                    "\n3- Apuesta Tipo 3: por Nombre de Equipo");
        }

        /*
        //PedirValidarCantidadApuesta
        public int pedirValidarCantidadApuesta(){
            Scanner sc = new Scanner(System.in);
            int cantidadApuesta = 0;
            try{
                do{
                    cantidadApuesta = sc.nextInt();
                    if(cantidadApuesta<0){
                        System.out.println("No puede introducir una cantidad negativa, vuelva a introducirla");
                    }
                }while (cantidadApuesta < 0);
            }catch (InputMismatchException e){
                cantidadApuesta = pedirValidarCantidadApuesta();
            }

            return cantidadApuesta;
        }
*/
        /*
         * Signatura: public GregorianCalendar pedirValidarFechaHora()
         * Comentario: pide y valida una fecha y hora
         * Precondiciones:
         * Entradas:
         * Salidas: objeto GregorianCalendar
         * Postcondiciones: Asociado al nombre devuelve un objeto GregorianCalendar con la fecha y hora
         * validada que haya introducido el usuario.
         * */
        public GregorianCalendar pedirValidarFechaHora() {
            GregorianCalendar fecha = null;
            GregorianCalendar fechaReal;
            int dia, mes, anio, hora, minutos;
            do {
                dia = pedirValidarDia();
                mes = pedirValidarMes() - 1; //porque calendar va de 0 a 11
                anio = pedirValidarAnio();
                hora = pedirValidarHora();
                minutos = pedirValidarMinutos();
                fechaReal = new GregorianCalendar(anio, mes, dia, hora, minutos);

                if (fechaReal.get(Calendar.DAY_OF_MONTH) == dia
                        && fechaReal.get(Calendar.MONTH) == mes
                        && fechaReal.get(Calendar.YEAR) == anio
                        //Nota: Calendar.HOUR devuelve hora formato 12 H, Calendar.HOUR_OF_DAY es formato 24 h
                        && fechaReal.get(Calendar.HOUR_OF_DAY) == hora
                        && fechaReal.get(Calendar.MINUTE) == minutos
                ) {
                    fecha = new GregorianCalendar(anio, mes, dia, hora, minutos);
                }
            } while (fecha == null);


            return fecha;
        }

        //Validaciones datos fecha

        /*
         * Signatura: public int pedirValidarDia()
         * Comentario: pide y valida un día
         * Precondiciones:
         * Entradas:
         * Salidas: entero dia
         * Postcondiciones: Asociado al nombre devuelve un entero con el dia validado introducido por el usuario
         * */
        public int pedirValidarDia() {
            Scanner sc = new Scanner(System.in);
            int dia;
            try {
                do {
                    System.out.println("Introducir día");
                    dia = sc.nextInt();
                    if (dia < 1 || dia > 31) {
                        System.out.println("Error al introducir el día, vuelva a introducirlo");
                    }
                } while (dia < 1 || dia > 31);
            } catch (InputMismatchException e) {
                dia = pedirValidarDia();
            }
            return dia;
        }

        /*
         * Signatura: public  int pedirValidarMes()
         * Comentario: pide y valida un mes
         * Precondiciones:
         * Entradas:
         * Salidas: entero mes
         * Postcondiciones: Asociado al nombre devuelve un entero con el mes validado introducido por el usuario
         * */
        public int pedirValidarMes() {
            Scanner sc = new Scanner(System.in);
            int mes = 0;
            try {
                do {
                    System.out.println("Introducir mes");
                    mes = sc.nextInt();
                    if (mes < 1 || mes > 12) {
                        System.out.println("Error al introducir el mes, vuelva a introducirlo");
                    }
                } while (mes < 1 || mes > 12);
            } catch (InputMismatchException e) {
                mes = pedirValidarMes();
            }
            return mes;
        }

        /*
         * Signatura:  public int pedirValidarAnio()
         * Comentario: pide y valida un año
         * Precondiciones:
         * Entradas:
         * Salidas: entero anio
         * Postcondiciones: Asociado al nombre devuelve un entero con el año validado introducido por el usuario
         * */
        public int pedirValidarAnio() {
            Scanner sc = new Scanner(System.in);
            int anio = 0;
            try {
                do {
                    System.out.println("Introducir año");
                    anio = sc.nextInt();
                    if (anio < 1900) {
                        System.out.println("Error al introducir el año, vuelva a introducirlo");
                    }
                } while (anio < 1900);
            } catch (InputMismatchException e) {
                anio = pedirValidarAnio();
            }

            return anio;
        }

        /*
         * Signatura:  public int pedirValidarHora()
         * Comentario: pide y valida una hora
         * Precondiciones:
         * Entradas:
         * Salidas: entero hora
         * Postcondiciones: Asociado al nombre devuelve un entero con la hora validada introducida por el usuario
         * */
        public int pedirValidarHora() {
            Scanner sc = new Scanner(System.in);
            int hora = 0;
            try {
                do {
                    System.out.println("Introducir hora");
                    hora = sc.nextInt();
                    if (hora < 0 || hora > 23) {
                        System.out.println("Error al introducir la hora, vuelva a introducirla");
                    }
                } while (hora < 0 || hora > 23);
            } catch (InputMismatchException e) {
                hora = pedirValidarHora();
            }

            return hora;
        }

        /*
         * Signatura:  public int pedirValidarMinutos()
         * Comentario: pide y valida minutos
         * Precondiciones:
         * Entradas:
         * Salidas: entero minutos
         * Postcondiciones: Asociado al nombre devuelve un entero con los minutos validados introducidos por el usuario
         * */
        public int pedirValidarMinutos() {
            Scanner sc = new Scanner(System.in);
            int minutos = 0;
            try {
                do {
                    System.out.println("Introducir minutos");
                    minutos = sc.nextInt();
                    if (minutos < 0 || minutos > 59) {
                        System.out.println("Error al introducir la hora, vuelva a introducirla");
                    }
                } while (minutos < 0 || minutos > 59);
            } catch (InputMismatchException e) {
                minutos = pedirValidarMinutos();
            }

            return minutos;
        }

        //Nuevas validaciones 25/11/2019

        /*
         * Signatura:  public int pedirValidarNumeroGoles()
         * Comentario: pide y valida el número de goles
         * Precondiciones:
         * Entradas:
         * Salidas: entero numeroGoles
         * Postcondiciones: Asociado al nombre devuelve un entero con el número de goles validados introducidos por el usuario
         * */
        public int pedirValidarNumeroGoles() {
            Scanner sc = new Scanner(System.in);
            int numeroGoles = 0;
            try {
                do {
                    System.out.println("Introduce el numero de goles: ");
                    numeroGoles = sc.nextInt();
                    if (numeroGoles < 0) {
                        System.out.println("No puede introducir un número negativo, vuelva a introducir el número de goles");
                    }
                } while (numeroGoles < 0);
            } catch (InputMismatchException e) {
                numeroGoles = pedirValidarNumeroGoles();
            }

            return numeroGoles;
        }


        public GregorianCalendar introducirTiempoPartido(GregorianCalendar tiempoPartido) {
            tiempoPartido.set(Calendar.HOUR_OF_DAY, pedirValidarHora());
            tiempoPartido.set(Calendar.MINUTE, pedirValidarMinutos());
            return tiempoPartido;
        }


//        public boolean validarFechaFinPosteriorFechaInicio(GregorianCalendar fechaInicio, GregorianCalendar fechaFin){
//            boolean exito = false;
//            if (fechaFin.after(fechaInicio)) {
//                exito = true;
//            }
//            return exito;
//        }

        /*Pide y valida un equipo que sera
         '1' o '2'
        * */
        public char pedirValidarEquipoPuja() {
            Scanner sc = new Scanner(System.in);
            char puja = ' ';
            try {
                do {
                    System.out.println("Introduce el equipo: ");
                    System.out.println("1. Local");
                    System.out.println("2. Visitante");
                    puja = sc.nextLine().charAt(0);
                    if (puja != '1' && puja != '2') {
                        System.out.println("Introduce 1 o 2");
                    }
                } while (puja != '1' && puja != '2');
            } catch (InputMismatchException e) {
                puja = pedirValidarEquipoPuja();
            }

            return puja;
        }

        /*Pide y valida un resultado
         '1' o '2' o 'x'
        * */
        public char pedirValidarResultadoPuja() {
            Scanner sc = new Scanner(System.in);
            char puja = ' ';
            try {
                do {
                    System.out.println("Introduce el equipo por el que apuestas: ");
                    System.out.println("1. Local");
                    System.out.println("2. Visitante");
                    System.out.println("X. Empate");
                    puja = sc.nextLine().charAt(0);
                    puja = Character.toLowerCase(puja);
                    if (puja != '1' && puja != '2' && puja != 'x') {
                        System.out.println("Introduce 1, x ó 2");
                    }
                } while (puja != '1' && puja != '2' && puja != 'x');
            } catch (InputMismatchException e) {
                puja = pedirValidarResultadoPuja();
            }

            return puja;
        }


        /*
         * Signatura: public Apuesta pedirValidarApuesta()
         * Comentario: pide los datos y construye un objeto apuesta
         * Precondiciones:
         * Entradas: objeto usuario que realiza la apuesta
         * Salidas: objeto Apuesta
         * Postcondiciones: asociado al nombre se devolverá un objeto apuesta
         * */
        public Apuesta pedirValidarApuesta(UsuarioImpl usuario) {
            PartidoImpl partidoElegido;
            int tipoApuesta;
            GestionPartidos gestionPartidos = new GestionPartidos();
            GestionApuestas gestionApuestas = new GestionApuestas();
            double cantidadDineroAApostar = 0.0;
            Apuesta apuesta = null;
            double cuotaApuesta = 0.0;


            //PedirValidar el partido al que quiere apostar
            partidoElegido = pedirValidarPartidoDeUnaLista
                    (gestionPartidos.VerPartidosDisponibles());
            //PedirValidar tipo apuesta
            tipoApuesta = pedirValidarTipoApuesta();
            //PedirValidar la cantidad de dinero
            cantidadDineroAApostar = pedirValidarCantidadDinero();
            //Calcular la cuota (se calcula con una formula)


            //segun el tipo de apuesta
            //pedir los datos necesarios para cada tipo
            switch (tipoApuesta) {
                case 1:
                    apuesta = new ApuestaTipo1();
                    apuesta = (ApuestaTipo1) apuesta;

                    apuesta.setTipo('1');
                    //PedirValidar Goles Locales y goles Visitante
                    System.out.println("GOLES EQUIPO LOCAL: ");
                    ((ApuestaTipo1) apuesta).setGolesLocal(pedirValidarNumeroGoles());

                    System.out.println("GOLES EQUIPO VISITANTE: ");
                    ((ApuestaTipo1) apuesta).setGolesVisitante(pedirValidarNumeroGoles());
                    break;
                case 2:
                    apuesta = new ApuestaTipo2();
                    apuesta = (ApuestaTipo2) apuesta;
                    apuesta.setTipo('2');
                    //PedirValidar cantidad de goles y equipo
                    ((ApuestaTipo2) apuesta).setCantidadGoles(pedirValidarNumeroGoles());
                    ((ApuestaTipo2) apuesta).setEquipo(pedirValidarEquipoPuja());
                    break;
                case 3:
                    apuesta = new ApuestaTipo3();
                    apuesta = (ApuestaTipo3) apuesta;
                    apuesta.setTipo('3');
                    //Pedir validar equipo
                    ((ApuestaTipo3) apuesta).setEquipo(pedirValidarResultadoPuja());

                    break;
            }
            if (apuesta != null) {
                apuesta.setUsuario(usuario);
                apuesta.setPartido(partidoElegido);
                apuesta.setCantidad(cantidadDineroAApostar);
                cuotaApuesta = gestionApuestas.calcularCuotaApuesta(apuesta, tipoApuesta);
                apuesta.setCuota(cuotaApuesta);
            }
            return apuesta;
        }

        //29/11/2019

        /* Signatura:public String pedirValidarCorreo()
         * Comentario:
         *   Método que pide al usuario un correo y lo valida
         * Precondiciones:
         * Entradas:
         * Salidas: Cadena correo
         * Postcondiciones: Asociado al nombre devuelve un String correo validado introducido por el usuario.
         *                  El correo no superará los 30 caracteres ya que la BBDD sólo admite hasta 30.
         * */
        public String pedirValidarCorreo() {
            Scanner sc = new Scanner(System.in);
            String correo;
            do {
                System.out.println("Introduzca un correo");
                correo = sc.next();
                if (correo.length() >= 30 || correo == null) {
                    System.out.println("Error, debe introducir un correo no superior a 30 carateres");
                }
            } while (correo.length() >= 30 || correo == null);

            return correo;
        }

        /* Signatura:public String pedirValidarPassword()
         * Comentario:
         *   Método que pide al usuario una contraseña y la valida que ésta no supere los 25 caracteres
         *   ya que la BBDD sólo admite hasta 25.
         * Precondiciones:
         * Entradas:
         * Salidas: Cadena password
         * Postcondiciones: Asociado al nombre devuelve un String password validado introducido por el usuario.
         *                  La contraseña no superará los 25 caracteres ya que la BBDD sólo admite hasta 25.
         * */
        public String pedirValidarPassword() {
            Scanner sc = new Scanner(System.in);
            String password;
            do {
                System.out.println("Introduzca la contraseña");
                password = sc.next();
                if (password.length() >= 25 || password == null) {
                    System.out.println("Error, debe introducir una contraseña no superior a 25 caracteres");
                }
            } while (password.length() >= 25 || password == null);

            return password;
        }


        /* Signatura: public boolean pedirValidarIsAdministrador()
         * Comentario:
         *   Método que muestra un menú preguntado si se desea registrar como Administrador o usuario estándar, y valida
         *   que el dato introducido sea correcto.
         * Precondiciones:
         * Entradas:
         * Salidas: boolean idAdmin
         * Postcondiciones: Asociado al nombre se devuelve un boolean que indica si es Administrador (true) o usuario estándar (false).
         * */
        public boolean pedirValidarIsAdministrador() {
            Scanner sc = new Scanner(System.in);
            int opcionAdmin;
            boolean isAdmin = false;

            do {
                System.out.println("¿Cómo desea registrarse?" +
                        "\n1: Administrador" +
                        "\n2: Usuario Estándar");
                opcionAdmin = sc.nextInt();
                if (opcionAdmin < 1 || opcionAdmin > 2) {
                    System.out.println("Error, introduzca opción de menú");
                } else {
                    if (opcionAdmin == 1) {
                        isAdmin = true;
                    }
                }
            } while (opcionAdmin < 1 || opcionAdmin > 2);
            return isAdmin;
        }


        /* Signatura:  public String pedirValidarNombrePartido()
         * Comentario: se pide el nombre del partido y se valida
         * Precondiciones:
         * Entradas:
         * Salidas: cadena nombreEquipo
         * Postcondiciones: Asociado al nombre se devuelve un String con el nombre del equipo validado, introducido por el usuario.
         *                  No será mayor a 20 caracteres ya que es lo máximo que admite la BBDD.
         * */
        public String pedirValidarNombreEquipo() {
            Scanner sc = new Scanner(System.in);
            String nombreEquipo;
            do {
                System.out.println("Introduzca el nombre del Equipo");
                nombreEquipo = sc.nextLine();
                if (nombreEquipo.length() > 20) {
                    System.out.println("Error, el nombre del equipo no puede superar los 20 caracteres.");
                }
            } while (nombreEquipo.length() > 20);
            return nombreEquipo;
        }


        /* Signatura:  public PartidoImpl pedirValidarDatosPartido()
         * Comentario: se piden los datos del partido y se validan
         * Precondiciones:
         * Entradas:
         * Salidas: Objeto PartidoImpl
         * Postcondiciones: Asociado al nombre se devuelve un objeto partido con los datos validados.
         * */
        public PartidoImpl pedirValidarDatosPartido() {
            PartidoImpl partidoNuevo = new PartidoImpl();

            //Variables datos
            int golLocal, golVisitante;
            GregorianCalendar fechaComun, fechaInicio, fechaFin;
            String nombreLocal, nombreVisitante;
            boolean fechasCorrectas = false;
            boolean periodoApuestasIsAbierto = false;
            //TODO nuevo 12/12/2019
            Double apuestasMaximasTipo1;
            Double apuestasMaximasTipo2;
            Double apuestasMaximasTipo3;

            //Validación fechas Inicio y Fin y que el orden sea correcto
            System.out.println("Introduzca fecha del partido");
            //Fecha partido (dia, mes, año) que será común a tiempo inicio y fin
            fechaComun = pedirValidarFechaSinHora();
            //Instancio fechas inicio y fin con datos de fecha común, sin hora (para añadírsela más adelante)
            fechaInicio = new GregorianCalendar(fechaComun.get(Calendar.YEAR), fechaComun.get(Calendar.MONTH), fechaComun.get(Calendar.DAY_OF_MONTH), 0, 0);
            fechaFin = new GregorianCalendar(fechaComun.get(Calendar.YEAR), fechaComun.get(Calendar.MONTH), fechaComun.get(Calendar.DAY_OF_MONTH), 0, 0);

            //Añado el tiempo inicio y fin del partido
            do {
                System.out.println("Introduzca tiempo inicio Partido");
                introducirHoraYMinutos(fechaInicio);
                System.out.println("Introduzca tiempo final partido");
                introducirHoraYMinutos(fechaFin);
                fechasCorrectas = fechaInicio.before(fechaFin);

                if (!fechasCorrectas) {
                    System.out.println("Fechas incorrectas, vuelva a introducirlas");
                }
            } while (!fechasCorrectas);

            //Pedir datos equipo local
            System.out.println("Introduzca los datos del equipo Local");
            nombreLocal = pedirValidarNombreEquipo();
            golLocal = pedirValidarNumeroGoles();
            //Pedir datos equipo visitante
            System.out.println("Introduzca los datos del equipo Visitante");
            nombreVisitante = pedirValidarNombreEquipo();
            golVisitante = pedirValidarNumeroGoles();

            periodoApuestasIsAbierto = pedirValidarIsPeriodoApuestasAbierto();

            //TODO nuevo 12/12/2019
            System.out.println("Introduzca cantidad máxima de apuestas tipo1");
            apuestasMaximasTipo1 = pedirValidarMaximoApuestas();
            System.out.println("Introduzca cantidad máxima de apuestas tipo2");
            apuestasMaximasTipo2 = pedirValidarMaximoApuestas();
            System.out.println("Introduzca cantidad máxima de apuestas tipo3");
            apuestasMaximasTipo3 = pedirValidarMaximoApuestas();


            //periodoApuestasIsAbierto = true;

            //Paso los datos validados al objeto partido
            partidoNuevo.setPeriodoApuestasAbierto(periodoApuestasIsAbierto);
            partidoNuevo.setGolesLocal(golLocal);
            partidoNuevo.setGolesVisitante(golVisitante);
            partidoNuevo.setFechaInicio(fechaInicio);
            partidoNuevo.setFechaFin(fechaFin);
            partidoNuevo.setNombreLocal(nombreLocal);
            partidoNuevo.setNombreVisitante(nombreVisitante);
            //TODO nuevo 12/12/2019
            partidoNuevo.setMaximoApuestasTipo1(apuestasMaximasTipo1);
            partidoNuevo.setMaximoApuestasTipo2(apuestasMaximasTipo2);
            partidoNuevo.setMaximoApuestasTipo3(apuestasMaximasTipo3);

            return partidoNuevo;
        }

        /* Signatura:  public String pedirValidarFechaParaFormatoConversion(GregorianCalendar fechaApuesta)
         * Comentario: se convierte una fecha al formato: dia/mes/año
         * Precondiciones: los datos de la fechaApuesta deben estar validados
         * Entradas: Objeto GregorianCalendar fechaApuesta
         * Salidas: cadena fechaFormatoConversion
         * Postcondiciones: Asociado al nombre se devuelve un objeto fechaFormatoConversion en el formato dia/mes/año
         * */
        public String pedirValidarFechaParaFormatoConversion(GregorianCalendar fechaApuesta) { //Para consulta de apuestas según fecha
            String fechaFormatoConversion = null;

//            System.out.println("Introduzca fecha");
//            GregorianCalendar fechaNormalSinHora = pedirValidarFechaSinHora();
            //Datos fecha
            int dia = fechaApuesta.get(Calendar.DAY_OF_MONTH);
            int mes = fechaApuesta.get(Calendar.MONTH);
            int anio = fechaApuesta.get(Calendar.YEAR);
            if (dia < 10 && mes < 10) {
                fechaFormatoConversion = "0" + mes + "/0" + dia + "/" + anio;
            } else if (dia < 10) {
                fechaFormatoConversion = mes + "/0" + dia + "/" + anio;
            } else if (mes < 10) {
                fechaFormatoConversion = "0" + mes + "/" + dia + "/" + anio;   // mes/dia/año, así es como funciona en la consulta
            } else {
                fechaFormatoConversion = mes + "/" + dia + "/" + anio;
            }

//            fechaFormatoConversion = anio + "/" + mes + "/" + dia;   // mes/dia/año, así es como funciona en la consulta

            return fechaFormatoConversion;

        }

        /* Signatura:   public int pedirValidarIdApuesta()
         * Comentario: se pide y valida el id de la apuesta
         * Precondiciones:
         * Entradas:
         * Salidas: entero idApuesta
         * Postcondiciones: Asociado al nombre se devuelve un int con el id de la apuesta
         * */
        public int pedirValidarIdApuesta() {
            Scanner sc = new Scanner(System.in);
            int idApuesta;
            do {
                System.out.println("Introduzca el id de la apuesta");
                idApuesta = sc.nextInt();
                if (idApuesta < 0) {
                    System.out.println("Error al introducir el id de la apuesta");
                }
            } while (idApuesta < 0);
            return idApuesta;
        }

        /*
         * Signatura:  public void mostrarListaApuestasPorFecha(ArrayList<Apuesta> listaApuestas)
         * Comentario: muestra un listado de apuestas según la fecha
         * Precondiciones:
         * Entradas: ArrayList de Apuestas: listaApuestas
         * Salidas:
         * Postcondiciones:
         * */
        public void mostrarListaApuestasPorFecha(ArrayList<Apuesta> listaApuestas) { //Recibe usuario logeado
            //ArrayList<Apuesta> listaApuestas = obtenerListaApuestasPorFecha(usuarioApuesta);
            for (int i = 0; i < listaApuestas.size(); i++) {
                System.out.println("Id: " + listaApuestas.get(i).getId() + " Cuota: " + listaApuestas.get(i).getCuota() + " Cantidad: " + " Tipo: " + listaApuestas.get(i).getTipo() + " Fecha y Hora: " + listaApuestas.get(i).getFechaHora().getTime());
            }
        }

        /*
            prototipo: public int PedirValidarIdPartido(ArrayList<PartidoImpl> listadoPartidosAApostar)
            comentarios: se lee y se valida el id de los partidos que estan en uja lista,
                            o sea se ve si el partido existe por su id
            precondiciones: array lleno
            entradas: un array de partidos
            salidas: entero opcion que elige el usuario que coincide con el id del partido
            entradas/salidas: no hay
            postcondiciones: AN devuelve un numero entero positivo mayor que cero
            */
        public int PedirValidarIdPartido(ArrayList<PartidoImpl> listadoPartidosAApostar) {
            Scanner sc = new Scanner(System.in);
            GestionPartidos gestionPartidos = new GestionPartidos();
            PartidoImpl p = null;
            int opcionCaso10 = 0;
            boolean encontrado = false;

            do {
                System.out.println("Introduce el numero del partido ");
                mostrarPartidosComoUnMenu(listadoPartidosAApostar);
                opcionCaso10 = sc.nextInt();
                for (int i = 0; i < listadoPartidosAApostar.size() && encontrado == false; i++) {
                    if (listadoPartidosAApostar.get(i).getId() == opcionCaso10) {
                        opcionCaso10 = listadoPartidosAApostar.get(i).getId();
                        encontrado = true;
                    }
                }
            } while (!gestionPartidos.ComprobarExistenciaDelPartidoPorId(opcionCaso10, listadoPartidosAApostar));

            return opcionCaso10;
        }

        //TODO fecha Sin Hora para inserción nuevo partido 12/12/2019
        /*
         * Signatura: public GregorianCalendar pedirValidarFechaSinHora()
         * Comentario: pide y valida una fecha (día, mes y año)
         * Precondiciones:
         * Entradas:
         * Salidas: objeto GregorianCalendar
         * Postcondiciones: Asociado al nombre devuelve un objeto GregorianCalendar con la fecha validada que haya introducido el usuario.
         * */
        public GregorianCalendar pedirValidarFechaSinHora() {
            GregorianCalendar fecha = null;
            GregorianCalendar fechaReal;
            int dia, mes, anio, hora, minutos;
            do {
                dia = pedirValidarDia();
                mes = pedirValidarMes() - 1; //porque calendar va de 0 a 11
                anio = pedirValidarAnio();
//                hora = pedirValidarHora();
//                minutos = pedirValidarMinutos();
//                fechaReal = new GregorianCalendar(anio, mes, dia, hora, minutos);
                fechaReal = new GregorianCalendar(anio, mes, dia);

                if (fechaReal.get(Calendar.DAY_OF_MONTH) == dia
                        && fechaReal.get(Calendar.MONTH) == mes
                        && fechaReal.get(Calendar.YEAR) == anio
                    //Nota: Calendar.HOUR devuelve hora formato 12 H, Calendar.HOUR_OF_DAY es formato 24 h
//                        && fechaReal.get(Calendar.HOUR_OF_DAY) == hora
//                        && fechaReal.get(Calendar.MINUTE) == minutos
                ) {
//                    fecha = new GregorianCalendar(anio, mes, dia, hora, minutos);
                    fecha = new GregorianCalendar(anio, mes, dia);
                }
            } while (fecha == null);


            return fecha;
        }

        /* Signatura: public GregorianCalendar introducirHoraYMinutos(GregorianCalendar tiempoHoraYMinutos)
         * Comentario: se añade a una fecha hora y minutos
         * Precondiciones: la fecha de deberá estar correctamente validada
         * Entradas: Objeto GregorianCalendar fecha
         * Salidas: Objeto GregorianCalendar fecha
         * Entradas/Salida: el objeto contenía datos sólo de día, mes y año y se devolverá con hora y minutos
         * Postcondiciones: Asociado al nombre se devuelve un objeto GregorianCalendar fecha con los datos de fecha, ahora con hora y minutos añadidos
         * */
        public GregorianCalendar introducirHoraYMinutos(GregorianCalendar fecha) {       //Lo necesito para introducir solo hora y minutos en tiempo partido al crear objeto
            fecha.set(Calendar.HOUR_OF_DAY, pedirValidarHora());
            fecha.set(Calendar.MINUTE, pedirValidarMinutos());
            return fecha;
        }

        /*
         * Signatura:  public boolean pedirValidarIsPeriodoApuestasAbierto()
         * Comentario: valida si el periodo de apuestas está abierto
         * Precondiciones:
         * Entradas:
         * Salidas: boolean isAbierto
         * Postcondiciones: Asociado al nombre, devuelve un boolean que indica si el periodo de apuestas está abierto (true) o cerrado (false)
         * */
        public boolean pedirValidarIsPeriodoApuestasAbierto() {
            Scanner sc = new Scanner(System.in);
            int respueta;
            boolean isAbierto = false;
            System.out.println("Establecer si el período de apuestas está abierto: " +
                    "\n1: Si" +
                    "\n2: No");
            respueta = sc.nextInt();

            if (respueta == 1) {
                isAbierto = true;
            }
            return isAbierto;
        }

        /*
         * Signatura:  public Double pedirValidarMaximoApuestas()
         * Comentario: pide una cantidad para el máximo de apuestas y lo valida
         * Precondiciones:
         * Entradas:
         * Salidas: Double maximoApuesta
         * Postcondiciones: Asociado al nombre, devuelve un double que indica la cantidad máxima que se puede apostar
         * */
        public Double pedirValidarMaximoApuestas() {
            Scanner sc = new Scanner(System.in);
            Double maximoApuesta;

            do {
                maximoApuesta = sc.nextDouble();
                if (maximoApuesta < 0) {
                    System.out.println("Debe introducir una cantidad correcta");
                }
            } while (maximoApuesta < 0);
            return maximoApuesta;
        }

        /*
         * Signatura:  public int pedirValidarOpcionApuesta()
         * Comentario: pide y valida una opción a elegir de una lista de apuestas
         * Precondiciones:
         * Entradas:
         * Salidas:
         * Postcondiciones:
         * */
        public int pedirValidarOpcionApuesta(){
            Scanner sc = new Scanner(System.in);
            int opcion;
            do{
                System.out.println("Introduzca una opción de la lista de apuestas");
                opcion = sc.nextInt();
                if(opcion<0){
                    System.out.println("Error, no ha introducido una opción válida");
                }
            }while (opcion<0);
            return opcion;
        }

        /*
         * Signatura:   public void mostrarListaApuestas(ArrayList<Apuesta> listaApuestas)
         * Comentario: muestra un listado de apuestas
         * Precondiciones:
         * Entradas: ArrayList de Apuestas: listaApuestas
         * Salidas:
         * Postcondiciones:
         * */
        public void mostrarListaApuestas(ArrayList<Apuesta> listaApuestas){ //Funciona
//            ArrayList<Apuesta> listaApuestas = obtenerListaApuestasPorFecha(usuarioApuesta);
            for (int i = 0; i < listaApuestas.size(); i++){
//                System.out.println("Id: " + listaApuestas.get(i).getId() + " Cuota: " + listaApuestas.get(i).getCuota() + " Cantidad: " + " Tipo: " + listaApuestas.get(i).getTipo() + " Fecha y Hora: " + listaApuestas.get(i).getFechaHora().getTime());
                String fechaFormatoCorto = listaApuestas.get(i).getFechaHora().get(Calendar.DAY_OF_MONTH) + "/" + listaApuestas.get(i).getFechaHora().get(Calendar.MONTH) + "/" + listaApuestas.get(i).getFechaHora().get(Calendar.YEAR);
                System.out.println(i + ": Id: " + listaApuestas.get(i).getId() + ", Fecha: " + fechaFormatoCorto +  ", Cantidad: " + listaApuestas.get(i).getCantidad());
            }
        }

    }
