package com.example.application
import EventDbHelper
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.application.Event
import com.example.application.EventContract
import com.example.application.R
import com.google.android.material.textfield.TextInputEditText

class EditEventActivity : AppCompatActivity() {
    companion object {
        const val EDIT_EVENT_REQUEST_CODE = 1
    }
    private var eventId: Int = -1
    private lateinit var titleEditText: TextInputEditText
    private lateinit var dateEditText: TextInputEditText
    private lateinit var timeEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        titleEditText = findViewById(R.id.titleEditText)
        dateEditText = findViewById(R.id.dateEditText)
        timeEditText = findViewById(R.id.timeEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            saveEvent()
        }

        eventId = intent.getIntExtra("event_id", -1)
        val event = getEventById(eventId)
        populateEventData(event)
    }

    private fun getEventById(eventId: Int): Event {
        val dbHelper = EventDbHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            EventContract.EventEntry.COLUMN_TITLE,
            EventContract.EventEntry.COLUMN_DATE,
            EventContract.EventEntry.COLUMN_TIME,
            EventContract.EventEntry.COLUMN_DESCRIPTION
        )

        val selection = "${EventContract.EventEntry._ID} = ?"
        val selectionArgs = arrayOf(eventId.toString())

        val cursor = db.query(
            EventContract.EventEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var event: Event? = null

        if (cursor.moveToFirst()) {
            val titleIndex = cursor.getColumnIndex(EventContract.EventEntry.COLUMN_TITLE)
            val dateIndex = cursor.getColumnIndex(EventContract.EventEntry.COLUMN_DATE)
            val timeIndex = cursor.getColumnIndex(EventContract.EventEntry.COLUMN_TIME)
            val descriptionIndex = cursor.getColumnIndex(EventContract.EventEntry.COLUMN_DESCRIPTION)

            val title = cursor.getString(titleIndex)
            val date = cursor.getString(dateIndex)
            val time = cursor.getString(timeIndex)
            val description = cursor.getString(descriptionIndex)

            event = Event(eventId, title, date, time, description)
        }

        cursor.close()
        db.close()

        return event ?: throw IllegalStateException("Event not found")
    }

    private fun populateEventData(event: Event) {
        titleEditText.setText(event.title)
        dateEditText.setText(event.date)
        timeEditText.setText(event.time)
        descriptionEditText.setText(event.description)
    }

    private fun saveEvent() {
        val updatedTitle = titleEditText.text.toString()
        val updatedDate = dateEditText.text.toString()
        val updatedTime = timeEditText.text.toString()
        val updatedDescription = descriptionEditText.text.toString()

        val dbHelper = EventDbHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(EventContract.EventEntry.COLUMN_TITLE, updatedTitle)
            put(EventContract.EventEntry.COLUMN_DATE, updatedDate)
            put(EventContract.EventEntry.COLUMN_TIME, updatedTime)
            put(EventContract.EventEntry.COLUMN_DESCRIPTION, updatedDescription)
        }

        val selection = "${EventContract.EventEntry._ID} = ?"
        val selectionArgs = arrayOf(eventId.toString())

        val updatedRows = db.update(
            EventContract.EventEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )

        db.close()

        if (updatedRows > 0) {
            val intent = Intent()
            intent.putExtra("event_id", eventId)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            // Обработка ошибки сохранения
        }
    }

    // Rest of the code...
}