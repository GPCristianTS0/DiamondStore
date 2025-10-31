package Entidades;

public class Deuda {
    private int id_cliente;
    private int id_deuda;
    private int saldo_pendiente;
    private int saldo_total;
    private String plazo;//SEmanal, quincenal, mensual
    private String fecha_pago;
    private int pagos_restantes;
    private int pagos_totales;
    private int monto_pagos;
    private String id_productos;

    public Deuda(int id_cliente, int id_deuda, int saldo_pendiente, int saldo_total, String plazo, String fecha_pago, int pagos_restantes, int pagos_totales, int monto_pagos, String id_productos) {
        this.id_cliente = id_cliente;
        this.id_deuda = id_deuda;
        this.saldo_pendiente = saldo_pendiente;
        this.saldo_total = saldo_total;
        this.plazo = plazo;
        this.fecha_pago = fecha_pago;
        this.pagos_restantes = pagos_restantes;
        this.pagos_totales = pagos_totales;
        this.monto_pagos = monto_pagos;
        this.id_productos = id_productos;
    }

    public Deuda() {
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getSaldo_pendiente() {
        return saldo_pendiente;
    }

    public void setSaldo_pendiente(int saldo_pendiente) {
        this.saldo_pendiente = saldo_pendiente;
    }

    public int getSaldo_total() {
        return saldo_total;
    }

    public void setSaldo_total(int saldo_total) {
        this.saldo_total = saldo_total;
    }

    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public int getPagos_restantes() {
        return pagos_restantes;
    }

    public void setPagos_restantes(int pagos_restantes) {
        this.pagos_restantes = pagos_restantes;
    }

    public int getPagos_totales() {
        return pagos_totales;
    }

    public void setPagos_totales(int pagos_totales) {
        this.pagos_totales = pagos_totales;
    }

    public int getMonto_pagos() {
        return monto_pagos;
    }

    public void setMonto_pagos(int monto_pagos) {
        this.monto_pagos = monto_pagos;
    }

    public String getId_productos() {
        return id_productos;
    }

    public void setId_productos(String id_productos) {
        this.id_productos = id_productos;
    }

    public String getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(String fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public int getId_deuda() {
        return id_deuda;
    }

    public void setId_deuda(int id_deuda) {
        this.id_deuda = id_deuda;
    }

    public String toColumns() {
        return "id_cliente" +
                "id_deuda"  +
                ", saldo_pendiente" +
                ", saldo_total" +
                ", plazo" +
                ", fehca_pago" +
                ", pagos_restantes" +
                ", pagos_totales" +
                ", monto_pagos" +
                ", id_productos";
    }
    @Override
    public String toString() {
        return "Deuda{" +
                "id_cliente=" + id_cliente +
                "id_deuda="+id_deuda+
                ", saldo_pendiente=" + saldo_pendiente +
                ", saldo_total=" + saldo_total +
                ", plazo='" + plazo + '\'' +
                ", fehca_pago='" + fecha_pago + '\'' +
                ", pagos_restantes=" + pagos_restantes +
                ", pagos_totales=" + pagos_totales +
                ", monto_pagos=" + monto_pagos +
                ", id_productos='" + id_productos + '\'' +
                '}';
    }
}
