package com.example.myweatherdbhelper

object DbConstants {
    const val DATABASE_NAME = "weather.db"
    const val DATABASE_VERSION = 1

    const val TABLE_WEATHER = "weather"
    const val COLUMN_ID = "id"
    const val COLUMN_LOCATION_NAME = "locationName"
    const val COLUMN_START_TIME = "startTime"
    const val COLUMN_END_TIME = "endTime"
    const val COLUMN_ELEMENT_NAME = "elementName"
    const val COLUMN_PARAMETER_NAME = "parameterName"
    const val COLUMN_PARAMETER_VALUE = "parameterValue"
    const val COLUMN_PARAMETER_UNIT = "parameterUnit"

    const val CREATE_TABLE_WEATHER = """
    CREATE TABLE $TABLE_WEATHER (
        $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_LOCATION_NAME TEXT,
        $COLUMN_START_TIME TEXT,
        $COLUMN_END_TIME TEXT,
        $COLUMN_ELEMENT_NAME TEXT,
        $COLUMN_PARAMETER_NAME TEXT,
        $COLUMN_PARAMETER_VALUE TEXT,
        $COLUMN_PARAMETER_UNIT TEXT
    )
    """

}