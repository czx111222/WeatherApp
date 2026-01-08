package com.example.weatherapp.utils

import com.example.weatherapp.R
import java.util.Locale

object WeatherAnimationMapper {

    fun getAnimationResource(weatherCondition: String): Int {
        val lowerCondition = weatherCondition.lowercase(Locale.getDefault())
        println("DEBUG AnimationMapper: 获取动画资源，天气状况: '$weatherCondition'，小写后: '$lowerCondition'")

        // 支持中英文混合匹配
        val result = when {
            // 晴天/晴朗
            lowerCondition.contains("sunny") ||
                    lowerCondition.contains("clear") ||
                    lowerCondition.contains("晴") ||
                    lowerCondition.contains("晴朗") -> {
                println("DEBUG AnimationMapper: 匹配到 晴天/晴/sunny/clear")
                R.raw.weather_sunny
            }

            // 多云/阴天
            lowerCondition.contains("cloud") ||
                    lowerCondition.contains("阴") ||
                    lowerCondition.contains("多云") ||
                    lowerCondition.contains("阴天") -> {
                println("DEBUG AnimationMapper: 匹配到 多云/阴/cloudy")
                R.raw.weather_cloudy
            }

            // 雨/雨天
            lowerCondition.contains("rain") ||
                    lowerCondition.contains("雨") ||
                    lowerCondition.contains("下雨") ||
                    lowerCondition.contains("小雨") ||
                    lowerCondition.contains("中雨") ||
                    lowerCondition.contains("大雨") ||
                    lowerCondition.contains("暴雨") -> {
                println("DEBUG AnimationMapper: 匹配到 雨/rain")
                R.raw.weather_rain
            }

            // 雪/雪天
            lowerCondition.contains("snow") ||
                    lowerCondition.contains("雪") ||
                    lowerCondition.contains("下雪") ||
                    lowerCondition.contains("小雪") ||
                    lowerCondition.contains("中雪") ||
                    lowerCondition.contains("大雪") ||
                    lowerCondition.contains("暴雪") -> {
                println("DEBUG AnimationMapper: 匹配到 雪/snow")
                R.raw.weather_snow
            }

            // 雷雨/雷暴/风暴
            lowerCondition.contains("thunder") ||
                    lowerCondition.contains("storm") ||
                    lowerCondition.contains("雷") ||
                    lowerCondition.contains("雷雨") ||
                    lowerCondition.contains("雷暴") ||
                    lowerCondition.contains("风暴") -> {
                println("DEBUG AnimationMapper: 匹配到 雷雨/thunderstorm")
                R.raw.weather_thunderstorm
            }

            // 雾/雾天/霾
            lowerCondition.contains("fog") ||
                    lowerCondition.contains("mist") ||
                    lowerCondition.contains("haze") ||
                    lowerCondition.contains("雾") ||
                    lowerCondition.contains("大雾") ||
                    lowerCondition.contains("雾霾") ||
                    lowerCondition.contains("霾") -> {
                println("DEBUG AnimationMapper: 匹配到 雾/fog")
                R.raw.weather_fog
            }

            // 其他天气状况
            lowerCondition.contains("风") || lowerCondition.contains("wind") -> {
                println("DEBUG AnimationMapper: 匹配到 风/windy，使用多云动画")
                R.raw.weather_cloudy
            }

            lowerCondition.contains("沙") || lowerCondition.contains("dust") -> {
                println("DEBUG AnimationMapper: 匹配到 沙尘/dust，使用雾动画")
                R.raw.weather_fog
            }

            else -> {
                println("DEBUG AnimationMapper: 未匹配到任何条件，使用默认 sunny")
                R.raw.weather_sunny
            }
        }

        println("DEBUG AnimationMapper: 返回资源ID: $result")
        return result
    }

    fun getAnimationSpeed(weatherCondition: String): Float {
        val lowerCondition = weatherCondition.lowercase(Locale.getDefault())

        return when {
            // 暴雨、暴雪、大风等天气动画更快
            lowerCondition.contains("暴雨") ||
                    lowerCondition.contains("暴雪") ||
                    lowerCondition.contains("狂风") ||
                    lowerCondition.contains("heavy") -> 2.0f

            // 雨、雪、雷雨等中等速度
            lowerCondition.contains("雨") ||
                    lowerCondition.contains("雪") ||
                    lowerCondition.contains("雷") ||
                    lowerCondition.contains("rain") ||
                    lowerCondition.contains("snow") ||
                    lowerCondition.contains("thunder") -> 1.5f

            // 风大时动画稍快
            lowerCondition.contains("风") ||
                    lowerCondition.contains("wind") -> 1.8f

            // 晴天、阴天等正常速度
            else -> 1.0f
        }
    }



}