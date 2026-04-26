package com.Clover.prueba.domain.productos.usecase;

import com.Clover.prueba.data.dao.interfaces.IProducto;

import java.util.ArrayList;

public class GetSeccionesUseCase {
    private final IProducto productoDAO;
    public GetSeccionesUseCase(IProducto productoDAO) {
        this.productoDAO = productoDAO;
    }
    public ArrayList<String> execute(){
        return productoDAO.getSeccione();
    }
}
