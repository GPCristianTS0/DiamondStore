package Entidades;

import java.io.Serializable;
import java.util.ArrayList;

public class Productos implements Serializable {
    private String rutaImagen;
    private String id;
    private String nombre;
    private String marca;
    private String seccion;
    private int precioPublico;
    private int precioNeto;
    private String descripcion;
    private int vendidos;
    private int stock;
    private String ultimoPedido;

    public Productos() {
    }

    public Productos(String rutaImagen, String id, String nombre, String marca, String seccion, int precioPublico, int precioNeto, String descripcion, int vendidos, int stock, String ultimoPedido) {
        this.rutaImagen = rutaImagen;
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.seccion = seccion;
        this.precioPublico = precioPublico;
        this.precioNeto = precioNeto;
        this.descripcion = descripcion;
        this.vendidos = vendidos;
        this.stock = stock;
        this.ultimoPedido = ultimoPedido;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public int getPrecioPublico() {
        return precioPublico;
    }

    public void setPrecioPublico(int precioPublico) {
        this.precioPublico = precioPublico;
    }

    public int getPrecioNeto() {
        return precioNeto;
    }

    public void setPrecioNeto(int precioNeto) {
        this.precioNeto = precioNeto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getVendidos() {
        return vendidos;
    }

    public void setVendidos(int vendidos) {
        this.vendidos = vendidos;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUltimoPedido() {
        return ultimoPedido;
    }

    public void setUltimoPedido(String ultimoPedido) {
        this.ultimoPedido = ultimoPedido;
    }

    public String toColumns(){
        return "rutaImagen,id,nombre,marca,precioPublico,precioNeto,descripcion,vendidos,stock,ultimo_Pedido";
    }
    public static ArrayList<String> getArrayColumn(){
        ArrayList<String> array = new ArrayList<>();
        array.add("rutaImagen");
        array.add("id");
        array.add("nombre");
        array.add("marca");
        array.add("precioPublico");
        array.add("precioNeto");
        array.add("descripcion");
        array.add("vendidos");
        array.add("stock");
        array.add("ultimo_Pedido");
        return array;
    }
    @Override
    public String toString() {
        return "Productos{" +
                "rutaImagen='" + rutaImagen + '\'' +
                ", id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", marca='" + marca + '\'' +
                ", seccion='" + seccion + '\'' +
                ", precioPublico=" + precioPublico +
                ", precioNeto=" + precioNeto +
                ", descripcion='" + descripcion + '\'' +
                ", vendidos=" + vendidos +
                ", stock=" + stock +
                ", ultimoPedido='" + ultimoPedido + '\'' +
                '}';
    }
}
