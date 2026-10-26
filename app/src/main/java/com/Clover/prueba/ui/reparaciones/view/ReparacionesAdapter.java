package com.Clover.prueba.ui.reparaciones.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.models.Reparaciones;

import java.util.ArrayList;
import java.util.Locale;

public class ReparacionesAdapter extends RecyclerView.Adapter<ReparacionesAdapter.ViewHolder> {

    private ArrayList<Reparaciones> listaReparaciones;
    private OnReparacionClickListener listener;

    public interface OnReparacionClickListener {
        void onReparacionClick(Reparaciones reparacion, int position);
    }

    public ReparacionesAdapter(ArrayList<Reparaciones> listaReparaciones, OnReparacionClickListener listener) {
        this.listaReparaciones = listaReparaciones;
        this.listener = listener;
    }

    public void setLista(ArrayList<Reparaciones> nuevaLista) {
        this.listaReparaciones = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reparacion_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reparaciones rep = listaReparaciones.get(position);

        holder.tvFolio.setText(String.format(Locale.getDefault(), "#REP-%03d", rep.getId_reparacion()));

        if (rep.getFecha_ingreso() != null && !rep.getFecha_ingreso().isEmpty()) {
            holder.tvFecha.setText("• " + rep.getFecha_ingreso());
            holder.tvFecha.setVisibility(View.VISIBLE);
        } else {
            holder.tvFecha.setVisibility(View.GONE);
        }

        String estado = rep.getEstado() != null ? rep.getEstado().toUpperCase() : "RECIBIDO";
        holder.tvEstadoBadge.setText(estado);
        aplicarColorEstado(holder.tvEstadoBadge, estado);

        String equipoYColor = rep.getEquipo() != null ? rep.getEquipo() : "Equipo Sin Especificar";
        if (rep.getColor() != null && !rep.getColor().trim().isEmpty()) {
            equipoYColor += " - " + rep.getColor();
        }
        holder.tvEquipo.setText(equipoYColor);

        String clienteText = rep.getId_cliente() != null ? rep.getId_cliente() : "Cliente General";
        holder.tvCliente.setText("Cliente: " + clienteText);

        String fallaText = rep.getFalla_reportada() != null && !rep.getFalla_reportada().isEmpty()
                ? rep.getFalla_reportada()
                : "Sin falla detallada";
        holder.tvFalla.setText("Falla: " + fallaText);

        if (rep.getDiagnostico() != null && !rep.getDiagnostico().trim().isEmpty()) {
            holder.tvDiagnostico.setText("Diag: " + rep.getDiagnostico());
            holder.tvDiagnostico.setVisibility(View.VISIBLE);
        } else {
            holder.tvDiagnostico.setVisibility(View.GONE);
        }

        holder.tvMontoTotal.setText(String.valueOf(rep.getMonto_total()));
        holder.tvAnticipo.setText(String.valueOf(rep.getAnticipo()));
        holder.tvMontoPendiente.setText(String.valueOf(rep.getMonto_pendiente()));

        holder.cardItem.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onReparacionClick(rep, pos);
                }
            }
        });
    }

    private void aplicarColorEstado(TextView tvBadge, String estado) {
        switch (estado) {
            case "EN REPARACION":
            case "EN REPARACIÓN":
            case "PROCESO":
                tvBadge.setBackgroundColor(Color.parseColor("#E3F2FD"));
                tvBadge.setTextColor(Color.parseColor("#1976D2"));
                break;
            case "LISTO":
            case "TERMINADO":
                tvBadge.setBackgroundColor(Color.parseColor("#E8F5E9"));
                tvBadge.setTextColor(Color.parseColor("#388E3C"));
                break;
            case "ENTREGADO":
                tvBadge.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvBadge.setTextColor(Color.parseColor("#616161"));
                break;
            case "CANCELADO":
                tvBadge.setBackgroundColor(Color.parseColor("#FFEBEE"));
                tvBadge.setTextColor(Color.parseColor("#D32F2F"));
                break;
            case "RECIBIDO":
            default:
                tvBadge.setBackgroundColor(Color.parseColor("#FFF8E1"));
                tvBadge.setTextColor(Color.parseColor("#FFA000"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listaReparaciones != null ? listaReparaciones.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardItem;
        TextView tvFolio, tvFecha, tvEstadoBadge, tvEquipo, tvCliente, tvFalla, tvDiagnostico;
        TextView tvMontoTotal, tvAnticipo, tvMontoPendiente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardItem = itemView.findViewById(R.id.cardReparacionItem);
            tvFolio = itemView.findViewById(R.id.tvFolioReparacion);
            tvFecha = itemView.findViewById(R.id.tvFechaIngreso);
            tvEstadoBadge = itemView.findViewById(R.id.tvEstadoBadge);
            tvEquipo = itemView.findViewById(R.id.tvEquipoModelo);
            tvCliente = itemView.findViewById(R.id.tvClienteNombre);
            tvFalla = itemView.findViewById(R.id.tvFallaReportada);
            tvDiagnostico = itemView.findViewById(R.id.tvDiagnostico);
            tvMontoTotal = itemView.findViewById(R.id.tvMontoTotal);
            tvAnticipo = itemView.findViewById(R.id.tvAnticipo);
            tvMontoPendiente = itemView.findViewById(R.id.tvMontoPendiente);
        }
    }
}