package com.example.carbonfootprints;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorEventListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        this.setListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            skipToHome();
        }
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
    private void skipToHome() {
        Intent intent = new Intent(this, Dashboard.class);
        this.startActivity(intent);
    }

}
