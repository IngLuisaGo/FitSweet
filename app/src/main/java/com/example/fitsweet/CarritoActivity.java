package com.example.fitsweet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsweet.adapters.CarritoAdapter;
import com.example.fitsweet.database.DBHelper;
import com.example.fitsweet.models.CarritoItem;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarritoActivity extends AppCompatActivity {

    private TextView tvEstadoCarrito;
    private TextView tvTotalCarrito;
    private RecyclerView recyclerCarrito;
    private CarritoAdapter adapter;
    private DBHelper dbHelper;
    private ArrayList<CarritoItem> items;
    private ExecutorService dbExecutor;

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
        dbExecutor = Executors.newSingleThreadExecutor();

        cargarCarrito();

        btnSeguirComprando.setOnClickListener(v -> {
            Intent intent = new Intent(CarritoActivity.this, ProductosActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void cargarCarrito() {
        dbExecutor.execute(() -> {
            ArrayList<CarritoItem> carrito = dbHelper.obtenerCarrito();
            runOnUiThread(() -> {
                items = carrito;
                adapter = new CarritoAdapter(this, items, this::actualizarTotal);
                recyclerCarrito.setAdapter(adapter);
                actualizarTotal();
            });
        });
    }

    private void actualizarTotal() {
        dbExecutor.execute(() -> {
            double total = 0;
            if (items != null) {
                for (CarritoItem item : items) {
                    total += item.getTotal();
                }
            }

            int cantidadItems = items == null ? 0 : items.size();
            runOnUiThread(() -> {
                tvTotalCarrito.setText("Total: $" + String.format("%.2f", total));
                if (cantidadItems == 0) {
                    tvEstadoCarrito.setText("Tu carrito está vacío por ahora.");
                } else {
                    tvEstadoCarrito.setText("Tienes " + cantidadItems + " productos en tu carrito.");
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbExecutor != null && !dbExecutor.isShutdown()) {
            dbExecutor.shutdownNow();
        }
    }
}
