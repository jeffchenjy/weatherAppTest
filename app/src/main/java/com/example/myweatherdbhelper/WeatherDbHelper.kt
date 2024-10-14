package com.example.myweatherdbhelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myweatherdbhelper.DbConstants.CREATE_TABLE_WEATHER
import com.example.myweatherdbhelper.DbConstants.DATABASE_NAME
import com.example.myweatherdbhelper.DbConstants.DATABASE_VERSION
import com.example.myweatherdbhelper.DbConstants.TABLE_WEATHER

class WeatherDbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        @Volatile
        private var instance: WeatherDbHelper? = null

        fun getInstance(context: Context): WeatherDbHelper {
            return instance ?: synchronized(this) {
                instance ?: WeatherDbHelper(context.applicationContext).also { instance = it }
            }
        }
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_WEATHER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS ${TABLE_WEATHER}")
            onCreate(db)
        }
    }


}