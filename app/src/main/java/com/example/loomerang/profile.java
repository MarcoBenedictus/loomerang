package com.example.loomerang;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class profile extends AppCompatActivity {

    AppDatabase db;
    String currentUsername;

    TextView tvUser, tvNim, tvEmail, tvPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentUsername = getIntent().getStringExtra("CURRENT_USERNAME");

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        tvUser = findViewById(R.id.tvProfileUsername);
        tvNim = findViewById(R.id.tvProfileNim);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvPoints = findViewById(R.id.tvProfilePoints);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "loomerang-db")
                .allowMainThreadQueries()
                .build();

        loadProfileData();
    }

    private void loadProfileData() {
        if (currentUsername == null) return;

        new Thread(() -> {
            User user = db.userDao().getUser(currentUsername);

            if (user != null) {
                runOnUiThread(() -> {
                    tvUser.setText(user.username);
                    tvNim.setText(user.nim);
                    tvEmail.setText(user.email);
                    tvPoints.setText(user.itemsFoundCount + " PTS");
                });
            }
        }).start();
    }
}