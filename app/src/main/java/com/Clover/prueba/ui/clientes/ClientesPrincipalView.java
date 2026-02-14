package com.Clover.prueba.ui.clientes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.services.sharing.ShareManager;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import com.Clover.prueba.data.dao.interfaces.IClient;
import com.Clover.prueba.data.dao.ClientesDAO;
import com.Clover.prueba.data.models.Clientes;

public class ClientesPrincipalView extends AppCompatActivity {
    private String columnaGlobal;
    private String busquedaGlobal = "";
    private boolean deudoresGlobal;
    private final IClient controller = new ClientesDAO(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.clientes_principalview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rellenarTabla(controller.getClients());
        rellenarSpinner();
        //Funcion textField
        inputBusqueda();
        //Funcion chips deudores
        ChipGroup chipGroup = findViewById(R.id.CP_chipGroupFiltros);
        chipGroup.check(R.id.CP_chipTodos);
        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup chipGroup, @NonNull List<Integer> list) {
                if (list.get(0)==R.id.CP_chipTodos) {
                    deudoresGlobal = false;
                }
                if (list.get(0)==R.id.CP_chipDeudores) {
                    deudoresGlobal = true;
                }
                rellenarTabla(controller.getClient(columnaGlobal, busquedaGlobal, deudoresGlobal));
            }
        });
    }
    private void rellenarTabla(ArrayList<Clientes> cliente){
        ClientesPrincipalAdapter adapter;
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.CP_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        ShareManager shareManager = new ShareManager();
        adapter = new ClientesPrincipalAdapter(cliente, new ClientesPrincipalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Clientes cliente, int position) {
                Intent intent = new Intent( ClientesPrincipalView.this, ClientesPerfil.class);
                intent.putExtra("cliente", cliente);
                startActivity(intent);
            }

            @Override
            public void OnWhatsappClick(String numero, int position) {
                shareManager.abrirWhatsapp(ClientesPrincipalView.this, numero, null);
            }

            @Override
            public void OnLlamarClick(String numero, int position) {
                shareManager.abrirLlamada(ClientesPrincipalView.this, numero);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void rellenarSpinner(){
        Spinner spinerSeccion = findViewById(R.id.CP_spinnerBusqueda);
        Clientes clientes = new Clientes();
        String[] columnas = clientes.getColumn();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.productos_spiner_item, columnas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerSeccion.setAdapter(adapter);

        spinerSeccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String columna = columnas[position];
                Log.e("Clover_App", "onItemSelected: "+columna);
                if (columna.equals("ID")) columnaGlobal = "id_cliente";
                if (columna.equals("Nombre")) columnaGlobal = "nombre_cliente";
                if (columna.equals("Apodo")) columnaGlobal = "apodo";
                if (columna.equals("Saldo")) columnaGlobal = "saldo";
                if (columna.equals("Puntos")) columnaGlobal = "puntos";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void inputBusqueda(){
        TextInputEditText t = findViewById(R.id.CP_busquedaTXT);
        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                busquedaGlobal = s.toString();
                rellenarTabla(controller.getClient(columnaGlobal, busquedaGlobal, deudoresGlobal));
            }
        });
    }
    public void OnClickAgregarCPButtom(View v){
        Intent intent = new Intent(ClientesPrincipalView.this, ClientesFormulario.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rellenarTabla(controller.getClient(columnaGlobal, busquedaGlobal, deudoresGlobal));
    }
}