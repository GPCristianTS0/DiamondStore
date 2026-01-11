package com.Clover.prueba.data.models;

public class DetalleVenta {
    private int id_detalle;
    private int id_venta;
    private String id_producto;
    private String nombre_producto;
    private int cantidad;
    private int precio;

    /// Variable para usar en agregar al carrito en ventas
    private Productos producto;


    public DetalleVenta() {
    }

    public DetalleVenta(int id_detalle, int id_venta, String id_producto, String nombre_producto, int cantidad, int precio, Productos producto) {
        this.id_detalle = id_detalle;
        this.id_venta = id_venta;
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.producto = producto;
    }

    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
    public String toColumns(){
        return "id_venta, id_producto, cantidad, precio";
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "DetalleVenta{" +
                "id_detalle=" + id_detalle +
                ", id_venta=" + id_venta +
                ", id_producto='" + id_producto + '\'' +
                ", nombre_producto='" + nombre_producto + '\'' +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                ", producto=" + producto +
                '}';
    }

    public String toValues(){
        return id_venta+",'"+id_producto+"',"+cantidad+","+precio;
    }
}
