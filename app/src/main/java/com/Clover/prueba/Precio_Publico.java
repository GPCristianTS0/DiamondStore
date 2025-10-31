package com.Clover.prueba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Precio_Publico extends AppCompatActivity {
    private Button botonGenerar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_precio_publico);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //botonGenerar = findViewById(R.id.sacarBtn);
        //botonGenerar.setOnClickListener(new Listener());
    }
    /*class Listener implements View.OnClickListener{
        @Override*/
        public void onClicke(View v) {
            TextView costoTotal = findViewById(R.id.costoGeneratxt);
            TextView costoProducto = findViewById(R.id.costoProductotxt);
            TextView ganancia = findViewById(R.id.gananciatxt);
            TextView costoEnvio = findViewById(R.id.costoEnviotxt);
            TextView unidades = findViewById(R.id.unidadestxt);
            double precioFinal = sacarPrecio(Double.parseDouble(costoTotal.getText().toString()), Double.parseDouble(costoProducto.getText().toString()), Double.parseDouble( ganancia.getText().toString()), Double.parseDouble(costoEnvio.getText().toString()),Double.parseDouble(unidades.getText().toString()));

            TextView costoTotallbl = findViewById(R.id.precioFinal2);
            costoTotallbl.setText(String.valueOf(precioFinal));
        }
    /*}*/
    public double sacarPrecio(double costoTotal, double costoTotalProducto, double ganancia, double costoEnvio, double unidades){
        double porcentajeDeCosto = (costoTotalProducto/costoTotal)*100;
        double parteEnvio = (costoEnvio * porcentajeDeCosto)/100;
        double costoPorUnidad = costoTotalProducto /unidades;
        parteEnvio = parteEnvio/unidades;
        double precioBruto = costoPorUnidad+parteEnvio;
        double precioFinal = precioBruto/(1-(ganancia/100));
        return precioFinal;
    }
}