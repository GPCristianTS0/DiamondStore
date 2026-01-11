package com.Clover.prueba.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Clover.prueba.R;
import com.Clover.prueba.ui.clientes.ClientesPrincipalView;
import com.Clover.prueba.ui.historialventas.HistorialVentasView;
import com.Clover.prueba.ui.productos.ProductosView;
import com.Clover.prueba.ui.productos.ProductosActualizarStock;
import com.Clover.prueba.ui.ventas.VentaView;

import java.util.ArrayList;

import com.Clover.prueba.data.dao.ProductoDAO;
import com.Clover.prueba.data.controller.ControllerProducto;
import com.Clover.prueba.data.models.Productos;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ControllerProducto controllerProducto = new ProductoDAO(this);
        rellenarDatos();


    }
    //Accion boton de clientes
    public void onClickClientesView(View v){
         Intent intent = new Intent(MenuPrincipal.this, ClientesPrincipalView.class);
         startActivity(intent);
    }
    //Accion boton de productos
    public void onClickProductosView(View v){
        Intent intent = new Intent(MenuPrincipal.this, ProductosView.class);
        startActivity(intent);
    }
    //Accion Boton Precio Publico
    public void onClickPrecioPublico(View v){
        Intent intent = new Intent(MenuPrincipal.this, Precio_Publico.class);
        startActivity(intent);
    }
    //Accion boton ventas
    public void onClickVentas(View v){
        Intent intent = new Intent(MenuPrincipal.this, VentaView.class);
        startActivity(intent);
    }
    //Accion boton historial ventas

    public void onClickHistorialVentas(View v){
        Intent intent = new Intent(MenuPrincipal.this, HistorialVentasView.class);
        startActivity(intent);
    }
    //Accion boton actualizar stock
    public void onClickActualizarStock(View v){
        Intent intent = new Intent(MenuPrincipal.this, ProductosActualizarStock.class);
        startActivity(intent);
    }
    //Rellenado de datos de los productos
    private void rellenarDatos(){
          int contadorProductos;
          int contadorVentas =0;
          int contadorUnidades=0;
          int contadorStockBajos=0;
          ControllerProducto controller = new ProductoDAO(this);
          ArrayList<Productos> productos = controller.getProductos();
          for (Productos producto : productos) {
              contadorUnidades += producto.getStock();
              contadorVentas += producto.getVendidos();
              if (producto.getStock() < 2) {
                  contadorStockBajos++;
              }
          }
          contadorProductos = productos.size();
          TextView co = findViewById(R.id.productosTotalContador);
          co.setText(String.valueOf(contadorProductos));
          co = findViewById(R.id.unidadesTotalContador);
          co.setText(String.valueOf(contadorUnidades));
          co = findViewById(R.id.stockTotalContador);
          co.setText(String.valueOf(contadorStockBajos));
          co = findViewById(R.id.vendidosTotalContador);
          co.setText(String.valueOf(contadorVentas));
    }

    @Override
    protected void onResume() {
        super.onResume();
        rellenarDatos();
    }
}