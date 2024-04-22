package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.ArrayList

lateinit var btnAdding: Button
lateinit var btnSetting: ImageView
lateinit var btnProfilo: ImageView
lateinit var btnRicerca: ImageView
lateinit var btnChat: ImageView
lateinit var btnBacheca: ImageView
lateinit var announcementList: ListView
lateinit var adapter: ArrayAdapter<String>
lateinit var btnRemove: Button



class Bacheca : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bacheca)

        btnAdding = findViewById<Button>(R.id.AddingB)
        btnSetting = findViewById<ImageView>(R.id.settingsB)
        btnProfilo = findViewById<ImageView>(R.id.ProfileB)
        btnRicerca = findViewById<ImageView>(R.id.SearchB)
        btnChat = findViewById<ImageView>(R.id.ChatB)
        btnBacheca = findViewById<ImageView>(R.id.BachecaB)
        btnRemove = findViewById<Button>(R.id.RemoveB)

        announcementList = findViewById<ListView>(R.id.announcementList)
        adapter = CustomListAdapter(this, ArrayList())
        announcementList.adapter = adapter

        loadAnnouncement()


        btnAdding.setOnClickListener {
            val intent = Intent(this, AddAnnouncement::class.java)
            startActivity(intent)
            finish()
        }

        btnSetting.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }

        btnProfilo.setOnClickListener {
            val intent = Intent(this, Profilo::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadAnnouncement() {
        val db = FirebaseFirestore.getInstance()

        db.collection("utenti")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val announcementList = ArrayList<String>()
                for (document in result) {
                    val title = document.getString("Titolo")
                    val description = document.getString("Descrizione")
                    val user = document.getString("username")
                    if (title != null && description != null && user != null) {
                        val announcement = "$user\n$title\n$description"
                        announcementList.add(announcement)
                    }
                }
                adapter.clear()
                adapter.addAll(announcementList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Errore durante il caricamento degli annunci", Toast.LENGTH_SHORT).show()
            }
    }
}
