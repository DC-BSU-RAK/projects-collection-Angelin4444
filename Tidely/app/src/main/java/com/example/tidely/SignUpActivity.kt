package com.example.tidely

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btnBack = findViewById<ImageView>(R.id.btn_back_signup)
        val btnLetsGo = findViewById<ImageView>(R.id.btn_lets_go_signup)
        val etName = findViewById<EditText>(R.id.et_name_signup)
        val etEmail = findViewById<EditText>(R.id.et_email_signup)
        val etPassword = findViewById<EditText>(R.id.et_password_signup)

        btnBack.setOnClickListener {
            finish()
        }

        btnLetsGo.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val sharedPreferences = getSharedPreferences("TidelyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("USER_NAME", name)
                editor.putString("USER_EMAIL", email)
                editor.putString("USER_PASSWORD", password)
                editor.apply()

                Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()

                // JUMP TO BUBBLY PAGE
                val intent = Intent(this, BubblyActivity::class.java)
                startActivity(intent)
                finish() // Closes the sign-up page so the user can't accidentally hit the back button and sign up again
            }
        }
    }
}