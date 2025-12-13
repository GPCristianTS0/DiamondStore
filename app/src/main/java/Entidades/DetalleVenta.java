package Entidades;

public class DetalleVenta {
    private int id_detalle;
    private int id_venta;
    private String id_producto;
    private int cantidad;
    private int precio;

    /// Variable para usar en agregar al carrito en ventas
    private Productos producto;


    public DetalleVenta() {
    }

    public DetalleVenta(int id_detalle, int id_venta, String id_producto, int cantidad, int precio) {
        this.id_detalle = id_detalle;
        this.id_venta = id_venta;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio = precio;
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
                ", cantidad=" + cantidad +
                ", precio=" + precio  +
                '}';
    }
    public String toValues(){
        return id_venta+",'"+id_producto+"',"+cantidad+","+precio;
    }
}
