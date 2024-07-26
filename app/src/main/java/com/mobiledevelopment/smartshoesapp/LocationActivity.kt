package com.mobiledevelopment.smartshoesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.Marker

class LocationActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        Configuration.getInstance().load(applicationContext, android.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext))

        mapView = findViewById(R.id.map)
        mapView.setTileSource(TileSourceFactory.MAPNIK)

        mapView.controller.setZoom(17.5)
        val geoPoint = GeoPoint(-23.466133, -51.840033)
        mapView.controller.setCenter(geoPoint)

        val startMarker = Marker(mapView)
        startMarker.position = geoPoint
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapView.overlays.add(startMarker)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()  //needed for compass, my location overlays, v6.0.0 and up
    }
}