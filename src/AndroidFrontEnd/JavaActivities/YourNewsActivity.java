package com.example.premnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class YourNewsActivity extends AppCompatActivity {

    private ImageButton home;
    private ListView listView;
    private EditText search;
    private Button searchBtn;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    private String uID;
    String selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_news);

        home = findViewById(R.id.home);
        listView = findViewById(R.id.listview);
        search = findViewById(R.id.search);
        searchBtn = findViewById(R.id.searchbtn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        uID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(uID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        selection = document.getData().toString();
                        display(selection);
                    } else {
                        Toast.makeText(YourNewsActivity.this, "No Teams Have Been Added", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(YourNewsActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void display(String selection) {
        String[] team = selection.split("=");
        String[] teams = team[1].split(",");

        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item, list);
        listView.setAdapter(adapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("news");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String data = snapshot.child("Title").getValue().toString();
                    if (selection.contains("ALL")){
                        list.add(data);
                    } else {
                        for (String i : teams){
                            if (data.contains(i)){
                                list.add(data);
                            }
                        }
                    }
                }
                Collections.reverse(list);
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                        String title = parent.getItemAtPosition(position).toString();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String value = snapshot.getValue().toString();
                            if (value.contains(title)){
                                String link = snapshot.child("Link").getValue().toString();
                                Intent intent = new Intent(YourNewsActivity.this, ArticleActivity.class);
                                intent.putExtra("l", link);
                                Log.d("Test", link);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = search.getText().toString().toLowerCase();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String data = snapshot.child("Title").getValue().toString();
                            if (data.toLowerCase().contains(keyword)) {
                                if (selection.contains("ALL")) {
                                    list.add(data);
                                } else {
                                    for (String i : teams) {
                                        if (data.contains(i)) {
                                            list.add(data);
                                        }
                                    }
                                }
                            }
                        }
                        Collections.reverse(list);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
    }
}