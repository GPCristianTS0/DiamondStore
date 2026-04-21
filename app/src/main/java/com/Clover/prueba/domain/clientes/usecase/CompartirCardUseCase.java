package com.Clover.prueba.domain.clientes.usecase;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.domain.clientes.generators.CardClientGenerator;
import com.Clover.prueba.services.generators.GeneradorQR;
import com.Clover.prueba.services.generators.ImageGenerator;
import com.Clover.prueba.services.sharing.ShareManager;
import com.Clover.prueba.services.storage.StorageImage;

public class CompartirCardUseCase {
    private final Context context;
    private final GeneradorQR qr;
    public CompartirCardUseCase(Context context, GeneradorQR qr) {
        this.context = context;
        this.qr = qr;
    }
    public boolean execute(Clientes cliente) {
        //Genera y guarda el QR
        Bitmap bitmap = qr.generarQR(cliente.getId_cliente());
        //Genera el view con el qr y los datos del cliente
        CardClientGenerator cardClientGenerator = new CardClientGenerator();
        View viewCard = cardClientGenerator.generarCardCliente(context, cliente, bitmap);
        //Genera y guarda la imagen
        StorageImage storageImage = new StorageImage(context);
        ImageGenerator imageGenerator = new ImageGenerator();
        Bitmap bitmapa = imageGenerator.getImageBitmap(viewCard, 0);
        Uri rutaImagen = storageImage.guardarImagenTemporal(bitmapa);
        //Compartir imagen
        ShareManager shareManager = new ShareManager();
        shareManager.compartirImagen(context, "Gracias por ser parte de nosotros", rutaImagen);
        return false;
    }
}
