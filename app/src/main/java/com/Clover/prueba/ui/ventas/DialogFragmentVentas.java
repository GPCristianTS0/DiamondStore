package com.Clover.prueba.ui.ventas;

import static android.view.View.VISIBLE;

import static com.Clover.prueba.utils.Constantes.CONST_METODO_CREDITO;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_EFECTIVO;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_TRANSFERENCIA;
import static com.Clover.prueba.utils.Constantes.CONST_PAGO_MENSUAL;
import static com.Clover.prueba.utils.Constantes.CONST_PAGO_QUINCENAL;
import static com.Clover.prueba.utils.Constantes.CONST_PAGO_SEMANAL;
import static com.Clover.prueba.utils.Constantes.CONST_PAGO_UNICO;
import static com.Clover.prueba.utils.Constantes.CONST_METODO_TARJETA;
import static com.Clover.prueba.utils.Constantes.CONST_TARJETA_CREDITO;
import static com.Clover.prueba.utils.Constantes.CONST_TARJETA_DEBITO;
import static com.Clover.prueba.utils.Constantes.VENTA_PAGADA;
import static com.Clover.prueba.utils.Constantes.VENTA_PENDIENTE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.ConfiguracionControl;
import com.Clover.prueba.data.models.CarritoDTO;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.Ventas;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

public class DialogFragmentVentas extends DialogFragment {
    private ViewFlipper viewFlipper;
    private Button btnAceptar;
    private Button btnTicket;
    private Button btnEfectivo, btnTarjeta, btnTransfer, btnCredito, breturn;
    private boolean pago = false;

    private ventaConfirmada ventaConfirmada;
    private CarritoDTO modelCarrito;

