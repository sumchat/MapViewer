package com.example.android.mapviewer.locationreminders.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object for the reminders table.
 */
@Dao
interface RemindersDao {
    /**
     * @return all reminders.
     */
    @Query("SELECT * FROM reminders")
     fun getReminders(): List<ReminderDTO>

    /**
     * @param reminderId the id of the reminder
     * @return the reminder object with the reminderId
     */
    @Query("SELECT * FROM reminders where entry_id = :reminderId")
     fun getReminderById(reminderId: String): ReminderDTO?

    /**
     * Insert a reminder in the database. If the reminder already exists, replace it.
     *
     * @param reminder the reminder to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReminder(reminder: ReminderDTO)

    /**
     * Delete all reminders.
     */
    @Query("DELETE FROM reminders")
     fun deleteAllReminders()

    @Query("Delete from reminders where entry_id = :reminderId")
     fun deleteReminder(reminderId:String)

}