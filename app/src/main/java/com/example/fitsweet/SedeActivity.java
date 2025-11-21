package com.example.fitsweet;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class SedeActivity extends AppCompatActivity {

    private static final String MAP_IFRAME_HTML = "" +
            "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<meta name=\"viewport\" content=\"initial-scale=1.0, width=device-width\" />" +
            "<style>body,html{margin:0;padding:0;height:100%;} iframe{border:0;width:100%;height:100%;}</style>" +
            "</head>" +
            "<body>" +
            "<iframe " +
            "src=\"https://www.google.com/maps?q=Valverde%20Menta%20Ch%C3%ADa%20Cundinamarca&output=embed\" " +
            "allowfullscreen loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>" +
            "</body>" +
            "</html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sede);

        WebView webView = findViewById(R.id.webViewSede);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadDataWithBaseURL(null, MAP_IFRAME_HTML, "text/html", "UTF-8", null);
    }
}
