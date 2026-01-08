package com.example.weatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.City
import com.example.weatherapp.data.CityRepository
import com.example.weatherapp.data.TemperatureUnit
import com.example.weatherapp.data.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CityRepository()

    val cities: LiveData<List<City>> = repository.cities
    val selectedCity: LiveData<City?> = repository.selectedCity

    // 添加一个状态来通知UI刷新完成
    private val _refreshComplete = MutableLiveData<Boolean>()
    val refreshComplete: LiveData<Boolean> = _refreshComplete

    // 温度单位使用 StateFlow 确保一致性
    private val _temperatureUnit = MutableStateFlow(TemperatureUnit.CELSIUS)

    // 为 Fragment 提供 LiveData 版本
    val temperatureUnit: LiveData<TemperatureUnit> = _temperatureUnit.asLiveData()

    // 获取当前温度单位值（用于直接访问）
    fun getCurrentTemperatureUnit(): TemperatureUnit {
        return _temperatureUnit.value
    }

    fun getCurrentWeatherData(): WeatherData? {
        return repository.getCurrentWeatherData()
    }

    fun getWeatherData(cityId: Int): WeatherData? {
        return repository.getWeatherData(cityId)
    }

    // 添加刷新方法
    fun refreshCurrentWeather() {
        viewModelScope.launch {
            try {
                repository.refreshCurrentWeatherData()
                // 通知UI刷新完成
                _refreshComplete.postValue(true)
            } catch (e: Exception) {
                _refreshComplete.postValue(false)
            }
        }
    }

    // 刷新指定城市的天气
    fun refreshWeatherForCity(cityId: Int) {
        println("DEBUG: 开始刷新城市 $cityId 的天气")
        viewModelScope.launch {
            try {
                repository.refreshWeatherData(cityId)

                // 获取刷新后的数据
                val weatherData = repository.getWeatherData(cityId)
                println("DEBUG: 刷新完成，新天气状况: ${weatherData?.weatherCondition ?: "未知"}")

                _refreshComplete.postValue(true)
            } catch (e: Exception) {
                println("DEBUG: 刷新失败: ${e.message}")
                _refreshComplete.postValue(false)
            }
        }
    }

    fun addCity(city: City) {
        viewModelScope.launch {
            repository.addCity(city)
        }
    }

    fun selectCity(cityId: Int) {
        viewModelScope.launch {
            repository.selectCity(cityId)
        }
    }

    fun removeCity(cityId: Int) {
        viewModelScope.launch {
            repository.removeCity(cityId)
        }
    }

    fun toggleTemperatureUnit() {
        viewModelScope.launch {
            _temperatureUnit.value = when (_temperatureUnit.value) {
                TemperatureUnit.CELSIUS -> TemperatureUnit.FAHRENHEIT
                TemperatureUnit.FAHRENHEIT -> TemperatureUnit.CELSIUS
            }
        }
    }

    fun setTemperatureUnit(unit: TemperatureUnit) {
        viewModelScope.launch {
            _temperatureUnit.value = unit
        }
    }

    fun convertTemperature(celsius: Double, unit: TemperatureUnit): Double {
        return when (unit) {
            TemperatureUnit.CELSIUS -> celsius
            TemperatureUnit.FAHRENHEIT -> celsius * 9/5 + 32
        }
    }

    fun formatTemperature(temp: Double): String {
        return String.format("%.1f", temp)
    }
}