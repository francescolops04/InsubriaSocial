package com.social.insubriasocial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

import com.social.insubriasocial.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Configuration.getInstance().load(this, getSharedPreferences("OpenStreetMap", MODE_PRIVATE))

        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val controller: IMapController = map.controller
        controller.setZoom(18.0)

        val extras = intent.extras
        val username = extras?.getString("usernameMaps")

        if (username != null) {
            findLatLong(username)
        }
    }

    private fun findLatLong(username: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("utenti")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val latitude = document.getDouble("latitudine")
                    val longitude = document.getDouble("longitudine")

                    if (latitude != null && longitude != null) {
                        val startPoint = GeoPoint(latitude, longitude)
                        val marker = Marker(map)
                        marker.position = startPoint
                        marker.title = "Puntatore sul padiglione"
                        map.overlays.add(marker)
                        map.controller.animateTo(startPoint)
                    } else {
                        val intent = Intent(this, Bacheca::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this, "Coordinate non disponibili", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Errore nel recupero delle coordinate", Toast.LENGTH_SHORT).show()
            }
    }
}