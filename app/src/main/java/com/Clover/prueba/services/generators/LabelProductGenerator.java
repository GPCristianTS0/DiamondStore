package com.Clover.prueba.services.generators;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.models.Configuracion;
import com.Clover.prueba.data.models.Productos;

public class LabelProductGenerator {
    private final GeneradorQR qr;
    private final LayoutInflater layoutInflater;


    public LabelProductGenerator(Context context, GeneradorQR qr) {
        this.qr = qr;
        layoutInflater = LayoutInflater.from(context);
    }

    //Funcion para generar el view de la etiqueta del producto
    public View getView(Productos productos, Configuracion conf) {
        View view = layoutInflater.inflate(R.layout.productos_etiqueta, null);

        //Inicializacion
        TextView txtNombre = view.findViewById(R.id.EP_txtNombre);
        ImageView image = view.findViewById(R.id.EP_imgQR);
        TextView txtCodigo = view.findViewById(R.id.EP_txtCodigo);
        TextView txtPrecio = view.findViewById(R.id.EP_txtPrecio);
        ImageView imgLogo = view.findViewById(R.id.EP_imgLogo);
        TextView txtEmpresa = view.findViewById(R.id.EP_txtEmpresa);
        TextView txtTelefono = view.findViewById(R.id.EP_txtTelefono);

        //Bind de datos
        String importe = "$ ";
        txtNombre.setText(productos.getNombre());
        txtCodigo.setText(productos.getId());
        txtPrecio.setText(importe.concat(productos.getPrecioPublico()+""));
        if (conf.getRutaLogo()!=null) imgLogo.setImageURI(Uri.parse(conf.getRutaLogo()));
        txtEmpresa.setText(conf.getNombreNegocio());
        txtTelefono.setText(conf.getTelefono());
        image.setImageBitmap(qr.generarQR(productos.getId()));

        return view;
    }
}
