package com.Clover.prueba.ui.corte;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.models.CorteCaja;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterCorte extends RecyclerView.Adapter<AdapterCorte.ViewHolder> {
    private final OnClickListItemListener onClickListItemListener;
    private final ArrayList<CorteCaja> cortes;

    public interface OnClickListItemListener {
        void onClickListItem(int position);
    }

    public AdapterCorte(ArrayList<CorteCaja> cortes, OnClickListItemListener onClickListItemListener) {
        this.cortes = cortes;
        this.onClickListItemListener = onClickListItemListener;
    }

    @NonNull
    @Override
    public AdapterCorte.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.corte_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCorte.ViewHolder holder, int position) {
        CorteCaja corte = cortes.get(position);
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", new Locale("es", "MX"));
        String fechaa = corte.getFecha_cierre();
        try {
            Date fecha = formatoEntrada.parse(fechaa);
            fechaa = formatoSalida.format(fecha);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.txtFecha.setText(fechaa);
        holder.txtTotalReal.setText("Dinero en caja: "+corte.getDinero_en_caja());
        StringBuilder diferencia = new StringBuilder();
        if (corte.getDiferencia() < 0) {
            holder.txtDiferencia.setTextColor(Color.parseColor("#D32F2F"));
            diferencia.append("");
        }else if (corte.getDiferencia() > 0){
            diferencia.append("+");
            holder.txtDiferencia.setTextColor(Color.parseColor("#388E3C"));
        }else {
            holder.txtDiferencia.setTextColor(holder.itemView.getResources().getColor(R.color.black));
        }
        diferencia.append(corte.getDiferencia());
        holder.txtDiferencia.setText(diferencia);
        holder.c.setOnClickListener(v -> {
            if (onClickListItemListener != null) {
                onClickListItemListener.onClickListItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cortes.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFecha;
        TextView txtTotalReal;
        TextView txtDiferencia;
        CardView c;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFecha = itemView.findViewById(R.id.HC_txtFecha);
            txtTotalReal = itemView.findViewById(R.id.HC_txtTotalReal);
            txtDiferencia = itemView.findViewById(R.id.HC_txtDiferencia);
            c = itemView.findViewById(R.id.HC_View);
        }
    }
}
