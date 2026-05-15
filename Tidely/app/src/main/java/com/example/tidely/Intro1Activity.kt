package com.example.tidely

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Intro1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro1)

        val btnNext = findViewById<ImageView>(R.id.btn_next_intro_1)
        val btnSkip = findViewById<TextView>(R.id.btn_skip_intro_1)

        btnNext.setOnClickListener {
            val intent = Intent(this, Intro2Activity::class.java)
            startActivity(intent)
        }

        btnSkip.setOnClickListener {
            val intent = Intent(this, Intro4Activity::class.java)
            startActivity(intent)
        }
    }
}