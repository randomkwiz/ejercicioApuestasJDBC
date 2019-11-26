package tests;

import utilidad.Utilidad;
import validacion.Validar;

import java.util.GregorianCalendar;

public class TestsApuestas {
    public static void main(String[] args) {


        Validar validar = new Validar();
        Utilidad utilidad = new Utilidad();
        GregorianCalendar fecha;
        fecha = validar.pedirValidarFechaHora();
        System.out.println("La fecha elegida es: ");
        System.out.println(utilidad.formatearFecha(fecha));

    }
}
