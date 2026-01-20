package com.Clover.prueba.ui.clientes;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        void OnWhatsappClick(String numero, int position);
        void OnLlamarClick(String numero, int position);
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
        holder.CP_item_apodo.setText(cliente.getApodo());
        holder.CP_item_direccion.setText(cliente.getDireccion());
        holder.CP_item_direccion.setMinLines(2);
        holder.CP_item_puntos.setText(String.valueOf(cliente.getPuntos()));
        holder.iconAvatar.setText(cliente.getNombre_cliente().substring(0, 1).toUpperCase());
        //Listener
        holder.c.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(cliente, holder.getAdapterPosition());
            }
        });
        holder.CP_item_btnWhatsApp.setOnClickListener(v -> {
            if (listener != null) {
                listener.OnWhatsappClick(cliente.getId_cliente(), holder.getAdapterPosition());
            }
        });
        holder.CP_item_btnLlamar.setOnClickListener(v -> {
            if (listener != null) {
                listener.OnLlamarClick(cliente.getId_cliente(), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CP_item_id, CP_item_nombre, CP_item_saldo, CP_item_apodo, CP_item_fecha, CP_item_puntos, CP_item_direccion, iconAvatar;
        ImageButton CP_item_btnLlamar, CP_item_btnWhatsApp;
        CardView c;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CP_item_id = itemView.findViewById(R.id.CP_item_id);
            CP_item_nombre = itemView.findViewById(R.id.CP_item_nombre);
            CP_item_saldo = itemView.findViewById(R.id.CP_item_saldo);
            CP_item_apodo = itemView.findViewById(R.id.CP_item_apodo);
            CP_item_fecha = itemView.findViewById(R.id.CP_item_fecha);
            CP_item_puntos = itemView.findViewById(R.id.CP_item_puntos);
            CP_item_direccion = itemView.findViewById(R.id.CP_item_direccion);
            CP_item_btnLlamar = itemView.findViewById(R.id.CP_item_btnLlamar);
            CP_item_btnWhatsApp = itemView.findViewById(R.id.CP_item_btnWhatsApp);
            iconAvatar = itemView.findViewById(R.id.iconAvatar);
            c = itemView.findViewById(R.id.CP_item);
        }
    }
}
