package com.Clover.prueba.domain.productos.usecase;

import com.Clover.prueba.data.dao.interfaces.IProducto;
import com.Clover.prueba.data.models.Productos;

public class DeleteProductUseCase {
    private final IProducto iProducto;
    public DeleteProductUseCase(IProducto iProducto) {
        this.iProducto = iProducto;
    }
    public void deleteProduct(Productos producto){
        iProducto.deleteProducto(producto);
    }
}
