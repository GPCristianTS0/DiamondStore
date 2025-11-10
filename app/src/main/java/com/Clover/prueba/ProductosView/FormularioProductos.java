package com.Clover.prueba.ProductosView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.annotation.NonNull;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;


import com.Clover.prueba.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import BD.CRUD.ProductoDB;
import BD.Controller.ControllerProducto;
import Entidades.Productos;
import Tools.EscanerCodeBar;

public class FormularioProductos extends AppCompatActivity {
    private boolean addOrEdit;
    private String codigo;
    private ControllerProducto controllerProducto ;
    private ImageView imagenView;//Para las imagenes
    private Uri selectedImageUri;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_productos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Boton Agregar
        Button btn = findViewById(R.id.agregarBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductView(v);
            }
        });
        //Boton Eliminar
        Button btnDelete = findViewById(R.id.eliminarBtn);
        btnDelete.setVisibility(INVISIBLE);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });

        //Escanner de codigo de barras
        controllerProducto = new ProductoDB(this, "Productos.db", null, 1);
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
                Intent intent = new Intent(FormularioProductos.this, ProductosView.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //Rellenar Espacios para modificar productos
    private void rellenarEspacios(Productos producto) {
        TextInputEditText t = findViewById(R.id.nombertxt);
        t.setText(producto.getNombre());
        t = findViewById(R.id.marcatxt);
        t.setText(producto.getMarca());
        t = findViewById(R.id.modelotxt);
        t.setEnabled(false);
        t.setText(producto.getSeccion());
        t = findViewById(R.id.p_publicotxt);
        t.setText(String.valueOf(producto.getPrecioPublico()));
        t = findViewById(R.id.p_netotxt);
        t.setText(String.valueOf(producto.getPrecioNeto()));
        t = findViewById(R.id.descripciontxt);
        t.setText(producto.getDescripcion());
        t = findViewById(R.id.unidadestxt);
        t.setText(String.valueOf(producto.getStock()));
        ImageView imagen = findViewById(R.id.imagenFP);
        imagen.setImageURI(Uri.parse(producto.getRutaImagen()));
        rutaGuardada = producto.getRutaImagen();
        t = findViewById(R.id.codeBartxt);
        t.setText(producto.getId());
        this.codigo = producto.getId();
        t.setEnabled(false);
        Button btn = findViewById(R.id.agregarBtn);
        btn.setText("Actualizar");
        TextView te = findViewById(R.id.tittlelbl);
        te.setText("Actualizar Producto");
        Button btnEscanner = findViewById(R.id.escanearButton);
        btnEscanner.setClickable(false);
    }
    //Actualizar productos
    private void actualizarProducto(Productos productoOld) {
        Productos productoNew = getProductoOfInputs();
        productoNew.setUltimoPedido(productoOld.getUltimoPedido());
        if (selectedImageUri!=null){
            rutaGuardada = guardarImagenEnPrivado(selectedImageUri);
            productoNew.setRutaImagen(rutaGuardada);
        }else{
            productoNew.setRutaImagen(rutaGuardada);
        }
        Log.e("Clover_App", "productoOld: "+productoOld.toString());
        Log.e("Clover_App", "productoNew: "+productoNew.toString());
        controllerProducto.updateProducto(productoOld, productoNew);
        Toast.makeText(this, "Producto Actualizado", Toast.LENGTH_SHORT).show();
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
        TextInputEditText codigoTxt = findViewById(R.id.codeBartxt);
        if (controllerProducto.getProductoCode(codigo).getId().equals(codigo)) {
            Toast.makeText(this, "El codigo ya esta registrado", Toast.LENGTH_SHORT).show();
            codigoTxt.setText("");
        } else {
            codigoTxt.setText(codigo);
        }
        this.codigo =codigo;
    }
    private String getCodigo(){
        return this.codigo;
    }

    //Funcion Boton agregar
    public void addProductView(View v){

        TextInputEditText t = findViewById(R.id.codeBartxt);
        Productos productos = controllerProducto.getProductoCode(t.getText().toString().trim());
        if (productos.getId().equals(t.getText().toString())) {
            t.setText("");
            Toast.makeText(this, "El codigo ya esta registrado", Toast.LENGTH_SHORT).show();
        }else {
            productos = getProductoOfInputs();
            String fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            productos.setUltimoPedido(fechaHoy);
            rutaGuardada = guardarImagenEnPrivado(selectedImageUri);
            productos.setRutaImagen(rutaGuardada);
            //controllerProducto.addProducto(productos);
            Toast.makeText(this, "Producto Agregado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FormularioProductos.this, ProductosView.class);
            startActivity(intent);
            finish();
        }
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
        try {
            InputStream inputStream = getContentResolver().openInputStream(uriImagen);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // 🔹 Reducimos tamaño si es muy grande (por ejemplo, 800x800 máx)
            int maxSize = 800;
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            if (width > maxSize || height > maxSize) {
                float ratio = (float) width / height;
                if (ratio > 1) { // Imagen horizontal
                    width = maxSize;
                    height = (int) (width / ratio);
                } else { // Imagen vertical
                    height = maxSize;
                    width = (int) (height * ratio);
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }

            // 🔹 Creamos el archivo en almacenamiento privado
            String nombreArchivo = "img_" + System.currentTimeMillis() + ".jpg";
            File archivo = new File(getFilesDir(), nombreArchivo);

            // 🔹 Escribimos el bitmap comprimido (80% calidad)
            FileOutputStream outputStream = new FileOutputStream(archivo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

            // 🔹 Cerramos flujos
            outputStream.close();
            bitmap.recycle(); // Libera memoria RAM
            inputStream.close();
            outputStream.close();

            // 🔹 Retornar la ruta del archivo guardado
            return archivo.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //Elimiinar Producto Funcion
    private void deleteProduct(){
        Productos producto = getProductoOfInputs();
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
        t = findViewById(R.id.modelotxt);
        productos.setSeccion(t.getText().toString().trim());
        t = findViewById(R.id.p_publicotxt);
        productos.setPrecioPublico(Integer.parseInt(t.getText().toString()));
        t = findViewById(R.id.p_netotxt);
        productos.setPrecioNeto(Integer.parseInt(t.getText().toString()));
        t = findViewById(R.id.descripciontxt);
        productos.setDescripcion(t.getText().toString().trim());
        productos.setVendidos(0);
        t = findViewById(R.id.unidadestxt);
        productos.setStock(Integer.parseInt(t.getText().toString()));
        return productos;
    }

}