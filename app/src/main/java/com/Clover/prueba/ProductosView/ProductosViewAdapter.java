package com.Clover.prueba.ProductosView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;
import com.Clover.prueba.VentasViews.VentasViewAdapter;

import java.util.List;

import Entidades.Productos;

public class ProductosViewAdapter extends RecyclerView.Adapter<ProductosViewAdapter.ViewHolder> {
    private List<Productos> productos;
    private OnItemClickListener listener;

    public ProductosViewAdapter(List<Productos> productos, OnItemClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void OnClickEditProduct(Productos producto, int position);
    }

    @NonNull
    @Override
    public ProductosViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductosViewAdapter.ViewHolder holder, int position) {
        Productos producto = productos.get(position);
        holder.txtNombre.setText(producto.getNombre());
        holder.txtMarca.setText(producto.getMarca());
        holder.txtSeccion.setText(producto.getSeccion());
        holder.txtDescripcion.setText(producto.getDescripcion());
        holder.txtVendidos.setText(String.valueOf(producto.getVendidos()));
        holder.txtUnidades.setText(String.valueOf(producto.getStock()));
        String b = "$ " + producto.getPrecioNeto();
        holder.txtPrecioNeto.setText(b);
        String a = "$ "+producto.getPrecioPublico();
        holder.txtPrecio.setText(a);
        holder.txtCodigo.setText(producto.getId());
        holder.btn.setOnClickListener(v -> {
            if (listener != null) {
                listener.OnClickEditProduct(producto, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPrecio, txtCodigo, txtNombre, txtMarca, txtSeccion, txtDescripcion, txtVendidos, txtUnidades,txtPrecioNeto;
        Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.edit);
            txtNombre = itemView.findViewById(R.id.nombretxt);
            txtPrecio = itemView.findViewById(R.id.preciotxt);
            txtMarca = itemView.findViewById(R.id.marcaItemProducto);
            txtSeccion = itemView.findViewById(R.id.secciontxt);
            txtDescripcion = itemView.findViewById(R.id.descripcionItemProducto);
            txtVendidos = itemView.findViewById(R.id.vendidosCountItemProducto);
            txtUnidades = itemView.findViewById(R.id.cantidadtxt);
            txtCodigo = itemView.findViewById(R.id.codigoBarralbl);
            txtPrecioNeto = itemView.findViewById(R.id.precioNetoCountItemProducto);
        }
    }
}
