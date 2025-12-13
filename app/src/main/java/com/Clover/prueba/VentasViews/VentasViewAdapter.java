package com.Clover.prueba.VentasViews;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;

import java.util.List;

import Entidades.DetalleVenta;
import Entidades.Productos;

public class VentasViewAdapter extends RecyclerView.Adapter<VentasViewAdapter.ViewHolder> {

    private List<DetalleVenta> listaProductos;
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onEliminarClick(DetalleVenta producto, int position);
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
        Log.e("Clover_App", "onBindViewHolder: "+listaProductos.toString() + " "+position);
        holder.txtNombre.setText(producto.getNombre());
        String a = "$ "+producto.getPrecioPublico();
        holder.txtPrecio.setText(a);
        holder.txtCodigo.setText(producto.getId());
        holder.btn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEliminarClick(listaProductos.get(position), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtPrecio, txtCodigo;
        Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.eliminarBtn);
            txtNombre = itemView.findViewById(R.id.nombreProducto);
            txtPrecio = itemView.findViewById(R.id.precioProducto);
            txtCodigo = itemView.findViewById(R.id.codeProductolbl);
        }
    }
}
