/*
Nombre del programa: ProgramaApuestasJDBC
Descripción: Programa para gestionar apuestas
Entrada del programa: opciones del usuario
Salida: eco de los datos, mensajes de ayuda al usuario

* PSEUDOCODIGO MAIN
* inicio
	repetir
        pedirValidarSiCrearCuentaOIniciarSesion
	si opcion no es salir
	    segun
        caso iniciar sesion
        //puede iniciar sesion como admin o como user estandar
            pedirValidarCredenciales
                si es usuario
                    pedirValidarOpcionMenuUsuarioEstandar
                sino (es admin)
                    pedirValidarOpcionMenuAdministrador
                finSi
                segun opcion elegida
                caso 1: realizar apuesta
                caso 2: ver los partidos disponibles para apostar
                caso 3: comprobar el resultado de una apuesta anterior
                caso 4: hacer un ingreso en cuenta
                caso 5: hacer una retirada de dinero
                caso 6: ver movimientos de la cuenta incluyendo apuestas realizadas y apuestas ganadas
                //aqui empiezan las opciones de admin
                caso 7: crear un nuevo partido
                caso 8: abrir un partido para que acepte apuestas
                caso 9: cerrar un partido para que no se pueda apostar
                caso 10: consultar las apuestas de un partido, indicando la cantidad de dinero apostado a cada posible resultado
                caso 11: pagar las apuestas ganadoras de un partido finalizado
                finSegun

        caso crear cuenta
        //solo puede crear cuenta como usuario estándar
            crearCuenta
	    finSegun
	mientras el usuario no desee salir
fin
* */
package main;

import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

import clases.*;
import gestion.GestionApuestas;
import gestion.GestionPartidos;
import gestion.GestionSaldo;
import gestion.GestionUsuarios;
import utilidad.Utilidad;
import validacion.Validar;

