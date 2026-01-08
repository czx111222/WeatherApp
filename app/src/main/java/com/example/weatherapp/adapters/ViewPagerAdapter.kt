package com.example.weatherapp.adapters

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapp.CityManageFragment
import com.example.weatherapp.SettingsFragment
import com.example.weatherapp.WeatherDetailFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        Log.d("ViewPagerAdapter", "创建Fragment位置: $position")
        return when (position) {
            0 -> {
                Log.d("ViewPagerAdapter", "创建WeatherDetailFragment")
                WeatherDetailFragment()
            }
            1 -> {
                Log.d("ViewPagerAdapter", "创建CityManageFragment")
                CityManageFragment()
            }
            2 -> {
                Log.d("ViewPagerAdapter", "创建SettingsFragment")
                SettingsFragment()
            }
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}