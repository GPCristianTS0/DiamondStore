package com.Clover.prueba.ui.corte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.CorteCajaController;
import com.Clover.prueba.data.models.CorteCaja;

import java.util.ArrayList;
import java.util.Locale;

public class CorteView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.corte_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CorteCajaController corteCajaController = new CorteCajaController(this);
        ArrayList<CorteCaja> cortes = corteCajaController.getCortes();
        rellenarRecycler(cortes);
    }
    private void rellenarRecycler(ArrayList<CorteCaja> cortes) {
        RecyclerView recyclerView = findViewById(R.id.HC_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        AdapterCorte adapter = new AdapterCorte(cortes, position -> {
            abrirDialogTicket(cortes.get(position));
        });
        recyclerView.setAdapter(adapter);
    }
    private void abrirDialogTicket(CorteCaja corteCaja){
        CorteDetalleDialog corteDetalleDialog = new CorteDetalleDialog();
        Bundle args = new Bundle();
        args.putSerializable("corte", corteCaja);
        corteDetalleDialog.setArguments(args);
        corteDetalleDialog.show(getSupportFragmentManager(), "CorteDetalleDialog");

    }
}