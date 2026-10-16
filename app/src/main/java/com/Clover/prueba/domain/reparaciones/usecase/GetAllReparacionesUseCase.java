package com.Clover.prueba.domain.reparaciones.usecase;

import com.Clover.prueba.data.dao.interfaces.IReparaciones;
import com.Clover.prueba.data.models.Reparaciones;

import java.util.ArrayList;

public class GetAllReparacionesUseCase {
    private final IReparaciones repaDAO;

    public GetAllReparacionesUseCase(IReparaciones repaDAO) {
        this.repaDAO = repaDAO;
    }
    public ArrayList<Reparaciones> execute(){
        return repaDAO.getAllReparaciones();
    }
}
