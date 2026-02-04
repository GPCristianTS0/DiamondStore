package com.Clover.prueba.ui.credito;

import android.content.Intent;
import android.net.Uri;
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
import com.Clover.prueba.data.controller.ControllerCredito;
import com.Clover.prueba.data.dao.ClientesDAO;
import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.dao.interfaces.IVentas;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.utils.AbrirAppExternas;

import java.util.ArrayList;

public class CreditoPrincipalView extends AppCompatActivity {
    private IClient controllerClientes;
    private ControllerCredito controllerCredito;
    private IVentas ventasDAO;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.credito_principal_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        controllerClientes = new ClientesDAO(this);
        controllerCredito = new ControllerCredito(this);
        ventasDAO = new VentasDAO(this);
        TextView txtTotal = findViewById(R.id.CPC_txtGranTotal);
        String importes = "$ ";
        txtTotal.setText(importes.concat(controllerCredito.getSaldoTotal()+""));
        rellenarDatos(controllerClientes.getDeudores());

    }
    private void rellenarDatos(ArrayList<Clientes> clientes){
        TextView txtClientes = findViewById(R.id.CPC_txtClientesCount);
        txtClientes.setText(clientes.size()+" Clientes con Deuda");
        AbrirAppExternas abrirAppExternas = new AbrirAppExternas();
        CreditoAdapterMain adapter = new CreditoAdapterMain(clientes, new CreditoAdapterMain.ListenerCreditoMain() {
            @Override
            public void onLlamar(Clientes cliente) {
                abrirAppExternas.abrirLlamada(CreditoPrincipalView.this, cliente.getId_cliente());
            }
            @Override
            public void onWhatsapp(Clientes cliente) {
                String telefono = cliente.getId_cliente();
                abrirAppExternas.abrirWhatsapp(CreditoPrincipalView.this, telefono,"");
            }
            @Override
            public void onAbonar(Clientes cliente) {
                CreditoDarAbono darAbono = new CreditoDarAbono();
                Bundle bundle = new Bundle();
                bundle.putString("idCliente", cliente.getId_cliente());
                darAbono.setArguments(bundle);
                darAbono.show(getSupportFragmentManager(), "CreditoDarAbono");
            }
        });
        RecyclerView recyclerView = findViewById(R.id.CPC_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}