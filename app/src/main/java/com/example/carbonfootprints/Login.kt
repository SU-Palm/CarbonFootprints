package com.example.carbonfootprints

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setListeners()
    }

    private fun setListeners() {
        val signUpTextView = findViewById<TextView>(R.id.signUpBottomRight)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpTextView.setOnClickListener{
            signUp()
        }
    }

    private fun signUp() {
        val intent = Intent(this, Signup::class.java);
        startActivity(intent)
    }
}