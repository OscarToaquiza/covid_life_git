package com.covid.life.models;

import java.util.List;

public class Provincia {
    String nombre;
    int posicion;
    List <Canton> canton;

    public Provincia(){

    }
    public Provincia(String nombre, List <Canton> canton, int posicion) {
        this.nombre = nombre;
        this.canton = canton;
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List <Canton> getCanton() {
        return canton;
    }

    public void setCanton(List <Canton> canton) {
        this.canton = canton;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
}
