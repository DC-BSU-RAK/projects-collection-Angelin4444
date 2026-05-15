package com.example.litlore

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {

    // This is where the app wakes up to start doing its thing!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Making sure the app takes up the whole screen, edge-to-edge, for that clean modern aesthetic.
        enableEdgeToEdge()

        // Connecting this code to the actual splash screen design layout.
        setContentView(R.layout.activity_splash)

        // 🎵 TELL THE DJ TO START THE MUSIC INSTANTLY
        MusicManager.startMusic(this)

        // Just making sure the design fits perfectly without getting hidden behind the battery icon or swipe bar.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Grabbing the candle image from the UI to make it move later.
        val ivSplashCandle = findViewById<ImageView>(R.id.ivSplashCandle)

        // Grabbing the litlore text logo too.
        val ivSplashText = findViewById<ImageView>(R.id.ivSplashText)

        // Time for magic! Let's do a smooth fade-in for the candle first.
        ivSplashCandle.animate()
            .alpha(1f) // Fading it in from invisible to fully visible
            .setDuration(1500) // Taking 1.5 seconds so it feels chill plus smooth
            .withEndAction {

                // Right after the candle fully appears, let's fade in the text logo.
                ivSplashText.animate()
                    .alpha(1f)
                    .setDuration(1500)
                    .withEndAction {

                        // Now both are on screen. Let's pause to hold this view for exactly 1 second.
                        Handler(Looper.getMainLooper()).postDelayed({

                            // Alright, intro is done! Time to jump over to the main app screen.
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)

                            // Closing this splash screen forever so if the user presses 'back', they don't get stuck here.
                            finish()
                        }, 1000)
                    }
                    .start()
            }
            .start()
    }
}