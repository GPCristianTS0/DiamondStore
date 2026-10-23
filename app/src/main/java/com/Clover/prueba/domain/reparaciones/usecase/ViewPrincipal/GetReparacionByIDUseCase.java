package com.Clover.prueba.domain.reparaciones.usecase.ViewPrincipal;

import com.Clover.prueba.data.dao.interfaces.IReparaciones;
import com.Clover.prueba.data.models.Reparaciones;

public class GetReparacionByIDUseCase{
    private final IReparaciones repaDao;

    public GetReparacionByIDUseCase(IReparaciones repaDao) {
        this.repaDao = repaDao;
    }

    public Reparaciones execute(int id) throws Exception {
        Reparaciones repa = repaDao.getReparacionById(id);
        if (repa==null) throw new Exception("Error al obtener la reparacion");
        return repa;
    }
}
