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
import androidx.lifecycle.ViewModelProvider;

import com.Clover.prueba.R;
import com.Clover.prueba.data.dao.ReparacionesDAO;
import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.databinding.DialogDiagnosticoRefaccionesBinding;
import com.Clover.prueba.ui.reparaciones.viewModel.DetallesReparacionVM;
import com.Clover.prueba.ui.reparaciones.viewModel.ReparacionesFactory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class DialogDiagnosticoRefacciones extends BottomSheetDialogFragment {

    private DetallesReparacionVM viewModel;
    private DialogDiagnosticoRefaccionesBinding binding;
    public static DialogDiagnosticoRefacciones newInstance() {
        return new DialogDiagnosticoRefacciones();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogDiagnosticoRefaccionesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ReparacionesFactory factory = new ReparacionesFactory(view.getContext());
        viewModel = new ViewModelProvider(requireActivity(),factory).get(DetallesReparacionVM.class);
        viewModel.exito.observe(getViewLifecycleOwner(), exito ->{
            if (exito){
                dismiss();
            }
        });

        binding.btnDiagCancelar.setOnClickListener(v -> dismiss());


        binding.btnDiagGuardar.setOnClickListener(v -> {
            String diagnostico = binding.etDiagDiagnostico.getText() != null ? binding.etDiagDiagnostico.getText().toString().trim() : "";
            String costoStr = binding.etDiagCostoRefacciones.getText() != null ? binding.etDiagCostoRefacciones.getText().toString().trim() : "0";
            viewModel.estadoListo( diagnostico, costoStr);
        });
    }

    @Override
    public int getTheme() {
        return com.google.android.material.R.style.Theme_MaterialComponents_Light_BottomSheetDialog;
    }
}