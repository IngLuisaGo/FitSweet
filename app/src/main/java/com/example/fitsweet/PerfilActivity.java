package com.example.fitsweet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Button btnVerProductos = findViewById(R.id.btnVerProductos);

        btnVerProductos.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, ProductosActivity.class);
            startActivity(intent);
        });
    }
}
