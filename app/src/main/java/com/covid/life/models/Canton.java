package com.covid.life.models;

import java.util.ArrayList;

public class Canton {
    String nombre;
    int posicion;
    ArrayList<String> parroquias;

    public Canton(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public ArrayList<String> getParroquias() {
        return parroquias;
    }

    public void setParroquias(ArrayList<String> parroquias) {
        this.parroquias = parroquias;
    }
}
