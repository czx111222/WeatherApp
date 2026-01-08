package com.example.weatherapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.data.City
import com.example.weatherapp.databinding.DialogAddCityBinding
import com.example.weatherapp.viewmodel.WeatherViewModel

class AddCityDialog : DialogFragment() {

    private lateinit var binding: DialogAddCityBinding
    private val viewModel: WeatherViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddCityBinding.inflate(requireActivity().layoutInflater)

        // 设置热门城市按钮
        setupPopularCities()

        return AlertDialog.Builder(requireContext())
            .setTitle("添加城市")
            .setView(binding.root)
            .setPositiveButton("添加") { _, _ ->
                val cityName = binding.cityNameInput.text.toString().trim()
                if (cityName.isNotEmpty()) {
                    addNewCity(cityName)
                } else {
                    Toast.makeText(requireContext(), "请输入城市名称", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .create()
    }

    private fun setupPopularCities() {
        val popularCities = listOf("北京市", "上海市", "广州市", "深圳市", "成都市", "杭州市")

        // 清除现有按钮
        binding.popularCitiesContainer.removeAllViews()

        // 动态添加热门城市按钮
        popularCities.forEach { cityName ->
            androidx.appcompat.widget.AppCompatButton(requireContext()).apply {
                text = cityName
                layoutParams = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 8.dpToPx()
                    bottomMargin = 8.dpToPx()
                }
                setBackgroundColor(resources.getColor(android.R.color.transparent, null))

                // 修改这里：使用 primary_color 代替 primary_blue
                setTextColor(resources.getColor(com.example.weatherapp.R.color.primary_color, null))

                setOnClickListener {
                    binding.cityNameInput.setText(cityName)
                }
            }.also { button ->
                binding.popularCitiesContainer.addView(button)
            }
        }
    }

    private fun addNewCity(cityName: String) {
        // 检查是否已存在
        val existingCity = viewModel.cities.value?.find { it.name == cityName }
        if (existingCity != null) {
            Toast.makeText(requireContext(), "城市已存在", Toast.LENGTH_SHORT).show()
            return
        }

        // 生成模拟的城市ID和数据
        val newCityId = (100000000..999999999).random()
        val newCity = City(
            id = newCityId,
            name = cityName,
            latitude = 30.0 + (Math.random() * 10 - 5),
            longitude = 120.0 + (Math.random() * 10 - 5)
        )

        viewModel.addCity(newCity)
        viewModel.selectCity(newCityId)  // 自动选中新添加的城市

        Toast.makeText(requireContext(), "已添加城市: $cityName", Toast.LENGTH_SHORT).show()
        dismiss()
    }

    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }
}