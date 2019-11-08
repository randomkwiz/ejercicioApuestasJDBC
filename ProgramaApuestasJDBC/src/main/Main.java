/*
Nombre del programa: ProgramaApuestasJDBC
Descripción: Programa para gestionar apuestas
Entrada del programa: opciones del usuario
Salida: eco de los datos, mensajes de ayuda al usuario

* PSEUDOCODIGO MAIN
* inicio
	repetir
	pedirValidarSiCrearCuentaOIniciarSesion
	si iniciar sesion
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

	sino (crear cuenta)
	//solo puede crear cuenta como usuario estándar
		crearCuenta
	finSi
	mientras el usuario desee ejecutar el programa
fin
* */
package main;
public class Main {
    public static void main(String[] args) {

    }
}
