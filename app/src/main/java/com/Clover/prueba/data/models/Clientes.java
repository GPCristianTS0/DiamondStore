package com.Clover.prueba.data.models;

import java.io.Serializable;

public class Clientes implements Serializable {
    private String id_cliente;
    private String nombre_cliente;
    private String apodo;
    private String direccion;
    private int puntos;
    private int saldo;
    private String fecha_registro;

    public Clientes() {

    }
    public Clientes(String id, String nombre, String apodo, int saldo){
        this.id_cliente = id;
        this.nombre_cliente = nombre;
        this.apodo = apodo;
        this.saldo = saldo;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String[] getColumn(){
        return new String[]{"ID", "Nombre", "Apodo", "Saldo", "Puntos"};
    }

    @Override
    public String toString() {
        return "Clientes{" +
                "id=" + id_cliente +
                ", nombre='" + nombre_cliente + '\'' +
                ", apodo='" + apodo + '\'' +
                ", saldo=" + saldo +
                ", direccion='" + direccion + '\'' +
                ", puntos=" + puntos +
                ", fecha_registro='" + fecha_registro + '\'' +
                '}';
    }
}
