package com.example.android.mapviewer

import android.app.Application
import com.example.android.mapviewer.locationreminders.SaveReminderViewModel
import com.example.android.mapviewer.locationreminders.data.LocalDB
import com.example.android.mapviewer.locationreminders.data.ReminderDataSource
import com.example.android.mapviewer.locationreminders.data.RemindersLocalRepository
import com.example.android.mapviewer.reminderslist.RemindersListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
            /*viewModel {
                RemindersListViewModel(
                    get(),
                    get() as ReminderDataSource
                )
            }*/
            //Declare singleton definitions to be later injected using by inject()
            single {
                //This view model is declared singleton to be used across multiple fragments
                SaveReminderViewModel(
                    get(),
                    get() as ReminderDataSource
                )

            }
            single {
                //This view model is declared singleton to be used across multiple fragments

                RemindersListViewModel(
                    get(),
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(this@MyApp) }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
    }
}