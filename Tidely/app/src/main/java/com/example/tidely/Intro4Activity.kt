package com.example.tidely

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Intro4Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro4)

        val bearImage = findViewById<ImageView>(R.id.bear_background_view)
        bearImage.setImageResource(R.drawable.bear_animation)
        val bearAnimation = bearImage.drawable as AnimationDrawable
        bearAnimation.start()

        val btnBack = findViewById<ImageView>(R.id.btn_back_intro_4)
        val btnGetStarted = findViewById<ImageView>(R.id.btn_get_started_intro_4)
        val btnLogin = findViewById<ImageView>(R.id.text_login_intro_4)

        btnBack.setOnClickListener {
            finish()
        }

        btnGetStarted.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}