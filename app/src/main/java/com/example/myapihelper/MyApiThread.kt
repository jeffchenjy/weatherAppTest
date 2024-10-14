package com.example.myapihelper

import android.content.Context

class MyApiThread(private val context: Context): Thread() {
    override fun run() {
        MyApiGetter.getApiInstance(context).getWeatherData()
        MyApiGetter.getApiInstance(context).printWeatherData()
    }
}