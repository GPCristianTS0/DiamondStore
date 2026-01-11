package com.Clover.prueba.ui.ventas;

import static android.view.View.INVISIBLE;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import com.Clover.prueba.data.dao.ProductoDAO;
import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.data.controller.ControllerProducto;
import com.Clover.prueba.data.controller.ControllerVentas;
import com.Clover.prueba.data.models.DetalleVenta;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.data.models.Ventas;
import utils.EscanerCodeBar;

public class VentaView extends AppCompatActivity {

    private final ControllerProducto controller = new ProductoDAO(this);
    private final ArrayList<DetalleVenta> detallesVenta = new ArrayList<>();
    private VentasViewAdapter adapter ;
    private final ControllerVentas controllerVentas = new VentasDAO(this);
    private TextView noArticulosCount ;
    private TextView totallbl;

    private TextInputEditText codetxt;
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
    }
    //Accion boton Escaner
    public void onClickEscanner(View v){
        EscanerCodeBar escaner = new EscanerCodeBar();
        escaner.inicializarEscaner(VentaView.this);
    }
    private void agregarAlCarrito(String codigo){
        Productos producto = controller.getProductoCode(codigo);
        //Comprobacion del producto si esta agotado o no existe
        if (!comprobacionProducto(producto)) return;
        boolean encontrado = false;
        //recorre el carrito para checar si existe en el carrito
        for (DetalleVenta detalle: detallesVenta){
            //comprueba si el producto ya esta en el carrito
            if (detalle.getProducto().getId().equals(producto.getId())){
                //Comprueba que no se pase del stock
                if (producto.getStock()<detalle.getCantidad()+1){
                    Toast.makeText(this, "Articulo agotado", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    //Aumenta la cantidad
                    detalle.setCantidad(detalle.getCantidad()+1);
                }
                //Actualiza el recycler
                adapter.notifyItemChanged(detallesVenta.indexOf(detalle));
                encontrado = true;
                break;
            }
        }
        if (!encontrado){
            //Agrega el producto al carrito en caso que no se haya encontrado
            DetalleVenta detalleVentas = new DetalleVenta();
            detalleVentas.setId_producto(producto.getId());
            detalleVentas.setNombre_producto(producto.getNombre());
            detalleVentas.setProducto(producto);
            detalleVentas.setCantidad(1);
            detalleVentas.setPrecio(producto.getPrecioPublico());
            detallesVenta.add(0, detalleVentas);
            adapter.notifyItemInserted(0);
            RecyclerView recyclerView = findViewById(R.id.recyclerProductosView);
            recyclerView.scrollToPosition(0);
        }
        //Actualiza el total y el numero de articulos
        codetxt.setText("");
        total += producto.getPrecioPublico();
        noArticulosCount.setText(String.valueOf(totalpiezas()));
        totallbl.setText("Total: $"+total);
    }
    //Repinta la tabla con el recycler
    private void rellenarCarrito(){
        RecyclerView recyclerView = findViewById(R.id.recyclerProductosView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new VentasViewAdapter(detallesVenta, new VentasViewAdapter.OnItemClickListener() {
            @Override
            public void onAgregarClick(DetalleVenta producto, int position) {
                agregarAlCarrito(producto.getId_producto());
                adapter.notifyItemChanged(position);
            }x

            @Override
            public void onDisminuirClick(DetalleVenta producto, int position) {
                //comprueba que la cantidad no sea 0
                if ( detallesVenta.get(position).getCantidad()>0){
                    detallesVenta.get(position).setCantidad(detallesVenta.get(position).getCantidad()-1);
                    adapter.notifyItemChanged(position);
                }
                //Actualiza el total y el numero de articulos restando
                TextView noArticulosCount = findViewById(R.id.noArticulosCount);
                TextView totallbl = findViewById(R.id.totallbl);
                total -= detallesVenta.get(position).getProducto().getPrecioPublico();
                noArticulosCount.setText(String.valueOf(totalpiezas()));
                totallbl.setText("Total: $"+total);
                //En caso que sea 0 elimina el producto
                if (detallesVenta.get(position).getCantidad()==0){
                    detallesVenta.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private boolean comprobacionProducto(Productos producto) {
        if (producto.getId() == null) {
            Toast.makeText(this, "Articulo no registrado", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (producto.getStock()==0) {
            Toast.makeText(this, "Articulo agotado", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
                venta.setTotal_piezas(totalpiezas());
                venta.setTipo_pago("Efectivo");
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault());
                String fecha = LocalDateTime.now().format(format);
                venta.setFecha_hora(fecha);
                //Agregar venta
                controllerVentas.addVenta(venta, detallesVenta);
                //Limpiar carrito
                detallesVenta.clear();
                noArticulosCount.setText(String.valueOf(0));
                totallbl.setText(String.valueOf(0));
                total = 0;
                adapter.notifyDataSetChanged();
            }
        });

    }
    private int totalpiezas(){
        int total = 0;
        for (DetalleVenta detalle : detallesVenta){
            total += detalle.getCantidad();
        }
        return total;
    }
    //Funciones para el dialogFragment de los productos
    private void onClickBuscarProducto(){
        DialogVentasBuscarProducto frament = new DialogVentasBuscarProducto();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, frament).addToBackStack(null).commit();

        FrameLayout fragmentContainer = findViewById(R.id.fragment_container_view_tag);
        fragmentContainer.setVisibility(View.VISIBLE);
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
}