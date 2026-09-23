package com.Clover.prueba.ui.ventas;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.models.Productos;
import com.Clover.prueba.domain.ventas.VentasViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class DialogCalculadoraPeso extends BottomSheetDialogFragment {
    private final VentasViewModel ventasViewModel;
    private TextView txtProducto;
    private TextView txtPrecioKilo;
    private TextView txtPeso;
    private TextView txtMonto;
    private Button btnAgregar;
    private Button btnCancelar;
    private TextInputEditText edtPeso;
    private TextInputEditText edtMonto;

    public DialogCalculadoraPeso(VentasViewModel ventasViewModel) {
        // Required empty public constructor
        this.ventasViewModel = ventasViewModel;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ventas_calculadora_peso, container, false);
        //Inicializar los campos
        txtProducto = view.findViewById(R.id.CalcP_txtProducto);
        txtPrecioKilo = view.findViewById(R.id.CalcP_txtPrecioKilo);
        txtPeso = view.findViewById(R.id.CalcP_edtPeso);
        txtMonto = view.findViewById(R.id.CalcP_edtMonto);
        btnAgregar = view.findViewById(R.id.CalcP_btnAgregar);
        btnCancelar = view.findViewById(R.id.CalcP_btnCancelar);

        Productos producto = getArguments().getSerializable("producto", Productos.class);
        if (producto!=null){
            rellenarEspacios(producto);
        }
        //TextField Listener
        listenerTextField();
        //Botones Listener
        btnAgregar.setOnClickListener(v -> dismiss());
        btnCancelar.setOnClickListener(v -> dismiss());

        return view;
    }
    private void rellenarEspacios(Productos producto) {
        txtProducto.setText(producto.getNombre());
        txtPrecioKilo.setText(String.valueOf(producto.getPrecioPublico()));
    }
    private void listenerTextField(){
        edtPeso.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        edtMonto.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }
}
