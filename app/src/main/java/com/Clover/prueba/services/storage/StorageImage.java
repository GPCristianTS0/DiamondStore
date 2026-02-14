package com.Clover.prueba.services.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StorageImage {
    Context context;
    public StorageImage(Context context) {
        this.context = context;
    }
    @Nullable
    public String guardarImagenInterno(Uri uriImagen) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uriImagen);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            int maxSize = 800;
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            if (width > maxSize || height > maxSize) {
                float ratio = (float) width / height;
                if (ratio > 1) {
                    width = maxSize;
                    height = (int) (width / ratio);
                } else {
                    height = maxSize;
                    width = (int) (height * ratio);
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }

            String nombreArchivo = "img_" + System.currentTimeMillis() + ".jpg";
            File archivo = new File(context.getExternalFilesDir(null), nombreArchivo);


            FileOutputStream outputStream = new FileOutputStream(archivo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

            outputStream.close();
            bitmap.recycle();
            inputStream.close();

            return archivo.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Uri guardarImgaenTemporal(Context context, Bitmap bitmap) {
        try {
            //Busca la carpeta y si no existe la crea
            File cache = new File(context.getCacheDir(), "tickets");
            if (!cache.exists()) cache.mkdirs();

            //Se borrar tickets Antiguos
            File[] files = cache.listFiles();
            if (files != null)
                for (File file : files) file.delete();

            //Se crea una archivo temporal para guardar el ticket
            File newFile = new File(cache, "ticket.png");
            FileOutputStream stream = new FileOutputStream(newFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            //Se genera la Uri
            Uri contentUri = FileProvider.getUriForFile(context, "com.Clover.prueba.fileprovider", newFile);

            if (contentUri == null) {
                Log.e("Clover_App", "Error al generar la URI");
                return null;
            }
            return contentUri;
        } catch (Exception e) {
            Log.e("Clover_App", "Error al compartir ticket"+e.getMessage());
            return null;
        }
    }
    public Uri guardarImagenExterno(Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "etiqueta_"+System.currentTimeMillis()+".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+"/DiamondStore");
        values.put(MediaStore.Images.Media.IS_PENDING, 1);

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream outputStream = contentResolver.openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.close();

            values.clear();
            values.put(MediaStore.Images.Media.IS_PENDING, 0);
            contentResolver.update(uri, values, null, null);
            return uri;
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
