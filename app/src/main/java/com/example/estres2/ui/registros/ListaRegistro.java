package com.example.estres2.ui.registros;

public class ListaRegistro {
    private String nombreRegistro;
    private int imagenEliminar;

    public ListaRegistro(String resgistro, int eliminar) {
        nombreRegistro = resgistro;
        imagenEliminar = eliminar;
    }

    public String getNombreRegistro() {
        return nombreRegistro;
    }

    public int getImagenEliminar() {
        return imagenEliminar;
    }

}
