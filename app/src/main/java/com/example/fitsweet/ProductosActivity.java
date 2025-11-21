package com.example.fitsweet;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsweet.adapters.ProductoAdapter;
import com.example.fitsweet.database.DBHelper;
import com.example.fitsweet.models.Producto;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductosActivity extends AppCompatActivity {

    RecyclerView recyclerProductos;
    ProductoAdapter adapter;
    DBHelper dbHelper;
    Button btnAgregarProducto;
    Button btnVerCarrito;
    Button btnGestionUsuarios;
    ArrayList<Producto> listaProductos;
    boolean esAdmin;
    private ExecutorService dbExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        recyclerProductos = findViewById(R.id.recyclerProductos);
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto);
        btnVerCarrito = findViewById(R.id.btnVerCarrito);
        btnGestionUsuarios = findViewById(R.id.btnGestionUsuarios);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        dbExecutor = Executors.newSingleThreadExecutor();
        esAdmin = getIntent().getBooleanExtra("ES_ADMIN", false);
        btnAgregarProducto.setVisibility(esAdmin ? View.VISIBLE : View.GONE);
        btnGestionUsuarios.setVisibility(esAdmin ? View.VISIBLE : View.GONE);
        btnVerCarrito.setVisibility(esAdmin ? View.GONE : View.VISIBLE);
        cargarProductos();

        btnAgregarProducto.setOnClickListener(v -> mostrarDialogoAgregar());
        btnVerCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(ProductosActivity.this, CarritoActivity.class);
            startActivity(intent);
        });
        btnGestionUsuarios.setOnClickListener(v -> {
            Intent intent = new Intent(ProductosActivity.this, UsuariosActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbExecutor != null && !dbExecutor.isShutdown()) {
            dbExecutor.shutdownNow();
        }
    }

    private void cargarProductos() {
        dbExecutor.execute(() -> {
            ArrayList<Producto> productos = dbHelper.obtenerProductos();
            runOnUiThread(() -> {
                listaProductos = productos;
                adapter = new ProductoAdapter(this, listaProductos, esAdmin);
                recyclerProductos.setAdapter(adapter);
            });
        });
    }

    private void mostrarDialogoAgregar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View vista = inflater.inflate(R.layout.dialog_agregar_producto, null);
        builder.setView(vista);

        EditText etNombre = vista.findViewById(R.id.etNombreProducto);
        EditText etDescripcion = vista.findViewById(R.id.etDescripcionProducto);
        EditText etPrecio = vista.findViewById(R.id.etPrecioProducto);
        EditText etImagen = vista.findViewById(R.id.etImagenProducto);

        builder.setTitle("Agregar Producto");
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            String precioStr = etPrecio.getText().toString();
            String imagenUrl = etImagen.getText().toString();

            if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio = Double.parseDouble(precioStr);
            dbExecutor.execute(() -> {
                dbHelper.insertarProducto(nombre, descripcion, precio, imagenUrl);
                ArrayList<Producto> productosActualizados = dbHelper.obtenerProductos();
                runOnUiThread(() -> {
                    listaProductos = productosActualizados;
                    adapter = new ProductoAdapter(this, listaProductos, esAdmin);
                    recyclerProductos.setAdapter(adapter);
                    Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show();
                });
            });
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
