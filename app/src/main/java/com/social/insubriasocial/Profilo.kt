package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

lateinit var nameProfile: TextView
lateinit var userNameProfile: TextView
lateinit var facultyProfile: TextView
lateinit var descriptionProfile: EditText
lateinit var btnSubmit: Button
lateinit var btnSettingsP: ImageView
lateinit var btnBachecaP: ImageView
lateinit var btnSearchP: ImageView
lateinit var btnChatP: ImageView


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
        btnSettingsP = findViewById<ImageView>(R.id.settingsP)
        btnBachecaP = findViewById<ImageView>(R.id.BachecaP)
        btnSearchP = findViewById<ImageView>(R.id.SearchP)
        btnChatP = findViewById<ImageView>(R.id.ChatP)

        profileData()
        profileDesc()

        descriptionProfile.doAfterTextChanged { editable ->
            checkDescriptionAndUpdateButton(editable.toString())
        }

        btnSubmit.setOnClickListener {
            collectDesc(descriptionProfile)
            btnSubmit.visibility = View.GONE
        }

        btnSettingsP.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        };

        btnBachecaP.setOnClickListener{
            val intent = Intent(this, Bacheca::class.java)
            startActivity(intent)
            finish()
        }

        btnSearchP.setOnClickListener {
            val intent = Intent(this, SistemaDiRicerca::class.java)
            startActivity(intent)
            finish()
        }

        btnChatP.setOnClickListener {
            val intent = Intent(this, ChatList::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun checkDescriptionAndUpdateButton(currentDescription: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (currentUserID != null) {
            db.collection("utenti")
                .document(currentUserID)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val descriptionInDb = document.getString("Description") ?: ""
                        btnSubmit.visibility = if (descriptionInDb != currentDescription) View.VISIBLE else View.GONE
                    } else {
                        Toast.makeText(this, "Nessun documento trovato", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Errore durante il recupero dei dati: $exception", Toast.LENGTH_SHORT).show()
                }
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
            db.collection("utenti")
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
            val desc = hashMapOf<String, Any>(
                "Description" to descriptionText
            )

            db.collection("utenti").document(user.uid)
                .update(desc)
                .addOnSuccessListener { documentReference ->
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}

