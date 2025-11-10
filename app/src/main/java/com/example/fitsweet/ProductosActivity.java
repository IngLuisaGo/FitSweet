package com.example.fitsweet;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ProductosActivity extends AppCompatActivity {

    RecyclerView recyclerProductos;
    ProductoAdapter adapter;
    DBHelper dbHelper;
    Button btnAgregarProducto;
    ArrayList<Producto> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        recyclerProductos = findViewById(R.id.recyclerProductos);
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        cargarProductos();

        btnAgregarProducto.setOnClickListener(v -> mostrarDialogoAgregar());
    }

    private void cargarProductos() {
        listaProductos = dbHelper.obtenerProductos();
        adapter = new ProductoAdapter(this, listaProductos);
        recyclerProductos.setAdapter(adapter);
    }

    private void mostrarDialogoAgregar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View vista = inflater.inflate(R.layout.dialog_agregar_producto, null);
        builder.setView(vista);

        EditText etNombre = vista.findViewById(R.id.etNombreProducto);
        EditText etDescripcion = vista.findViewById(R.id.etDescripcionProducto);
        EditText etPrecio = vista.findViewById(R.id.etPrecioProducto);

        builder.setTitle("Agregar Producto");
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            String precioStr = etPrecio.getText().toString();

            if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio = Double.parseDouble(precioStr);
            dbHelper.insertarProducto(nombre, descripcion, precio);
            cargarProductos(); // refresca el RecyclerView
            Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
