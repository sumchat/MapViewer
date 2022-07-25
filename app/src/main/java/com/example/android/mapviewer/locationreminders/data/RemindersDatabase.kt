package com.example.android.mapviewer.locationreminders.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room Database that contains the reminders table.
 */
@Database(entities = [ReminderDTO::class], version = 1, exportSchema = false)
abstract class RemindersDatabase : RoomDatabase() {

    abstract fun reminderDao(): RemindersDao
}