package com.Clover.prueba.ClientesView;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Clover.prueba.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import BD.Controller.ControllerClient;
import BD.DAOs.ClientesDAO;
import Entidades.Clientes;

public class clientes_formulario extends AppCompatActivity {
    ControllerClient controller = new ClientesDAO(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.clientes_formulario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button agregar = findViewById(R.id.CP_agregarBoton);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarCleinte();
            }
        });
        //Cuando se edita un cliente
        Clientes cliente = (Clientes) getIntent().getSerializableExtra("cliente");
        if (cliente!=null){
            actualizarCliente(cliente);
        }
        //Funcion boton eliminar
        Button eliminar = findViewById(R.id.CP_buttonEliminar);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controller.deleteClient(cliente)) finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void actualizarCliente(Clientes cliente) {
        TextInputEditText nombre = findViewById(R.id.CP_nombreTXT);
        nombre.setText(cliente.getNombre_cliente());
        TextInputEditText apodo = findViewById(R.id.CP_apodoTXT);
        apodo.setText(cliente.getApodo());
        TextInputEditText saldo = findViewById(R.id.CP_saldoTXT);
        saldo.setVisibility(VISIBLE);
        saldo.setText(String.valueOf(cliente.getSaldo()));
        TextInputEditText direccion = findViewById(R.id.CP_direccionTXT);
        direccion.setText(cliente.getDireccion());
        TextInputEditText telefono = findViewById(R.id.CP_telefonoTXT);
        telefono.setText(cliente.getId_cliente());
        TextInputEditText puntos = findViewById(R.id.CP_puntosTXT);
        puntos.setVisibility(VISIBLE);
        puntos.setText(String.valueOf(cliente.getPuntos()));
        TextView saldoL = findViewById(R.id.CP_saldoLabel);
        TextView puntosL = findViewById(R.id.CP_puntosLabel);
        saldoL.setVisibility(VISIBLE);
        puntosL.setVisibility(VISIBLE);
        Button historial = findViewById(R.id.CP_buttinHistorialVentas);
        historial.setVisibility(VISIBLE);
        Button abonos = findViewById(R.id.CP_buttonAbonos);
        abonos.setVisibility(VISIBLE);
        Button eliminar = findViewById(R.id.CP_buttonEliminar);
        eliminar.setVisibility(VISIBLE);
        Button agregar = findViewById(R.id.CP_agregarBoton);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clientes clienteNew = getClienteOfInputs();
                if (controller.updateClient(cliente, clienteNew)) {
                    finish();
                } else {
                    Log.e("Clover_App", "Error al actualizar cliente");
                }

            }
        });
        agregar.setText("Actualizar");
        TextView titulo = findViewById(R.id.CP_tituloLabel);
        titulo.setText("Actualizar Cliente");
    }

    public void agregarCleinte(){
        Clientes cliente = getClienteOfInputs();;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.getDefault());
        String fecha = LocalDateTime.now().format(format);
        cliente.setFecha_registro(fecha);
        cliente.setPuntos(0);
        cliente.setSaldo(0);
        controller.addClient(cliente);
        finish();
    }
    private Clientes getClienteOfInputs(){
        Clientes cliente = new Clientes();
        TextInputEditText nombre = findViewById(R.id.CP_nombreTXT);
        cliente.setNombre_cliente(nombre.getText().toString());
        TextInputEditText apodo = findViewById(R.id.CP_apodoTXT);
        cliente.setApodo(apodo.getText().toString());
        TextInputEditText saldo = findViewById(R.id.CP_saldoTXT);
        if (saldo.getVisibility() == VISIBLE) {
            cliente.setSaldo(Integer.parseInt(saldo.getText().toString()));
            TextInputEditText puntos = findViewById(R.id.CP_puntosTXT);
            cliente.setPuntos(Integer.parseInt(puntos.getText().toString()));

        }
        TextInputEditText direccion = findViewById(R.id.CP_direccionTXT);
        cliente.setDireccion(direccion.getText().toString());
        TextInputEditText telefono = findViewById(R.id.CP_telefonoTXT);
        cliente.setId_cliente(telefono.getText().toString());
        Log.e("Clover_App", "getClienteOfInputs: "+cliente.toString());
        return cliente;
    }
}