package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList
import java.util.UUID

private lateinit var backbtnchat: Button
private lateinit var sendbtnchat: Button
private lateinit var btnRefreshC: Button
private lateinit var messagelist: ListView
private lateinit var adapterMessage: ArrayAdapter<String>
private lateinit var messageText: EditText
private lateinit var contactName: TextView
private lateinit var chatID: String

class Chat : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        backbtnchat = findViewById<Button>(R.id.backBtnChat)
        sendbtnchat = findViewById<Button>(R.id.sendBtn)
        btnRefreshC = findViewById<Button>(R.id.btnRefreshC)
        messageText = findViewById<EditText>(R.id.messageText)
        contactName = findViewById<TextView>(R.id.userContact)
        chatID = intent.getStringExtra("chatID") ?: ""
        contactName.text = intent.getStringExtra("contactUsername")


        messagelist = findViewById<ListView>(R.id.list_messages)
        adapterMessage = ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList())
        messagelist.adapter = adapterMessage

        mostraMessaggi()

        backbtnchat.setOnClickListener {
            val intent = Intent(this, ChatList::class.java)
            startActivity(intent)
            finish()
        }

        sendbtnchat.setOnClickListener {
            inviaMessaggio()
            mostraMessaggi()
        }

        btnRefreshC.setOnClickListener {
            mostraMessaggi()
        }
    }

    //Funzione per inviare un messaggio nella chat corrente
    private fun inviaMessaggio() {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val db = FirebaseFirestore.getInstance()
        val testoMessaggio = messageText.text.toString().trim()

        // Verifica se il testo del messaggio non è vuoto
        if (testoMessaggio.isNotEmpty()) {
            val messageID = UUID.randomUUID().toString()

            val chatRef = db.collection("chats").document(chatID)

            val messagesRef = chatRef.collection("messages").document(messageID)

            val messaggio = hashMapOf(
                "testo" to testoMessaggio,
                "mittente" to currentUserID,
                "timestamp" to System.currentTimeMillis()
            )

            messagesRef.set(messaggio)
                .addOnSuccessListener {
                    messageText.text.clear()
                }

        } else {
            Toast.makeText(this, "Inserire un messaggio", Toast.LENGTH_SHORT).show()
        }
    }

    // Funzione per caricare e visualizzare i messaggi della chat
    private fun mostraMessaggi() {
        val db = FirebaseFirestore.getInstance()
        val messagesRef = db.collection("chats").document(chatID).collection("messages")

        messagesRef.orderBy("timestamp").get()
            .addOnSuccessListener { snapshot ->
                val messaggi = ArrayList<String>()

                val messaggiNonOrdinati = ArrayList<Pair<Long, String>>()

                // Itera attraverso i documenti dei messaggi
                for (document in snapshot.documents) {
                    val messaggio = document.getString("testo")
                    val mittente = document.getString("mittente")
                    val timestamp = document.getLong("timestamp")

                    // Verifica se il messaggio, il mittente e il timestamp non sono nulli
                    if (messaggio != null && mittente != null && timestamp != null) {
                        db.collection("utenti").document(mittente).get()
                            .addOnSuccessListener { userSnapshot ->
                                val username = userSnapshot.getString("username")
                                if (username != null) {
                                    val formattedMessage = "$username: $messaggio"
                                    messaggiNonOrdinati.add(Pair(timestamp, formattedMessage))

                                    /* Se tutti i messaggi sono stati aggiunti alla lista temporanea,
                                    * ordina i messaggi per timestamp e aggiorna l'adapter della ListView */
                                    if (messaggiNonOrdinati.size == snapshot.size()) {
                                        messaggiNonOrdinati.sortBy { it.first }

                                        for (msg in messaggiNonOrdinati) {
                                            messaggi.add(msg.second)
                                        }

                                        adapterMessage.clear()
                                        adapterMessage.addAll(messaggi)
                                    }
                                }
                            }
                    }
                }
            }
    }







}