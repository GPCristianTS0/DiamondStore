package com.Clover.prueba.HistorialVentas;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;

import java.util.ArrayList;

import Entidades.Ventas;

public class HistorialVentasAdapter extends RecyclerView.Adapter<HistorialVentasAdapter.ViewHolder> {
    private ArrayList<Ventas> ventas;
    private OnItemClickListener listener;

    public HistorialVentasAdapter(ArrayList<Ventas> ventas, OnItemClickListener listener) {
        this.ventas = ventas;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void OnClickEditProduct(Ventas venta, int position);
    }


    @NonNull
    @Override
    public HistorialVentasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.historialventas_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialVentasAdapter.ViewHolder holder, int position) {
        Ventas venta = ventas.get(position);
        holder.id_venta.setText(String.valueOf(venta.getId_venta()));
        holder.fecha_hora.setText(venta.getFecha_hora());
        holder.monto.setText(String.valueOf("$ "+venta.getMonto()));
        holder.total_piezas.setText(String.valueOf(venta.getTotal_piezas()));
        holder.id_cliente.setText(String.valueOf(venta.getId_cliente()));
        holder.c.setOnClickListener(v -> {
            if (listener != null) {
                listener.OnClickEditProduct(venta, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return ventas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_venta;
        TextView id_cliente;
        TextView fecha_hora;
        TextView monto;
        TextView total_piezas;
        ConstraintLayout c;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_venta = itemView.findViewById(R.id.hv_idOut);
            id_cliente = itemView.findViewById(R.id.hv_apodoClienteOut);
            fecha_hora = itemView.findViewById(R.id.hv_fechaOut);
            monto = itemView.findViewById(R.id.hv_montoOut);
            total_piezas = itemView.findViewById(R.id.hv_NoProductosOut);
            c = itemView.findViewById(R.id.itemventas);
        }
    }
}
