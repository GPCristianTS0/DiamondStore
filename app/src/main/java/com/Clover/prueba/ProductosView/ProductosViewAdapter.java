package com.Clover.prueba.ProductosView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Clover.prueba.R;

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
                .inflate(R.layout.producto_item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductosViewAdapter.ViewHolder holder, int position) {
        Productos producto = productos.get(position);
        if (producto.getRutaImagen()!=null)
            holder.imagen.setImageURI(Uri.parse(producto.getRutaImagen()));
        else
            holder.imagen.setImageResource(R.drawable.agregar_imgaen);
        holder.txtNombre.setText(producto.getNombre());
        holder.txtMarca.setText(producto.getMarca());
        holder.txtSeccion.setText(producto.getSeccion());
        String a = "$ "+producto.getPrecioPublico();
        holder.txtPrecio.setText(a);
        holder.txtCodigo.setText(producto.getId());
        holder.c.setOnClickListener(v -> {
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
        TextView txtPrecio, txtCodigo, txtNombre, txtMarca, txtSeccion;
        ConstraintLayout c;
        ImageView imagen;
        public ViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imageItemProducto);
            txtNombre = itemView.findViewById(R.id.nombretxt);
            txtPrecio = itemView.findViewById(R.id.preciotxt);
            txtMarca = itemView.findViewById(R.id.marcaItemProducto);
            txtSeccion = itemView.findViewById(R.id.secciontxt);
            c = itemView.findViewById(R.id.itemproductos);
            txtCodigo = itemView.findViewById(R.id.codigoBarralbl);
        }
    }
}
