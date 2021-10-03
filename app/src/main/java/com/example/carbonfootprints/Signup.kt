package com.example.carbonfootprints

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setListeners()
    }

    private fun setListeners() {
        val loginTextView = findViewById<TextView>(R.id.loginBottomRight)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        loginTextView.setOnClickListener{
            login()
        }
    }

    private fun login() {
        val intent = Intent(this, Login::class.java);
        startActivity(intent)
    }

}