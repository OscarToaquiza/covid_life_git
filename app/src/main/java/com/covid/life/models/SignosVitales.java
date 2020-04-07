package com.covid.life.models;

import java.util.Date;

public class SignosVitales {
    private String idPaciente;
    private Double temperatura;
    private Double frecuenciaCardiaca;
    private Double saturacionOxigeno;
    private Date fecha;

    public  SignosVitales(){

    }
    public SignosVitales(String idPaciente, Double temperatura, Double frecuenciaCardiaca, Double saturacionOxigeno, Date fecha) {
        this.idPaciente = idPaciente;
        this.temperatura = temperatura;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.saturacionOxigeno = saturacionOxigeno;
        this.fecha = fecha;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(Double frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public Double getSaturacionOxigeno() {
        return saturacionOxigeno;
    }

    public void setSaturacionOxigeno(Double saturacionOxigeno) {
        this.saturacionOxigeno = saturacionOxigeno;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
