package com.example.tidely

data class HabitItem(
    val name: String,
    val targetGoal: Int,
    var progress: Int = 0
)