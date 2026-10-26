package com.Clover.prueba.ui.reparaciones.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.Clover.prueba.R;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.ui.reparaciones.viewModel.ReparacionesFactory;
import com.Clover.prueba.services.Helpers.BannerError;
import com.Clover.prueba.ui.reparaciones.viewModel.FormularioViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClienteRapidoDialog extends BottomSheetDialogFragment {

    private OnClienteGuardadoListener listener;
    private FormularioViewModel viewModel;

    public interface OnClienteGuardadoListener {
        void onClienteGuardado();
    }

    public static ClienteRapidoDialog newInstance(OnClienteGuardadoListener listener) {
        ClienteRapidoDialog dialog = new ClienteRapidoDialog();
        dialog.listener = listener;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clientes_formulario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ReparacionesFactory factory = new ReparacionesFactory(view.getContext());
        viewModel = new ViewModelProvider(this, factory).get(FormularioViewModel.class);
        viewModel.exito.observe(this, exito ->{
            if (exito) dismiss();
        });
        viewModel.error.observe(this, error ->{
            BannerError.mostrarError(view, error);
        });
        Button btnGuardar = view.findViewById(R.id.CP_agregarBoton);
        if (btnGuardar != null) {
            btnGuardar.setOnClickListener(v -> guardarCliente(view));
        }
    }

    private void guardarCliente(View view) {
        TextInputEditText etNombre = view.findViewById(R.id.CP_nombreTXT);
        TextInputEditText etApodo = view.findViewById(R.id.CP_apodoTXT);
        TextInputEditText etTelefono = view.findViewById(R.id.CP_telefonoTXT);
        TextInputEditText etDireccion = view.findViewById(R.id.CP_direccionTXT);

        Clientes cliente = new Clientes();
        cliente.setId_cliente(etTelefono.getText().toString());
        cliente.setNombre_cliente(etNombre.getText().toString());
        cliente.setApodo(etApodo.getText().toString());
        cliente.setDireccion(etDireccion.getText().toString());

        viewModel.addClient(cliente);
    }

    @Override
    public int getTheme() {
        return com.google.android.material.R.style.Theme_MaterialComponents_Light_BottomSheetDialog;
    }
}