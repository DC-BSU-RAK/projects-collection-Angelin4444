package com.example.tidely

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val splashImage = findViewById<ImageView>(R.id.splash_image_view)

        if (splashImage.background is AnimationDrawable) {
            val animation = splashImage.background as AnimationDrawable
            animation.start()
        } else if (splashImage.drawable is AnimationDrawable) {
            val animation = splashImage.drawable as AnimationDrawable
            animation.start()
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.wave_sound)
        mediaPlayer?.start()

        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreferences = getSharedPreferences("TidelyPrefs", Context.MODE_PRIVATE)
            val savedEmail = sharedPreferences.getString("USER_EMAIL", "")

            if (!savedEmail.isNullOrEmpty()) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, Intro1Activity::class.java)
                startActivity(intent)
            }
            finish()
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}