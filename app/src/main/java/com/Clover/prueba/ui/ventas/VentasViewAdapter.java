package com.Clover.prueba.ui.ventas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;

import java.util.List;

import com.Clover.prueba.data.models.DetalleVenta;
import com.Clover.prueba.data.models.Productos;

public class VentasViewAdapter extends RecyclerView.Adapter<VentasViewAdapter.ViewHolder> {

    private List<DetalleVenta> listaProductos;
    private OnItemClickListener listener;



    public interface OnItemClickListener {
        void onAgregarClick(DetalleVenta producto, int position);
        void onDisminuirClick(DetalleVenta producto, int position);
    }

    public VentasViewAdapter(List<DetalleVenta> listaProductos, OnItemClickListener listener) {
        this.listaProductos = listaProductos;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ventas_items_ventaview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Productos producto = listaProductos.get(position).getProducto();
        holder.txtNombre.setText(producto.getNombre());
        String a = "$ "+producto.getPrecioPublico();
        holder.contadorPiezas.setText(String.valueOf(listaProductos.get(position).getCantidad()));
        holder.txtPrecio.setText(a);
        holder.txtCodigo.setText(producto.getId());
        holder.btn.setOnClickListener(v -> {
            int posReal = holder.getBindingAdapterPosition();
            if (listener != null) {
                listener.onAgregarClick(listaProductos.get(posReal), holder.getAdapterPosition());
            }
        });
        holder.dismPiezas.setOnClickListener(v ->{
            int posReal = holder.getBindingAdapterPosition();
            if (listener != null ) {
                listener.onDisminuirClick(listaProductos.get(posReal), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtPrecio, txtCodigo, contadorPiezas;
        Button btn, dismPiezas;

        public ViewHolder(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.addProductoBtn);
            txtNombre = itemView.findViewById(R.id.nombreProducto);
            txtPrecio = itemView.findViewById(R.id.precioProducto);
            txtCodigo = itemView.findViewById(R.id.codeProductolbl);
            contadorPiezas = itemView.findViewById(R.id.contadorProductosVentasView);
            dismPiezas = itemView.findViewById(R.id.dismProductoBtn);
        }
    }
}
