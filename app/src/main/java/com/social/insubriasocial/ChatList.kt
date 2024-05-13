package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


private lateinit var btnSettingChat: ImageView
private lateinit var btnProfiloChat: ImageView
private lateinit var btnRicercaChat: ImageView
private lateinit var btnBachecaChat: ImageView
private lateinit var Chatlist: ListView
private lateinit var adapterChatlist: ArrayAdapter<String>

class ChatList : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        btnSettingChat = findViewById<ImageView>(R.id.SettingsChat)
        btnProfiloChat  = findViewById<ImageView>(R.id.ProfileChat)
        btnRicercaChat = findViewById<ImageView>(R.id.SearchChat)
        btnBachecaChat = findViewById<ImageView>(R.id.BachecaChat)

        Chatlist = findViewById<ListView>(R.id.listChatList)
        adapterChatlist = CustomChatAdapter(this, ArrayList())
        Chatlist.adapter = adapterChatlist


        btnRicercaChat.setOnClickListener {
            val intent = Intent(this, SistemaDiRicerca::class.java)
            startActivity(intent)
            finish()
        }

        btnSettingChat.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }

        btnProfiloChat.setOnClickListener {
            val intent = Intent(this, Profilo::class.java)
            startActivity(intent)
            finish()
        }

        btnBachecaChat.setOnClickListener {
            val intent = Intent(this, Bacheca::class.java)
            startActivity(intent)
            finish()
        }

        loadChatList()

        Chatlist.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedUser = parent.getItemAtPosition(position) as String
            val contactUsername = selectedUser.substringBefore("\n")

            val db = FirebaseFirestore.getInstance()

            db.collection("utenti")
                .whereEqualTo("username", contactUsername)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val contactUserID = document.id

                        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

                        db.collection("chats")
                            .whereEqualTo("user1", currentUserID)
                            .whereEqualTo("user2", contactUserID)
                            .get()
                            .addOnSuccessListener { chatDocuments ->
                                if (chatDocuments.isEmpty) {
                                    db.collection("chats")
                                        .whereEqualTo("user1", contactUserID)
                                        .whereEqualTo("user2", currentUserID)
                                        .get()
                                        .addOnSuccessListener { chat2Documents ->
                                            for (chatDocument in chat2Documents) {
                                                val chatID = chatDocument.id
                                                val intent = Intent(this, Chat::class.java)
                                                intent.putExtra("chatID", chatID)
                                                intent.putExtra("contactUsername", contactUsername)
                                                startActivity(intent)
                                            }
                                        }
                                } else {
                                    for (chatDocument in chatDocuments) {
                                        val chatID = chatDocument.id
                                        val intent = Intent(this, Chat::class.java)
                                        intent.putExtra("chatID", chatID)
                                        intent.putExtra("contactUsername", contactUsername)
                                        startActivity(intent)
                                    }
                                }
                            }
                    }
                }
        }


    }

    private fun loadChatList() {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        val searchListChat = ArrayList<String>() // Lista esterna per unire i risultati

        db.collection("chats")
            .whereIn("user1", listOf(currentUserID))
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val contactUserID = document.getString("user2")
                    if (contactUserID != null) {
                        db.collection("utenti").document(contactUserID)
                            .get()
                            .addOnSuccessListener { userDocument ->
                                val contactUsername = userDocument.getString("username")
                                val contactName = userDocument.getString("nome")
                                val contactLastName = userDocument.getString("cognome")
                                if (contactUsername != null && contactName != null && contactLastName != null) {
                                    val infoContact = "$contactUsername\n$contactName $contactLastName"
                                    searchListChat.add(infoContact)
                                    adapterChatlist.clear()
                                    adapterChatlist.addAll(searchListChat)
                                }
                            }
                    }
                }
            }

        db.collection("chats")
            .whereIn("user2", listOf(currentUserID))
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val contactUserID = document.getString("user1")
                    if (contactUserID != null) {
                        db.collection("utenti").document(contactUserID)
                            .get()
                            .addOnSuccessListener { userDocument ->
                                val contactUsername = userDocument.getString("username")
                                val contactName = userDocument.getString("nome")
                                val contactLastName = userDocument.getString("cognome")
                                if (contactUsername != null && contactName != null && contactLastName != null) {
                                    val infoContact = "$contactUsername\n$contactName $contactLastName"
                                    searchListChat.add(infoContact)
                                    adapterChatlist.clear()
                                    adapterChatlist.addAll(searchListChat)
                                }
                            }
                    }
                }
            }
    }



}