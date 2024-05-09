package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


lateinit var btnSettingChat: ImageView
lateinit var btnProfiloChat: ImageView
lateinit var btnRicercaChat: ImageView
lateinit var btnBachecaChat: ImageView
lateinit var Chatlist: ListView
lateinit var adapterChatlist: ArrayAdapter<String>

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

    private fun loadChatList() {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("chats")
            .whereEqualTo("user1", currentUserID)
            .get()
            .addOnSuccessListener { documents ->
                val searchListChat = ArrayList<String>()
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

    }
}