package com.Clover.prueba.domain.productos.viewmodel;


import android.net.Uri;

import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.domain.productos.GenerarEtiquetaProductoUseCase;

public class ProductosViewModel {
    private final GenerarEtiquetaProductoUseCase etiquetaProductosUseCase;
    public ProductosViewModel(GenerarEtiquetaProductoUseCase etiquetaProductosUseCase) {
        this.etiquetaProductosUseCase = etiquetaProductosUseCase;
    }

    public Uri generarEtiqueta(Productos producto, Configuracion conf) {
        return etiquetaProductosUseCase.ejecutar(producto, conf);
    }
}
