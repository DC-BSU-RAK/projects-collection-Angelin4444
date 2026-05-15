package com.example.tidely

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class CalendarAdapter(
    private val dates: List<DateItem>,
    private val selectedDate: LocalDate
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: LinearLayout = view.findViewById(R.id.ll_date_container)
        val tvDayName: TextView = view.findViewById(R.id.tv_day_name)
        val tvDateNumber: TextView = view.findViewById(R.id.tv_date_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val dateItem = dates[position]
        holder.tvDayName.text = dateItem.dayName
        holder.tvDateNumber.text = dateItem.dayNumber

        // Highlights the current selected date
        if (dateItem.date == selectedDate) {
            holder.container.setBackgroundResource(R.drawable.bg_date_selected_pill)
            holder.tvDayName.setTextColor(Color.WHITE)
            holder.tvDateNumber.setTextColor(Color.WHITE)
        } else {
            holder.container.setBackgroundColor(Color.TRANSPARENT)
            holder.tvDayName.setTextColor(Color.parseColor("#003366"))
            holder.tvDateNumber.setTextColor(Color.parseColor("#003366"))
        }
    }

    override fun getItemCount(): Int {
        return dates.size
    }
}