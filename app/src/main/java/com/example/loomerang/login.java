package com.example.loomerang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class login extends AppCompatActivity {

    EditText etUsername, etNim, etPassword;
    Button btnLogin, btnBack;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "loomerang-db").build();

        etUsername = findViewById(R.id.txtusernamelogin);
        etNim = findViewById(R.id.txtnimlogin);
        etPassword = findViewById(R.id.txtpasswordlogin);
        btnLogin = findViewById(R.id.loginbtn);
        btnBack = findViewById(R.id.backbtnlogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    private void performLogin() {
        String username = etUsername.getText().toString().trim();
        String nim = etNim.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || nim.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nim.matches("[0-9]+")) {
            Toast.makeText(this, "NIM must contain only numbers!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = db.userDao().login(username, password);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user == null) {
                            Toast.makeText(login.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!user.nim.equals(nim)) {
                                Toast.makeText(login.this, "Wrong NIM for this user!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(login.this, "Welcome, " + user.username, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(login.this, dashboard.class);
                                intent.putExtra("CURRENT_USERNAME", user.username);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
            }
        }).start();
    }
}