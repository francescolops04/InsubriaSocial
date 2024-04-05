package com.social.insubriasocial

import android.annotation.SuppressLint
import android.os.Bundle
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
lateinit var lastnameProfile: TextView
lateinit var userNameProfile: TextView
lateinit var facultyProfile: TextView




@SuppressLint("MissingInflatedId")
class Profilo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilo)

        nameProfile = findViewById<TextView>(R.id.nameProfile)
        lastnameProfile = findViewById<TextView>(R.id.lastnameProfile)
        userNameProfile = findViewById<TextView>(R.id.userProfile)
        facultyProfile = findViewById<TextView>(R.id.facultyProfile)


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

                        nameProfile.text = "$name"
                        lastnameProfile.text = "$lastName"
                        userNameProfile.text = "$username"
                        facultyProfile.text = "$faculty"

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
}

