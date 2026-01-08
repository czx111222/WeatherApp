package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.adapters.ViewPagerAdapter
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.viewmodel.WeatherViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 使用自定义工厂创建ViewModel
    val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "=== onCreate ===")

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            Log.d("MainActivity", "布局加载成功")

            setupViewPager()
            Log.d("MainActivity", "ViewPager设置完成")

        } catch (e: Exception) {
            Log.e("MainActivity", "初始化失败: ${e.message}", e)
            // 显示错误信息
            val errorView = android.widget.TextView(this).apply {
                text = "初始化失败: ${e.message}"
                textSize = 14f
                setPadding(20, 20, 20, 20)
            }
            setContentView(errorView)
        }
    }

    private fun setupViewPager() {
        try {
            val adapter = ViewPagerAdapter(this)
            binding.viewPager.adapter = adapter

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "天气"
                    1 -> tab.text = "城市"
                    2 -> tab.text = "设置"
                }
            }.attach()

            Log.d("MainActivity", "TabLayout设置完成")
        } catch (e: Exception) {
            Log.e("MainActivity", "设置ViewPager失败: ${e.message}", e)
            throw e
        }
    }

    fun switchToWeatherTab() {
        binding.viewPager.currentItem = 0
    }
}