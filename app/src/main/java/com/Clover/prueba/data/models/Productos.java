package com.Clover.prueba.data.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Productos implements Serializable {
    private String rutaImagen;
    private String id;
    private String nombre;
    private String marca;
    private String seccion;
    private double precioPublico;
    private double precioNeto;
    private String descripcion;
    private int vendidos;
    private int stock;
    private boolean ventaxpeso;
    private String ultimoPedido;

    private int id_seccion;

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

    public double getPrecioPublico() {
        return precioPublico;
    }

    public void setPrecioPublico(double precioPublico) {
        this.precioPublico = precioPublico;
    }

    public double getPrecioNeto() {
        return precioNeto;
    }

    public void setPrecioNeto(double precioNeto) {
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

    public boolean isVentaxpeso() {
        return ventaxpeso;
    }
    public int getVentaxpeso(){
        return ventaxpeso?1:0;
    }
    public void setVentaxpeso(boolean ventaxpeso) {
        this.ventaxpeso = ventaxpeso;
    }

    public String getUltimoPedido() {
        return ultimoPedido;
    }

    public void setUltimoPedido(String ultimoPedido) {
        this.ultimoPedido = ultimoPedido;
    }
    public double getMargenGanacia(){
        return (precioNeto * 100) /precioPublico;
    }

    public String toColumns(){
        return "rutaImagen,id,nombre,marca,precioPublico,precioNeto,descripcion,vendidos,stock,ultimo_Pedido";
    }
    public static ArrayList<String>
    getArrayColumn(){
        ArrayList<String> array = new ArrayList<>();
        array.add("rutaImagen");
        array.add("id_producto");
        array.add("nombre_producto");
        array.add("marca");
        array.add("seccion");
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
                ", ventaxpeso=" + ventaxpeso +
                ", ultimoPedido='" + ultimoPedido + '\'' +
                '}';
    }
}
