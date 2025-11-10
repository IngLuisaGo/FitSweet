package com.example.fitsweet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText txtCorreoLogin, txtContrasenaLogin;
    private Button btnIniciarSesion, btnVolverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCorreoLogin = findViewById(R.id.txtCorreoLogin);
        txtContrasenaLogin = findViewById(R.id.txtContrasenaLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnVolverLogin = findViewById(R.id.btnVolverLogin);

        // Acción del botón Iniciar Sesión
        btnIniciarSesion.setOnClickListener(v -> {
            String correo = txtCorreoLogin.getText().toString().trim();
            String contrasena = txtContrasenaLogin.getText().toString().trim();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Inicio de sesión exitoso. ¡Bienvenida de nuevo a FitSweet 🍓!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, BienvenidaActivity.class));
                finish();
            }
        });

        // Acción del botón Volver
        btnVolverLogin.setOnClickListener(v -> finish());
        Intent intent = new Intent(LoginActivity.this, ProductosActivity.class);
        startActivity(intent);
        finish();

    }
}
