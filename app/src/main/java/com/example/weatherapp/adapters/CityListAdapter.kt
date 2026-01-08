package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.City

class CityListAdapter(
    private val onCityClick: (City) -> Unit,
    private val onCityDelete: (City) -> Unit
) : ListAdapter<City, CityListAdapter.CityViewHolder>(CityDiffCallback()) {

    class CityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cityName: TextView = view.findViewById(R.id.city_name)
        val selectedIndicator: View = view.findViewById(R.id.selected_indicator)
        val deleteButton: ImageView = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city_simple, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = getItem(position)

        holder.cityName.text = city.name
        holder.selectedIndicator.visibility = if (city.isSelected) View.VISIBLE else View.INVISIBLE

        holder.itemView.setOnClickListener {
            onCityClick(city)
        }

        holder.deleteButton.setOnClickListener {
            onCityDelete(city)
        }
    }
}

class CityDiffCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem
    }
}