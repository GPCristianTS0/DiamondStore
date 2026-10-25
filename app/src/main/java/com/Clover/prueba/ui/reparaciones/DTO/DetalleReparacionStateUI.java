package com.Clover.prueba.ui.reparaciones.DTO;

import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Reparaciones;

public class DetalleReparacionStateUI {
    //Dato Reparacion
    private String folio;
    private String estado;
    //Datos Cliente
    private String nombreCliente;
    private String idCliente;
    //Datos Equipo
    private String equipo;
    private String color;
    private String fallaReportada;
    private String diagnostico;
    //Fechas
    private String fechaIngreso;
    private String fechaEntrega;
    private String fechaPromesa;
    //Financiero
    private int montoTotal;
    private int anticipo;
    private int montoPendiente;

    public DetalleReparacionStateUI() {
    }

    public DetalleReparacionStateUI(String folio, String estado, String nombreCliente, String idCliente, String equipo, String color, String fallaReportada, String fechaIngreso, String fechaEntrega, String fechaPromesa, int montoTotal, int anticipo, int montoPendiente) {
        this.folio = folio;
        this.estado = estado;
        this.nombreCliente = nombreCliente;
        this.idCliente = idCliente;
        this.equipo = equipo;
        this.color = color;
        this.fallaReportada = fallaReportada;
        this.fechaIngreso = fechaIngreso;
        this.fechaEntrega = fechaEntrega;
        this.fechaPromesa = fechaPromesa;
        this.montoTotal = montoTotal;
        this.anticipo = anticipo;
        this.montoPendiente = montoPendiente;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFallaReportada() {
        return fallaReportada;
    }

    public void setFallaReportada(String fallaReportada) {
        this.fallaReportada = fallaReportada;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getFechaPromesa() {
        return fechaPromesa;
    }

    public void setFechaPromesa(String fechaPromesa) {
        this.fechaPromesa = fechaPromesa;
    }

    public int getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(int montoTotal) {
        this.montoTotal = montoTotal;
    }

    public int getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(int anticipo) {
        this.anticipo = anticipo;
    }

    public int getMontoPendiente() {
        return montoPendiente;
    }

    public void setMontoPendiente(int montoPendiente) {
        this.montoPendiente = montoPendiente;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public void setReparacion(Reparaciones reparacion) {
        this.estado = reparacion.getEstado();
        this.folio = String.valueOf(reparacion.getId_reparacion());
        this.equipo = reparacion.getEquipo();
        this.color = reparacion.getColor();
        this.fallaReportada = reparacion.getFalla_reportada();
        this.diagnostico = reparacion.getDiagnostico();
        this.fechaIngreso = reparacion.getFecha_ingreso();
        this.fechaEntrega = reparacion.getFecha_entrega();
        this.fechaPromesa = reparacion.getFecha_promesa();
        this.montoTotal = reparacion.getMonto_total();
        this.anticipo = reparacion.getAnticipo();
        this.montoPendiente = reparacion.getMonto_pendiente();
    }
    public Reparaciones getReparacion(){
        return new Reparaciones(Integer.parseInt(folio),estado, equipo, color, fallaReportada, "", fechaIngreso, fechaPromesa, "", "", montoPendiente, anticipo, montoTotal,0, "");
    }
    public void setCliente(Clientes cliente) {
        this.nombreCliente = cliente.getNombre_cliente();
        this.idCliente = cliente.getId_cliente();
    }
    public Clientes getCliente(){
        return new Clientes(idCliente, nombreCliente);
    }
}
