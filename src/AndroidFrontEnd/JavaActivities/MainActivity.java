package com.example.premnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Button news;
    private Button teams;
    private Button logout;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    private Button date;
    private Button source;
    private TextView yourteams;
    private ImageButton keyInfo;
    private ImageButton srcInfo;
    private ImageButton dateInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        news = findViewById(R.id.news);
        teams = findViewById(R.id.teams);
        logout = findViewById(R.id.logout);
        date = findViewById(R.id.date);
        source = findViewById(R.id.source);
        yourteams = findViewById(R.id.yourteams);
        keyInfo = findViewById(R.id.keyInfo);
        srcInfo = findViewById(R.id.srcInfo);
        dateInfo = findViewById(R.id.dateInfo);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
            }
        });

        teams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SelectActivity.class));
                finish();
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, YourNewsActivity.class));
                finish();
            }
        });

        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SourceActivity.class));
                finish();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DateActivity.class));
                finish();
            }
        });

        srcInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Click here to filter your team " +
                        "news by source!", Toast.LENGTH_LONG).show();
            }
        });

        keyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Click here to filter your team " +
                        "news by keywords or phrases!", Toast.LENGTH_LONG).show();
            }
        });

        dateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Click here to filter your team " +
                        "news by date!", Toast.LENGTH_LONG).show();
            }
        });

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String teams = document.getData().toString();
                        String[] split = teams.split("=");
                        teams = split[1];
                        teams = teams.substring(0, teams.length() -1);
                        if (teams.contains("ALL")){
                            yourteams.setText("ALL");
                        } else {
                            yourteams.setText(teams);
                        }
                    } else {
                        yourteams.setText("N/A");
                    }
                }
            }
        });
    }
}