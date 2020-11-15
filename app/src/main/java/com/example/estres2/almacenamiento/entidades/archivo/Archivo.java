package com.example.estres2.almacenamiento.entidades.archivo;

import java.io.Serializable;

public class Archivo implements Serializable {
    String Id, Boleta;

    public Archivo() {
    }

    public Archivo(String id, String boleta) {
        Id = id;
        Boleta = boleta;
    }

    public String getId() {
        return Id;
    }

    public String getBoleta() {
        return Boleta;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setBoleta(String boleta) {
        Boleta = boleta;
    }
}
