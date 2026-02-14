package com.Clover.prueba.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Clover.prueba.ui.credito.CreditoDarAbono;
import com.Clover.prueba.ui.credito.CreditoPrincipalView;
import com.Clover.prueba.ui.financiero.DashboardFinanciero;
import com.Clover.prueba.R;
import com.Clover.prueba.domain.configuracion.ConfiguracionControl;
import com.Clover.prueba.domain.ventas.CorteCajaController;
import com.Clover.prueba.data.dao.interfaces.IVentas;
import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.ui.clientes.ClientesPrincipalView;
import com.Clover.prueba.ui.config.ConfigView;
import com.Clover.prueba.ui.corte.CorteView;
import com.Clover.prueba.ui.gastos.GastoFormulario;
import com.Clover.prueba.ui.gastos.GastosDialog;
import com.Clover.prueba.ui.historialventas.HistorialVentasView;
import com.Clover.prueba.ui.productos.ProductosView;
import com.Clover.prueba.ui.productos.ProductosActualizarStock;
import com.Clover.prueba.ui.proveedores.ProveedorView;
import com.Clover.prueba.ui.ventas.VentaView;

import com.Clover.prueba.data.dao.ProductoDAO;
import com.Clover.prueba.data.dao.interfaces.IProducto;
import com.Clover.prueba.ui.corte.VentasCerrarCorte;

public class MenuPrincipal extends AppCompatActivity {
    private IVentas iVentas;
    private IProducto iProductos;
    private TextView co ;

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
        IProducto iProducto = new ProductoDAO(this);
        iVentas = new VentasDAO(this);
        iProductos = new ProductoDAO(this);
        rellenarDatos();

        Button btn = findViewById(R.id.abonoBtn);
        btn.setOnClickListener(v -> {
            CreditoDarAbono dialog = new CreditoDarAbono();
            dialog.show(getSupportFragmentManager(), "dialog");
        });
        Button btn2 = findViewById(R.id.verDeudoresBtn);
        btn2.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, CreditoPrincipalView.class);
            startActivity(intent);
        });
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
    //Accion Boton Proveedores
    public void onClickProveedores(View v){
        Intent intent = new Intent(MenuPrincipal.this, ProveedorView.class);
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
    //Accion boton Gastos
    public void onClickGastosView(View v){
        GastosDialog dialog = new GastosDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }
    //Accion para boton Agregar Gasto
    public void onClickAgregarGasto(View v){
        GastoFormulario dialog = new GastoFormulario();
        dialog.show(getSupportFragmentManager(), "dialog");
    }
    //Accion cerrar turno
    public void onClickCerrarTurno(View v){
        CorteCajaController corteCajaController = new CorteCajaController(this);
        if (corteCajaController.isCorteAbierto()){
            Intent intent = new Intent(MenuPrincipal.this, VentasCerrarCorte.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, "No hay un turno abierto", Toast.LENGTH_SHORT).show();
        }
    }
    //Accion boton configuracion
    public void onClickConfiguracion(View v){
        Intent intent = new Intent(MenuPrincipal.this, ConfigView.class);
        startActivity(intent);
    }
    //Accion botn reporte financiero
    public void onClickFinanciero(View v){
        Intent intent = new Intent(MenuPrincipal.this, DashboardFinanciero.class);
        startActivity(intent);
    }
    //Accion boton historial cortes
    public void onClickHistorialCortes(View v){
        Intent intent = new Intent(MenuPrincipal.this, CorteView.class);
        startActivity(intent);
    }

    //Rellenado de datos de los productos
    private void rellenarDatos(){

        //Contador de ganancias
        int gananciasTotales = iVentas.getGanancias();
        String ganancia = "$ "+gananciasTotales;
        co = findViewById(R.id.unidadesTotalContador);
        co.setText(ganancia);
        if (gananciasTotales>0) co.setTextColor(Color.parseColor("#008000"));
        else if (gananciasTotales<0) co.setTextColor(Color.parseColor("#FF0000"));

        //Producto mas vendido
        String productoEstrella = iVentas.getProductoMasVendido();
        co = findViewById(R.id.vendidosTotalContador);
        co.setText(String.valueOf(productoEstrella));

        //contador de dinero en caja
        int dineroEnCaja = iVentas.getSaldoTotal();
        co = findViewById(R.id.productosTotalContador);
        co.setText(String.valueOf("$ "+dineroEnCaja));

        Configuracion configuracion = new ConfiguracionControl(this).getConfiguracion();
        int stockMinimo = configuracion.getStockMinimo();
        //Contador de stock bajo
        int contadorStockBajos= iProductos.getStockBajo(stockMinimo);
        co = findViewById(R.id.stockTotalContador);
        if (contadorStockBajos>0) co.setTextColor(Color.parseColor("#FF0000"));
        else co.setTextColor(Color.parseColor("#008000"));
        co.setText(String.valueOf(contadorStockBajos));

    }

    @Override
    protected void onResume() {
        super.onResume();
        rellenarDatos();
    }
}