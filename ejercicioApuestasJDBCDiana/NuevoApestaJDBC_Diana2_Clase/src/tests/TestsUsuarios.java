package tests;

import clases.UsuarioImpl;
import gestion.GestionUsuarios;

public class TestsUsuarios {
    public static void main(String[] args) {
        GestionUsuarios gestionUsuarios = new GestionUsuarios();
        UsuarioImpl usuario = new UsuarioImpl();
        usuario.setCorreo("angela@gmail.com");
        usuario.setPassword("123");


        System.out.println("Antes de obtener los datos");
        System.out.println("Correo usuario: " + usuario.getCorreo());
        System.out.println("Contraseña usuario: " + usuario.getPassword());
        System.out.println("ID usuario: " + usuario.getId());
        System.out.println("Saldo usuario: " + usuario.getCantidadActualDinero());
        System.out.println("isAdmin: " + usuario.isAdmin());

        System.out.println("Exito al obtener usuario completo: " + gestionUsuarios.obtenerObjetoUsuarioCompleto(usuario) );

        System.out.println("Despues de obtener los datos");
        System.out.println("Correo usuario: " + usuario.getCorreo());
        System.out.println("Contraseña usuario: " + usuario.getPassword());
        System.out.println("ID usuario: " + usuario.getId());
        System.out.println("Saldo usuario: " + usuario.getCantidadActualDinero());
        System.out.println("isAdmin: " + usuario.isAdmin());
    }
}
