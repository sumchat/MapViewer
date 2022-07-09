package com.example.android.mapviewer

import ViewPagerAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil.setContentView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// TODO: Rename parameter arguments, choose names that match


/**
 * A simple [Fragment] subclass.
 * Use the [MainTabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainTabFragment : Fragment() {
    private lateinit var pager: ViewPager2 // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar    // creating object of ToolBar
    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main_tab, container, false)
        // set the references of the declared objects above
        val root = inflater.inflate(R.layout.fragment_main_tab, container, false)
        viewOfLayout = inflater.inflate(R.layout.fragment_main_tab, container, false)

        pager = viewOfLayout.findViewById(R.id.viewPager)
        tab = viewOfLayout.findViewById(R.id.tab_layout)
        bar = viewOfLayout.findViewById(R.id.toolbar)
        // Initializing the ViewPagerAdapter
        val adapter = ViewPagerAdapter(childFragmentManager,lifecycle)

        // add fragment to the list
        adapter.addFragment(AttachmentFragment(), "Attachments")
        adapter.addFragment(AttributeFragment(), "Attributes")
        adapter.notifyDataSetChanged()

        // Adding the Adapter to the ViewPager
        pager.adapter = adapter

        // bind the viewPager with the TabLayout.
        //tab.setupWithViewPager(pager)
        TabLayoutMediator(tab, pager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
            pager.setCurrentItem(tab.position,true)
            // tab.text = "OBJECT ${(position + 1)}"
        }.attach()
        //setSupportActionBar(bar)
        return root //inflater.inflate(R.layout.fragment_main_tab, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val tabLayout = view.findViewById(R.id.tab_layout)
      /*  TabLayoutMediator(tab, pager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
           // tab.text = "OBJECT ${(position + 1)}"
        }.attach()*/
    }

    companion object {
        fun newInstance(bundle: Bundle? = null): MainTabFragment = MainTabFragment().apply {
            arguments = bundle
        }
    }



}