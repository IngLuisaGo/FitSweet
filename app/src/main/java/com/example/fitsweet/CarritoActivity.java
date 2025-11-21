package com.example.fitsweet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsweet.R;
import com.example.fitsweet.adapters.CarritoAdapter;
import com.example.fitsweet.database.DBHelper;
import com.example.fitsweet.models.CarritoItem;

import java.util.ArrayList;

public class CarritoActivity extends AppCompatActivity {

    private TextView tvEstadoCarrito;
    private TextView tvTotalCarrito;
    private RecyclerView recyclerCarrito;
    private CarritoAdapter adapter;
    private DBHelper dbHelper;
    private ArrayList<CarritoItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        tvEstadoCarrito = findViewById(R.id.tvEstadoCarrito);
        tvTotalCarrito = findViewById(R.id.tvTotalCarrito);
        Button btnSeguirComprando = findViewById(R.id.btnSeguirComprando);
        recyclerCarrito = findViewById(R.id.recyclerCarrito);
        recyclerCarrito.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DBHelper(this);

        cargarCarrito();

        btnSeguirComprando.setOnClickListener(v -> {
            Intent intent = new Intent(CarritoActivity.this, ProductosActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void cargarCarrito() {
        items = dbHelper.obtenerCarrito();
        adapter = new CarritoAdapter(this, items, this::actualizarTotal);
        recyclerCarrito.setAdapter(adapter);
        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = dbHelper.calcularTotalCarrito();
        tvTotalCarrito.setText("Total: $" + String.format("%.2f", total));
        if (items == null || items.isEmpty()) {
            tvEstadoCarrito.setText("Tu carrito está vacío por ahora.");
        } else {
            tvEstadoCarrito.setText("Tienes " + items.size() + " productos en tu carrito.");
        }
    }
}
