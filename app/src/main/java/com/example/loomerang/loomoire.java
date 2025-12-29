package com.example.loomerang;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import java.util.List;

public class loomoire extends AppCompatActivity {

    RecyclerView recyclerView;
    AppDatabase db;
    String currentUsername;
    LoomoireAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loomoire);

        currentUsername = getIntent().getStringExtra("CURRENT_USERNAME");

        // Header
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // RecyclerView Setup
        recyclerView = findViewById(R.id.recyclerViewLoomoire);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load Data
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "loomerang-db").allowMainThreadQueries().build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        List<FoundItem> items = db.foundItemDao().getAllItems();

        adapter = new LoomoireAdapter(this, items, db, currentUsername);
        recyclerView.setAdapter(adapter);
    }
}

// Property of Marco - https://github.com/MarcoBenedictus