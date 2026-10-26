package com.Clover.prueba.services.generators;

import static com.Clover.prueba.domain.ventas.VentasConstantes.CONST_METODO_CREDITO;
import static com.Clover.prueba.domain.ventas.VentasConstantes.CONST_METODO_EFECTIVO;
import static com.Clover.prueba.domain.ventas.VentasConstantes.CONST_METODO_TARJETA;
import static com.Clover.prueba.domain.ventas.VentasConstantes.CONST_METODO_TRANSFERENCIA;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.Clover.prueba.R;
import com.Clover.prueba.domain.configuracion.ConfiguracionControl;
import com.Clover.prueba.data.dao.ConfiguracionDAO;
import com.Clover.prueba.data.models.Abonos;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.DetalleVenta;
import com.Clover.prueba.data.models.Reparaciones;
import com.Clover.prueba.data.models.Ventas;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class TicketUtils {
    private final ConfiguracionControl configuracionControl;
    private Context context;

    public TicketUtils(Context context) {
        this.context = context;
        configuracionControl = new ConfiguracionControl(context);
    }
/*
    public void generarTicketVenta(Context context,String nombreCliente, Ventas venta, ArrayList<DetalleVenta>listaProductos, boolean waoOrGeneric){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ticket_virtual, null);

        TextView txtCliente = view.findViewById(R.id.HV_clienteOut);
        TextView txtNombre = view.findViewById(R.id.HV_nombreNegocio);
        TextView txtFecha = view.findViewById(R.id.HV_fechaOut);
        TextView txtDireccion = view.findViewById(R.id.HV_direccion);
        TextView txtDatosFiscales = view.findViewById(R.id.HV_datosFiscales);
        TextView txtHora = view.findViewById(R.id.HV_horaOut);
        TextView txtTotal = view.findViewById(R.id.HV_totalVenta);
        TextView txtProductostotal = view.findViewById(R.id.HV_totalArticulos);
        TextView txtFolio = view.findViewById(R.id.HV_txtFolio);
        TextView txtMetodoPago = view.findViewById(R.id.HV_metodoPago);
        TextView txtTipoTarjeta = view.findViewById(R.id.HV_tipoTarjeta);
        TextView txtDatosTarjeta = view.findViewById(R.id.HV_datosTarjeta);
        TextView txtImportePago = view.findViewById(R.id.HV_importePago);
        TextView txtCambio = view.findViewById(R.id.HV_cambio);

        if (nombreCliente == null) txtCliente.setText("Publico General");
        else txtCliente.setText(nombreCliente);
        if(venta.getTipo_pago().equals(CONST_METODO_TARJETA)){

        }
        LinearLayout contenedor = view.findViewById(R.id.HV_contenedorItems);
        ImageView imagen = view.findViewById(R.id.HV_imagen);
        Configuracion configuracion = configuracionControl.getConfiguracion();
        imagen.setImageURI(Uri.parse(configuracion.getRutaLogo()));
        txtNombre.setText(configuracion.getNombreNegocio());
        txtDireccion.setText(configuracion.getDireccion());
        txtDatosFiscales.setText("Tel: "+configuracion.getTelefono()+" | RFC: "+configuracion.getRfc());


        try {
            String fechaf = venta.getFecha_hora();
            DateTimeFormatter inputFormatter;
            if (fechaf.length() > 16) {
                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            } else {
                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault());
            }

            LocalDateTime fechaHora = LocalDateTime.parse(fechaf, inputFormatter);

            txtFecha.setText(fechaHora.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            txtHora.setText(fechaHora.format(DateTimeFormatter.ofPattern("HH:mm")));

        } catch (Exception e) {
            txtFecha.setText(venta.getFecha_hora());
            txtHora.setText("--:--");
            e.printStackTrace();
        }


        int piezas = 0;
        for (DetalleVenta detalleVenta : listaProductos) {
            View rowView = inflater.inflate(R.layout.ticket_item, null);
            TextView nombreProducto = rowView.findViewById(R.id.ticketItem_nombreProducto);
            TextView piezasTextView = rowView.findViewById(R.id.ticketitem_piezas);
            TextView precioTextView = rowView.findViewById(R.id.ticketItem_precio);
            nombreProducto.setText(detalleVenta.getNombre_producto()+"\n\t (x $"+detalleVenta.getPrecio()+")");
            piezasTextView.setText(String.valueOf(detalleVenta.getCantidad()));
            precioTextView.setText("$ "+detalleVenta.getPrecio()*detalleVenta.getCantidad());
            contenedor.addView(rowView);
            // Sumar las piezas
            piezas += detalleVenta.getCantidad();
        }



        // Aquí puedes poner la fecha real con DateFormat
        assert nombreCliente != null;
        Log.e("Clover_App", nombreCliente);
        if (nombreCliente.isEmpty()) txtCliente.setText("Publico General");
        else txtCliente.setText(nombreCliente);
        txtProductostotal.setText(String.valueOf(piezas));
        txtTotal.setText(String.valueOf(venta.getMonto()));
        String folio = "FOLIO DE VENTA: #";
        txtFolio.setText(folio.concat(String.valueOf(venta.getId_venta())));
        txtMetodoPago.setText(venta.getTipo_pago().toUpperCase());
        String importes = "$ ";
        if(venta.getTipo_pago().equals(CONST_METODO_TARJETA)){
            txtTipoTarjeta.setText(venta.getTipo_tarjeta().toUpperCase());
            txtDatosTarjeta.setText(venta.getBanco_tarjeta().toUpperCase().concat(" **** ").concat(venta.getNumero_tarjeta()+" (Aprob: "+venta.getNumero_aprobacion()+")"));
            txtImportePago.setText(importes.concat(String.valueOf(venta.getMonto())));

        }else if (venta.getTipo_pago().equals(CONST_METODO_CREDITO)) {
            txtTipoTarjeta.setText(String.valueOf(venta.getFrecuencia_pago()));
            String fechaLimite = "Fecha Proximo Pago: ";
            txtDatosTarjeta.setText(fechaLimite.concat(venta.getFecha_limite()));
            txtImportePago.setText(importes.concat(" 0.00"));

        }else {
            txtTipoTarjeta.setVisibility(View.GONE);
            txtDatosTarjeta.setVisibility(View.GONE);
        }
        if(venta.getTipo_pago().equals(CONST_METODO_EFECTIVO)){
            txtCambio.setText(importes.concat(String.valueOf(venta.getPago_con()-venta.getMonto())));
            txtImportePago.setText(importes.concat(String.valueOf(venta.getPago_con())));
        }
        if (venta.getTipo_pago().equals(CONST_METODO_TRANSFERENCIA)){
            txtCambio.setText(importes.concat("0.00"));
            txtImportePago.setText(importes.concat(String.valueOf(venta.getMonto())));
        }
        generarYCompartirTicket(view, configuracion);
    }
    public void generarTicketAbono(Context context, Abonos abono, String nombreCliente, boolean waoOrGeneric){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ticket_virtual_abono, null);

        String importes = "$ ";

        Configuracion configuracion = new ConfiguracionDAO(context).getConfiguracion();
        ImageView imagen = view.findViewById(R.id.imgLogoEmpresa);
        TextView txtNombre = view.findViewById(R.id.txtNombreEmpresa);
        TextView txtDireccion = view.findViewById(R.id.txtDireccionEmpresa);
        TextView txtTelefono = view.findViewById(R.id.txtTelefonoEmpresa);
        TextView txtMontoAbono = view.findViewById(R.id.CDA_txtMontoAbono);
        TextView txtFechaHora = view.findViewById(R.id.txtFechaHora);
        TextView txtNombreCliente = view.findViewById(R.id.txtNombreCliente);
        TextView txtFolio = view.findViewById(R.id.CDA_txtFolio);
        TextView txtSaldoAnterior = view.findViewById(R.id.CDA_txtSaldoAnterior);
        TextView txtSaldoRestante = view.findViewById(R.id.CDA_txtSaldoRestante);

        txtNombre.setText(configuracion.getNombreNegocio());
        txtDireccion.setText("Dirección: "+configuracion.getDireccion());
        txtTelefono.setText("Telefono: "+configuracion.getTelefono());
        if (abono.getMonto()>abono.getSaldoAnterior()){
            txtMontoAbono.setText(importes.concat(abono.getSaldoAnterior()+""));
        }else
            txtMontoAbono.setText(importes.concat(abono.getMonto()+""));
        txtNombreCliente.setText(nombreCliente);
        txtFolio.setText("#A-"+abono.getId());
        imagen.setImageURI(Uri.parse(configuracion.getRutaLogo()));
        txtFechaHora.setText(getFecha(abono.getFecha()));
        txtSaldoAnterior.setText(importes.concat(abono.getSaldoAnterior()+""));
        txtSaldoRestante.setText(importes.concat(abono.getSaldoActual()+""));

        generarYCompartirTicket(view,  configuracion, waoOrGeneric);

    }

/*
    private Uri generarYCompartirTicket(View view, Configuracion configuracion) {

        //Medicion de la pantalla
        int widthSpec = View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY); // Ancho fijo
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED); // Alto automático
        view.measure(widthSpec, heightSpec);
        int altoDelContenido = view.getMeasuredHeight();

        //Definimos tu altura mínima deseada (1920px)
        int altoMinimo = 1920;

        //ELEGIMOS EL MAYOR: Si el contenido es chico, usamos 1920. Si es grande, usamos el contenido.
        int altoFinal = Math.max(altoDelContenido, altoMinimo);


        view.layout(0, 0, view.getMeasuredWidth(), altoFinal);

        // 5. DIBUJAR (DRAW): Creamos el Bitmap y le decimos a la vista "dibújate aquí"
        float escala = 2.5f;
        return CreacionImagen(view, configuracion, altoFinal, escala);
    }
/*
    private Uri CreacionImagen(View view, Configuracion configuracion,int altoFinal, float escala) {
        int anchoReal = (int) (view.getMeasuredWidth() * escala);
        int altoReal = (int) (altoFinal * escala);

        Bitmap bitmap = Bitmap.createBitmap(anchoReal, altoReal, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        try {
            canvas.drawColor(Color.parseColor("#0D111E"));
            canvas.scale(escala, escala);
            view.draw(canvas);
            // 6. COMPARTIR: Guardamos temporalmente y lanzamos WhatsApp
            return compartirBitmap(bitmap, configuracion);
        } catch (OutOfMemoryError|Exception e) {
            Log.e("Clover_App", "Error al generar ticket: "+e.getMessage());

            if (escala <= 1.0f) {
                Toast.makeText(context, "No hay memoria suficiente para generar el ticket", Toast.LENGTH_SHORT).show();
                return null;
            }

            CreacionImagen(view, configuracion, altoReal, 1.0f);
        }
        return null;
    }
*/
    private Uri compartirBitmap(Bitmap bitmap, Configuracion configuracion, boolean wapOrGeneric) {
        Uri contentUri = null;
        try {
            //Busca la carpeta y si no existe la crea
            File cache = new File(context.getCacheDir(), "tickets");
            if (!cache.exists()) cache.mkdirs();

            //Se borrar tickets Antiguos
            File[] files = cache.listFiles();
            if (files != null)
                for (File file : files) file.delete();

            //Se crea una archivo temporal para guardar el ticket
            File newFile = new File(cache, "ticket.png");
            FileOutputStream stream = new FileOutputStream(newFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            //Se genera la Uri
            contentUri = FileProvider.getUriForFile(context, "com.Clover.prueba.fileprovider", newFile);

            if (contentUri == null) {
                Log.e("Clover_App", "Error al generar la URI");
                return null;
            }
        } catch (Exception e) {
            Log.e("Clover_App", "Error al compartir ticket"+e.getMessage());
        }
        return contentUri;
    }

    private String getFecha(String fecha){
        LocalDateTime date = LocalDateTime.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return capitalizar(date.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy", new Locale("es", "ES"))))+" "+date.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
    }
    private static String capitalizar(String string){
        if (string == null || string.isEmpty()) return "";
        return string.substring(0, 1).toUpperCase()+string.substring(1);
    }
}
