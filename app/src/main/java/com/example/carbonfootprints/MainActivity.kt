package com.example.carbonfootprints

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListeners()

    }

    private fun setListeners() {
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        loginButton.setOnClickListener {
            login()
        }

        signUpButton.setOnClickListener {
            signUp()
        }

    }

    private fun login() {
        val intent = Intent(this, Login::class.java);
        startActivity(intent)
    }

    private fun signUp() {
        val intent = Intent(this, Signup::class.java);
        startActivity(intent)
    }

}