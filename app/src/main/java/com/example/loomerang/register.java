package com.example.loomerang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class register extends AppCompatActivity {

    EditText etUsername, etNim, etEmail, etPassword;
    Button btnRegister, btnBack;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "loomerang-db").build();

        etUsername = findViewById(R.id.txtusername);
        etNim = findViewById(R.id.txtnim);
        etEmail = findViewById(R.id.txtemail);
        etPassword = findViewById(R.id.txtpassword);
        btnRegister = findViewById(R.id.registerbtn);
        btnBack = findViewById(R.id.backbtn);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String nim = etNim.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || nim.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nim.matches("[0-9]+")) {
            Toast.makeText(this, "NIM must contain only numbers!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.contains("@")) {
            Toast.makeText(this, "Email must contain an @", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(username, nim, email, password);

        new Thread(new Runnable() {
            @Override
            public void run() {
                db.userDao().registerUser(newUser);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(register.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).start();
    }
}

// Property of Marco - https://github.com/MarcoBenedictus