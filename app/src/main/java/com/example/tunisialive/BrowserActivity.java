package com.example.tunisialive;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class BrowserActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        webView = findViewById(R.id.webView);

        // Récupérer l'URL depuis l'Intent
        String articleUrl = getIntent().getStringExtra("articleUrl");

        // Charger l'URL dans la WebView
        if (articleUrl != null) {
            webView.loadUrl(articleUrl);
        }
    }
}