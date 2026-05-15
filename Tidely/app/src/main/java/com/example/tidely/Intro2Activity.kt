package com.example.tidely

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Intro2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro2)

        val btnNext = findViewById<ImageView>(R.id.btn_next_intro_2)
        val btnBack = findViewById<ImageView>(R.id.btn_back_intro_2)

        btnNext.setOnClickListener {
            val intent = Intent(this, Intro3Activity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}