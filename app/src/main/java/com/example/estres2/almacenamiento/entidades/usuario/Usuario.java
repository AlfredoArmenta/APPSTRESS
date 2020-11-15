package com.example.estres2.almacenamiento.entidades.usuario;

import java.io.Serializable;

public class Usuario implements Serializable {
    // Creamos las variables String que serán las encargadas de componer los parametros del objeto Usuario
    String Boleta, Nombre, Edad, Semestre, Genero, Password;

    public Usuario() {
    }

    public Usuario(String boleta, String nombre, String edad, String genero, String semestre, String password) {
        Boleta = boleta;
        Nombre = nombre;
        Edad = edad;
        Semestre = semestre;
        Genero = genero;
        Password = password;
    }

    public String getBoleta() {
        return Boleta;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getEdad() {
        return Edad;
    }

    public String getSemestre() {
        return Semestre;
    }

    public String getGenero() {
        return Genero;
    }

    public String getPassword() {
        return Password;
    }

    public void setBoleta(String boleta) {
        Boleta = boleta;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setEdad(String edad) {
        Edad = edad;
    }

    public void setSemestre(String semestre) {
        Semestre = semestre;
    }

    public void setGenero(String genero) {
        Genero = genero;
    }

    public void setPassword(String password) {
        Password = password;
    }
    // ********************** Fin de la clase InicioSesión ************************ //
}