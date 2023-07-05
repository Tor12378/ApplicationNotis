package com.example.application

import EventDbHelper
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.application.EventContract.EventEntry

class EventEditActivity : AppCompatActivity() {
    private lateinit var dbHelper: EventDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_edit)

        dbHelper = EventDbHelper(this)

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            saveEvent()
        }
    }

    private fun saveEvent() {
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val dateEditText = findViewById<EditText>(R.id.dateEditText)
        val timeEditText = findViewById<EditText>(R.id.timeEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)

        val title = titleEditText.text.toString()
        val date = dateEditText.text.toString()
        val time = timeEditText.text.toString()
        val description = descriptionEditText.text.toString()

        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(EventEntry.COLUMN_TITLE, title)
            put(EventEntry.COLUMN_DATE, date)
            put(EventEntry.COLUMN_TIME, time)
            put(EventEntry.COLUMN_DESCRIPTION, description)
        }

        val newRowId = db.insert(EventEntry.TABLE_NAME, null, values)

        if (newRowId != -1L) {
            // Событие успешно сохранено
            finish()
        } else {
            // Возникла ошибка при сохранении события
            // Обработайте соответствующим образом
        }
    }
}