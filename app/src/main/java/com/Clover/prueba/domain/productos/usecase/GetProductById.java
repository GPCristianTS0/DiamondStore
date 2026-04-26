package com.Clover.prueba.domain.productos.usecase;

import com.Clover.prueba.data.dao.interfaces.IProducto;
import com.Clover.prueba.data.models.Productos;

public class GetProductById {
    private final IProducto productoDAO;
    public GetProductById(IProducto productoDAO) {
        this.productoDAO = productoDAO;
    }
    public Productos execute(String id){
        return productoDAO.getProductoCode(id);
    }
}
