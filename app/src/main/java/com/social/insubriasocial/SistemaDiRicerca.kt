package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

lateinit var btnBachecaSe: ImageView
lateinit var btnChatSe: ImageView
lateinit var btnSearchSe: ImageView
lateinit var btnProfileSe: ImageView
lateinit var btnSettingsSe: ImageView
lateinit var adapterSearch: ArrayAdapter<String>
lateinit var searchList: ListView
lateinit var searchText: EditText

class SistemaDiRicerca : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sistema_di_ricerca)

        btnBachecaSe = findViewById<ImageView>(R.id.BachecaSe)
        btnChatSe = findViewById<ImageView>(R.id.ChatSe)
        btnSearchSe = findViewById<ImageView>(R.id.SearchSe)
        btnProfileSe = findViewById<ImageView>(R.id.ProfileSe)
        btnSettingsSe = findViewById<ImageView>(R.id.SettingsSe)
        searchList = findViewById<ListView>(R.id.list_user)
        searchText = findViewById<EditText>(R.id.searchUser)


        searchList = findViewById<ListView>(R.id.list_user)
        adapterSearch = ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList())
        searchList.adapter = adapterSearch

        searchText.doAfterTextChanged { editable ->
            searchUser(editable.toString())
        }

        btnSettingsSe.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }

        btnProfileSe.setOnClickListener {
            val intent = Intent(this, Profilo::class.java)
            startActivity(intent)
            finish()
        }

        btnBachecaSe.setOnClickListener {
            val intent = Intent(this, Bacheca::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun searchUser(user: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("utenti")
            .get()
            .addOnSuccessListener { result ->
                val searchList = ArrayList<String>()
                for (document in result) {
                    val nome = document.getString("nome")
                    val cognome = document.getString("cognome")
                    val user = document.getString("username")
                    if (nome != null && cognome != null && user != null) {
                        val searchitemlist = "$user\n$nome$cognome"
                        searchList.add(searchitemlist)
                    }
                }
                adapterSearch.clear()
                adapterSearch.addAll(searchList)
            }
    }
}