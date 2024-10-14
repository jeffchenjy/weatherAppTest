package com.example.myapihelper

import android.content.Context
import android.util.Log
import com.example.myapihelper.MyApiManager.API_KEY
import com.example.myweatherdbhelper.WeatherDao.Companion.getInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyApiGetter(private val context: Context) {
    companion object {
        @Volatile
        private var instance: MyApiGetter? = null

        fun getApiInstance(context: Context): MyApiGetter {
            return instance ?: synchronized(this) {
                instance ?: MyApiGetter(context.applicationContext).also { instance = it }
            }
        }
    }
    fun getWeatherData() {
        val call = MyApiManager.myApiService.getWeatherData(API_KEY)
        call.enqueue(object : Callback<Model.WeatherResponse> {
            override fun onResponse(
                call: Call<Model.WeatherResponse>,
                response: Response<Model.WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.records?.location?.forEach{location ->
                        getInstance(context).deleteWeatherDataByLocation(location.locationName)
                        getInstance(context).insertData(location)
                    }
                    Log.d("WeatherResponse", "Weather data saved to database")
                } else {
                    Log.e("WeatherResponse", "Request failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Model.WeatherResponse>, t: Throwable) {
                Log.e("WeatherResponse", "Request error", t)
            }
        })
    }
    fun printWeatherData() {
        val weatherDataList = getInstance(context).readWeatherDataFromDb()
        weatherDataList.forEach { weatherData ->
            Log.d("WeatherData", weatherData.toString())
        }
    }
}