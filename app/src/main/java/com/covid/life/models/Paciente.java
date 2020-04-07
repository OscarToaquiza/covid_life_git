package com.covid.life.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class Paciente {
    private String id;
    private String cedula;
    private String nombres;
    private String apellido;
    private String correo;
    private Timestamp fechaNacimiento;
    private String genero;
    private String provincia;
    private String canton;
    private String telefono;
    private GeoPoint ubicacion;
    private String direccion;
    private String aislado_por;
    private String alergia_medicamentos;
    private String tiene_diagnosticado_enfermedad;
    private boolean es_diagnosticado_cancer;
    private boolean es_embarazada;
    private boolean esta_dando_lactar;
    private boolean fue_es_fumador;
    private boolean tiene_carnet_discapacidad;
    private boolean tiene_diabetes;
    private boolean tiene_presion_alta;
    private int estado_actual;
    private int familiares_cerco;
    private Timestamp fecha_creacion;

    public Paciente() {
    }

    public Paciente(String id, String cedula, String nombres, String apellido, String correo, Timestamp fechaNacimiento, String genero, String provincia, String canton, String telefono, GeoPoint ubicacion, String direccion, String aislado_por, String alergia_medicamentos, String tiene_diagnosticado_enfermedad, boolean es_diagnosticado_cancer, boolean es_embarazada, boolean esta_dando_lactar, boolean fue_es_fumador, boolean tiene_carnet_discapacidad, boolean tiene_diabetes, boolean tiene_presion_alta, int estado_actual, int familiares_cerco, Timestamp fecha_creacion) {
        this.id = id;
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellido = apellido;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.provincia = provincia;
        this.canton = canton;
        this.telefono = telefono;
        this.ubicacion = ubicacion;
        this.direccion = direccion;
        this.aislado_por = aislado_por;
        this.alergia_medicamentos = alergia_medicamentos;
        this.tiene_diagnosticado_enfermedad = tiene_diagnosticado_enfermedad;
        this.es_diagnosticado_cancer = es_diagnosticado_cancer;
        this.es_embarazada = es_embarazada;
        this.esta_dando_lactar = esta_dando_lactar;
        this.fue_es_fumador = fue_es_fumador;
        this.tiene_carnet_discapacidad = tiene_carnet_discapacidad;
        this.tiene_diabetes = tiene_diabetes;
        this.tiene_presion_alta = tiene_presion_alta;
        this.estado_actual = estado_actual;
        this.familiares_cerco = familiares_cerco;
        this.fecha_creacion = fecha_creacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Timestamp getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Timestamp fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public GeoPoint getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(GeoPoint ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getAislado_por() {
        return aislado_por;
    }

    public void setAislado_por(String aislado_por) {
        this.aislado_por = aislado_por;
    }

    public String getAlergia_medicamentos() {
        return alergia_medicamentos;
    }

    public void setAlergia_medicamentos(String alergia_medicamentos) {
        this.alergia_medicamentos = alergia_medicamentos;
    }

    public String getTiene_diagnosticado_enfermedad() {
        return tiene_diagnosticado_enfermedad;
    }

    public void setTiene_diagnosticado_enfermedad(String tiene_diagnosticado_enfermedad) {
        this.tiene_diagnosticado_enfermedad = tiene_diagnosticado_enfermedad;
    }

    public boolean isEs_diagnosticado_cancer() {
        return es_diagnosticado_cancer;
    }

    public void setEs_diagnosticado_cancer(boolean es_diagnosticado_cancer) {
        this.es_diagnosticado_cancer = es_diagnosticado_cancer;
    }

    public boolean isEs_embarazada() {
        return es_embarazada;
    }

    public void setEs_embarazada(boolean es_embarazada) {
        this.es_embarazada = es_embarazada;
    }

    public boolean isEsta_dando_lactar() {
        return esta_dando_lactar;
    }

    public void setEsta_dando_lactar(boolean esta_dando_lactar) {
        this.esta_dando_lactar = esta_dando_lactar;
    }

    public boolean isFue_es_fumador() {
        return fue_es_fumador;
    }

    public void setFue_es_fumador(boolean fue_es_fumador) {
        this.fue_es_fumador = fue_es_fumador;
    }

    public boolean isTiene_carnet_discapacidad() {
        return tiene_carnet_discapacidad;
    }

    public void setTiene_carnet_discapacidad(boolean tiene_carnet_discapacidad) {
        this.tiene_carnet_discapacidad = tiene_carnet_discapacidad;
    }

    public boolean isTiene_diabetes() {
        return tiene_diabetes;
    }

    public void setTiene_diabetes(boolean tiene_diabetes) {
        this.tiene_diabetes = tiene_diabetes;
    }

    public boolean isTiene_presion_alta() {
        return tiene_presion_alta;
    }

    public void setTiene_presion_alta(boolean tiene_presion_alta) {
        this.tiene_presion_alta = tiene_presion_alta;
    }

    public int getEstado_actual() {
        return estado_actual;
    }

    public void setEstado_actual(int estado_actual) {
        this.estado_actual = estado_actual;
    }

    public int getFamiliares_cerco() {
        return familiares_cerco;
    }

    public void setFamiliares_cerco(int familiares_cerco) {
        this.familiares_cerco = familiares_cerco;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
