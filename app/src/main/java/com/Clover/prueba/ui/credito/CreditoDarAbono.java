package com.Clover.prueba.ui.credito;

import static com.Clover.prueba.utils.Constantes.CONST_METODO_EFECTIVO;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_TARJETA;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_TRANSFERENCIA;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.ControllerCredito;
import com.Clover.prueba.data.controller.GastosController;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Gastos;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class CreditoDarAbono extends BottomSheetDialogFragment {
    private ControllerCredito controllerCredito;
    private Clientes cliente;
    private LinearLayout da_layoutResultado;
    private LinearLayout da_layoutExito;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.credito_abonar, container, false);
        controllerCredito = new ControllerCredito(getContext());

        txtAbono = view.findViewById(R.id.DA_txtMontoAbono);
        lblError = view.findViewById(R.id.DA_lblError);
        txtNombre = view.findViewById(R.id.DA_lblNombreCliente);
        txtTelefono= view.findViewById(R.id.DA_lblTelefonoCliente);
        txtDeuda = view.findViewById(R.id.DA_txtDeudaTotal);
        iconAvatar = view.findViewById(R.id.iconAvatar);
        recycler = view.findViewById(R.id.DA_recyclerVentas);
        txtNuevoSaldo = view.findViewById(R.id.DA_txtNuevoSaldo);
        da_layoutResultado = view.findViewById(R.id.DA_layoutResultado);
        da_layoutExito = view.findViewById(R.id.DA_layoutExito);
        btnConfirmar = view.findViewById(R.id.DA_btnConfirmarAbono);
        btnCancelar = view.findViewById(R.id.DA_btnCancelar);
        btnTicket = view.findViewById(R.id.DA_btnTicket);
        btnListo = view.findViewById(R.id.DA_btnListo);

        Button btn = view.findViewById(R.id.DA_btnBuscar);
        btn.setOnClickListener(v -> {
            TextInputEditText et = view.findViewById(R.id.DA_txtBusqueda);
            String idCliente = et.getText()+"";
            busquedaCliente(view, idCliente);
        });


        btnConfirmar.setOnClickListener(v -> {
            String monto = txtAbono.getText()+"";
            if (monto.isEmpty()){
                lblError.setText("Ingrese un monto");
                lblError.setVisibility(View.VISIBLE);
                return;
            }
            double montoAbono = 0;
            try {
                montoAbono = Double.parseDouble(monto);
            } catch (NumberFormatException e) {
                lblError.setText("Ingrese un monto válido");
                lblError.setVisibility(View.VISIBLE);
                return;
            }
            if (montoAbono<=0){
                lblError.setText("Ingrese un monto valido");
                lblError.setVisibility(View.VISIBLE);
                return;
            }
            RadioGroup radioGroup = view.findViewById(R.id.DA_radioGroupMetodo);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String tipoPago = "";

            if(selectedId == R.id.DA_rbEfectivo) tipoPago = CONST_METODO_EFECTIVO;
            if (selectedId== R.id.DA_rbTarjeta) tipoPago = CONST_METODO_TARJETA;
            if (selectedId== R.id.DA_rbTransf) tipoPago = CONST_METODO_TRANSFERENCIA;

            if(controllerCredito.darAbono(montoAbono, cliente,tipoPago)){
                Toast.makeText(getContext(), "Abono registrado", Toast.LENGTH_SHORT).show();
                da_layoutExito.setVisibility(View.VISIBLE);
                da_layoutResultado.setVisibility(View.GONE);
                btnConfirmar.setVisibility(View.GONE);
                btnTicket.setVisibility(View.VISIBLE);
                btnListo.setVisibility(View.VISIBLE);
                btnCancelar.setVisibility(View.GONE);
            }else{
                Toast.makeText(getContext(), "Error al registrar abono", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelar.setOnClickListener(v -> {dismiss();});

        btnTicket.setOnClickListener(v -> {

        });

        btnListo.setOnClickListener(v -> {dismiss();});

        Bundle bundle = getArguments();
        if (bundle!=null){
            String id = bundle.getString("idCliente");
            busquedaCliente(view, id);
        }

        return view;
    }
    private void busquedaCliente(View v, String idCliente){
        if (idCliente.isEmpty()){
            lblError.setText("Seleccione un cliente");
            lblError.setVisibility(View.VISIBLE);
            return;
        }
        Clientes cliente = controllerCredito.getCliente(idCliente);
        if (cliente==null){
            lblError.setText("Cliente no encontrado");
            lblError.setVisibility(View.VISIBLE);
            return;
        }
        this.cliente = cliente;
        if (cliente.getSaldo()==0) {
            lblError.setText("Cliente sin deudas");
            lblError.setVisibility(View.VISIBLE);
            return;
        }
        cerrarTeclado();
        lblError.setVisibility(View.GONE);
        da_layoutResultado.setVisibility(View.VISIBLE);
        rellenarDatos(v, cliente);
        abonoTxt(cliente.getSaldo());
    }
    private void abonoTxt(double monto){
        txtAbono.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String importes = "$ ";
                try {
                    double abono = Double.parseDouble(s.toString());
                    if (abono>=monto){
                        txtNuevoSaldo.setText(importes.concat(0.00+""));
                    }else if (abono>0&&abono<monto){
                        txtNuevoSaldo.setText(importes.concat((monto-abono)+""));
                    }
                } catch (NumberFormatException e) {
                    if (s.toString().isEmpty()){
                        txtNuevoSaldo.setText(importes.concat(monto+""));
                        return;
                    }
                    lblError.setText("Ingrese un monto válido");
                    lblError.setVisibility(View.VISIBLE);
                    return;
                }
                lblError.setVisibility(View.GONE);
            }
        });
    }
    private TextView lblError;
    private TextView txtNombre ;
    private TextView txtTelefono ;
    private TextView txtDeuda ;
    private TextView iconAvatar;

    private TextView txtNuevoSaldo;
    private RecyclerView recycler;
    private Button btnConfirmar, btnCancelar, btnTicket, btnListo;
    private TextInputEditText txtAbono;
    private void rellenarDatos(View v, Clientes cliente){
        txtNombre.setText(cliente.getNombre_cliente());
        txtTelefono.setText(cliente.getApodo());
        String importes = "$ ";
        txtDeuda.setText(importes.concat(String.valueOf(cliente.getSaldo())));
        txtNuevoSaldo.setText(importes.concat(String.valueOf(cliente.getSaldo())));
        iconAvatar.setText(cliente.getNombre_cliente().substring(0,1));

        recycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recycler.setAdapter(new CreditoVentasAdapter(controllerCredito.getVentas(cliente.getId_cliente())));

    }
    private void cerrarTeclado() {
        View view = this.getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
