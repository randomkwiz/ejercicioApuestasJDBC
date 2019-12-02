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
import conexion.ConexionJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

public class ApuestaTipo3 extends Apuesta {
    private double apuestasMaximas;
    private char equipo;

    public ApuestaTipo3(int id, double cuota, int cantidad, char tipo, GregorianCalendar fechaHora, UsuarioImpl usuario, PartidoImpl partido, double apuestasMaximas, char equipo) {
        super(id, cuota, cantidad, tipo, fechaHora, usuario, partido);
        this.apuestasMaximas = apuestasMaximas;
        this.equipo = equipo;
    }

    public ApuestaTipo3(){
        super();
        this.apuestasMaximas = 0.0;
        this.equipo = ' ';
    }

    /*
     * Signatura: public abstract void consultarResultadoApuesta(int id)
     * Comentario: muestra los resultados de una apuesta anterior del tipo3
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
        ApuestaTipo3 apuestaTipo3 = new ApuestaTipo3();
        String sentenciaSql = "SELECT * FROM Apuestas_tipo3 WHERE id = ?";

        try{
            conexion = objConexion.getConnection();
            preparedStatement = conexion.prepareStatement(sentenciaSql);
            preparedStatement.setInt(1, idApuesta);
            resultset = preparedStatement.executeQuery();

            while (resultset.next()){
                apuestaTipo3.setId(resultset.getInt("id"));
                apuestaTipo3.setApuestasMaximas(resultset.getInt("apuestasMáximas"));
                apuestaTipo3.setEquipo(resultset.getString("puja").charAt(0));
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
        System.out.println("Id: " + apuestaTipo3.getId() + " Apuestas Máximas: " + apuestaTipo3.getApuestasMaximas() + " Puja: " + apuestaTipo3.getEquipo());
    }

    //Getters y setters
    public double getApuestasMaximas() {
        return apuestasMaximas;
    }

    public void setApuestasMaximas(double apuestasMaximas) {
        this.apuestasMaximas = apuestasMaximas;
    }

    public char getEquipo() {
        return equipo;
    }

    public void setEquipo(char equipo) {
        this.equipo = equipo;
    }
}
