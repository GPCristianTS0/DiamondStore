package com.Clover.prueba.ui.ventas;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.ui.productos.ProductosViewAdapter;
import com.Clover.prueba.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import com.Clover.prueba.data.controller.ControllerProducto;
import com.Clover.prueba.data.dao.ProductoDAO;
import com.Clover.prueba.data.models.Productos;

public class DialogVentasBuscarProducto extends DialogFragment {
    private String seccionG = "Todas";
    private String columnaObtencionG = "Codigo Barras";
    private ControllerProducto controller;
    public DialogVentasBuscarProducto(){
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.venta_view_productolista, container, false);
        controller = new ProductoDAO(getContext());
        // Aquí inicializas tu RecyclerView
        rellenarTabla(controller.getProductos(), view);
        rellenarSpinnerColumnas(view);
        rellenarSpinnerSecciones(view);
        inputBusqueda(view);
        return view;
    }
    private void rellenarTabla(ArrayList<Productos> productos, View v){
        ProductosViewAdapter adapter;
        RecyclerView recyclerView = v.findViewById(R.id.recyclerProductosView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        adapter = new ProductosViewAdapter(productos, new ProductosViewAdapter.OnItemClickListener() {
            @Override
            public void OnClickEditProduct(Productos producto, int position) {
                onProductoSeleccionado.onProductoSeleccionado(producto);
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);

    }

    //Spinner secciones
    private void rellenarSpinnerSecciones(View v){
        Spinner spinerSeccion = v.findViewById(R.id.spinner);
        ArrayList<String> secciones = controller.getSeccione();
        secciones.add(0, "Todas");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(v.getContext(), R.layout.productos_spiner_item, secciones);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerSeccion.setAdapter(adapter1);

        spinerSeccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Productos> productos;
                seccionG = secciones.get(position);
                Log.e("Clover_App", "onItemSelected: "+secciones.get(position));
                if (position==0){
                    rellenarTabla(controller.getProductos(), v);
                    return;
                }
                productos = controller.buscarProductosPor(secciones.get(position), columnaObtencionG, "");
                rellenarTabla(productos, v);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //rellenar Spinner columnas
    private void rellenarSpinnerColumnas(View v){
        Spinner spinerColumnas;
        spinerColumnas = v.findViewById(R.id.spinner2);
        String[] columnas = {"Codigo Barras","Nombre", "Marca","seccion", "Precio Publico", "Precio Neto", "Descripcion", "Vendidos", "Stock", "Ultimo Pedido"};
        ArrayList<String> columnasBD = Productos.getArrayColumn();
        columnasBD.remove(0);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(v.getContext(), R.layout.productos_spiner_item, columnas);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerColumnas.setAdapter(adapter2);
        spinerColumnas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                columnaObtencionG = columnasBD.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //Funcion Inpur Busqueda
    String busquedaIn = "";
    private void inputBusqueda(View v){
        TextInputEditText t = v.findViewById(R.id.textInputEditText);
        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                busquedaIn = s.toString();
                rellenarTabla(controller.buscarProductosPor(seccionG, columnaObtencionG, s.toString()), v);
            }

            ;
        });
    }
    //inteface para comunicar a la activity y entregar el producto
    public interface OnProductoSeleccionado {
        void onProductoSeleccionado(Productos producto);
    }
    private OnProductoSeleccionado onProductoSeleccionado;
    public void setOnProductoSeleccionado(OnProductoSeleccionado listener) {
        this.onProductoSeleccionado = listener;

    }
}
