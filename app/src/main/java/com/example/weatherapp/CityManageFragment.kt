package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adapters.CityListAdapter
import com.example.weatherapp.databinding.FragmentCityManageBinding
import com.example.weatherapp.viewmodel.WeatherViewModel

class CityManageFragment : Fragment() {

    private var _binding: FragmentCityManageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by activityViewModels()
    private lateinit var adapter: CityListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityManageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = CityListAdapter(
            onCityClick = { city ->
                // 选中城市并切换回天气页面
                viewModel.selectCity(city.id)
                (requireActivity() as MainActivity).switchToWeatherTab()
            },
            onCityDelete = { city ->
                viewModel.removeCity(city.id)
            }
        )

        // 注意：这里使用 layout/city_recycler_view 而不是 layout/cityRecyclerView
        binding.cityRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cityRecyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.cities.observe(viewLifecycleOwner) { cities ->
            adapter.submitList(cities)

            // 更新空状态显示
            if (cities.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.cityRecyclerView.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.cityRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupListeners() {
        // 注意：这里使用 layout/add_city_button 而不是 layout/addCityButton
        binding.addCityButton.setOnClickListener {
            showAddCityDialog()
        }
    }

    private fun showAddCityDialog() {
        val dialog = AddCityDialog()
        dialog.show(childFragmentManager, "AddCityDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}