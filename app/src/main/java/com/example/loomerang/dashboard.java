package com.example.loomerang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class dashboard extends AppCompatActivity {

    ImageButton btnProfile, btnMenuDots, btnLapor, btnLoomoire, btnCari, btnLeaderboard;
    TextView tvDbNumber;
    AppDatabase db;
    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        currentUsername = getIntent().getStringExtra("CURRENT_USERNAME");

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "loomerang-db").build();

        btnProfile = findViewById(R.id.btnProfile);
        btnMenuDots = findViewById(R.id.btnMenuDots);
        btnLapor = findViewById(R.id.btnLapor);
        btnLoomoire = findViewById(R.id.btnLoomoire);
        btnCari = findViewById(R.id.btnCari);
        btnLeaderboard = findViewById(R.id.btnLeaderboard);
        tvDbNumber = findViewById(R.id.tvDbNumber);

        loadUserData();

        View.OnClickListener mainRedirect = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, MainActivity.class);
                startActivity(intent);
            }
        };
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, profile.class);
                // Important: Pass the username so the profile knows WHO to load
                intent.putExtra("CURRENT_USERNAME", currentUsername);
                startActivity(intent);
            }
        });

        btnLapor.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, ReportFound.class);
            intent.putExtra("CURRENT_USERNAME", currentUsername);
            intent.putExtra("REPORT_TYPE", "FOUND");
            startActivity(intent);
        });

        btnLoomoire.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, loomoire.class);
            intent.putExtra("CURRENT_USERNAME", currentUsername);
            startActivity(intent);
        });

        btnCari.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, ReportFound.class);
            intent.putExtra("CURRENT_USERNAME", currentUsername);
            intent.putExtra("REPORT_TYPE", "LOST");
            startActivity(intent);
        });

        btnLeaderboard.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, leaderboard.class);
            startActivity(intent);
        });

        btnMenuDots.setOnClickListener(v -> showPopupMenu(v));

        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                moveTaskToBack(true);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Force the app to refresh the Dashboard as the Data Updates everytime the Dashboard reappears
        loadUserData();
    }

    private void loadUserData() {
        if (currentUsername == null) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                User currentUser = db.userDao().getUser(currentUsername);

                if (currentUser != null) {
                    final int count = currentUser.itemsFoundCount;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvDbNumber.setText(String.valueOf(count));
                        }
                    });
                }
            }
        }).start();
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.dashboard_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_notifications) {
                    Intent intent = new Intent(dashboard.this, NotificationActivity.class);
                    intent.putExtra("CURRENT_USERNAME", currentUsername);
                    startActivity(intent);
                    return true;
                }

                else if (id == R.id.menu_settings) {
                    Intent intent = new Intent(dashboard.this, settings.class);
                    startActivity(intent);
                    return true;
                }

                else if (id == R.id.menu_logout) {
                    showLogoutConfirmation();
                    return true;
                }

                return false;
            }
        });
        popup.show();
    }

    private void showLogoutConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(dashboard.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

                Toast.makeText(dashboard.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

}