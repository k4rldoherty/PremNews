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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class NewsActivity extends AppCompatActivity {

    private ImageButton home;
    private ListView listview;
    private EditText search;
    private Button searchBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        home = findViewById(R.id.home);
        listview = findViewById(R.id.listview);
        search = findViewById(R.id.search);
        searchBTN = findViewById(R.id.searchbtn);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(NewsActivity.this, MainActivity.class));
                finish();
            }
        });

        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item, list);
        listview.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("news");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String data = snapshot.child("Title").getValue().toString();
                    list.add(data);
                }
                Collections.reverse(list);
                adapter.notifyDataSetChanged();
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                        String title = parent.getItemAtPosition(position).toString();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String value = snapshot.getValue().toString();
                            if (value.contains(title)){
                                String link = snapshot.child("Link").getValue().toString();
                                Intent intent = new Intent(NewsActivity.this, ArticleActivity.class);
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

        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = search.getText().toString();
                filter(keyword, list, adapter);
            }
        });
    }

    private void filter(String keyword, ArrayList<String> list, ArrayAdapter adapter) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("news");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String title = snapshot.child("Title").getValue().toString();
                    if (title.contains(keyword)){
                        list.add(title);
                    }
                }
                Collections.reverse(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}