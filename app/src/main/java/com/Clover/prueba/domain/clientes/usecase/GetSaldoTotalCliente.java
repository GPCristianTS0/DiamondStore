package com.Clover.prueba.domain.clientes.usecase;

import com.Clover.prueba.data.dao.interfaces.IVentas;

public class GetSaldoTotalCliente {
    private final IVentas ventasDAO;

    public GetSaldoTotalCliente(IVentas ventasDAO) {
        this.ventasDAO = ventasDAO;
    }
    public double execute(String idCliente) {
        return ventasDAO.getSaldoTotal(idCliente);
    }
}
