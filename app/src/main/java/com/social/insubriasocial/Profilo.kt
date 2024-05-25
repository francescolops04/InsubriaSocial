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

private lateinit var btnSubmit: Button
private lateinit var nameProfile: TextView
private lateinit var userNameProfile: TextView
private lateinit var facultyProfile: TextView
private lateinit var descriptionProfile: EditText
private lateinit var btnSettingsP: ImageView
private lateinit var btnBachecaP: ImageView
private lateinit var btnSearchP: ImageView
private lateinit var btnChatP: ImageView


@SuppressLint("MissingInflatedId")
class Profilo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilo)

        btnSubmit = findViewById<Button>(R.id.btnSubmitDesc)
        nameProfile = findViewById<TextView>(R.id.nameProfile)
        userNameProfile = findViewById<TextView>(R.id.userProfile)
        facultyProfile = findViewById<TextView>(R.id.facultyProfile)
        descriptionProfile = findViewById<EditText>(R.id.profileDesc)
        btnSettingsP = findViewById<ImageView>(R.id.settingsP)
        btnBachecaP = findViewById<ImageView>(R.id.BachecaP)
        btnSearchP = findViewById<ImageView>(R.id.SearchP)
        btnChatP = findViewById<ImageView>(R.id.ChatP)

        // Popola i campi del profilo con i dati dell'utente
        profileData()
        profileDesc()

        // Aggiorna il pulsante di invio della descrizione durante l'immissione del testo
        descriptionProfile.doAfterTextChanged { editable ->
            checkDescriptionAndUpdateButton(editable.toString())
        }

        btnSettingsP.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }

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

        btnSubmit.setOnClickListener {
            collectDesc(descriptionProfile)
            btnSubmit.visibility = View.GONE
        }
    }

    // Controlla se la descrizione è stata modificata e aggiorna la visibilità del pulsante di invio
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


    // Popola i campi del profilo con i dati dell'utente
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

    // Popola il campo della descrizione del profilo con i dati dell'utente
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

    // Aggiorna la descrizione dell'utente nel database Firestore
    private fun collectDesc (profileDesc:EditText){
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val descriptionText = profileDesc.text.toString()

        if(user!=null){
            // Crea una mappa (hashMap) che contiene il campo "Description" con il valore di "descriptionText"
            val desc = hashMapOf<String, Any>(
                "Description" to descriptionText
            )

            // Ottiene la collezione "utenti" e seleziona il documento dell'utente corrente usando il suo ID
            db.collection("utenti").document(user.uid)
                .update(desc)
                .addOnSuccessListener { documentReference ->
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Impossibile salvare i dati dell'utente: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}

