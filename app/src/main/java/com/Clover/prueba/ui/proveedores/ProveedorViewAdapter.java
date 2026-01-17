package com.Clover.prueba.ui.proveedores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.models.Proveedor;

import java.util.ArrayList;

public class ProveedorViewAdapter extends RecyclerView.Adapter<ProveedorViewAdapter.ViewHolder> {
    private ArrayList<Proveedor> proveedores;
    private OnClickListener listener;

    public interface OnClickListener{
        void onClickLlamar(String numero, int position);
        void onClickWhatsapp(String numero, int position);
        void onClickModificar(Proveedor proveedor, int position);
    }

    public ProveedorViewAdapter(ArrayList<Proveedor> proveedores, OnClickListener listener) {
        this.proveedores = proveedores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProveedorViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proveedor_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProveedorViewAdapter.ViewHolder holder, int position) {
        Proveedor proveedor = proveedores.get(position);
        holder.icon.setText(proveedor.getNombre_proveedor().substring(0, 1));
        holder.txtNombreEmpresa.setText(proveedor.getNombre_proveedor());
        holder.txtDetalles.setText(proveedor.getCategoria() + " • Vendedor: " + proveedor.getNombre_vendedor());
        holder.txtDiaVisita.setText("📅 Visita: " + proveedor.getDias_visita());
        holder.btnLlamar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClickLlamar(proveedor.getTelefono(), holder.getAdapterPosition());
            }
        });
        holder.btnWhatsapp.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClickWhatsapp(proveedor.getTelefono(), holder.getAdapterPosition());
            }
        });
        holder.contenedor.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClickModificar(proveedor, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return proveedores.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreEmpresa, txtDetalles, txtDiaVisita, icon;
        ImageView btnLlamar, btnWhatsapp;
        ConstraintLayout contenedor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.PrVItem_icon);
            txtNombreEmpresa = itemView.findViewById(R.id.PrVItem_NombreEmpresa);
            txtDetalles = itemView.findViewById(R.id.PrVItem_Detalles);
            txtDiaVisita = itemView.findViewById(R.id.PrVItem_DiaVisita);
            btnLlamar = itemView.findViewById(R.id.btnLlamar);
            btnWhatsapp = itemView.findViewById(R.id.btnWhatsapp);
            contenedor = itemView.findViewById(R.id.PrVItemContener);
        }
    }

}
