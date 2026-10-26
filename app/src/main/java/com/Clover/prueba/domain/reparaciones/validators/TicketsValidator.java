package com.Clover.prueba.domain.reparaciones.validators;

import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.Reparaciones;

public class TicketsValidator {

    public void validarNotaReparacion(Configuracion configuracion, Reparaciones reparacion, Clientes cliente) {
        // Validar configuración de la empresa
        if (configuracion == null) {
            throw new IllegalArgumentException("Error al obtener datos de la empresa");
        }
        if (configuracion.getNombreNegocio() == null || configuracion.getNombreNegocio().trim().isEmpty()) {
            throw new IllegalArgumentException("Antes de compartir la nota debes configurar el nombre del negocio en ajustes");
        }
        if (configuracion.getTelefono() == null || configuracion.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("Antes de compartir la nota debes configurar el teléfono en ajustes");
        }
        if (configuracion.getDireccion() == null || configuracion.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException("Antes de compartir la nota debes configurar la dirección en ajustes");
        }

        // Validar datos de la reparación
        if (reparacion == null) {
            throw new IllegalArgumentException("La información de la reparación no es válida");
        }
        if (reparacion.getId_reparacion() <= 0) {
            throw new IllegalArgumentException("El folio de la reparación no es válido");
        }
        if (reparacion.getEquipo() == null || reparacion.getEquipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El equipo/modelo de la reparación es requerido");
        }
        if (reparacion.getFalla_reportada() == null || reparacion.getFalla_reportada().trim().isEmpty()) {
            throw new IllegalArgumentException("La falla reportada de la reparación es requerida");
        }
        if (reparacion.getFecha_ingreso() == null || reparacion.getFecha_ingreso().trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de ingreso de la reparación no es válida");
        }
        if (reparacion.getMonto_total() < 0) {
            throw new IllegalArgumentException("El monto total de la reparación no es válido");
        }

        // Validar datos del cliente
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente asignado a la reparación no existe o es inválido");
        }
        if (cliente.getNombre_cliente() == null || cliente.getNombre_cliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es requerido para generar la nota");
        }
        if (cliente.getId_cliente() == null || cliente.getId_cliente().isEmpty()) throw new IllegalArgumentException("El telefono del cliente es invalido");
    }
}