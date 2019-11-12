/*
 * Estudio de clase
 * Nombre: IngresoImpl
 * Propiedades
 * 	-Basicas:
 * 		-entero id
 * 		-entero cantidad
 * 		-cadena descripcion
 *      -usuario usuario
 * 	-Derivadas
 * 	-Compartidas
 *
 * Metodos de la interfaz
 * getters y setters
 * */
package clases;
import interfaces.Ingreso;
public class IngresoImpl implements Ingreso {
    private int id;
    private double cantidad;    //he cambiado esto que era un int y debe ser double
    private String descripcion;
    private UsuarioImpl usuario;

    public IngresoImpl(int id, double cantidad, String descripcion, UsuarioImpl usuario) {
        this.id = id;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.usuario = usuario;
    }

    public IngresoImpl() {
        this.id = 0;
        this.cantidad = 0;
        this.descripcion = " ";
        this.usuario = new UsuarioImpl();
    }

    //Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public UsuarioImpl getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioImpl usuario) {
        this.usuario = usuario;
    }
}
