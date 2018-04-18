package com.guiaguga.guiaguga

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.guiaguga.guiaguga.domain.CoffeeShop
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.Marker
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.info

class MapsActivity : AppCompatActivity(),
        OnMapReadyCallback,
        OnMarkerClickListener,
        AnkoLogger {

    // Maps instance
    private lateinit var mMap: GoogleMap

    // Coffeeshop name
    val name = find<TextView>(R.id.tv_shopName)

    // Map creating relation between markers id and coffeeShops
    var coffeShopMarkers: HashMap<String, CoffeeShop> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        mMap.setMinZoomPreference(16.00f)

        for ((index, coffeShop) in getCoffeshops().withIndex()) {
            val markerOption = MarkerOptions()
                    .position(LatLng(coffeShop.lat, coffeShop.long))
                    .title(coffeShop.name);

            val marker = mMap.addMarker(markerOption)
            coffeShopMarkers.put(marker.id, coffeShop)
        }

        val coffeeShop = getCoffeshops().first()
        val mapInitialPosition = LatLng(coffeeShop.lat, coffeeShop.long)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapInitialPosition))
    }

    /**
     * Returns all locations to be marked on the map
     */
    fun getCoffeshops(): List<CoffeeShop> {
        return listOf(
                CoffeeShop("Starbucks", -23.0042166, -43.3181479)
        )
    }

    /**
     * Map Marker onClick Listener
     */
    override fun onMarkerClick(marker: Marker): Boolean {

        val coffeeShop = coffeShopMarkers.get(marker.id)

        if(coffeeShop != null)
            name.text = coffeeShop.name

        return true
    }

}
