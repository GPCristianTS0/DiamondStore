package com.Clover.prueba.data.models;

import java.io.Serializable;

public class Ventas implements Serializable {
    private int id_venta;
    private String id_cliente;
    private String fecha_hora;
    private double monto;
    private int total_piezas;
    private String tipo_pago;
    private String estado;
    private int dias_plazo;
    private String frecuencia_pago;
    private String fecha_limite;
    private String banco_tarjeta;
    private String numero_tarjeta;
    private String numero_aprobacion;
    private String tipo_tarjeta;
    private double pago_con;
    private int id_corte;


    public Ventas() {
    }

    public Ventas(int id_venta, String id_cliente, String fecha_hora, double monto, int total_piezas, String tipo_pago) {
        this.id_venta = id_venta;
        this.id_cliente = id_cliente;
        this.fecha_hora = fecha_hora;
        this.monto = monto;
        this.total_piezas = total_piezas;
        this.tipo_pago = tipo_pago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getDias_plazo() {
        return dias_plazo;
    }

    public void setDias_plazo(int dias_plazo) {
        this.dias_plazo = dias_plazo;
    }

    public String getFrecuencia_pago() {
        return frecuencia_pago;
    }

    public void setFrecuencia_pago(String frecuencia_pago) {
        this.frecuencia_pago = frecuencia_pago;
    }

    public String getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(String fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public String getBanco_tarjeta() {
        return banco_tarjeta;
    }

    public void setBanco_tarjeta(String banco_tarjeta) {
        this.banco_tarjeta = banco_tarjeta;
    }

    public String getNumero_tarjeta() {
        return numero_tarjeta;
    }

    public void setNumero_tarjeta(String numero_tarjeta) {
        this.numero_tarjeta = numero_tarjeta;
    }

    public String getNumero_aprobacion() {
        return numero_aprobacion;
    }

    public void setNumero_aprobacion(String numero_aprobacion) {
        this.numero_aprobacion = numero_aprobacion;
    }

    public String getTipo_tarjeta() {
        return tipo_tarjeta;
    }

    public void setTipo_tarjeta(String tipo_tarjeta) {
        this.tipo_tarjeta = tipo_tarjeta;
    }

    public double getPago_con() {
        return pago_con;
    }

    public void setPago_con(double pago_con) {
        this.pago_con = pago_con;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getTotal_piezas() {
        return total_piezas;
    }

    public void setTotal_piezas(int total_piezas) {
        this.total_piezas = total_piezas;
    }

    public String getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(String tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    public int getId_corte() {
        return id_corte;
    }

    public void setId_corte(int id_corte) {
        this.id_corte = id_corte;
    }

    public String toColumns(){
        return "id_cliente, fecha_Hora, monto, total_piezas, tipo_pago";
    }
    @Override
    public String toString() {
        return "Ventas{" +
                "id_venta=" + id_venta +
                ", id_cliente=" + id_cliente +
                ", fecha_Hora=" + fecha_hora +
                ", monto=" + monto +
                ", total_piezas=" + total_piezas +
                ", tipo_pago='" + tipo_pago + '\'' +
                '}';
    }
}
