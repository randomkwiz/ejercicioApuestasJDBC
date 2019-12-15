/*
 * Estudio de clase
 * Nombre: ApuestaTipo1
 * Propiedades
 * 	-Basicas:
 * 		-real apuestasMaximas
 * 		-entero golesLocal
 * 		-entero golesVisitante
 * 	-Derivadas
 * 	-Compartidas
 *
 * Metodos de la interfaz
 * getters y setters
 * */
package clases;
import java.util.GregorianCalendar;

public class ApuestaTipo1 extends Apuesta{
    //private double apuestasMaximas;
    private int golesLocal;
    private int golesVisitante;

    public ApuestaTipo1(int id, double cuota, int cantidad, char tipo, GregorianCalendar fechaHora, UsuarioImpl usuario, PartidoImpl partido, double apuestasMaximas, int golesLocal, int golesVisitante) {
        super(id, cuota, cantidad, tipo, fechaHora, usuario, partido);
        //this.apuestasMaximas = apuestasMaximas;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
    }

    public ApuestaTipo1() {
        super();
        //this.apuestasMaximas = 0.0;
        this.golesLocal = 0;
        this.golesVisitante = 0;
    }

    public ApuestaTipo1(double apuestasMaximas, int golesLocal, int golesVisitante) {
        //this.apuestasMaximas = apuestasMaximas;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
    }


    //Getters y setters
//    public double getApuestasMaximas() {
//        return apuestasMaximas;
//    }
//
//    public void setApuestasMaximas(double apuestasMaximas) {
//        this.apuestasMaximas = apuestasMaximas;
//    }

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





}
