package com.example.weatherapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.weatherapp.data.TemperatureUnit
import com.example.weatherapp.databinding.FragmentWeatherDetailBinding
import com.example.weatherapp.utils.WeatherAnimationMapper
import com.example.weatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class WeatherDetailFragment : Fragment() {

    private var _binding: FragmentWeatherDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by activityViewModels()
    private var currentCityId: Int? = null
    private var currentWeatherCondition: String = ""
    private var currentAnimationResId: Int? = null
    private var isRefreshing: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAnimationView()
        setupSwipeRefresh()
        setupObservers()
        setupListeners()
    }

    private fun setupAnimationView() {
        // 配置Lottie动画视图
        val animationView = binding.weatherAnimation as LottieAnimationView
        animationView.repeatCount = LottieDrawable.INFINITE
    }

    private fun setupSwipeRefresh() {
        // 设置下拉刷新颜色
        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.primary_color,
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light
        )

        // 设置进度条背景色
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.card_background)
    }

    private fun setupObservers() {
        // 观察选中的城市
        viewModel.selectedCity.observe(viewLifecycleOwner) { city ->
            city?.let {
                currentCityId = it.id
                println("DEBUG WeatherDetailFragment: 选中城市变化: ${it.name} (ID: ${it.id})")
                updateCityInfo(it)
                updateWeatherData(it.id)

                // 添加UI动画
                animateUI()
            }
        }

        // 观察温度单位
        viewModel.temperatureUnit.observe(viewLifecycleOwner) { unit ->
            updateTemperatureDisplay(unit)
        }

        // 观察刷新完成状态
        viewModel.refreshComplete.observe(viewLifecycleOwner) { success ->
            println("DEBUG WeatherDetailFragment: 刷新完成，成功: $success")
            if (success) {
                // 刷新成功，更新UI
                currentCityId?.let { cityId ->
                    println("DEBUG WeatherDetailFragment: 开始更新UI，城市ID: $cityId")
                    updateWeatherData(cityId)
                } ?: run {
                    println("DEBUG WeatherDetailFragment: 当前城市ID为空")
                }

                // 停止下拉刷新动画
                binding.swipeRefreshLayout.isRefreshing = false

                Toast.makeText(requireContext(), "天气已刷新", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "刷新失败", Toast.LENGTH_SHORT).show()

                // 停止下拉刷新动画
                binding.swipeRefreshLayout.isRefreshing = false
            }

            // 重置刷新状态
            isRefreshing = false
        }
    }

    private fun setupListeners() {
        // 设置下拉刷新监听器
        binding.swipeRefreshLayout.setOnRefreshListener {
            println("DEBUG: 下拉刷新触发")
            refreshWeatherData()
        }

        // 刷新按钮点击效果
        binding.refreshButton.setOnClickListener {
            // 旋转动画
            val rotateAnim = ObjectAnimator.ofFloat(binding.refreshButton, "rotation", 0f, 360f).apply {
                duration = 500
                interpolator = AccelerateDecelerateInterpolator()
            }
            rotateAnim.start()

            refreshWeatherData()
        }

        // 添加动画点击监听，点击可以暂停/恢复动画
        binding.weatherAnimation.setOnClickListener {
            val animationView = binding.weatherAnimation as LottieAnimationView
            if (animationView.isAnimating) {
                animationView.pauseAnimation()
            } else {
                animationView.resumeAnimation()
            }
        }

        // 卡片点击效果（可选的增强交互）
        setupCardClickEffects()
    }

    private fun setupCardClickEffects() {
        // 主卡片点击效果
        val mainCard = binding.root.findViewById<CardView>(R.id.card_main)
        mainCard?.setOnClickListener {
            // 轻微缩放反馈
            mainCard.animate()
                .scaleX(0.98f)
                .scaleY(0.98f)
                .setDuration(100)
                .withEndAction {
                    mainCard.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
        }

        // 详情卡片点击效果
        val detailCard = binding.root.findViewById<CardView>(R.id.card_details)
        detailCard?.setOnClickListener {
            detailCard.animate()
                .scaleX(0.98f)
                .scaleY(0.98f)
                .setDuration(100)
                .withEndAction {
                    detailCard.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
        }

        // 空气质量卡片点击效果
        val airQualityCard = binding.root.findViewById<CardView>(R.id.card_air_quality)
        airQualityCard?.setOnClickListener {
            airQualityCard.animate()
                .scaleX(0.98f)
                .scaleY(0.98f)
                .setDuration(100)
                .withEndAction {
                    airQualityCard.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
        }
    }

    private fun updateCityInfo(city: com.example.weatherapp.data.City) {
        binding.cityName.text = city.name
    }

    private fun updateWeatherData(cityId: Int) {
        val weatherData = viewModel.getWeatherData(cityId)
        println("DEBUG WeatherDetailFragment: 获取天气数据，城市ID: $cityId")
        println("DEBUG WeatherDetailFragment: 天气数据: $weatherData")

        weatherData?.let {
            println("DEBUG WeatherDetailFragment: 天气状况: ${it.weatherCondition}")
            println("DEBUG WeatherDetailFragment: 温度: ${it.temperature}")

            currentWeatherCondition = it.weatherCondition

            // 更新天气动画
            updateWeatherAnimation(it.weatherCondition)

            // 更新天气状况文本
            binding.weatherCondition.text = it.weatherCondition

            // 更新其他天气信息
            binding.humidityValue.text = "${it.humidity}%"
            binding.windSpeedValue.text = "${it.windSpeed} m/s"
            binding.pressureValue.text = "${it.pressure} hPa"
            binding.visibilityValue.text = "${it.visibility} km"

            // 更新时间
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val updateTime = dateFormat.format(Date(it.lastUpdated))
            binding.updateTimeValue.text = "更新时间: $updateTime"

            // 更新温度显示
            val currentUnit = viewModel.getCurrentTemperatureUnit()
            updateTemperatureDisplay(currentUnit)

            // 更新动态主题
            setupDynamicTheme(it.weatherCondition)
        } ?: run {
            println("DEBUG WeatherDetailFragment: 未获取到天气数据")
        }
    }

    private fun updateWeatherAnimation(weatherCondition: String) {
        println("DEBUG WeatherDetailFragment: 开始更新动画，天气状况: '$weatherCondition'")

        try {
            // 获取动画资源
            val animationRes = WeatherAnimationMapper.getAnimationResource(weatherCondition)
            val animationSpeed = WeatherAnimationMapper.getAnimationSpeed(weatherCondition)

            println("DEBUG WeatherDetailFragment: 获取到的动画资源ID: $animationRes")

            // 设置动画
            val animationView = binding.weatherAnimation as LottieAnimationView

            // 检查资源是否存在
            try {
                // 如果动画资源相同，只调整速度
                if (animationRes == currentAnimationResId) {
                    animationView.speed = animationSpeed
                    println("DEBUG WeatherDetailFragment: 动画资源相同，只调整速度: $animationSpeed")
                } else {
                    // 设置新的动画
                    animationView.setAnimation(animationRes)
                    animationView.speed = animationSpeed
                    animationView.playAnimation()
                    currentAnimationResId = animationRes

                    println("DEBUG WeatherDetailFragment: 动画设置成功，资源ID: $animationRes")
                }
            } catch (e: Exception) {
                println("DEBUG WeatherDetailFragment: 设置动画失败: ${e.message}")
                // 尝试加载默认动画
                try {
                    animationView.setAnimation(R.raw.weather_sunny)
                    animationView.playAnimation()
                    currentAnimationResId = R.raw.weather_sunny
                    println("DEBUG WeatherDetailFragment: 使用默认动画")
                } catch (e2: Exception) {
                    println("DEBUG WeatherDetailFragment: 加载默认动画也失败: ${e2.message}")
                    binding.weatherAnimation.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("DEBUG WeatherDetailFragment: 更新动画异常: ${e.message}")
        }
    }

    private fun setupDynamicTheme(weatherCondition: String) {
        // 根据天气状况设置不同的温馨提示
        val weatherTips = when {
            weatherCondition.contains("暴雨") || weatherCondition.contains("暴雪") ->
                "⚠️ 恶劣天气，请注意安全，避免外出"
            weatherCondition.contains("雷") ->
                "⚡ 有雷雨，请关闭电器，注意防雷"
            weatherCondition.contains("雨") ->
                "☔ 今天有雨，出门请带伞"
            weatherCondition.contains("雪") ->
                "❄️ 路面可能结冰，出行请注意安全"
            weatherCondition.contains("雾") || weatherCondition.contains("霾") ->
                "🌫️ 能见度较低，出行请注意安全"
            weatherCondition.contains("晴") ->
                "☀️ 天气晴朗，适合户外活动"
            weatherCondition.contains("多云") || weatherCondition.contains("阴") ->
                "☁️ 天气舒适，适合外出"
            else -> "今天天气舒适，适合外出"
        }

        binding.weatherTips.text = weatherTips
    }

    private fun updateTemperatureDisplay(unit: TemperatureUnit) {
        currentCityId?.let { cityId ->
            val weatherData = viewModel.getWeatherData(cityId)
            weatherData?.let {
                val temp = viewModel.convertTemperature(it.temperature, unit)
                val feelsLike = viewModel.convertTemperature(it.feelsLike, unit)
                val unitSymbol = if (unit == TemperatureUnit.CELSIUS) "°C" else "°F"

                binding.temperature.text = "${viewModel.formatTemperature(temp)}$unitSymbol"
                binding.feelsLikeValue.text = "体感温度: ${viewModel.formatTemperature(feelsLike)}$unitSymbol"
            }
        }
    }

    private fun refreshWeatherData() {
        // 防止重复刷新
        if (isRefreshing) return

        isRefreshing = true

        // 显示刷新状态
        binding.refreshButton.isEnabled = false

        // 保存当前的动画信息
        val originalWeatherCondition = currentWeatherCondition

        // 添加刷新动画效果
        val animationView = binding.weatherAnimation as LottieAnimationView
        val currentSpeed = animationView.speed
        animationView.speed = currentSpeed * 1.5f

        // 调用ViewModel的刷新方法
        currentCityId?.let { cityId ->
            viewModel.refreshWeatherForCity(cityId)
        } ?: run {
            viewModel.refreshCurrentWeather()
        }

        // 3秒后恢复按钮状态和动画速度（无论成功失败）
        binding.root.postDelayed({
            binding.refreshButton.isEnabled = true

            // 恢复动画速度
            if (originalWeatherCondition.isNotEmpty()) {
                val normalSpeed = WeatherAnimationMapper.getAnimationSpeed(originalWeatherCondition)
                animationView.speed = normalSpeed
            } else {
                animationView.speed = 1.0f
            }

            // 如果下拉刷新还在显示，停止它
            if (binding.swipeRefreshLayout.isRefreshing) {
                binding.swipeRefreshLayout.isRefreshing = false
            }

            isRefreshing = false
        }, 3000)
    }

    private fun animateUI() {
        // 温度数字动画
        binding.temperature.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(200)
            .withEndAction {
                binding.temperature.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .start()
            }
            .start()

        // 卡片淡入动画
        val fadeInAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)

        // 检查卡片是否存在
        binding.root.findViewById<CardView?>(R.id.card_main)?.startAnimation(fadeInAnim)

        binding.root.findViewById<CardView?>(R.id.card_details)?.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in).apply {
                startOffset = 100
            }
        )

        // 空气质量卡片可能不存在，使用安全调用
        binding.root.findViewById<CardView?>(R.id.card_air_quality)?.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in).apply {
                startOffset = 200
            }
        )
    }
    override fun onResume() {
        super.onResume()
        // 恢复动画播放
        val animationView = binding.weatherAnimation as LottieAnimationView
        if (!animationView.isAnimating && currentAnimationResId != null) {
            animationView.resumeAnimation()
        }
    }

    override fun onPause() {
        super.onPause()
        // 暂停动画以节省资源
        val animationView = binding.weatherAnimation as LottieAnimationView
        if (animationView.isAnimating) {
            animationView.pauseAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}