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
import conexion.ConexionJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

public class ApuestaTipo2 extends Apuesta{
    private double apuestasMaximas;
    private int cantidadGoles;
    private char equipo;

    public ApuestaTipo2(int id, double cuota, int cantidad, char tipo, GregorianCalendar fechaHora, UsuarioImpl usuario, PartidoImpl partido, double apuestasMaximas, int cantidadGoles, char equipo) {
        super(id, cuota, cantidad, tipo, fechaHora, usuario, partido);
        this.apuestasMaximas = apuestasMaximas;
        this.cantidadGoles = cantidadGoles;
        this.equipo = equipo;
    }

    public ApuestaTipo2() {
        super();
        this.apuestasMaximas = 0.0;
        this.cantidadGoles = 0;
        this.equipo = ' ';
    }

    /*
     * Signatura: public abstract void consultarResultadoApuesta(int id)
     * Comentario: muestra los resultados de una apuesta anterior del tipo2
     * Precondiciones: los datos de la apuesta deberán existir en la BBDD
     * Entradas:
     * Salidas:
     * Postcondiciones:
     * */
    @Override
    public void consultarResultadoApuesta(int id) {
        ConexionJDBC objConexion = new ConexionJDBC();
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultset = null;
        ApuestaTipo2 apuestaTipo2 = new ApuestaTipo2();
        String sentenciaSql = "SELECT * FROM Apuestas_tipo2 WHERE id = ?";

        try{
            conexion = objConexion.getConnection();
            preparedStatement = conexion.prepareStatement(sentenciaSql);
            preparedStatement.setInt(1, id);
            resultset = preparedStatement.executeQuery();

            while (resultset.next()){
                apuestaTipo2.setId(resultset.getInt("id"));
                apuestaTipo2.setApuestasMaximas(resultset.getInt("apuestasMáximas"));
                apuestaTipo2.setCantidadGoles(resultset.getInt("gol"));
                apuestaTipo2.setEquipo(resultset.getString("puja").charAt(0));
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
        System.out.println("Id: " + apuestaTipo2.getId() + " Apuestas Máximas: " + apuestaTipo2.getApuestasMaximas() + " Goles : " + apuestaTipo2.getCantidadGoles() + " Puja: " + apuestaTipo2.getEquipo());

    }


    //Getters y setters
    public double getApuestasMaximas() {
        return apuestasMaximas;
    }

    public void setApuestasMaximas(double apuestasMaximas) {
        this.apuestasMaximas = apuestasMaximas;
    }

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
