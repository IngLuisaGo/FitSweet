package com.example.fitsweet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class BienvenidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        // Botones de la pantalla de bienvenida
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegistro = findViewById(R.id.btnRegistro);
        Button btnSede = findViewById(R.id.btnSede);

        // Ir al Login
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(BienvenidaActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Ir al Registro
        btnRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(BienvenidaActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        // Ir a Sede
        btnSede.setOnClickListener(v -> {
            Intent intent = new Intent(BienvenidaActivity.this, SedeActivity.class);
            startActivity(intent);
        });
    }
}
