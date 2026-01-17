package com.Clover.prueba.ui.gastos;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.GastosController;
import com.Clover.prueba.data.dao.GastosDAO;
import com.Clover.prueba.data.dao.interfaces.IGastos;
import com.Clover.prueba.data.models.Gastos;

import java.util.ArrayList;

public class GastosDialog extends DialogFragment {
    private GastosController controllerGastos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gastos_view, container, false);
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
        controllerGastos = new GastosController(getContext());
        controllerGastos.prepararDatosSpinner();
        rellenarTabla(controllerGastos.getGastos("", ""), view);
        return view;
    }
    private void rellenarTabla(ArrayList<Gastos> gastos, View v){
        GastosViewAdapter adapter;
        RecyclerView recyclerView = v.findViewById(R.id.rvHistorialGastos);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapter = new GastosViewAdapter(gastos, new GastosViewAdapter.OnItemClickListener() {
            @Override
            public void OnClickGastos(Gastos gasto, int position) {
                mostrarTicket(gasto);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    @SuppressLint("DefaultLocale")
    private void mostrarTicket(Gastos gasto){
        android.view.View view = getLayoutInflater().inflate(R.layout.gastos_ticket, null);
        TextView monto = view.findViewById(R.id.G_ticketMontoDetalle);
        TextView valFecha = view.findViewById(R.id.G_ticketFecha);
        TextView valMetodo = view.findViewById(R.id.G_ticketMetodo);
        TextView valProveedor = view.findViewById(R.id.G_ticketProveedor);
        TextView valDesc = view.findViewById(R.id.G_ticketDesc);
        Button btnEliminar = view.findViewById(R.id.G_ticketBtnEliminarGasto);
        monto.setText(String.format("$%.2f", gasto.getMonto()));
        valFecha.setText(gasto.getFecha_hora());
        ImageView icon = view.findViewById(R.id.imgIconoDetalle);
        if ("Efectivo".equals(gasto.getMetodo_pago())) {
            icon.setColorFilter(android.graphics.Color.parseColor("#FF5722")); // Naranja
        } else {
            icon.setColorFilter(android.graphics.Color.parseColor("#2196F3")); // Azul
        }
        valMetodo.setText(gasto.getMetodo_pago());
        valProveedor.setText(controllerGastos.getNombresProveedorById(gasto.getId_proveedor()));
        valDesc.setText(gasto.getDescripcion());

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setView(view);
        final android.app.AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        btnEliminar.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(getContext())
                    .setTitle("¿Borrar definitivamente?")
                    .setPositiveButton("Sí", (d, w) -> {
                        if (controllerGastos.deleteGasto(gasto.getId_gasto())){
                            rellenarTabla(controllerGastos.getGastos("",""), getView());
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Gasto eliminado", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getContext(), "Error al eliminar gasto", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
        dialog.show();
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().setCanceledOnTouchOutside(true);

            int altoPantalla = getResources().getDisplayMetrics().heightPixels;
            int altoDeseado = (int) (altoPantalla * 0.80); // <--- Aquí está el truco

            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    altoDeseado
            );
        }
    }
}
