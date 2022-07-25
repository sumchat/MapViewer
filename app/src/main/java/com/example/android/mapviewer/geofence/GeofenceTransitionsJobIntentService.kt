package com.example.android.mapviewer.geofence

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.example.android.mapviewer.locationreminders.data.ReminderDataSource
import com.example.android.mapviewer.locationreminders.data.ReminderDTO
import com.example.android.mapviewer.locationreminders.data.Result
import com.example.android.mapviewer.locationreminders.ReminderDataItem
import com.example.android.mapviewer.utils.sendNotification
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class GeofenceTransitionsJobIntentService : JobIntentService(), CoroutineScope {

    //private val repository by inject<ReminderDataSource>()
    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    companion object {
        private const val JOB_ID = 573

        //        TODO: call this to start the JobIntentService to handle the geofencing transition events
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionsJobIntentService::class.java, JOB_ID,
                intent
            )
        }
    }

    override fun onHandleWork(intent: Intent) {
        //TODO: handle the geofencing transition events and
        // send a notification to the user when he enters the geofence area
        //TODO call @sendNotification
        val geoFenceEvent= GeofencingEvent.fromIntent(intent)
        if (geoFenceEvent != null) {
            when (geoFenceEvent.geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    if (geoFenceEvent != null) {
                        geoFenceEvent.triggeringGeofences?.let { sendNotification(it) }
                    }
                }
            }
        }
    }

    //TODO: get the request id of the current geofence
    private fun sendNotification(triggeringGeofences: List<Geofence>) {

        for (item in triggeringGeofences) {
            val requestId = item.requestId

            //Get the local repository instance
            val remindersLocalRepository by inject<ReminderDataSource>()//RemindersDataSource by inject()
//        Interaction to the repository has to be through a coroutine scope
            CoroutineScope(coroutineContext).launch(SupervisorJob()) {
                //get the reminder with the request id
                val result = remindersLocalRepository.getReminder(requestId)
                if (result is Result.Success<ReminderDTO>) {
                    val reminderDTO = result.data
                    //send a notification to the user with the reminder details
                    sendNotification(
                        this@GeofenceTransitionsJobIntentService, ReminderDataItem(
                            reminderDTO.title,
                            reminderDTO.description,
                           // reminderDTO.location,
                            reminderDTO.latitude,
                            reminderDTO.longitude,
                            reminderDTO.id
                        )
                    )
                }
            }
        }
    }

}