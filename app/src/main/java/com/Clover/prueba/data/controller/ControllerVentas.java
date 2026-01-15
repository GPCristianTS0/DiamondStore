package com.Clover.prueba.data.controller;

import java.util.ArrayList;

import com.Clover.prueba.data.models.DetalleVenta;
import com.Clover.prueba.data.models.Ventas;

public interface ControllerVentas {
    public void addVenta(Ventas venta, ArrayList<DetalleVenta> detallesVenta);

    public void deleteVenta(Ventas venta);

    public ArrayList<Ventas> getVentas();
    public ArrayList<Ventas> getVentas(String mes, String year, String busqueda);
    public ArrayList<DetalleVenta> getDetalleVentas(int idVenta); //Obtiene el detalle de la venta por el id de la venta "Folio"
    public ArrayList<String> getAnios();
    public int getGanancias();
    public int getVentasTotales();
    public String getProductoMasVendido();
}
