package com.example.loomerang;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tvEmpty;
    AppDatabase db;
    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        currentUsername = getIntent().getStringExtra("CURRENT_USERNAME");

        ImageButton btnBack = findViewById(R.id.btnBack);
        tvEmpty = findViewById(R.id.tvEmpty);
        recyclerView = findViewById(R.id.recyclerNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "loomerang-db")
                .allowMainThreadQueries()
                .build();

        if (currentUsername != null) {
            List<Notification> myNotifications = db.notificationDao().getMyNotifications(currentUsername);

            if (myNotifications.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                NotificationAdapter adapter = new NotificationAdapter(this, myNotifications);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}