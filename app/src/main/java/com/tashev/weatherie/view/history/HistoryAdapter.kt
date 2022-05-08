package com.tashev.weatherie.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tashev.weatherie.MyApp.Companion.getStringResourceByName
import com.tashev.weatherie.R
import com.tashev.weatherie.dataSource.Weather

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var weatherData: List<Weather> = listOf()

    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.history_fragment_recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.render(weatherData[position])
    }

    override fun getItemCount() = weatherData.size

    fun getCities(): List<String> {
        val list = mutableListOf<String>()
        weatherData.forEach { weather -> list.add(weather.weatherFact.city.name) }

        return LinkedHashSet(list).toMutableList()
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun render(weather: Weather) {
            itemView.findViewById<TextView>(R.id.historyCityName).text =
                weather.weatherFact.city.name

            itemView.findViewById<TextView>(R.id.historyCondition).text =
                getStringResourceByName(weather.weatherFact.condition)

            itemView.findViewById<TextView>(R.id.historyTemperatureValue).text =
                weather.weatherFact.temperature.toString()

            itemView.findViewById<TextView>(R.id.historyFeelsLikeValue).text =
                weather.weatherFact.feelsLike.toString()

            itemView.findViewById<TextView>(R.id.historyWindSpeed).text =
                weather.weatherFact.windSpeed.toString()

            itemView.findViewById<TextView>(R.id.historyHumidity).text =
                weather.weatherFact.humidity.toString()

            itemView.findViewById<TextView>(R.id.historyPressure).text =
                weather.weatherFact.pressure.toString()
        }
    }
}