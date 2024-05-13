package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


lateinit var btnBackUP: Button
lateinit var btnChatUP: Button
lateinit var usernameUP: TextView
lateinit var nameUP: TextView
lateinit var facultyUP: TextView
lateinit var descriptionUP: TextView


class UsersProfile : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_profile)

        btnBackUP = findViewById<Button>(R.id.backUP)
        usernameUP = findViewById<TextView>(R.id.userProfileUP)
        nameUP = findViewById<TextView>(R.id.nameProfileUP)
        facultyUP = findViewById<TextView>(R.id.facultyProfileUP)
        descriptionUP = findViewById<TextView>(R.id.profileDescUP)
        btnChatUP = findViewById<Button>(R.id.chatUP)


        btnBackUP.setOnClickListener {
            val intent = Intent(this, SistemaDiRicerca::class.java)
            startActivity(intent)
            finish()
        }

        btnChatUP.setOnClickListener {
            startChat(usernameUP.text.toString())
        }

        val extras = intent.extras
        val user = extras?.getString("user")
        if (user != null) {
            profileData(user)
        }
    }

    private fun profileData(user: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("utenti")
            .whereEqualTo("username", user)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val nome = document.getString("nome")
                    val cognome = document.getString("cognome")
                    val username = document.getString("username")
                    val facoltà = document.getString("facoltà")
                    val descrizione = document.getString("Description")

                    nameUP.text = "$nome $cognome"
                    usernameUP.text = "$username"
                    facultyUP.text = "Facoltà: $facoltà"
                    if (descrizione.isNullOrEmpty()) {
                        descriptionUP.text = ""
                    } else {
                        descriptionUP.text = "$descrizione"

                    }
                }
            }
    }


    private fun startChat(contactUsername: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid


        val db = FirebaseFirestore.getInstance()
        db.collection("utenti")
            .whereEqualTo("username", contactUsername)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val contactUserID = documents.documents[0].id

                    val chatID = if (currentUserID!! < contactUserID) {
                        "$currentUserID-$contactUserID"
                    } else {
                        "$contactUserID-$currentUserID"
                    }
                    db.collection("chats").document(chatID)
                        .get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                Toast.makeText(
                                    this,
                                    "La chat è già stata avviata",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val chatData = hashMapOf(
                                    "user1" to currentUserID,
                                    "user2" to contactUserID
                                )

                                db.collection("chats").document(chatID)
                                    .set(chatData)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this,
                                            "Chat creata con successo",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            this,
                                            "Errore durante la creazione della chat: $e",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Errore durante il controllo della chat: $e",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(this, "Utente non trovato", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Errore durante la ricerca dell'utente: $e",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }
}


