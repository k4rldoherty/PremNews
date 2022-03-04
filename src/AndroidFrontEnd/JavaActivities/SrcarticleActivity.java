package com.example.premnews;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class SrcarticleActivity extends AppCompatActivity {

    private WebView web;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srcarticle);

        Bundle extras = getIntent().getExtras();
        String link = extras.getString("l");
        Toast.makeText(SrcarticleActivity.this, "Press Back Button to Return to Page" , Toast.LENGTH_LONG).show();

        web = (WebView) findViewById(R.id.web);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl(link);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SrcarticleActivity.this, SourceActivity.class));
                finish();
            }
        });

    }
}