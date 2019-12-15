/*
 * Estudio de clase
 * Nombre: PartidoImpl
 * Propiedades
 * 	-Basicas:
 * 		-entero id
 * 		-entero golesLocales
 * 		-entero golesVisitante
 *      -fecha fechaInicio
 *      -fecha fechaFin
 *      -cadena nombreLocal
 *      -cadena nombreVisitante
 * 	-Derivadas
 * 	-Compartidas
 *
 * Metodos de la interfaz
 * getters y setters
 * */
package clases;
import interfaces.Partido;

import java.util.GregorianCalendar;

public class PartidoImpl implements Partido {
    private int id;
    private int golesLocal;
    private int golesVisitante;
    private GregorianCalendar fechaInicio;
    private GregorianCalendar fechaFin;
    private String nombreLocal;
    private String nombreVisitante;
    private boolean isPeriodoApuestasAbierto;
    private double maximoApuestasTipo1;
    private double maximoApuestasTipo2;
    private double maximoApuestasTipo3;


    public PartidoImpl(int id, int golesLocal, int golesVisitante, GregorianCalendar fechaInicio, GregorianCalendar fechaFin, String nombreLocal, String nombreVisitante, boolean isPeriodoApuestasAbierto) {
        this.id = id;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nombreLocal = nombreLocal;
        this.nombreVisitante = nombreVisitante;
        this.isPeriodoApuestasAbierto = isPeriodoApuestasAbierto;
        this.maximoApuestasTipo1 = 0;
        this.maximoApuestasTipo2 = 0;
        this.maximoApuestasTipo3 = 0;
    }

    public PartidoImpl(int id, int golesLocal, int golesVisitante, GregorianCalendar fechaInicio, GregorianCalendar fechaFin, String nombreLocal, String nombreVisitante, boolean isPeriodoApuestasAbierto, double maximoApuestasTipo1, double maximoApuestasTipo2, double maximoApuestasTipo3) {
        this.id = id;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nombreLocal = nombreLocal;
        this.nombreVisitante = nombreVisitante;
        this.isPeriodoApuestasAbierto = isPeriodoApuestasAbierto;
        this.maximoApuestasTipo1 = maximoApuestasTipo1;
        this.maximoApuestasTipo2 = maximoApuestasTipo2;
        this.maximoApuestasTipo3 = maximoApuestasTipo3;
    }

    public PartidoImpl(int id) {
        this.id = id;
    }

    public PartidoImpl(){
        this.id = 0;
        this.golesLocal = 0;
        this.golesVisitante = 0;
        this.fechaInicio = new GregorianCalendar(1900,1,1);
        this.fechaFin = new GregorianCalendar(1900,1,1);
        this.nombreLocal = " ";
        this.nombreVisitante = " ";
        this.isPeriodoApuestasAbierto = false;
        this.maximoApuestasTipo1 = 0;
        this.maximoApuestasTipo2 = 0;
        this.maximoApuestasTipo3 = 0;
    }
    //Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public GregorianCalendar getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(GregorianCalendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public GregorianCalendar getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(GregorianCalendar fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getNombreVisitante() {
        return nombreVisitante;
    }

    public void setNombreVisitante(String nombreVisitante) {
        this.nombreVisitante = nombreVisitante;
    }

    public boolean isPeriodoApuestasAbierto() {
        return isPeriodoApuestasAbierto;
    }

    public void setPeriodoApuestasAbierto(boolean periodoApuestasAbierto) {
        isPeriodoApuestasAbierto = periodoApuestasAbierto;
    }

    public double getMaximoApuestasTipo1() {
        return maximoApuestasTipo1;
    }

    public void setMaximoApuestasTipo1(double maximoApuestasTipo1) {
        this.maximoApuestasTipo1 = maximoApuestasTipo1;
    }

    public double getMaximoApuestasTipo2() {
        return maximoApuestasTipo2;
    }

    public void setMaximoApuestasTipo2(double maximoApuestasTipo2) {
        this.maximoApuestasTipo2 = maximoApuestasTipo2;
    }

    public double getMaximoApuestasTipo3() {
        return maximoApuestasTipo3;
    }

    public void setMaximoApuestasTipo3(double maximoApuestasTipo3) {
        this.maximoApuestasTipo3 = maximoApuestasTipo3;
    }
}
