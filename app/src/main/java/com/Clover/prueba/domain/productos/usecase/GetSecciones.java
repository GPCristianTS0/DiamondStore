package com.Clover.prueba.domain.productos.usecase;

import com.Clover.prueba.data.dao.interfaces.IProducto;

import java.util.ArrayList;

public class GetSecciones {
    private final IProducto iProducto;
    public GetSecciones(IProducto iProducto) {
        this.iProducto = iProducto;
    }
    public ArrayList<String> execute() {
        return iProducto.getSeccione();
    }
}
