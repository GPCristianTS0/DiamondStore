package com.Clover.prueba.ui.productos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Clover.prueba.data.dao.ConfiguracionDAO;
import com.Clover.prueba.data.dao.ProductoDAO;
import com.Clover.prueba.data.dao.interfaces.IProducto;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.databinding.ProductoPerfilBinding;
import com.Clover.prueba.domain.productos.generators.GenerarEtiquetaProductoUseCase;
import com.Clover.prueba.domain.productos.usecase.GetProductById;
import com.Clover.prueba.domain.productos.viewmodel.ProductosViewModel;
import com.Clover.prueba.services.Helpers.BannerError;
import com.Clover.prueba.services.generators.GeneradorQR;
import com.Clover.prueba.services.generators.ImageGenerator;
import com.Clover.prueba.services.generators.LabelProductGenerator;
import com.Clover.prueba.services.storage.StorageImage;

public class ProductoPerfil extends AppCompatActivity {
    private ProductosViewModel viewModel;
    private ProductoPerfilBinding binding;
    private String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ProductoPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        GeneradorQR generadorQR = new GeneradorQR(this);
        LabelProductGenerator productGenerator = new LabelProductGenerator(this, generadorQR);
        ImageGenerator imageGenerator = new ImageGenerator();
        StorageImage storageImage = new StorageImage(this);
        //Repository
        IProducto productoDAO = new ProductoDAO(this);
        //UseCase
        GenerarEtiquetaProductoUseCase useCase = new GenerarEtiquetaProductoUseCase(storageImage, imageGenerator, productGenerator);
        GetProductById getProductById = new GetProductById(productoDAO);
        viewModel = new ProductosViewModel(useCase, getProductById);
        viewModel.getMensaje().observe(this, mensaje -> {
            BannerError.mostrarError(binding.getRoot(), mensaje);
        });
        viewModel.getProducto().observe(this, this::bindData);
        viewModel.getEtiqueta().observe(this, uri -> {
            if(uri != null) {
                DialogConfirmacionEtiqueta dialog = new DialogConfirmacionEtiqueta(uri);
                dialog.show(getSupportFragmentManager(), "ConfirmacionEtiqueta");

                viewModel.clearEtiqueta();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Productos producto = extras.getSerializable("producto", Productos.class);
            viewModel.cargarProducto(producto.getId());
            codigo = producto.getId();
        }
    }
    private void bindData(Productos producto) {

        //Establecer los listeners de clic para los botones
        binding.PPBtnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(ProductoPerfil.this, FormularioProductos.class);
            intent.putExtra("producto", producto);
            startActivity(intent);
        });

        binding.PPBtnGenerarEtiqueta.setOnClickListener(v -> {
            Configuracion conf = new ConfiguracionDAO(this).getConfiguracion();
            viewModel.generarEtiqueta(producto, conf);
        });

        // Establecer los valores en los TextViews
        String importes = "$ ";
        binding.PPTxtNombre.setText(producto.getNombre());
        binding.PPTxtMarca.setText("Marca: " + producto.getMarca());
        binding.PPTxtPrecioVenta.setText(importes.concat(producto.getPrecioPublico()+""));
        binding.PPTxtStock.setText(String.valueOf(producto.getStock()));
        binding.PPTxtVendidos.setText(String.valueOf(producto.getVendidos()));
        binding.PPTxtSeccion.setText(producto.getSeccion());
        binding.PPTxtCodigo.setText(producto.getId());
        binding.PPTxtCostoNeto.setText(importes.concat(producto.getPrecioNeto()+""));
        binding.PPTxtMargen.setText(producto.getMargenGanacia()+"%");
        binding.PPTxtDescripcion.setText(producto.getDescripcion());
        binding.PPTxtUltimoPedido.setText(producto.getUltimoPedido());
        if (producto.getRutaImagen()!=null) binding.PPImgProducto.setImageURI(Uri.parse(producto.getRutaImagen()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (codigo != null) viewModel.cargarProducto(codigo);
    }
}