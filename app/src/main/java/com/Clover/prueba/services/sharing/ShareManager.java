package com.Clover.prueba.services.sharing;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.Clover.prueba.data.models.Configuracion;

import java.net.URLEncoder;

public class ShareManager {
    private final Context context;
    public ShareManager(Context context) {
        this.context = context;
    }

    public Intent abrirLlamada(String numero) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + numero));
        return intent;
    }
    public Intent abrirWhatsapp(String numero, String mensaje) {
        numero = numero.replace("+", "").replace(" ", "");
        if (numero.length() == 10) {
            numero = "52" + numero;
        }
        if (mensaje == null)
            mensaje = "";
        String url;
        try{
            url = "https://api.whatsapp.com/send?phone=" + numero + "&text=" + URLEncoder.encode(mensaje, "UTF-8");
        }catch (Exception e){
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (isPackageInstalled("com.whatsapp.w4b"))  intent.setPackage("com.whatsapp.w4b");
        else if (isPackageInstalled("com.whatsapp")) intent.setPackage("com.whatsapp");
        else return null;
        return intent;
    }
    public Intent compartirImagen( String mensaje, Uri contentUri, boolean whatsapp) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_TEXT, mensaje);

        if (!whatsapp) return intent;

        if (isPackageInstalled("com.whatsapp.w4b"))
            intent.setPackage("com.whatsapp.w4b");
        else if (isPackageInstalled("com.whatsapp"))
            intent.setPackage("com.whatsapp");
        else
            return null;
        return intent;
    }
    private boolean isPackageInstalled(String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
