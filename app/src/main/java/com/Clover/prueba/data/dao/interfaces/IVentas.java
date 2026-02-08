package com.Clover.prueba.data.dao.interfaces;

import java.util.ArrayList;

import com.Clover.prueba.data.models.DetalleVenta;
import com.Clover.prueba.data.models.Ventas;

public interface IVentas {
    public long addVenta(Ventas venta, ArrayList<DetalleVenta> detallesVenta);

    public void deleteVenta(Ventas venta);

    public ArrayList<Ventas> getVentas();
    public ArrayList<Ventas> getVentas(String mes, String year, String busqueda);
    public ArrayList<Ventas> getVentas(String idCliente); //Obtiene las ventas que el cliente debe pagar
    public ArrayList<DetalleVenta> getDetalleVentas(int idVenta); //Obtiene el detalle de la venta por el id de la venta "Folio"
    public ArrayList<String> getAnios();
    public int getGanancias();
    public int getSaldoTotal();

    double getSaldoTotal(String idCliente);

    public double getVentasMetodoPago(String metodoPago, int id_corte);

    public String getProductoMasVendido();

    double getSaldoPendiente(String idCliente);
}
