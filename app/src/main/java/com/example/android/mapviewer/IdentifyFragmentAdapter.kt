package com.example.android.mapviewer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.esri.arcgisruntime.data.Feature
import com.example.android.mapviewer.data.FieldItem
import com.example.android.mapviewer.reminderslist.ReminderListFragment

class IdentifyFragmentAdapter(private val operation:String,private val mFieldItems: List<FieldItem>?, private val _feature: Feature?,
                              fragmentActivity: FragmentActivity, private var totalTabs: Int) :
    FragmentStateAdapter(fragmentActivity) {

    // this is for fragment tabs
    override fun createFragment(position: Int): Fragment {
          if(operation == "Identify") {

              when (position) {
                  0 -> {
                      val featureFragment: FeatureFragment =
                          mFieldItems?.let { FeatureFragment(it) }!!
                      return featureFragment
                  }
                  1 -> {
                      return _feature?.let { ProfileFragment(it) }!!
                  }

                  else -> {
                      val featureFragment: FeatureFragment =
                          mFieldItems?.let { FeatureFragment(it) }!!
                      return featureFragment
                  }

              }
          }
        else if (operation == "ShowReminders")
          {
              val reminderFragment: ReminderListFragment = ReminderListFragment()

              return reminderFragment
          }
        else {
              val featureFragment: FeatureFragment =
                  mFieldItems?.let { FeatureFragment(it) }!!
              return featureFragment
          }



    }

    // this counts total number of tabs
    override fun getItemCount(): Int {
        return totalTabs
    }
}