package com.example.fitsweet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CarritoActivity extends AppCompatActivity {

    private TextView tvEstadoCarrito;
    private TextView tvTotalCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        tvEstadoCarrito = findViewById(R.id.tvEstadoCarrito);
        tvTotalCarrito = findViewById(R.id.tvTotalCarrito);
        Button btnSeguirComprando = findViewById(R.id.btnSeguirComprando);

        // Información inicial del carrito
        tvEstadoCarrito.setText("Tu carrito está vacío por ahora.");
        tvTotalCarrito.setText("Total: $0.00");

        btnSeguirComprando.setOnClickListener(v -> {
            Intent intent = new Intent(CarritoActivity.this, ProductosActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
