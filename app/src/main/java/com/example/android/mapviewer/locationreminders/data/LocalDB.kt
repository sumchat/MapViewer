package com.example.android.mapviewer.locationreminders.data

import android.content.Context
import androidx.room.Room

object LocalDB {
    /**
     * static method that creates a reminder class and returns the DAO of the reminder
     */
    fun createRemindersDao(context: Context): RemindersDao {
        return Room.databaseBuilder(
            context.applicationContext,
            RemindersDatabase::class.java, "locationReminders.db"
        ).build().reminderDao()
    }
}