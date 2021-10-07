package com.example.carbonfootprints;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public final class Signup extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_signup);
        Button signUpButton = findViewById(R.id.signupButtonG);
        signUpButton.setOnClickListener(v -> signUp());    }


    private void signUp() {
        //SWITCH TO DASHBOARD when dashboard is created
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}
