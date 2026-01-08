package com.example.weatherapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CityRepository {
    private val _cities = MutableLiveData<List<City>>()
    private val _selectedCity = MutableLiveData<City?>()
    private val _weatherData = mutableMapOf<Int, WeatherData>()

    init {
        // 初始化预设城市
        val initialCities = PresetCities.cities.mapIndexed { index, city ->
            city.copy(orderIndex = index)
        }
        _cities.value = initialCities

        // 设置默认选中的城市
        _selectedCity.value = initialCities.firstOrNull()

        // 初始化天气数据
        _weatherData.putAll(PresetCities.initialWeatherData)

        // 为其他城市生成模拟数据
        initialCities.forEach { city ->
            if (!_weatherData.containsKey(city.id)) {
                _weatherData[city.id] = generateMockWeatherData(city.id)
            }
        }
    }

    val cities: LiveData<List<City>> get() = _cities
    val selectedCity: LiveData<City?> get() = _selectedCity

    fun getWeatherData(cityId: Int): WeatherData? {
        return _weatherData[cityId]
    }

    fun getCurrentWeatherData(): WeatherData? {
        return _selectedCity.value?.let { city ->
            _weatherData[city.id]
        }
    }

    // 添加刷新天气数据的方法
    fun refreshWeatherData(cityId: Int): WeatherData? {
        val newWeatherData = generateMockWeatherData(cityId)
        _weatherData[cityId] = newWeatherData
        return newWeatherData
    }

    // 刷新当前选中城市的天气数据
    fun refreshCurrentWeatherData(): WeatherData? {
        return _selectedCity.value?.let { city ->
            refreshWeatherData(city.id)
        }
    }

    fun addCity(city: City) {
        val currentList = _cities.value?.toMutableList() ?: mutableListOf()
        if (currentList.none { it.id == city.id }) {
            val newCity = city.copy(orderIndex = currentList.size)
            currentList.add(newCity)
            _cities.value = currentList

            // 为新城市生成模拟天气数据
            val weatherData = generateMockWeatherData(city.id)
            _weatherData[city.id] = weatherData
        }
    }

    fun selectCity(cityId: Int) {
        val citiesList = _cities.value ?: return

        val updatedCities = citiesList.map { city ->
            city.copy(isSelected = city.id == cityId)
        }
        _cities.value = updatedCities

        val selected = updatedCities.firstOrNull { it.id == cityId }
        _selectedCity.value = selected
    }

    fun removeCity(cityId: Int) {
        val currentList = _cities.value?.toMutableList() ?: return
        currentList.removeAll { it.id == cityId }
        _cities.value = currentList
        _weatherData.remove(cityId)

        // 如果删除的是当前选中的城市，选择第一个城市
        if (_selectedCity.value?.id == cityId) {
            _selectedCity.value = currentList.firstOrNull()
        }
    }

    private fun generateMockWeatherData(cityId: Int): WeatherData {
        val temp = (15..35).random().toDouble()
        val conditions = listOf("晴", "多云", "小雨", "阴", "阵雨")
        val icons = listOf("☀️", "⛅", "🌧", "☁️", "🌦")
        val index = (0 until conditions.size).random()

        return WeatherData(
            cityId = cityId,
            temperature = temp,
            feelsLike = temp + (0..3).random(),
            humidity = (30..90).random(),
            windSpeed = (1..10).random().toDouble(),
            weatherCondition = conditions[index],
            weatherIcon = icons[index],
            pressure = 1000 + (0..30).random(),
            visibility = (5..20).random(),
            lastUpdated = System.currentTimeMillis()
        )
    }
}