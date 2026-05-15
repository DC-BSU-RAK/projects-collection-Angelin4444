package com.example.tidely

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CustomizeDaysActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize_days)

        val btnBack = findViewById<TextView>(R.id.btn_back_days)
        val btnSave = findViewById<TextView>(R.id.btn_save_days)

        val rowSun = findViewById<RelativeLayout>(R.id.row_sunday)
        val rowMon = findViewById<RelativeLayout>(R.id.row_monday)
        val rowTue = findViewById<RelativeLayout>(R.id.row_tuesday)
        val rowWed = findViewById<RelativeLayout>(R.id.row_wednesday)
        val rowThu = findViewById<RelativeLayout>(R.id.row_thursday)
        val rowFri = findViewById<RelativeLayout>(R.id.row_friday)
        val rowSat = findViewById<RelativeLayout>(R.id.row_saturday)

        val checkSun = findViewById<TextView>(R.id.check_sunday)
        val checkMon = findViewById<TextView>(R.id.check_monday)
        val checkTue = findViewById<TextView>(R.id.check_tuesday)
        val checkWed = findViewById<TextView>(R.id.check_wednesday)
        val checkThu = findViewById<TextView>(R.id.check_thursday)
        val checkFri = findViewById<TextView>(R.id.check_friday)
        val checkSat = findViewById<TextView>(R.id.check_saturday)

        val savedDays = intent.getStringExtra("SAVED_DAYS") ?: ""

        if (savedDays == "Everyday") {
            checkSun.visibility = View.VISIBLE
            checkMon.visibility = View.VISIBLE
            checkTue.visibility = View.VISIBLE
            checkWed.visibility = View.VISIBLE
            checkThu.visibility = View.VISIBLE
            checkFri.visibility = View.VISIBLE
            checkSat.visibility = View.VISIBLE
        } else {
            val daysList = savedDays.split(" ")
            if (daysList.contains("S")) checkSun.visibility = View.VISIBLE
            if (daysList.contains("M")) checkMon.visibility = View.VISIBLE
            if (daysList.contains("T")) checkTue.visibility = View.VISIBLE
            if (daysList.contains("W")) checkWed.visibility = View.VISIBLE
            if (daysList.contains("TH")) checkThu.visibility = View.VISIBLE
            if (daysList.contains("F")) checkFri.visibility = View.VISIBLE
            if (daysList.contains("SA")) checkSat.visibility = View.VISIBLE
        }

        fun toggleCheck(checkView: TextView) {
            if (checkView.visibility == View.VISIBLE) {
                checkView.visibility = View.INVISIBLE
            } else {
                checkView.visibility = View.VISIBLE
            }
        }

        rowSun.setOnClickListener { toggleCheck(checkSun) }
        rowMon.setOnClickListener { toggleCheck(checkMon) }
        rowTue.setOnClickListener { toggleCheck(checkTue) }
        rowWed.setOnClickListener { toggleCheck(checkWed) }
        rowThu.setOnClickListener { toggleCheck(checkThu) }
        rowFri.setOnClickListener { toggleCheck(checkFri) }
        rowSat.setOnClickListener { toggleCheck(checkSat) }

        btnBack.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            var resultDays = ""
            var selectedCount = 0

            if (checkSun.visibility == View.VISIBLE) { resultDays += "S "; selectedCount++ }
            if (checkMon.visibility == View.VISIBLE) { resultDays += "M "; selectedCount++ }
            if (checkTue.visibility == View.VISIBLE) { resultDays += "T "; selectedCount++ }
            if (checkWed.visibility == View.VISIBLE) { resultDays += "W "; selectedCount++ }
            if (checkThu.visibility == View.VISIBLE) { resultDays += "TH "; selectedCount++ }
            if (checkFri.visibility == View.VISIBLE) { resultDays += "F "; selectedCount++ }
            if (checkSat.visibility == View.VISIBLE) { resultDays += "SA "; selectedCount++ }

            if (selectedCount == 7) {
                resultDays = "Everyday"
            } else {
                resultDays = resultDays.trim()
            }

            val resultIntent = Intent()
            resultIntent.putExtra("SELECTED_DAYS", resultDays)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}