package com.Clover.prueba.ui.historialventas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;

import java.util.ArrayList;

import com.Clover.prueba.data.dao.ProductoDAO;
import com.Clover.prueba.data.controller.ControllerProducto;
import com.Clover.prueba.data.models.DetalleVenta;

public class DetallesVentaAdapter extends RecyclerView.Adapter<DetallesVentaAdapter.ViewHolder> {
    private ArrayList<DetalleVenta> detallesVentas;
    private Context context;

    ControllerProducto controllerProducto;
    public DetallesVentaAdapter(Context context, ArrayList<DetalleVenta> detallesVentas) {
        this.context = context;
        this.detallesVentas = detallesVentas;
        controllerProducto  = new ProductoDAO(context);
    }


    @NonNull
    @Override
    public DetallesVentaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.historialventas_detalles_item, parent, false);
        return new DetallesVentaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetallesVentaAdapter.ViewHolder holder, int position) {
        DetalleVenta detalleVenta = detallesVentas.get(position);
        holder.nombreProducto.setText(detalleVenta.getNombre_producto());
        holder.piezas.setText(String.valueOf(detalleVenta.getCantidad()));
        holder.precio.setText(String.valueOf("$ "+detalleVenta.getPrecio()));
    }

    @Override
    public int getItemCount() {
        return detallesVentas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreProducto, piezas, precio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.ticketItem_nombreProducto);
            piezas = itemView.findViewById(R.id.ticketitem_piezas);
            precio = itemView.findViewById(R.id.ticketItem_precio);
        }
    }
}
