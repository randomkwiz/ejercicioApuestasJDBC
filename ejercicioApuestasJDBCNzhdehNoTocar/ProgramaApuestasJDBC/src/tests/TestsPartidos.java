package tests;

import java.util.ArrayList;

import clases.PartidoImpl;
import gestion.GestionPartidos;

public class TestsPartidos {
    public static void main(String[] args) {
    	
    	GestionPartidos gp=new GestionPartidos();
    	ArrayList<PartidoImpl> partido=new ArrayList<PartidoImpl>();
    	partido=gp.VerPartidosDisponibles();
    	
    	for(int i=0;i<partido.size();i++) {
    		System.out.println(partido.get(i).toString());
    	}

    }
}
