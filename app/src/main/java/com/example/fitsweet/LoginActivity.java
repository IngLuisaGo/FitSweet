package com.example.fitsweet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitsweet.database.DBHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText txtCorreoLogin, txtContrasenaLogin;
    private Button btnIniciarSesion, btnVolverLogin, btnRegistrarse;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCorreoLogin = findViewById(R.id.txtCorreoLogin);
        txtContrasenaLogin = findViewById(R.id.txtContrasenaLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnVolverLogin = findViewById(R.id.btnVolverLogin);
        dbHelper = new DBHelper(this);

        // Acción del botón Iniciar Sesión
        btnIniciarSesion.setOnClickListener(v -> {
            String correo = txtCorreoLogin.getText().toString().trim();
            String contrasena = txtContrasenaLogin.getText().toString().trim();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                String rol = dbHelper.obtenerRolPorCredenciales(correo, contrasena);
                if (rol == null) {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "Inicio de sesión exitoso. ¡Bienvenida de nuevo a FitSweet 🍓!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ProductosActivity.class);
                intent.putExtra("ES_ADMIN", "admin".equalsIgnoreCase(rol));
                startActivity(intent);
                finish();
            }
        });

        // Acción del botón Registrarse
        btnRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroActivity.class);
            startActivity(intent);
        });

        // Acción del botón Volver
        btnVolverLogin.setOnClickListener(v -> finish());

    }
}
