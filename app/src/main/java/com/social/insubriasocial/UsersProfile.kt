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

        btnChatUP.setOnClickListener {
            avviaChat(nameUP.text.toString(), usernameUP.text.toString())
        }

        btnBackUP.setOnClickListener {
            val intent = Intent(this, SistemaDiRicerca::class.java)
            startActivity(intent)
            finish()
        }

        val extras = intent.extras
        val user = extras?.getString("user")
        if (user != null) {
            profileData(user)
        }
    }

    private fun profileData(user: String){
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
                    if(descrizione.isNullOrEmpty()){
                        descriptionUP.text = ""
                    }else{
                        descriptionUP.text = "$descrizione"

                    }
                }
            }
    }

    private fun avviaChat(nome: String, username: String) {
        val db = FirebaseFirestore.getInstance()

        val chatId = "${FirebaseAuth.getInstance().currentUser?.uid}"

        val userChatRef = db.collection("utenti")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .collection("chat")
            .document(chatId)

        db.collection("utenti")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val otherUserId = document.id
                    val otherUserChatRef = db.collection("utenti")
                        .document(otherUserId)
                        .collection("chat")
                        .document(chatId)

                    userChatRef.get().addOnSuccessListener { userChatSnapshot ->
                        if (!userChatSnapshot.exists()) {
                            val chatData = hashMapOf(
                                "nome" to nome,
                                "username" to username
                            )

                            userChatRef.set(chatData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Chat avviata con successo!", Toast.LENGTH_SHORT).show()
                                }


                            otherUserChatRef.set(chatData)
                                .addOnSuccessListener {
                                }
                        } else {
                            Toast.makeText(this, "Questa chat è già stata avviata.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }


}


