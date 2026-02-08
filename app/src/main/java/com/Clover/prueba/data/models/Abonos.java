package com.Clover.prueba.data.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Abonos implements Serializable {
    private int id;
    private String idCliente;
    private double monto;
    private String fecha;
    private int idCorte;
    private double saldoAnterior;
    private double saldoActual;
    private int idEmpleado;
    private String observacion;
    private String tipoPago;


    // Constructor, getters y setters


    public Abonos() {
    }

    public Abonos(int id, String idCliente, double monto, String fecha, int idCorte, double saldoAnterior, double saldoActual, int idEmpleado, String observacion) {
        this.id = id;
        this.idCliente = idCliente;
        this.monto = monto;
        this.fecha = fecha;
        this.idCorte = idCorte;
        this.saldoAnterior = saldoAnterior;
        this.saldoActual = saldoActual;
        this.idEmpleado = idEmpleado;
        this.observacion = observacion;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdCorte() {
        return idCorte;
    }

    public void setIdCorte(int idCorte) {
        this.idCorte = idCorte;
    }

    public double getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(double saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @NonNull
    @Override
    public String toString() {
        return "Abonos{" +
                "id=" + id +
                ", idCliente=" + idCliente +
                ", monto=" + monto +
                ", fecha='" + fecha + '\'' +
                ", idCorte=" + idCorte +
                ", saldoAnterior=" + saldoAnterior +
                ", saldoActual=" + saldoActual +
                ", idEmpleado=" + idEmpleado +
                ", observacion='" + observacion + '\'' +
                '}';
    }
}
