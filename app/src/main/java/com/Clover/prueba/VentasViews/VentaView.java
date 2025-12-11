package com.Clover.prueba.VentasViews;

import static android.view.View.INVISIBLE;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import BD.DAOs.ProductoDAO;
import BD.DAOs.VentasDAO;
import BD.Controller.ControllerProducto;
import BD.Controller.ControllerVentas;
import Entidades.DetalleVenta;
import Entidades.Productos;
import Entidades.Ventas;
import Tools.EscanerCodeBar;

public class VentaView extends AppCompatActivity {
    private ArrayList<Productos> productos = new ArrayList<>();
    private VentasViewAdapter adapter ;
    private ControllerVentas controllerVentas = new VentasDAO(this);
    TextView noArticulosCount ;
    TextView totallbl;
    private int total;
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
        movimientoCodigo();
        //Inicializar recycler
        rellenarCarrito();
        noArticulosCount = findViewById(R.id.noArticulosCount);
        totallbl = findViewById(R.id.totallbl);
        TextInputEditText codigoTxt = findViewById(R.id.escanertxt);
        codigoTxt.setOnEditorActionListener((v, actionId, event) ->{
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE|| (event != null && event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                String texto = codigoTxt.getText().toString().trim();
                if (texto.isEmpty()){
                    return false;
                }
                agregarAlCarrito(texto);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(codigoTxt.getWindowToken(), 0);
                codigoTxt.setText("");
                return true;
            }
            return false;
        });
    }
    //Accion boton Escaner
    public void onClickEscanner(View v){
        EscanerCodeBar escaner = new EscanerCodeBar();
        escaner.inicializarEscaner(VentaView.this);
    }
    private void agregarAlCarrito(String codigo){
        ControllerProducto controller = new ProductoDAO(this);
        if (!verificarCodigo(codigo)) {
            Toast.makeText(this, "Articulo no registrado", Toast.LENGTH_SHORT).show();
            return;
        }
        Productos producto = controller.getProductoCode(codigo);
        productos.add(0, producto);
        TextView noArticulosCount = findViewById(R.id.noArticulosCount);
        TextView totallbl = findViewById(R.id.totallbl);
        total += producto.getPrecioPublico();
        noArticulosCount.setText(String.valueOf(productos.size()));
        totallbl.setText(String.valueOf(total));
        adapter.notifyItemInserted(0);
        Toast.makeText(this, "Articulo agregado", Toast.LENGTH_SHORT).show();
        TextInputEditText codetxt = findViewById(R.id.escanertxt);
        codetxt.setText("");

    }
    //Elimina un producto del array
    private void eliminarProducto(Productos producto) {
        // Si quieres, actualiza totales
        total -= producto.getPrecioPublico();
        noArticulosCount.setText(String.valueOf(productos.size()));
        totallbl.setText(String.valueOf(total));
    }

    //Repinta la tabla con el recycler
    private void rellenarCarrito(){
        RecyclerView recyclerView = findViewById(R.id.recyclerProductosView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new VentasViewAdapter(productos, new VentasViewAdapter.OnItemClickListener() {
            @Override
            public void onEliminarClick(Productos producto, int position) {
                // Elimina del array original también
                productos.remove(producto);

                adapter.notifyItemRemoved(position);
                eliminarProducto(producto);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private boolean verificarCodigo(String codigo) {
        ControllerProducto controller = new ProductoDAO(this);
        Productos productos = controller.getProductoCode(codigo);
        return productos.getId().equals(codigo);
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
        if (total <= 0){
            Toast.makeText(this, "No hay articulos", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogFragmentVentas frament = new DialogFragmentVentas(total);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, frament).addToBackStack(null).commit();

        FrameLayout fragmentContainer = findViewById(R.id.fragment_container_view_tag);
        fragmentContainer.setVisibility(View.VISIBLE);
        fragmentContainer.setOnClickListener(v -> {
            getSupportFragmentManager().popBackStack();
            fragmentContainer.setVisibility(INVISIBLE);
        });
        frament.setVentaConfirmada(new DialogFragmentVentas.ventaConfirmada(){
            @Override
            public void ventaConfirmada() {
                Ventas venta = new Ventas();
                venta.setId_cliente(0);
                venta.setMonto(total);
                venta.setTotal_piezas(productos.size());
                venta.setTipo_pago("Efectivo");
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault());
                String fecha = LocalDateTime.now().format(format);
                venta.setFecha_hora(fecha);

                ArrayList<DetalleVenta> detalleVentas = new ArrayList<>();
                for (Productos producto : productos) {
                    boolean encontrado = false;

                    for (DetalleVenta detalleVenta : detalleVentas){
                        if (detalleVenta.getId_producto().equals(producto.getId())){
                            detalleVenta.setCantidad(detalleVenta.getCantidad()+1);
                            encontrado = true;
                            break;
                        }
                    }

                    if (!encontrado){
                        DetalleVenta detallevVenta = new DetalleVenta();
                        detallevVenta.setId_venta(venta.getId_venta());
                        detallevVenta.setId_producto(producto.getId());
                        detallevVenta.setCantidad(1);
                        detallevVenta.setPrecio(producto.getPrecioPublico());
                        detalleVentas.add(detallevVenta);
                    }
                }
                Log.e("Clover_App", detalleVentas.toString());
                controllerVentas.addVenta(venta, detalleVentas);
                productos.clear();
                noArticulosCount.setText(0+"");
                totallbl.setText(0+"");
                total = 0;
                adapter.notifyDataSetChanged();
            }
        });

    }
}