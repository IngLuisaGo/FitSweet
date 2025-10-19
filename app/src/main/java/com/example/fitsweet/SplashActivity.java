package com.example.fitsweet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Espera 2.5 segundos y pasa a la pantalla de bienvenida
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, BienvenidaActivity.class));
            finish();
        }, 2500);
    }
}
