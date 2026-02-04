package com.Clover.prueba.ui.credito;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.models.Ventas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CreditoVentasAdapter extends  RecyclerView.Adapter<CreditoVentasAdapter.ViewHolder>{
    private ArrayList<Ventas> ventas;

    public CreditoVentasAdapter(ArrayList<Ventas> ventas) {
        this.ventas = ventas;
    }

    @NonNull
    @Override
    public CreditoVentasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.credito_abono_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditoVentasAdapter.ViewHolder holder, int position) {
        String importes = "$ ";

        holder.txtFecha.setText(getFormattedDate(ventas.get(position).getFecha_hora()));
        holder.txtMonto.setText(importes.concat(String.valueOf(ventas.get(position).getMonto_pendiente())));
        holder.txtFolio.setText(String.valueOf(ventas.get(position).getId_venta()));

    }

    @Override
    public int getItemCount() {
        return ventas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFecha, txtMonto, txtFolio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFecha = itemView.findViewById(R.id.IVP_txtFecha);
            txtMonto = itemView.findViewById(R.id.IVP_txtMonto);
            txtFolio = itemView.findViewById(R.id.IVP_txtFolio);
        }
    }
    private String getFormattedDate(String fechaOriginal) {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // 2. Definimos cómo queremos que se vea
        // "dd" = día, "MMMM" = Nombre del mes completo, "HH:mm" = Hora militar sin segundos
        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MMMM/yyyy HH:mm", new Locale("es", "ES"));

        try {
            // Convertimos el String a Objeto Fecha
            Date date = formatoEntrada.parse(fechaOriginal);

            // Convertimos el Objeto Fecha al nuevo String bonito
            // Le ponemos mayúscula a la primera letra del mes (opcional)
            String fechaFinal = formatoSalida.format(date);
            return fechaFinal.substring(0, 1).toUpperCase() + fechaFinal.substring(1);

        } catch (Exception e) {
            e.printStackTrace();
            return fechaOriginal; // Si falla, regresa la original fea para que no se vea vacío
        }
    }
}
