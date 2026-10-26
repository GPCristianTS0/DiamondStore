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
    private final Context context;

    public StorageImage(Context context) {
        this.context = context;
    }

    @Nullable
    public String guardarImagenInterno(Uri uriImagen) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uriImagen);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            if (bitmap == null) return null;

            int minSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
            int startX = (bitmap.getWidth() - minSize) / 2;
            int startY = (bitmap.getHeight() - minSize) / 2;
            Bitmap squareBitmap = Bitmap.createBitmap(bitmap, startX, startY, minSize, minSize);

            int maxSize = 800;
            Bitmap finalBitmap = squareBitmap;
            if (minSize > maxSize) {
                finalBitmap = Bitmap.createScaledBitmap(squareBitmap, maxSize, maxSize, true);
            }

            File directorio = new File(context.getFilesDir(), "fotos_clover");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            String nombreArchivo = "img_" + System.currentTimeMillis() + ".jpg";
            File archivo = new File(directorio, nombreArchivo);

            FileOutputStream outputStream = new FileOutputStream(archivo);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);

            outputStream.close();
            if (finalBitmap != squareBitmap) {
                finalBitmap.recycle();
            }
            if (squareBitmap != bitmap) {
                squareBitmap.recycle();
            }
            bitmap.recycle();
            inputStream.close();

            return archivo.getAbsolutePath();

        } catch (IOException e) {
            Log.e("Clover_App", "Error al guardar imagen interna: " + e.getMessage());
            return null;
        }
    }

    public Uri guardarImagenTemporal(Bitmap bitmap) {
        try {
            File cache = new File(context.getCacheDir(), "tickets");
            if (!cache.exists()) cache.mkdirs();

            File[] files = cache.listFiles();
            if (files != null) {
                for (File file : files) file.delete();
            }

            File newFile = new File(cache, "ticket.png");
            FileOutputStream stream = new FileOutputStream(newFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            Uri contentUri = FileProvider.getUriForFile(context, "com.Clover.prueba.fileprovider", newFile);

            if (contentUri == null) {
                Log.e("Clover_App", "Error al generar la URI del ticket");
                return null;
            }
            return contentUri;

        } catch (Exception e) {
            Log.e("Clover_App", "Error al compartir ticket: " + e.getMessage());
            return null;
        }
    }

    public Uri guardarImagenExterno(Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "etiqueta_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/DiamondStore");
        values.put(MediaStore.Images.Media.IS_PENDING, 1);

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (uri == null) return null;

        try {
            OutputStream outputStream = contentResolver.openOutputStream(uri);
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
            }

            values.clear();
            values.put(MediaStore.Images.Media.IS_PENDING, 0);
            contentResolver.update(uri, values, null, null);
            return uri;

        } catch (IOException e) {
            Log.e("Clover_App", "Error al guardar imagen externa: " + e.getMessage());
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
            Log.e("Clover_App", "Error al eliminar imagen: " + e.getMessage());
            return false;
        }
    }
}