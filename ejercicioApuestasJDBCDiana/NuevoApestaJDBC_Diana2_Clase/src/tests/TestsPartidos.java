package tests;

import clases.PartidoImpl;
import gestion.GestionPartidos;
import validacion.Validar;

import java.util.GregorianCalendar;

public class TestsPartidos {
    public static void main(String[] args) {
    	
//    	GestionPartidos gp=new GestionPartidos();
////    	gp.VerPartidosDisponibles();
        Validar validar = new Validar();
//        PartidoImpl partidoNuevo = validar.pedirValidarDatosPartido();
//        System.out.println(partidoNuevo.getFechaInicio().getTime());
//        System.out.println(partidoNuevo.getFechaFin().getTime());
//        System.out.println(partidoNuevo.getGolesLocal());
//        System.out.println(partidoNuevo.getGolesVisitante());
//        System.out.println(partidoNuevo.getNombreLocal());
//        System.out.println(partidoNuevo.getNombreVisitante());
//        System.out.println(partidoNuevo.isPeriodoApuestasAbierto());

//        System.out.println(validar.pedirValidarIsPeriodoApuestasAbierto());
//        System.out.println(validar.pedirValidarMaximoApuestas());

        GestionPartidos gp = new GestionPartidos();
        PartidoImpl nuevoPartido = new PartidoImpl(8, 1, 2, new GregorianCalendar(2019,2,15, 15,10), new GregorianCalendar(2019,2,15,15,45), "sevilla", "betis",false, 2000.0, 3000.0, 4000.0);
        gp.insertarPartido(nuevoPartido);

    }
}
