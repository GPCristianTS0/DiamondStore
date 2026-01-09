package com.Clover.prueba.ClientesView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;

import java.util.ArrayList;

import Entidades.Clientes;

public class ClientesPrincipalAdapter extends RecyclerView.Adapter<ClientesPrincipalAdapter.ViewHolder> {
    Context context;
    ArrayList<Clientes> clientes;

    public ClientesPrincipalAdapter(Context context, ArrayList<Clientes> clientes) {
        this.context = context;
        this.clientes = clientes;
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
        holder.CP_item_saldo.setText(String.valueOf(cliente.getSaldo()));
        if (cliente.getSaldo() > 0) holder.CP_item_saldo.setTextColor(Color.RED);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CP_item_id, CP_item_nombre, CP_item_saldo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CP_item_id = itemView.findViewById(R.id.CP_item_id);
            CP_item_nombre = itemView.findViewById(R.id.CP_item_nombre);
            CP_item_saldo = itemView.findViewById(R.id.CP_item_saldo);
        }
    }
}
