package com.example.tidely

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import java.util.Locale

class AddTodoActivity : AppCompatActivity() {

    // This screen handles creating a new to-do task.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Connecting the code to the screen layout file.
        setContentView(R.layout.activity_add_todo)

        // Grabbing all the buttons plus text fields from the screen design.
        val btnBack = findViewById<TextView>(R.id.btn_back_todo)
        val btnSave = findViewById<TextView>(R.id.btn_save_todo)
        val etTodoName = findViewById<EditText>(R.id.et_todo_name)
        val tvDueDate = findViewById<TextView>(R.id.tv_due_date_result)
        val tvRemindTime = findViewById<TextView>(R.id.tv_remind_time_result)
        val tvRepeat = findViewById<TextView>(R.id.tv_repeat_result)

        // Clicking back just closes this screen.
        btnBack.setOnClickListener {
            finish()
        }

        // Opening a popup to pick a specific calendar date.
        tvDueDate.setOnClickListener {

            // Grabbing the current date first to set it as the starting point.
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Showing the calendar popup.
            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->

                // Formatting the chosen date to look nice before putting it on the screen.
                val formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                tvDueDate.text = formattedDate
            }, year, month, day)
            datePickerDialog.show()
        }

        // Opening a popup to pick a specific time.
        tvRemindTime.setOnClickListener {

            // Figuring out the current time to start there.
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Showing the time popup. Setting it up for a 12-hour format with AM/PM instead of military time.
            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val amPm = if (selectedHour >= 12) "PM" else "AM"
                val formattedHour = if (selectedHour == 0) 12 else if (selectedHour > 12) selectedHour - 12 else selectedHour
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s", formattedHour, selectedMinute, amPm)
                tvRemindTime.text = formattedTime
            }, hour, minute, false)
            timePickerDialog.show()
        }

        // A simple tap to switch between Yes plus No for repeating this task.
        tvRepeat.setOnClickListener {
            if (tvRepeat.text == "No") {
                tvRepeat.text = "Yes"
            } else {
                tvRepeat.text = "No"
            }
        }

        // Time to save the task!
        btnSave.setOnClickListener {

            // Grabbing the typed task name.
            val typedName = etTodoName.text.toString().trim()

            // If the text box is empty, calling it 'New Task' automatically.
            val todoNameToSave = if (typedName.isNotEmpty()) typedName else "New Task"

            // Opening local phone storage to keep this data permanently.
            val sharedPreferences = getSharedPreferences("TidelyPrefs", Context.MODE_PRIVATE)
            val gson = Gson()

            // Checking if a task list already exists.
            val existingJson = sharedPreferences.getString("TODO_LIST", null)
            val type = object : TypeToken<MutableList<TodoItem>>() {}.type

            // Loading the existing list. If it is empty, making a brand new one.
            val todoList: MutableList<TodoItem> = if (existingJson != null) {
                gson.fromJson(existingJson, type)
            } else {
                mutableListOf()
            }

            // Adding the newly created task into the list.
            todoList.add(TodoItem(todoNameToSave))

            // Saving the updated list back into local storage.
            sharedPreferences.edit()
                .putString("TODO_LIST", gson.toJson(todoList))
                .apply()

            // Jumping back to the main home screen.
            val intent = Intent(this, HomeActivity::class.java)

            // Clearing out old screens so pressing back will not reopen this page.
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}