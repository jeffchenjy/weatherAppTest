package com.example.myapihelper

object Model {

    data class WeatherResponse(
        val success: String, // "true" or "false" is a string in your JSON
        val result: Result,
        val records: Records
    )
    data class Result(
        val resource_id: String,
        val fields: List<Field>
    )
    data class Field(
        val id: String,
        val type: String
    )
    data class Records(
        val datasetDescription: String,
        val location: List<Location>
    )
    data class Location(
        val locationName: String,
        val weatherElement: List<WeatherElement>
    )

    data class WeatherElement(
        val elementName: String,
        val time: List<Time>
    )

    data class Time(
        val startTime: String,
        val endTime: String,
        val parameter: Parameter
    )

    data class Parameter(
        val parameterName: String,
        val parameterValue: String?,
        val parameterUnit: String?
    )
}