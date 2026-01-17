package com.Clover.prueba.data.models;

public class CorteCaja {
    private int id_corte;
    private String fecha_apertura;
    private String fecha_cierre;
    private double monto_inicial;
    private double ventas_totales;
    private double abonos_totales;
    private double gastos_totales;
    private double dinero_en_caja;
    private double diferencia;
    private String estado;

    public CorteCaja(){

    }

    public CorteCaja(int id_corte, String fecha_apertura, String fecha_cierre, double monto_inicial, double ventas_totales, double abonos_totales, double gastos_totales, double dinero_en_caja, double diferencia, String estado) {
        this.id_corte = id_corte;
        this.fecha_apertura = fecha_apertura;
        this.fecha_cierre = fecha_cierre;
        this.monto_inicial = monto_inicial;
        this.ventas_totales = ventas_totales;
        this.abonos_totales = abonos_totales;
        this.gastos_totales = gastos_totales;
        this.dinero_en_caja = dinero_en_caja;
        this.diferencia = diferencia;
        this.estado = estado;
    }

    public int getId_corte() {
        return id_corte;
    }

    public void setId_corte(int id_corte) {
        this.id_corte = id_corte;
    }

    public String getFecha_apertura() {
        return fecha_apertura;
    }

    public void setFecha_apertura(String fecha_apertura) {
        this.fecha_apertura = fecha_apertura;
    }

    public String getFecha_cierre() {
        return fecha_cierre;
    }

    public void setFecha_cierre(String fecha_cierre) {
        this.fecha_cierre = fecha_cierre;
    }

    public double getMonto_inicial() {
        return monto_inicial;
    }

    public void setMonto_inicial(double monto_inicial) {
        this.monto_inicial = monto_inicial;
    }

    public double getVentas_totales() {
        return ventas_totales;
    }

    public void setVentas_totales(double ventas_totales) {
        this.ventas_totales = ventas_totales;
    }

    public double getAbonos_totales() {
        return abonos_totales;
    }

    public void setAbonos_totales(double abonos_totales) {
        this.abonos_totales = abonos_totales;
    }

    public double getGastos_totales() {
        return gastos_totales;
    }

    public void setGastos_totales(double gastos_totales) {
        this.gastos_totales = gastos_totales;
    }

    public double getDinero_en_caja() {
        return dinero_en_caja;
    }

    public void setDinero_en_caja(double dinero_en_caja) {
        this.dinero_en_caja = dinero_en_caja;
    }

    public double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "CorteCaja{" +
                "id_corte=" + id_corte +
                ", fecha_apertura='" + fecha_apertura + '\'' +
                ", fecha_cierre='" + fecha_cierre + '\'' +
                ", monto_inicial=" + monto_inicial +
                ", ventas_totales=" + ventas_totales +
                ", abonos_totales=" + abonos_totales +
                ", gastos_totales=" + gastos_totales +
                ", dinero_en_caja=" + dinero_en_caja +
                ", diferencia=" + diferencia +
                ", estado='" + estado + '\'' +
                '}';
    }
}
