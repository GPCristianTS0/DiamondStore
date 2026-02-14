package com.Clover.prueba.services.generators;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.Clover.prueba.data.models.Configuracion;

public class ImageGenerator {
    public ImageGenerator() {
    }
    private Bitmap creacionImagen(View view, int altoMinimo) {

        //Medicion de la pantalla
        int widthSpec = View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY); // Ancho fijo
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED); // Alto automático
        view.measure(widthSpec, heightSpec);
        int altoDelContenido = view.getMeasuredHeight();
        int altoFinal;
        if (altoMinimo==0) {
            altoFinal = Math.max(altoDelContenido, altoMinimo);
        }else
            altoFinal = altoDelContenido;

        view.layout(0, 0, view.getMeasuredWidth(), altoFinal);

        // 5. DIBUJAR (DRAW): Creamos el Bitmap y le decimos a la vista "dibújate aquí"
        float escala = 2.0f;

        int anchoReal = (int) (view.getMeasuredWidth() * escala);
        int altoReal = (int) (altoFinal * escala);

        Bitmap bitmap = Bitmap.createBitmap(anchoReal, altoReal, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.scale(escala, escala);
        view.draw(canvas);
        return bitmap;
    }
}
