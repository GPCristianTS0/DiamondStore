package com.Clover.prueba.ui.clientes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.models.Abonos;
import com.Clover.prueba.utils.FormatterFechas;

import java.util.ArrayList;

public class DetalleAbonoAdapter extends RecyclerView.Adapter<DetalleAbonoAdapter.ViewHolder> {
    private ArrayList<Abonos> abonos;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public DetalleAbonoAdapter(ArrayList<Abonos> abonos, OnItemClickListener listener) {
        this.abonos = abonos;
        this.listener = listener;
    }
    @NonNull
    @Override
    public DetalleAbonoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clientes_abonos_item, parent, false);
        return new DetalleAbonoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetalleAbonoAdapter.ViewHolder holder, int position) {
        Abonos abono = abonos.get(position);
        String importes = "$ ";
        String fecha = FormatterFechas.formatDate(abono.getFecha(), "dd MMMM yyyy HH:mm", false);
        holder.txtFecha.setText(fecha);
        holder.txtMonto.setText(importes.concat(abono.getMonto()+""));
        holder.txtTipoPago.setText(abono.getTipoPago());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return abonos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFecha;
        TextView txtMonto;
        TextView txtTipoPago;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFecha = itemView.findViewById(R.id.txtFechaAbono);
            txtMonto = itemView.findViewById(R.id.txtMontoItem);
            txtTipoPago = itemView.findViewById(R.id.txtMetodoPago);
            layout = itemView.findViewById(R.id.CAI_layout);

        }
    }
}
