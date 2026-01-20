package com.Clover.prueba.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
    Context context;
    public ImageUtils(Context context) {
        this.context = context;
    }
    @Nullable
    public String guardarImagen(Uri uriImagen) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uriImagen);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // 🔹 Reducimos tamaño si es muy grande (por ejemplo, 800x800 máx)
            int maxSize = 800;
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            if (width > maxSize || height > maxSize) {
                float ratio = (float) width / height;
                if (ratio > 1) { // Imagen horizontal
                    width = maxSize;
                    height = (int) (width / ratio);
                } else { // Imagen vertical
                    height = maxSize;
                    width = (int) (height * ratio);
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }

            // 🔹 Creamos el archivo en almacenamiento privado
            String nombreArchivo = "img_" + System.currentTimeMillis() + ".jpg";
            File archivo = new File(context.getExternalFilesDir(null), nombreArchivo);


            // 🔹 Escribimos el bitmap comprimido (80% calidad)
            FileOutputStream outputStream = new FileOutputStream(archivo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

            // 🔹 Cerramos flujos
            outputStream.close();
            bitmap.recycle(); // Libera memoria RAM
            inputStream.close();
            outputStream.close();

            // 🔹 Retornar la ruta del archivo guardado
            return archivo.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean eliminarImagen(String rutaImagen) {
        if (rutaImagen == null || rutaImagen.isEmpty()) {
            return false;
        }

        try {
            File archivo = new File(rutaImagen);

            if (archivo.exists()) {
                boolean eliminado = archivo.delete();

                if (eliminado) {
                    Log.d("Clover_App", "Imagen eliminada correctamente: " + rutaImagen);
                } else {
                    Log.e("Clover_App", "No se pudo eliminar el archivo: " + rutaImagen);
                }

                return eliminado;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
