package com.Clover.prueba.domain.reparaciones.usecase.ViewPrincipal;

import com.Clover.prueba.data.models.Reparaciones;

import java.util.ArrayList;

public class FiltrarListaUseCase {
    public ArrayList<Reparaciones> execute(ArrayList<Reparaciones> listaCompleta, String query, String estadoSel, String servicioSel){
        ArrayList<Reparaciones> listaFiltrada = new ArrayList<>();
        for (Reparaciones rep : listaCompleta) {
            boolean coincideQuery = query.isEmpty()
                    || (rep.getEquipo() != null && rep.getEquipo().toLowerCase().contains(query))
                    || (rep.getId_cliente() != null && rep.getId_cliente().toLowerCase().contains(query))
                    || (rep.getFalla_reportada() != null && rep.getFalla_reportada().toLowerCase().contains(query))
                    || String.valueOf(rep.getId_reparacion()).contains(query);

            boolean coincideEstado = estadoSel.equals("Todos los estados")
                    || (rep.getEstado() != null && rep.getEstado().equalsIgnoreCase(estadoSel));

            boolean coincideServicio = servicioSel.equals("Todos los servicios")
                    || (rep.getTipo_servicio() != null && rep.getTipo_servicio().equalsIgnoreCase(servicioSel));

            if (coincideQuery && coincideEstado && coincideServicio) {
                listaFiltrada.add(rep);
            }
        }
        return listaFiltrada;
    }
}
