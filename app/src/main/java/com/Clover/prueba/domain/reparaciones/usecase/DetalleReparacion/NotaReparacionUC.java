package com.Clover.prueba.domain.reparaciones.usecase.DetalleReparacion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.Clover.prueba.data.dao.interfaces.IConfiguracion;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.domain.reparaciones.DTO.NotaReparacionDTO;
import com.Clover.prueba.domain.reparaciones.constantes.EstadoReparcion;
import com.Clover.prueba.domain.reparaciones.validators.TicketsValidator;
import com.Clover.prueba.services.generators.ImageGenerator;
import com.Clover.prueba.services.generators.TicketReparacionGenerator;
import com.Clover.prueba.services.sharing.ShareManager;
import com.Clover.prueba.services.storage.StorageImage;

public class NotaReparacionUC {
    Configuracion configuracion ;
    private final StorageImage storageImage;
    private final ImageGenerator imageGenerator;
    private final TicketReparacionGenerator ticketGenerator;
    private final ShareManager shareManager;
    private final TicketsValidator validator;

    public NotaReparacionUC(IConfiguracion daoConf, StorageImage storageImage, ImageGenerator imageGenerator, TicketReparacionGenerator ticketGenerator, ShareManager shareManager, TicketsValidator validator) {
        this.storageImage = storageImage;
        this.imageGenerator = imageGenerator;
        this.ticketGenerator = ticketGenerator;
        this.shareManager = shareManager;
        this.validator = validator;
        configuracion = daoConf.getConfiguracion();
    }
    public Intent execute(Reparaciones reparacion, Clientes cliente, boolean whatsapp) throws Exception{
        return shareManager.compartirImagen(configuracion.getMensajeShare(), crear(reparacion,cliente), whatsapp);
    }
    private Uri crear(Reparaciones reparacion, Clientes cliente) throws Exception {


        // Validar
        validator.validarNotaReparacion(configuracion, reparacion, cliente);

        // Mappear DTO
        NotaReparacionDTO datosView = new NotaReparacionDTO();

        // Datos configuracion empresa
        datosView.setNombreNegocio(configuracion.getNombreNegocio());
        datosView.setEslogan(configuracion.getEslogan());
        datosView.setTelefonoNegocio(configuracion.getTelefono());
        datosView.setDireccionNegocio(configuracion.getDireccion());
        datosView.setRutaLogo(configuracion.getRutaLogo());
        datosView.setNotaGarantia(configuracion.getNotaGarantia());

        // Datos reparacion
        datosView.setFolio(reparacion.getId_reparacion());
        datosView.setFechaIngreso(formatFecha(reparacion.getFecha_ingreso()));
        if (reparacion.getFecha_promesa()==null)
            datosView.setFechaPromesa(formatFecha(reparacion.getFecha_ingreso()));
        else
            datosView.setFechaPromesa(formatFecha(reparacion.getFecha_promesa()));
        datosView.setEquipo(reparacion.getEquipo());
        datosView.setColor(reparacion.getColor());
        datosView.setFallaReportada(reparacion.getFalla_reportada());
        if (reparacion.getEstado() != null && !reparacion.getEstado().trim().isEmpty()) {
            datosView.setEstadoReparacion(EstadoReparcion.fromString(reparacion.getEstado()));
        } else {
            datosView.setEstadoReparacion(EstadoReparcion.RECIBIDO);
        }
        datosView.setMontoTotal((int) reparacion.getMonto_total());
        datosView.setAnticipo((int) reparacion.getAnticipo());
        datosView.setRestan((int) reparacion.getMonto_pendiente());

        // Datos cliente
        datosView.setNombreCliente(cliente.getNombre_cliente());
        datosView.setTelefonoCliente(cliente.getId_cliente());

        // Generar View
        View ticketView = ticketGenerator.generarTicketReparacion(datosView);

        // Convertir View a Imagen Bitmap
        Bitmap bitmap = imageGenerator.getImageBitmap(ticketView, 0);

        // Guardar Imagen Temporalmente
        return storageImage.guardarImagenTemporal(bitmap);
    }
    private String formatFecha(String fecha){
        if (fecha != null && fecha.contains(" ")) {
            return fecha.split(" ")[0];
        } else {
            return fecha != null ? fecha : "---";
        }
    }
}