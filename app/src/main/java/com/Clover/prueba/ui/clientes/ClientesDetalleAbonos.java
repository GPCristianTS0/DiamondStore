package com.Clover.prueba.ui.clientes;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.ControllerClientes;
import com.Clover.prueba.data.controller.ControllerCredito;
import com.Clover.prueba.data.dao.interfaces.IAbonos;
import com.Clover.prueba.data.models.Abonos;
import com.Clover.prueba.data.models.Clientes;

import java.util.ArrayList;

public class ClientesDetalleAbonos extends AppCompatActivity {
    private ControllerClientes controllerClientes;
    private ControllerCredito controllerCredito;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.clientes_abonos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        controllerClientes = new ControllerClientes(this);
        controllerCredito = new ControllerCredito(this);
        Clientes cliente = getIntent().getSerializableExtra("cliente", Clientes.class);
        if(cliente != null) {
            rellenarDatos(cliente);
            rellenarAbonos(controllerClientes.getAbonos(cliente.getId_cliente()));
        }
    }
    private void rellenarDatos(Clientes cliente){
        TextView txtNombre = findViewById(R.id.txtNombreCliente);
        TextView txtSaldo = findViewById(R.id.txtSaldoPendiente);
        TextView txtDeuda = findViewById(R.id.txtDeudaTotal);
        ProgressBar progressDeuda = findViewById(R.id.progressDeuda);

        double saldoPendiente = controllerClientes.getSaldoPendiente(cliente.getId_cliente());
        double saldoTotal = controllerClientes.getSaldoTotal(cliente.getId_cliente());
        txtNombre.setText(cliente.getNombre_cliente());
        txtSaldo.setText(String.valueOf(cliente.getSaldo()));
        txtDeuda.setText(String.valueOf(saldoTotal));
        if (saldoPendiente>0)
            progressDeuda.setProgress((int) ((saldoPendiente/saldoTotal)*100));
        else
            progressDeuda.setProgress(0);
    }
    private void rellenarAbonos(ArrayList<Abonos> abonos){
        DetalleAbonoAdapter adapter = new DetalleAbonoAdapter(abonos, new DetalleAbonoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                DetallesAbono detallesAbono = new DetallesAbono();
                Bundle args = new Bundle();
                args.putSerializable("abono", abonos.get(position));
                detallesAbono.setArguments(args);
                detallesAbono.show(getSupportFragmentManager(), "detallesAbono");
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerAbonos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        if (abonos.size()==0){
            findViewById(R.id.layoutVacio).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}