    public interface ventaConfirmada {
        void ventaConfirmada(Ventas venta);
        void vaciarCarrito();
    }
    public void setVentaConfirmada(ventaConfirmada ventaConfirmada) {
        this.ventaConfirmada = ventaConfirmada;
    }
    public DialogFragmentVentas() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ventas_pago_ventas, container, false);

        String cliente;
        inicializarVistas(view);
        Bundle bundle = getArguments();
        if (bundle != null){
            modelCarrito = bundle.getSerializable("carrito", CarritoDTO.class);
        } else {
            cliente = "";
        }

        btnEfectivo.setOnClickListener(v ->{
            setBtnMetodosPago(view.findViewById(R.id.PVI_btnRegresar),
                    view.findViewById(R.id.PVI_btnAceptar),
                    view.findViewById(R.id.PVI_btnCompartir),
                    v1 -> configEfectivo(view), 1);
        });
        btnTarjeta.setOnClickListener(v -> {
            setBtnMetodosPago(view.findViewById(R.id.PVI_btnRegresarT),
                    view.findViewById(R.id.PVI_btnCobrarTarjeta),
                    view.findViewById(R.id.PVI_btnCompartirT),
                    v1 -> configTarjeta(view), 2);
        });
        btnTransfer.setOnClickListener(v -> {
            setBtnMetodosPago(view.findViewById(R.id.PVI_btnRegresarTransf),
                    view.findViewById(R.id.PVI_btnCobrarTransf),
                    view.findViewById(R.id.PVI_btnCompartirTrans),
                    v1 -> configTransfer(view), 3);
        });
        btnCredito.setOnClickListener(v -> {
            if (modelCarrito.getClienteNombre().isEmpty()){
                Toast.makeText(getContext(), "Agregue un cliente para pagar de esta forma", Toast.LENGTH_SHORT).show();
                return;
            }
            setBtnMetodosPago(view.findViewById(R.id.PVI_btnRegresarCredito),
                    view.findViewById(R.id.PVI_btnCobrarCredito),
                    view.findViewById(R.id.PVI_btnCompartirCredito),
                    v1 -> configCredito(view), 4);
        });

        TextView textViewTotal = view.findViewById(R.id.PVI_totalCount);
        textViewTotal.setText("$ "+modelCarrito.getTotal());

        ConfiguracionControl configuracionControl = new ConfiguracionControl(getContext());
        Configuracion config = configuracionControl.getConfiguracion();
        TextView txtBeneficiario = view.findViewById(R.id.PVI_txtNombreInfo);
        txtBeneficiario.setText(config.getBeneficiario());
        TextView txtBanco = view.findViewById(R.id.PVI_txtBancoInfo);
        txtBanco.setText(config.getBanco());
        TextView txtCuenta = view.findViewById(R.id.PVI_txtCuentaInfo);
        txtCuenta.setText(config.getCuenta());

        return view;
    }
    private void inicializarVistas(View view) {
        //Contenedores
        viewFlipper = view.findViewById(R.id.PVI_viewFlipper);

        //Inicializar los botones de pago individuales
        btnEfectivo = view.findViewById(R.id.PVI_btnEfectivo);
        btnTarjeta = view.findViewById(R.id.PVI_btnTarjeta);
        btnTransfer = view.findViewById(R.id.PVI_btnTransfer);
        btnCredito = view.findViewById(R.id.PVI_btnCredito);
    }
    private void setBtnMetodosPago(View breturnL, View btnAceptarL, View btnTicketL, View.OnClickListener listener, int flipperInt){
        //Inicializa botones
        btnTicket = (Button) btnTicketL;
        btnAceptar = (Button) btnAceptarL;
        breturn = (Button) breturnL;
        breturn.setOnClickListener(v -> setBreturn());
        btnAceptar.setOnClickListener(listener);
        //Animaciones
        viewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
        viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);
        viewFlipper.setDisplayedChild(flipperInt);
    }

    private void setBreturn(){
        viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(getContext(), R.anim.slide_in_left);
        viewFlipper.setDisplayedChild(0);
    }
    //Validaciones de metodos de pago
    private void configEfectivo(View view){
        //Detecta si ya se presiono el boton y lo cierra en caso que si
        if (pago){
            procesarCobro(null);
            return;
        }
        //Valida los campos para continuar
        TextInputEditText editTextPagaCon = view.findViewById(R.id.PVI_txtPagaCon);
        String s = editTextPagaCon.getText().toString();
        if (s.isEmpty()) {
            Toast.makeText(getContext(), "Ingrese monto", Toast.LENGTH_SHORT).show();
            return;
        }
        double cantidad = Double.parseDouble(s);
        if (cantidad < modelCarrito.getTotal()) {
            Toast.makeText(getContext(), "Monto insuficiente", Toast.LENGTH_SHORT).show();
            return;
        }
        double cambio = cantidad - modelCarrito.getTotal();
        TextView textViewCambio = view.findViewById(R.id.PVI_cambioCount);
        String cambioS = "$ ";
        textViewCambio.setText(cambioS.concat(String.valueOf(cambio)));
        editTextPagaCon.setEnabled(false);
        Ventas venta = new Ventas();
        venta.setTipo_pago(CONST_METODO_EFECTIVO);
        venta.setPago_con(cantidad);
        venta.setEstado(VENTA_PAGADA);

        //Ya que se valido se procesa la venta y el cobro
        procesarCobro(venta);
    }
    private void configTarjeta(View v){
        if (pago){
            procesarCobro(null);
            return;
        }
        //Validaciones
        TextInputEditText editTextBanco = v.findViewById(R.id.PVI_txtBanco);
        TextInputEditText editTextDigitos = v.findViewById(R.id.PVI_txtDigitos);
        TextInputEditText editTextAprobacion = v.findViewById(R.id.PVI_txtAprobacion);
        ChipGroup chipGroup = v.findViewById(R.id.PVI_chipGroupTipo);
        if (chipGroup.getCheckedChipId() == -1){
            Toast.makeText(getContext(), "Ingrese un tipo de tarjeta", Toast.LENGTH_SHORT).show();
            return;
        }
        String tipo = "";
        int id = chipGroup.getCheckedChipId();
        if (id == R.id.PVI_chipDebito){
            tipo = CONST_TARJETA_DEBITO;
        }else if (id == R.id.PVI_chipCredito){
            tipo = CONST_TARJETA_CREDITO;
        }
        Ventas venta = new Ventas();
        venta.setTipo_tarjeta(tipo);
        venta.setBanco_tarjeta(String.valueOf(editTextBanco.getText()));
        venta.setNumero_tarjeta(String.valueOf(editTextDigitos.getText()));
        venta.setNumero_aprobacion(String.valueOf(editTextAprobacion.getText()));
        venta.setTipo_pago(CONST_METODO_TARJETA);
        venta.setEstado(VENTA_PAGADA);
        //Ya que se valido se procesa la venta y el cobro
        procesarCobro(venta);
    }
    private void configTransfer(View v){
        if (pago){
            procesarCobro(null);
            return;
        }
        //Validaciones
        Ventas venta = new Ventas();
        venta.setEstado(VENTA_PAGADA);
        venta.setTipo_pago(CONST_METODO_TRANSFERENCIA);
        //Ya que se valido se procesa la venta y el cobro
        procesarCobro(venta);
    }
    private void configCredito(View v){
        if (pago){
            procesarCobro(null);
            return;
        }
        //Validaciones
        TextInputEditText diasDePlazo = v.findViewById(R.id.PVI_txtDiasCredito);
        try {
            int s = Integer.parseInt(String.valueOf(diasDePlazo.getText()));
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Ingrese los días de plazo", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioGroup radioGroup = v.findViewById(R.id.PVI_radioGroupFrecuencia);
        if (radioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(getContext(), "Seleccione la frecuencia de pago", Toast.LENGTH_SHORT).show();
            return;
        }
        Ventas venta = new Ventas();

        venta.setEstado(VENTA_PENDIENTE);
        String frecuenciaDePago = getFrecuenciaPago(radioGroup);
        venta.setDias_plazo(Integer.parseInt(String.valueOf(diasDePlazo.getText())));
        venta.setFrecuencia_pago(frecuenciaDePago);
        venta.setTipo_pago(CONST_METODO_CREDITO);

        //Ya que se valido se procesa la venta y el cobro
        procesarCobro(venta);
    }

    @NonNull
    private static String getFrecuenciaPago(RadioGroup radioGroup) {
        String frecuenciaDePago = "";
        if (radioGroup.getCheckedRadioButtonId()==R.id.PVI_rbPagoUnico){
            frecuenciaDePago = CONST_PAGO_UNICO;
        }else if (radioGroup.getCheckedRadioButtonId()==R.id.PVI_rbSemanal){
            frecuenciaDePago = CONST_PAGO_SEMANAL;
        }else if (radioGroup.getCheckedRadioButtonId()==R.id.PVI_rbQuincenal){
            frecuenciaDePago = CONST_PAGO_QUINCENAL;
        }else if (radioGroup.getCheckedRadioButtonId()==R.id.PVI_rbMensual){
            frecuenciaDePago = CONST_PAGO_MENSUAL;
        }
        return frecuenciaDePago;
    }

    //Proceso para el cobro o el cierre de la venta
    private void procesarCobro(Ventas venta){
        if (pago){
            if (ventaConfirmada != null){
                ventaConfirmada.vaciarCarrito();
            }
            getParentFragmentManager().popBackStack();
            dismiss();
        }else{
            pago = true;
            cambiarEstado();
            setBtnTicket();
            if (ventaConfirmada != null) {
                Toast.makeText(getContext(), "Venta confirmada", Toast.LENGTH_SHORT).show();
                modelCarrito.ventaConfirmada(venta);
            }
        }
    }
    //Botones
    private void setBtnTicket(){
        btnTicket.setOnClickListener(v -> {
            modelCarrito.compartirTicket();
            ventaConfirmada.vaciarCarrito();
            getParentFragmentManager().popBackStack();
            dismiss();
        });
    }
    private void cambiarEstado(){
        btnAceptar.setText("¡Listo!");
        breturn.setVisibility(View.GONE);
        btnTicket.setVisibility(VISIBLE);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().setCanceledOnTouchOutside(true);
        }
    }
}
