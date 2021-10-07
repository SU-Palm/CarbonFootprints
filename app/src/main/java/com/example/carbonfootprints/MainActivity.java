package com.example.carbonfootprints;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.setListeners();
    }

    private void setListeners() {
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);
        loginButton.setOnClickListener(v -> login());
        signUpButton.setOnClickListener(v -> signUp());
    }

    private void login() {
        Intent intent = new Intent(this, Login.class);
        this.startActivity(intent);
    }

    private void signUp() {
        Intent intent = new Intent(this, Signup.class);
        this.startActivity(intent);
    }
}
