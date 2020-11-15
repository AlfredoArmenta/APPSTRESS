package com.example.estres2.almacenamiento.entidades.wearable;

import java.io.Serializable;

public class Wearable implements Serializable {
    String Id, Mac;

    public Wearable() {
    }

    public Wearable(String id, String mac) {
        Id = id;
        Mac = mac;
    }

    public String getId() {
        return Id;
    }

    public String getMac() {
        return Mac;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setMac(String mac) {
        Mac = mac;
    }
}