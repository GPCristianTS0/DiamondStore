package com.Clover.prueba.domain.reparaciones.usecase.ViewPrincipal;

import com.Clover.prueba.data.dao.interfaces.IReparaciones;
import com.Clover.prueba.domain.reparaciones.constantes.EstadoReparcion;

public class ActualizarEstadoUSECASE {
    private final IReparaciones dao;

    public ActualizarEstadoUSECASE(IReparaciones dao) {
        this.dao = dao;
    }
    public void executeListo(String folio, String diagnostico, String costoRefacciones) throws Exception {
        if (folio == null) throw new Exception("No hay reparacion");
        int costo ;
        try{
            costo = Integer.parseInt(costoRefacciones);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Costo invalido");
        }
        dao.updateListoReparacion(folio, diagnostico, costo);
        execute(folio, EstadoReparcion.LISTO);
    }
    public void execute(String folio, EstadoReparcion estado) throws Exception {
        if (folio==null) throw new Exception("No hay reparacion");
        dao.updateEstateReparacion(folio, estado);
    }
}
