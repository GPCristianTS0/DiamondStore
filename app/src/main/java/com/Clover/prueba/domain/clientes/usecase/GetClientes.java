package com.Clover.prueba.domain.clientes.usecase;

import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.models.Clientes;

import java.util.ArrayList;

public class GetClientes {
    private final IClient clientesDAO;
    public GetClientes(IClient clientesDAO) {
        this.clientesDAO = clientesDAO;
    }
    public Clientes execute(String idCliente) {
        return clientesDAO.getClient(idCliente);
    }
}
