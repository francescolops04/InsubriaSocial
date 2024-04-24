package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

lateinit var btnBack: Button
lateinit var btnAccept: Button
lateinit var titleA: EditText
lateinit var descA: EditText

class AddAnnouncement : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_announcement)


        btnBack = findViewById<Button>(R.id.buttonBackA)
        btnAccept = findViewById<Button>(R.id.buttonAcceptA)
        titleA = findViewById<EditText>(R.id.titleA)
        descA = findViewById<EditText>(R.id.announcementA)


        btnBack.setOnClickListener {
            val intent = Intent(this, Bacheca::class.java)
            startActivity(intent)
            finish()
        }

        btnAccept.setOnClickListener {
            if(titleA.text.toString().isEmpty() || descA.text.toString().isEmpty()){
                Toast.makeText(this, "Inserire titolo e/o descrizione", Toast.LENGTH_SHORT).show()
            } else {
                createAnnouncement(titleA, descA)
                Thread.sleep(1000)
                val intent = Intent(this, Bacheca::class.java)
                startActivity(intent)
                finish()
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
                "timestamp" to FieldValue.serverTimestamp()
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