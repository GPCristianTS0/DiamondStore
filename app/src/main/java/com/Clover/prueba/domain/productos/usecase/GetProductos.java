package com.Clover.prueba.domain.productos.usecase;

import com.Clover.prueba.data.dao.interfaces.IProducto;
import com.Clover.prueba.data.models.Productos;

import java.util.ArrayList;

public class GetProductos {
    private final IProducto productoDAO;
    public GetProductos(IProducto productoDAO) {
        this.productoDAO = productoDAO;
    }
    public ArrayList<Productos> execute(){
        return productoDAO.getProductos();
    }
}
