package com.Clover.prueba.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

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
}
