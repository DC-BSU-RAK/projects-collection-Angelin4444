package com.example.tidely

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnBack = findViewById<ImageView>(R.id.btn_back_login)
        val btnLetsGo = findViewById<ImageView>(R.id.btn_lets_go_login)
        val etUsername = findViewById<EditText>(R.id.et_username_login)
        val etPassword = findViewById<EditText>(R.id.et_password_login)
        val tvForgotPassword = findViewById<TextView>(R.id.tv_forget_password)

        btnBack.setOnClickListener {
            finish()
        }

        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Password reset link sent to your email!", Toast.LENGTH_SHORT).show()
        }

        btnLetsGo.setOnClickListener {
            val inputEmail = etUsername.text.toString()
            val inputPassword = etPassword.text.toString()

            val sharedPreferences = getSharedPreferences("TidelyPrefs", Context.MODE_PRIVATE)
            val savedEmail = sharedPreferences.getString("USER_EMAIL", "")
            val savedPassword = sharedPreferences.getString("USER_PASSWORD", "")

            if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (inputEmail == savedEmail && inputPassword == savedPassword) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish() // Closes the login page so they can't hit 'back' to go to it
            } else {
                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}