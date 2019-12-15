/*
 * Estudio de clase
 * Nombre: ApuestaTipo2
 * Propiedades
 * 	-Basicas:
 * 		-real apuestasMaximas
 * 		-caracter equipo
 * 	-Derivadas
 * 	-Compartidas
 *
 * Metodos de la interfaz
 * getters y setters
 * */
package clases;
import java.util.GregorianCalendar;

public class ApuestaTipo3 extends Apuesta {
    //private double apuestasMaximas;
    private char equipo;

    public ApuestaTipo3(int id, double cuota, int cantidad, char tipo, GregorianCalendar fechaHora, UsuarioImpl usuario, PartidoImpl partido, double apuestasMaximas, char equipo) {
        super(id, cuota, cantidad, tipo, fechaHora, usuario, partido);
        //this.apuestasMaximas = apuestasMaximas;
        this.equipo = equipo;
    }

    public ApuestaTipo3(){
        super();
        //this.apuestasMaximas = 0.0;
        this.equipo = ' ';
    }

    //Getters y setters
//    public double getApuestasMaximas() {
//        return apuestasMaximas;
//    }

//    public void setApuestasMaximas(double apuestasMaximas) {
//        this.apuestasMaximas = apuestasMaximas;
//    }

    public char getEquipo() {
        return equipo;
    }

    public void setEquipo(char equipo) {
        this.equipo = equipo;
    }
}
