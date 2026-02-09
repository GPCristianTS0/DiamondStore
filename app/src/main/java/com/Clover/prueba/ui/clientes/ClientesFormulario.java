package com.Clover.prueba.ui.clientes;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.dao.ClientesDAO;
import com.Clover.prueba.data.models.Clientes;
import com.google.android.material.textfield.TextInputLayout;

public class ClientesFormulario extends AppCompatActivity {
    IClient controller = new ClientesDAO(this);
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
        Clientes cliente = getIntent().getSerializableExtra("cliente", Clientes.class);
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
        TextInputEditText direccion = findViewById(R.id.CP_direccionTXT);
        direccion.setText(cliente.getDireccion());
        TextInputEditText telefono = findViewById(R.id.CP_telefonoTXT);
        telefono.setText(cliente.getId_cliente());
        TextInputLayout saldo = findViewById(R.id.CP_layoutSaldo);
        saldo.setVisibility(VISIBLE);
        saldo.getEditText().setText(String.valueOf(cliente.getSaldo()));
        TextInputLayout puntos = findViewById(R.id.CP_layoutPuntos);
        puntos.setVisibility(VISIBLE);
        puntos.getEditText().setText(String.valueOf(cliente.getPuntos()));
        LinearLayout container = findViewById(R.id.CP_accionesContainer);
        container.setVisibility(VISIBLE);
        Button agregar = findViewById(R.id.CP_agregarBoton);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clientes clienteNew = getClienteOfInputs();
                if (isClientInvalide(clienteNew)){
                    Toast.makeText(ClientesFormulario.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
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
        Clientes cliente = getClienteOfInputs();
        Log.e("Clover_App", "agregarCleinte: "+cliente.toString());
        if (isClientInvalide(cliente)){
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.getDefault());
        String fecha = LocalDateTime.now().format(format);
        cliente.setFecha_registro(fecha);
        cliente.setPuntos(0);
        cliente.setSaldo(0);
        Toast.makeText(this, cliente.getId_cliente(), Toast.LENGTH_SHORT).show();
        //controller.addClient(cliente);
        finish();
    }
    private boolean isClientInvalide(Clientes cliente){
        if (cliente.getId_cliente() == null||cliente.getNombre_cliente().isEmpty())
            return true;
        if (cliente.getApodo().isEmpty())
            return true;
        if (cliente.getDireccion().isEmpty())
            return true;
        return false;
    }
    private Clientes getClienteOfInputs(){
        Clientes cliente = new Clientes();
        TextInputEditText nombre = findViewById(R.id.CP_nombreTXT);
        cliente.setNombre_cliente(nombre.getText().toString());
        TextInputEditText apodo = findViewById(R.id.CP_apodoTXT);
        cliente.setApodo(apodo.getText().toString());
        LinearLayout saldo = findViewById(R.id.CP_accionesContainer);
        if (saldo.getVisibility() == VISIBLE) {
            TextInputEditText saldot = findViewById(R.id.CP_saldoTXT);
            TextInputEditText puntos = findViewById(R.id.CP_puntosTXT);
            if (saldot.getText().toString().isEmpty()||puntos.getText().toString().isEmpty()) {
                Toast.makeText(this, "Rellena los campos de saldo y puntos", Toast.LENGTH_SHORT).show();
            }else{
                cliente.setSaldo(Integer.parseInt(saldot.getText().toString()));
                cliente.setPuntos(Integer.parseInt(puntos.getText().toString()));
            }

        }
        TextInputEditText direccion = findViewById(R.id.CP_direccionTXT);
        cliente.setDireccion(direccion.getText().toString());
        TextInputEditText telefono = findViewById(R.id.CP_telefonoTXT);
        cliente.setId_cliente(telefono.getText().toString());
        Log.e("Clover_App", "getClienteOfInputs: "+cliente.toString());
        return cliente;
    }
}