package com.Clover.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Clover.prueba.HistorialVentas.historial_ventasView;
import com.Clover.prueba.ProductosView.ProductosView;
import com.Clover.prueba.VentasViews.VentaView;

import java.util.ArrayList;
import java.util.Calendar;

import BD.CRUD.ProductoDB;
import BD.CRUD.VentasDB;
import BD.Controller.ControllerProducto;
import BD.Controller.ControllerVentas;
import Entidades.Productos;

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
        ControllerVentas controllerVentas = new VentasDB(this, "historial_ventas.db", null, 1);
        controllerVentas.createTable();
        rellenarDatos();
    }
    //Accion boton de clientes
    public void onClickClientesView(View v){
         Intent intent = new Intent(MenuPrincipal.this, ClientesPrincipal.class);
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
        Intent intent = new Intent(MenuPrincipal.this, historial_ventasView.class);
        startActivity(intent);
    }
    //Rellenado de datos de los productos
    private void rellenarDatos(){
          int contadorProductos;
          int contadorVentas;
          int contadorUnidades=0;
          int contadorStockBajos=0;
          ControllerProducto controller = new ProductoDB(this, "Productos.db", null, 1);
          ArrayList<Productos> productos = controller.getProductos("" ,"");
          for (Productos producto : productos) {
              contadorUnidades += producto.getStock();
              if (producto.getStock() < 3) {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        rellenarDatos();
    }
}