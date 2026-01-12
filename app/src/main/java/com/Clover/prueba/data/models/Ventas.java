package com.Clover.prueba.data.models;

import java.io.Serializable;

public class Ventas implements Serializable {
    private int id_venta;
    private String id_cliente;
    private String fecha_hora;
    private int monto;
    private int total_piezas;
    private String tipo_pago;

    public Ventas() {
    }

    public Ventas(int id_venta, String id_cliente, String fecha_hora, int monto, int total_piezas, String tipo_pago) {
        this.id_venta = id_venta;
        this.id_cliente = id_cliente;
        this.fecha_hora = fecha_hora;
        this.monto = monto;
        this.total_piezas = total_piezas;
        this.tipo_pago = tipo_pago;
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

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
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
