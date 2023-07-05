package com.example.application

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EventDetailsActivity : AppCompatActivity() {

    private lateinit var eventDateSet: String // Поле для хранения даты события

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val eventId = intent.getIntExtra("event_id", -1)
        val event = getEventById(eventId)

        // Заполнение полей с информацией о событии
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val timeTextView = findViewById<TextView>(R.id.timeTextView)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)

        titleTextView.text = event.title
        dateTextView.text = event.date
        timeTextView.text = event.time
        descriptionTextView.text = event.description

        // Сохранение даты события
        eventDateSet = event.date

        val editButton = findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            // Обработка нажатия на кнопку "Редактировать"
            val intent = Intent(this, EditEventActivity::class.java)
            intent.putExtra("event_id", eventId)
            startActivityForResult(intent, EDIT_EVENT_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_EVENT_REQUEST_CODE && resultCode == RESULT_OK) {
            val editedEventId = data?.getIntExtra("event_id", -1)
            if (editedEventId != null && editedEventId != -1) {
                val editedEvent = getEventById(editedEventId)
                // Обновите данные в UI с использованием отредактированного события
                // Например, обновите поля с информацией о событии
                val titleTextView = findViewById<TextView>(R.id.titleTextView)
                val dateTextView = findViewById<TextView>(R.id.dateTextView)
                val timeTextView = findViewById<TextView>(R.id.timeTextView)
                val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)

                titleTextView.text = editedEvent.title
                dateTextView.text = editedEvent.date
                timeTextView.text = editedEvent.time
                descriptionTextView.text = editedEvent.description
            }
        }
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

    // Вспомогательный класс для работы с базой данных
    private class EventDbHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            val createTableQuery = "CREATE TABLE ${EventContract.EventEntry.TABLE_NAME} (" +
                    "${EventContract.EventEntry._ID} INTEGER PRIMARY KEY," +
                    "${EventContract.EventEntry.COLUMN_TITLE} TEXT," +
                    "${EventContract.EventEntry.COLUMN_DATE} TEXT," +
                    "${EventContract.EventEntry.COLUMN_TIME} TEXT," +
                    "${EventContract.EventEntry.COLUMN_DESCRIPTION} TEXT)"

            db.execSQL(createTableQuery)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // Здесь можно реализовать обновление схемы базы данных
        }

        companion object {
            private const val DATABASE_NAME = "event.db"
            private const val DATABASE_VERSION = 1
        }
    }

    companion object {
        const val EDIT_EVENT_REQUEST_CODE = 1
    }
}