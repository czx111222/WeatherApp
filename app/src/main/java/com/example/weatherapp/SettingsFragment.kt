package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.data.TemperatureUnit
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.viewmodel.WeatherViewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()

        // 初始化设置
        initializeTemperatureSetting()
    }

    private fun initializeTemperatureSetting() {
        // 获取当前温度单位并设置单选按钮
        val currentUnit = viewModel.getCurrentTemperatureUnit()
        when (currentUnit) {
            TemperatureUnit.CELSIUS -> {
                binding.celsiusRadio.isChecked = true
                binding.fahrenheitRadio.isChecked = false
            }
            TemperatureUnit.FAHRENHEIT -> {
                binding.celsiusRadio.isChecked = false
                binding.fahrenheitRadio.isChecked = true
            }
        }
    }

    private fun setupObservers() {
        // 观察温度单位变化
        viewModel.temperatureUnit.observe(viewLifecycleOwner) { unit ->
            when (unit) {
                TemperatureUnit.CELSIUS -> {
                    binding.celsiusRadio.isChecked = true
                    binding.fahrenheitRadio.isChecked = false
                }
                TemperatureUnit.FAHRENHEIT -> {
                    binding.celsiusRadio.isChecked = false
                    binding.fahrenheitRadio.isChecked = true
                }
            }
        }
    }

    private fun setupListeners() {
        // Celsius 单选按钮
        binding.celsiusRadio.setOnClickListener {
            viewModel.setTemperatureUnit(TemperatureUnit.CELSIUS)
        }

        // Fahrenheit 单选按钮
        binding.fahrenheitRadio.setOnClickListener {
            viewModel.setTemperatureUnit(TemperatureUnit.FAHRENHEIT)
        }

        // RadioGroup 监听器（备用方案）
        binding.temperatureGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.celsius_radio -> {
                    viewModel.setTemperatureUnit(TemperatureUnit.CELSIUS)
                }
                R.id.fahrenheit_radio -> {
                    viewModel.setTemperatureUnit(TemperatureUnit.FAHRENHEIT)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}