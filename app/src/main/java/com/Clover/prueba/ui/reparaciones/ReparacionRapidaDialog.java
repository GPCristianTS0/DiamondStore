package com.Clover.prueba.ui.reparaciones;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.Clover.prueba.R;
import com.Clover.prueba.data.dao.ClientesDAO;
import com.Clover.prueba.data.dao.ReparacionesDAO;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.CorteCaja;
import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.domain.corteCaja.CorteCajaController;
import com.Clover.prueba.services.Helpers.ConfirmDialog;
import com.Clover.prueba.services.generators.TicketUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReparacionRapidaDialog extends BottomSheetDialogFragment {

    private ReparacionesDAO reparacionesDAO;
    private ClientesDAO clientesDAO;
    private CorteCajaController corteCajaController;
    private ArrayList<Clientes> listaClientes;
    private Spinner spClientes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_reparacion_rapida, container, false);

        reparacionesDAO = new ReparacionesDAO(requireContext());
        clientesDAO = new ClientesDAO(requireContext());
        corteCajaController = new CorteCajaController(requireContext());

        spClientes = view.findViewById(R.id.spClientesRep);
        cargarClientes();

        Button btnGuardar = view.findViewById(R.id.btnGuardarReparacion);
        btnGuardar.setOnClickListener(v -> guardarReparacion(view));

        return view;
    }

    private void cargarClientes() {
        listaClientes = clientesDAO.getClients();
        ArrayList<String> nombresClientes = new ArrayList<>();

        if (listaClientes == null || listaClientes.isEmpty()) {
            nombresClientes.add("Sin clientes registrados");
        } else {
            for (Clientes c : listaClientes) {
                String textoMostrar = c.getNombre_cliente();
                if (c.getApodo() != null && !c.getApodo().trim().isEmpty()) {
                    textoMostrar += " (" + c.getApodo() + ")";
                }
                nombresClientes.add(textoMostrar);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, nombresClientes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClientes.setAdapter(adapter);
    }

    private void guardarReparacion(View view) {
        if (listaClientes == null || listaClientes.isEmpty()) {
            Toast.makeText(getContext(), "Primero debe registrar un cliente en la sección Clientes", Toast.LENGTH_LONG).show();
            return;
        }

        int posCliente = spClientes.getSelectedItemPosition();
        if (posCliente < 0 || posCliente >= listaClientes.size()) {
            Toast.makeText(getContext(), "Selección de cliente no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        Clientes clienteSeleccionado = listaClientes.get(posCliente);

        TextInputEditText etEquipo = view.findViewById(R.id.etEquipoRep);
        TextInputEditText etColor = view.findViewById(R.id.etColorRep);
        TextInputEditText etReparacionRealizada = view.findViewById(R.id.etReparacionRealizadaRep);
        TextInputEditText etCostoTotal = view.findViewById(R.id.etCostoTotalRep);
        RadioGroup rgMetodoPago = view.findViewById(R.id.rgMetodoPagoRep);

        String equipo = etEquipo.getText() != null ? etEquipo.getText().toString().trim() : "";
        String color = etColor.getText() != null ? etColor.getText().toString().trim() : "";
        String reparacionRealizada = etReparacionRealizada.getText() != null ? etReparacionRealizada.getText().toString().trim() : "";
        String costoTotalStr = etCostoTotal.getText() != null ? etCostoTotal.getText().toString().trim() : "0";

        if (equipo.isEmpty()) {
            Toast.makeText(getContext(), "Por favor ingrese el equipo / modelo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (reparacionRealizada.isEmpty()) {
            Toast.makeText(getContext(), "Por favor ingrese la reparación realizada", Toast.LENGTH_SHORT).show();
            return;
        }

        double costoTotal = 0;
        try {
            if (!costoTotalStr.isEmpty()) costoTotal = Double.parseDouble(costoTotalStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Costo no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (costoTotal <= 0) {
            Toast.makeText(getContext(), "Ingrese un costo total mayor a cero", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPagoId = rgMetodoPago.getCheckedRadioButtonId();
        String tipoPago = "Efectivo";
        if (selectedPagoId == R.id.rbTarjetaRep) {
            tipoPago = "Tarjeta";
        } else if (selectedPagoId == R.id.rbTransferenciaRep) {
            tipoPago = "Transferencia";
        }

        String fechaHoraActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        int idCorte = 0;
        if (corteCajaController.isCorteAbierto()) {
            CorteCaja corte = corteCajaController.getCorteActual();
            if (corte != null) {
                idCorte = corte.getId_corte();
            }
        }

        Reparaciones rep = new Reparaciones();
        rep.setTipo_servicio("GENERAL");
        rep.setId_cliente(clienteSeleccionado.getId_cliente());
        rep.setEquipo(equipo);
        rep.setColor(color);
        rep.setFalla_reportada(reparacionRealizada);
        rep.setCosto_refacciones(0);
        rep.setMonto_total(costoTotal);
        rep.setAnticipo(costoTotal);
        rep.setMonto_pendiente(0);
        rep.setTipo_pago(tipoPago);
        rep.setEstado("ENTREGADO");
        rep.setFecha_ingreso(fechaHoraActual);
        rep.setFecha_entrega(fechaHoraActual);
        rep.setId_corte(idCorte);

        if (reparacionesDAO.addReparacion(rep)) {
            dismiss();
            Context context = requireContext();
            ConfirmDialog.mostrarDialogoConfirmacion(
                    context,
                    "Reparación Registrada",
                    "¿Deseas enviar el comprobante de reparación al cliente por WhatsApp?",
                    "Enviar Comprobante",
                    false,
                    () -> {
                        TicketUtils ticketUtils = new TicketUtils(context);
                        ticketUtils.generarTicketReparacion(context, rep, clienteSeleccionado, true);
                    }
            );
        } else {
            Toast.makeText(getContext(), "Error al guardar la reparación", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getTheme() {
        return com.google.android.material.R.style.Theme_MaterialComponents_Light_BottomSheetDialog;
    }
}
