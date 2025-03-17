package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.provider.AlarmClock

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alarmTextView: TextView = findViewById(R.id.tvAlarmInfo)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: android.content.Intent) {
        if (intent.extras != null) {
            val hour = intent.getIntExtra(AlarmClock.EXTRA_HOUR, -1)
            val minute = intent.getIntExtra(AlarmClock.EXTRA_MINUTES, -1)
            val message = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE) ?: "No message"

            val alarmTextView: TextView = findViewById(R.id.tvAlarmInfo)
            alarmTextView.text = "Alarm Set: $hour:$minute\n$message"
        }
    }
}
