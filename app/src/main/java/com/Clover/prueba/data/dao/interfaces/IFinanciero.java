package com.Clover.prueba.data.dao.interfaces;

public interface IFinanciero {
    public double getVentasTotales(String fechaInicial, String fechaFin);
    public double getCostoMercancia(String fechaInicial, String fechaFin);
    public int getCantidadVentas(String fechaInicial, String fechaFin);
    public double getTotalPorMetodoPago(String metodoPago, String fechaInicial, String fechaFin);


}
