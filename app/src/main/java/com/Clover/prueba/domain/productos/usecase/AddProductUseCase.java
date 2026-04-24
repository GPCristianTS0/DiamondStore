package com.Clover.prueba.domain.productos.usecase;

import android.net.Uri;
import android.widget.Toast;

import com.Clover.prueba.data.dao.interfaces.IProducto;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.services.storage.StorageImage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddProductUseCase {

    private final IProducto iProducto;
    private final StorageImage storageImage;

    public AddProductUseCase(IProducto iProducto, StorageImage storageImage) {
        this.iProducto = iProducto;
        this.storageImage = storageImage;
    }
    public void addProduct(Productos producto, Uri uri){
        Productos productoe = iProducto.getProductoCode(producto.getId());
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        producto.setUltimoPedido(fechaHoy);

        if (productoe.getId() != null){
            throw new RuntimeException("El codigo ya esta registrado");
        }
        if(producto.getSeccion().equals("Seleccionar")) {
            throw new RuntimeException("Seleccione una seccion");
        }
        if (uri != null) {
            producto.setRutaImagen(storageImage.guardarImagenInterno(uri));
        }
        iProducto.addProducto(producto);
    }
}
