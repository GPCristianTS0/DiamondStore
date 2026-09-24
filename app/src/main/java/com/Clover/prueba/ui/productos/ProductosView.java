package com.Clover.prueba.ui.productos;

import android.content.Intent;
import android.os.Bundle;
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
import com.Clover.prueba.data.dao.ProductoDAO;
import com.Clover.prueba.data.dao.interfaces.IProducto;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.domain.productos.usecase.GetProductBy;
import com.Clover.prueba.domain.productos.usecase.GetProductos;
import com.Clover.prueba.domain.productos.usecase.GetSeccionesUseCase;
import com.Clover.prueba.domain.productos.viewmodel.BusquedaViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ProductosView extends AppCompatActivity {
    private int seccionG;
    private String columnaObtencionG;
    private BusquedaViewModel viewModel;
    private TextInputEditText t;
    private ProductosViewAdapter adapter;

    private Chip chipTodo;
    private Chip chipRefaccion;
    private Chip chipVenta;
    private Chip chipDisponible;
    private List<Productos> listaProductosOriginal = new ArrayList<>();
    private boolean isUpdatingChips = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.productos_view_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Repositorio
        IProducto productoDAO = new ProductoDAO(this);
        // UseCase
        GetSeccionesUseCase seccionesUseCase = new GetSeccionesUseCase(productoDAO);
        GetProductBy productosByUseCase = new GetProductBy(productoDAO);
        GetProductos productosUseCase = new GetProductos(productoDAO);
        viewModel = new BusquedaViewModel(seccionesUseCase, productosByUseCase, productosUseCase);

        viewModel.productos.observe(this, productos -> {
            listaProductosOriginal = productos != null ? productos : new ArrayList<>();
            aplicarFiltrosChips();
        });

        seccionG = 0;
        t = findViewById(R.id.textInputEditText);
        rellenarTabla(new ArrayList<>());
        setupChips();
        rellenarSpinnerSecciones();
        rellenarSpinnerColumnas();
        inputBusqueda();
    }

    private void setupChips() {
        chipTodo = findViewById(R.id.chipTodo);
        chipRefaccion = findViewById(R.id.chipRefaccion);
        chipVenta = findViewById(R.id.chipVenta);
        chipDisponible = findViewById(R.id.chipDisponible);

        if (chipTodo == null || chipRefaccion == null || chipVenta == null || chipDisponible == null) {
            return;
        }

        isUpdatingChips = true;
        chipTodo.setChecked(true);
        chipDisponible.setChecked(true);
        isUpdatingChips = false;

        chipTodo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isUpdatingChips) return;
            if (isChecked) {
                isUpdatingChips = true;
                chipRefaccion.setChecked(false);
                chipVenta.setChecked(false);
                isUpdatingChips = false;
            }
            aplicarFiltrosChips();
        });

        chipRefaccion.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isUpdatingChips) return;
            if (isChecked) {
                isUpdatingChips = true;
                chipVenta.setChecked(false);
                chipTodo.setChecked(false);
                isUpdatingChips = false;
            }
            aplicarFiltrosChips();
        });

        chipVenta.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isUpdatingChips) return;
            if (isChecked) {
                isUpdatingChips = true;
                chipRefaccion.setChecked(false);
                chipTodo.setChecked(false);
                isUpdatingChips = false;
            }
            aplicarFiltrosChips();
        });

        chipDisponible.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isUpdatingChips) return;
            aplicarFiltrosChips();
        });
    }

    private void aplicarFiltrosChips() {
        if (listaProductosOriginal == null) return;

        boolean filtroRefaccion = chipRefaccion != null && chipRefaccion.isChecked();
        boolean filtroVenta = chipVenta != null && chipVenta.isChecked();
        boolean filtroDisponible = chipDisponible != null && chipDisponible.isChecked();

        List<Productos> filtrados = new ArrayList<>();
        for (Productos p : listaProductosOriginal) {
            boolean cumpleTipo = true;
            if (filtroRefaccion) {
                cumpleTipo = p.isRefaccion();
            } else if (filtroVenta) {
                cumpleTipo = !p.isRefaccion();
            }

            boolean cumpleDisponible = true;
            if (filtroDisponible) {
                cumpleDisponible = p.getStock() > 0;
            }

            if (cumpleTipo && cumpleDisponible) {
                filtrados.add(p);
            }
        }

        if (adapter != null) {
            adapter.setProductos(filtrados);
            adapter.notifyDataSetChanged();
        }
    }

    private void rellenarTabla(List<Productos> productos) {
        RecyclerView recyclerView = findViewById(R.id.recyclerProductosView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ProductosViewAdapter(productos, (producto, position) -> {
            Intent intent = new Intent(ProductosView.this, ProductoPerfil.class);
            intent.putExtra("producto", producto);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void rellenarSpinnerSecciones() {
        Spinner spinerSeccion = findViewById(R.id.spinner);
        ArrayList<String> secciones = viewModel.getSecciones();
        secciones.add(0, "Todas");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.productos_spiner_item, secciones);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerSeccion.setAdapter(adapter1);

        spinerSeccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seccionG = position;
                if (position == 0) {
                    viewModel.getProductos();
                    return;
                }
                viewModel.buscarProductoPor(position, columnaObtencionG, "");
                t.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void rellenarSpinnerColumnas() {
        Spinner spinerColumnas = findViewById(R.id.spinner2);
        String[] columnas = {"Codigo Barras", "Nombre", "Marca", "modelo", "seccion", "Precio Publico", "Precio Neto", "Descripcion", "Vendidos", "Stock", "Ultimo Pedido", "calidad"};
        ArrayList<String> columnasBD = Productos.getArrayColumn();
        columnasBD.remove(0);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.productos_spiner_item, columnas);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerColumnas.setAdapter(adapter2);
        spinerColumnas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                columnaObtencionG = columnasBD.get(position);
                t.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    String busquedaIn = "";

    private void inputBusqueda() {
        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                busquedaIn = s.toString();
                viewModel.buscarProductoPor(seccionG, columnaObtencionG, s.toString());
            }
        });
    }

    public void onClickAddProduct(View v) {
        Intent intent = new Intent(ProductosView.this, FormularioProductos.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.buscarProductoPor(seccionG, columnaObtencionG, busquedaIn);
    }
}
