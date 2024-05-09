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

lateinit var btnBack: Button
lateinit var btnAccept: Button
lateinit var titleA: EditText
lateinit var descA: EditText
lateinit var padSpinner: Spinner

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
            if(titleA.text.toString().isEmpty() || descA.text.toString().isEmpty()){
                Toast.makeText(this, "Inserire titolo e/o descrizione", Toast.LENGTH_SHORT).show()
            } else {
                if(padSpinner.selectedItem.toString().equals("Seleziona padiglione (opzionale)")){
                    createAnnouncement(titleA, descA)
                    Thread.sleep(1000)
                    val intent = Intent(this, Bacheca::class.java)
                    startActivity(intent)
                    finish()
                }else if (padSpinner.selectedItem.toString().equals("Mensa")){
                    createAnnouncementCoordinates(titleA, descA,"45.798601", "8.853576")
                    Thread.sleep(1000)
                    val intent = Intent(this, Bacheca::class.java)
                    startActivity(intent)
                    finish()
                }else if (padSpinner.selectedItem.toString().equals("Montegeneroso")){
                    createAnnouncementCoordinates(titleA, descA,"45.798185", "8.852733")
                    Thread.sleep(1000)
                    val intent = Intent(this, Bacheca::class.java)
                    startActivity(intent)
                    finish()
                }else if (padSpinner.selectedItem.toString().equals("Antonini")){
                    createAnnouncementCoordinates(titleA, descA,"45.798866","8.850094")
                    Thread.sleep(1000)
                    val intent = Intent(this, Bacheca::class.java)
                    startActivity(intent)
                    finish()
                }else if (padSpinner.selectedItem.toString().equals("Morselli")){
                    createAnnouncementCoordinates(titleA, descA,"45.798795", "8.849096")
                    Thread.sleep(1000)
                    val intent = Intent(this, Bacheca::class.java)
                    startActivity(intent)
                    finish()
                }else if (padSpinner.selectedItem.toString().equals("Seppilli")){
                    createAnnouncementCoordinates(titleA, descA,"45.799311", "8.847229")
                    Thread.sleep(1000)
                    val intent = Intent(this, Bacheca::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }
    }


    private fun createAnnouncementCoordinates(title: EditText, desc: EditText, lat: String, long: String){
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
                    Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }
    }
}