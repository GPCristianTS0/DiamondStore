package com.Clover.prueba.domain.reparaciones.usecase.ViewPrincipal;

import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.domain.reparaciones.DTO.ResultadoContadores;

import java.util.ArrayList;

public class CalcularContadoresUseCase {
    public ResultadoContadores execute(ArrayList<Reparaciones> listaCompleta) throws Exception {
        if (listaCompleta==null) throw new Exception("Lista Vacia");
        int pendientes = 0;
        int enProceso = 0;
        int listos = 0;
        for (Reparaciones r : listaCompleta) {
            String est = r.getEstado() != null ? r.getEstado().toUpperCase() : "";
            if (est.contains("RECIBIDO") || est.contains("PENDIENTE")) {
                pendientes++;
            } else if (est.contains("REPARACION") || est.contains("PROCESO")) {
                enProceso++;
            } else if (est.contains("LISTO") || est.contains("TERMINADO")) {
                listos++;
            }
        }
        return new ResultadoContadores(pendientes,enProceso,listos);
    }
}
