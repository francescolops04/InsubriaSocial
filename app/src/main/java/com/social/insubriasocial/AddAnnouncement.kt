package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var btnBack: Button
private lateinit var btnAccept: Button
private lateinit var titleA: EditText
private lateinit var descA: EditText
private lateinit var padSpinner: Spinner

class AddAnnouncement : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_announcement)


        btnBack = findViewById<Button>(R.id.buttonBackA)
        btnAccept = findViewById<Button>(R.id.buttonAcceptA)
        titleA = findViewById<EditText>(R.id.titleA)
        descA = findViewById<EditText>(R.id.announcementA)
        padSpinner = findViewById<Spinner>(R.id.spinnerPad)


        btnBack.setOnClickListener {
            val intent = Intent(this, Bacheca::class.java)
            startActivity(intent)
            finish()
        }

        btnAccept.setOnClickListener {
            val title = titleA.text.toString()
            val description = descA.text.toString()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Inserire titolo e/o descrizione", Toast.LENGTH_SHORT).show()
            } else {
                val coordinates = when (padSpinner.selectedItem.toString()) {
                    "Mensa" -> Pair(45.798601, 8.853576)
                    "Montegeneroso" -> Pair(45.798185, 8.852733)
                    "Antonini" -> Pair(45.798866, 8.850094)
                    "Morselli" -> Pair(45.798795, 8.849096)
                    "Seppilli" -> Pair(45.799311, 8.847229)
                    else -> null
                }

                if (coordinates == null) {
                    createAnnouncement(titleA, descA)
                } else {
                    createAnnouncementCoordinates(titleA, descA, coordinates.first, coordinates.second)
                }

                Thread.sleep(1000)
                val intent = Intent(this, Bacheca::class.java)
                startActivity(intent)
                finish()
            }
        }

    }


    private fun createAnnouncementCoordinates(title: EditText, desc: EditText, lat: Double, long: Double){
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val titleText = title.text.toString()
        val descText = desc.text.toString()


        if(user!=null){
            val ann = hashMapOf<String, Any>(
                "Titolo" to titleText,
                "Descrizione" to descText,
                "timestamp" to FieldValue.serverTimestamp(),
                "latitudine" to lat,
                "longitudine" to long
            )

            db.collection("utenti").document(user.uid)
                .update(ann)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Annuncio creato con successo", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Impossibile salvare i dati dell'utente: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }
    }

    private fun createAnnouncement(title: EditText, desc: EditText){
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val titleText = title.text.toString()
        val descText = desc.text.toString()


        if(user!=null){
            val ann = hashMapOf<String, Any>(
                "Titolo" to titleText,
                "Descrizione" to descText,
                "timestamp" to FieldValue.serverTimestamp(),
            )

            db.collection("utenti").document(user.uid)
                .update(ann)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Annuncio creato con successo", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Impossibile salvare i dati dell'utente: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }
    }
}