package com.example.application

import EventDbHelper
import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarActivity : AppCompatActivity() {

    private lateinit var eventDateSet: String // Поле для хранения даты события
    private lateinit var calendarView: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        calendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = formatDate(dayOfMonth, month + 1, year)
            Toast.makeText(this, "Выбранная дата: $selectedDate", Toast.LENGTH_SHORT).show()

            // Здесь можно выполнить дополнительные действия на выбранную дату

            // Проверяем, совпадает ли выбранная дата с датой события
            if (selectedDate == eventDateSet) {
                markEventDateOnCalendar(selectedDate)
            }
        }

        eventDateSet = getEventDateFromDatabase() // Получаем дату события из базы данных
    }

    private fun getEventDateFromDatabase(): String {
        val dbHelper = EventDbHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(EventContract.EventEntry.COLUMN_DATE)

        val cursor = db.query(
            EventContract.EventEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        var eventDate: String? = null

        if (cursor.moveToFirst()) {
            val dateIndex = cursor.getColumnIndex(EventContract.EventEntry.COLUMN_DATE)
            eventDate = cursor.getString(dateIndex)
        }

        cursor.close()
        db.close()

        return eventDate ?: "01/01/2022" // Возвращаем фиксированную дату для демонстрации
    }

    private fun markEventDateOnCalendar(date: String) {
        // Отметить дату на календаре
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val parts = date.split("/")
        val year = parts[2].toInt()
        val month = parts[1].toInt() - 1
        val dayOfMonth = parts[0].toInt()
        val selectedTimeInMillis = calendarView.date
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.timeInMillis = selectedTimeInMillis
        selectedCalendar.set(year, month, dayOfMonth)
        val selectedDateTimeInMillis = selectedCalendar.timeInMillis
        calendarView.setDate(selectedDateTimeInMillis, true, true)
    }

    // Функция для форматирования даты в строку
    private fun formatDate(day: Int, month: Int, year: Int): String {
        return String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year)

    }
}



