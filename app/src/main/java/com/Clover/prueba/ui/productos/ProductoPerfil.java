package com.Clover.prueba.ui.productos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Clover.prueba.R;
import com.Clover.prueba.data.dao.ConfiguracionDAO;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.domain.productos.GenerarEtiquetaProductoUseCase;
import com.Clover.prueba.domain.productos.viewmodel.ProductosViewModel;
import com.Clover.prueba.services.generators.GeneradorQR;
import com.Clover.prueba.services.generators.ImageGenerator;
import com.Clover.prueba.services.generators.LabelProductGenerator;
import com.Clover.prueba.services.storage.StorageImage;

public class ProductoPerfil extends AppCompatActivity {
    private ProductosViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.producto_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GeneradorQR generadorQR = new GeneradorQR(this);
        LabelProductGenerator productGenerator = new LabelProductGenerator(this, generadorQR);
        ImageGenerator imageGenerator = new ImageGenerator();
        StorageImage storageImage = new StorageImage(this);
        GenerarEtiquetaProductoUseCase useCase = new GenerarEtiquetaProductoUseCase(storageImage, imageGenerator, productGenerator);
        viewModel = new ProductosViewModel(useCase);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Productos producto = extras.getSerializable("producto", Productos.class);
            bindData(producto);
        }
    }
    private void bindData(Productos producto) {
        TextView txtNombre = findViewById(R.id.PP_txtNombre);
        TextView txtMarca = findViewById(R.id.PP_txtMarca);
        TextView txtPrecioVenta = findViewById(R.id.PP_txtPrecioVenta);
        TextView txtStock = findViewById(R.id.PP_txtStock);
        TextView txtVendidos = findViewById(R.id.PP_txtVendidos);
        TextView txtSeccion = findViewById(R.id.PP_txtSeccion);
        TextView txtCodigo = findViewById(R.id.PP_txtCodigo);
        TextView txtCostoNeto = findViewById(R.id.PP_txtCostoNeto);
        TextView txtMargen = findViewById(R.id.PP_txtMargen);
        TextView txtUltimoPedido = findViewById(R.id.PP_txtUltimoPedido);
        TextView txtDescripcion = findViewById(R.id.PP_txtDescripcion);
        ImageView imgProducto = findViewById(R.id.PP_imgProducto);
        Button btnEditar = findViewById(R.id.PP_btnEditar);
        Button btnGenerarEtiqueta = findViewById(R.id.PP_btnGenerarEtiqueta);

        //Establecer los listeners de clic para los botones
        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(ProductoPerfil.this, FormularioProductos.class);
            intent.putExtra("producto", producto);
            startActivity(intent);
        });

        btnGenerarEtiqueta.setOnClickListener(v -> {
            Configuracion conf = new ConfiguracionDAO(this).getConfiguracion();
            Uri result = viewModel.generarEtiqueta(producto, conf);

            if (result==null) {
                Toast.makeText(this, "No se pudo generar la etiqueta", Toast.LENGTH_SHORT).show();
            } else {
                DialogConfirmacionEtiqueta dialog = new DialogConfirmacionEtiqueta(result);
                dialog.show(getSupportFragmentManager(), "ConfirmacionEtiqueta");
            }
        });

        // Establecer los valores en los TextViews
        String importes = "$ ";
        txtNombre.setText(producto.getNombre());
        txtMarca.setText("Marca: " + producto.getMarca());
        txtPrecioVenta.setText(importes.concat(producto.getPrecioPublico()+""));
        txtStock.setText(String.valueOf(producto.getStock()));
        txtVendidos.setText(String.valueOf(producto.getVendidos()));
        txtSeccion.setText(producto.getSeccion());
        txtCodigo.setText(producto.getId());
        txtCostoNeto.setText(importes.concat(producto.getPrecioNeto()+""));
        txtMargen.setText(producto.getMargenGanacia()+"%");
        txtDescripcion.setText(producto.getDescripcion());
        txtUltimoPedido.setText(producto.getUltimoPedido());
        if (producto.getRutaImagen()!=null) imgProducto.setImageURI(Uri.parse(producto.getRutaImagen()));


    }
}