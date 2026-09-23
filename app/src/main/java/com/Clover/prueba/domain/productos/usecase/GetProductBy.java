package com.Clover.prueba.domain.productos.usecase;

import com.Clover.prueba.data.dao.interfaces.IProducto;
import com.Clover.prueba.data.models.Productos;

import java.util.ArrayList;

public class GetProductBy {
    private final IProducto productoDAO;
    public GetProductBy(IProducto productoDAO) {
        this.productoDAO = productoDAO;
    }
    public ArrayList<Productos> execute(int seccion, String columnaObtencion, String busqueda){
        return productoDAO.buscarProductosPor(seccion, columnaObtencion, busqueda);
    }
}
