package com.Clover.prueba.HistorialVentas;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import BD.CRUD.VentasDB;
import BD.Controller.ControllerVentas;
import Entidades.Productos;
import Entidades.Ventas;

public class historial_ventasView extends AppCompatActivity {

    private ControllerVentas controllerVentas = new VentasDB(this, "historial_ventas.db", null, 1);
    private Spinner spinner ;
    private Spinner spinner2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.historialventas_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spinner = findViewById(R.id.spinner3);
        rellenospiner();
        //Listener textfield busqueda
        TextInputEditText busquedaInput = findViewById(R.id.busquedaInputo);
        busquedaInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                busqueda(s.toString());
            }
        });

    }
    //funcion busqueda textField
    private void busqueda(String busqueda){
        ArrayList<Ventas> ventas = controllerVentas.getVentas(spinner.getSelectedItem().toString().toLowerCase(),spinner2.getSelectedItem().toString(),busqueda);
        rellenarScroll(ventas);
    }

    //relleno de scroll con las ventas
    private void rellenarScroll(ArrayList<Ventas> ventas){
        HistorialVentasAdapter adapter;
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recyclerVentasView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        Collections.reverse(ventas);

        adapter = new HistorialVentasAdapter(ventas, new HistorialVentasAdapter.OnItemClickListener() {
            @Override
            public void OnClickEditProduct(Ventas venta, int position) {
                Intent intent = new Intent(historial_ventasView.this, historialventas_detalleventa.class);
                intent.putExtra("venta", venta);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

    }

    //relleno de spinners
    private void rellenospiner(){
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        ArrayList<String> mesese = new ArrayList<>(Arrays.asList(meses));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.productos_spiner_item, mesese);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        //Pone el spiner al mes actual
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES"));
        String fecha = LocalDateTime.now().format(format);
        int pos = mesese.indexOf(capitalizar(fecha));
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Ventas> ventas = controllerVentas.getVentas(mesese.get(position).toLowerCase(),LocalDateTime.now().getYear()+"","");
                rellenarScroll(ventas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Relleno de spinner año
        spinner2 = findViewById(R.id.spinner4);
        ArrayList<String> anios = controllerVentas.getAnios();
        adapter1 = new ArrayAdapter<>(this, R.layout.productos_spiner_item, anios);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter1);
        spinner2.setSelection(anios.indexOf(Calendar.getInstance().get(Calendar.YEAR)+""));

        Log.e("Clover_App", "onCreate: "+anios);

    }
    private String capitalizar(String string){
        return string.substring(0, 1).toUpperCase()+string.substring(1);
    }
}