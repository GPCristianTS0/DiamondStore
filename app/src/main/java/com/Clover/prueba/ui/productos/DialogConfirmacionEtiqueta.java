package com.Clover.prueba.ui.productos;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.Clover.prueba.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DialogConfirmacionEtiqueta extends BottomSheetDialogFragment {
    private final Uri imagen;
    public DialogConfirmacionEtiqueta(Uri imagen) {
        this.imagen = imagen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirmacion_etiqueta_productos, container, false);
        ImageView imgPreview = view.findViewById(R.id.CE_imgPreview);
        imgPreview.setImageURI(imagen);
        Button btnAceptar = view.findViewById(R.id.CE_btnAceptar);
        btnAceptar.setOnClickListener(v -> dismiss());
        return view;
    }
}
