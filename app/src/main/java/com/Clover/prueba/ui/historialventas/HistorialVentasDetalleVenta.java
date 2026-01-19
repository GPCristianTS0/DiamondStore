package com.Clover.prueba.ui.historialventas;

import android.os.Bundle;
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

import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.dao.ClientesDAO;
import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.data.dao.interfaces.IVentas;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.DetalleVenta;
import com.Clover.prueba.data.models.Ventas;

public class HistorialVentasDetalleVenta extends AppCompatActivity {
    private IVentas iVentas;

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
        iVentas = new VentasDAO(this);


        Ventas venta = (Ventas) getIntent().getSerializableExtra("venta");
        if (venta!=null){
            rellenarInformacion(venta);
            rellenoProductosj(venta);
        }

    }
    private void rellenarInformacion(Ventas venta){
        TextView fecha,hora, monto, totalPiezas, idCliente;
        fecha = findViewById(R.id.ticketL_fechaOut);
        hora = findViewById(R.id.ticketL_horaOut);
        try {
            String fechaf = venta.getFecha_hora();
            DateTimeFormatter inputFormatter;
            if (fechaf.length() > 16) {
                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            } else {
                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault());
            }

            LocalDateTime fechaHora = LocalDateTime.parse(fechaf, inputFormatter);

            fecha.setText(fechaHora.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            hora.setText(fechaHora.format(DateTimeFormatter.ofPattern("HH:mm")));

        } catch (Exception e) {
            fecha.setText(venta.getFecha_hora());
            hora.setText("--:--");
            e.printStackTrace();
        }

        monto = findViewById(R.id.ticket_TotalOut);
        totalPiezas = findViewById(R.id.ticketL_noArticulos);
        idCliente = findViewById(R.id.ticketL_apodoClienteOut);
        monto.setText(String.valueOf("$ "+venta.getMonto()));
        totalPiezas.setText(String.valueOf(venta.getTotal_piezas()));
        IClient controller = new ClientesDAO(this);
        Clientes cliente = controller.getClient(venta.getId_cliente());
        idCliente.setText(cliente.getNombre_cliente());
    }
    //rellenar detalle venta productos
    private void rellenoProductosj(Ventas venta){
        DetallesVentaAdapter adapter;
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.hvD_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<DetalleVenta> detalleVentas = iVentas.getDetalleVentas(venta.getId_venta());
        adapter = new DetallesVentaAdapter(this, detalleVentas);

        recyclerView.setAdapter(adapter);

    }

}