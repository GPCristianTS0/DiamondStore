package com.Clover.prueba.ui.ventas;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.Clover.prueba.data.models.DetalleVenta;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.utils.EscanerCodeBar;

public class VentaView extends AppCompatActivity {
    private VentasModel modelVentas;
    private VentasViewAdapter adapter ;
    private TextView noArticulosCount ;
    private TextView totallbl;

    private TextInputEditText codetxt;

    private TextView vi;
    private void movimientoCodigo(){
        ConstraintLayout view = findViewById(R.id.escanerVentasLayout);
        View rootView = findViewById(android.R.id.content);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;

            // Si el teclado está visible (ocupa más del 15% de la pantalla)
            if (keypadHeight > screenHeight * 0.15) {
                // Subir el fragment unos píxeles
                view.animate().translationY((float) -keypadHeight).setDuration(200).start();
            } else {
                // Volver a la posición original
                view.animate().translationY(0).setDuration(200).start();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.venta_view_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        modelVentas = new VentasModel(this);
        rellenarCarrito();
        movimientoCodigo();
        inputCliente();
        //Inicializar recycler
        noArticulosCount = findViewById(R.id.noArticulosCount);
        totallbl = findViewById(R.id.totallbl);
        vi = findViewById(R.id.VV_clienteNombre);
        codetxt = findViewById(R.id.escanertxt);
        codetxt.setOnEditorActionListener((v, actionId, event) ->{
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE|| (event != null && event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                String texto = codetxt.getText().toString().trim();
                if (texto.isEmpty()){
                    return false;
                }
                agregarAlCarrito(texto);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(codetxt.getWindowToken(), 0);
                codetxt.setText("");
                return true;
            }
            return false;
        });
        //Accion boton buscar productos
        Button b = findViewById(R.id.VV_btnBuscarProducto);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBuscarProducto();
            }
        });
        //Accion boton cancelar
        Button b2 = findViewById(R.id.button5);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelVentas.vaciarCarrito();
                adapter.notifyDataSetChanged();
                noArticulosCount.setText(String.valueOf(0));
                String total = "Total: $";
                totallbl.setText(total.concat(0+""));
                codetxt.setText("");
                TextView vi = findViewById(R.id.VV_clienteNombre);
                vi.setText("Nombre Cliente");
                TextInputEditText t = findViewById(R.id.VV_clienteNombreTxt);
                t.setVisibility(VISIBLE);
            }
        });
    }
    //Accion boton Escaner
    public void onClickEscanner(View v){
        EscanerCodeBar escaner = new EscanerCodeBar();
        escaner.inicializarEscaner(VentaView.this);
    }
    private void agregarAlCarrito(String codigo){
        String result = modelVentas.agregarAlCarrito(codigo);
        if (result.equals("No existe")) {
            Toast.makeText(this, "No existe", Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.equals("Agotado")) {
            Toast.makeText(this, "Agotado", Toast.LENGTH_SHORT).show();
            return;
        }
        if(result.equals("insertado")){
            adapter.notifyItemInserted(0);
            RecyclerView recyclerView = findViewById(R.id.recyclerProductosView);
            recyclerView.scrollToPosition(0);

        }else
            adapter.notifyItemChanged(Integer.parseInt(result));
        //Actualiza el total y el numero de articulos
        codetxt.setText("");
        noArticulosCount.setText(String.valueOf(modelVentas.totalpiezas()));
        String total = "Total: $";
        totallbl.setText(total.concat(modelVentas.getTotal()+""));
    }
    //Repinta la tabla con el recycler
    private void rellenarCarrito(){
        RecyclerView recyclerView = findViewById(R.id.recyclerProductosView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new VentasViewAdapter(modelVentas.getDetallesVenta(), new VentasViewAdapter.OnItemClickListener() {
            @Override
            public void onAgregarClick(DetalleVenta producto, int position) {
                agregarAlCarrito(producto.getId_producto());
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onDisminuirClick(DetalleVenta producto, int position) {
                String result = modelVentas.disminuirClick(position);
                if(result.equals("resto")){
                    adapter.notifyItemChanged(position);
                }
                if(result.equals("eliminado")){
                    adapter.notifyItemRemoved(position);
                }
                //Actualiza el total y el numero de articulos restando
                noArticulosCount.setText(String.valueOf(modelVentas.totalpiezas()));
                String total = "Total: $";
                totallbl.setText(total.concat(modelVentas.getTotal()+""));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            } else {
                agregarAlCarrito(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    //Accion boton pagar
    public void onClickPagar(View view){
        if (modelVentas.vacio()){
            Toast.makeText(this, "No hay articulos", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogFragmentVentas frament = new DialogFragmentVentas(modelVentas.getTotal());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, frament).addToBackStack(null).commit();

        FrameLayout fragmentContainer = findViewById(R.id.fragment_container_view_tag);
        fragmentContainer.setVisibility(VISIBLE);
        fragmentContainer.setOnClickListener(v -> {
            getSupportFragmentManager().popBackStack();
            fragmentContainer.setVisibility(INVISIBLE);
        });
        frament.setVentaConfirmada(new DialogFragmentVentas.ventaConfirmada(){
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void ventaConfirmada() {
                modelVentas.ventaConfirmada("Efectivo");
                modelVentas.vaciarCarrito();
                //Limpiar
                noArticulosCount.setText(String.valueOf(0));
                totallbl.setText(String.valueOf(0));
                codetxt.setText("");
                vi.setText("Nombre Cliente");
                adapter.notifyDataSetChanged();
            }
        });

    }
    //Funciones para el dialogFragment de los productos
    private void onClickBuscarProducto(){
        DialogVentasBuscarProducto frament = new DialogVentasBuscarProducto();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, frament).addToBackStack(null).commit();

        FrameLayout fragmentContainer = findViewById(R.id.fragment_container_view_tag);
        fragmentContainer.setVisibility(VISIBLE);
        fragmentContainer.setOnClickListener(v -> {
            getSupportFragmentManager().popBackStack();
            fragmentContainer.setVisibility(INVISIBLE);
        });
        frament.setOnProductoSeleccionado(new DialogVentasBuscarProducto.OnProductoSeleccionado() {
            @Override
            public void onProductoSeleccionado(Productos producto) {
                agregarAlCarrito(producto.getId());
            }
        });
    }
    //Funcion agregar Cliente
    private void inputCliente(){
        TextInputEditText t = findViewById(R.id.VV_clienteNombreTxt);
        t.setOnEditorActionListener((v, actionId, event) ->{
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE|| (event != null && event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                String texto = t.getText().toString().trim();
                if (texto.isEmpty()){
                    return false;
                }
                if(modelVentas.setCliente(texto)){
                    t.setVisibility(INVISIBLE);
                    vi.setText(modelVentas.getClienteNombre());
                }else{
                    Toast.makeText(this, "Cliente no existe", Toast.LENGTH_SHORT).show();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(t.getWindowToken(), 0);
                t.setText("");
                return true;
            }
            return false;
        });

    }
}