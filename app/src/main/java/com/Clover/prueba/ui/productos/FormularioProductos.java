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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.Clover.prueba.R;
import com.Clover.prueba.utils.ImageUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.Clover.prueba.data.dao.ProductoDAO;
import com.Clover.prueba.data.controller.ControllerProducto;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.utils.EscanerCodeBar;

public class FormularioProductos extends AppCompatActivity {
    private static final String FILTRO_TODAS = "Todas";
    private boolean addOrEdit;
    private String codigo;
    private ControllerProducto controllerProducto = new ProductoDAO(this);;
    private ImageView imagenView;//Para las imagenes
    private Uri selectedImageUri;
    private Button btn;

    private TextInputEditText t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productos_formulario_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Desaparecer los campos de vendidos
        TextInputEditText vendidosTxt = findViewById(R.id.vendidosInputFP);
        vendidosTxt.setVisibility(INVISIBLE);
        TextView vendidostxtf = findViewById(R.id.textView17);
        vendidostxtf.setVisibility(INVISIBLE);
        t = findViewById(R.id.codeBartxt);
        //rellenar el spiner de secciones
        rellenarSpiner();
        //Boton Agregar
        Button btn = findViewById(R.id.agregarBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductView(v);
            }
        });
        //Boton Eliminar
        Button btnDelete = findViewById(R.id.addProductoBtn);
        btnDelete.setVisibility(INVISIBLE);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });

        //Escanner de codigo de barras
        Button escanearBtn = findViewById(R.id.escanearButton);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }
        EscanerCodeBar escaner = new EscanerCodeBar();
        escanearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escaner.inicializarEscaner(FormularioProductos.this);
            }
        });

        //Sccion de agregar imagen
        imagenView = findViewById(R.id.imagenFP);
        addImage();
        imagenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        //para la modificacion de productos
        Productos producto = (Productos) getIntent().getSerializableExtra("producto");
        if (producto!=null){
            rellenarEspacios(producto);
            btnDelete.setVisibility(VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
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
    //Rellenar Espacios para modificar productos
    private void rellenarEspacios(Productos producto) {
        Spinner sp = findViewById(R.id.spinnerSeccionFP);
        int position = controllerProducto.getSeccione().indexOf(producto.getSeccion());
        sp.setSelection(position+1);
        TextInputEditText t = findViewById(R.id.nombertxt);
        t.setText(producto.getNombre());
        t = findViewById(R.id.marcatxt);
        t.setText(producto.getMarca());
        t.setText(producto.getSeccion());
        t = findViewById(R.id.p_publicotxt);
        t.setText(String.valueOf(producto.getPrecioPublico()));
        t = findViewById(R.id.p_netotxt);
        t.setText(String.valueOf(producto.getPrecioNeto()));
        t = findViewById(R.id.descripciontxt);
        t.setText(producto.getDescripcion());
        t = findViewById(R.id.unidadestxt);
        t.setText(String.valueOf(producto.getStock()));
        t = findViewById(R.id.vendidosInputFP);
        t.setText(String.valueOf(producto.getVendidos()));
        t = findViewById(R.id.vendidosInputFP);
        t.setVisibility(VISIBLE);
        TextView vendidostxtf = findViewById(R.id.textView17);
        vendidostxtf.setVisibility(VISIBLE);
        ImageView imagen = findViewById(R.id.imagenFP);
        if (producto.getRutaImagen()!=null) {
            imagen.setImageURI(Uri.parse(producto.getRutaImagen()));
            rutaGuardada = producto.getRutaImagen();
        }else
            imagen.setImageResource(R.drawable.agregar_imgaen);
        this.t.setText(producto.getId());
        this.codigo = producto.getId();
        Button btn = findViewById(R.id.agregarBtn);
        btn.setText("Actualizar");
        TextView te = findViewById(R.id.tittlelbl);
        te.setText("Actualizar Producto");
        Button btnEscanner = findViewById(R.id.escanearButton);
    }
    //Actualizar productos
    private void actualizarProducto(Productos productoOld) {
        Productos productoNew = getProductoOfInputs();
        productoNew.setUltimoPedido(productoOld.getUltimoPedido());
        if (selectedImageUri!=null) {
            rutaGuardada = guardarImagenEnPrivado(selectedImageUri);
            productoNew.setRutaImagen(rutaGuardada);
        }
        controllerProducto.updateProducto(productoOld, productoNew);
        Toast.makeText(this, "Producto Actualizado", Toast.LENGTH_SHORT).show();
        finish();
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
        if (controllerProducto.getProductoCode(codigo).getId()!=null) {
            Toast.makeText(this, "El codigo ya esta registrado", Toast.LENGTH_SHORT).show();
            t.setText("");
        } else {
            t.setText(codigo);
        }
        this.codigo =codigo;
    }

    //Funcion Boton agregar
    public void addProductView(View v){
        Spinner sp = findViewById(R.id.spinnerSeccionFP);
        Productos productos = controllerProducto.getProductoCode(t.getText().toString());
        if (productos.getId()!=null){
            t.setText("");
            Toast.makeText(this, "El codigo ya esta registrado", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageUri != null) {
            rutaGuardada = guardarImagenEnPrivado(selectedImageUri);
        }
        if(sp.getSelectedItem().toString().equals("Seleccionar")) {
            Toast.makeText(this, "Seleccione una seccion", Toast.LENGTH_SHORT).show();
            return;
        }
        productos.setSeccion(sp.getSelectedItem().toString());
        productos = getProductoOfInputs();
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        productos.setRutaImagen(rutaGuardada);
        productos.setUltimoPedido(fechaHoy);
        controllerProducto.addProducto(productos);
        Toast.makeText(this, "Producto Agregado", Toast.LENGTH_SHORT).show();
        finish();
    }
    ActivityResultLauncher<Intent> launcherActivityGalery;

    private String rutaGuardada; // Aquí guardaremos la ruta del archivo

    //Funcion para agregar imagen
    public void addImage(){
        // 🔹 1. Crear el launcher
        launcherActivityGalery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();

                    // 🔹 2. Mostrar la imagen en pantalla
                    imagenView.setImageURI(selectedImageUri);
                }
            }
        );
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        launcherActivityGalery.launch(intent);
    }
    private String guardarImagenEnPrivado(Uri uriImagen) {
        ImageUtils utils = new ImageUtils(this);
        return utils.guardarImagen(uriImagen);
    }

    //Elimiinar Producto Funcion
    private void deleteProduct(){
        Productos producto = getProductoOfInputs();
        Log.e("Clover_App", "deleteProduct: "+producto.toString());
        controllerProducto.deleteProducto(producto);
        Toast.makeText(this, "Producto Eliminado", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(FormularioProductos.this, ProductosView.class);
        startActivity(intent);
        finish();
    }
    //Pasa a un producto todos los inputs del formulario
    private Productos getProductoOfInputs(){
        Productos productos = new Productos();
        TextInputEditText t = findViewById(R.id.codeBartxt);
        productos.setId(t.getText().toString());
        t = findViewById(R.id.nombertxt);
        productos.setNombre(t.getText().toString().trim());
        t = findViewById(R.id.marcatxt);
        productos.setMarca(t.getText().toString().trim());
        Spinner sp = findViewById(R.id.spinnerSeccionFP);
        productos.setSeccion(sp.getSelectedItem().toString());
        t = findViewById(R.id.p_publicotxt);
        productos.setPrecioPublico(Integer.parseInt(t.getText().toString()));
        t = findViewById(R.id.p_netotxt);
        productos.setPrecioNeto(Integer.parseInt(t.getText().toString()));
        t = findViewById(R.id.descripciontxt);
        productos.setDescripcion(t.getText().toString().trim());
        t = findViewById(R.id.unidadestxt);
        productos.setStock(Integer.parseInt(t.getText().toString()));
        t = findViewById(R.id.vendidosInputFP);
        if (t.getVisibility() == VISIBLE)
            productos.setVendidos(Integer.parseInt(t.getText().toString()));
        return productos;
    }
    private void rellenarSpiner(){
        ArrayList<String> secciones = controllerProducto.getSeccione();
        secciones.add(0, "Seleccionar");
        secciones.add("Agregar Nueva");
        Spinner sp = findViewById(R.id.spinnerSeccionFP);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.productos_spiner_item, secciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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