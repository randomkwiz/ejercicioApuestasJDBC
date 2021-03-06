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
    //Añadido por Diana
    private Double apuestasMaximasTipo1;
    private Double apuestasMaximasTipo2;
    private Double apuestasMaximasTipo3;

    public PartidoImpl(int id, int golesLocal, int golesVisitante, GregorianCalendar fechaInicio, GregorianCalendar fechaFin, String nombreLocal, String nombreVisitante, boolean isPeriodoApuestasAbierto) {
        this.id = id;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nombreLocal = nombreLocal;
        this.nombreVisitante = nombreVisitante;
        this.isPeriodoApuestasAbierto = isPeriodoApuestasAbierto;
    }

    //TODO Constructor añadido por Diana para inserción de partidos
    public PartidoImpl(int id, int golesLocal, int golesVisitante, GregorianCalendar fechaInicio, GregorianCalendar fechaFin, String nombreLocal, String nombreVisitante, boolean isPeriodoApuestasAbierto, Double apuestasMaximasTipo1, Double apuestasMaximasTipo2, Double apuestasMaximasTipo3){
        this.id = id;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nombreLocal = nombreLocal;
        this.nombreVisitante = nombreVisitante;
        this.isPeriodoApuestasAbierto = isPeriodoApuestasAbierto;
        this.apuestasMaximasTipo1 = apuestasMaximasTipo1;
        this.apuestasMaximasTipo2 = apuestasMaximasTipo2;
        this.apuestasMaximasTipo3 = apuestasMaximasTipo3;
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

    //TODO Getters y Setters añadidos por Diana
    public Double getApuestasMaximasTipo1() {
        return apuestasMaximasTipo1;
    }

    public void setApuestasMaximasTipo1(Double apuestasMaximasTipo1) {
        this.apuestasMaximasTipo1 = apuestasMaximasTipo1;
    }

    public Double getApuestasMaximasTipo2() {
        return apuestasMaximasTipo2;
    }

    public void setApuestasMaximasTipo2(Double apuestasMaximasTipo2) {
        this.apuestasMaximasTipo2 = apuestasMaximasTipo2;
    }

    public Double getApuestasMaximasTipo3() {
        return apuestasMaximasTipo3;
    }

    public void setApuestasMaximasTipo3(Double apuestasMaximasTipo3) {
        this.apuestasMaximasTipo3 = apuestasMaximasTipo3;
    }
}
