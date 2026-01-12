package com.Clover.prueba.ui.productos;

import android.content.Context;

import com.Clover.prueba.data.controller.ControllerProducto;
import com.Clover.prueba.data.dao.ProductoDAO;
import com.Clover.prueba.data.models.Productos;

public class ProductosModel {
    private final ControllerProducto controller;

    public ProductosModel(Context context) {
        controller = new ProductoDAO(context);
    }

    public void actualizarProducto(Productos productoOld, Productos producto){

    }
}
