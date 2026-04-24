package com.Clover.prueba.domain.productos.generators;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.services.generators.ImageGenerator;
import com.Clover.prueba.services.generators.LabelProductGenerator;
import com.Clover.prueba.services.storage.StorageImage;

public class GenerarEtiquetaProductoUseCase {
    private final ImageGenerator imageGenerator;
    private final StorageImage storageImage;
    private final LabelProductGenerator labelProductGenerator;
    public GenerarEtiquetaProductoUseCase(StorageImage storageImage, ImageGenerator imageGenerator, LabelProductGenerator labelProductGenerator) {
        this.imageGenerator = imageGenerator;
        this.storageImage = storageImage;
        this.labelProductGenerator = labelProductGenerator;
    }
    public Uri ejecutar(Productos producto, Configuracion conf) {
        View v = labelProductGenerator.getView(producto, conf);
        Bitmap bitmap = imageGenerator.getImageBitmap(v, 0);
        Uri uri = storageImage.guardarImagenExterno(bitmap);
        if (uri == null) {
            throw new RuntimeException("No se pudo guardar la imagen");
        }
        return uri;
    }
}
