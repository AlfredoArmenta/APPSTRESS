package com.example.estres2;

import java.io.Serializable;

public class Usuario implements Serializable {

    // Creamos las variables String que serán las encargadas de componer los parametros del objeto Usuario
    String Boleta,Nombre, Edad, Semestre, UnidadA, Genero, Contraseña;

    public Usuario() {
    }

    // Se crea el Constructor con sus respestivas variables.
    public Usuario(String boleta, String nombre, String edad, String genero, String semestre, String unidadA, String contraseña) {
        Boleta = boleta;
        Nombre = nombre;
        Edad = edad;
        Genero = genero;
        Semestre = semestre;
        UnidadA = unidadA;
        Contraseña = contraseña;
    }

    // Se generan las funciones get() y set(), para asignar y obtener los valores del objeto Usuario
    public String getBoleta() { return Boleta; }

    public void setBoleta(String boleta) { Boleta = boleta; }

    public String getEdad() { return Edad; }

    public void setEdad(String edad) { Edad = edad; }

    public String getSemestre() { return Semestre; }

    public void setSemestre(String semestre) { Semestre = semestre; }

    public String getNombre() { return Nombre; }

    public void setNombre(String nombre) { Nombre = nombre; }

    public String getUnidadA() { return UnidadA; }

    public void setUnidadA(String unidadA) {
        UnidadA = unidadA;
    }

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String genero) {
        Genero = genero;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    // ********************** Fin de la clase InicioSesión ************************ //
}
