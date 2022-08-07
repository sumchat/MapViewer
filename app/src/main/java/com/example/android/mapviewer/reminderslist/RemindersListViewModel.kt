package com.example.android.mapviewer.reminderslist

import android.app.Application
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.mapviewer.R
import com.example.android.mapviewer.locationreminders.BaseViewModel
import com.example.android.mapviewer.locationreminders.ReminderDataItem
import com.example.android.mapviewer.locationreminders.ReminderObject
import com.example.android.mapviewer.locationreminders.data.ReminderDTO
import com.example.android.mapviewer.locationreminders.data.ReminderDataSource
import com.example.android.mapviewer.locationreminders.data.Result
import kotlinx.coroutines.Dispatchers


import kotlinx.coroutines.launch

class RemindersListViewModel(
    app: Application,
    private val dataSource: ReminderDataSource
) : BaseViewModel(app) {
    // list that holds the reminder data to be displayed on the UI
    val remindersList = MutableLiveData<List<ReminderDataItem>>()
    val activeReminderDataItem = MutableLiveData<ReminderObject>()

    /**
     * Get all the reminders from the DataSource and add them to the remindersList to be shown on the UI,
     * or show error if any
     */
    fun loadReminders() {
        showLoading.value = true
        viewModelScope.launch {
            //interacting with the dataSource has to be through a coroutine
            val result = dataSource.getReminders()
            showLoading.postValue(false)
            when (result) {
                is Result.Success<*> -> {
                    val dataList = ArrayList<ReminderDataItem>()
                    dataList.addAll((result.data as List<ReminderDTO>).map { reminder ->
                        //map the reminder data from the DB to the be ready to be displayed on the UI
                        ReminderDataItem(
                            reminder.title,
                            reminder.description,

                            reminder.latitude,
                            reminder.longitude,
                            reminder.id
                        )
                    })
                    remindersList.value = dataList

                }
                is Result.Error ->
                    showSnackBar.value = result.message
            }

            //check if no data has to be shown
            invalidateShowNoData()
        }
    }

    suspend fun refreshReminders()
    {
       var result = dataSource.getReminders()

        showLoading.postValue(false)
        when (result) {
            is Result.Success<*> -> {
                val dataList = ArrayList<ReminderDataItem>()
                dataList.addAll((result.data as List<ReminderDTO>).map { reminder ->
                    //map the reminder data from the DB to the be ready to be displayed on the UI
                    ReminderDataItem(
                        reminder.title,
                        reminder.description,

                        reminder.latitude,
                        reminder.longitude,
                        reminder.id
                    )
                })
                remindersList.postValue(dataList)


            }
            is Result.Error ->
                showSnackBar.postValue(result.message)
        }
    }


    fun deleteReminder(reminderId:String)
    {
        viewModelScope.launch(Dispatchers.IO) {

           val result = dataSource.deleteReminder(reminderId)
            when (result) {
                 true -> {
                    refreshReminders()
                  showSnackBar.postValue("reminder deleted")

                }
                false ->
                    showSnackBar.postValue( "cannot be deleted")
            }

        }
    }



    /**
     * Inform the user that there's not any data if the remindersList is empty
     */
    private fun invalidateShowNoData() {
        showNoData.value = remindersList.value == null || remindersList.value!!.isEmpty()
    }
}