package com.example.worldskills.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskills.R
import com.example.worldskills.databinding.ActivityBankomatsMapsBinding
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.adapters.BankomatAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.text.SimpleDateFormat
import java.util.*

class BankomatsMapsActivity : AppCompatActivity(), OnMapReadyCallback, BankomatAdapter.OnItemClickListener, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap

    private lateinit var binding: ActivityBankomatsMapsBinding

    lateinit var adapter: BankomatAdapter

    private val markers = mutableListOf<Marker>()

    private val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)

    private var locationPermissionGranted = false

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val defaultLocation = LatLng(42.0, 42.0)
    private val defaultZoom = 15f

    fun loadBankomats() {
        val bankomats = BankApi.loadBankomats()

        Handler(Looper.getMainLooper()).post {
            adapter.data = bankomats

            for (i in bankomats.indices) {
                val marker = map.addMarker(MarkerOptions().position(bankomats[i].geo).icon(BitmapDescriptorFactory.fromResource(R.drawable.point2)))
                marker.tag = i

                markers.add(marker)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankomatsMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        adapter = BankomatAdapter(this)

        binding.bankomatsRv.layoutManager = LinearLayoutManager(this)
        binding.bankomatsRv.adapter = adapter

        getLocationPermission()

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Thread {
            loadBankomats()
        }.start()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerClickListener(this)

        updateLocationUI()
        getDeviceLocation()
    }

    override fun onItemClick(position: Int) {
        val bankomat = adapter.data[position]

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(bankomat.geo, 10f))
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val bnk = adapter.data[marker?.tag as Int]

        AlertDialog.Builder(this)
            .setTitle("Информация о точке")
            .setMessage("Адрес: ${bnk.street}\nТип: ${bnk.name}\nСостояние: ${if (bnk.isWork) "Работает" else "Не работает"}\nЧасы работы: ${sdf.format(bnk.workStart.time)} - ${sdf.format(bnk.workEnd.time)}")
            .create()
            .show()

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        locationPermissionGranted = false
        when (requestCode) {
            REQUEST_PERMISSION_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationPermissionGranted = true
            }
        }
        updateLocationUI()
        getDeviceLocation()
    }

    private fun getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationPermissionGranted = true
        else
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_ACCESS_FINE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (locationPermissionGranted) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
        } else {
            map.isMyLocationEnabled = false
            map.uiSettings.isMyLocationButtonEnabled = false
            getLocationPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        if (locationPermissionGranted) {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
                            location.latitude,
                            location.longitude
                    ), defaultZoom))
                } else {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, defaultZoom))
                }
            }
        }
    }

    companion object {
        const val REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 0
    }
}