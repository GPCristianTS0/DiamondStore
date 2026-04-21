package com.Clover.prueba.services.generators;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Clover.prueba.R;
import com.Clover.prueba.data.dao.ConfiguracionDAO;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.services.sharing.ShareManager;
import com.Clover.prueba.services.storage.StorageImage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class GeneradorQR {
    private final Context context;
    public GeneradorQR(Context context) {
        this.context = context;
    }
    public Bitmap generarQR(String texto) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, 700, 700, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            Bitmap qrbitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrbitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return combinarImagenes(qrbitmap);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }
    private Bitmap combinarImagenes(Bitmap qr) {
        int ancho = qr.getWidth();
        int alto = qr.getHeight();

        Bitmap bitCombinado = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitCombinado);
        canvas.drawBitmap(qr, 0, 0, null);

        float escala = 0.12f;

        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logotipo_diamondsystems);

        int anchoLogo = (int) (logoBitmap.getWidth() * escala);
        int altoLogo = logoBitmap.getHeight();

        float escalaLogo = (float) anchoLogo / logoBitmap.getWidth();

        Matrix matrix = new Matrix();
        matrix.postScale(escalaLogo, escalaLogo);

        float logoNuevoAncho = logoBitmap.getWidth() * escalaLogo;
        float logoNuevoAlto = altoLogo * escalaLogo;

        float x = (ancho - logoNuevoAncho) / 2;
        float y = (alto - logoNuevoAlto)     / 2;

        matrix.postTranslate(x,y);

        Paint paintBlanco = new Paint();
        paintBlanco.setColor(Color.WHITE);
        paintBlanco.setStyle(Paint.Style.FILL);

        // Dibujamos un cuadrado blanco un poquito más grande que el logo
        canvas.drawRect(x - 5, y - 5,
                x + logoNuevoAncho + 5, y + logoNuevoAlto + 5,
                paintBlanco);
        canvas.drawBitmap(logoBitmap, matrix, null);

        return bitCombinado;
    }
}
