package com.Clover.prueba.data.dto;

public class ProductoMasCompradoDTO {
    private String ruta;
    private String nombre;
    private int cantidad;
    public ProductoMasCompradoDTO() {
    }

    public ProductoMasCompradoDTO(String ruta, String nombre, int cantidad) {
        this.ruta = ruta;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
