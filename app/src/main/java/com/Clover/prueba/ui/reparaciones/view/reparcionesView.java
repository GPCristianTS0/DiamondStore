package com.Clover.prueba.ui.reparaciones.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.databinding.ActivityReparcionesViewBinding;
import com.Clover.prueba.ui.reparaciones.viewModel.ReparacionesFactory;
import com.Clover.prueba.ui.reparaciones.viewModel.ReparacionesViewModel;
import com.Clover.prueba.services.Helpers.BannerError;

import java.util.ArrayList;

public class reparcionesView extends AppCompatActivity {
    private ReparacionesViewModel viewModel;
    private ActivityReparcionesViewBinding binding;
    private ReparacionesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityReparcionesViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //viewModel
        ReparacionesFactory factory = new ReparacionesFactory(this);
        viewModel = new ViewModelProvider(this,factory).get(ReparacionesViewModel.class);
        viewModel.reparacionesAMostrar.observe(this, lista->{
            adapter.setLista(lista);

            if (lista.isEmpty()) {
                binding.rvReparaciones.setVisibility(View.GONE);
                binding.layoutEmptyState.setVisibility(View.VISIBLE);
            } else {
                binding.rvReparaciones.setVisibility(View.VISIBLE);
                binding.layoutEmptyState.setVisibility(View.GONE);
            }
        });
        viewModel.contadoresResult.observe(this, resultadosContadores -> {
            binding.tvCountPendientes.setText(String.valueOf(resultadosContadores.recibido));
            binding.tvCountEnProceso.setText(String.valueOf(resultadosContadores.enReparacion));
            binding.tvCountListos.setText(String.valueOf(resultadosContadores.listo));
        });
        viewModel.error.observe(this, error->{
            BannerError.mostrarError(binding.getRoot(), error);
        });

        setupRecyclerView(new ArrayList<>());
        setupSpinners();
        setupSearch();
        setupClickListeners();
        cargarReparaciones();
    }

    private void setupRecyclerView(ArrayList<Reparaciones> listaFiltrada) {
        binding.rvReparaciones.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReparacionesAdapter(listaFiltrada, (reparacion, position) -> abrirDetalleReparacion(reparacion));
        binding.rvReparaciones.setAdapter(adapter);
    }

    private void abrirDetalleReparacion(Reparaciones reparacion) {
        ReparacionDetalleDialog dialog = ReparacionDetalleDialog.newInstance(reparacion);
        dialog.show(getSupportFragmentManager(), "ReparacionDetalleDialog");
    }

    private void setupSpinners() {
        String[] estados = {"Todos los estados", "RECIBIDO", "EN REPARACION", "LISTO", "ENTREGADO", "CANCELADO"};
        ArrayAdapter<String> spinnerAdapterEstado = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, estados);

        binding.spFiltroEstado.setAdapter(spinnerAdapterEstado);

        String[] servicios = {"Todos los servicios","DOMICILIO", "REPARACION", "MANTENIMIENTO", "GARANTIA"};
        ArrayAdapter<String> spinnerAdapterServicio = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, servicios);
        binding.spFiltroServicio.setAdapter(spinnerAdapterServicio);

        binding.spFiltroServicio.setSelection(0);
        binding.spFiltroEstado.setSelection(1);

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        binding.spFiltroEstado.setOnItemSelectedListener(filterListener);
        binding.spFiltroServicio.setOnItemSelectedListener(filterListener);
    }

    private void setupSearch() {
        binding.etBuscarReparacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltros();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupClickListeners() {
        View.OnClickListener abrirDialogoNuevaReparacion = v -> abrirDialogoNuevaReparacion();

        binding.layoutBtnAgregarReparacion.setOnClickListener(abrirDialogoNuevaReparacion);
        binding.fabAgregarReparacion.setOnClickListener(abrirDialogoNuevaReparacion);
        binding.btnAgregarVacio.setOnClickListener(abrirDialogoNuevaReparacion);
    }

    private void abrirDialogoNuevaReparacion() {
        Intent intent = new Intent(this, FormularioReparaciones.class);
        startActivity(intent);
    }

    public void cargarReparaciones() {
        actualizarContadores();
        aplicarFiltros();
    }

    private void actualizarContadores() {
        viewModel.calcularContadores();
    }

    private void aplicarFiltros() {
        String query = binding.etBuscarReparacion.getText() != null
                ? binding.etBuscarReparacion.getText().toString().trim().toLowerCase()
                : "";

        String estadoSel = binding.spFiltroEstado.getSelectedItem() != null
                ? binding.spFiltroEstado.getSelectedItem().toString()
                : "Todos los estados";

        String servicioSel = binding.spFiltroServicio.getSelectedItem() != null
                ? binding.spFiltroServicio.getSelectedItem().toString()
                : "Todos los servicios";

        viewModel.aplicarFiltros(query, estadoSel, servicioSel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarReparaciones();
    }

}