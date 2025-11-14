package com.Clover.prueba.ProductosView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;

import java.util.ArrayList;

import BD.CRUD.ProductoDB;
import BD.Controller.ControllerProducto;
import Entidades.Productos;

public class ProductosView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private int numeroProductos;
    private ProductosViewAdapter adapter;
    private ControllerProducto controller = new ProductoDB(this, "Productos.db", null, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productos_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rellenarTabla();
    }

    private void rellenarTabla(){
        recyclerView = findViewById(R.id.recyclerProductosView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        ArrayList<Productos> productos = controller.getProductos();



        adapter = new ProductosViewAdapter(productos, new ProductosViewAdapter.OnItemClickListener() {
            @Override
            public void OnClickEditProduct(Productos producto, int position) {
                Intent intent = new Intent(ProductosView.this, FormularioProductos.class);
                intent.putExtra("producto", producto);
                startActivity(intent);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);

    }
    //Obtener las unidades totales
    private int getUnidades(ArrayList<Productos> productos){
        int unidades = 0;
        for (Productos producto : productos) {
            unidades += producto.getStock();
        }
        return unidades;
    }
    //Spinner
    private void rellenarSpinner(){
        Spinner spiner = findViewById(R.id.spinner);
        ArrayList<String> secciones = controller.getSecciones();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, secciones);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapter1);

        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //Spinner cuando se selecciona una seccion

    //Funcion boton Agregar producto
    public void onClickAddProduct(View v){
        Intent intent = new Intent(ProductosView.this, FormularioProductos.class);
        startActivity(intent);
        finish();
    }
}