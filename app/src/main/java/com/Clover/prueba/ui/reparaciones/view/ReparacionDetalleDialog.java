package com.Clover.prueba.ui.reparaciones.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.Clover.prueba.R;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.ui.reparaciones.viewModel.ReparacionesFactory;
import com.Clover.prueba.ui.reparaciones.viewModel.ReparacionesViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

public class ReparacionDetalleDialog extends BottomSheetDialogFragment {

    private ReparacionesViewModel viewModel;

    private TextView tvFolio, tvEstadoBadge, tvClienteNombre, tvEquipo, tvFalla, tvDiagnostico;
    private TextView tvFechaIngreso, tvFechaEntrega, tvMontoTotal, tvAnticipo, tvMontoPendiente;
    private Button btnCambiarEstado, btnCompartirTicket, btnEliminar;

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
        View view = inflater.inflate(R.layout.reparacion_detalle_view, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        tvFolio = v.findViewById(R.id.tvDetalleFolio);
        tvEstadoBadge = v.findViewById(R.id.tvDetalleEstadoBadge);
        tvClienteNombre = v.findViewById(R.id.tvDetalleClienteNombre);
        tvEquipo = v.findViewById(R.id.tvDetalleEquipo);
        tvFalla = v.findViewById(R.id.tvDetalleFalla);
        tvDiagnostico = v.findViewById(R.id.tvDetalleDiagnostico);
        tvFechaIngreso = v.findViewById(R.id.tvDetalleFechaIngreso);
        tvFechaEntrega = v.findViewById(R.id.tvDetalleFechaEntrega);
        tvMontoTotal = v.findViewById(R.id.tvDetalleMontoTotal);
        tvAnticipo = v.findViewById(R.id.tvDetalleAnticipo);
        tvMontoPendiente = v.findViewById(R.id.tvDetalleMontoPendiente);

        btnCambiarEstado = v.findViewById(R.id.btnDetalleCambiarEstado);
        btnCompartirTicket = v.findViewById(R.id.btnDetalleCompartirTicket);
        btnEliminar = v.findViewById(R.id.btnDetalleEliminar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ReparacionesFactory reparacionesFactory = new ReparacionesFactory(requireContext());
        viewModel = new ViewModelProvider(this, reparacionesFactory).get(ReparacionesViewModel.class);

        setupObservers();
        setupClickListeners();

        if (getArguments() != null && getArguments().containsKey("reparacion")) {
            Reparaciones rep = (Reparaciones) getArguments().getSerializable("reparacion");
            viewModel.setReparacion(rep);
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

    private void setupObservers() {
        viewModel.reparacion.observe(getViewLifecycleOwner(), this::mostrarDatosReparacion);

        viewModel.cliente.observe(getViewLifecycleOwner(), this::mostrarDatosCliente);

        viewModel.mensaje.observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.eliminado.observe(getViewLifecycleOwner(), eliminado -> {
            if (Boolean.TRUE.equals(eliminado)) {
                notificarCambioYSalir();
            }
        });
    }

    private void mostrarDatosReparacion(Reparaciones rep) {
        if (rep == null) return;

        tvFolio.setText(String.format(Locale.getDefault(), "Folio #REP-%03d", rep.getId_reparacion()));

        String estado = rep.getEstado() != null ? rep.getEstado().toUpperCase() : "RECIBIDO";
        tvEstadoBadge.setText(estado);
        aplicarColorEstado(tvEstadoBadge, estado);

        String equipoYColor = rep.getEquipo() != null ? rep.getEquipo() : "Equipo Sin Especificar";
        if (rep.getColor() != null && !rep.getColor().trim().isEmpty()) {
            equipoYColor += " - " + rep.getColor();
        }
        tvEquipo.setText(equipoYColor);

        String fallaText = rep.getFalla_reportada() != null && !rep.getFalla_reportada().isEmpty()
                ? "Falla reportada: " + rep.getFalla_reportada()
                : "Falla reportada: Sin detalle";
        tvFalla.setText(fallaText);

        if (rep.getDiagnostico() != null && !rep.getDiagnostico().trim().isEmpty()) {
            tvDiagnostico.setText("Diagnóstico: " + rep.getDiagnostico());
            tvDiagnostico.setVisibility(View.VISIBLE);
        } else {
            tvDiagnostico.setVisibility(View.GONE);
        }

        String fechaIng = rep.getFecha_ingreso() != null ? rep.getFecha_ingreso() : "No especificada";
        tvFechaIngreso.setText("Ingreso: " + fechaIng);

        String fechaEnt = rep.getFecha_entrega() != null && !rep.getFecha_entrega().isEmpty()
                ? rep.getFecha_entrega()
                : "Pendiente";
        tvFechaEntrega.setText("Entrega: " + fechaEnt);

        tvMontoTotal.setText(String.valueOf(rep.getMonto_total()));
        tvAnticipo.setText(String.valueOf(rep.getAnticipo()));
        tvMontoPendiente.setText(String.valueOf(rep.getMonto_pendiente()));
    }

    private void mostrarDatosCliente(Clientes cliente) {
        if (cliente != null) {
            String nombreText = cliente.getNombre_cliente();
            if (cliente.getApodo() != null && !cliente.getApodo().trim().isEmpty()) {
                nombreText += " (" + cliente.getApodo() + ")";
            }
            tvClienteNombre.setText(nombreText);
        }
    }

    private void setupClickListeners() {
        btnCambiarEstado.setOnClickListener(v -> mostrarDialogoCambiarEstado());

        //btnCompartirTicket.setOnClickListener(v -> viewModel.compartirComprobante(requireContext()));

        btnEliminar.setOnClickListener(v -> confirmarEliminacion());
    }

    private void mostrarDialogoCambiarEstado() {
        String[] estados = {"RECIBIDO", "EN REPARACION", "LISTO", "ENTREGADO", "CANCELADO"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Seleccionar Nuevo Estado")
                .setItems(estados, (dialog, which) -> {
                    String nuevoEstado = estados[which];
                    if ("LISTO".equalsIgnoreCase(nuevoEstado)) {
                        Reparaciones repActual = viewModel.reparacion.getValue();
                        if (repActual != null) {
                            DialogDiagnosticoRefacciones diagDialog = DialogDiagnosticoRefacciones.newInstance(repActual, this::notificarCambioYSalir);
                            diagDialog.show(getParentFragmentManager(), "DialogDiagnosticoRefacciones");
                        }
                    } else {
                        //viewModel.actualizarEstado(nuevoEstado);
                        notificarCambioYSalir();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmarEliminacion() {
        /*new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Reparación")
                .setMessage("¿Estás seguro de que deseas eliminar esta reparación?")
                .setPositiveButton("Eliminar", (dialog, which) -> viewModel.eliminarReparacion(requireContext()))
                .setNegativeButton("Cancelar", null)
                .show();*/
    }

    private void notificarCambioYSalir() {
        if (getActivity() instanceof reparcionesView) {
            ((reparcionesView) getActivity()).cargarReparaciones();
        }
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
}