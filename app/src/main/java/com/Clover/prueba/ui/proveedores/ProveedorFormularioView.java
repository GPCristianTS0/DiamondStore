package com.Clover.prueba.ui.proveedores;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.ProveedoresController;
import com.Clover.prueba.data.models.Proveedor;
import com.google.android.material.textfield.TextInputEditText;

public class ProveedorFormularioView extends AppCompatActivity {
    private ProveedoresController controllerProveedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.proveedor_formularioview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        controllerProveedores = new ProveedoresController(this);
        Button btnAddOrEdit = findViewById(R.id.button8);
        btnAddOrEdit.setOnClickListener(v -> {
             if (controllerProveedores.addProveedor(getInputedProveedor())){
                 Toast.makeText(this, "Proveedor agregado", Toast.LENGTH_SHORT).show();
             } else {
                 Toast.makeText(this, "Error al agregar proveedor", Toast.LENGTH_SHORT).show();
             }
            finish();
        });
        //Accion Boton Eliminar

        Proveedor proveedor = (Proveedor) getIntent().getSerializableExtra("proveedor");
        if (proveedor!=null||getIntent().hasExtra("proveedor")  ) {
            Button bEliminar = findViewById(R.id.PrF_btnEliminar);
            rellenarEspacios(proveedor);
            bEliminar.setOnClickListener(v -> {// Dentro del OnClickListener de tu botón eliminar
                new AlertDialog.Builder(this)
                        .setTitle("¿Eliminar Proveedor?")
                        .setMessage("Esta acción no se puede deshacer. ¿Estás seguro?")
                        .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                            if (controllerProveedores.deleteProveedor(proveedor.getId_proveedor())){
                                finish();
                                Toast.makeText(this, "Proveedor eliminado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Error al eliminar proveedor", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancelar", null) // No hace nada, solo cierra el diálogo
                        .show();
            });
            btnAddOrEdit.setOnClickListener( v -> {
                if (controllerProveedores.updateProveedor(proveedor, getInputedProveedor())){
                    finish();
                    Toast.makeText(this, "Proveedor actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al actualizar proveedor", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void rellenarEspacios(Proveedor proveedor) {
        TextInputEditText C = findViewById(R.id.PrF_nombreProveedortxt);
        C.setText(proveedor.getNombre_proveedor());
        C = findViewById(R.id.PrF_nombrevendedor);
        C.setText(proveedor.getNombre_vendedor());
        C = findViewById(R.id.PrF_categoria);
        C.setText(proveedor.getCategoria());
        C = findViewById(R.id.prF_direcciontxt2);
        C.setText(proveedor.getDireccion());
        C = findViewById(R.id.prF_correotxt);
        C.setText(proveedor.getEmail());
        C = findViewById(R.id.prF_telefonotxt);
        C.setText(proveedor.getTelefono());
        C = findViewById(R.id.prF_visitatxt);
        C.setText(proveedor.getDias_visita());
        C = findViewById(R.id.prF_Pagptxt);
        C.setText(proveedor.getDiasPago());
        C = findViewById(R.id.textInputEditText3);
        C.setText(proveedor.getObservaciones());
        TextView t = findViewById(R.id.PrF_tuttle);
        t.setText("Actualizar Proveedor");
        Button b = findViewById(R.id.button8);
        b.setText("Actualizar");
        b = findViewById(R.id.PrF_btnEliminar);
        b.setVisibility(TextView.VISIBLE);
    }
    private Proveedor getInputedProveedor(){
        Proveedor proveedor = new Proveedor();
        TextInputEditText C = findViewById(R.id.PrF_nombreProveedortxt);
        proveedor.setNombre_proveedor(C.getText().toString());
        C = findViewById(R.id.PrF_nombrevendedor);
        proveedor.setNombre_vendedor(C.getText().toString());
        C = findViewById(R.id.PrF_categoria);
        proveedor.setCategoria(C.getText().toString());
        C = findViewById(R.id.prF_direcciontxt2);
        proveedor.setDireccion(C.getText().toString());
        C = findViewById(R.id.prF_correotxt);
        proveedor.setEmail(C.getText().toString());
        C = findViewById(R.id.prF_telefonotxt);
        proveedor.setTelefono(C.getText().toString());
        C = findViewById(R.id.prF_visitatxt);
        proveedor.setDias_visita(C.getText().toString());
        C = findViewById(R.id.prF_Pagptxt);
        proveedor.setDiasPago(C.getText().toString());
        C = findViewById(R.id.textInputEditText3);
        proveedor.setObservaciones(C.getText().toString());
        return proveedor;
    }
}