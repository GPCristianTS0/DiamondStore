package com.Clover.prueba.services.generators;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.dao.ConfiguracionDAO;
import com.Clover.prueba.data.models.Clientes;
import com.Clover.prueba.data.models.Configuracion;

public class CardClientGenerator {
    public CardClientGenerator() {}
    //Funcion para compartir QR de la card del cliente
    public View generarCardCliente(Context context, Clientes cliente, Bitmap codeQR){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cliente_card, null);

        //Inicializacion
        TextView txtNombre = view.findViewById(R.id.CRED_txtNombre);
        TextView txtID = view.findViewById(R.id.CRED_txtID);
        TextView txtNombreEmpresa = view.findViewById(R.id.CRED_txtEmpresa);
        ImageView image = view.findViewById(R.id.CRED_imgQR);
        Configuracion conf = new ConfiguracionDAO(context).getConfiguracion();
        //Bind de datos
        txtNombre.setText(cliente.getNombre_cliente());
        txtID.setText(String.valueOf("#"+cliente.getId_cliente()));
        txtNombreEmpresa.setText(conf.getNombreNegocio());
        image.setImageBitmap(codeQR);
        //Compartir datos
        return view;
    }
}
