package com.example.loomerang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class EditItemActivity extends AppCompatActivity {

    EditText etName, etLocation, etDesc;
    ImageButton btnPhoto;
    Button btnSave;
    AppDatabase db;

    int itemId;
    String currentImagePath; // keep old photo if not changed by user

    // Gallery Logic
    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    getContentResolver().takePersistableUriPermission(selectedImageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    currentImagePath = selectedImageUri.toString(); // Update path
                    btnPhoto.setImageURI(selectedImageUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "loomerang-db").allowMainThreadQueries().build();

        etName = findViewById(R.id.etItemName);
        etLocation = findViewById(R.id.etLocation);
        etDesc = findViewById(R.id.etDescription);
        btnPhoto = findViewById(R.id.btnUploadPhoto);
        btnSave = findViewById(R.id.btnSubmit);

        // GET DATA
        itemId = getIntent().getIntExtra("ID", -1);
        etName.setText(getIntent().getStringExtra("NAME"));
        etLocation.setText(getIntent().getStringExtra("LOC"));
        etDesc.setText(getIntent().getStringExtra("DESC"));
        currentImagePath = getIntent().getStringExtra("IMG");

        if (currentImagePath != null && !currentImagePath.isEmpty()) {
            btnPhoto.setImageURI(Uri.parse(currentImagePath));
        }

        // IMAGE CLICK
        btnPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            galleryLauncher.launch(intent);
        });

        // SAVE CLICK
        btnSave.setText("SAVE CHANGES");
        btnSave.setOnClickListener(v -> {
            String newName = etName.getText().toString();
            String newLoc = etLocation.getText().toString();
            String newDesc = etDesc.getText().toString();

            db.foundItemDao().updateItem(itemId, newName, newDesc, newLoc, currentImagePath);

            Toast.makeText(this, "Item Updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}

// Property of Marco - https://github.com/MarcoBenedictus