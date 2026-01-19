package com.Clover.prueba.ui.ventas;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.CorteCajaController;
import com.Clover.prueba.data.models.CorteCaja;
import com.google.android.material.textfield.TextInputEditText;

public class VentasCerrarCorte extends AppCompatActivity {
    private CorteCajaController corteCajaController;
    private String dineroEnCaja;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ventas_corte_cierre);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        corteCajaController = new CorteCajaController(this);
        rellenarDatos(corteCajaController.getCorteActual());
        input();
        Button boton = findViewById(R.id.VCC_BtnCerrarTurno);
        boton.setOnClickListener(v -> {
            if (dineroEnCaja.isEmpty()){
                Toast.makeText(this, "Ingrese un valor", Toast.LENGTH_SHORT).show();
                return;
            }
            if (corteCajaController.cerrarTurno(Double.parseDouble(dineroEnCaja))){
                Toast.makeText(this, "Turno cerrado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al cerrar turno", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void rellenarDatos(CorteCaja corteCaja){
        TextView co = findViewById(R.id.VCC_ValorFondo);
        co.setText(String.valueOf("+$"+corteCaja.getMonto_inicial()));
        co = findViewById(R.id.VCC_ValorVentas);
        co.setText(String.valueOf("+$"+corteCaja.getVentas_totales()));
        co = findViewById(R.id.VCC_ValorAbonos);
        co.setText(String.valueOf("+$"+corteCaja.getAbonos_totales()));
        co = findViewById(R.id.VCC_ValorGastos);
        co.setText(String.valueOf("-$"+corteCaja.getGastos_totales()));
        co = findViewById(R.id.VCC_ValorDiferencia);
        co.setText(String.valueOf(corteCaja.getDiferencia()));
        co = findViewById(R.id.VCC_ValorTotalSistema);
        co.setText(String.valueOf("$ "+corteCaja.getDinero_en_caja()));
        co = findViewById(R.id.VCC_ValorGastosTrans);
        co.setText(String.valueOf("-$"+corteCajaController.getVentasTotalesCorte(corteCaja.getId_corte(), "Transferencia")));
    }
    private void input(){
        TextView diferencia = findViewById(R.id.VCC_ValorDiferencia);
        TextInputEditText input = findViewById(R.id.VCC_InputDineroFisico);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    diferencia.setText("$ 0.0");
                    diferencia.setTextColor(Color.parseColor("#008000"));
                    return;
                }
                double diferenciaa = corteCajaController.calcularDiferencia(Double.parseDouble(s.toString()));
                if (diferenciaa>=0){
                    diferencia.setTextColor(Color.parseColor("#008000"));
                }else if (diferenciaa<0){
                    diferencia.setTextColor(Color.parseColor("#FF0000"));
                }
                diferencia.setText(String.valueOf("$ "+diferenciaa));
                corteCajaController.setDiferencia(diferenciaa);
                dineroEnCaja = s.toString();
            }
        });
    }
}