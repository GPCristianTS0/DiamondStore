package com.Clover.prueba.ui.ventas;

import static android.view.View.VISIBLE;

import static com.Clover.prueba.utils.Constantes.CONST_EFECTIVO;
import static com.Clover.prueba.utils.Constantes.CONST_TARJETA;
import static com.Clover.prueba.utils.Constantes.CONST_TRANSFERENCIA;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.ConfiguracionControl;
import com.Clover.prueba.data.models.Configuracion;
import com.google.android.material.textfield.TextInputEditText;

public class DialogFragmentVentas extends DialogFragment {
    private ViewFlipper viewFlipper;
    private Button btnAceptar;
    private Button btnTicket;
    private Button btnEfectivo, btnTarjeta, btnTransfer, btnCredito, breturn;
    private final int total;
    private boolean pago = false;
    private String metodo;
    private ventaConfirmada ventaConfirmada;

    public interface ventaConfirmada {
        void ventaConfirmada(String metodo);
        void vaciarCarrito();
    }
    public void setVentaConfirmada(ventaConfirmada ventaConfirmada) {
        this.ventaConfirmada = ventaConfirmada;
    }
    public DialogFragmentVentas(int total) {
        this.total = total;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ventas_pago_ventas, container, false);

        inicializarVistas(view);

        btnEfectivo.setOnClickListener(v ->{
            setBtnMetodosPago(view.findViewById(R.id.PVI_btnRegresar),
                    view.findViewById(R.id.PVI_btnAceptar),
                    view.findViewById(R.id.PVI_btnCompartir),
                    CONST_EFECTIVO, v1 -> configEfectivo(view), 1);
        });
        btnTarjeta.setOnClickListener(v -> {
            setBtnMetodosPago(view.findViewById(R.id.PVI_btnRegresarT),
                    view.findViewById(R.id.PVI_btnCobrarTarjeta),
                    view.findViewById(R.id.PVI_btnCompartirT),
                    CONST_TARJETA, v1 -> configTarjeta(view), 2);
        });
        btnTransfer.setOnClickListener(v -> {
            setBtnMetodosPago(view.findViewById(R.id.PVI_btnRegresarTransf),
                    view.findViewById(R.id.PVI_btnCobrarTransf),
                    view.findViewById(R.id.PVI_btnCompartirTrans),
                    CONST_TRANSFERENCIA, v1 -> configTransfer(view), 3);
        });
        TextView textViewTotal = view.findViewById(R.id.PVI_totalCount);
        textViewTotal.setText("$ "+total);

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
    public interface ticketGenerado {
        void ticketGenerado();
    }
    private ticketGenerado ticketGenerado;
    public void setTicketGenerado(ticketGenerado ticketGenerado) {
        this.ticketGenerado = ticketGenerado;
    }
    private void inicializarVistas(View view) {
      // Contenedores
        viewFlipper = view.findViewById(R.id.PVI_viewFlipper);

        // Inicializar los botones de pago individuales
        btnEfectivo = view.findViewById(R.id.PVI_btnEfectivo);
        btnTarjeta = view.findViewById(R.id.PVI_btnTarjeta);
        btnTransfer = view.findViewById(R.id.PVI_btnTransfer);
        btnCredito = view.findViewById(R.id.PVI_btnCredito);
    }
    private void setBtnMetodosPago(View breturnL, View btnAceptarL, View btnTicketL, String metodo, View.OnClickListener listener, int flipperInt){
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
        //Metodo de pago
        this.metodo = metodo;
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
            procesarCobro();
            return;
        }
        //Valida los campos para continuar
        TextInputEditText editTextPagaCon = view.findViewById(R.id.PVI_txtPagaCon);
        String s = editTextPagaCon.getText().toString();
        if (s.isEmpty()) {
            Toast.makeText(getContext(), "Ingrese monto", Toast.LENGTH_SHORT).show();
            return;
        }
        int cantidad = Integer.parseInt(s);
        if (cantidad < total) {
            Toast.makeText(getContext(), "Monto insuficiente", Toast.LENGTH_SHORT).show();
            return;
        }
        int cambio = cantidad - total;
        TextView textViewCambio = view.findViewById(R.id.PVI_cambioCount);
        textViewCambio.setText("$ " + cambio);
        editTextPagaCon.setEnabled(false);

        //Ya que se valido se procesa la venta y el cobro
        procesarCobro();
    }
    private void configTarjeta(View v){
        if (pago){
            procesarCobro();
            return;
        }
        //Validaciones

        //Ya que se valido se procesa la venta y el cobro
        procesarCobro();
    }
    private void configTransfer(View v){
        if (pago){
            procesarCobro();
            return;
        }
        //Validaciones

        //Ya que se valido se procesa la venta y el cobro
        procesarCobro();
    }
    //Proceso para el cobro o el cierre de la venta
    private void procesarCobro(){
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
                ventaConfirmada.ventaConfirmada(metodo);
            }
        }
    }
    //Botones
    private void setBtnTicket(){
        btnTicket.setOnClickListener(v -> {
            if (ticketGenerado != null) {
                ticketGenerado.ticketGenerado();
                getParentFragmentManager().popBackStack();
                dismiss();
            }
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
