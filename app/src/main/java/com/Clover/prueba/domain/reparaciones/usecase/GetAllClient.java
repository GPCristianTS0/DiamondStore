package com.Clover.prueba.domain.reparaciones.usecase;

import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.models.Clientes;

import java.util.ArrayList;

public class GetAllClient {
    private final IClient dao;

    public GetAllClient(IClient dao) {
        this.dao = dao;
    }

    public ArrayList<Clientes> execute(){
        return dao.getClients();
    }
}
