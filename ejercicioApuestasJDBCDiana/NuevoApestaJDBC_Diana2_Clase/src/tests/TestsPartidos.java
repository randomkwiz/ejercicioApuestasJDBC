package tests;

import clases.PartidoImpl;
import gestion.GestionPartidos;
import utilidad.Utilidad;
import validacion.Validar;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TestsPartidos {
    public static void main(String[] args) {
    	
//    	GestionPartidos gp=new GestionPartidos();
//////    	gp.VerPartidosDisponibles();
////
////        PartidoImpl nuevoPartido = gp.crearObjetoPartido();
////
////        System.out.println(nuevoPartido.getGolesLocal());
////        System.out.println(nuevoPartido.getGolesVisitante());
////        System.out.println(nuevoPartido.getFechaInicio());
////        System.out.println(nuevoPartido.getFechaFin());
////        System.out.println(nuevoPartido.getNombreLocal());
////        System.out.println(nuevoPartido.getNombreVisitante());
////        System.out.println(nuevoPartido.isPeriodoApuestasAbierto());
////        System.out.println(nuevoPartido.getId());
//
//        GregorianCalendar fechaInicio = new GregorianCalendar();
//        fechaInicio.set(Calendar.DAY_OF_MONTH, 15);
//        fechaInicio.set(Calendar.MONTH, 2);
//        fechaInicio.set(Calendar.YEAR, 2019);
//        fechaInicio.set(Calendar.HOUR_OF_DAY, 15);
//        fechaInicio.set(Calendar.MINUTE, 15);
//
//        GregorianCalendar fechaFin = new GregorianCalendar();
//        fechaFin.set(Calendar.DAY_OF_MONTH, 15);
//        fechaFin.set(Calendar.MONTH, 2);
//        fechaFin.set(Calendar.YEAR, 2019);
//        fechaFin.set(Calendar.HOUR_OF_DAY, 15);
//        fechaFin.set(Calendar.MINUTE, 30);
//
//        PartidoImpl nuevoPartidoPrefabricado = new PartidoImpl(1, 2, 3, fechaInicio, fechaFin, "Sevilla", "Barcelona", true);
//
//        boolean exito = gp.insertarPartido(nuevoPartidoPrefabricado); //TODO: da error
//        System.out.println(exito);

//        Validar validar = new Validar();
//        PartidoImpl partidoNuevo = validar.pedirValidarDatosPartido();
//
//        System.out.println("Partido " + partidoNuevo);
//
//        System.out.println(partidoNuevo.getNombreVisitante());
//        System.out.println(partidoNuevo.getNombreLocal());
//        System.out.println(partidoNuevo.getFechaFin().getTime());
//        System.out.println(partidoNuevo.getFechaInicio().getTime());
//        System.out.println(partidoNuevo.getGolesVisitante());
//        System.out.println(partidoNuevo.getGolesLocal());

        GregorianCalendar fechaInicio = new GregorianCalendar();
        fechaInicio.set(Calendar.DAY_OF_MONTH, 15);
        fechaInicio.set(Calendar.MONTH, 2);
        fechaInicio.set(Calendar.YEAR, 2019);
        fechaInicio.set(Calendar.HOUR_OF_DAY, 15);
        fechaInicio.set(Calendar.MINUTE, 15);

        System.out.println(fechaInicio);




    }
}
