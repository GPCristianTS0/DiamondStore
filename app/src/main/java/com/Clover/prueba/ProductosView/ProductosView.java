package com.Clover.prueba.ProductosView;

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

import java.util.ArrayList;

import BD.CRUD.ProductoDB;
import BD.Controller.ControllerProducto;
import Entidades.Productos;

public class ProductosView extends AppCompatActivity {
    private Spinner spinerSeccion;
    private Spinner spinerColumnas;
    private boolean all;
    private String seccionG;
    private String columnaObtencionG;
    private String busquedaG;
    private int positionSeccionSpinner;
    private int postionColumnaSpinner;
    private final ControllerProducto controller = new ProductoDB(this, "Productos.db", null, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.productos_view_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rellenarSpinnerSecciones();
        rellenarSpinnerColumnas();
        inputBusqueda();
    }

    private void rellenarTabla(String seccion, String columnaObtencion, String busqueda){
        ProductosViewAdapter adapter;
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recyclerProductosView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        ArrayList<Productos> productos;
        if (all) {
            if (!busqueda.isEmpty())
                productos = controller.getProductos(columnaObtencion, busqueda);
            else
                productos = controller.getProductos("", "");
        }else {
            productos = controller.getProductos(seccion, columnaObtencion, busqueda);
        }


        adapter = new ProductosViewAdapter(productos, new ProductosViewAdapter.OnItemClickListener() {
            @Override
            public void OnClickEditProduct(Productos producto, int position) {
                Intent intent = new Intent(ProductosView.this, FormularioProductos.class);
                intent.putExtra("producto", producto);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

    }
    //Spinner secciones
    private void rellenarSpinnerSecciones(){
        spinerSeccion = findViewById(R.id.spinner);
        ArrayList<String> secciones = controller.getSecciones();
        secciones.add(0, "Todas");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.productos_spiner_item, secciones);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerSeccion.setAdapter(adapter1);

        spinerSeccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionSeccionSpinner = position;
                busquedaG = "";
                if (position==0){
                    all = true;
                    seccionG = null;
                    columnaObtencionG = null;
                    busquedaG = null;
                    rellenarTabla( "", "", "");
                }else{
                    seccionG = secciones.get(position);
                    all = false;
                    rellenarTabla(seccionG, columnaObtencionG, busquedaG );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //rellenar Spinner columnas
    private void rellenarSpinnerColumnas(){
        spinerColumnas = findViewById(R.id.spinner2);
        ArrayList<String> columnas = Productos.getArrayColumn();
        columnas.remove(0);
        columnas.add(0, "Seleccionar");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.productos_spiner_item, columnas);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerColumnas.setAdapter(adapter2);
        spinerColumnas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    columnaObtencionG = "nombre";
                }else
                    columnaObtencionG = columnas.get(position);
                postionColumnaSpinner = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //Funcion Inpur Busqueda
    private void inputBusqueda(){
        TextInputEditText t = findViewById(R.id.textInputEditText);
        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rellenarTabla(seccionG, columnaObtencionG, s.toString());
            }

            ;
        });
    }

    //Funcion boton Agregar producto
    public void onClickAddProduct(View v){
        Intent intent = new Intent(ProductosView.this, FormularioProductos.class);
        startActivity(intent);
    }
    //On resume

    @Override
    protected void onResume() {
        super.onResume();
        if (seccionG!=null&&columnaObtencionG!=null&&busquedaG!=null){
            Log.e("Clover_App", "onResumeif: "+seccionG+" "+columnaObtencionG+" "+busquedaG);
            all = false;
            rellenarTabla( seccionG, columnaObtencionG, busquedaG);
        }else{
            all = true;
            rellenarTabla( "", "", "");
        }
        spinerSeccion.setSelection(positionSeccionSpinner);
        Log.e("Clover_App", "onResume: "+positionSeccionSpinner);

    }
}