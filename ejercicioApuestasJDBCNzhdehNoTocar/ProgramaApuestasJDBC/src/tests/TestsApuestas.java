package tests;

import clases.Apuesta;
import clases.ApuestaTipo1;
import clases.PartidoImpl;
import clases.UsuarioImpl;
import gestion.GestionApuestas;
import utilidad.Utilidad;
import validacion.Validar;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class TestsApuestas {
    public static void main(String[] args) {
        Validar validar = new Validar();
        Utilidad utilidad = new Utilidad();
        GestionApuestas gestionApuestas = new GestionApuestas();
        UsuarioImpl usuario = new UsuarioImpl(1,50,"aabb@gmail.com","1234",false );
        Apuesta apuesta ;
        GregorianCalendar fecha = new GregorianCalendar();
        fecha.set(2019,0,11);
/*
        fecha = validar.pedirValidarFechaHora();
        System.out.println("La fecha elegida es: ");
        System.out.println(utilidad.formatearFecha(fecha));
*/

        System.out.println(utilidad.formatearFecha(fecha));
        ArrayList<Apuesta> listadoApuestas = validar.validarListaApuestasPorFecha(usuario,fecha);

        System.out.println(listadoApuestas.size());



    }
}
