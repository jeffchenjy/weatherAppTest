package com.example.fragmentmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecyclerviewhelper.CityRecyclerViewAdapter
import com.example.myrecyclerviewhelper.ItemClickSupport
import com.example.myrecyclerviewhelper.onItemClick
import com.example.weatherapp.FragmentShift
import com.example.weatherapp.R

class SelectCityFragment: Fragment() {
    private lateinit var toolbar: Toolbar
    private lateinit var cityRecyclerView: RecyclerView
    private lateinit var myCityRecyclerViewAdapter: CityRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findView(view)
        setUpToolbar()
        init()
    }
    private fun findView(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        cityRecyclerView = view.findViewById(R.id.cityRecyclerView)
    }
    private fun setUpToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.citySelect)
        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_navigation_back)
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setNavigationOnClickListener {
            FragmentShift.getInstance().returnBackStackFragment(
                requireActivity(),
                FragmentShift.SELECT_CITY_FRAGMENT_SHIFT_TAG
            )
        }
    }
    private fun init() {
        val locationNameList: MutableList<Pair<String, String>> = mutableListOf()
        val locationName = listOf<Int>(
            R.string.taipeiCity,
            R.string.newTaipeiCity,
            R.string.taoyuanCity,
            R.string.taichungCity,
            R.string.kaohsiungCity,
            R.string.yilanCounty,
            R.string.hsinchuCounty,
            R.string.hsinchuCity,
            R.string.miaoliCounty,
            R.string.changhuaCounty,
            R.string.nantouCounty,
            R.string.yunlinCounty,
            R.string.chiayiCounty,
            R.string.chiayiCity,
            R.string.pingtungCounty,
            R.string.taitungCounty,
            R.string.hualienCounty,
            R.string.penghuCounty,
            R.string.keelungCity,
            R.string.lienchiangCounty,
            R.string.kinmenCounty,
        )
        locationName.forEach{element ->
            var locationNamePair: Pair<String, String> = Pair(element.toString(), requireContext().getString(element))
            locationNameList.add(locationNamePair)
        }

        cityRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        myCityRecyclerViewAdapter = CityRecyclerViewAdapter(null, requireContext(), null, locationNameList)
        cityRecyclerView.adapter = myCityRecyclerViewAdapter
        ItemClickSupport.addTo(cityRecyclerView)
        cityRecyclerView.onItemClick { recycler, position, v ->
            val locationName = (recycler.adapter as CityRecyclerViewAdapter).getItem(position)
            returnDataToHomeFragment(locationName.first)
        }
    }
    fun returnDataToHomeFragment(locationName: String) {
        val bundle = Bundle().apply {
            putString("locationName", locationName)
        }
        parentFragmentManager.setFragmentResult("locationName", bundle)
        parentFragmentManager.popBackStack()
    }
}