package com.Clover.prueba.data.models;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class ReporteFinanciero implements Serializable {
    private double ventasTotales;
    private double costoMercancia;
    private double ganancias;
    private int margenGlobal;
    private int cantidadVentas;
    private double totalEfectivo;
    private double totalTarjeta;
    private double ticketPromedio;
    private double totalTransferencia;
    private LinkedHashMap<String, Double> datosGrafica;


    public ReporteFinanciero(){

    }

    public double getTotalTransferencia() {
        return totalTransferencia;
    }

    public void setTotalTransferencia(double totalTransferencia) {
        this.totalTransferencia = totalTransferencia;
    }

    public LinkedHashMap<String, Double> getDatosGrafica() {
        return datosGrafica;
    }

    public void setDatosGrafica(LinkedHashMap<String, Double> datosGrafica) {
        this.datosGrafica = datosGrafica;
    }

    public double getTicketPromedio() {
        return ticketPromedio;
    }

    public void setTicketPromedio(double ticketPromedio) {
        this.ticketPromedio = ticketPromedio;
    }

    public double getTotalEfectivo() {
        return totalEfectivo;
    }

    public void setTotalEfectivo(double totalEfectivo) {
        this.totalEfectivo = totalEfectivo;
    }

    public double getTotalTarjeta() {
        return totalTarjeta;
    }

    public void setTotalTarjeta(double totalTarjeta) {
        this.totalTarjeta = totalTarjeta;
    }

    public int getCantidadVentas() {
        return cantidadVentas;
    }

    public void setCantidadVentas(int cantidadVentas) {
        this.cantidadVentas = cantidadVentas;
    }

    public double getVentasTotales() {
        return ventasTotales;
    }

    public void setVentasTotales(double ventasTotales) {
        this.ventasTotales = ventasTotales;
    }

    public double getCostoMercancia() {
        return costoMercancia;
    }

    public void setCostoMercancia(double costoMercancia) {
        this.costoMercancia = costoMercancia;
    }

    public double getGanancias() {
        return ganancias;
    }

    public void setGanancias(double ganancias) {
        this.ganancias = ganancias;
    }

    public int getMargenGlobal() {
        return margenGlobal;
    }

    public void setMargenGlobal(int margenGlobal) {
        this.margenGlobal = margenGlobal;
    }

    @Override
    public String toString() {
        return "ReporteFinanciero{" +
                "ventasTotales=" + ventasTotales +
                ", costoMercancia=" + costoMercancia +
                ", ganancias=" + ganancias +
                ", margenGlobal=" + margenGlobal +
                '}';
    }

}
