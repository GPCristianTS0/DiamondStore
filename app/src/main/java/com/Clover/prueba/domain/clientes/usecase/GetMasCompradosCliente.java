package com.Clover.prueba.domain.clientes.usecase;

import com.Clover.prueba.data.dao.interfaces.IVentas;
import com.Clover.prueba.data.dto.ProductoMasCompradoDTO;

import java.util.ArrayList;

public class GetMasCompradosCliente {
    private final IVentas ventasDAO;
    public GetMasCompradosCliente(IVentas ventasDAO) {
        this.ventasDAO = ventasDAO;
    }
    public ArrayList<ProductoMasCompradoDTO> execute(String idCliente, int noProductos) {
        return ventasDAO.getMasCompradobyCliente(idCliente, noProductos);
    }
}
