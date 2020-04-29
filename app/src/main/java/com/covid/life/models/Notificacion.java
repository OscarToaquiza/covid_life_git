package com.covid.life.models;

import java.util.Date;

public class Notificacion {
    private String token;
    private String url ;
    private Date fecha;

    public Notificacion(){

    }

    public Notificacion(String token, String url, Date fecha) {
        this.token = token;
        this.url = url;
        this.fecha = fecha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
