package com.example.tidely

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class EditTimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_time)

        val btnBack = findViewById<TextView>(R.id.btn_back_edit_time)
        val btnSave = findViewById<TextView>(R.id.btn_save_time)
        val timePicker = findViewById<TimePicker>(R.id.time_picker)
        val llSavedTimesContainer = findViewById<LinearLayout>(R.id.ll_saved_times_container)

        timePicker.setIs24HourView(false)

        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            // Get the time from the wheel
            val hour = timePicker.hour
            val minute = timePicker.minute

            val amPm = if (hour >= 12) "PM" else "AM"
            val formattedHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
            val formattedTime = String.format(Locale.getDefault(), "%02d : %02d %s", formattedHour, minute, amPm)

            // Spawn a new "item_saved_time" template
            val newTimeBox = LayoutInflater.from(this).inflate(R.layout.item_saved_time, llSavedTimesContainer, false)

            // Find the Text and the new Delete button inside this specific box
            val tvTimeText = newTimeBox.findViewById<TextView>(R.id.tv_final_time)
            val btnDelete = newTimeBox.findViewById<TextView>(R.id.btn_delete_time)

            tvTimeText.text = formattedTime

            // ==========================================
            // 🗑️ THE DELETE MAGIC
            // ==========================================
            btnDelete.setOnClickListener {
                // This line instantly removes this box from the container!
                llSavedTimesContainer.removeView(newTimeBox)
            }

            // Add it to the container!
            llSavedTimesContainer.addView(newTimeBox)
        }
    }
}