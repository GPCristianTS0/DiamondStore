package com.Clover.prueba.domain.reparaciones.DTO;

import com.Clover.prueba.domain.reparaciones.constantes.EstadoReparcion;

import java.util.Locale;

public class NotaReparacionDTO {
    //Datos reparacion
    private int folio;
    private String fechaIngreso;
    private String fechaPromesa;
    private String equipo;
    private String color;
    private String fallaReportada;
    private EstadoReparcion estadoReparacion;
    private int montoTotal;
    private int anticipo;
    private int restan;
    //Datos Cliente
    private String nombreCliente;
    private String telefonoCliente;
    //Configuracion
    private String rutaLogo;
    private String nombreNegocio;
    private String eslogan;
    private String telefonoNegocio;
    private String direccionNegocio;
    private String notaGarantia;

    public String getFolio() {
        return String.format(Locale.getDefault(), "#REP-%03d", folio);
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaPromesa() {
        return fechaPromesa;
    }

    public void setFechaPromesa(String fechaPromesa) {
        this.fechaPromesa = fechaPromesa;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFallaReportada() {
        return fallaReportada;
    }

    public void setFallaReportada(String fallaReportada) {
        this.fallaReportada = fallaReportada;
    }

    public EstadoReparcion getEstadoReparacion() {
        return estadoReparacion;
    }

    public void setEstadoReparacion(EstadoReparcion estadoReparacion) {
        this.estadoReparacion = estadoReparacion;
    }

    public String getMontoTotal() {
        return "$ "+montoTotal;
    }

    public void setMontoTotal(int montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getAnticipo() {
        return "$ "+anticipo;
    }

    public void setAnticipo(int anticipo) {
        this.anticipo = anticipo;
    }

    public String getRestan() {
        return "$ "+restan;
    }

    public void setRestan(int restan) {
        this.restan = restan;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getRutaLogo() {
        return rutaLogo;
    }

    public void setRutaLogo(String rutaLogo) {
        this.rutaLogo = rutaLogo;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    public String getEslogan() {
        return eslogan;
    }

    public void setEslogan(String eslogan) {
        this.eslogan = eslogan;
    }

    public String getTelefonoNegocio() {
        return telefonoNegocio;
    }

    public void setTelefonoNegocio(String telefonoNegocio) {
        this.telefonoNegocio = telefonoNegocio;
    }

    public String getDireccionNegocio() {
        return direccionNegocio;
    }

    public void setDireccionNegocio(String direccionNegocio) {
        this.direccionNegocio = direccionNegocio;
    }

    public String getNotaGarantia() {
        return notaGarantia;
    }

    public void setNotaGarantia(String notaGarantia) {
        this.notaGarantia = notaGarantia;
    }
}
