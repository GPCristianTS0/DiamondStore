package com.Clover.prueba.ui.proveedores;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.domain.proveedores.ProveedoresController;
import com.Clover.prueba.data.models.Proveedor;
import com.Clover.prueba.services.sharing.ShareManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ProveedorView extends AppCompatActivity {
    private ProveedoresController controllerProveedores;
    private String columnaSpinner = "";
    private String busqueda = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.proveedor_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        controllerProveedores = new ProveedoresController(this);
        rellenarSpinner();
        ArrayList<Proveedor> p = controllerProveedores.getProveedoresBy(null, null);
        rellenarTabla(p);
        ImageView btnAgregar = findViewById(R.id.PrV_btnAgregar);
        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProveedorFormularioView.class);
            startActivity(intent);
        });
        inputListener();
    }
    private void rellenarTabla(ArrayList<Proveedor> proveedores){
        ProveedorViewAdapter adapter;
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.PrV_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ShareManager shareManager = new ShareManager();
        adapter = new ProveedorViewAdapter(proveedores, new ProveedorViewAdapter.OnClickListener(){
            @Override
            public void onClickLlamar(String numero, int position) {
                if (numero != null && !numero.isEmpty()) {
                    shareManager.abrirLlamada(ProveedorView.this, numero);
                } else {
                    Toast.makeText(null, "Sin número registrado", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onClickWhatsapp(String numero, int position) {
                if (numero != null && !numero.isEmpty()) {
                    shareManager.abrirWhatsapp(ProveedorView.this, numero, null);
                } else {
                    android.widget.Toast.makeText(null, "Sin número para WhatsApp", android.widget.Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onClickModificar(Proveedor proveedor, int position) {
                Intent intent = new Intent(ProveedorView.this, ProveedorFormularioView.class);
                intent.putExtra("proveedor", proveedor);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void rellenarSpinner(){
        Spinner spinerColumnas = findViewById(R.id.PrV_spinner);
        String[] columnas = Proveedor.getArrayColumnUI();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.productos_spiner_item, columnas);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerColumnas.setAdapter(adapter2);
        spinerColumnas.setSelection(1);

        spinerColumnas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                columnaSpinner = Proveedor.getArrayColumn()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
    }
    private void inputListener(){
        TextInputEditText t = findViewById(R.id.PrV_buscartxt);
        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                busqueda = s.toString();
                rellenarTabla(controllerProveedores.getProveedoresBy(columnaSpinner, busqueda));
            }
        });
    }

    @Override
    protected void onResume() {
        rellenarTabla(controllerProveedores.getProveedoresBy(columnaSpinner, busqueda));
        super.onResume();
    }
}