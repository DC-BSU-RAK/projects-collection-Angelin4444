package com.example.tidely

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Intro3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro3)

        val btnNext = findViewById<ImageView>(R.id.btn_next_intro_3)
        val btnBack = findViewById<ImageView>(R.id.btn_back_intro_3)

        btnNext.setOnClickListener {
            val intent = Intent(this, Intro4Activity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}