package com.Clover.prueba.utils;

import android.app.Activity;
import com.google.zxing.integration.android.IntentIntegrator;

public class EscanerCodeBar {
    public void inicializarEscaner(Activity activity) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Apunta al código de barras");
        integrator.setCameraId(0); // 0 = trasera
        integrator.setBeepEnabled(true);
        integrator.setCaptureActivity(CaptureAct.class); // Muy importante
        integrator.initiateScan();
    }
    public void createCodeBar(String codigo) {

    }
}
