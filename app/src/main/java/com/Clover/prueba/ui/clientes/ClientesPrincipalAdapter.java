package com.Clover.prueba.ui.clientes;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;

import java.util.ArrayList;

import com.Clover.prueba.data.models.Clientes;

public class ClientesPrincipalAdapter extends RecyclerView.Adapter<ClientesPrincipalAdapter.ViewHolder> {
    private ArrayList<Clientes> clientes;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Clientes cliente, int position);
    }


    public ClientesPrincipalAdapter (ArrayList<Clientes> clientes, OnItemClickListener listener) {
        this.clientes = clientes;
        this.listener = listener;
    }



    @NonNull
    @Override
    public ClientesPrincipalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clientes_itemprincipal, parent, false);
        return new ClientesPrincipalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientesPrincipalAdapter.ViewHolder holder, int position) {
        Clientes cliente = clientes.get(position);
        holder.CP_item_id.setText(String.valueOf(cliente.getId_cliente()));
        holder.CP_item_nombre.setText(cliente.getNombre_cliente());
        if (cliente.getSaldo()>0) {
            holder.CP_item_saldo.setText("-$" + cliente.getSaldo());
            holder.CP_item_saldo.setTextColor(Color.parseColor("#FF0000"));
        }else{
            holder.CP_item_saldo.setText("$" + cliente.getSaldo());
            holder.CP_item_saldo.setTextColor(Color.parseColor("#008000"));
        }
        holder.c.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(cliente, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CP_item_id, CP_item_nombre, CP_item_saldo;
        ConstraintLayout c;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CP_item_id = itemView.findViewById(R.id.CP_item_id);
            CP_item_nombre = itemView.findViewById(R.id.CP_item_nombre);
            CP_item_saldo = itemView.findViewById(R.id.CP_item_saldo);
            c = itemView.findViewById(R.id.CP_item);
        }
    }
}
