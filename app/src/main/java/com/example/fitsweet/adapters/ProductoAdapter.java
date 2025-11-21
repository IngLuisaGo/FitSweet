package com.example.fitsweet.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsweet.R;
import com.example.fitsweet.database.DBHelper;
import com.example.fitsweet.models.Producto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Producto> listaProductos;
    private DBHelper dbHelper;
    private boolean esAdmin;

    public ProductoAdapter(Context context, ArrayList<Producto> listaProductos, boolean esAdmin) {
        this.context = context;
        this.listaProductos = listaProductos;
        this.dbHelper = new DBHelper(context);
        this.esAdmin = esAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvDescripcion.setText(producto.getDescripcion());
        holder.tvPrecio.setText("$" + producto.getPrecio());
        cargarImagen(holder.ivProducto, producto.getImagenUrl());

        holder.btnEditar.setVisibility(esAdmin ? View.VISIBLE : View.GONE);
        holder.btnEliminar.setVisibility(esAdmin ? View.VISIBLE : View.GONE);
        holder.btnAgregarCarrito.setVisibility(esAdmin ? View.GONE : View.VISIBLE);

        if (esAdmin) {
            // 🔹 Botón Editar
            holder.btnEditar.setOnClickListener(v -> mostrarDialogoEditar(producto));

            // 🔹 Botón Eliminar
            holder.btnEliminar.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Eliminar producto")
                        .setMessage("¿Deseas eliminar " + producto.getNombre() + "?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            dbHelper.eliminarProducto(producto.getId());
                            listaProductos.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            });
        } else {
            holder.btnAgregarCarrito.setOnClickListener(v -> {
                dbHelper.agregarProductoAlCarrito(producto.getId(), 1);
                Toast.makeText(context, "Producto añadido al carrito", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void mostrarDialogoEditar(Producto producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View vista = LayoutInflater.from(context).inflate(R.layout.dialog_agregar_producto, null);
        builder.setView(vista);

        EditText etNombre = vista.findViewById(R.id.etNombreProducto);
        EditText etDescripcion = vista.findViewById(R.id.etDescripcionProducto);
        EditText etPrecio = vista.findViewById(R.id.etPrecioProducto);
        EditText etImagen = vista.findViewById(R.id.etImagenProducto);

        etNombre.setText(producto.getNombre());
        etDescripcion.setText(producto.getDescripcion());
        etPrecio.setText(String.valueOf(producto.getPrecio()));
        etImagen.setText(producto.getImagenUrl());

        builder.setTitle("Editar Producto");
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            double precio = Double.parseDouble(etPrecio.getText().toString());
            String imagenUrl = etImagen.getText().toString();

            dbHelper.actualizarProducto(producto.getId(), nombre, descripcion, precio, imagenUrl);
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setImagenUrl(imagenUrl);
            notifyDataSetChanged();
            Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvPrecio;
        ImageView ivProducto;
        ImageButton btnEditar, btnEliminar;
        View btnAgregarCarrito;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivProducto = itemView.findViewById(R.id.ivProducto);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnAgregarCarrito = itemView.findViewById(R.id.btnAgregarCarrito);
        }
    }

    private void cargarImagen(ImageView imageView, String url) {
        if (url == null || url.trim().isEmpty()) {
            imageView.setImageResource(R.drawable.logo_fitsweet);
            return;
        }
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.logo_fitsweet)
                .error(R.drawable.logo_fitsweet)
                .into(imageView);
    }
}
