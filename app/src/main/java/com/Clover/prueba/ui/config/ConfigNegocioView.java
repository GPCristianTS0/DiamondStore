package com.Clover.prueba.ui.config;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.ConfiguracionControl;
import com.Clover.prueba.data.models.Configuracion;
import com.google.android.material.textfield.TextInputEditText;

public class ConfigNegocioView extends AppCompatActivity {
    private ConfiguracionControl configuracionControl;

    private Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.config_negocio_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainConfigNegocio), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        configuracionControl = new ConfiguracionControl(this);
        Configuracion configuracion = configuracionControl.getConfiguracion();

        if (configuracion == null) {
            return;
        }
        String ruta = configuracion.getRutaLogo();
        Log.d("Clover_App", "Ruta del logo: " + ruta);
        //relleno de campos
        ImageView imgLogo = findViewById(R.id.ConP_imgLogo);
        TextInputEditText txtNombre = findViewById(R.id.ConP_txtNombre);
        TextInputEditText txtEslogan = findViewById(R.id.ConP_txtEslogan);
        TextInputEditText txtDireccion = findViewById(R.id.ConP_txtDireccion);
        TextInputEditText txtTelefono = findViewById(R.id.ConP_txtTelefono);
        TextInputEditText txtRFC = findViewById(R.id.ConP_txtRFC);
        TextInputEditText txtStockMin = findViewById(R.id.ConP_txtStockMin);
        TextInputEditText txtImpuesto = findViewById(R.id.ConP_txtImpuesto);
        TextInputEditText txtGarantia = findViewById(R.id.ConP_txtGarantia);
        TextInputEditText txtMensajeShare = findViewById(R.id.ConP_txtMensajeShare);
        TextInputEditText txtBeneficiario = findViewById(R.id.ConP_txtBeneficiario);
        TextInputEditText txtBanco = findViewById(R.id.ConP_txtBanco);
        TextInputEditText txtCuenta = findViewById(R.id.ConP_txtCuenta);
        txtNombre.setText(configuracion.getNombreNegocio());
        txtEslogan.setText(configuracion.getEslogan());
        txtDireccion.setText(configuracion.getDireccion());
        txtTelefono.setText(configuracion.getTelefono());
        txtRFC.setText(configuracion.getRfc());
        txtStockMin.setText(String.valueOf(configuracion.getStockMinimo()));
        txtImpuesto.setText(String.valueOf(configuracion.getIva()));
        txtGarantia.setText(configuracion.getNotaGarantia());
        txtMensajeShare.setText(configuracion.getMensajeShare());
        txtBeneficiario.setText(configuracion.getBeneficiario());
        txtBanco.setText(configuracion.getBanco());
        txtCuenta.setText(configuracion.getCuenta());
        //Imagen
        if (configuracion.getRutaLogo() != null){
            imgLogo.setImageURI(Uri.parse(configuracion.getRutaLogo()));
        }
        //Boton Accion
        findViewById(R.id.ConP_btnActualizar).setOnClickListener(v -> {
            Configuracion configuracionActualizada = new Configuracion();
            Log.v("Clover_App", "Configuracion: " + configuracionActualizada.toString() + " "+configuracion.toString());
            configuracionActualizada.setNombreNegocio(txtNombre.getText().toString());
            configuracionActualizada.setEslogan(txtEslogan.getText().toString());
            configuracionActualizada.setDireccion(txtDireccion.getText().toString());
            configuracionActualizada.setTelefono(txtTelefono.getText().toString());
            configuracionActualizada.setRfc(txtRFC.getText().toString());
            configuracionActualizada.setStockMinimo(Integer.parseInt(txtStockMin.getText().toString()));
            configuracionActualizada.setIva(Double.parseDouble(txtImpuesto.getText().toString()));
            configuracionActualizada.setNotaGarantia(txtGarantia.getText().toString());
            configuracionActualizada.setMensajeShare(txtMensajeShare.getText().toString());
            configuracionActualizada.setBeneficiario(txtBeneficiario.getText().toString());
            configuracionActualizada.setBanco(txtBanco.getText().toString());
            configuracionActualizada.setCuenta(txtCuenta.getText().toString());
            Log.e("Clover_App", "Ruta: " + ruta + " RutaNueva: "+configuracionActualizada.getRutaLogo());
            if (selectedImageUri != null) {
                configuracionActualizada.setLogoURL(selectedImageUri);
            }
            configuracionActualizada.setOldRutaLogo(ruta);
            if (configuracionControl.updateConfiguracionNegocio(configuracionActualizada)){
                Toast.makeText(this, "Actualización exitosa", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
        //Accion cambiar logo
        addImage(imgLogo);
        imgLogo.setOnClickListener(v -> {
            abrirGaleria();
        });
    }

    ActivityResultLauncher<Intent> launcherActivityGalery;

    private String rutaGuardada; // Aquí guardaremos la ruta del archivo

    //Funcion para agregar imagen
    public void addImage(ImageView imagenView){
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
}