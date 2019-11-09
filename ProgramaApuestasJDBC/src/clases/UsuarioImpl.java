/*
 * Estudio de clase
 * Nombre: UsuarioImpl
 * Propiedades
 * 	-Basicas:
 * 		-entero id
 * 		-real cantidadActualDinero
 * 		-cadena correo
 *      -cadena password
 * 	-Derivadas
 * 	-Compartidas
 *
 * Metodos de la interfaz
 * getters y setters
 * */
package clases;
import interfaces.Usuario;

public class UsuarioImpl implements Usuario {
    private int id;
    private double cantidadActualDinero;
    private String correo;
    private String password;
    private boolean isAdmin;

    public UsuarioImpl(int id, double cantidadActualDinero, String correo, String password, boolean isAdmin) {
        this.id = id;
        this.cantidadActualDinero = cantidadActualDinero;
        this.correo = correo;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    public UsuarioImpl(){
        this.id = 0;
        this.cantidadActualDinero = 0;
        this.correo = " ";
        this.password = " ";
        this.isAdmin = false;
    }

    //Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCantidadActualDinero() {
        return cantidadActualDinero;
    }

    public void setCantidadActualDinero(double cantidadActualDinero) {
        this.cantidadActualDinero = cantidadActualDinero;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
