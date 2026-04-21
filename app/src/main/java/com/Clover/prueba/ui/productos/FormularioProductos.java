package com.Clover.prueba.ui.productos;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.Clover.prueba.R;
import com.Clover.prueba.databinding.ProductosFormularioActivityBinding;
import com.Clover.prueba.domain.productos.usecase.AddProductUseCase;
import com.Clover.prueba.domain.productos.usecase.GetProductById;
import com.Clover.prueba.domain.productos.usecase.GetSecciones;
import com.Clover.prueba.domain.productos.usecase.UpdateProductUseCase;
import com.Clover.prueba.domain.productos.viewmodel.FormularioProductosViewModel;
import com.Clover.prueba.services.Helpers.BannerError;
import com.Clover.prueba.services.storage.StorageImage;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import com.Clover.prueba.data.dao.ProductoDAO;
import com.Clover.prueba.data.dao.interfaces.IProducto;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.ui.scanner.EscanerCodeBar;

public class FormularioProductos extends AppCompatActivity {
    private String codigo;
    private FormularioProductosViewModel viewModel;
    private Uri selectedImageUri;
    private ProductosFormularioActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProductosFormularioActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Repositorio
        IProducto iProducto = new ProductoDAO(this);
        //Services
        StorageImage storageImage = new StorageImage(this);
        //UseCase
        AddProductUseCase addProductUseCase = new AddProductUseCase(iProducto, storageImage);
        GetProductById getProductById = new GetProductById(iProducto);
        UpdateProductUseCase updateProductUseCase = new UpdateProductUseCase(iProducto, storageImage);
        GetSecciones getSecciones = new GetSecciones(iProducto);
        //ViewModel
        viewModel = new FormularioProductosViewModel(addProductUseCase, getProductById, updateProductUseCase, getSecciones);
        viewModel.getError().observe(this, mensaje -> {
            BannerError.mostrarError(binding.getRoot(), mensaje);
        });
        viewModel.getExito().observe(this, mensaje -> {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
            finish();
        });

        //Desaparecer los campos de vendidos
        binding.vendidosInputFP.setVisibility(INVISIBLE);
        binding.FPLayoutVendidos.setVisibility(INVISIBLE);

        inicilizarBotonesListener();
        EscannerCamara();
        rellenarSpiner(); //rellenar el spiner de secciones
        addImage();//para agregar imagenes


