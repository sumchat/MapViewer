package com.example.android.mapviewer.reminderslist

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.mapviewer.R
import com.example.android.mapviewer.databinding.FragmentRemindersBinding
import com.example.android.mapviewer.locationreminders.ReminderDataItem
import com.example.android.mapviewer.locationreminders.ReminderObject
import com.example.android.mapviewer.locationreminders.reminderslist.RemindersListAdapter
import com.example.android.mapviewer.utils.setup
import org.koin.android.ext.android.inject

class ReminderListFragment : Fragment() {
    //use Koin to retrieve the ViewModel instance
   // val _viewModel: RemindersListViewModel by viewModel()

    val _viewModel: RemindersListViewModel by inject()
   //private lateinit var _viewmodel:RemindersListViewModel
    private lateinit var binding: FragmentRemindersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_reminders, container, false
            )

        binding.viewModel = _viewModel


        //setHasOptionsMenu(true)
       // setDisplayHomeAsUpEnabled(false)
       // setTitle(getString(R.string.app_name))
       // setupObserver()

        binding.refreshLayout.setOnRefreshListener { _viewModel.loadReminders() }

        return binding.root
    }
 /*   fun showReminders()
    {
        val fm = getParentFragmentManager()
        val _mapView =
        (fm!!.findFragmentById(R.id.mapFragment) as MapFragment?)?.mapView
        val _graphicsOverlay =
            (fm!!.findFragmentById(R.id.mapFragment) as MapFragment?)?._graphicsOverlay
        var remindersList =  _viewModel.remindersList as MutableList<ReminderDataItem>

        for (pt in remindersList) {
            val pointGeometry = pt.latitude?.let {
                pt.longitude?.let { it1 ->
                    com.esri.arcgisruntime.geometry.Point(
                        it1,
                        it, SpatialReferences.getWebMercator()
                    )
                }
            }
            val pointGraphic = Graphic(pointGeometry)

                _graphicsOverlay?.graphics?.add(pointGraphic)


        }
    }*/




    /*  private fun setupObserver() {
          _authenticationViewModel.navigateBackToAuthenticationActivity.observe(
              viewLifecycleOwner,
              { navigateToAuthenticationActivity ->
                  if (navigateToAuthenticationActivity) {
                      createIntentForNavigatingToAuthenticationActivity()
                  }
              })

          _viewModel.intentCommand.observe(viewLifecycleOwner, { command ->
              if (command is IntentCommand.BackTo) {
                  navigateToAuthenticationActivity(command)
              } else
                  if (command is IntentCommand.ToReminderDescriptionActivity) {
                      navigateToReminderDescriptionActivity(command)
                  }
          })
      }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
       /* binding.addReminderFAB.setOnClickListener {
            navigateToAddReminder()
        }*/
    }

    override fun onResume() {
        super.onResume()
        //load the reminders list on the ui
        _viewModel.loadReminders()
       /* _viewModel.remindersList.observe(viewLifecycleOwner, {
            it?.let {
                showReminders()
            }
        })*/
    }

   /* private fun navigateToAddReminder() {
        //use the navigationCommand live data to navigate between the fragments
        _viewModel.navigationCommand.postValue(
            NavigationCommand.To(
                ReminderListFragmentDirections.toSaveReminder()
            )
        )
    }*/
   private fun adapterOnClick(reminderDataItem: ReminderDataItem,action:String) {
      //val _reminderDataItem = MutableLiveData<ReminderDataItem>().apply { postValue(reminderDataItem)}
      val _reminderObject = ReminderObject(
          reminderDataItem,
           action)
       _viewModel.activeReminderDataItem.value = _reminderObject
       //if(action === "Delete")
        //   _viewModel.deleteReminder(reminderDataItem)

   }

   /* private fun deleteOnClick(reminderDataItem: ReminderDataItem) {
        //val _reminderDataItem = MutableLiveData<ReminderDataItem>().apply { postValue(reminderDataItem)}
        _viewModel.activeReminderDataItem.value = reminderDataItem
    }*/

    private fun setupRecyclerView() {
        val adapter = RemindersListAdapter { _reminderDataItem1:ReminderDataItem,action:String ->
            adapterOnClick(_reminderDataItem1,action)

        }



//        setup the recycler view using the extension function
        binding.reminderssRecyclerView.setup(adapter)
    }



}
