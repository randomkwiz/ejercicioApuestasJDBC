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

import clases.UsuarioImpl;
import gestion.GestionUsuarios;
import validacion.Validar;

public class Main {
    public static void main(String[] args) {
        int opcionLoginOrCreateNewAccount ;
        Validar validar = new Validar();
        UsuarioImpl usuarioLogado;
        int opcionMenu ;
        GestionUsuarios gestionUsuarios = new GestionUsuarios();


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
                        if(usuarioLogado.isAdmin()){
                            //si es admin
                            //System.out.println("Eres admin!");
                            opcionMenu = validar.pedirValidarMenuAdministrador();
                        }else{
                            //no es admin
                            //System.out.println("No eres admin!");
                            opcionMenu = validar.pedirValidarMenuUsuarioEstandar();
                        }

                        switch (opcionMenu){
                            case 1:
                                //1: realizar apuesta
                                System.out.println("Opcion 1. En construcción.");
                                break;
                            case 2:
                                //2: ver los partidos disponibles para apostar
                                System.out.println("Opcion 2. En construcción.");
                                break;
                            case 3:
                                //3: comprobar el resultado de una apuesta anterior
                                System.out.println("Opcion 3. En construcción.");
                                break;
                            case 4:
                                //4: hacer un ingreso en cuenta
                                System.out.println("Opcion 4. En construcción.");
                                break;
                            case 5:
                                //5: hacer una retirada de dinero
                                System.out.println("Opcion 5. En construcción.");
                                break;
                            case 6:
                                //6: ver movimientos de la cuenta incluyendo apuestas realizadas y apuestas ganadas
                                System.out.println("Opcion 6. En construcción.");
                                break;
                            case 7:
                                //aqui empiezan las opciones de admin
                                //7: crear un nuevo partido
                                System.out.println("Opcion 7. En construcción.");
                                break;
                            case 8:
                                //8: abrir un partido para que acepte apuestas
                                System.out.println("Opcion 8. En construcción.");
                                break;
                            case 9:
                                //9: cerrar un partido para que no se pueda apostar
                                System.out.println("Opcion 9. En construcción.");
                                break;
                            case 10:
                                //10: consultar las apuestas de un partido, indicando la cantidad de dinero apostado a cada posible resultado
                                System.out.println("Opcion 10. En construcción.");
                                break;
                            case 11:
                                //11: pagar las apuestas ganadoras de un partido finalizado
                                System.out.println("Opcion 11. En construcción.");
                                break;
                        }
                        break;
                    case 2:
                        //crear cuenta (usuario standard)
                        System.out.println("Vas a crear una cuenta nueva");
                        break;
                }

            }
        }while(opcionLoginOrCreateNewAccount != 0);
    }
}
