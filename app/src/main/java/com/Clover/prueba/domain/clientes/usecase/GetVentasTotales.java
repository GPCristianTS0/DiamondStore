package com.Clover.prueba.domain.clientes.usecase;

import com.Clover.prueba.data.dao.interfaces.IVentas;

public class GetVentasTotales {
    private final IVentas ventasDAO;
    public GetVentasTotales(IVentas ventasDAO) {
        this.ventasDAO = ventasDAO;
    }
    public int execute(String idCliente) {
        return ventasDAO.getVentasTotales(idCliente);
    }
}
