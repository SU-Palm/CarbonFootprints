package com.example.carbonfootprints;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_dashboard);
        //Button signOutButton = findViewById(R.id.signOutButton);
        //mAuth = FirebaseAuth.getInstance();
        //signOutButton.setOnClickListener(v -> logOut());

    }
    private void logOut() {
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}