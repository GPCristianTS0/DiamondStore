package com.Clover.prueba.domain.reparaciones.usecase;

import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.models.Clientes;

public class GetClienteUseCase {
    private final IClient dao;

    public GetClienteUseCase(IClient dao) {
        this.dao = dao;
    }
    public Clientes execute(String idCliente) throws Exception {
        if (idCliente== null||idCliente.isEmpty()) throw new Exception("Ingrese un valor valido");
        Clientes c = dao.getClient(idCliente);
        if (c == null) throw new Exception("Error al conseguir cliente");
        return c;
    }
}
