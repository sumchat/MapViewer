package com.example.android.mapviewer

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.esri.arcgisruntime.data.Feature
import com.esri.arcgisruntime.portal.PortalItem
import com.example.android.mapviewer.data.FieldItem

class IdentifyFragmentAdapter ( private val mFieldItems: List<FieldItem>,private val _feature:Feature,fragmentActivity: FragmentActivity, private var totalTabs: Int) :
    FragmentStateAdapter(fragmentActivity) {

    // this is for fragment tabs
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                val featureFragment: FeatureFragment = FeatureFragment(mFieldItems)
                return featureFragment
            }
            1 -> {
                return ProfileFragment(_feature)
            }

            else -> {
                val featureFragment: FeatureFragment = FeatureFragment(mFieldItems)
                return featureFragment
            }

        }
    }

    // this counts total number of tabs
    override fun getItemCount(): Int {
        return totalTabs
    }
}