        //para la modificacion de productos
        Productos producto = (Productos) getIntent().getSerializableExtra("producto");
        if (producto!=null){
            rellenarEspacios(producto);
            binding.addProductoBtn.setVisibility(VISIBLE);
            binding.addProductoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actualizarProducto(producto);
                }
            });
        }
        //Sobreescritura del regreso
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void inicilizarBotonesListener() {
        //Boton Agregar
        binding.agregarBtn.setOnClickListener(v -> addProductView());
        //Boton Imagen
        binding.imagenFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });
    }

    private void EscannerCamara() {
        //Escanner de codigo de barras
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }
        EscanerCodeBar escaner = new EscanerCodeBar();
        binding.escanearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escaner.inicializarEscaner(FormularioProductos.this);
            }
        });
    }

    //Rellenar Espacios para modificar productos
    private void rellenarEspacios(Productos producto) {
        binding.switchVentaGranelFP.setChecked(producto.isVentaxpeso());
        int position = viewModel.getSeccion(producto.getSeccion());
        binding.spinnerSeccionFP.setSelection(position+1);
        binding.nombertxt.setText(producto.getNombre());
        binding.marcatxt.setText(producto.getMarca());
        binding.pPublicotxt.setText(String.valueOf(producto.getPrecioPublico()));
        binding.pNetotxt.setText(String.valueOf(producto.getPrecioNeto()));
        binding.descripciontxt.setText(producto.getDescripcion());
        binding.unidadestxt.setText(String.valueOf(producto.getStock()));
        binding.vendidosInputFP.setText(String.valueOf(producto.getVendidos()));
        binding.vendidosInputFP.setVisibility(VISIBLE);
        binding.vendidosInputFP.setVisibility(VISIBLE);
        binding.FPLayoutVendidos.setVisibility(VISIBLE);
        if (producto.getRutaImagen()!=null) {
            binding.imagenFP.setImageURI(Uri.parse(producto.getRutaImagen()));
        }else
            binding.imagenFP.setImageResource(R.drawable.agregar_imgaen);
        binding.codeBartxt.setText(producto.getId());
        this.codigo = producto.getId();

        binding.agregarBtn.setText("Actualizar");
        binding.tittlelbl.setText("Actualizar Producto");
    }
    //Actualizar productos
    private void actualizarProducto(Productos productoOld) {
        Productos productoNew = getProductoOfInputs();
        if (productoNew == null) {
            return;
        }
        productoNew.setUltimoPedido(productoOld.getUltimoPedido());

        viewModel.updateProducto(productoNew, productoOld, selectedImageUri);
    }
    //Escaner de codigo de barras
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            } else {
                setCodigo(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void setCodigo(String codigo){
        if (viewModel.isInvalidCodigo(codigo)) {
            BannerError.mostrarError(binding.getRoot(), "El codigo ya existe");
            binding.codeBartxt.setText("");
        } else {
            binding.codeBartxt.setText(codigo);
        }
        this.codigo =codigo;
    }

    //Funcion Boton agregar
    public void addProductView(){
        Productos productos = getProductoOfInputs();
        if (productos == null) {
            return;
        }
        viewModel.addProduct(productos, selectedImageUri);
    }
    private ActivityResultLauncher<Intent> launcherActivityGalery;
    public void addImage(){
        launcherActivityGalery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    binding.imagenFP.setImageURI(selectedImageUri);
                }
            }
        );
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        launcherActivityGalery.launch(intent);
    }

    //Pasa a un producto todos los inputs del formulario
    private Productos getProductoOfInputs(){
        Productos productos = new Productos();
        if (isInvalidInputs(binding.nombertxt, "Ingrese el nombre del producto")) return null;
        if (isInvalidInputs(binding.marcatxt, "Ingrese la marca del producto")) return null;
        if (isInvalidInputs(binding.pPublicotxt, "Ingrese el precio publico del producto")) return null;
        if (isInvalidInputs(binding.pNetotxt, "Ingrese el precio neto del producto")) return null;
        if (isInvalidInputs(binding.descripciontxt, "Ingrese la descripcion del producto")) return null;
        if (isInvalidInputs(binding.unidadestxt, "Ingrese la cantidad del producto")) return null;
        if (binding.vendidosInputFP.getVisibility() == VISIBLE)
            if (isInvalidInputs(binding.vendidosInputFP, "Ingrese la cantidad de productos vendidos")) return null;
        if (isInvalidInputs(binding.codeBartxt, "Ingrese el codigo del producto")) return null;
        if (binding.spinnerSeccionFP.getSelectedItemPosition() == 0) {
            BannerError.mostrarError(binding.getRoot(), "Seleccione una seccion");
            return null;
        }
        //validaciones para numeros
        int stock;
        int vendidos = 0;
        int precioPublico;
        int precioNeto;
        try {
            precioPublico = Integer.parseInt((binding.pPublicotxt.getText().toString()));
            precioNeto = Integer.parseInt(binding.pNetotxt.getText().toString());
            stock = Integer.parseInt(binding.unidadestxt.getText().toString());
            if (binding.vendidosInputFP.getVisibility() == VISIBLE)
                vendidos = Integer.parseInt(binding.vendidosInputFP.getText().toString());
        } catch (NumberFormatException e) {
            BannerError.mostrarError(binding.getRoot(), "Ingrese un numero valido");
            return null;
        }
        productos.setId(binding.codeBartxt.getText().toString());
        productos.setNombre(binding.nombertxt.getText().toString().trim());
        productos.setMarca(binding.marcatxt.getText().toString().trim());
        productos.setSeccion(binding.spinnerSeccionFP.getSelectedItem().toString());
        productos.setPrecioPublico(precioPublico);
        productos.setPrecioNeto(precioNeto);
        productos.setDescripcion(binding.descripciontxt.getText().toString().trim());
        productos.setStock(stock);
        productos.setVentaxpeso(binding.switchVentaGranelFP.isChecked()?1:0);
        if (binding.vendidosInputFP.getVisibility() == VISIBLE)
            productos.setVendidos(vendidos);
        return productos;
    }
    private boolean isInvalidInputs(TextInputEditText field, String mensaje){
        if(field.getText().toString().trim().isEmpty()){
            BannerError.mostrarError(binding.getRoot(), mensaje);
            return true;
        }
        return false;
    }
    private void rellenarSpiner(){
        ArrayList<String> secciones = viewModel.getSeccion();
        secciones.add(0, "Seleccionar");
        secciones.add("Agregar Nueva");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.productos_spiner_item, secciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp = binding.spinnerSeccionFP;
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==secciones.size()-1){
                    FragmentNuevaSeccion fragment = new FragmentNuevaSeccion();
                    fragment.setListener(new FragmentNuevaSeccion.OnDimissListener() {
                        @Override
                        public void onDialogCerrado() {
                            sp.setSelection(0);
                            rellenarSpiner();
                        }
                    });
                    fragment.show(getSupportFragmentManager(), "NuevaSeccion");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}