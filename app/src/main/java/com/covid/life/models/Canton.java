package com.covid.life.models;

public class Canton {
    String Nombre;

    public Canton(){

    }

    public Canton(String nombre) {
        Nombre = nombre;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
