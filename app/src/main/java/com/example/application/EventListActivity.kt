package com.example.application

import EventDbHelper

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class EventListActivity : AppCompatActivity() {
    private lateinit var dbHelper: EventDbHelper
    private lateinit var eventListView: ListView
    private lateinit var eventListAdapter: ArrayAdapter<String>
    private lateinit var eventList: List<Event>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)

        dbHelper = EventDbHelper(this)

        eventListView = findViewById(R.id.eventListView)

        eventList = getAllEvents()
        val eventTitles = eventList.map { it.title }
        eventListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, eventTitles)
        eventListView.adapter = eventListAdapter

        eventListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedEvent = eventList[position]
            val intent = Intent(this, EventDetailsActivity::class.java)
            intent.putExtra("event_id", selectedEvent.id)
            startActivity(intent)
        }
    }

    private fun getAllEvents(): List<Event> {
        return dbHelper.getAllEvents()
    }
}