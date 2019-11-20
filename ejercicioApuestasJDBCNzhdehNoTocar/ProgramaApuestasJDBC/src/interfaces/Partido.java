package interfaces;

import java.util.GregorianCalendar;

public interface Partido {
    public int getId() ;

    public void setId(int id) ;

    public int getGolesLocal() ;

    public void setGolesLocal(int golesLocal) ;

    public int getGolesVisitante() ;

    public void setGolesVisitante(int golesVisitante);

    public GregorianCalendar getFechaInicio() ;

    public void setFechaInicio(GregorianCalendar fechaInicio) ;

    public GregorianCalendar getFechaFin() ;

    public void setFechaFin(GregorianCalendar fechaFin) ;

    public String getNombreLocal() ;

    public void setNombreLocal(String nombreLocal) ;

    public String getNombreVisitante() ;

    public void setNombreVisitante(String nombreVisitante) ;
}
