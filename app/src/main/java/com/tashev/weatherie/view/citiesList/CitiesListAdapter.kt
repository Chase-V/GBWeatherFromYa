package com.tashev.weatherie.view.citiesList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tashev.weatherie.R
import com.tashev.weatherie.dataSource.Weather

class CitiesListAdapter(private var onItemViewClickListener: CitiesListFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<CitiesListAdapter.CitiesViewHolder>() {

    private var weatherData: List<Weather> = listOf()

    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CitiesViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.main_fragment_recycler_item, parent, false) as View
    )

    override fun onBindViewHolder(holder: CitiesListAdapter.CitiesViewHolder, position: Int) =
        holder.bind(weatherData[position])

    override fun getItemCount() = weatherData.size

    inner class CitiesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            itemView.findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
                weather.weatherFact.city.name
            itemView.setOnClickListener { onItemViewClickListener?.onItemViewClick(weather) }
        }
    }

    fun removeListener() {
        onItemViewClickListener = null
    }
}