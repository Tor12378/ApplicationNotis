package com.example.application

import android.provider.BaseColumns

object EventContract {
    object EventEntry : BaseColumns {
        const val TABLE_NAME = "events"
        const val _ID = "_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
        const val COLUMN_DESCRIPTION = "description"
    }
}