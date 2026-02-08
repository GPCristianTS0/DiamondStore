package com.Clover.prueba.ui.clientes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.ControllerCredito;
import com.Clover.prueba.data.models.Abonos;
import com.Clover.prueba.utils.FormatterFechas;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DetallesAbono extends BottomSheetDialogFragment {
    private ControllerCredito controllerCredito;
    private int abonoo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clientes_detalle_abono, container, false);
        TextView txtFecha = view.findViewById(R.id.CDA_txtFecha);
        TextView txtMonto = view.findViewById(R.id.CDA_txtMontoAbono);
        TextView txtFolio = view.findViewById(R.id.CDA_txtFolio);
        TextView txtTipoPago = view.findViewById(R.id.CDA_txtMetodo);
        TextView txtSaldoRestante = view.findViewById(R.id.CDA_txtSaldoRestante);
        TextView txtSaldoAnterior = view.findViewById(R.id.CDA_txtSaldoAnterior);
        Button btnEliminar = view.findViewById(R.id.CDA_btnCancelarAbono);
        Button btnCompartir = view.findViewById(R.id.CDA_btnCompartirAbono);
        Abonos abono;
        controllerCredito = new ControllerCredito(getContext());
        if (getArguments()!=null) {
            String importes = "$ ";
            abono = getArguments().getSerializable("abono", Abonos.class);
            txtFolio.setText(String.valueOf("#A-"+abono.getId()));
            String fecha = FormatterFechas.formatDate(abono.getFecha(), "dd MMMM yyyy HH:mm", false);
            txtFecha.setText(fecha);
            txtMonto.setText(importes.concat(abono.getMonto() + ""));
            txtTipoPago.setText(abono.getTipoPago());
            txtSaldoRestante.setText(importes.concat(abono.getSaldoActual() + ""));
            txtSaldoAnterior.setText(importes.concat(abono.getSaldoAnterior() + ""));

            btnEliminar.setOnClickListener(v -> {
                if (controllerCredito.deleteAbono(abono.getId())) {
                    dismiss();
                }
            });
            btnCompartir.setOnClickListener(v -> {
                controllerCredito.generarTicketAbono(getContext(), "", true, abono);
            });
        }
        return view;
    }
}
