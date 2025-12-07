package BD.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import Entidades.DetalleVenta;
import Entidades.Ventas;

public interface ControllerVentas {
    public void createTable();
    public void addVenta(Ventas venta);
    public void addDetalleVenta(ArrayList<DetalleVenta> detalleVenta);

    public void deleteVenta(Ventas venta);
    public void deleteDetalleVenta(DetalleVenta detalleVenta);

    public ArrayList<Ventas> getVentas();
    public ArrayList<Ventas> getVentas(String mes, String year, String busqueda);
    public ArrayList<DetalleVenta> getDetalleVentas(int idVenta, LocalDateTime fecha); //Obtiene el detalle de la venta por el id de la venta "Folio"
    public ArrayList<String> getAnios();


}
