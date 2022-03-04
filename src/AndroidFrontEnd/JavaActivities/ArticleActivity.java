package com.example.premnews;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class ArticleActivity extends AppCompatActivity {

    private WebView web;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Bundle extras = getIntent().getExtras();
        String link = extras.getString("l");
        Toast.makeText(ArticleActivity.this, "Press Back Button to Return to Page", Toast.LENGTH_LONG).show();

        web = (WebView) findViewById(R.id.web);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl(link);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ArticleActivity.this, NewsActivity.class));
                finish();
            }
        });


    }
}