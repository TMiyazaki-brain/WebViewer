package com.example.webviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Pubg extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubg);

        webView = (WebView) findViewById(R.id.pubg_web);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://liquipedia.net/pubg/Main_Page");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_pubg);
        bottomNavigationView.setSelectedItemId(R.id.pubg);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.youtube:
                        startActivity(new Intent(getApplication()
                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.pubg:
                        return true;

                    case R.id.apex:
                        startActivity(new Intent(getApplication()
                                ,Apex.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }


    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}