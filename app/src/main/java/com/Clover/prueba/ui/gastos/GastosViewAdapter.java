package com.Clover.prueba.ui.gastos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.models.Gastos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GastosViewAdapter extends RecyclerView.Adapter<GastosViewAdapter.ViewHolder> {
    private ArrayList<Gastos> gastos;
    private OnItemClickListener listener;

    public GastosViewAdapter(ArrayList<Gastos> gastos, OnItemClickListener listener) {
        this.gastos = gastos;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void OnClickGastos(Gastos gasto, int position);
    }

    @NonNull
    @Override
    public GastosViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gastos_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastosViewAdapter.ViewHolder holder, int position) {
        Gastos gasto = gastos.get(position);
        String soloHora = gasto.getFecha_hora().substring(11, 16);
        holder.GP_itemFechaCorta.setText(soloHora);
        holder.GP_itemDescripcion.setText(gasto.getDescripcion());
        holder.GP_itemSubtituloMov.setText(gasto.getMetodo_pago());
        holder.GP_itemMontoMov.setText(String.valueOf(gasto.getMonto()));
        holder.GP_itemContener.setOnClickListener(v -> {
            if (listener != null) {
                listener.OnClickGastos(gasto, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView GP_itemFechaCorta;
        TextView GP_itemDescripcion;
        TextView GP_itemSubtituloMov;
        TextView GP_itemMontoMov;
        ConstraintLayout GP_itemContener;


        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            GP_itemFechaCorta = itemView.findViewById(R.id.GP_itemFechaCorta);
            GP_itemDescripcion = itemView.findViewById(R.id.GP_itemDescripcion);
            GP_itemSubtituloMov = itemView.findViewById(R.id.GP_itemSubtituloMov);
            GP_itemMontoMov = itemView.findViewById(R.id.GP_itemMontoMov);
            GP_itemContener = itemView.findViewById(R.id.GP_itemContener);
        }
    }
}
