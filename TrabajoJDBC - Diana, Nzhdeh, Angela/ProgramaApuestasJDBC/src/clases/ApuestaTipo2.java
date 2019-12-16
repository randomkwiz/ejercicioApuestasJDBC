/*
 * Estudio de clase
 * Nombre: ApuestaTipo2
 * Propiedades
 * 	-Basicas:
 * 		-real apuestasMaximas
 * 		-entero cantidadGoles
 * 		-caracter equipo
 * 	-Derivadas
 * 	-Compartidas
 *
 * Metodos de la interfaz
 * getters y setters
 * */
package clases;
import java.util.GregorianCalendar;

public class ApuestaTipo2 extends Apuesta{
    //private double apuestasMaximas;
    private int cantidadGoles;
    private char equipo;

    public ApuestaTipo2(int id, double cuota, int cantidad, char tipo, GregorianCalendar fechaHora, UsuarioImpl usuario, PartidoImpl partido, double apuestasMaximas, int cantidadGoles, char equipo) {
        super(id, cuota, cantidad, tipo, fechaHora, usuario, partido);
        //this.apuestasMaximas = apuestasMaximas;
        this.cantidadGoles = cantidadGoles;
        this.equipo = equipo;
    }

    public ApuestaTipo2() {
        super();
        //this.apuestasMaximas = 0.0;
        this.cantidadGoles = 0;
        this.equipo = ' ';
    }

    //Getters y setters
//    public double getApuestasMaximas() {
//        return apuestasMaximas;
//    }
//
//    public void setApuestasMaximas(double apuestasMaximas) {
//        this.apuestasMaximas = apuestasMaximas;
//    }

    public int getCantidadGoles() {
        return cantidadGoles;
    }

    public void setCantidadGoles(int cantidadGoles) {
        this.cantidadGoles = cantidadGoles;
    }

    public char getEquipo() {
        return equipo;
    }

    public void setEquipo(char equipo) {
        this.equipo = equipo;
    }
}
