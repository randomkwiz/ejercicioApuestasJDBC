
package interfaces;
import clases.UsuarioImpl;
public interface Ingreso {

    public int getId() ;
    public void setId(int id) ;
    public double getCantidad();
    public void setCantidad(double cantidad);
    public String getDescripcion() ;
    public void setDescripcion(String descripcion) ;
    public UsuarioImpl getUsuario() ;
    public void setUsuario(UsuarioImpl usuario) ;
}
