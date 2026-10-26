package com.Clover.prueba.ui.reparaciones.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.databinding.ReparacionDetalleViewBinding;
import com.Clover.prueba.domain.reparaciones.constantes.EstadoReparcion;
import com.Clover.prueba.services.Helpers.ConfirmDialog;
import com.Clover.prueba.ui.reparaciones.DTO.DetalleReparacionStateUI;
import com.Clover.prueba.ui.reparaciones.viewModel.DetallesReparacionVM;
import com.Clover.prueba.ui.reparaciones.viewModel.ReparacionesFactory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

public class ReparacionDetalleDialog extends BottomSheetDialogFragment {

    private DetallesReparacionVM viewModel;
    private ReparacionDetalleViewBinding binding;
    public static ReparacionDetalleDialog newInstance(Reparaciones reparacion) {
        ReparacionDetalleDialog fragment = new ReparacionDetalleDialog();
        Bundle args = new Bundle();
        args.putSerializable("reparacion", reparacion);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ReparacionDetalleViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ReparacionesFactory reparacionesFactory = new ReparacionesFactory(requireContext());
        viewModel = new ViewModelProvider(requireActivity(), reparacionesFactory).get(DetallesReparacionVM.class);

        viewModel.detallesReparacion.observe(getViewLifecycleOwner(), this::mostrarDatosReparacion);
        viewModel.eliminado.observe(getViewLifecycleOwner(), eliminado -> {
            if (Boolean.TRUE.equals(eliminado)) {
                notificarCambioYSalir();
                viewModel.resetEliminado();
            }
        });
        viewModel.exito.observe(getViewLifecycleOwner(), exito ->{
            if (exito) {
                notificarCambioYSalir();
                viewModel.resetExito();
            }
        });
        viewModel.nota.observe(getViewLifecycleOwner(), intent->{
            if (intent!=null) {
                startActivity(intent);
                viewModel.resetIntent();
            }
        });
        setupClickListeners();

        if (getArguments() != null && getArguments().containsKey("reparacion")) {
            Reparaciones rep = (Reparaciones) getArguments().getSerializable("reparacion");
            assert rep != null;
            viewModel.setReparacion(rep.getId_reparacion());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            View bottomSheet = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                com.google.android.material.bottomsheet.BottomSheetBehavior<View> behavior =
                        com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheet);
                behavior.setState(com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
            }
        }
    }


    private void mostrarDatosReparacion(DetalleReparacionStateUI rep) {
        if (rep == null) return;
        int folio = Integer.parseInt(rep.getFolio());
        binding.tvDetalleFolio.setText(String.format(Locale.getDefault(), "Folio #REP-%03d", folio));
        String estado = rep.getEstado() != null ? rep.getEstado().toUpperCase() : "RECIBIDO";
        binding.tvDetalleEstadoBadge.setText(estado);
        aplicarColorEstado(binding.tvDetalleEstadoBadge, estado);

        String equipoYColor = rep.getEquipo() != null ? rep.getEquipo() : "Equipo Sin Especificar";
        if (rep.getColor() != null && !rep.getColor().trim().isEmpty()) {
            equipoYColor += " - " + rep.getColor();
        }
        binding.tvDetalleEquipo.setText(equipoYColor);

        String fallaText = rep.getFallaReportada() != null && !rep.getFallaReportada().isEmpty()
                ? rep.getFallaReportada()
                : "Falla reportada: Sin detalle";
        binding.tvDetalleFalla.setText(fallaText);

        if (rep.getDiagnostico() != null && !rep.getDiagnostico().trim().isEmpty()) {
            binding.tvDetalleDiagnostico.setText((rep.getDiagnostico()));
            binding.tvDetalleDiagnostico.setVisibility(View.VISIBLE);
        } else {
            binding.tvDetalleDiagnostico.setVisibility(View.GONE);
        }
        String fechaIng = rep.getFechaIngreso() != null ? rep.getFechaIngreso() : "No especificada";
        binding.tvDetalleFechaIngreso.setText((fechaIng));

        String fechaEnt = rep.getFechaEntrega() != null && !rep.getFechaEntrega().isEmpty()
                ? rep.getFechaEntrega()
                : "Pendiente";
        binding.tvDetalleFechaEntrega.setText((fechaEnt));

        binding.tvDetalleMontoTotal.setText(String.valueOf(rep.getMontoTotal()));
        binding.tvDetalleAnticipo.setText(String.valueOf(rep.getAnticipo()));
        binding.tvDetalleMontoPendiente.setText(String.valueOf(rep.getMontoPendiente()));
        //Datos Clientes
        String text = rep.getNombreCliente();
        if (text != null) {
            if (rep.getIdCliente() != null && !rep.getIdCliente().trim().isEmpty())
                text+=" ("+rep.getIdCliente()+") ";
        }
        binding.tvDetalleClienteNombre.setText(text);
    }

    private void setupClickListeners(){
        binding.btnDetalleCambiarEstado.setOnClickListener(v -> mostrarDialogoCambiarEstado());

        binding.btnDetalleCompartirWhatsApp.setOnClickListener(v -> {viewModel.compartirTicket(true);});

        binding.btnDetalleCompartirGenerico.setOnClickListener(view -> {viewModel.compartirTicket(false);});

        binding.btnDetalleEliminar.setOnClickListener(v -> confirmarEliminacion());
    }

    private void mostrarDialogoCambiarEstado() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Seleccionar Nuevo Estado")
                .setItems(EstadoReparcion.getArray(), (dialog, which) -> {
                    String nuevoEstado = EstadoReparcion.getArray()[which];
                    if (nuevoEstado.equals(EstadoReparcion.LISTO.getEstado())) {
                        DialogDiagnosticoRefacciones diagDialog = DialogDiagnosticoRefacciones.newInstance();
                        diagDialog.show(getParentFragmentManager(), "DialogDiagnosticoRefacciones");
                    }
                    else viewModel.updateEstado(EstadoReparcion.fromString(nuevoEstado));
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmarEliminacion() {
        ConfirmDialog.mostrarDialogoConfirmacion(requireContext(),
                "Eliminar reparación",
                "¿Estás seguro de que deseas eliminar esta reparación?",
                "Eliminar",
                true,
                ()->viewModel.eliminarReparacion(),
                ()->{});
    }

    private void notificarCambioYSalir() {
        Bundle result = new Bundle();
        result.putBoolean("actualizarLista", true);

        getParentFragmentManager().setFragmentResult("request_reparaciones", result);

        dismiss();
    }

    private void aplicarColorEstado(TextView tvBadge, String estado) {
        switch (estado) {
            case "EN REPARACION":
            case "EN REPARACIÓN":
            case "PROCESO":
                tvBadge.setBackgroundColor(Color.parseColor("#E3F2FD"));
                tvBadge.setTextColor(Color.parseColor("#1976D2"));
                break;
            case "LISTO":
            case "TERMINADO":
                tvBadge.setBackgroundColor(Color.parseColor("#E8F5E9"));
                tvBadge.setTextColor(Color.parseColor("#388E3C"));
                break;
            case "ENTREGADO":
                tvBadge.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvBadge.setTextColor(Color.parseColor("#616161"));
                break;
            case "CANCELADO":
                tvBadge.setBackgroundColor(Color.parseColor("#FFEBEE"));
                tvBadge.setTextColor(Color.parseColor("#D32F2F"));
                break;
            case "RECIBIDO":
            default:
                tvBadge.setBackgroundColor(Color.parseColor("#FFF8E1"));
                tvBadge.setTextColor(Color.parseColor("#FFA000"));
                break;
        }
    }

    @Override
    public int getTheme() {
        return com.google.android.material.R.style.Theme_MaterialComponents_Light_BottomSheetDialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}