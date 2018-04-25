package com.guiaguga.guiaguga

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.guiaguga.guiaguga.domain.CoffeeShop
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find

class MapsActivity : AppCompatActivity(),
        OnMapReadyCallback,
        OnMarkerClickListener,
        AnkoLogger {

    // Maps instance
    private lateinit var mMap: GoogleMap

    // Coffeeshop name
    private lateinit var name: TextView

    // Map creating relation between markers id and coffeeShops
    private var coffeShopMarkers: HashMap<String, CoffeeShop> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        name = find<TextView>(R.id.tv_shopName)

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

        val coffeshops = getCoffeshops()

        for (coffeShop in coffeshops) {
            val markerOption = MarkerOptions()
                    .position(LatLng(coffeShop.lat, coffeShop.long))
                    .title(coffeShop.name)

            val marker = mMap.addMarker(markerOption)
            coffeShopMarkers[marker.id] = coffeShop
        }

        renderInitialCoffeeShop(coffeshops)
    }

    /**
     *  Decides and shows the first CoffeeShop on the map
     */
    private fun renderInitialCoffeeShop(coffeeShops: List<CoffeeShop>) {
        val initialCoffeeShop = coffeeShops.first()
        val mapInitialPosition = LatLng(initialCoffeeShop.lat, initialCoffeeShop.long)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapInitialPosition))
        displayCoffeeShop(initialCoffeeShop)
    }


    /**
     * Returns all locations to be marked on the map
     */
    private fun getCoffeshops(): List<CoffeeShop> {
        return listOf(
                CoffeeShop("Starbucks", -23.0042166, -43.3181479)
        )
    }

    /**
     * Map Marker onClick Listener
     */
    override fun onMarkerClick(marker: Marker): Boolean {
        val coffeeShop = coffeShopMarkers[marker.id]
        if (coffeeShop != null) {
            displayCoffeeShop(coffeeShop)
        }
        return true
    }

    /**
     * Given a CoffeeShop, display its info
     */
    private fun displayCoffeeShop(coffeeShop: CoffeeShop) {
        name.text = coffeeShop.name
    }

}