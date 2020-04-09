package com.covid.life.models;

import java.util.Date;

public class Seguimiento {

    private String idPaciente;
    private String idDoctor;
    private Double temperatura;
    private Double ritmo_cardiaco;
    private Double sat_oxigeno;
    private Date fecha;
    private Integer dificultad_respirar;
    private Integer toma_muestra;
    private Integer tipo;
    private Integer estado_alta;

    public Seguimiento(){

    }

    public Seguimiento(String idPaciente, String idDoctor, Double temperatura, Double ritmo_cardiaco, Double sat_oxigeno, Date fecha, Integer dificultad_respirar, Integer toma_muestra, Integer tipo, Integer estado_alta) {
        this.idPaciente = idPaciente;
        this.idDoctor = idDoctor;
        this.temperatura = temperatura;
        this.ritmo_cardiaco = ritmo_cardiaco;
        this.sat_oxigeno = sat_oxigeno;
        this.fecha = fecha;
        this.dificultad_respirar = dificultad_respirar;
        this.toma_muestra = toma_muestra;
        this.tipo = tipo;
        this.estado_alta = estado_alta;
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

    public Integer getDificultad_respirar() {
        return dificultad_respirar;
    }

    public void setDificultad_respirar(Integer dificultad_respirar) {
        this.dificultad_respirar = dificultad_respirar;
    }

    public Integer getToma_muestra() {
        return toma_muestra;
    }

    public void setToma_muestra(Integer toma_muestra) {
        this.toma_muestra = toma_muestra;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getEstado_alta() {
        return estado_alta;
    }

    public void setEstado_alta(Integer estado_alta) {
        this.estado_alta = estado_alta;
    }
}
