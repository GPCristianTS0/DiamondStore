package com.Clover.prueba.services.generators;

import android.content.Context;
import android.content.res.Resources;
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
    public Bitmap getImageBitmap(View view, int altoMinimo) {
        //Medicion de la pantalla
        int widthSpec = View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);
        int measureWidth = view.getMeasuredWidth();
        int altoDelContenido = view.getMeasuredHeight();
        int altoFinal = Math.max(altoMinimo,altoDelContenido);

        view.layout(0, 0, measureWidth, altoFinal);

        float escala = 1.0f;

        int anchoReal = (int) (measureWidth * escala);
        int altoReal = (int) (altoFinal * escala);

        Bitmap bitmap = Bitmap.createBitmap(anchoReal, altoReal, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(android.graphics.Color.parseColor("#000000"));
        canvas.scale(escala, escala);
        view.draw(canvas);
        return bitmap;
    }
}
