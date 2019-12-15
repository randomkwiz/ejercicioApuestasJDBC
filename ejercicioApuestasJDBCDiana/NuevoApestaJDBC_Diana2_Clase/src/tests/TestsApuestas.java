package tests;

import clases.*;
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
        /*

        GregorianCalendar fecha;
        fecha = validar.pedirValidarFechaHora();
        System.out.println("La fecha elegida es: ");
        System.out.println(utilidad.formatearFecha(fecha));
*/

        GregorianCalendar fechaApuesta = new GregorianCalendar(2019, 1, 11);

//        ArrayList<Apuesta> listaApuestas =  validar.validarListaApuestasPorFecha(usuario, fechaApuesta);
//        validar.mostrarListaApuestas(listaApuestas);


        //Probar funcionamiento mostrarListasApuestas:
        //Partido apuestaReal
        PartidoImpl partido = new PartidoImpl(1, 1, 2, new GregorianCalendar(2019,1,12,12,0),new GregorianCalendar(2019,1,12,13,45), "Sevilla", "Betis", true, 5000.00, 1000.0,6000.00);

        ArrayList<Apuesta> listadoApuestas = new ArrayList<>();
        Apuesta apuesta1 = new ApuestaTipo1();
        apuesta1.setFechaHora(fechaApuesta);
        Apuesta apuesta2 = new ApuestaTipo2();
        apuesta2.setId(2);
        Apuesta apuesta3 = new ApuestaTipo3();
        apuesta3.setId(3);
        apuesta3.setCantidad(1.2);

        //int id, double cuota, int cantidad, char tipo, GregorianCalendar fechaHora, UsuarioImpl usuario, PartidoImpl partido, int golesLocal, int golesVisitante
        ApuestaTipo1 apuestaReal = new ApuestaTipo1(7, 1.20,50, '1', new GregorianCalendar(2019, 1, 11, 12, 0), usuario, partido, 3, 2);


        listadoApuestas.add(apuesta1);
        listadoApuestas.add(apuesta2);
        listadoApuestas.add(apuesta3);
        listadoApuestas.add(apuestaReal);

        gestionApuestas.verResultadoApuesta(listadoApuestas); //Funciona




    }
}
