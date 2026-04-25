package com.Clover.prueba.domain.clientes.usecase;

import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.models.Clientes;

import java.util.ArrayList;

public class GetClientsSearch {
    private final IClient clientesDAO;
    public GetClientsSearch(IClient clientesDAO) {
        this.clientesDAO = clientesDAO;
    }
    public ArrayList<Clientes> execute(String filtro, String valor, boolean deudor) {
        return clientesDAO.getClient(filtro, valor, deudor);
    }
}
