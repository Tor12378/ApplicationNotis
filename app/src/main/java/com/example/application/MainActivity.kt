package com.example.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addEventButton = findViewById<Button>(R.id.addEventButton)
        val viewEventsButton = findViewById<Button>(R.id.viewEventsButton)
        val calendarButton = findViewById<Button>(R.id.calendarButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)

        addEventButton.setOnClickListener {
            val intent = Intent(this, EventEditActivity::class.java)
            startActivity(intent)
        }

        viewEventsButton.setOnClickListener {
            val intent = Intent(this, EventListActivity::class.java)
            startActivity(intent)
        }

        calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
