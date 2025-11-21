package com.example.fitsweet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsweet.R;
import com.example.fitsweet.database.DBHelper;
import com.example.fitsweet.models.CarritoItem;

import java.util.ArrayList;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    public interface OnCarritoChangeListener {
        void onCarritoActualizado();
    }

    private final Context context;
    private final ArrayList<CarritoItem> items;
    private final DBHelper dbHelper;
    private final OnCarritoChangeListener listener;

    public CarritoAdapter(Context context, ArrayList<CarritoItem> items, OnCarritoChangeListener listener) {
        this.context = context;
        this.items = items;
        this.dbHelper = new DBHelper(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarritoItem item = items.get(position);
        holder.tvNombre.setText(item.getNombre());
        holder.tvDescripcion.setText(item.getDescripcion());
        holder.tvPrecio.setText("$" + item.getPrecio());
        holder.tvCantidad.setText(String.valueOf(item.getCantidad()));
        holder.tvTotalLinea.setText("Total: $" + String.format("%.2f", item.getTotal()));

        holder.btnSumar.setOnClickListener(v -> actualizarCantidad(item, item.getCantidad() + 1, holder.getAdapterPosition()));
        holder.btnRestar.setOnClickListener(v -> actualizarCantidad(item, item.getCantidad() - 1, holder.getAdapterPosition()));
        holder.btnEliminar.setOnClickListener(v -> eliminarItem(item, holder.getAdapterPosition()));
    }

    private void actualizarCantidad(CarritoItem item, int nuevaCantidad, int position) {
        if (nuevaCantidad <= 0) {
            eliminarItem(item, position);
            return;
        }
        item.setCantidad(nuevaCantidad);
        dbHelper.actualizarCantidadCarrito(item.getId(), nuevaCantidad);
        notifyItemChanged(position);
        if (listener != null) listener.onCarritoActualizado();
    }

    private void eliminarItem(CarritoItem item, int position) {
        dbHelper.eliminarItemCarrito(item.getId());
        items.remove(position);
        notifyItemRemoved(position);
        if (listener != null) listener.onCarritoActualizado();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvPrecio, tvCantidad, tvTotalLinea;
        ImageButton btnSumar, btnRestar, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCarrito);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionCarrito);
            tvPrecio = itemView.findViewById(R.id.tvPrecioCarrito);
            tvCantidad = itemView.findViewById(R.id.tvCantidadCarrito);
            tvTotalLinea = itemView.findViewById(R.id.tvTotalLinea);
            btnSumar = itemView.findViewById(R.id.btnSumar);
            btnRestar = itemView.findViewById(R.id.btnRestar);
            btnEliminar = itemView.findViewById(R.id.btnEliminarItem);
        }
    }
}
