package com.Clover.prueba.domain.clientes.usecase;

import com.Clover.prueba.data.dao.interfaces.IAbonos;
import com.Clover.prueba.data.models.Abonos;

import java.util.ArrayList;

public class GetAbonosClientes {
    private final IAbonos abonosDAO;

    public GetAbonosClientes(IAbonos abonosDAO) {
        this.abonosDAO = abonosDAO;
    }
    public ArrayList<Abonos> execute(String id_cliente) {
        return abonosDAO.getAbonos(id_cliente);
    }
}
