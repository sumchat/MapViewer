package com.example.android.mapviewer

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.esri.arcgisruntime.data.Feature
import com.example.android.mapviewer.network.ElevationViewModel
import com.example.android.mapviewer.data.ElevationRequestParam
import com.example.android.mapviewer.data.ElevationResponse
import com.example.android.mapviewer.data.ElevationResponseClass
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import com.example.android.mapviewer.databinding.FragmentProfileBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.math.RoundingMode
import java.text.DecimalFormat
import android.view.Gravity

import android.widget.FrameLayout

import android.widget.TextView





/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment(private val mFeature:Feature) : Fragment() {
    lateinit var binding : FragmentProfileBinding
    lateinit var chart:LineChart
     var _container:ViewGroup? = null

    /**
     * Lazily initialize our [ElevationViewModel].
     */
    private val elevViewModel: ElevationViewModel by lazy {
        ViewModelProvider(this).get(ElevationViewModel::class.java)
    }


    fun setUpChart(container:ViewGroup?){
        chart.setOnChartValueSelectedListener(object:
            OnChartValueSelectedListener {

            override fun onNothingSelected() {
                Log.i("Entry selected", "Nothing selected.")
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                Log.i("Entry selected", e.toString())
                val x: Float = e!!.x
                val y: Float = e!!.y
                chart.highlightValue(h)

            }
        })

        chart.animateXY(2000, 2000)
        chart.setTouchEnabled(true)
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        // force pinch zoom along both axis
        chart.setPinchZoom(true)

    }

    fun showLabels()
    {
        val xAxisName = TextView(activity)
        xAxisName.setText("Date")
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
        params.setMargins(0, 0, 0, 20)

        val yAxisName = TextView(activity, null)
        yAxisName.setText("Yaxis Label")
        val params2 = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params2.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL

        _container?.addView(xAxisName, params)
        _container?.addView(yAxisName, params2)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)//inflater.inflate(R.layout.fragment_profile, container, false)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = elevViewModel
        chart = binding.chartView
        _container = container
        setUpChart(container)

        elevViewModel.response.observe(viewLifecycleOwner)
        {
            response ->
            if(response != null)
            {
                processChartData(response)
            }
        }
        elevViewModel.getElevationData(mFeature)
        return binding.root
    }

    fun processChartData(chartData: ElevationResponseClass)
    {
      val _paths =
          chartData.results?.get(0)?.value?.features?.get(0)?.geometry?.paths
      val entries = mutableListOf<Entry>()
        try {
            for (i in 0.._paths!!.size - 1) {
                for (j in 0.._paths[i].size - 1) {
                    val xyzm = _paths[i][j];
                    val dist = ((xyzm[3]) * 0.000621371) //((xyzm.m) * 0.000621371) //metersToMiles
                    val elev = xyzm[1] * 3.28084 //xyzm.y * 3.28084
                    val df = DecimalFormat("#.###")
                    df.roundingMode = RoundingMode.DOWN
                    val roundoff_dist = df.format(dist)
                    val roundoffElev = df.format(elev)
                    val _entry = Entry(
                        roundoff_dist.toFloat(),
                        roundoffElev.toFloat()
                    )
                    entries.add(_entry)
                }
            }
        }
        catch(e:Exception){
            Log.d("Logs", e.toString())
        }

        var dataSet = LineDataSet(entries, "Elevation Profile")
        dataSet.color = Color.BLACK
        dataSet.valueTextColor = Color.GRAY
        dataSet.highLightColor = Color.RED
        dataSet.setDrawValues(false)
        dataSet.lineWidth = 1.5f
        dataSet.isHighlightEnabled = true
        dataSet.setDrawHighlightIndicators(true)
        chart.data = LineData(dataSet)
        showLabels()

        val desc = Description()
        desc.setText("X:Distance (meters) \n Y :Elevation(ft)")
        desc.setTextSize(12f)
        chart.setDescription(desc)

       // chart.animateXY(2000, 2000)
        chart.invalidate()

    }



}