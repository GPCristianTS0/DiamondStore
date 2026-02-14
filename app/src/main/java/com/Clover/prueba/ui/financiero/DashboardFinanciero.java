package com.Clover.prueba.ui.financiero;

import static com.Clover.prueba.domain.reportes.FiltroRangoFechas.CONST_ESTA_SEMANA;
import static com.Clover.prueba.domain.reportes.FiltroRangoFechas.CONST_ESTE_DIA;
import static com.Clover.prueba.domain.reportes.FiltroRangoFechas.CONST_ESTE_MES;
import static com.Clover.prueba.domain.reportes.FiltroRangoFechas.CONST_THIS_YEAR;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Clover.prueba.R;
import com.Clover.prueba.domain.reportes.ControllerFinanciero;
import com.Clover.prueba.data.models.ReporteFinanciero;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardFinanciero extends AppCompatActivity {
    // 1. Declaramos las vistas aquí para no buscarlas a cada rato
    private TextView txtVentasTotales, txtTicketPromedio, txtCostos, txtNumVentas;
    private TextView txtUtilidad, txtTotalEfectivo, txtTotalTarjeta;
    private Spinner spinner;
    private ControllerFinanciero controllerFinanciero;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dashboard_financiero);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        controllerFinanciero = new ControllerFinanciero(this);
        rellenarSpinner();
        inicializarVistas();
    }
    private void rellenarSpinner(){
        String[] rangos = {CONST_ESTA_SEMANA, CONST_ESTE_MES,CONST_ESTE_DIA, CONST_THIS_YEAR};
        Spinner spinner = findViewById(R.id.RF_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.productos_spiner_item, rangos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String rango = rangos[position];
                cargarDatos(rango);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void cargarDatos(String rango){
        ReporteFinanciero reporte = controllerFinanciero.getReporteFinanciero(rango);
        pintarGrafica(reporte.getDatosGrafica());
        setReporte(reporte);
    }
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setReporte(ReporteFinanciero reporte){txtVentasTotales.setText(String.format("$ %.2f", reporte.getVentasTotales()));
        txtTicketPromedio.setText(String.format("$ %.2f", reporte.getTicketPromedio()));
        txtCostos.setText(String.format("$ %.2f", reporte.getCostoMercancia()));

        txtNumVentas.setText(String.valueOf(reporte.getCantidadVentas()));

        txtUtilidad.setText(String.format("$ %.2f", reporte.getGanancias()));
        txtTotalEfectivo.setText(String.format("$ %.2f", reporte.getTotalEfectivo()));
        txtTotalTarjeta.setText(String.format("$ %.2f", reporte.getTotalTarjeta()));
        if (reporte.getGanancias() > 0){
            txtUtilidad.setTextColor(Color.parseColor("#388E3C"));
        }else{
            txtUtilidad.setTextColor(Color.parseColor("#D32F2F"));
        }

    }
    private void inicializarVistas(){
        spinner = findViewById(R.id.RF_spinner);
        txtVentasTotales = findViewById(R.id.RF_txtVentasTotales);
        txtTicketPromedio = findViewById(R.id.RF_txtTicketPromedio);
        txtCostos = findViewById(R.id.RF_txtCostos);
        txtNumVentas = findViewById(R.id.RF_txtNumVentas);
        txtUtilidad = findViewById(R.id.RF_txtUtilidad);
        txtTotalEfectivo = findViewById(R.id.RF_txtTotalEfectivo);
        txtTotalTarjeta = findViewById(R.id.RF_txtTotalTarjeta);
        barChart = findViewById(R.id.barChart);
    }
    private void pintarGrafica(LinkedHashMap<String, Double> datos) {
        if (datos == null || datos.isEmpty()) {
            barChart.clear();
            barChart.setNoDataText("Sin datos para este periodo");
            return;
        }

        ArrayList<BarEntry> entradas = new ArrayList<>();
        ArrayList<String> etiquetas = new ArrayList<>();

        int i = 0;
        for (Map.Entry<String, Double> entry : datos.entrySet()) {
            entradas.add(new BarEntry(i, entry.getValue().floatValue()));

            String etiquetaBonita = getString(entry);

            etiquetas.add(etiquetaBonita);
            i++;
        }

        // ... (El resto del código de configuración del BarDataSet es igual) ...
        BarDataSet dataSet = new BarDataSet(entradas, "Ventas");
        // ... setColors, setSizes ...

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(etiquetas));
        // ... resto de config ...

        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

    private String getString(Map.Entry<String, Double> entry) {
        String rawKey = entry.getKey();
        String etiquetaBonita = rawKey;

        // 1. Si es hora (longitud 2, ej: "09", "14")
        if (rawKey.length() == 2) {
            etiquetaBonita = rawKey + ":00";
        }
        // 2. Si es Mes (longitud 7, ej: "2026-01")
        else if (rawKey.length() == 7) {
            String mesNum = rawKey.substring(5); // "01"
            etiquetaBonita = obtenerNombreMes(mesNum); // "Ene"
        }
        // 3. Si es Día (longitud 10, ej: "2026-01-25")
        else if (rawKey.length() == 10) {
            etiquetaBonita = rawKey.substring(8); // Solo el día: "25"
        }
        return etiquetaBonita;
    }

    private String obtenerNombreMes(String numeroMes) {
        switch (numeroMes) {
            case "01": return "Ene";
            case "02": return "Feb";
            case "03": return "Mar";
            case "04": return "Abr";
            case "05": return "May";
            case "06": return "Jun";
            case "07": return "Jul";
            case "08": return "Ago";
            case "09": return "Sep";
            case "10": return "Oct";
            case "11": return "Nov";
            case "12": return "Dic";
            default: return numeroMes;
        }
    }
}