package com.Clover.prueba.ProductosView;

import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.Clover.prueba.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import BD.Controller.ControllerProducto;
import BD.DAOs.ProductoDAO;

public class FragmentNuevaSeccion extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.productos_nuevaseccionfragment, container, false);
        View rootView = requireActivity().findViewById(android.R.id.content);


        Button btn = view.findViewById(R.id.button4);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText input = view.findViewById(R.id.crearSeccionInputFP);
                String texto = input.getText().toString();
                if (texto.isEmpty()){
                    Toast.makeText(getContext(), "Ingrese un nombre", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(crearSeccion(texto)) {
                    Toast.makeText(getContext(), "Seccion creada", Toast.LENGTH_SHORT).show();
                    dismiss();
                }else Toast.makeText(getContext(), "Error al crear seccion", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    public interface OnDimissListener{
        void onDialogCerrado();
    }
    private OnDimissListener listener;

    public void setListener(OnDimissListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener!=null){
            listener.onDialogCerrado();
        }
    }

    public FragmentNuevaSeccion() {
    }
    private boolean crearSeccion(String nombre){
        ControllerProducto controller = new ProductoDAO(getContext());
        ArrayList<String> secciones = controller.getSeccione();
        for (String seccion : secciones) {
            if (seccion.equals(nombre)) return false;
        }
        return controller.addSeccion(nombre)!=-1;
    }
}
