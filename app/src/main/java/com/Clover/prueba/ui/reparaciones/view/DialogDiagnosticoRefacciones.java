package com.Clover.prueba.ui.reparaciones.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.Clover.prueba.R;
import com.Clover.prueba.data.dao.ReparacionesDAO;
import com.Clover.prueba.data.models.Reparaciones;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class DialogDiagnosticoRefacciones extends BottomSheetDialogFragment {

    private Reparaciones reparacion;
    private OnDiagnosticoGuardadoListener listener;

    public interface OnDiagnosticoGuardadoListener {
        void onDiagnosticoGuardado();
    }

    public static DialogDiagnosticoRefacciones newInstance(Reparaciones reparacion, OnDiagnosticoGuardadoListener listener) {
        DialogDiagnosticoRefacciones dialog = new DialogDiagnosticoRefacciones();
        dialog.reparacion = reparacion;
        dialog.listener = listener;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_diagnostico_refacciones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText etDiagnostico = view.findViewById(R.id.etDiagDiagnostico);
        TextInputEditText etCostoRefacciones = view.findViewById(R.id.etDiagCostoRefacciones);

        if (reparacion != null) {
            if (reparacion.getDiagnostico() != null) {
                etDiagnostico.setText(reparacion.getDiagnostico());
            }
            if (reparacion.getCosto_refacciones() > 0) {
                etCostoRefacciones.setText(String.valueOf(reparacion.getCosto_refacciones()));
            }
        }

        Button btnCancelar = view.findViewById(R.id.btnDiagCancelar);
        Button btnGuardar = view.findViewById(R.id.btnDiagGuardar);

        if (btnCancelar != null) {
            btnCancelar.setOnClickListener(v -> dismiss());
        }

        if (btnGuardar != null) {
            btnGuardar.setOnClickListener(v -> {
                String diagnostico = etDiagnostico.getText() != null ? etDiagnostico.getText().toString().trim() : "";
                String costoStr = etCostoRefacciones.getText() != null ? etCostoRefacciones.getText().toString().trim() : "0";

                int costoRefacciones = 0;
                if (!TextUtils.isEmpty(costoStr)) {
                    try {
                        costoRefacciones = Integer.parseInt(costoStr);
                    } catch (NumberFormatException ignored) {}
                }

                if (reparacion != null) {
                    reparacion.setDiagnostico(diagnostico);
                    reparacion.setCosto_refacciones(costoRefacciones);
                    reparacion.setEstado("LISTO");

                    ReparacionesDAO dao = new ReparacionesDAO(requireContext());
                    if (dao.updateReparacion(reparacion)) {
                        Toast.makeText(getContext(), "Reparación marcada como LISTO", Toast.LENGTH_SHORT).show();
                        if (listener != null) {
                            listener.onDiagnosticoGuardado();
                        }
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Error al actualizar la reparación", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getTheme() {
        return com.google.android.material.R.style.Theme_MaterialComponents_Light_BottomSheetDialog;
    }
}