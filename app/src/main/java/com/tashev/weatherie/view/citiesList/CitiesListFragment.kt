package com.tashev.weatherie.view.citiesList

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tashev.weatherie.R
import com.tashev.weatherie.dataSource.City
import com.tashev.weatherie.dataSource.Weather
import com.tashev.weatherie.dataSource.WeatherFact
import com.tashev.weatherie.databinding.CitiesListFragmentBinding
import com.tashev.weatherie.view.weatherDetails.DetailedWeatherFragment
import com.tashev.weatherie.viewModel.AppState
import com.tashev.weatherie.viewModel.MainViewModel
import java.io.IOException
import java.util.*

class CitiesListFragment : Fragment(R.layout.cities_list_fragment) {

    companion object {
        fun newInstance() = CitiesListFragment()
    }


    private var _binding: CitiesListFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private var isDataSetRus: Boolean = true

    private val REFRESH_PERIOD = 10000L
    private val MINIMAL_DISTANCE = 100f
    private val REQUEST_CODE = 999

    private val adapter = CitiesListAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            val manager = activity?.supportFragmentManager
            manager?.let {
                val bundle = Bundle()
                bundle.putParcelable(DetailedWeatherFragment.BUNDLE_KEY, weather)

                manager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.main_container, DetailedWeatherFragment.newInstance(bundle))
                    addToBackStack("")
                }
            }
        }
    })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = CitiesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            mainFragmentRecyclerView.adapter = adapter
            mainFragmentRegionFAB.setOnClickListener { changeWeatherDataSet() }
        }

        with(viewModel) {
            getLiveData().observe(viewLifecycleOwner, { renderData(it) })
            getWeatherFromLocalSourceRus()
        }

        binding.mainFragmentMapFAB.setOnClickListener { checkPermission() }

    }

    private fun checkPermission() {
        context?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getLocation()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRatio()
            } else {
                myRequestPermission()
            }
        }
    }

    private fun showRatio() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_rationale_title)
            .setMessage(R.string.dialog_rationale_message)
            .setPositiveButton(R.string.dialog_rationale_give_access) { dialog, which ->
                myRequestPermission()
            }
            .setNegativeButton(R.string.dialog_rationale_decline) { dialog, which ->
                dialog.dismiss()
            }
            .create().show()
    }

    private fun myRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    context?.let {
                        showRatio()
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val onLocationChangeListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            getAddressAsync(requireContext(), location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        } 
    }

    private fun getAddressAsync(context: Context, location: Location) {
        val geoCoder = Geocoder(context, Locale.getDefault())
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )

                binding.mainFragmentMapFAB.post {
                    showAddressDialog(addresses!![0].locality, location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
//        var address = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
//        showAddressDialog(address[0].getAddressLine(0), location)
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            androidx.appcompat.app.AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    onItemClick(
                        Weather(
                            WeatherFact(
                                City(
                                    address,
                                    location.latitude,
                                    location.longitude
                                )
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun onItemClick(weather: Weather) {
        val bundle = Bundle()
        bundle.putParcelable(DetailedWeatherFragment.BUNDLE_KEY, weather)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, DetailedWeatherFragment.newInstance(bundle))
            .addToBackStack("")
            .commit()
    }

    private fun getLocation() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager =
                    it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationChangeListener
                        )
                    }

                } else {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    } else {
                        getAddressAsync(requireContext(), location)
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }

                }
            } else {
                showRatio()
            }
        }
    }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }


    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentRegionFAB.setImageResource(R.drawable.world)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentRegionFAB.setImageResource(R.drawable.russian_flag)
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

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }


    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }

    fun snackbarWithString(message: String, actionMessage: String) {
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

    fun snackbarWithString(message: Int, actionMessage: Int) {
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

    fun snackBarWithoutAction(string: Int) {
        Snackbar
            .make(
                binding.root,
                getString(string),
                Snackbar.LENGTH_INDEFINITE
            )
            .show()
    }
}