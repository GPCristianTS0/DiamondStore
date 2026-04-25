package com.Clover.prueba.domain.clientes.usecase;

import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.models.Clientes;

import java.util.ArrayList;

public class GetClientsList {
    private final IClient clientesDAO;
    public GetClientsList(IClient clientesDAO) {
        this.clientesDAO = clientesDAO;
    }
    public ArrayList<Clientes> execute() {
        return clientesDAO.getClients();
    }
}
