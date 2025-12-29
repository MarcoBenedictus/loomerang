package com.example.loomerang;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import java.util.List;

public class leaderboard extends AppCompatActivity {

    RecyclerView recyclerView;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if(btnBack != null) btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerLeaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "loomerang-db")
                .allowMainThreadQueries()
                .build();

        List<User> topUsers = db.userDao().getTop10Users();

        LeaderboardAdapter adapter = new LeaderboardAdapter(this, topUsers);
        recyclerView.setAdapter(adapter);
    }
}

// Property of Marco - https://github.com/MarcoBenedictus