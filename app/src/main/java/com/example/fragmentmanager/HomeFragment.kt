package com.example.fragmentmanager

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.weatherapp.FragmentShift
import com.example.weatherapp.FragmentShift.Companion.LOCALE_FRAGMENT_SHIFT_TAG
import com.example.weatherapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.example.myweatherdbhelper.WeatherDao
import com.example.myweatherdbhelper.WeatherData
import com.example.weatherapp.FragmentShift.Companion.SELECT_CITY_FRAGMENT_SHIFT_TAG
import com.example.weatherapp.LocaleHelper
import java.text.SimpleDateFormat

import java.util.Date
import java.util.Locale

class HomeFragment: Fragment() {
    /* toolbar */
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    /* TextView */
    private lateinit var locationTitle: TextView
    private lateinit var timeTextView: TextView
    private lateinit var tempTextView: TextView
    private lateinit var weatherTextView: TextView
    private lateinit var softTextView: TextView
    private lateinit var rainTextView: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findView(view)
        setUpToolbar()
        getReturnData()
        setUI()
        setTextViewClickListener()
        setNavigationItemSelectedListener()
    }
    private fun findView(view: View) {
        toolbar = view.findViewById(R.id.home_toolbar)
        drawerLayout = view.findViewById(R.id.drawerLayout)
        navigationView = view.findViewById(R.id.navigationView)
        //
        locationTitle = view.findViewById(R.id.locationTitle)

        timeTextView = view.findViewById(R.id.timeTextView)
        tempTextView = view.findViewById(R.id.tempTextView)

        weatherTextView = view.findViewById(R.id.weatherTextView)
        softTextView = view.findViewById(R.id.softTextView)
        rainTextView = view.findViewById(R.id.rainTextView)
    }
    private fun setUpToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        //(requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.mainTitle)
        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_menu)
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setNavigationOnClickListener {
            drawerLayout.let {
                if (it.isDrawerOpen(GravityCompat.START)) {
                    it.closeDrawer(GravityCompat.START)
                } else {
                    it.openDrawer(GravityCompat.START)
                }
            }
        }
    }

    private fun getReturnData() {
        locationTitle.text = requireContext().getString(getCurrentLocationName()!!.toInt())
        parentFragmentManager.setFragmentResultListener("locationName", this) { requestKey, bundle ->
            val result = bundle.getString("locationName")
            // 在這裡處理接收到的資料
            if (result != null) {
                saveLocationNamePreference(result)
                locationTitle.text = requireContext().getString(result.toInt())
                setUI()
            }
        }
    }
    private fun saveLocationNamePreference(locationName: String?) {
        val prefs = requireContext().getSharedPreferences("locationName", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("locationName", locationName)
        editor.apply()
    }
    fun getCurrentLocationName(): String? {
        val prefs = requireContext().getSharedPreferences("locationName", Context.MODE_PRIVATE)
        return prefs.getString("locationName", R.string.taipeiCity.toString())
    }

    private fun setUI() {
        val locationNameForComparison = if (cityNameMap.containsKey(requireContext().getString(getCurrentLocationName()!!.toInt()))) {
            cityNameMap[locationTitle.text.toString()]
        } else {
            locationTitle.text.toString()
        }
        var latestWx: WeatherData? = null
        var latestPoP: WeatherData? = null
        var latestCI: WeatherData? = null
        var latestMaxT: WeatherData? = null
        var latestMinT: WeatherData? = null
        var latestTime: Date? = null
        val weatherDataList = WeatherDao.getInstance(requireContext()).readWeatherDataFromDb()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        weatherDataList.forEach { weatherData ->
            if (locationNameForComparison == weatherData.locationName) {
                val startTime = sdf.parse(weatherData.startTime)
                if (latestTime == null || startTime.after(latestTime)) {
                    latestTime = startTime
                }
                when (weatherData.elementName) {
                    "Wx" -> if (latestWx == null || startTime.after(sdf.parse(latestWx!!.startTime))) {
                        latestWx = weatherData
                    }
                    "PoP" -> if (latestPoP == null || startTime.after(sdf.parse(latestPoP!!.startTime))) {
                        latestPoP = weatherData
                    }
                    "CI" -> if (latestCI == null || startTime.after(sdf.parse(latestCI!!.startTime))) {
                        latestCI = weatherData
                    }
                    "MaxT" -> if (latestMaxT == null || startTime.after(sdf.parse(latestMaxT!!.startTime))) {
                        latestMaxT = weatherData
                    }
                    "MinT" -> if (latestMinT == null || startTime.after(sdf.parse(latestMinT!!.startTime))) {
                        latestMinT = weatherData
                    }
                }
            }
        }
        latestTime?.let {
            val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            timeTextView.text = sdfDate.format(it)
        }
        latestWx?.let {
            weatherTextView.text = it.parameterName
        }
        latestPoP?.let {
            rainTextView.text = "${it.parameterName} %"
        }
        latestCI?.let {
            softTextView.text = it.parameterName
        }
        latestMaxT?.let {
            val maxT = it.parameterName
            latestMinT?.let {
                val minT = it.parameterName
                tempTextView.text = "$minT ~ $maxT ℃"
            }
        }
    }

    private fun setTextViewClickListener() {
        locationTitle.setOnClickListener {
            FragmentShift.getInstance().goToNextFragment(
                SelectCityFragment(),
                requireActivity(),
                SELECT_CITY_FRAGMENT_SHIFT_TAG,
                SELECT_CITY_FRAGMENT_SHIFT_TAG
            )
        }
    }
    private fun setNavigationItemSelectedListener() {
        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawer(GravityCompat.START)
            when(item.itemId) {
                R.id.drawHome -> {

                }
                R.id.drawLanguage -> {
                    FragmentShift.getInstance().goToNextFragment(
                        LocaleFragment(),
                        requireActivity(),
                        LOCALE_FRAGMENT_SHIFT_TAG,
                        LOCALE_FRAGMENT_SHIFT_TAG
                    )
                }
                R.id.drawAbout -> {
                    aboutDialog()
                }
                R.id.drawCopyright -> {
                    copyrightDialog()
                }
                R.id.drawExit -> {
                    exitAppDialog()
                }
            }
            false
        }
    }
    private fun aboutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.ic_about)
            .setTitle(resources.getString(R.string.aboutTitle))
            .setMessage(resources.getString(R.string.aboutApp))
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                // Respond to positive button press
                dialog.dismiss()
            }
            .show()
    }
    private fun copyrightDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.ic_copyright)
            .setTitle(resources.getString(R.string.copyrightTitle))
            .setMessage(resources.getString(R.string.copyright))
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                // Respond to positive button press
                dialog.dismiss()
            }
            .show()
    }
    private fun exitAppDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.ic_exit)
            .setTitle(resources.getString(R.string.escTitle))
            .setMessage(resources.getString(R.string.esc))
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                // Respond to positive button press
                val activityManager = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val tasks = activityManager.appTasks
                for (task in tasks) {
                    task.finishAndRemoveTask()
                }
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private val cityNameMap = mapOf(
        "Taipei City" to "臺北市",
        "New Taipei City" to "新北市",
        "Taoyuan City" to "桃園市",
        "Taichung City" to  "臺中市",
        "Kaohsiung City" to "高雄市",
        "Yilan County" to  "宜蘭縣",
        "Hsinchu County" to "新竹縣",
        "Hsinchu City" to "新竹市",
        "Miaoli County" to "苗栗縣",
        "Changhua County" to "彰化縣",
        "Nantou County" to "南投縣",
        "Yunlin County" to "雲林縣",
        "Chiayi County" to "嘉義縣",
        "Chiayi City" to "嘉義市",
        "Pingtung County" to "屏東縣",
        "Taitung County" to "臺東縣",
        "Hualien County" to "花蓮縣",
        "Penghu County" to "澎湖縣",
        "Keelung City" to "基隆市",
        "Lienchiang County" to "連江縣",
        "Kinmen County" to "金門縣"
    )
}