public class Main {
    public static void main(String[] args) {
        int opcionLoginOrCreateNewAccount ;
        Validar validar = new Validar();
        UsuarioImpl usuarioLogado;
        IngresoImpl movimientoSaldo;
        PartidoImpl partidoElegido = null;
        int opcionMenu, opcionCaso10;
        /*
        int tipoApuesta = 0;
        double cuotaApuesta = 0.0;
        double cantidadDineroAApostar = 0.0;
        ApuestaTipo1 apuestaTipo1 = null;
        ApuestaTipo2 apuestaTipo2 = null;
        ApuestaTipo3 apuestaTipo3 = null;
        */
        Apuesta apuesta = null;
        UsuarioImpl nuevoUsuario = null;
        PartidoImpl partidoNuevo = null;


        GestionUsuarios gestionUsuarios = new GestionUsuarios();
        GestionSaldo gestionSaldo = new GestionSaldo();
        GestionPartidos gestionPartidos = new GestionPartidos();
        GestionApuestas gestionApuestas = new GestionApuestas();
	    ArrayList<PartidoImpl> listadoPartidosAApostar = new ArrayList<PartidoImpl>();


        do{
            //pedirValidarSiCrearCuentaOIniciarSesion
            opcionLoginOrCreateNewAccount = validar.pedirValidarCrearCuentaOLogin();

            if(opcionLoginOrCreateNewAccount != 0)  //si opcion no es salir
            {
                switch (opcionLoginOrCreateNewAccount){
                    case 1:
                        //iniciar sesion
                        //puede iniciar sesion como admin o como user estandar
                        usuarioLogado = validar.pedirValidarCredenciales();
                        gestionUsuarios.obtenerObjetoUsuarioCompleto(usuarioLogado);
                        do {
                            if(usuarioLogado.isAdmin()){
                                //si es admin
                                //System.out.println("Eres admin!");
                                opcionMenu = validar.pedirValidarMenuAdministrador();
                            }else{
                                //no es admin
                                //System.out.println("No eres admin!");
                                opcionMenu = validar.pedirValidarMenuUsuarioEstandar();
                            }


                            switch (opcionMenu) {
                                case 1:
                                    //1: realizar apuesta
                                    //System.out.println("Opcion 1. En construcción.");
                                    String mensaje = "";
                                    apuesta = validar.pedirValidarApuesta(usuarioLogado);

                                    if(apuesta.getCuota() >= 1.5){
                                        mensaje = (gestionApuestas.realizarApuesta(apuesta)) ? "La apuesta se realizó correctamente" :
                                                "Hubo un problema, inténtelo de nuevo más tarde";
                                    }else{
                                        mensaje = "La cuota sale inferior a 1.5€, por lo que no se puede realizar esta apuesta.";
                                    }
                                    System.out.println(mensaje);
                                    break;
                                case 2:
                                    //2: ver los partidos disponibles para apostar
                                    listadoPartidosAApostar=gestionPartidos.VerPartidosDisponibles();
					                //MostrarListadoPartidosAApostar
                                	validar.MostrarListadoPartidosAApostar(listadoPartidosAApostar);
                                    break;
                                case 3:
                                    //3: comprobar el resultado de una apuesta anterior
                                    //System.out.println("Opcion 3. En construcción.");
                                    System.out.println("Comprobar resultado de una apuesta");
//                                    gestionApuestas.verResultadosApuesta(usuarioLogado);
                                    System.out.println("Introduzca los datos de la fecha de la apuesta");
                                    GregorianCalendar fechaApuesta = validar.pedirValidarFechaSinHora();
                                    //System.out.println(new Utilidad().formatearFecha(fechaApuesta));
                                    ArrayList<Apuesta> listadoApuestas = validar.validarListaApuestasPorFecha(usuarioLogado, fechaApuesta);
                                    if(listadoApuestas.size() > 0){
                                        gestionApuestas.verResultadoApuesta(listadoApuestas); //Funciona
                                    }else{
                                        System.out.println("No hay apuestas");
                                    }


                                    break;
                                case 4:
                                    //4: hacer un ingreso en cuenta
                                    //System.out.println("Opcion 4. En construcción.");
                                    System.out.println("Hacer un ingreso en cuenta");
                                    movimientoSaldo = new IngresoImpl();
                                    movimientoSaldo.setCantidad(validar.pedirValidarCantidadDinero());
                                    movimientoSaldo.setDescripcion(validar.pedirValidarDescripcionMovimientoDinero());
                                    if (validar.pedirValidarEstaSeguroDeseaRealizarMovimiento(movimientoSaldo)) {
                                        gestionSaldo.realizarIngresoEnCuentaUsuario(usuarioLogado, movimientoSaldo);
                                        //esto es para actualizar el saldo actual que tiene el usuario logado en memoria
                                        gestionUsuarios.obtenerObjetoUsuarioCompleto(usuarioLogado);
                                        System.out.println("Su saldo ha sido modificado, ahora es de " + usuarioLogado.getCantidadActualDinero());
                                    } else {
                                        System.out.println("No se ha realizado el ingreso.");
                                    }

                                    break;
                                case 5:
                                    //5: hacer una retirada de dinero
                                	 System.out.println("Hacer una retirada de la cuenta");
                                     movimientoSaldo = new IngresoImpl();
                                     movimientoSaldo.setCantidad(validar.pedirValidarCantidadDinero());
                                     movimientoSaldo.setDescripcion(validar.pedirValidarDescripcionMovimientoDinero());
                                     if (validar.pedirValidarEstaSeguroDeseaRealizarMovimiento(movimientoSaldo)) {
                                         gestionSaldo.RetirarDineroDeLaCuentaUsuario(usuarioLogado, movimientoSaldo);
                                         //esto es para actualizar el saldo actual que tiene el usuario logado en memoria
                                         gestionUsuarios.obtenerObjetoUsuarioCompleto(usuarioLogado);
                                         System.out.println("Su saldo ha sido modificado, ahora es de " + usuarioLogado.getCantidadActualDinero());
                                     } else {
                                         System.out.println("No se ha realizado la retiraada.");
                                     }
                                    //System.out.println("Opcion 5. En construccion.");
                                    break;
                                case 6:
                                    //6: ver movimientos de la cuenta incluyendo apuestas realizadas y apuestas ganadas
                                    gestionUsuarios.VerMovimientosCuentaUsuario(usuarioLogado.getId());
                                    break;
                                case 7:
                                    //aqui empiezan las opciones de admin
                                    //7: crear un nuevo partido
                                    //System.out.println("Opcion 7. En construcción.");

                                    System.out.println("Registrar nuevo partido");
                                    partidoNuevo = validar.pedirValidarDatosPartido();
                                    boolean exito = gestionPartidos.insertarPartido(partidoNuevo);
                                    if (exito){
                                        System.out.println("Nuevo partido registrado correctamente");
                                    }else {
                                        System.out.println("Error al registrar nuevo partido");
                                    }


                                    break;
                                case 8:
                                    //8: abrir un partido para que acepte apuestas
                                    System.out.println("Abrir un partido para que acepte apuestas.");
                                    partidoElegido = validar.pedirValidarPartidoDeUnaLista(gestionPartidos.obtenerListadoPartidos());
                                    if(partidoElegido != null){
                                        gestionPartidos.modificarAperturaPeriodoApuestasDePartido(partidoElegido,true);
                                    }
                                    break;
                                case 9:
                                    //9: cerrar un partido para que no se pueda apostar
                                    System.out.println("Cerrar un partido para que no se pueda apostar.");
                                    partidoElegido = validar.pedirValidarPartidoDeUnaLista(gestionPartidos.obtenerListadoPartidos());
                                    if(partidoElegido != null){
                                        gestionPartidos.modificarAperturaPeriodoApuestasDePartido(partidoElegido,false);
                                    }
                                    break;
                                case 10:
                                     //10: consultar las apuestas de un partido, indicando la cantidad de dinero apostado a cada posible resultado
                                	listadoPartidosAApostar=gestionPartidos.obtenerListadoPartidosAnteriorAHoy();
                                	
                                	//MostrarListadoPartidosAApostar
                                	opcionCaso10=validar.PedirValidarIdPartido(listadoPartidosAApostar);
                                	
                                	gestionPartidos.DineroApostadoPorUnPosibleResultadoDeUnPartido(opcionCaso10);
                                    break;

                                    case 11:
                                    //11: pagar las apuestas ganadoras de un partido finalizado
                                    //System.out.println("Opcion 11. En construcción.");
                                    System.out.println("Los pagos se realizan automaticamente");

                                    break;


                            }
                        }while (opcionMenu != 0);
                        break;

                    case 2:
                        //crear cuenta (usuario standard)
                        System.out.println("Vas a crear una cuenta nueva");
                        nuevoUsuario = validar.pedirValidarDatosUsuario();
                        boolean exito = gestionUsuarios.insertarUsuario(nuevoUsuario);
                        if(exito){
                            System.out.println("Nuevo usuario registrado correctamente");
                        }else {
                            System.out.println("Error al registrar nuevo usuario");
                        }
                        break;
                }

            }
        }while(opcionLoginOrCreateNewAccount != 0);
    }
}
