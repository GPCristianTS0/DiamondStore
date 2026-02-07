package com.Clover.prueba.data.models;

import static com.Clover.prueba.utils.Constantes.CONST_METODO_CREDITO;
import static com.Clover.prueba.utils.Constantes.VENTA_PENDIENTE;

import android.content.Context;
import android.util.Log;

import com.Clover.prueba.data.controller.CorteCajaController;
import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.dao.interfaces.IProducto;
import com.Clover.prueba.data.dao.interfaces.IVentas;
import com.Clover.prueba.data.dao.ClientesDAO;
import com.Clover.prueba.data.dao.ProductoDAO;
import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.utils.Constantes;
import com.Clover.prueba.utils.TicketUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class CarritoDTO implements Serializable {
    private final IVentas iVentas;
    private final IProducto controller;
    private final ArrayList<DetalleVenta> detallesVenta = new ArrayList<>();
    private double total;
    private Clientes cliente;
    private Context context;

    public CarritoDTO(Context context) {
        controller = new ProductoDAO(context);
        iVentas = new VentasDAO(context);
        this.context = context;
    }

    public ArrayList<DetalleVenta> getDetallesVenta() {
        return detallesVenta;
    }

    public double getTotal() {
        recalcularTotal();
        return total;
    }
    public int totalpiezas(){
        int total = 0;
        for (DetalleVenta detalle: detallesVenta){
            total += detalle.getCantidad();
        }
        return total;
    }
    public boolean setCliente(String idCliente) {
        IClient controller = new ClientesDAO(context);
        cliente = controller.getClient(idCliente);
        return cliente.getId_cliente() != null;
    }

    public String getClienteNombre(){
        if (cliente == null) return "";
        return cliente.getNombre_cliente();
    }

    public String agregarAlCarrito(String codigo){
        Productos producto = controller.getProductoCode(codigo);
        //Comprobacion del producto si esta agotado o no existe
        if (producto.getId() == null) return "No existe";
        if (producto.getStock()==0) return "Agotado";
        int pos;
        //recorre el carrito para checar si existe en el carrito
        for (DetalleVenta detalle: detallesVenta){
            //comprueba si el producto ya esta en el carrito
            if (detalle.getProducto().getId().equals(producto.getId())){
                //Comprueba que no se pase del stock
                if (producto.getStock()<detalle.getCantidad()+1) return "Agotado";
                //Aumenta la cantidad
                detalle.setCantidad(detalle.getCantidad()+1);
                return ""+detallesVenta.indexOf(detalle);
            }
        }
        //Agrega el producto al carrito en caso que no se haya encontrado
        DetalleVenta detalleVentas = new DetalleVenta();
        detalleVentas.setId_producto(producto.getId());
        detalleVentas.setNombre_producto(producto.getNombre());
        detalleVentas.setProducto(producto);
        detalleVentas.setCantidad(1);
        detalleVentas.setPrecio(producto.getPrecioPublico());
        detalleVentas.setPrecio_neto(producto.getPrecioNeto());
        detalleVentas.setProducto(producto);
        detallesVenta.add(0, detalleVentas);
        recalcularTotal();
        return "insertado";
    }

    private void recalcularTotal(){
        total = 0;
        for (DetalleVenta detalle: detallesVenta){
            total += detalle.getPrecio()*detalle.getCantidad();
        }
    }

    public String disminuirClick(int position){
        //comprueba que la cantidad no sea 0
        DetalleVenta producto = detallesVenta.get(position);
        if (producto.getCantidad()>0) producto.setCantidad(producto.getCantidad()-1);
        //En caso que sea 0 elimina el producto
        if (producto.getCantidad()==0){
            detallesVenta.remove(position);
            return "eliminado";
        }
        return "resto";
    }
    public boolean isVacio(){
        return detallesVenta.isEmpty();
    }
    public void vaciarCarrito(){
        detallesVenta.clear();
        total = 0;
        cliente = null;
    }
    private Ventas venta;
    public void ventaConfirmada(Ventas venta){
        if (cliente == null) venta.setId_cliente("Publico General");
        else venta.setId_cliente(cliente.getId_cliente());
        venta.setMonto(total);
        venta.setTotal_piezas(totalpiezas());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        LocalDate fechaLimite = LocalDate.now();
        int dias = venta.getDias_plazo();
        venta.setFecha_limite(fechaLimite.plusDays(dias).toString());
        String fecha = LocalDateTime.now().format(format);
        venta.setFecha_hora(fecha);
        if (venta.getTipo_pago().equals(CONST_METODO_CREDITO)) {
            venta.setMonto_pendiente(total);
            venta.setEstado(VENTA_PENDIENTE);
        }
        else
            venta.setEstado(Constantes.VENTA_PAGADA);
        venta.setId_corte(new CorteCajaController(context).getCorteActual().getId_corte());
        this.venta = venta;
        //Agregar venta
        long id = iVentas.addVenta(venta, detallesVenta);
        this.venta.setId_venta((int) id);
    }
    public void compartirTicket(){
        TicketUtils ticketUtils = new TicketUtils(context);
        Log.i("Clover_App", "detallesVenta: "+detallesVenta.toString());
        ticketUtils.generarTicketVenta(context, getClienteNombre(), venta, detallesVenta, false);
        vaciarCarrito();
    }

}
