package com.example.tidely

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class BubblyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bubbly)

        // Find the "Next" button and the text box where they type their Sea Name
        val btnNext = findViewById<ImageView>(R.id.btn_next_bubbly)
        val etSeaName = findViewById<EditText>(R.id.et_sea_name)

        // When the user clicks "Next"
        btnNext.setOnClickListener {

            // 1. Save their sea name so we can use it later
            val seaName = etSeaName.text.toString()
            val sharedPreferences = getSharedPreferences("TidelyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("USER_SEA_NAME", seaName)
            editor.apply()

            // 2. Open the door to the Home Page!
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            // 3. Close the Bubbly page behind them
            finish()
        }
    }
}