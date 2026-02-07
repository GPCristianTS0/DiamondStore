package com.Clover.prueba.ui.historialventas;

import static com.Clover.prueba.utils.Constantes.CONST_METODO_CREDITO;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_EFECTIVO;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_TARJETA;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_TRANSFERENCIA;
import static com.Clover.prueba.utils.Constantes.VENTA_PAGADA;
import static com.Clover.prueba.utils.Constantes.VENTA_PENDIENTE;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import com.Clover.prueba.data.dao.ConfiguracionDAO;
import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.dao.ClientesDAO;
import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.data.dao.interfaces.IVentas;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.DetalleVenta;
import com.Clover.prueba.data.models.Ventas;
import com.Clover.prueba.utils.Constantes;
import com.Clover.prueba.utils.TicketUtils;

public class HistorialVentasDetalleVenta extends AppCompatActivity {
    private IVentas iVentas;
    private IClient controller;
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
        controller = new ClientesDAO(this);

        Ventas venta = (Ventas) getIntent().getSerializableExtra("venta");
        Clientes cliente = controller.getClient(venta.getId_cliente());
        String nombreCliente;
        if (cliente==null){
            nombreCliente = "Publico General";
        }else
            nombreCliente = cliente.getNombre_cliente();
        //Rellenar informacion
        rellenarInformacion(venta, nombreCliente);
        rellenoProductosj(venta);

        Button btnCompartir = findViewById(R.id.HV_btnCompartir);
        btnCompartir.setOnClickListener(v -> {
            TicketUtils ticketUtils = new TicketUtils(this);
            ticketUtils.generarTicketVenta(this, nombreCliente, venta, iVentas.getDetalleVentas(venta.getId_venta()), false);
        });

    }
    private void rellenarInformacion(Ventas venta, String nombre_cliente){
        TextView fecha,hora, monto, totalPiezas, idCliente, status, folio;
        TextView metodoPago, datosTarjeta, txtRecibido, txtCambio;
        //Inicializacion
        status = findViewById(R.id.HV_statusVenta);
        folio = findViewById(R.id.HV_txtFolio);
        metodoPago = findViewById(R.id.HV_metodoPago);
        datosTarjeta = findViewById(R.id.HV_datosTarjeta);
        txtRecibido = findViewById(R.id.HV_txtRecibido);
        txtCambio = findViewById(R.id.HV_txtCambio);
        fecha = findViewById(R.id.ticketL_fechaOut);
        hora = findViewById(R.id.ticketL_horaOut);
        monto = findViewById(R.id.ticket_TotalOut);
        totalPiezas = findViewById(R.id.ticketL_noArticulos);
        idCliente = findViewById(R.id.ticketL_apodoClienteOut);

        //Relleno de información
        Configuracion configuracion = new ConfiguracionDAO(this).getConfiguracion();
        ImageView imagen = findViewById(R.id.HV_imagen);
            //Imagen
        if (configuracion.getRutaLogo() != null){
            imagen.setImageURI(Uri.parse(configuracion.getRutaLogo()));
        }
        String fechaf = venta.getFecha_hora();
        try {
            DateTimeFormatter inputFormatter;
            if (fechaf.length() > 16) {
                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            } else {
                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault());
            }

            LocalDateTime fechaHora = LocalDateTime.parse(fechaf, inputFormatter);

            fecha.setText(fechaHora.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())));
            hora.setText(fechaHora.format(DateTimeFormatter.ofPattern("HH:mm")));

        } catch (Exception e) {
            fecha.setText(venta.getFecha_hora());
            hora.setText("--:--");
            e.printStackTrace();
        }
        String importes = "$ ";
        monto.setText(importes.concat(""+venta.getMonto()));
        totalPiezas.setText(String.valueOf(venta.getTotal_piezas()));
        folio.setText("Folio: #"+venta.getId_venta());

        //Verifica si esta pagado o esta pendiente
        if (venta.getEstado().equals(VENTA_PAGADA)){
            status.setText("PAGADO");
        }else if (venta.getEstado().equals(VENTA_PENDIENTE)){
            status.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FFC107")));
            status.setTextColor(Color.parseColor("#FFC107"));
            status.setText("PENDIENTE");
        }

        //Metodo de pago datos
        String metodoPagoS = venta.getTipo_pago().toUpperCase();
        if (venta.getTipo_pago().equals(CONST_METODO_EFECTIVO)){
            datosTarjeta.setVisibility(View.GONE);
            metodoPago.setText(metodoPagoS);
            txtRecibido.setText(importes.concat(venta.getPago_con()+""));
            txtCambio.setText(importes.concat((venta.getPago_con()-venta.getMonto())+""));
        }else if (venta.getTipo_pago().equals(CONST_METODO_TARJETA)){
            metodoPago.setText(metodoPagoS.concat(" "+venta.getTipo_tarjeta().toUpperCase()));
            datosTarjeta.setText(venta.getBanco_tarjeta()+" ****"+venta.getNumero_tarjeta()+" Aprob:("+venta.getNumero_aprobacion()+")");
            txtRecibido.setText(importes.concat(venta.getMonto()+""));
            txtCambio.setText(importes.concat("0.00"));
        }else if(venta.getTipo_pago().equals(CONST_METODO_CREDITO)){
            DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
            LocalDate fechaLimite = LocalDate.parse(venta.getFecha_limite(), input);
            metodoPago.setText(metodoPagoS);
            String datos = "Fecha Proximo Pago: ";
            datos = datos.concat(fechaLimite.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault()))).concat("\n").concat(venta.getFrecuencia_pago());
            datosTarjeta.setText(datos);
            txtRecibido.setText(importes.concat("0.00"));
            txtCambio.setText(importes.concat("0.00"));
        } else if (venta.getTipo_pago().equals(CONST_METODO_TRANSFERENCIA)) {
            metodoPago.setText(metodoPagoS);
            datosTarjeta.setVisibility(View.GONE);
            txtRecibido.setText(importes.concat(venta.getMonto()+""));
            txtCambio.setText(importes.concat("0.00"));
        }
        //Datos del cliente
        if (nombre_cliente==null)
            idCliente.setText("Publico General");
        else
            idCliente.setText(nombre_cliente);
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