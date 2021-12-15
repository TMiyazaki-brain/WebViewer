package com.example.webviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Apex extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // タイトルバーを消す
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを消す
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apex);
        webView = (WebView) findViewById(R.id.apex_web);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://liquipedia.net/apexlegends/Main_Page");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_apex);
        bottomNavigationView.setSelectedItemId(R.id.apex);

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
                        startActivity(new Intent(getApplication()
                                ,Pubg.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.apex:
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