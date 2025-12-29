package com.example.loomerang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class detailactivity extends AppCompatActivity {

    AppDatabase db;
    String currentUserSending;
    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailactivity);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "loomerang-db")
                .allowMainThreadQueries() // Simple setup for assignment
                .build();

        ImageView ivImage = findViewById(R.id.ivDetailImage);
        TextView tvStatus = findViewById(R.id.tvDetailStatus);
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvReporter = findViewById(R.id.tvDetailReporter);
        TextView tvLocation = findViewById(R.id.tvDetailLocation);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        Button btnNotify = findViewById(R.id.btnNotifyDetail);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);
        android.view.View layoutOwner = findViewById(R.id.layoutOwnerActions);
        ImageButton btnBack = findViewById(R.id.btnBack);

        itemId = getIntent().getIntExtra("ITEM_ID", -1);

        String item_name = getIntent().getStringExtra("ITEM_NAME");
        String item_desc = getIntent().getStringExtra("ITEM_DESC");
        String item_loc = getIntent().getStringExtra("ITEM_LOC");
        String imagePath = getIntent().getStringExtra("ITEM_IMAGE");
        String type = getIntent().getStringExtra("ITEM_TYPE");
        String reporter_username = getIntent().getStringExtra("ITEM_REPORTER");
        currentUserSending = getIntent().getStringExtra("CURRENT_USER_SENDING");

        tvName.setText(item_name);
        tvDesc.setText(item_desc);
        tvLocation.setText(item_loc);
        tvReporter.setText(reporter_username);

        btnNotify.setText("NOTIFY " + reporter_username.toUpperCase() + "?");

        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                ivImage.setImageURI(Uri.parse(imagePath));
            } catch (Exception e) {
                ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }

        if ("LOST".equals(type)) {
            tvStatus.setText("DICARI");
            tvStatus.setTextColor(android.graphics.Color.parseColor("#FF5252"));
        } else {
            tvStatus.setText("DITEMUKAN");
            tvStatus.setTextColor(android.graphics.Color.parseColor("#69F0AE"));
        }

        if (currentUserSending != null && currentUserSending.equals(reporter_username)) {
            // YOU OWN THIS POST
            btnNotify.setVisibility(View.GONE);
            layoutOwner.setVisibility(View.VISIBLE);
        } else {
            // STRANGER
            btnNotify.setVisibility(View.VISIBLE);
            layoutOwner.setVisibility(View.GONE);
        }

        btnBack.setOnClickListener(v -> finish());

        btnNotify.setOnClickListener(v -> {
            if (reporter_username.equals(currentUserSending)) {
                Toast.makeText(this, "You cannot notify yourself", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                int count = db.notificationDao().checkNotificationExists(currentUserSending, reporter_username, item_name);

                if (count > 0) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "You have already notified this user", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                String msg = "LOST".equals(type) ?
                        currentUserSending + " claims they found: " + item_name :
                        currentUserSending + " claims they own: " + item_name;

                Notification notif = new Notification(reporter_username, currentUserSending, msg, item_name);
                db.notificationDao().insertNotification(notif);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Notification sent", Toast.LENGTH_SHORT).show();
                });
            }).start();
        });

        // DELETE Logic
        btnDelete.setOnClickListener(v -> {
            new Thread(() -> {
                if ("FOUND".equals(type)) {
                    db.userDao().decrementPoints(reporter_username);
                }

                db.foundItemDao().deleteById(itemId);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Post Deleted, your points have been deducted.", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });

        // EDIT Logic
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(detailactivity.this, EditItemActivity.class);
            intent.putExtra("ID", itemId);
            intent.putExtra("NAME", item_name);
            intent.putExtra("DESC", item_desc);
            intent.putExtra("LOC", item_loc);
            intent.putExtra("IMG", imagePath);
            startActivity(intent);
            finish();
        });
    }
}

// Property of Marco - https://github.com/MarcoBenedictus