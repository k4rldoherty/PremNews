package com.example.premnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SelectActivity extends AppCompatActivity {

    ListView listViewData;
    ArrayAdapter<String> adapter;
    String[] teams = {"ALL","Arsenal", "Aston Villa", "Brentford", "Brighton", "Burnley", "Chelsea", "Crystal Palace",
        "Everton", "Leeds", "Leicester", "Liverpool", "Manchester City", "Manchester United",
        "Newcastle United", "Norwich City", "Southampton", "Tottenham", "Watford", "West Ham",
        "Wolverhampton"};
    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        listViewData = findViewById(R.id.list_data);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, teams);
        listViewData.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_done) {
            String selected = "";
            for (int i = 0; i < listViewData.getCount(); i++) {
                if (listViewData.isItemChecked(i)) {
                    selected += listViewData.getItemAtPosition(i) + ", ";
                }
            }
            Toast.makeText(this, "Teams selected:" + selected, Toast.LENGTH_SHORT).show();
            userID = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userID);
            Map<String, Object> user = new HashMap<>();
            user.put("Teams", selected);
            documentReference.set(user);
            startActivity(new Intent(SelectActivity.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}