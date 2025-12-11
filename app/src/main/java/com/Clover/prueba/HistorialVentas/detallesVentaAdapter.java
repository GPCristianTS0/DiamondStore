package com.Clover.prueba.HistorialVentas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;

import java.util.ArrayList;

import BD.DAOs.ProductoDAO;
import BD.Controller.ControllerProducto;
import Entidades.DetalleVenta;
import Entidades.Productos;

public class detallesVentaAdapter extends RecyclerView.Adapter<detallesVentaAdapter.ViewHolder> {
    private ArrayList<DetalleVenta> detallesVentas;
    private Context context;
    public detallesVentaAdapter(Context context, ArrayList<DetalleVenta> detallesVentas) {
        controllerProducto = new ProductoDAO( context);
        this.context = context;
        this.detallesVentas = detallesVentas;
    }


    @NonNull
    @Override
    public detallesVentaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.historialventas_detalles_item, parent, false);
        return new detallesVentaAdapter.ViewHolder(view);
    }

    ControllerProducto controllerProducto ;
    @Override
    public void onBindViewHolder(@NonNull detallesVentaAdapter.ViewHolder holder, int position) {
        DetalleVenta detalleVenta = detallesVentas.get(position);
        Log.e("Clover_App", detalleVenta.getId_producto());
        Productos producto = controllerProducto.getProductoCode(detalleVenta.getId_producto());
        holder.nombreProducto.setText(producto.getNombre());
        holder.piezas.setText(String.valueOf(detalleVenta.getCantidad()));
        holder.precio.setText(String.valueOf("$ "+detalleVenta.getPrecio()));
        Log.e("Clover_App", "onBindViewHolder: "+detalleVenta.toString());
    }

    @Override
    public int getItemCount() {
        return detallesVentas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreProducto, piezas, precio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.hvD_nombreProducto);
            piezas = itemView.findViewById(R.id.hvD_piezas);
            precio = itemView.findViewById(R.id.hvD_precio);
        }
    }
}
