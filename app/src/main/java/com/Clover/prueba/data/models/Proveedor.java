package com.Clover.prueba.data.models;

import java.io.Serializable;

public class Proveedor implements Serializable {
    private int id_proveedor;
    private String nombre_proveedor;
    private String nombre_vendedor;
    private String categoria;
    private String direccion;
    private String telefono;
    private String email;
    private String dias_visita;
    private String observaciones;
    private String fecha_registro;
    private String diasPago;

    public Proveedor() {
    }

    public Proveedor(int id_proveedor, String nombre_proveedor, String nombre_vendedor, String categoria, String direccion, String telefono, String email, String dias_visita, String observaciones, String fecha_registro, String diasPago) {
        this.id_proveedor = id_proveedor;
        this.nombre_proveedor = nombre_proveedor;
        this.nombre_vendedor = nombre_vendedor;
        this.categoria = categoria;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.dias_visita = dias_visita;
        this.observaciones = observaciones;
        this.fecha_registro = fecha_registro;
        this.diasPago = diasPago;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public String getNombre_proveedor() {
        return nombre_proveedor;
    }

    public void setNombre_proveedor(String nombre_proveedor) {
        this.nombre_proveedor = nombre_proveedor;
    }

    public String getNombre_vendedor() {
        return nombre_vendedor;
    }

    public void setNombre_vendedor(String nombre_vendedor) {
        this.nombre_vendedor = nombre_vendedor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDias_visita() {
        return dias_visita;
    }

    public void setDias_visita(String dias_visita) {
        this.dias_visita = dias_visita;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getDiasPago() {
        return diasPago;
    }

    public void setDiasPago(String diasPago) {
        this.diasPago = diasPago;
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "id_proveedor=" + id_proveedor +
                ", nombre_proveedor='" + nombre_proveedor + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", dias_visita='" + dias_visita + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", fecha_registro='" + fecha_registro + '\'' +
                ", diasPago='" + diasPago + '\'' +
                '}';
    }
    public static String[] getArrayColumnUI(){
        return new String[]{
                "Id Proveedor",
                "Nombre Proveedor",
                "Nombre Vendedor",
                "Categoria",
                "Direccion",
                "Telefono",
                "Email",
                "Dias Visita",
                "Observaciones",
                "Fecha Registro",
                "Dias Pago"
        };
    }

    public static String[] getArrayColumn(){
        return new String[]{
                "id_proveedor",
                "nombre_proveedor",
                "nombre_vendedor",
                "categoria",
                "direccion",
                "telefono",
                "email",
                "dias_visita",
                "observaciones",
                "fecha_registro",
                "diasPago"
        };
    }
}
