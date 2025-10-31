package com.Clover.prueba.VentasViews;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.google.android.material.textfield.TextInputEditText;

public class DialogFragmentVentas extends DialogFragment {
    private int total;
    private boolean pago = true;
    private ventaConfirmada ventaConfirmada;

    public interface ventaConfirmada {
        void ventaConfirmada();
    }
    public void setVentaConfirmada(ventaConfirmada ventaConfirmada) {
        this.ventaConfirmada = ventaConfirmada;
    }
    public DialogFragmentVentas(int total) {
        this.total = total;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pago_ventas, container, false);
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

        TextView textViewTotal = view.findViewById(R.id.totalCountPVI);
        TextView textViewCambio = view.findViewById(R.id.cambioCountPVI);
        TextView textViewTotalLabel = view.findViewById(R.id.totalPVIlbl);
        TextView textViewCambioLabel = view.findViewById(R.id.cambioPVIlbl);
        TextView textViewPagaCon = view.findViewById(R.id.pagaConPVIlbl);
        TextInputEditText editTextPagaCon = view.findViewById(R.id.txtPagaConPVI);
        Button btn = view.findViewById(R.id.btnaceptarPVI);

        textViewTotal.setText("$ "+total);
        textViewCambioLabel.setVisibility(INVISIBLE);
        textViewCambio.setVisibility(INVISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pago){
                    String s = editTextPagaCon.getText().toString();
                    if (s.isEmpty()){
                        Toast.makeText(getContext(), "Ingrese monto", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int cantidad = Integer.parseInt(s);
                    if (cantidad<total){
                        Toast.makeText(getContext(), "Monto insuficiente", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    textViewPagaCon.setVisibility(INVISIBLE);
                    editTextPagaCon.setVisibility(INVISIBLE);

                    textViewCambioLabel.setVisibility(VISIBLE);
                    textViewCambio.setVisibility(VISIBLE);
                    int cambio = cantidad - total;
                    textViewCambio.setText("$ "+cambio);
                    pago = false;
                }else{
                    if (ventaConfirmada != null) {
                        ventaConfirmada.ventaConfirmada();
                    }
                    dismiss();
                }
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().setCanceledOnTouchOutside(true);
        }
    }
}
