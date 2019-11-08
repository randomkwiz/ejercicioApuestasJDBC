package gestion;

import java.util.ArrayList;

public class GestionApuestas {

    /*EJEMPLO
    * Signatura: public boolean pintarCadenaEnPantalla(String mensaje)
    * Comentario: Este método pinta un mensaje pasado como parámetro en pantalla
    * Precondiciones: No hay
    * Entradas: Por valor se pasa una cadena como parámetro de entrada
    * Salidas: boolean que indica si la operacion se ha realizado con exito
    * Entrada/Salida: Nada
    * Postcondiciones: Asociado al nombre se devuelve un boolean que será true si el mensaje se ha pintado
    *                   en pantalla correctamente y sera false si hubo algun problema.
    * */
    public boolean pintarCadenaEnPantalla(String mensaje){
        boolean exito = false;

        System.out.println(mensaje);
        exito = true;

        return exito;
    }
}
