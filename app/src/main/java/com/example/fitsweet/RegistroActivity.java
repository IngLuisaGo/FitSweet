package com.example.fitsweet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    private EditText txtNombre, txtCorreo, txtContrasena;
    private Button btnRegistrar, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Vinculamos los elementos del layout con las variables Java
        txtNombre = findViewById(R.id.txtNombre);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolver);

        // Evento para el botón Registrar
        btnRegistrar.setOnClickListener(v -> {
            String nombre = txtNombre.getText().toString().trim();
            String correo = txtCorreo.getText().toString().trim();
            String contrasena = txtContrasena.getText().toString().trim();

            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Registro exitoso. ¡Bienvenida a FitSweet, " + nombre + "!", Toast.LENGTH_LONG).show();
            }
        });

        // Evento para volver a la pantalla anterior
        btnVolver.setOnClickListener(v -> finish());
    }
}
