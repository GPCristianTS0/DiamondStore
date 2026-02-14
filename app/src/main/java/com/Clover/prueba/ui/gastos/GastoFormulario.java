package com.Clover.prueba.ui.gastos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.Clover.prueba.R;
import com.Clover.prueba.domain.gastos.GastosController;
import com.Clover.prueba.data.models.Gastos;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class GastoFormulario extends BottomSheetDialogFragment {
    private GastosController controllerGastos;
    private int provedor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gasto_agregar, container, false);
        View rootView = requireActivity().findViewById(android.R.id.content);

        controllerGastos = new GastosController(getContext());
        controllerGastos.prepararDatosSpinner();
        Button btnGuardarGasto = view.findViewById(R.id.btnGuardarGasto);
        btnGuardarGasto.setOnClickListener(v -> {
            Gastos gasto = getGasto(view);
            if (gasto==null){
                return;
            }
            gasto.setId_proveedor(provedor);
            if (controllerGastos.addGasto(gasto)){
                Toast.makeText(getContext(), "Gasto agregado", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), "Error al agregar gasto", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });
        rellenarSpinner(view);
        return view;
    }
    private void rellenarSpinner(View v){
        Spinner spProveedoresGasto = v.findViewById(R.id.spProveedoresGasto);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), R.layout.productos_spiner_item, controllerGastos.getNombresProveedores());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProveedoresGasto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                provedor = controllerGastos.getIdProveedor(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spProveedoresGasto.setAdapter(adapter);
    }
    private Gastos getGasto(View v){
        TextInputEditText etMontoGasto = v.findViewById(R.id.etMontoGasto);
        TextInputEditText etDescripcionGasto = v.findViewById(R.id.etDescripcionGasto);
        Spinner spProveedoresGasto = v.findViewById(R.id.spProveedoresGasto);
        RadioGroup rgMetodoPago = v.findViewById(R.id.rgMetodoPago);
        //Validacion de datos y asignacion
        Gastos gasto = new Gastos();
        String monto = etMontoGasto.getText().toString();
        if (monto.isEmpty()){
            Toast.makeText(getContext(), "El monto es requerido", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (etDescripcionGasto.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "La descripcion es requerida", Toast.LENGTH_SHORT).show();
            return null;
        }
        gasto.setMonto(Double.parseDouble(etMontoGasto.getText().toString()));
        gasto.setDescripcion(etDescripcionGasto.getText().toString());
        gasto.setId_proveedor(spProveedoresGasto.getSelectedItemPosition());
        gasto.setMetodo_pago(rgMetodoPago.getCheckedRadioButtonId() == R.id.rbEfectivo ? "Efectivo" : "Transferencia");
        return gasto;
    }
    @Override
    public int getTheme() {
        return com.google.android.material.R.style.Theme_MaterialComponents_Light_BottomSheetDialog;
    }
}
