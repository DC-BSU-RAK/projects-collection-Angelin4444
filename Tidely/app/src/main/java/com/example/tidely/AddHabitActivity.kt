package com.example.tidely

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddHabitActivity : AppCompatActivity() {

    // Keeping track of how many times a user wants to complete a habit daily. Starting at 1.
    private var goalCount = 1

    // Remembering which days are selected for this habit.
    private var currentSelectedDays = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Connecting this code to the layout design file.
        setContentView(R.layout.activity_add_habit)

        // Grabbing all the UI elements from the screen so they can be controlled here.
        val btnBack = findViewById<TextView>(R.id.btn_back_add_habit)
        val btnSave = findViewById<TextView>(R.id.btn_save_habit)
        val btnMinus = findViewById<TextView>(R.id.btn_minus_goal)
        val btnPlus = findViewById<TextView>(R.id.btn_plus_goal)
        val tvGoalCount = findViewById<TextView>(R.id.tv_goal_count)
        val tvCustomizeDays = findViewById<TextView>(R.id.tv_customize_days)
        val tvFrequencyResult = findViewById<TextView>(R.id.tv_frequency_result)
        val tvCustomizeTime = findViewById<TextView>(R.id.tv_customize_time)
        val etHabitName = findViewById<EditText>(R.id.et_habit_name)

        // Setting up a listener to catch the result when coming back from the days customization screen.
        val customizeDaysLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                // Pulling the selected days text from the result.
                val days = result.data?.getStringExtra("SELECTED_DAYS") ?: ""
                currentSelectedDays = days

                // Updating the screen to show those chosen days.
                tvFrequencyResult.text = days
            }
        }

        // Setting up another listener for the time picker screen.
        val customizeTimeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                // Grabbing the chosen time.
                val time = result.data?.getStringExtra("SELECTED_TIME") ?: ""
            }
        }

        // Closing this screen when clicking the back button.
        btnBack.setOnClickListener { finish() }

        // Lowering the daily goal count but stopping at 1 so it never hits zero.
        btnMinus.setOnClickListener {
            if (goalCount > 1) {
                goalCount--
                tvGoalCount.text = goalCount.toString()
            }
        }

        // Increasing the daily goal count when clicking plus.
        btnPlus.setOnClickListener {
            goalCount++
            tvGoalCount.text = goalCount.toString()
        }

        // Opening a new screen to pick specific days. Passing along any already selected days.
        tvCustomizeDays.setOnClickListener {
            val intent = Intent(this, CustomizeDaysActivity::class.java)
            intent.putExtra("SAVED_DAYS", currentSelectedDays)
            customizeDaysLauncher.launch(intent)
        }

        // Opening the time editing screen.
        tvCustomizeTime.setOnClickListener {
            val intent = Intent(this, EditTimeActivity::class.java)
            customizeTimeLauncher.launch(intent)
        }

        // Time to save the new habit!
        btnSave.setOnClickListener {

            // Grabbing whatever text was typed into the habit name box.
            val typedName = etHabitName.text.toString().trim()

            // If the box is empty, give it a default name automatically.
            val habitNameToSave = if (typedName.isNotEmpty()) typedName else "My Habit"

            // Opening up the phone's local storage to save this data permanently.
            val sharedPreferences = getSharedPreferences("TidelyPrefs", Context.MODE_PRIVATE)
            val gson = Gson()

            // Checking if there is already a saved list of habits.
            val existingJson = sharedPreferences.getString("HABIT_LIST", null)
            val type = object : TypeToken<MutableList<HabitItem>>() {}.type

            // If a list exists, load it up. Otherwise, create a brand new empty list.
            val habitList: MutableList<HabitItem> = if (existingJson != null) {
                gson.fromJson(existingJson, type)
            } else {
                mutableListOf()
            }

            // Adding the brand new habit into the list with zero initial progress.
            habitList.add(HabitItem(habitNameToSave, goalCount, 0))

            // Saving the updated list back into local storage.
            sharedPreferences.edit()
                .putString("HABIT_LIST", gson.toJson(habitList))
                .apply()

            // Jumping back to the main home screen.
            val intent = Intent(this, HomeActivity::class.java)

            // Clearing out old screens so pressing back won't reopen this creation page.
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}