package com.covid.life.models;

import java.util.Date;

public class Seguimiento {

    private String idPaciente;
    private String idDoctor;
    private Double temperatura;
    private Double ritmo_cardiaco;
    private Double sat_oxigeno;
    private Date fecha;
    private boolean creadoporPaciente;
    private String Latitud;
    private String Longitud;
    private String Direccion;

    public Seguimiento(){

    }

    public Seguimiento(String idPaciente, String idDoctor, Double temperatura, Double ritmo_cardiaco, Double sat_oxigeno, Date fecha, boolean creadoporPaciente, String latitud, String longitud, String direccion) {
        this.idPaciente = idPaciente;
        this.idDoctor = idDoctor;
        this.temperatura = temperatura;
        this.ritmo_cardiaco = ritmo_cardiaco;
        this.sat_oxigeno = sat_oxigeno;
        this.fecha = fecha;
        this.creadoporPaciente = creadoporPaciente;
        Latitud = latitud;
        Longitud = longitud;
        Direccion = direccion;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(String idDoctor) {
        this.idDoctor = idDoctor;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getRitmo_cardiaco() {
        return ritmo_cardiaco;
    }

    public void setRitmo_cardiaco(Double ritmo_cardiaco) {
        this.ritmo_cardiaco = ritmo_cardiaco;
    }

    public Double getSat_oxigeno() {
        return sat_oxigeno;
    }

    public void setSat_oxigeno(Double sat_oxigeno) {
        this.sat_oxigeno = sat_oxigeno;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isCreadoporPaciente() {
        return creadoporPaciente;
    }

    public void setCreadoporPaciente(boolean creadoporPaciente) {
        this.creadoporPaciente = creadoporPaciente;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }
}
