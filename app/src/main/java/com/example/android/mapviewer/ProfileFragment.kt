package com.example.android.mapviewer

import android.os.Bundle
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
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import com.example.android.mapviewer.databinding.FragmentProfileBinding
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.math.RoundingMode
import java.text.DecimalFormat


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment(private val mFeature:Feature) : Fragment() {
    lateinit var binding : FragmentProfileBinding
    /**
     * Lazily initialize our [ElevationViewModel].
     */
    private val elevViewModel: ElevationViewModel by lazy {
        ViewModelProvider(this).get(ElevationViewModel::class.java)
    }


    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


       /* val entries =
            myData.map {
                Entry(
                    it.timestamp.toFloat(),
                    it.value.toFloat()
                )
            }
            val dataSet = LineDataSet(entries, "Unused label")
    it.color = Color.BLACK
    it.valueTextColor = Color.GRAY
    it.highLightColor = Color.RED
    it.setDrawValues(false)
    it.lineWidth = 1.5f
    it.isHighlightEnabled = true
    it.setDrawHighlightIndicators(false)
}

chartView.data = LineData(dataSet)
chartView.invalidate()

class MyMarker(context: Context) : MarkerView(context, R.layout.my_marker) {

    override fun refreshContent(entry: Entry, highlight: Highlight) {
        super.refreshContent(entry, highlight)
        dateView.text = formatDate(entry.x.toLong())
        valueView.text = formatCurrency(entry.y.toLong())
    }
}
chartView.marker = MyMarker(context)
            */


        binding = FragmentProfileBinding.inflate(inflater)//inflater.inflate(R.layout.fragment_profile, container, false)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = elevViewModel

       /* elevViewModel.response.observe(viewLifecycleOwner)
        {
            response ->
            if(response != null)
            {
                processChartData(response)
            }
        }*/
        elevViewModel.getElevationData(mFeature)
        return binding.root
    }

    fun processChartData(chartData: ElevationResponse)
    {
      val _paths = chartData.results[0].value.features[0].geometry.paths
      val entries = mutableListOf<Entry>()
        for (i in 0.._paths.size)
        {
           for(j in 0.._paths[i].size)
           {
               val xyzm = _paths[i][j];
               val dist = ((xyzm.m) * 0.000621371) //metersToMiles
               val elev = xyzm.y * 3.28084
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
        val dataSet = LineDataSet(entries, "Unused label")
        binding.chartView.data = LineData(dataSet)
        binding.chartView.invalidate()

    }



}