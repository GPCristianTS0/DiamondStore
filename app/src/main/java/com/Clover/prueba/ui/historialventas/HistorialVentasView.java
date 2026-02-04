package com.Clover.prueba.ui.historialventas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.Clover.prueba.data.dao.VentasDAO;
import com.Clover.prueba.data.dao.interfaces.IVentas;
import com.Clover.prueba.data.models.Ventas;

public class HistorialVentasView extends AppCompatActivity {

    private final IVentas iVentas = new VentasDAO(this);
    private String mes, year, busqueda = ""; // Inicializar busqueda vacía
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private ArrayList<String> anios;
    private RecyclerView recyclerView; // Variable global
    private Spinner spinner, spinner2;
    private final String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.historialventas_principal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. CONFIGURACIÓN ÚNICA DEL RECYCLERVIEW
        recyclerView = findViewById(R.id.recyclerVentasView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Optimizaciones de velocidad
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemAnimator(null); // Quita la animación de entrada (hace que se sienta más rápido)

        // Listener busqueda
        TextInputEditText busquedaInput = findViewById(R.id.busquedaInputo);
        busquedaInput.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                busqueda = s.toString();
                cargarVentasAsync(mes, year, busqueda);
            }
        });

        // Arrancamos la carga pesada
        inicializarPantallaAsync();
    }

    private void inicializarPantallaAsync() {
        executor.execute(() -> {
            // --- HILO SECUNDARIO (BACKGROUND) ---

            // 1. Cargar Años
            anios = iVentas.getAnios();
            if (anios == null) anios = new ArrayList<>();
            if (anios.isEmpty()) {
                anios.add(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            }

            // 2. Calcular Fechas Iniciales
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES"));
            String mesNombreActual = capitalizar(LocalDateTime.now().format(format));
            String yearActual = String.valueOf(LocalDateTime.now().getYear());

            if (!anios.contains(yearActual)) {
                yearActual = anios.get(0);
            }

            int indexMes = new ArrayList<>(Arrays.asList(meses)).indexOf(mesNombreActual);
            String mesNumeroInicial = String.format("%02d", indexMes + 1);

            // 3. CARGAR VENTAS AQUÍ MISMO (Sin esperar a la UI)
            ArrayList<Ventas> ventasIniciales = iVentas.getVentas(mesNumeroInicial, yearActual, "");

            // Variables finales para el handler
            String finalYear = yearActual;
            String finalMesNum = mesNumeroInicial;
            String finalMesNombre = mesNombreActual;

            // --- HILO PRINCIPAL (UI) ---
            handler.post(() -> {
                // A. Actualizamos variables globales
                this.mes = finalMesNum;
                this.year = finalYear;

                // B. Configuramos Spinners
                rellenospiner(finalMesNombre, finalYear);

                // C. Pintamos la lista DE GOLPE
                rellenarScroll(ventasIniciales);
            });
        });
    }

    // Método para pintar la lista
    private void rellenarScroll(ArrayList<Ventas> ventas){
        HistorialVentasAdapter adapter = new HistorialVentasAdapter(ventas, (venta, position) -> {
            Intent intent = new Intent(HistorialVentasView.this, HistorialVentasDetalleVenta.class);
            intent.putExtra("venta", venta);
            startActivity(intent);
        });

        // 🚨 CORRECCIÓN: Comentamos esta línea porque causaba que los datos se vieran revueltos
        // si no tienes configurado el 'getItemId' en el Adaptador.
        // adapter.setHasStableIds(true);

        recyclerView.setAdapter(adapter);
    }

    private void rellenospiner(String mesD, String yearD){
        spinner = findViewById(R.id.spinner3);
        ArrayList<String> mesese = new ArrayList<>(Arrays.asList(meses));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.productos_spiner_item, mesese);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);

        // Selección inicial sin disparar listener
        spinner.setSelection(mesese.indexOf(mesD), false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Dmes = String.format("%02d", position+1);
                if(!Dmes.equals(mes)) {
                    mes = Dmes;
                    cargarVentasAsync(mes, year, busqueda);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Spinner Año
        spinner2 = findViewById(R.id.spinner4);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.productos_spiner_item, anios);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        if(anios.contains(yearD)) {
            spinner2.setSelection(anios.indexOf(yearD), false);
        }

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nuevoyear = anios.get(position);
                if (!nuevoyear.equals(year)) {
                    year = nuevoyear;
                    cargarVentasAsync(mes, year, busqueda);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void cargarVentasAsync(String mes, String year, String busqueda) {
        executor.execute(() -> {
            // Hilo secundario
            ArrayList<Ventas> misVentas = iVentas.getVentas(mes, year, busqueda);

            // Hilo principal
            handler.post(() -> {
                rellenarScroll(misVentas);
            });
        });
    }

    private String capitalizar(String string){
        if (string == null || string.isEmpty()) return "";
        return string.substring(0, 1).toUpperCase()+string.substring(1);
    }
}