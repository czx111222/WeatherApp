package com.example.weatherapp.data

data class WeatherData(
    val cityId: Int,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val weatherCondition: String,
    val weatherIcon: String,
    val pressure: Int,
    val visibility: Int,
    val lastUpdated: Long,
    val unit: TemperatureUnit = TemperatureUnit.CELSIUS
)

enum class TemperatureUnit {
    CELSIUS, FAHRENHEIT
}