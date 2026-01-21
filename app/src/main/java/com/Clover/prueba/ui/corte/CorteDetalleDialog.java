package com.Clover.prueba.ui.corte;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.CorteCajaController;
import com.Clover.prueba.data.models.CorteCaja;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CorteDetalleDialog extends BottomSheetDialogFragment {
    private CorteCajaController corteCajaController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.corte_detalles_view, container, false);
        View rootView = requireActivity().findViewById(android.R.id.content);
        corteCajaController = new CorteCajaController(getContext());
        if (getArguments() != null) {
            CorteCaja corteCaja = (CorteCaja) getArguments().getSerializable("corte");
            rellenarDatos(corteCaja, view);
        }
        return view;
    }
    @SuppressLint("SetTextI18n")
    private void rellenarDatos(CorteCaja corteCaja, View v){
        TextView TC_txtTotalReal = v.findViewById(R.id.DC_txtTotalReal);
        TextView TC_txtDiferencia = v.findViewById(R.id.DC_txtDiferencia);
        TextView TC_txtTotalCalculado = v.findViewById(R.id.DC_txtTotalCalculado);
        TextView TC_txtGastosEfectivo = v.findViewById(R.id.DC_txtGastosEfectivo);
        TextView TC_txtGastosTransf = v.findViewById(R.id.DC_txtGastosTransf);
        TextView TC_txtVentasEfectivo = v.findViewById(R.id.DC_txtVentasEfectivo);
        TextView TC_txtVentasTarjeta = v.findViewById(R.id.DC_txtVentasTarjeta);
        TextView TC_txtAbonos = v.findViewById(R.id.DC_txtAbonos);
        TextView TC_txtFecha = v.findViewById(R.id.DC_txtFecha);
        TextView DC_monto = v.findViewById(R.id.DC_txtMontoInicial);


        CorteCajaController corteCajaController = new CorteCajaController(v.getContext());
        corteCaja = corteCajaController.getCorteCaja(corteCaja.getId_corte());
        if (corteCaja.getDiferencia() < 0) {
            TC_txtDiferencia.setTextColor(Color.parseColor("#D32F2F"));
        }else if (corteCaja.getDiferencia() > 0){
            TC_txtDiferencia.setTextColor(Color.parseColor("#388E3C"));
        }else {
            TC_txtDiferencia.setTextColor(getResources().getColor(R.color.black));
        }
        TC_txtDiferencia.setText("$ "+corteCaja.getDiferencia());
        double montoEsperado = corteCaja.getMonto_inicial() + corteCaja.getVentas_totales() + corteCaja.getAbonos_totales() - corteCaja.getGastos_totales();
        TC_txtTotalReal.setText("$ "+montoEsperado);
        TC_txtTotalCalculado.setText("$ "+corteCaja.getDinero_en_caja());
        TC_txtGastosEfectivo.setText("- $ "+corteCaja.getGastos_efectivo());
        TC_txtVentasEfectivo.setText("$ "+corteCaja.getVentas_efectivo());
        TC_txtVentasTarjeta.setText("$ "+corteCaja.getVentas_tarjeta());
        TC_txtAbonos.setText("$ "+corteCaja.getAbonos_totales());
        TC_txtGastosTransf.setText("- $ "+corteCaja.getGastos_transferencia());
        TC_txtFecha.setText(corteCaja.getFecha_apertura());
        DC_monto.setText("$ "+corteCaja.getMonto_inicial());

        Button btnCerrar = v.findViewById(R.id.DC_btnCerrar);
        btnCerrar.setOnClickListener(v1 -> {
            dismiss();
        });

    }
    @Override
    public int getTheme() {
        return com.google.android.material.R.style.Theme_MaterialComponents_Light_BottomSheetDialog;
    }
}
