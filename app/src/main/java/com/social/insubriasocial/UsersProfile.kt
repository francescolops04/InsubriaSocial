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


private lateinit var btnBackUP: Button
private lateinit var btnChatUP: Button
private lateinit var usernameUP: TextView
private lateinit var nameUP: TextView
private lateinit var facultyUP: TextView
private lateinit var descriptionUP: TextView


class UsersProfile : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_profile)

        btnBackUP = findViewById<Button>(R.id.backUP)
        btnChatUP = findViewById<Button>(R.id.chatUP)
        usernameUP = findViewById<TextView>(R.id.userProfileUP)
        nameUP = findViewById<TextView>(R.id.nameProfileUP)
        facultyUP = findViewById<TextView>(R.id.facultyProfileUP)
        descriptionUP = findViewById<TextView>(R.id.profileDescUP)


        btnBackUP.setOnClickListener {
            val intent = Intent(this, SistemaDiRicerca::class.java)
            startActivity(intent)
            finish()
        }

        btnChatUP.setOnClickListener {
            startChat(usernameUP.text.toString())
            Thread.sleep(5000)
            val intent = Intent(this, ChatList::class.java)
            startActivity(intent)
            finish()
        }

        //Recupera i dati dell'utente passati tramite intent
        val extras = intent.extras
        val user = extras?.getString("user")
        if (user != null) {
            profileData(user)
        }
    }

    //Recupera e visualizza i dati del profilo di un utente specifico.
    private fun profileData(user: String) {
        val db = FirebaseFirestore.getInstance()

        // Cerca l'utente nel Firestore
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

                    // Aggiorna i campi dell'interfaccia utente con i dati recuperati
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

    //Avvia una chat con un utente specifico
    private fun startChat(contactUsername: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid


        val db = FirebaseFirestore.getInstance()
        // Cerca l'utente con l'username specificato nel Firestore
        db.collection("utenti")
            .whereEqualTo("username", contactUsername)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val contactUserID = documents.documents[0].id

                    // Crea un ID univoco per la chat
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
                                    // Crea una nuova chat se non esiste già
                                    "user1" to currentUserID,
                                    "user2" to contactUserID
                                )

                                db.collection("chats").document(chatID)
                                    .set(chatData)
                                    .addOnSuccessListener {
                                        // Questo blocco viene eseguito se i dati della chat vengono salvati correttamente nel database
                                        Toast.makeText(
                                            this,
                                            "Chat creata con successo",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                    .addOnFailureListener { e ->
                                        // Questo blocco viene eseguito se c'è un errore nel salvare i dati della chat nel database
                                        Toast.makeText(
                                            this,
                                            "Errore durante la creazione della chat: $e",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            // Questo blocco viene eseguito se c'è un errore nel controllare l'esistenza del documento della chat nel database
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


