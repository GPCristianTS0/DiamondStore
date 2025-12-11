package BD.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import Entidades.DetalleVenta;
import Entidades.Ventas;

public interface ControllerVentas {
    public void addVenta(Ventas venta, ArrayList<DetalleVenta> detallesVenta);

    public void deleteVenta(Ventas venta);

    public ArrayList<Ventas> getVentas();
    public ArrayList<Ventas> getVentas(String mes, String year, String busqueda);
    public ArrayList<DetalleVenta> getDetalleVentas(int idVenta); //Obtiene el detalle de la venta por el id de la venta "Folio"

}
