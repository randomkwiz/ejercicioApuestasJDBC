package tests;

import clases.UsuarioImpl;
import gestion.GestionUsuarios;

public class TestsUsuarios {
    public static void main(String[] args) {
        GestionUsuarios gestionUsuarios = new GestionUsuarios();
//        UsuarioImpl usuario = new UsuarioImpl();
//        usuario.setCorreo("angela@gmail.com");
//        usuario.setPassword("123");
//
//
//        System.out.println("Antes de obtener los datos");
//        System.out.println("Correo usuario: " + usuario.getCorreo());
//        System.out.println("Contraseña usuario: " + usuario.getPassword());
//        System.out.println("ID usuario: " + usuario.getId());
//        System.out.println("Saldo usuario: " + usuario.getCantidadActualDinero());
//        System.out.println("isAdmin: " + usuario.isAdmin());
//
//        System.out.println("Exito al obtener usuario completo: " + gestionUsuarios.obtenerObjetoUsuarioCompleto(usuario) );
//
//        System.out.println("Despues de obtener los datos");
//        System.out.println("Correo usuario: " + usuario.getCorreo());
//        System.out.println("Contraseña usuario: " + usuario.getPassword());
//        System.out.println("ID usuario: " + usuario.getId());
//        System.out.println("Saldo usuario: " + usuario.getCantidadActualDinero());
//        System.out.println("isAdmin: " + usuario.isAdmin());
//
        /*-------------------------------*/


//        UsuarioImpl nuevoUsuario = gestionUsuarios.crearObjetoUsuario();
        UsuarioImpl nuevoUsuario = new UsuarioImpl(1,150, "selena@gmail.com","123", false);
        System.out.println(nuevoUsuario.getId());
        System.out.println(nuevoUsuario.getCorreo());
        System.out.println(nuevoUsuario.getPassword());
        System.out.println(nuevoUsuario.getCantidadActualDinero());
        System.out.println(nuevoUsuario.isAdmin());

        gestionUsuarios.insertarUsuario(nuevoUsuario);

    }
}
