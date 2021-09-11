package com.tashev.gbweatherfromya.view.citiesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tashev.gbweatherfromya.R
import com.tashev.gbweatherfromya.dataSource.Weather
import com.tashev.gbweatherfromya.databinding.CitiesListFragmentBinding
import com.tashev.gbweatherfromya.view.weatherDetails.DetailedWeatherFragment
import com.tashev.gbweatherfromya.viewModel.AppState
import com.tashev.gbweatherfromya.viewModel.MainViewModel

class CitiesListFragment : Fragment() {

    companion object {
        fun newInstance() = CitiesListFragment()
    }


    private var _binding: CitiesListFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private var isDataSetRus: Boolean = true

    private val adapter = CitiesListAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            val manager = activity?.supportFragmentManager
            manager?.let{
                val bundle = Bundle()
                bundle.putParcelable(DetailedWeatherFragment.BUNDLE_KEY, weather)

                manager.beginTransaction()
                    .add(R.id.main_container, DetailedWeatherFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commit()
            }
        }
    })


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = CitiesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            mainFragmentRecyclerView.adapter = adapter
            mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        }

        with(viewModel) {
            getLiveData().observe(viewLifecycleOwner, { renderData(it) })
            getWeatherFromLocalSourceRus()
        }
    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.world)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.russian_flag)
        }
        isDataSetRus = !isDataSetRus
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                snackbarWithString(R.string.error, R.string.reload)
            }
        }
    }

    interface OnItemViewClickListener {fun onItemViewClick(weather: Weather)}


    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }

    private fun snackbarWithString(message: String, actionMessage:String) {
        Snackbar
            .make(
                binding.root,
                message,
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(actionMessage) {
                viewModel.getWeatherFromLocalSourceRus()
            }
            .show()
    }

    private fun snackbarWithString(message: Int, actionMessage:Int) {
        Snackbar
            .make(
                binding.root,
                message,
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(actionMessage) {
                viewModel.getWeatherFromLocalSourceRus()
            }
            .show()
    }

    private fun snackBarWithoutAction(string: Int) {
        Snackbar
            .make(
                binding.root,
                getString(string),
                Snackbar.LENGTH_INDEFINITE
            )
            .show()
    }
}