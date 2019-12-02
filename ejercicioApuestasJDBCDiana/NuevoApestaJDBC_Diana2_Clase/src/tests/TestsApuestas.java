package tests;

import gestion.GestionApuestas;
import utilidad.Utilidad;
import validacion.Validar;

import java.util.GregorianCalendar;

public class TestsApuestas {
    public static void main(String[] args) {


//        Validar validar = new Validar();
//        Utilidad utilidad = new Utilidad();
//        GregorianCalendar fecha;
//        fecha = validar.pedirValidarFechaHora();
//        System.out.println("La fecha elegida es: ");
//        System.out.println(utilidad.formatearFecha(fecha));
        GestionApuestas gapuesta = new GestionApuestas();
        GregorianCalendar fecha = new GregorianCalendar(2019, 11,1, 12,0);
        gapuesta.mostrarListadoApuestasPorFecha(fecha);

    }
}
