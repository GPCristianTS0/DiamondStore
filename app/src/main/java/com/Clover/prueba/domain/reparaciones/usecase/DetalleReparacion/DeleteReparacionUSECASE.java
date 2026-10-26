package com.Clover.prueba.domain.reparaciones.usecase.DetalleReparacion;

import com.Clover.prueba.data.dao.interfaces.IReparaciones;

public class DeleteReparacionUSECASE {
    private final IReparaciones dao;

    public DeleteReparacionUSECASE(IReparaciones dao) {
        this.dao = dao;
    }
    public void execute(String folio) throws Exception {
        int folioo;
        if (folio == null || folio.isEmpty()) throw new Exception("Reparacion invalida");
        try{
            folioo = Integer.parseInt(folio);
        }catch (NumberFormatException e){
            throw new Exception("Folio invalido");
        }
        if (folioo<=0) throw new Exception("Folio invalido");
        if (!dao.deleteReparacion(folioo)) throw new Exception("Error al eliminar reparacion");
    }
}
