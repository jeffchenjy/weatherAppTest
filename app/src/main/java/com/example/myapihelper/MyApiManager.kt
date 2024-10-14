package com.example.myapihelper

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MyApiManager {
    const val baseUrl = "https://opendata.cwa.gov.tw/"
    const val API_KEY = "CWA-B55F4A2D-DEAF-452E-AEFF-2613CCEEF746"

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .connectTimeout(5, TimeUnit.SECONDS)
        //使用 HttpLoggingInterceptor 來攔截和記錄 HTTP 請求與響應的訊息
        //攔截器會記錄基本的 HTTP 請求和響應的資訊，包括請求的方法（如 GET、POST）、URL 和響應的狀態碼。
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()
    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()
    val myApiService : MyApiService = retrofit.create(MyApiService::class.java) //將 KClass 轉換為 Class<T>
}