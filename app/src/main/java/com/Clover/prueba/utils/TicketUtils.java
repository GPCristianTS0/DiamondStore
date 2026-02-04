package com.Clover.prueba.utils;

import static com.Clover.prueba.utils.Constantes.CONST_METODO_CREDITO;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_EFECTIVO;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_TARJETA;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_TRANSFERENCIA;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Clover.prueba.R;
import com.Clover.prueba.data.dao.ConfiguracionDAO;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.DetalleVenta;
import com.Clover.prueba.data.models.Ventas;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class TicketUtils {

    public void generarYCompartirTicket(Context context,String nombreCliente, Ventas venta, ArrayList<DetalleVenta>listaProductos) {

        //Medicion de la pantalla
        int widthSpec = View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY); // Ancho fijo
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED); // Alto automático
        view.measure(widthSpec, heightSpec);
        int altoDelContenido = view.getMeasuredHeight();

        //Definimos tu altura mínima deseada (1920px)
        int altoMinimo = 2040;

        //ELEGIMOS EL MAYOR: Si el contenido es chico, usamos 1920. Si es grande, usamos el contenido.
        int altoFinal = Math.max(altoDelContenido, altoMinimo);


        view.layout(0, 0, view.getMeasuredWidth(), altoFinal);

        // 5. DIBUJAR (DRAW): Creamos el Bitmap y le decimos a la vista "dibújate aquí"
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), altoFinal, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // Pintamos fondo blanco por si acaso
        canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        // 6. COMPARTIR: Guardamos temporalmente y lanzamos WhatsApp
        compartirBitmap(context, bitmap, configuracion);
    }

    private static void compartirBitmap(Context context, Bitmap bitmap, Configuracion configuracion) {
        try {
            // Guardamos la imagen en la galería temporal o caché para obtener una URI
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);

            // NOTA: Esto guarda en galería. Para producción profesional se usa FileProvider,
            // pero esto funciona rápido para empezar.
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Ticket_" + System.currentTimeMillis(), null);
            Uri imageUri = Uri.parse(path);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.putExtra(Intent.EXTRA_TEXT, configuracion.getMensajeShare().toString());

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
        } catch (Exception e) {
            Toast.makeText(context, "Error al generar ticket: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
