package com.Clover.prueba.ui.clientes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.ControllerClientes;
import com.Clover.prueba.data.dto.ProductoMasCompradoDTO;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.ui.credito.CreditoDarAbono;
import com.Clover.prueba.utils.GeneradorQR;

import java.util.ArrayList;

public class ClientesPerfil extends AppCompatActivity {
    private ControllerClientes controller;
    private Clientes clientes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.clientes_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        controller = new ControllerClientes(this);
        clientes = getIntent().getSerializableExtra("cliente", Clientes.class);
        if (clientes!=null) {
            bindDatos();
            bindContadores(clientes.getId_cliente());
            bindProductosMasComprados(controller.getMasComprados(clientes.getId_cliente(), 3));
            functionsButons();
        }
    }
    private void bindDatos(){
        ImageView imagen = findViewById(R.id.PC_imgQRPrincipal);
        TextView nombre = findViewById(R.id.PC_txtNombre);
        TextView apodo = findViewById(R.id.PC_txtApodo);
        TextView telefono = findViewById(R.id.PC_txtTelefono);
        TextView direccion = findViewById(R.id.PC_txtDireccion);
        TextView puntos = findViewById(R.id.PC_txtPuntos);
        TextView saldo = findViewById(R.id.PC_txtDeuda);

        String importes = "$ ";
        //Agrega los datos del cliente
        GeneradorQR qr = new GeneradorQR(this);
        Bitmap bitmap = qr.generarQR(clientes.getId_cliente());
        imagen.setImageBitmap(bitmap);
        nombre.setText(clientes.getNombre_cliente());
        apodo.setText(clientes.getApodo());
        telefono.setText(formatoNumeroTelefonico(clientes.getId_cliente()));
        direccion.setText(clientes.getDireccion());
        puntos.setText(String.valueOf(clientes.getPuntos()));

        //Estaod de cuenta
        double saldoPendiente = controller.getSaldoPendiente(clientes.getId_cliente());
        if (saldoPendiente<=0){
            saldo.setText(importes.concat("0.00"));
            saldo.setTextColor(Color.parseColor("#4CAF50"));
        }else
            saldo.setText(importes.concat("-"+saldoPendiente));

    }
    //Rellena los contadores
    /**Rellena la card de top 3 productos mas comprados
     *
    * @param masComprados Lista de productos mas comprados segun el cliente
     *
    * */
    private void bindProductosMasComprados(ArrayList<ProductoMasCompradoDTO> masComprados){//Validacion
        LinearLayout sinDatos = findViewById(R.id.PC_layoutSinDatosProductos);
        LinearLayout layoutMasComprados = findViewById(R.id.PC_layoutMasComprados);
        if (masComprados==null || masComprados.isEmpty()){
            sinDatos.setVisibility(LinearLayout.VISIBLE);
            layoutMasComprados.setVisibility(LinearLayout.GONE);
            return;
        }
        //Inicializar variables
        TextView prod1 = findViewById(R.id.PC_txtProd1);
        TextView prod2 = findViewById(R.id.PC_txtProd2);
        TextView prod3 = findViewById(R.id.PC_txtProd3);
        TextView cant1 = findViewById(R.id.PC_cantProd1);
        TextView cant2 = findViewById(R.id.PC_cantProd2);
        TextView cant3 = findViewById(R.id.PC_cantProd3);
        ImageView img1 = findViewById(R.id.PC_imgProd1);
        ImageView img2 = findViewById(R.id.PC_imgProd2);
        ImageView img3 = findViewById(R.id.PC_imgProd3);

        //Rellenar
        ProductoMasCompradoDTO productoMasComprado;
        int total = masComprados.size();
        if (total>0){
            productoMasComprado = masComprados.get(0);
            prod1.setText(productoMasComprado.getNombre());
            cant1.setText(String.valueOf(productoMasComprado.getCantidad()));
            img1.setImageURI(Uri.parse(productoMasComprado.getRuta()));
        }
        if (total>1){
            productoMasComprado = masComprados.get(1);
            img2.setImageURI(Uri.parse(productoMasComprado.getRuta()));
            prod2.setText(productoMasComprado.getNombre());
            cant2.setText(String.valueOf(productoMasComprado.getCantidad()));
        }
        if (total>2) {
            productoMasComprado = masComprados.get(2);
            prod3.setText(productoMasComprado.getNombre());
            cant3.setText(String.valueOf(productoMasComprado.getCantidad()));
            img3.setImageURI(Uri.parse(productoMasComprado.getRuta()));
        }
    }
    private String formatoNumeroTelefonico(String telefono){
        String numero = "";
        if (telefono.length()==10){
            numero = telefono.substring(0, 3)+"-"+telefono.substring(3, 6)+"-"+telefono.substring(6, 10);
            return numero;
        }
        return numero;
    }
    private void bindContadores(String idCliente){
        TextView ventas = findViewById(R.id.PC_txtVentasTotales);
        TextView ticket = findViewById(R.id.PC_txtTicketPromedio);
        TextView fecha = findViewById(R.id.PC_txtFechaUltimoPago);

        int ventasTotales = controller.getVentasTotales(idCliente);
        String ticketPromedio = controller.getTicketPromedio(idCliente);
        String fechaUltimoPago = controller.getUltimoAbono(idCliente);

        ventas.setText(String.valueOf(ventasTotales));
        ticket.setText(String.valueOf(ticketPromedio));
        fecha.setText(fechaUltimoPago);


    }
    private void functionsButons(){
        //Inicializar botones
        Button verVentas = findViewById(R.id.PC_btnVerVentas);
        Button verAbonos = findViewById(R.id.PC_btnVerAbonos);
        Button editarInfo = findViewById(R.id.PC_btnEditarInfo);
        Button abonarRapido = findViewById(R.id.PC_btnAbonarRapido);
        LinearLayout btnCompartir = findViewById(R.id.PC_layoutCompartir);


        //Funciones de botones
        verVentas.setOnClickListener(v -> {
            //Intent intent = new Intent(this, ClientesVentas.class);
            //startActivity(intent);
        });
        verAbonos.setOnClickListener(v -> {
            Intent intent = new Intent(ClientesPerfil.this, ClientesDetalleAbonos.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("cliente",clientes);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        editarInfo.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientesFormulario.class);
            intent.putExtra("cliente", clientes);
            startActivity(intent);
        });
        abonarRapido.setOnClickListener(v -> {
            if(clientes.getSaldo()<=0){
                Toast.makeText(this, "No tiene Deudas", Toast.LENGTH_SHORT).show();
                return;
            }
            CreditoDarAbono darAbono = new CreditoDarAbono();
            darAbono.setListener(() -> {
                clientes = controller.getClientes(clientes.getId_cliente());
                bindDatos();
            });
            Bundle bundle = new Bundle();
            bundle.putString("idCliente", clientes.getId_cliente());
            darAbono.setArguments(bundle);
            darAbono.show(getSupportFragmentManager(), "CreditoDarAbono");
        });
        btnCompartir.setOnClickListener(v -> {
            GeneradorQR generadorQR = new GeneradorQR(this);
            generadorQR.compartirQR(this, clientes);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Clientes cliente = controller.getClientes(clientes.getId_cliente());
        if (cliente!=null){
            clientes = cliente;
            bindDatos();
        }
    }
}