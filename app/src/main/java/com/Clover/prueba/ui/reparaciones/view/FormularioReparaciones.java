package com.Clover.prueba.ui.reparaciones.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.Clover.prueba.R;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.databinding.ReparacionFormularioBinding;
import com.Clover.prueba.services.Helpers.BannerError;
import com.Clover.prueba.ui.reparaciones.viewModel.ReparacionesFactory;
import com.Clover.prueba.ui.reparaciones.viewModel.FormularioViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FormularioReparaciones extends AppCompatActivity {

    private FormularioViewModel viewModel;
    private ReparacionFormularioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ReparacionFormularioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ReparacionesFactory factory = new ReparacionesFactory(this);
        viewModel = new ViewModelProvider(this, factory).get(FormularioViewModel.class);
        viewModel.listaClientesLiv.observe(this, this::cargarClientes);
        viewModel.exito.observe(this, exito ->{
            if (exito){
                Toast.makeText(this,"Reparacion registrada con exito",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        viewModel.error.observe(this, mensaje ->{
            BannerError.mostrarError(binding.getRoot(), mensaje);
        });

        setupClickListeners();
        cargarSpinners();
    }


    private void setupClickListeners() {
        binding.btnFormNuevoCliente.setOnClickListener(v -> {
            ClienteRapidoDialog dialog = ClienteRapidoDialog.newInstance(() -> {viewModel.getClient();});
            dialog.show(getSupportFragmentManager(), "ClienteRapidoDialog");
        });
        binding.btnGuardarFormularioReparacion.setOnClickListener(v ->{
            viewModel.addReparacion(obtenerDatos());
        });
        binding.etFormFechaPromesa.setOnClickListener(v ->{mostrarCalendario();});
    }

    private void cargarSpinners() {
        viewModel.getClient();
        cargarTiposServicio();
        cargarEstados();
    }
    private Reparaciones obtenerDatos(){
        Reparaciones reparacion = new Reparaciones();
        reparacion.setColor(binding.etFormColor.getText().toString());
        reparacion.setEquipo(binding.etFormEquipo.getText().toString());
        reparacion.setEstado(binding.spFormEstado.getSelectedItem().toString());
        reparacion.setTipo_servicio(binding.spFormTipoServicio.getSelectedItem().toString());
        Clientes cliente = (Clientes) binding.spFormCliente.getSelectedItem();
        reparacion.setId_cliente(cliente.getId_cliente());
        reparacion.setAnticipo(parsearCostos(binding.etFormAnticipo.getText() != null ? binding.etFormAnticipo.getText().toString().trim() : ""));
        reparacion.setMonto_total(parsearCostos(binding.etFormMontoTotal.getText() != null ? binding.etFormMontoTotal.getText().toString().trim() : ""));
        reparacion.setFalla_reportada(binding.etFormFallaReportada.getText().toString());
        int selectedId = binding.rgFormTipoPago.getCheckedRadioButtonId();
        String tipodepago;
        if (selectedId == -1) tipodepago = "-1";
        else{
            RadioButton rbSelected = findViewById(selectedId);
            tipodepago = rbSelected.getText().toString();
        }
        reparacion.setTipo_pago(tipodepago);
        reparacion.setFecha_promesa(binding.etFormFechaPromesa.getText().toString());
        return reparacion;
    }
    private void cargarClientes(ArrayList<Clientes> clientes) {
        ArrayAdapter<Clientes> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spFormCliente.setAdapter(adapter);

        if (!clientes.isEmpty()) {
            binding.spFormCliente.setSelection(clientes.size() - 1);
        }
    }private void mostrarCalendario() {
        final Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    int mesReal = month + 1;
                    String fechaVisual = String.format(Locale.getDefault(), "%d-%02d-%02d", year, mesReal, dayOfMonth);
                    binding.etFormFechaPromesa.setText(fechaVisual);
                },
                año, mes, dia
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    private void cargarTiposServicio() {
        String[] servicios = {"REPARACION","DOMICILIO", "GARANTIA", "MANTENIMIENTO"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,servicios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spFormTipoServicio.setAdapter(adapter);
    }

    private void cargarEstados() {
        String[] estados = {"RECIBIDO", "EN REPARACION", "LISTO", "ENTREGADO", "CANCELADO"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spFormEstado.setAdapter(adapter);
    }
    private int parsearCostos(String costo){
        int numer;
        try {
            if (costo.isEmpty()) return 0;
            numer = Integer.parseInt(costo);
        } catch (Exception e) {
            return -1;
        }
        return numer;
    }
}