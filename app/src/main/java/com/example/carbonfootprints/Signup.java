package com.example.carbonfootprints;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public final class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText userEmail;
    private EditText userPswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_signup);
        Button signUpButton = findViewById(R.id.signupButtonG);
        userEmail = findViewById(R.id.signUpEmail);
        userPswd = findViewById(R.id.signUpPswd);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(v -> createAccount(userEmail.getText().toString(), userPswd.getText().toString()));
    }


    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            signUp();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUp() {
        //SWITCH TO DASHBOARD when dashboard is created
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}
