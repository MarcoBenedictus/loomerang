package com.example.loomerang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

public class MainActivity extends AppCompatActivity {

    Button btnGoToRegister, btnGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoToRegister = findViewById(R.id.registerbtnmain);
        btnGoToLogin = findViewById(R.id.loginbtnmain);

        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, register.class);
                startActivity(intent);
            }
        });

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });
    }
}