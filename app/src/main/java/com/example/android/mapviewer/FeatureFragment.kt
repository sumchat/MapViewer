package com.example.android.mapviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mapviewer.data.FieldItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeatureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeatureFragment(private val mFieldItems: List<FieldItem>) : Fragment() {
    private lateinit var viewOfLayout: View
    private lateinit var mRecyclerView1: RecyclerView
    private lateinit var headerText: TextView
    private lateinit var _fieldItemAdapter:FieldValueAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout =  inflater.inflate(R.layout.fragment_feature, container, false)
        mRecyclerView1 = viewOfLayout.findViewById(R.id.recycler_view1)
        headerText = viewOfLayout.findViewById(R.id.textView3)
        //setupAdapter()
        return viewOfLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    fun setupAdapter()
    {
        _fieldItemAdapter = FieldValueAdapter()
        if (mRecyclerView1 != null) {
            mRecyclerView1.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL ,false)

            mRecyclerView1.adapter = _fieldItemAdapter
            _fieldItemAdapter.submitList(mFieldItems)


            // _fieldItemAdapter.notifyDataSetChanged();

        }
    }

}