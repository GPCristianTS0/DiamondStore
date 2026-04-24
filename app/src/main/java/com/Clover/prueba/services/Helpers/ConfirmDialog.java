package com.Clover.prueba.services.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Clover.prueba.R;
import com.google.android.material.button.MaterialButton;

public class ConfirmDialog {
    public interface OnConfirmarClickListener {
        void onConfirmarClick();
    }
    public static void mostrarDialogoConfirmacion(
            Context context,
            String titulo,
            String mensaje,
            String textoBotonConfirmar,
            boolean esAccionPeligrosa,
            OnConfirmarClickListener listener) {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.confirm_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView icono = dialog.findViewById(R.id.CD_imgIcono);
        TextView txtTitulo = dialog.findViewById(R.id.CD_txtTitulo);
        TextView txtMensaje = dialog.findViewById(R.id.CD_txtMensaje);
        MaterialButton btnCancelar = dialog.findViewById(R.id.CD_btnCancelar);
        MaterialButton btnConfirmar = dialog.findViewById(R.id.CD_btnConfirmar);
        txtTitulo.setText(titulo);
        txtMensaje.setText(mensaje);
        btnConfirmar.setText(textoBotonConfirmar);

        if (esAccionPeligrosa) {
            icono.setImageResource(android.R.drawable.ic_dialog_alert);
            icono.setColorFilter(Color.parseColor("#D32F2F")); // Rojo
            btnConfirmar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D32F2F")));
        } else {
            icono.setImageResource(android.R.drawable.ic_dialog_info);
            icono.setColorFilter(Color.parseColor("#1976D2")); // Azul
            btnConfirmar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1976D2")));
        }

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnConfirmar.setOnClickListener(v -> {
            listener.onConfirmarClick();
            dialog.dismiss();
        });
        dialog.show();
    }
}
