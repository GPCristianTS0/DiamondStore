package com.Clover.prueba.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.Clover.prueba.data.models.Configuracion;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AbrirAppExternas {
    public AbrirAppExternas() {
    }

    public void abrirLlamada(Context context, String numero) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + numero));
        context.startActivity(intent);
    }
    public boolean abrirWhatsapp(Context context, String numero, String mensaje) {
        numero = numero.replace("+", "").replace(" ", "");
        if (numero.length() == 10) {
            numero = "52" + numero;
        }
        try {
            if (mensaje == null)
                mensaje = "";

            String url = "https://api.whatsapp.com/send?phone=" + numero + "&text=" + URLEncoder.encode(mensaje, "UTF-8");

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setPackage("com.whatsapp.w4b");
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            try {
                String url = "https://api.whatsapp.com/send?phone=" + numero + "&text=" + URLEncoder.encode(mensaje, "UTF-8");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));

                intent.setPackage("com.whatsapp");
                context.startActivity(intent);

            } catch (Exception e2) {
                Toast.makeText(null, "No tienes WhatsApp instalado", Toast.LENGTH_SHORT).show();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void compartirImagen(Context context, String mensaje, Uri contentUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_TEXT, mensaje);
        context.startActivity(Intent.createChooser(intent, "Compartir Imagen Via...."));
    }
    public void compartirImagenWhatsapp(Context context, Configuracion configuracion, Uri contentUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        intent.putExtra(Intent.EXTRA_TEXT, configuracion.getMensajeShare());

        // ESTA ES LA CLAVE: Forzamos a que solo use WhatsApp
        intent.setPackage("com.whatsapp");

        // Evitamos que crashee si no tiene WA instalado
        try {
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            // Si tiene WhatsApp Business, el paquete cambia
            try {
                intent.setPackage("com.whatsapp.w4b");
                context.startActivity(intent);
            } catch (android.content.ActivityNotFoundException e2) {
                Toast.makeText(context, "No tienes WhatsApp instalado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
