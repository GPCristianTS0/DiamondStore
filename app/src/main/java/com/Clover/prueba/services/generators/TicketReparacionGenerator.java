package com.Clover.prueba.services.generators;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import com.Clover.prueba.databinding.TicketReparacionVirtualBinding;
import com.Clover.prueba.domain.reparaciones.DTO.NotaReparacionDTO;

public class TicketReparacionGenerator {
    private final Context context;

    public TicketReparacionGenerator(Context context) {
        this.context = context;
    }

    public View generarTicketReparacion(NotaReparacionDTO datos) {
        LayoutInflater inflater = LayoutInflater.from(context);
        TicketReparacionVirtualBinding binding = TicketReparacionVirtualBinding.inflate(inflater);

        //Datos empresa
        try{
            binding.imgLogoReparacion.setImageURI(Uri.parse(datos.getRutaLogo()));
        } catch (Exception ignored) {}
        binding.txtGarantiaRep.setText(datos.getNotaGarantia());
        binding.txtNombreNegocioRep.setText(datos.getNombreNegocio());
        binding.txtEsloganRep.setText(datos.getEslogan());
        binding.txtTelefonoNegocioRep.setText(datos.getTelefonoNegocio());
        binding.txtDireccionNegocioRep.setText(datos.getDireccionNegocio());
        //Datos reparacion
        binding.txtFolioRep.setText(datos.getFolio());
        binding.txtFechaRep.setText(datos.getFechaIngreso());
        binding.txtFechaEntregaRep.setText(datos.getFechaPromesa());
        binding.txtEquipoRep.setText(datos.getEquipo());
        binding.txtColorRep.setText(datos.getColor());
        binding.txtFallaRep.setText(datos.getFallaReportada());
        binding.txtBadgeEstadoRep.setText(datos.getEstadoReparacion().getEstado());
        binding.txtMontoTotalRep.setText(datos.getMontoTotal());
        //Datos Clientes
        binding.txtNombreClienteRep.setText(datos.getNombreCliente());
        binding.txtTelefonoClienteRep.setText(datos.getTelefonoCliente());
        //Datos Financieros
        binding.txtAnticipoRep.setText(datos.getAnticipo());
        binding.txtMontoTotalRep.setText(datos.getMontoTotal());
        binding.txtRestanteRep.setText(datos.getRestan());

        return binding.getRoot();
    }
}
