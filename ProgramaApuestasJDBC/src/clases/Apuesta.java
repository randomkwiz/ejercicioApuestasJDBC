/*
 * Estudio de clase
 * Nombre: Apuesta
 * Propiedades
 * 	-Basicas:
 * 		-entero id
 * 		-real cuota
 * 		-entero cantidad
 * 		-caracter tipo
 * 		-fecha fechaHora
 * 		-Usuario usuario
 * 		-Partido partido
 * 	-Derivadas
 * 	-Compartidas
 *
 * Metodos de la interfaz
 * getters y setters
 * */
package clases;
import java.util.GregorianCalendar;

public abstract class Apuesta {
	
	private int id;
	private double cuota;
	private double cantidad;//la cantidad que apuesta el usuario
	private char tipo;
	GregorianCalendar fechaHora;
	private UsuarioImpl usuario;
	private PartidoImpl partido;

	public Apuesta(int id, double cuota, double cantidad, char tipo, GregorianCalendar fechaHora, UsuarioImpl usuario, PartidoImpl partido) {
		this.id = id;
		this.cuota = cuota;
		this.cantidad = cantidad;
		this.tipo = tipo;
		this.fechaHora = fechaHora;
		this.usuario = usuario;
		this.partido = partido;
	}

	public Apuesta() {
		this.id = 0;
		this.cuota = 0.0;
		this.cantidad = 0.0;
		this.tipo = ' ';
		this.fechaHora = new GregorianCalendar(1990,1,1);
		this.usuario = new UsuarioImpl();
		this.partido = new PartidoImpl();
	}

	//getter y setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getCuota() {
		return cuota;
	}
	public void setCuota(double cuota) {
		this.cuota = cuota;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public char getTipo() {
		return tipo;
	}
	public void setTipo(char tipo) {
		this.tipo = tipo;
	}
	public GregorianCalendar getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(GregorianCalendar fechaHora) {
		this.fechaHora = fechaHora;
	}
	public UsuarioImpl getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioImpl usuario) {
		this.usuario = usuario;
	}
	public PartidoImpl getPartido() {
		return partido;
	}
	public void setPartido(PartidoImpl partido) {
		this.partido = partido;
	}
	



}
