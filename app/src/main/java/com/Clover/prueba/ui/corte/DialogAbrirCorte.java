package com.Clover.prueba.ui.corte;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.CorteCajaController;
import com.google.android.material.textfield.TextInputEditText;

public class DialogAbrirCorte extends DialogFragment {
    public interface OnMontoConfirmado {
        void onMontoConfirmado();
    }
    private OnMontoConfirmado onMontoConfirmado;
    public void setOnMontoConfirmado(OnMontoConfirmado onMontoConfirmado) {
        this.onMontoConfirmado = onMontoConfirmado;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ventas_inicio_corte, container, false);
        View rootView = requireActivity().findViewById(android.R.id.content);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;

            // Si el teclado está visible (ocupa más del 15% de la pantalla)
            if (keypadHeight > screenHeight * 0.15) {
                // Subir el fragment unos píxeles
                view.animate().translationY((float) -keypadHeight).setDuration(200).start();
            } else {
                // Volver a la posición original
                view.animate().translationY(0).setDuration(200).start();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getActivity() != null){
                    getActivity().finish();
                }
            }
        };
        Button b = view.findViewById(R.id.VCorte_boton);
        TextInputEditText t = view.findViewById(R.id.VCorte_montotxt);
        CorteCajaController corteCajaController = new CorteCajaController(getContext());

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Ingrese monto", Toast.LENGTH_SHORT).show();
                    return;
                }
                double monto = Double.parseDouble(t.getText().toString());
                if (monto <= 0){
                    Toast.makeText(getContext(), "Ingrese monto", Toast.LENGTH_SHORT).show();
                    return;
                }
                corteCajaController.iniciarTurno(monto);
                if (onMontoConfirmado != null) {
                    onMontoConfirmado.onMontoConfirmado();
                }
                callback.setEnabled(false);
                dismiss();
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return view;
    }
}
