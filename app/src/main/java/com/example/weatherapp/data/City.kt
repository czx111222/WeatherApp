package com.example.weatherapp.data

data class City(
    val id: Int,
    val name: String,
    val countryCode: String = "CN",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isFavorite: Boolean = true,
    val orderIndex: Int = 0,
    val isSelected: Boolean = false
)

// 预设城市数据
object PresetCities {
    val cities = listOf(
        City(101010100, "北京市", "CN", 39.9042, 116.4074, true, 0, true),
        City(101020100, "上海市", "CN", 31.2304, 121.4737),
        City(101280101, "广州市", "CN", 23.1291, 113.2644),
        City(101280601, "深圳市", "CN", 22.5431, 114.0579),
        City(101210101, "杭州市", "CN", 30.2741, 120.1551),
        City(101270101, "成都市", "CN", 30.5728, 104.0668),
        City(101190101, "南京市", "CN", 32.0603, 118.7969),
        City(101200101, "武汉市", "CN", 30.5928, 114.3052)
    )

    val initialWeatherData = mapOf(
        101010100 to WeatherData(
            cityId = 101010100,
            temperature = 25.0,
            feelsLike = 26.0,
            humidity = 45,
            windSpeed = 3.0,
            weatherCondition = "晴",
            weatherIcon = "☀️",
            pressure = 1013,
            visibility = 10,
            lastUpdated = System.currentTimeMillis()
        ),
        101020100 to WeatherData(
            cityId = 101020100,
            temperature = 22.0,
            feelsLike = 23.0,
            humidity = 85,
            windSpeed = 5.0,
            weatherCondition = "小雨",
            weatherIcon = "🌧",
            pressure = 1012,
            visibility = 8,
            lastUpdated = System.currentTimeMillis()
        ),
        101280101 to WeatherData(
            cityId = 101280101,
            temperature = 30.0,
            feelsLike = 31.0,
            humidity = 60,
            windSpeed = 2.0,
            weatherCondition = "晴",
            weatherIcon = "☀️",
            pressure = 1011,
            visibility = 12,
            lastUpdated = System.currentTimeMillis()
        )
    )
}