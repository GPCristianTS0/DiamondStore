package com.Clover.prueba.ui.ventas;

import android.content.Context;
import android.util.Log;

import com.Clover.prueba.data.controller.ControllerClient;
import com.Clover.prueba.data.controller.ControllerProducto;
import com.Clover.prueba.data.controller.ControllerVentas;
import com.Clover.prueba.data.dao.ClientesDAO;
import com.Clover.prueba.data.dao.ProductoDAO;
import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.DetalleVenta;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.data.models.Ventas;
import com.Clover.prueba.utils.TicketUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class VentasModel {
    private final ControllerVentas controllerVentas;
    private final ControllerProducto controller;
    private final ArrayList<DetalleVenta> detallesVenta = new ArrayList<>();
    private int total;
    private Clientes cliente;
    private Context context;

    public VentasModel(Context context) {
        controller = new ProductoDAO(context);
        controllerVentas = new VentasDAO(context);
        this.context = context;
    }

    public ArrayList<DetalleVenta> getDetallesVenta() {
        return detallesVenta;
    }

    public int getTotal() {
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
        ControllerClient controller = new ClientesDAO(context);
        cliente = controller.getClient(idCliente);
        return cliente.getId_cliente() != null;
    }

    public String getClienteNombre(){
        if (cliente == null) return "";
        return cliente.getNombre_cliente();
    }

    protected String agregarAlCarrito(String codigo){
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

    protected String disminuirClick(int position){
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
    protected boolean isVacio(){
        return detallesVenta.isEmpty();
    }
    protected void vaciarCarrito(){
        detallesVenta.clear();
        total = 0;
        cliente = null;
    }
    Ventas venta = new Ventas();
    protected void ventaConfirmada(String tipoPago){
        if (cliente == null) venta.setId_cliente("N/A");
        else venta.setId_cliente(cliente.getId_cliente());
        venta.setMonto(total);
        venta.setTotal_piezas(totalpiezas());
        venta.setTipo_pago(tipoPago);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault());
        String fecha = LocalDateTime.now().format(format);
        venta.setFecha_hora(fecha);
        //Agregar venta
        controllerVentas.addVenta(venta, detallesVenta);
    }
    public void compartirTicket(){
        TicketUtils ticketUtils = new TicketUtils();
        Log.i("Clover_App", "detallesVenta: "+detallesVenta.toString());
        ticketUtils.generarYCompartirTicket(context, getClienteNombre(), venta, detallesVenta);
        vaciarCarrito();
    }

}
