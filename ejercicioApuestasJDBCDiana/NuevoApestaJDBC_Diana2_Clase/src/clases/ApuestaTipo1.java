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
import conexion.ConexionJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

public class ApuestaTipo1 extends Apuesta{
    private double apuestasMaximas;
    private int golesLocal;
    private int golesVisitante;

    public ApuestaTipo1(int id, double cuota, int cantidad, char tipo, GregorianCalendar fechaHora, UsuarioImpl usuario, PartidoImpl partido, double apuestasMaximas, int golesLocal, int golesVisitante) {
        super(id, cuota, cantidad, tipo, fechaHora, usuario, partido);
        this.apuestasMaximas = apuestasMaximas;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
    }

    public ApuestaTipo1() {
        super();
        this.apuestasMaximas = 0.0;
        this.golesLocal = 0;
        this.golesVisitante = 0;
    }


    public ApuestaTipo1(double apuestasMaximas, int golesLocal, int golesVisitante) {
        this.apuestasMaximas = apuestasMaximas;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
    }

    //TODO añadir a común

    /*
     * Signatura: public abstract void consultarResultadoApuesta(int id)
     * Comentario: muestra los resultados de una apuesta anterior del tipo1
     * Precondiciones: los datos de la apuesta deberán existir en la BBDD
     * Entradas: entero idApuesta
     * Salidas:
     * Postcondiciones:
     * */
    @Override
    public void consultarResultadoApuesta(int idApuesta) {
        ConexionJDBC objConexion = new ConexionJDBC();
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultset = null;
        ApuestaTipo1 apuestaTipo1 = new ApuestaTipo1();
        String sentenciaSql = "SELECT * FROM Apuestas_tipo1 WHERE id = ?";

        try{
            conexion = objConexion.getConnection();
            preparedStatement = conexion.prepareStatement(sentenciaSql);
            preparedStatement.setInt(1, idApuesta);
            resultset = preparedStatement.executeQuery();

            while (resultset.next()){
                apuestaTipo1.setId(resultset.getInt("id"));
                apuestaTipo1.setApuestasMaximas(resultset.getInt("apuestasMáximas"));
                apuestaTipo1.setGolesLocal(resultset.getInt("golLocal"));
                apuestaTipo1.setGolesVisitante(resultset.getInt("golVisitante"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                resultset.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            objConexion.closeConnection(conexion);
        }
        System.out.println("Id: " + apuestaTipo1.getId() + " Apuestas Máximas: " + apuestaTipo1.getApuestasMaximas() + " Goles Local: " + apuestaTipo1.getGolesLocal() + " Goles Visitante: " + apuestaTipo1.getGolesVisitante());
    }

    //Getters y setters
    public double getApuestasMaximas() {
        return apuestasMaximas;
    }

    public void setApuestasMaximas(double apuestasMaximas) {
        this.apuestasMaximas = apuestasMaximas;
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

}
