package com.example.tidely

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var rvCalendar: RecyclerView
    private var currentOffset = 0
    private lateinit var llHabitContent: LinearLayout
    private lateinit var llTodoContent: LinearLayout
    private lateinit var sharedPreferences: android.content.SharedPreferences
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("TidelyPrefs", Context.MODE_PRIVATE)
        llHabitContent = findViewById(R.id.ll_habit_content)
        llTodoContent = findViewById(R.id.ll_todo_content)

        setupCalendar()
        setupInstructionModal()
        setupTabs()
        setupBottomNavigation()
        loadHabits()
        loadTodos()
    }

    private fun loadTodos() {
        llTodoContent.removeAllViews()

        val existingJson = sharedPreferences.getString("TODO_LIST", null)
        val type = object : TypeToken<MutableList<TodoItem>>() {}.type
        val todoList: MutableList<TodoItem> = if (existingJson != null) {
            gson.fromJson(existingJson, type)
        } else {
            mutableListOf()
        }

        for (todo in todoList) {
            val todoView = LayoutInflater.from(this).inflate(R.layout.item_todo, llTodoContent, false)

            val tvName = todoView.findViewById<TextView>(R.id.tv_todo_name)
            val btnComplete = todoView.findViewById<TextView>(R.id.btn_todo_complete)

            tvName.text = todo.name

            btnComplete.setOnClickListener {
                todoList.remove(todo)

                sharedPreferences.edit()
                    .putString("TODO_LIST", gson.toJson(todoList))
                    .apply()

                loadTodos()
            }

            llTodoContent.addView(todoView)
        }
    }

    private fun loadHabits() {
        llHabitContent.removeAllViews()

        val existingJson = sharedPreferences.getString("HABIT_LIST", null)
        val type = object : TypeToken<MutableList<HabitItem>>() {}.type
        val habitList: MutableList<HabitItem> = if (existingJson != null) {
            gson.fromJson(existingJson, type)
        } else {
            mutableListOf()
        }

        for (habit in habitList) {
            val habitView = LayoutInflater.from(this).inflate(R.layout.item_habit, llHabitContent, false)

            val tvName = habitView.findViewById<TextView>(R.id.tv_habit_name)
            val tvLeft = habitView.findViewById<TextView>(R.id.tv_habit_left)
            val btnClick = habitView.findViewById<TextView>(R.id.btn_habit_click)

            tvName.text = habit.name
            val left = habit.targetGoal - habit.progress

            tvLeft.text = "$left\nmore"

            btnClick.setOnClickListener {
                if (habit.progress < habit.targetGoal) {
                    habit.progress++

                    if (habit.progress >= habit.targetGoal) {
                        habitList.remove(habit)
                    }

                    sharedPreferences.edit()
                        .putString("HABIT_LIST", gson.toJson(habitList))
                        .apply()

                    loadHabits()
                }
            }

            llHabitContent.addView(habitView)
        }
    }

    private fun setupBottomNavigation() {
        val navProfile = findViewById<ImageView>(R.id.nav_profile)
        navProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupTabs() {
        val tabHabit = findViewById<TextView>(R.id.tab_habit)
        val tabTodo = findViewById<TextView>(R.id.tab_todo)
        val tvAddNewText = findViewById<TextView>(R.id.tv_add_new_text)
        val btnAddNew = findViewById<LinearLayout>(R.id.ll_add_new_button)

        tabHabit.setOnClickListener {
            tabHabit.setBackgroundResource(R.drawable.bg_tab_active)
            tabTodo.setBackgroundResource(R.drawable.bg_tab_inactive)
            llHabitContent.visibility = View.VISIBLE
            llTodoContent.visibility = View.GONE
            tvAddNewText.text = "Add New Habit"
        }

        tabTodo.setOnClickListener {
            tabHabit.setBackgroundResource(R.drawable.bg_tab_inactive)
            tabTodo.setBackgroundResource(R.drawable.bg_tab_active)
            llHabitContent.visibility = View.GONE
            llTodoContent.visibility = View.VISIBLE
            tvAddNewText.text = "Add New List"
        }

        btnAddNew.setOnClickListener {
            if (tvAddNewText.text == "Add New Habit") {
                val intent = Intent(this, AddHabitActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, AddTodoActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupInstructionModal() {
        val btnDots = findViewById<TextView>(R.id.btn_instruction_dots)

        btnDots?.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_instruction)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val btnClose = dialog.findViewById<ImageView>(R.id.btn_close_instruction)
            btnClose?.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun setupCalendar() {
        val rvCalendar = findViewById<RecyclerView>(R.id.rv_calendar_strip)
        val btnLeft = findViewById<TextView>(R.id.btn_calendar_left)
        val btnRight = findViewById<TextView>(R.id.btn_calendar_right)

        rvCalendar.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        loadCalendarPage(rvCalendar, 0)

        btnLeft.setOnClickListener {
            loadCalendarPage(rvCalendar, --currentOffset)
        }

        btnRight.setOnClickListener {
            loadCalendarPage(rvCalendar, ++currentOffset)
        }
    }

    private fun loadCalendarPage(rvCalendar: RecyclerView, offset: Int) {
        val centerDate = LocalDate.now().plusDays(offset.toLong())
        val startDate = centerDate.minusDays(2)
        val pagedDays = generatePagedDateItems(startDate, 5)
        rvCalendar.adapter = CalendarAdapter(pagedDays, centerDate)
    }

    private fun generatePagedDateItems(baseDate: LocalDate, numDays: Int): List<DateItem> {
        val list = mutableListOf<DateItem>()
        val formatterDate = DateTimeFormatter.ofPattern("d", Locale.ENGLISH)

        for (i in 0 until numDays) {
            val date = baseDate.plusDays(i.toLong())

            val dayName = when (date.dayOfWeek) {
                java.time.DayOfWeek.SUNDAY -> "S"
                java.time.DayOfWeek.MONDAY -> "M"
                java.time.DayOfWeek.TUESDAY -> "T"
                java.time.DayOfWeek.WEDNESDAY -> "W"
                java.time.DayOfWeek.THURSDAY -> "TH"
                java.time.DayOfWeek.FRIDAY -> "F"
                java.time.DayOfWeek.SATURDAY -> "SA"
            }

            val dayNumber = date.format(formatterDate)
            list.add(DateItem(dayName, dayNumber, date))
        }
        return list
    }
}