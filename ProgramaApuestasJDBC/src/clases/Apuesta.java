package clases;

import java.util.GregorianCalendar;

public class Apuesta {
	
	private int id;
	private double cuota;
	private int cantidad;//la cantidad que apuesta el usuario
	private char tipo;
	GregorianCalendar fechaHora;
	private UsuarioImpl usuario;
	private PartidoImpl partido;
	
	
	
	
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
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
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
