package com.Clover.prueba.domain.productos.usecase;

import android.net.Uri;

import com.Clover.prueba.data.dao.interfaces.IProducto;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.services.storage.StorageImage;

public class UpdateProductUseCase {
    private final IProducto iProducto;
    private final StorageImage storageImage;

    public UpdateProductUseCase(IProducto iProducto, StorageImage storageImage) {
        this.iProducto = iProducto;
        this.storageImage = storageImage;
    }
    public void updateProduct(Productos productoOld, Productos productoNew, Uri uri) {
        if (uri != null) {
            productoNew.setRutaImagen(storageImage.guardarImagenInterno(uri));
        }
        iProducto.updateProducto(productoOld, productoNew);
    }
}
