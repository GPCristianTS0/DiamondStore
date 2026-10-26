package com.Clover.prueba.ui.reparaciones.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.Clover.prueba.R;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.databinding.DialogReparacionRapidaBinding;
import com.Clover.prueba.domain.reparaciones.constantes.TipoReparacion;
import com.Clover.prueba.services.Helpers.BannerError;
import com.Clover.prueba.services.Helpers.ConfirmDialog;
import com.Clover.prueba.ui.reparaciones.viewModel.FormularioViewModel;
import com.Clover.prueba.ui.reparaciones.viewModel.ReparacionesFactory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ReparacionRapidaDialog extends BottomSheetDialogFragment {
    private DialogReparacionRapidaBinding binding;
    private FormularioViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogReparacionRapidaBinding.inflate(inflater,container, false);
        View view = binding.getRoot();

        ReparacionesFactory factory = new ReparacionesFactory(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(FormularioViewModel.class);
        viewModel.listaClientesLiv.observe(getViewLifecycleOwner(), this::cargarClientes);
        viewModel.exito.observe(getViewLifecycleOwner(), exito ->{
            if (exito){
                ConfirmDialog.mostrarDialogoConfirmacion(
                        requireContext(),
                        "Reparación Registrada",
                        "¿Deseas enviar el comprobante de reparación al cliente por WhatsApp?",
                        "Enviar Comprobante",
                        false,
                        () -> {
                            viewModel.generarTicket(true);
                        },
                        this::dismiss
                );
                viewModel.resetExito();
            }
        });
        viewModel.intentTicket.observe(getViewLifecycleOwner(), intent ->{
            if (intent!=null) {
                startActivity(Intent.createChooser(intent, "Compartir Imagen Via...."));
                viewModel.resetIntent();
                dismiss();
            }
        });
        viewModel.error.observe(getViewLifecycleOwner(), mensaje->{
            BannerError.mostrarError(view, mensaje);
        });
        binding.btnGuardarReparacion.setOnClickListener(v -> viewModel.addReparacion(getReparacion()));
        viewModel.getClient();
        return view;
    }

    private void cargarClientes(ArrayList<Clientes> clientes) {
        ArrayAdapter<Clientes> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, clientes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spClientesRep.setAdapter(adapter);

        if (!clientes.isEmpty()) {
            binding.spClientesRep.setSelection(clientes.size() - 1);
        }
    }

    private Reparaciones getReparacion() {
        String equipo = binding.etEquipoRep.getText().toString();
        String color = binding.etColorRep.getText().toString();
        String reparacionRealizada = binding.etReparacionRealizadaRep.getText().toString();
        String costoTotalStr = binding.etCostoTotalRep.getText().toString();

        int costoTotal = 0;
        try {
            if (!costoTotalStr.isEmpty()) costoTotal = Integer.parseInt(costoTotalStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Costo no válido", Toast.LENGTH_SHORT).show();
            return new Reparaciones();
        }

        int selectedPagoId = binding.rgMetodoPagoRep.getCheckedRadioButtonId();
        String tipoPago = "Efectivo";
        if (selectedPagoId == R.id.rbTarjetaRep) {
            tipoPago = "Tarjeta";
        } else if (selectedPagoId == R.id.rbTransferenciaRep) {
            tipoPago = "Transferencia";
        }
        Clientes clientesSeleccionado = (Clientes) binding.spClientesRep.getSelectedItem();
        Reparaciones rep = new Reparaciones();
        rep.setTipo_servicio(TipoReparacion.DOMICILIO.toString());
        rep.setId_cliente(clientesSeleccionado.getId_cliente());
        rep.setEquipo(equipo);
        rep.setColor(color);
        rep.setFalla_reportada(reparacionRealizada);
        rep.setCosto_refacciones(0);
        rep.setMonto_total(costoTotal);
        rep.setAnticipo(costoTotal);
        rep.setTipo_pago(tipoPago);
        viewModel.setClient(clientesSeleccionado);
        return  rep;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public int getTheme() {
        return com.google.android.material.R.style.Theme_MaterialComponents_Light_BottomSheetDialog;
    }
}
