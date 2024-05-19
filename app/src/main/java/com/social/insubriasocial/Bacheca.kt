package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.ArrayList

private lateinit var btnAdding: Button
private lateinit var btnRemove: Button
private lateinit var btnRefresh: Button
private lateinit var btnSetting: ImageView
private lateinit var btnProfilo: ImageView
private lateinit var btnRicerca: ImageView
private lateinit var btnChatB: ImageView
private lateinit var btnBacheca: ImageView
private lateinit var btnSearch: ImageView
private lateinit var announcementList: ListView
private lateinit var adapter: ArrayAdapter<String>


class Bacheca : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bacheca)

        btnAdding = findViewById<Button>(R.id.AddingB)
        btnRemove = findViewById<Button>(R.id.RemoveB)
        btnRefresh = findViewById<Button>(R.id.btnRefresh)
        btnSetting = findViewById<ImageView>(R.id.settingsB)
        btnProfilo = findViewById<ImageView>(R.id.ProfileB)
        btnRicerca = findViewById<ImageView>(R.id.SearchB)
        btnChatB = findViewById<ImageView>(R.id.ChatB)
        btnBacheca = findViewById<ImageView>(R.id.BachecaB)
        btnSearch = findViewById<ImageView>(R.id.SearchB)

        announcementList = findViewById<ListView>(R.id.announcementList)
        adapter = CustomListAdapter(this, ArrayList())
        announcementList.adapter = adapter

        loadAnnouncement()
        checkButtons()

        announcementList.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            val username = selectedItem.split("\n")[0]
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("usernameMaps", username)
            startActivity(intent)
        }

        btnRemove.setOnClickListener {
            val AlertDialog = AlertDialog.Builder(this@Bacheca)
            AlertDialog.setMessage("Sei sicuro di voler cancellare l'annuncio?")
            AlertDialog.setPositiveButton("Si"){_,_ ->
                deleteAnnouncement()
                val intent = Intent(this, Bacheca::class.java)
                startActivity(intent)
                finish()
            }
            AlertDialog.setNegativeButton("No"){_,_ ->
            }

            val AlertDialogBox = AlertDialog.create()
            AlertDialogBox.show()
        }

        btnAdding.setOnClickListener {
            val intent = Intent(this, AddAnnouncement::class.java)
            startActivity(intent)
            finish()
        }

        btnSetting.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }

        btnProfilo.setOnClickListener {
            val intent = Intent(this, Profilo::class.java)
            startActivity(intent)
            finish()
        }

        btnRefresh.setOnClickListener {
            val intent = Intent(this, Bacheca::class.java)
            startActivity(intent)
            finish()
        }

        btnSearch.setOnClickListener {
            val intent = Intent(this, SistemaDiRicerca::class.java)
            startActivity(intent)
            finish()
        }

        btnChatB.setOnClickListener {
            val intent = Intent(this, ChatList::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Verifica lo stato dei pulsanti in base ai dati utente
    private fun checkButtons() {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (currentUserID != null) {
            db.collection("utenti")
                .document(currentUserID)
                .get()
                .addOnSuccessListener { document ->
                    // Verifica se il documento esiste nel database Firestore
                    if (document != null) {
                        val title = document.getString("Titolo")
                        val desc = document.getString("Descrizione")

                        // Se sia il titolo che la descrizione non sono vuoti,
                        //rende visibile il pulsante di rimozione e cambia il testo del pulsante di aggiunta in "Modifica"
                        if (!title.isNullOrEmpty() && !desc.isNullOrEmpty()){
                            btnRemove.visibility = View.VISIBLE
                            btnAdding.text = "Modifica"
                        } else {
                            btnRemove.visibility = View.GONE
                        }
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

    // Carica gli annunci dal database
    private fun loadAnnouncement() {
        val db = FirebaseFirestore.getInstance()

        db.collection("utenti")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val announcementList = ArrayList<String>()
                for (document in result) {
                    val title = document.getString("Titolo")
                    val description = document.getString("Descrizione")
                    val user = document.getString("username")

                    // Se i campi "Titolo", "Descrizione" e "username" non sono nulli,
                    // crea una stringa formattata che combina il nome utente, il titolo e la descrizione dell'annuncio,
                    if (title != null && description != null && user != null) {
                        val announcement = "$user\n$title\n$description"
                        announcementList.add(announcement)
                    }
                }
                adapter.clear()
                adapter.addAll(announcementList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Errore durante il caricamento degli annunci",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // Elimina l'annuncio dell'utente corrente
    private fun deleteAnnouncement(){
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser


        if(user!=null){
            // Crea una mappa (hashMap) con i campi dell'annuncio da cancellare
            val ann = hashMapOf<String, Any>(
                "Titolo" to FieldValue.delete(),
                "Descrizione" to FieldValue.delete(),
                "timestamp" to FieldValue.delete(),
                "latitudine" to FieldValue.delete(),
                "longitudine" to FieldValue.delete()
                )

            db.collection("utenti").document(user.uid)
                .update(ann)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Annuncio cancellato con successo", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Cancellazione dei dati dell'utente non riuscita: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }
    }
}
