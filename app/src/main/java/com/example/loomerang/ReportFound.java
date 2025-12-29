package com.example.loomerang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
// import android.provider.MediaStore; // <--- We don't need this anymore
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class ReportFound extends AppCompatActivity {

    EditText etName, etLocation, etPhone, etDesc;
    ImageButton btnPhoto, btnBack;
    Button btnSubmit;
    TextView tvPageTitle;

    AppDatabase db;
    String currentUsername;
    String reportType;
    Uri selectedImageUri = null;

    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();

                    final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
                    try {
                        getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    btnPhoto.setImageTintList(null);
                    btnPhoto.setImageURI(selectedImageUri);
                    btnPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    btnPhoto.setBackground(null);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_found);

        if (getIntent() != null) {
            currentUsername = getIntent().getStringExtra("CURRENT_USERNAME");
            reportType = getIntent().getStringExtra("REPORT_TYPE");
        }

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "loomerang-db")
                .allowMainThreadQueries()
                .build();

        etName = findViewById(R.id.etItemName);
        etLocation = findViewById(R.id.etLocation);
        etPhone = findViewById(R.id.etPhone);
        etDesc = findViewById(R.id.etDescription);
        btnPhoto = findViewById(R.id.btnUploadPhoto);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvPageTitle = findViewById(R.id.tvPageTitle);

        if ("LOST".equals(reportType)) {
            tvPageTitle.setText("LAPOR KEHILANGAN BARANG");
            btnSubmit.setText("SUBMIT KEHILANGAN");
        } else {
            tvPageTitle.setText("LAPOR PENEMUAN BARANG");
            btnSubmit.setText("SUBMIT PENEMUAN");
        }

        btnPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            galleryLauncher.launch(intent);
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String loc = etLocation.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            String imagePathString = (selectedImageUri != null) ? selectedImageUri.toString() : "";

            if (name.isEmpty() || loc.isEmpty() || phone.isEmpty()) {
                Toast.makeText(ReportFound.this, "Please fill required fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                FoundItem item = new FoundItem(name, desc, loc, currentUsername, phone, imagePathString, reportType);
                db.foundItemDao().insertItem(item);

                if ("FOUND".equals(reportType)) {
                    db.userDao().incrementPoints(currentUsername);
                }

                runOnUiThread(() -> {
                    String msg = "FOUND".equals(reportType) ? "Thanks for finding it!" : "Lost item reported.";
                    Toast.makeText(ReportFound.this, msg, Toast.LENGTH_SHORT).show();
                    finish();

                });
            }).start();
        });
    }
}

// Property of Marco - https://github.com/MarcoBenedictus