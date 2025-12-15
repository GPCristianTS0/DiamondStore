package com.Clover.prueba.ProductosView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Clover.prueba.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import BD.Controller.ControllerProducto;
import BD.DAOs.ProductoDAO;
import Entidades.Productos;
import Tools.EscanerCodeBar;

public class productos_actualizarStock extends AppCompatActivity {
    private String idProducto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productos_actualizar_stock);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Funcion enter del teclado
        TextInputEditText idText = findViewById(R.id.textInputEditText2);
        idText.setOnEditorActionListener( (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE|| (event != null && event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                String texto = idText.getText().toString().trim();
                if (texto.isEmpty()){
                    return false;
                }
                rellenarInformacion(texto);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(idText.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }
    private final ControllerProducto controllerProducto = new ProductoDAO(this);
    private Context context;

    private void rellenarInformacion(String codigo){
        Productos producto = controllerProducto.getProductoCode(codigo);
        if (producto.getId()==null){
            Toast.makeText(this, "Articulo no registrado", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView nombreProductoOut = findViewById(R.id.acst_nombreProductoOut);
        TextView stockRestanteOut = findViewById(R.id.acst_stockRestanteOut);
        nombreProductoOut.setText(producto.getNombre());
        stockRestanteOut.setText(String.valueOf(producto.getStock()));
        idProducto = producto.getId();

    }
    //Escanner
    private void escanerTools(){
        EscanerCodeBar escaner = new EscanerCodeBar();
        escaner.inicializarEscaner(productos_actualizarStock.this);
    }
    //Boton escaner
    public void OnEscanerStock(android.view.View view) {
       escanerTools();
    }
    //Funcion del escanner
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        TextInputEditText idText = findViewById(R.id.textInputEditText2);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            } else {
                rellenarInformacion(result.getContents());
                idText.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    //Accion boton actualizar
    public void OnClickActualizar(android.view.View view) {
        TextInputEditText stockInput = findViewById(R.id.newStockInput);
        String stock = stockInput.getText().toString().trim();
        if (stock.isEmpty() || idProducto==null){
            Toast.makeText(this, "Ingrese un stock", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(stock)<0) {
            Toast.makeText(this, "Ingrese un stock valido", Toast.LENGTH_SHORT).show();
            return;
        }
        if(controllerProducto.updateStock(Integer.parseInt(stock), idProducto)){
            Toast.makeText(this, "Stock actualizado", Toast.LENGTH_SHORT).show();
            TextView nombreProductoOut = findViewById(R.id.acst_nombreProductoOut);
            TextView stockRestanteOut = findViewById(R.id.acst_stockRestanteOut);
            TextInputEditText idText = findViewById(R.id.textInputEditText2);
            nombreProductoOut.setText("");
            stockRestanteOut.setText("");
            idText.setText("");
        }
    }
}