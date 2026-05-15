package com.example.tidely

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvUserName = findViewById<TextView>(R.id.tv_user_name)
        val tvUserEmail = findViewById<TextView>(R.id.tv_user_email)
        val btnLogout = findViewById<TextView>(R.id.btn_logout)
        val btnDeleteAccount = findViewById<TextView>(R.id.btn_delete_account)
        val navHome = findViewById<ImageView>(R.id.nav_home)

        val sharedPreferences = getSharedPreferences("TidelyPrefs", Context.MODE_PRIVATE)
        val savedName = sharedPreferences.getString("USER_NAME", "Waiting for data...")
        val savedEmail = sharedPreferences.getString("USER_EMAIL", "Waiting for data...")

        tvUserName.text = "User Name : $savedName"
        tvUserEmail.text = "Email : $savedEmail"

        btnLogout.setOnClickListener {
            Toast.makeText(this, "You have been logged out.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnDeleteAccount.setOnClickListener {
            Toast.makeText(this, "Account successfully deleted.", Toast.LENGTH_SHORT).show()

            sharedPreferences.edit().clear().apply()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        navHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}