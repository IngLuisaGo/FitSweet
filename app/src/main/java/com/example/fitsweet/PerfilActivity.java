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

        Button cerrar = findViewById(R.id.btnCerrar);
        cerrar.setOnClickListener(v -> {
            startActivity(new Intent(this, BienvenidaActivity.class));
            finish();
        });
    }
}
