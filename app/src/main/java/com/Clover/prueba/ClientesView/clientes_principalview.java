package com.Clover.prueba.ClientesView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import Entidades.Clientes;
import Entidades.Productos;

public class clientes_principalview extends AppCompatActivity {
    private String columnaGlobal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.clientes_principalview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rellenarSpinner();
    }
    private void rellenarTabla(Clientes cliente){
        ClientesPrincipalAdapter adapter;
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.CP_recycler);
        adapter = new ClientesPrincipalAdapter(this, new ArrayList<Clientes>());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void rellenarSpinner(){
        Spinner spinerSeccion = findViewById(R.id.CP_spinnerBusqueda);
        Clientes clientes = new Clientes();
        String[] columnas = clientes.getColumn();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.productos_spiner_item, columnas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerSeccion.setAdapter(adapter);

        spinerSeccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String columna = columnas[position];
                Log.e("Clover_App", "onItemSelected: "+columna);
                if (columna.equals("ID")) columnaGlobal = "id_cliente";
                if (columna.equals("Nombre")) columnaGlobal = "nombre_cliente";
                if (columna.equals("Apodo")) columnaGlobal = "apodo";
                if (columna.equals("Saldo")) columnaGlobal = "saldo";
                if (columna.equals("Puntos")) columnaGlobal = "puntos";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void inputBusqueda(){
        TextInputEditText t = findViewById(R.id.CP_busquedaTXT);
        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }
}