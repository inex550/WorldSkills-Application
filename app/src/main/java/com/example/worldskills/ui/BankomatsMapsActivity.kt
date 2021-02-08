package com.example.worldskills.ui

import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskills.R
import com.example.worldskills.databinding.ActivityBankomatsMapsBinding
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.adapters.BankomatAdapter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.text.SimpleDateFormat
import java.util.*

class BankomatsMapsActivity : AppCompatActivity(), OnMapReadyCallback, BankomatAdapter.OnItemClickListener, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap

    private lateinit var binding: ActivityBankomatsMapsBinding

    lateinit var adapter: BankomatAdapter

    val markers = mutableListOf<Marker>()

    val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)

    fun loadBankomats() {
        val bankomats = BankApi.loadBankomats()

        Handler(Looper.getMainLooper()).post {
            adapter.data = bankomats

            for (i in bankomats.indices) {
                val marker = mMap.addMarker(MarkerOptions().position(bankomats[i].geo).icon(BitmapDescriptorFactory.fromResource(R.drawable.point2)))
                marker.tag = i

                markers.add(marker)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankomatsMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BankomatAdapter(this)

        binding.bankomatsRv.layoutManager = LinearLayoutManager(this)
        binding.bankomatsRv.adapter = adapter

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Thread {
            loadBankomats()
        }.start()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)

        val sydney = LatLng(-34.0, 151.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))
    }

    override fun onItemClick(position: Int) {
        val bankomat = adapter.data[position]

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bankomat.geo, 10f))
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
}