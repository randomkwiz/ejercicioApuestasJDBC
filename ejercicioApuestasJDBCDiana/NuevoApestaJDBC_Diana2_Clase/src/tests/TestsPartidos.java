package tests;

import clases.PartidoImpl;
import gestion.GestionPartidos;
import validacion.Validar;

public class TestsPartidos {
    public static void main(String[] args) {
    	
    	GestionPartidos gp=new GestionPartidos();
//    	gp.VerPartidosDisponibles();
        Validar validar = new Validar();
        PartidoImpl partidoNuevo = validar.pedirValidarDatosPartido();
        System.out.println(partidoNuevo.getFechaInicio().getTime());
        System.out.println(partidoNuevo.getFechaFin().getTime());
        System.out.println(partidoNuevo.getGolesLocal());
        System.out.println(partidoNuevo.getGolesVisitante());
        System.out.println(partidoNuevo.getNombreLocal());
        System.out.println(partidoNuevo.getNombreVisitante());
        System.out.println(partidoNuevo.isPeriodoApuestasAbierto());

    }
}
