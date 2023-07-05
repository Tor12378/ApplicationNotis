import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.application.Event
import com.example.application.EventContract

class EventDbHelper(context: Context) :
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

    fun getAllEvents(): List<Event> {
        val db = readableDatabase

        val projection = arrayOf(
            EventContract.EventEntry._ID,
            EventContract.EventEntry.COLUMN_TITLE,
            EventContract.EventEntry.COLUMN_DATE,
            EventContract.EventEntry.COLUMN_TIME,
            EventContract.EventEntry.COLUMN_DESCRIPTION
        )

        val sortOrder = "${EventContract.EventEntry.COLUMN_DATE} ASC"

        val cursor = db.query(
            EventContract.EventEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        val eventList = mutableListOf<Event>()

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(EventContract.EventEntry._ID))
                val title = getString(getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_TITLE))
                val date = getString(getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_DATE))
                val time = getString(getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_TIME))
                val description = getString(getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_DESCRIPTION))

                val event = Event(id, title, date, time, description)
                eventList.add(event)
            }
            close()
        }

        return eventList
    }

    companion object {
        private const val DATABASE_NAME = "event.db"
        private const val DATABASE_VERSION = 1
    }
}
