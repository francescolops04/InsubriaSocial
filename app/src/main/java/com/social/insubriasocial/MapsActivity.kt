package com.social.insubriasocial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

import com.social.insubriasocial.databinding.ActivityMapsBinding



class MapsActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var binding: ActivityMapsBinding
    private lateinit var btnBackM: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btnBackM = findViewById<Button>(R.id.buttonBackM)

        // Configurazione delle impostazioni dell'OpenStreetMap
        Configuration.getInstance().load(this, getSharedPreferences("OpenStreetMap", MODE_PRIVATE))

        // Collegamento del widget MapView
        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        // Impostazione del controller per lo zoom sulla mappa
        val controller: IMapController = map.controller
        controller.setZoom(18.0)

        // Recupero dell'username passato come extra
        val extras = intent.extras
        val username = extras?.getString("usernameMaps")

        // Recupero dell'username passato come extra
        if (username != null) {
            findLatLong(username)
        }

        btnBackM.setOnClickListener {
            val intent = Intent(this, Bacheca::class.java)
            startActivity(intent)
            finish()
        }

    }

    // Funzione per trovare le coordinate associate all'username specificato
    private fun findLatLong(username: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("utenti")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val latitude = document.getDouble("latitudine")
                    val longitude = document.getDouble("longitudine")

                    // Se latitude e longitude non sono nulli, aggiunge un marker sulla mappa
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