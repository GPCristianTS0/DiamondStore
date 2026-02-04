package com.Clover.prueba.ui.credito;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.data.controller.ControllerCredito;
import com.Clover.prueba.data.models.Clientes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class CreditoAdapterMain extends  RecyclerView.Adapter<CreditoAdapterMain.ViewHolder>{
    private ControllerCredito controllerCredito;
    private ArrayList<Clientes> clientes;
    private ListenerCreditoMain listener;

    public interface ListenerCreditoMain {
        void onLlamar(Clientes cliente);
        void onWhatsapp(Clientes cliente);
        void onAbonar(Clientes cliente);
    }
    public CreditoAdapterMain(ArrayList<Clientes> clientes, ListenerCreditoMain listener) {
        this.clientes = clientes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CreditoAdapterMain.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.credito_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditoAdapterMain.ViewHolder holder, int position) {
        String importes = "$ ";
        holder.txtNombre.setText(clientes.get(position).getNombre_cliente());
        holder.txtDeuda.setText(importes.concat(String.valueOf(clientes.get(position).getSaldo())));
        String ultimoAbono ="Ultimo abono: ";
        holder.txtUltimoAbono.setText(ultimoAbono.concat(fechaParse(clientes.get(position).getUltimoAbono(), false)));
        String fechaLimite ="Vence: ";
        Log.d("Clover_App", clientes.get(position).getFechaLimite());
        holder.txtFechaLimite.setText(fechaLimite.concat(fechaParse(clientes.get(position).getFechaLimite(), true)));
        holder.btnLlamar.setOnClickListener(v -> listener.onLlamar(clientes.get(position)));
        holder.btnWhatsapp.setOnClickListener(v -> listener.onWhatsapp(clientes.get(position)));
        holder.btnAbonar.setOnClickListener(v -> listener.onAbonar(clientes.get(position)));
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtFechaLimite, txtDeuda, txtUltimoAbono;
        Button btnAbonar;
        ImageButton btnLlamar, btnWhatsapp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre=itemView.findViewById(R.id.ICD_txtNombre);
            txtFechaLimite=itemView.findViewById(R.id.ICD_txtFechaLimite);
            txtDeuda=itemView.findViewById(R.id.ICD_txtMonto);
            txtUltimoAbono=itemView.findViewById(R.id.ICD_txtUltimoMov);
            btnLlamar=itemView.findViewById(R.id.ICD_btnLlamar);
            btnWhatsapp=itemView.findViewById(R.id.ICD_btnWhatsapp);
            btnAbonar=itemView.findViewById(R.id.ICD_btnAbonar);

        }
    }
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter INPUT_FORMAT_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("es", "ES"));
    private String fechaParse(String fechaOld, boolean formato2){
        if (fechaOld == null || fechaOld.isEmpty()) return "---";
        try {
            // A. Convertimos el String de la BD a Objeto Fecha
            LocalDateTime date;
            LocalDate date2;
            String fechaBonita ;
            if (!formato2) {
                date = LocalDateTime.parse(fechaOld, INPUT_FORMAT);
                fechaBonita = OUTPUT_FORMAT.format(date);
            }else {
                date2 = LocalDate.parse(fechaOld, INPUT_FORMAT_2);
                fechaBonita = OUTPUT_FORMAT.format(date2);
            }

            return capitalizar(fechaBonita);

        } catch (Exception e) {
            return fechaOld;
        }
    }
    private static String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}
