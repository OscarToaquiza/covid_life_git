package com.covid.life.models;

import java.util.ArrayList;
import java.util.List;

public class Provincia {
    String nombre;
    int posicion;
    ArrayList <String> cantones;

    public Provincia(){

    }
    public Provincia(String nombre, ArrayList <String> cantones, int posicion) {
        this.nombre = nombre;
        this.cantones = cantones;
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getCantones() {
        return cantones;
    }

    public void setCantones(ArrayList <String> cantones) {
        this.cantones = cantones;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
}
