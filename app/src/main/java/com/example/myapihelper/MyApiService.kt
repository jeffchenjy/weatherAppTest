package com.example.myapihelper

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MyApiService {
    @GET("api/v1/rest/datastore/F-C0032-001")
    fun getWeatherData(
        @Query("Authorization") authorization: String,
        @Query("format") format: String = "JSON"
    ): Call<Model.WeatherResponse>
}