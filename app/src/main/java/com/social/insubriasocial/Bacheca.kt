package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

lateinit var btnAdding: Button
lateinit var btnSetting: ImageView
lateinit var btnProfilo: ImageView
lateinit var btnRicerca: ImageView
lateinit var btnChat: ImageView
lateinit var btnBacheca: ImageView
lateinit var announcementList: ListView
lateinit var adapter: ArrayAdapter<String>



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

        announcementList = findViewById<ListView>(R.id.announcementList)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList())
        announcementList.adapter = adapter


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

    private fun loadAnnouncemente() {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            db.collection("utenti")
                .get()
                .addOnSuccessListener { result ->
                    val announcementList = ArrayList<String>()
                    for (document in result) {
                        val title = document.getString("Titolo")
                        val description = document.getString("Descrizione")
                        val user = document.getString("username")
                        if (title != null && description != null) {
                            val announcement = "$user\n$title\n$description"
                            announcementList.add(announcement)
                        }
                    }
                    adapter.clear()
                    adapter.addAll(announcementList)
                }
        }
    }
}