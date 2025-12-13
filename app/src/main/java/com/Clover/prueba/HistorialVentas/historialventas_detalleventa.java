package com.Clover.prueba.HistorialVentas;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import BD.DAOs.VentasDAO;
import BD.Controller.ControllerVentas;
import Entidades.DetalleVenta;
import Entidades.Ventas;

public class historialventas_detalleventa extends AppCompatActivity {
    private ControllerVentas controllerVentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.historialventas_detalleventa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        controllerVentas = new VentasDAO(this);


        Ventas venta = (Ventas) getIntent().getSerializableExtra("venta");
        if (venta!=null){
            rellenarInformacion(venta);
            rellenoProductosj(venta);
        }

    }
    private void rellenarInformacion(Ventas venta){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault());
        String fechaf = venta.getFecha_hora();
        LocalDateTime fechaHora = LocalDateTime.parse(fechaf, formatter);

        TextView fecha,hora, monto, totalPiezas, idCliente;
        fecha = findViewById(R.id.hvD_fechaOut);
        hora = findViewById(R.id.hvD_horaOut);
        monto = findViewById(R.id.hvD_TotalOut);
        totalPiezas = findViewById(R.id.hvD_noArticulos);
        idCliente = findViewById(R.id.hvD_apodoClienteOut);
        fecha.setText(String.valueOf(fechaHora.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        hora.setText(String.valueOf(fechaHora.format(DateTimeFormatter.ofPattern("HH:mm"))));
        monto.setText(String.valueOf("$ "+venta.getMonto()));
        totalPiezas.setText(String.valueOf(venta.getTotal_piezas()));
        idCliente.setText("0");
    }
    //rellenar detalle venta productos
    private void rellenoProductosj(Ventas venta){
        detallesVentaAdapter adapter;
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.hvD_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<DetalleVenta> detalleVentas = controllerVentas.getDetalleVentas(venta.getId_venta());
        adapter = new detallesVentaAdapter(this, detalleVentas);

        recyclerView.setAdapter(adapter);

    }

}