package com.Clover.prueba.data.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Gastos implements Serializable {
    private int id_gasto;
    private String descripcion;
    private double monto;
    private String fecha_hora;
    private String metodo_pago;
    private int id_corte;
    private int id_proveedor;

    public Gastos() {
    }

    public Gastos(int id_gasto, String descripcion, double monto, String fecha_hora, String metodo_pago, int id_corte, int id_proveedor) {
        this.id_gasto = id_gasto;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha_hora = fecha_hora;
        this.metodo_pago = metodo_pago;
        this.id_corte = id_corte;
        this.id_proveedor = id_proveedor;
    }

    public int getId_gasto() {
        return id_gasto;
    }

    public void setId_gasto(int id_gasto) {
        this.id_gasto = id_gasto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public int getId_corte() {
        return id_corte;
    }

    public void setId_corte(int id_corte) {
        this.id_corte = id_corte;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    @NonNull
    @Override
    public String toString() {
        return "Gastos{" +
                "id_gasto=" + id_gasto +
                ", descripcion='" + descripcion + '\'' +
                ", monto=" + monto +
                ", fecha_hora='" + fecha_hora + '\'' +
                ", metodo_pago='" + metodo_pago + '\'' +
                ", id_corte=" + id_corte +
                ", id_proveedor=" + id_proveedor +
                '}';
    }
}
