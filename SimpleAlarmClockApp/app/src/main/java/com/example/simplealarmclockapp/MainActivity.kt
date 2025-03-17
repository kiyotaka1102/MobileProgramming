package com.example.simplealarmclockapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.app.TimePickerDialog
import android.content.Intent
import java.util.*
import android.provider.AlarmClock

class MainActivity : AppCompatActivity() {
    private lateinit var timeTextView: TextView
    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeTextView = findViewById(R.id.tvTime)
        val pickTimeButton: Button = findViewById(R.id.btnPickTime)
        val setAlarmButton: Button = findViewById(R.id.btnSetAlarm)

        pickTimeButton.setOnClickListener {
            showTimePicker()
        }

        setAlarmButton.setOnClickListener {
            setAlarm()
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            this.selectedHour = selectedHour
            this.selectedMinute = selectedMinute
            timeTextView.text = String.format("Selected Time: %02d:%02d", selectedHour, selectedMinute)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun setAlarm() {
        val intent = Intent().apply {
            setClassName("com.example.myapplication", "com.example.myapplication.MainActivity")
            putExtra(AlarmClock.EXTRA_HOUR, selectedHour)
            putExtra(AlarmClock.EXTRA_MINUTES, selectedMinute)
            putExtra(AlarmClock.EXTRA_MESSAGE, "Wake up for class!")
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Second app not installed", Toast.LENGTH_SHORT).show()
        }
    }


}
