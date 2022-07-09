package com.example.android.mapviewer

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.android.mapviewer.databinding.FragmentWelcomeBinding
import com.google.android.material.navigation.NavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : Fragment() {

    lateinit var mActivity : FragmentActivity

    private lateinit var binding: FragmentWelcomeBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity?.let { mActivity = it }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_welcome,container,false)
        // binding..setOnClickListener {v: View -> v.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToWelcomeFragment())}
       //  binding.welcomebutton.setOnClickListener({v:View -> v.findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToMapFragment())})
        binding.welcomebutton.setOnClickListener({v:View -> v.findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToMapGalleryFragment())})


        //return inflater.inflate(R.layout.fragment_welcome, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

     //   setUpToolbar()

    }
    private fun setUpToolbar() {

        val mainActivity = mActivity as MainActivity
      val _view =  mActivity?.findViewById<View>(R.id.navView1)
       // val navigationView: NavigationView = mActivity?.findViewById(R.id.navView)//mainActivity.findViewById(R.id.navView)
        val navController = NavHostFragment.findNavController(this)
       // val appBarConfiguration = mainActivity.appBarConfiguration
       // NavigationUI.setupActionBarWithNavController(mainActivity,navController,appBarConfiguration)
       // NavigationUI.setupWithNavController(navigationView,navController)

    }




}