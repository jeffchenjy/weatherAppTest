package com.example.myweatherdbhelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.myapihelper.Model
import com.example.myweatherdbhelper.DbConstants.COLUMN_ELEMENT_NAME
import com.example.myweatherdbhelper.DbConstants.COLUMN_END_TIME
import com.example.myweatherdbhelper.DbConstants.COLUMN_LOCATION_NAME
import com.example.myweatherdbhelper.DbConstants.COLUMN_PARAMETER_NAME
import com.example.myweatherdbhelper.DbConstants.COLUMN_PARAMETER_UNIT
import com.example.myweatherdbhelper.DbConstants.COLUMN_PARAMETER_VALUE
import com.example.myweatherdbhelper.DbConstants.COLUMN_START_TIME
import com.example.myweatherdbhelper.DbConstants.TABLE_WEATHER

class WeatherDao(private val dbHelper: WeatherDbHelper) {
    //單例模式
    companion object {
        @Volatile
        private var instance: WeatherDao? = null

        fun getInstance(context: Context): WeatherDao {
            return instance ?: synchronized(this) {
                instance ?: WeatherDao(WeatherDbHelper.getInstance(context)).also { instance = it }
            }
        }
    }
    fun deleteWeatherDataByLocation(locationName: String) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        db.delete(TABLE_WEATHER, "${COLUMN_LOCATION_NAME}=?", arrayOf(locationName))
        //arrayOf(locationName) 是一個包含單個元素的陣列，其中 locationName 是要用來匹配 COLUMN_LOCATION_NAME 欄位值的實際值。
    }

    fun insertData(location: Model.Location) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        // create empty set to save data
        location.weatherElement.forEach{element ->
            element.time.forEach{time ->
                val contentValues = ContentValues().apply {
                    put(COLUMN_LOCATION_NAME, location.locationName)
                    put(COLUMN_START_TIME, time.startTime)
                    put(COLUMN_END_TIME, time.endTime)
                    put(COLUMN_ELEMENT_NAME, element.elementName)
                    put(COLUMN_PARAMETER_NAME, time.parameter.parameterName)
                    put(COLUMN_PARAMETER_VALUE, time.parameter.parameterValue)
                    put(COLUMN_PARAMETER_UNIT, time.parameter.parameterUnit)
                }
                //將創建好的資料集合1筆1筆存入資料庫中
                db.insert(TABLE_WEATHER, null, contentValues)
            }
        }
    }

    fun readWeatherDataFromDb(): List<WeatherData> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val weatherDataList = mutableListOf<WeatherData>()
        val cursor = db.query(
            TABLE_WEATHER,
            null,
            null,
            null,
            null,
            null,
            null,
        )
        while(cursor.moveToNext()) {
            val locationName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION_NAME))
            val startTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME))
            val endTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_TIME))
            val elementName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ELEMENT_NAME))
            val parameterName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PARAMETER_NAME))
            val parameterValue = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PARAMETER_VALUE))
            val parameterUnit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PARAMETER_UNIT))
            //將取出的資料整理成WeatherData類型
            val weatherData = WeatherData(
                locationName = locationName,
                startTime = startTime,
                endTime = endTime,
                elementName = elementName,
                parameterName = parameterName,
                parameterValue = parameterValue,
                parameterUnit = parameterUnit,
            )
            //1筆1筆存入MAP(weatherDataList)中
            weatherDataList.add(weatherData)
        }

        cursor.close()
        return weatherDataList
    }
}
data class WeatherData(
    val locationName: String,
    val startTime: String,
    val endTime: String,
    val elementName: String,
    val parameterName: String,
    val parameterValue: String?,
    val parameterUnit: String?,
)