package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.RowId

lateinit var nameProfile: TextView
lateinit var userNameProfile: TextView
lateinit var facultyProfile: TextView
lateinit var descriptionProfile: EditText
lateinit var btnSubmit: Button




@SuppressLint("MissingInflatedId")
class Profilo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilo)

        nameProfile = findViewById<TextView>(R.id.nameProfile)
        userNameProfile = findViewById<TextView>(R.id.userProfile)
        facultyProfile = findViewById<TextView>(R.id.facultyProfile)
        descriptionProfile = findViewById<EditText>(R.id.profileDesc)
        btnSubmit = findViewById<Button>(R.id.btnSubmitDesc)

        profileData()
        profileDesc()

        btnSubmit.setOnClickListener {
            collectDesc(descriptionProfile)
        }



    }

    private fun profileData(){
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (currentUserID != null) {
            db.collection("utenti")
                .document(currentUserID)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        val name = document.getString("nome")
                        val lastName = document.getString("cognome")
                        val username = document.getString("username")
                        val faculty = document.getString("facoltà")

                        nameProfile.text = "$name $lastName"
                        userNameProfile.text = "$username"
                        facultyProfile.text = "Facoltà: $faculty"

                    } else {
                        Toast.makeText(this, "Nessun documento trovato", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Errore durante il recupero dei dati: $exception", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Utente non autenticato", Toast.LENGTH_SHORT).show()
        }

    }

    private fun profileDesc() {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (currentUserID != null) {
            db.collection("descrizioni")
                .document(currentUserID)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        val description = document.getString("Description")
                        val editableDescription = SpannableStringBuilder(description ?: "")
                        descriptionProfile.text = editableDescription

                    } else {
                        Toast.makeText(this, "Nessun documento trovato", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Errore durante il recupero dei dati: $exception",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
    }

    private fun collectDesc (profileDesc:EditText){
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val descriptionText = profileDesc.text.toString()

        if(user!=null){
            val desc = hashMapOf(
                "Description" to descriptionText
            )

            db.collection("descrizioni").document(user.uid)
                .set(desc)
                .addOnSuccessListener { documentReference ->
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}

