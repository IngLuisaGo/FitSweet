package com.example.fitsweet;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class SedeActivity extends AppCompatActivity {

    private static final String MAP_URL = "https://www.google.com/maps?q=Valverde%20Menta%20Ch%C3%ADa%20Cundinamarca&output=embed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sede);

        WebView webView = findViewById(R.id.webViewSede);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(MAP_URL);
    }
}
