package com.swetha.aqisample.model

data class AQIResponse(
    val status: String,
    val data: AQIData?,
    val error: String? = null
)

data class AQIData(
    val city: City,
    val aqi: Int,
    val dominentpol: String,
    val iaqi: IAQI?
)

data class City(
    val name: String,
    val geo: List<Double>
)

data class IAQI(
    val pm25: IAQIValue?,
    val t: IAQIValue?,
    val h: IAQIValue?,
    val w: IAQIValue?
)

data class IAQIValue(
    val v: Double?
)