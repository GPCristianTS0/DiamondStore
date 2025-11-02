package com.Clover.prueba.ProductosView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.Clover.prueba.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import BD.CRUD.ProductoDB;
import BD.Controller.ControllerProducto;
import Entidades.Productos;
import Tools.EscanerCodeBar;

public class FormularioProductos extends AppCompatActivity {
    private String codigo;
    private ControllerProducto controllerProducto ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_productos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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


    }
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
            Toast.makeText(this, t.getText().toString(), Toast.LENGTH_SHORT).show();
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
            String fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            productos.setUltimoPedido(fechaHoy);
            controllerProducto.addProducto(productos);
            Toast.makeText(this, "Producto Agregado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FormularioProductos.this, ProductosView.class);
            startActivity(intent);
        }
    }
    ActivityResultLauncher<Intent> launcherActivityGalery;

    private String rutaGuardada; // Aquí guardaremos la ruta del archivo
    ImageView imageView ;
    //Funcion para agregar imagen
    public void addImage(View v){

        launcherActivityGalery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();

                    // 🔹 2. Mostrar la imagen en pantalla
                    imageView.setImageURI(selectedImageUri);

                    // 🔹 3. Guardarla en el almacenamiento interno
                    //rutaGuardada = guardarImagenEnPrivado(selectedImageUri);

                    if (rutaGuardada != null) {
                        Toast.makeText(this, "Imagen guardada en: " + rutaGuardada, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }

    private String guardarImagenEnPrivado(Uri selectedImageUri) {
        return null;
    }
}