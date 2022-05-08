package com.tashev.weatherie.view.history

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tashev.weatherie.MyApp
import com.tashev.weatherie.R
import com.tashev.weatherie.dataSource.City
import com.tashev.weatherie.dataSource.Weather
import com.tashev.weatherie.dataSource.WeatherFact
import com.tashev.weatherie.databinding.CitiesListFragmentBinding
import com.tashev.weatherie.databinding.HistoryFragmentBinding
import com.tashev.weatherie.databinding.HistoryFragmentRecyclerItemBinding
import com.tashev.weatherie.repository.LocalRepositoryImpl
import com.tashev.weatherie.viewModel.AppState
import com.tashev.weatherie.viewModel.HistoryViewModel
import com.tashev.weatherie.viewModel.MainViewModel

class HistoryFragment : Fragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private var _binding: HistoryFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = HistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getAllHistory()

        binding.historyFilterFAB.setOnClickListener {
            binding.historyFilterFAB.visibility = View.GONE
            val spinner = binding.historySpinner


            val list = adapter.getCities()

            val adapterSpinner = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                list.toTypedArray()
            )
            spinner.adapter = adapterSpinner

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.getHistoryByCity(spinner.selectedItem.toString())
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.historyFragmentRecyclerView.adapter = adapter
                binding.historyFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.historyFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.historyFragmentLoadingLayout.visibility = View.GONE
                val throwable = appState.error
                Snackbar.make(binding.root, "ERROR $throwable", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}