package com.Clover.prueba.data.models;

import androidx.annotation.NonNull;
import java.io.Serializable;

public class Reparaciones implements Serializable {
    private int id_reparacion;
    private String tipo_servicio;
    private String id_cliente;
    private String equipo;
    private String color;
    private String falla_reportada;
    private String diagnostico;
    private int costo_refacciones;
    private int monto_total;
    private int anticipo;
    private int monto_pendiente;
    private String tipo_pago;
    private String estado;
    private String fecha_ingreso;
    private String fecha_promesa;
    private String fecha_entrega;
    private int id_corte;

    public Reparaciones() {
    }

    public Reparaciones(int id_reparacion, String tipo_servicio, String id_cliente, String equipo, String color, String falla_reportada, String diagnostico, int costo_refacciones, int monto_total, int anticipo, int monto_pendiente, String tipo_pago, String estado, String fecha_ingreso, String fecha_promesa, String fecha_entrega, int id_corte) {
        this.id_reparacion = id_reparacion;
        this.tipo_servicio = tipo_servicio;
        this.id_cliente = id_cliente;
        this.equipo = equipo;
        this.color = color;
        this.falla_reportada = falla_reportada;
        this.diagnostico = diagnostico;
        this.costo_refacciones = costo_refacciones;
        this.monto_total = monto_total;
        this.anticipo = anticipo;
        this.monto_pendiente = monto_pendiente;
        this.tipo_pago = tipo_pago;
        this.estado = estado;
        this.fecha_ingreso = fecha_ingreso;
        this.fecha_promesa = fecha_promesa;
        this.fecha_entrega = fecha_entrega;
        this.id_corte = id_corte;
    }

    public int getId_reparacion() {
        return id_reparacion;
    }

    public void setId_reparacion(int id_reparacion) {
        this.id_reparacion = id_reparacion;
    }

    public String getTipo_servicio() {
        return tipo_servicio;
    }

    public void setTipo_servicio(String tipo_servicio) {
        this.tipo_servicio = tipo_servicio;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
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

    public String getFalla_reportada() {
        return falla_reportada;
    }

    public void setFalla_reportada(String falla_reportada) {
        this.falla_reportada = falla_reportada;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public int getCosto_refacciones() {
        return costo_refacciones;
    }

    public void setCosto_refacciones(int costo_refacciones) {
        this.costo_refacciones = costo_refacciones;
    }

    public int getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(int monto_total) {
        this.monto_total = monto_total;
    }

    public int getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(int anticipo) {
        this.anticipo = anticipo;
    }

    public int getMonto_pendiente() {
        return monto_pendiente;
    }

    public void setMonto_pendiente(int monto_pendiente) {
        this.monto_pendiente = monto_pendiente;
    }

    public String getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(String tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getFecha_promesa() {
        return fecha_promesa;
    }

    public void setFecha_promesa(String fecha_promesa) {
        this.fecha_promesa = fecha_promesa;
    }

    public String getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(String fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public int getId_corte() {
        return id_corte;
    }

    public void setId_corte(int id_corte) {
        this.id_corte = id_corte;
    }

    @NonNull
    @Override
    public String toString() {
        return "Reparaciones{" +
                "id_reparacion=" + id_reparacion +
                ", id_cliente='" + id_cliente + '\'' +
                ", equipo='" + equipo + '\'' +
                ", monto_total=" + monto_total +
                ", estado='" + estado + '\'' +
                '}';
    }
}
