package com.example.gagalmuluyaallah.Map

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.gagalmuluyaallah.R
import com.example.gagalmuluyaallah.ResultSealed
import com.example.gagalmuluyaallah.View.RegisterActivity
import com.example.gagalmuluyaallah.connection.UserPreference
import com.example.gagalmuluyaallah.connection.ViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.gagalmuluyaallah.databinding.ActivityMapsBinding
import com.example.gagalmuluyaallah.model.dataStore
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var mMap: GoogleMap
    private val boundsBuilder = LatLngBounds.Builder()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(RegisterActivity.SESSION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)
        mapsViewModel = getViewModel(this@MapsActivity)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupStoryMarkers()
    }

    private fun setupStoryMarkers() {
        mapsViewModel.getStoriesWithLocation().observe(this) {
            if (it != null) {
                when (it) {
                    is ResultSealed.Loading -> {
                        showLoading(true)
                    }

                    is ResultSealed.Success -> {
                        showLoading(false)

                        val response = it.data
                        response.forEach { data ->
                            if (data.lat != null && data.lon != null) {
                                val latLng = LatLng(data.lat, data.lon)
                                mMap.addMarker(
                                        MarkerOptions()
                                            .position(latLng)
                                            .title(data.name)
                                            .snippet(data.description)
                                )
                                boundsBuilder.include(latLng)
                            }
                        }

                        val bounds: LatLngBounds = boundsBuilder.build()
                        mMap.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(
                                        bounds,
                                        resources.displayMetrics.widthPixels,
                                        resources.displayMetrics.heightPixels,
                                        300
                                )
                        )
                    }

                    is ResultSealed.Error -> {
                        showLoading(false)
                        Log.e("MapsActivity", "Error: ${it.exception}")
                    }
                }
            }
        }
    }

    private fun getViewModel(activity: AppCompatActivity): MapsViewModel {
        val factory = ViewModelFactory.getInstance(
                activity.application,
                UserPreference.getInstance(dataStore)
        )
        return ViewModelProvider(activity, factory)[MapsViewModel::class.java]
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getUserLocation()
    }

    private val requestPermissionLauncher =
            registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    getUserLocation()
                } else {
                    Log.e("MapsActivity", "Permission denied")
                }
            }

    private fun getUserLocation() {
        if (ContextCompat.checkSelfPermission(
                    this.applicationContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val userLatLng = LatLng(location.latitude, location.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng)) // Move the camera immediately to the user location
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f)) // Animate the zoom process
                    } else {
                        Log.e("MapsActivity", "Location is null")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MapsActivity", "Error getting user location: ${e.message}")
                }

        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        //TODO("masih binggung biar ngecall getuserlocationnya biar ga double")
    }
}