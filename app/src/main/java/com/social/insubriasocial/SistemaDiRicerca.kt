package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

lateinit var btnBachecaSe: ImageView
lateinit var btnChatSe: ImageView
lateinit var btnSearchSe: ImageView
lateinit var btnProfileSe: ImageView
lateinit var btnSettingsSe: ImageView
lateinit var adapterSearch: ArrayAdapter<String>
lateinit var searchList: ListView
lateinit var searchText: EditText
lateinit var spinnerFilter: Spinner
lateinit var btnRefreshSP : Button

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
        spinnerFilter = findViewById<Spinner>(R.id.spinnerSR)
        btnRefreshSP = findViewById<Button>(R.id.btnRefreshSP)

        searchList = findViewById<ListView>(R.id.list_user)
        adapterSearch = UserSearchAdapter(this, ArrayList())
        searchList.adapter = adapterSearch



        btnChatSe.setOnClickListener {
            val intent = Intent(this, ChatList::class.java)
            startActivity(intent)
            finish()
        }

        btnRefreshSP.setOnClickListener {
            val intent = Intent(this, SistemaDiRicerca::class.java)
            startActivity(intent)
            finish()
        }

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (!selectedItem.equals("Seleziona la facoltà")) {
                    searchText.doAfterTextChanged { editable ->
                        searchUserFilter(editable.toString(), selectedItem)
                    }
                } else {
                    searchText.doAfterTextChanged { editable ->
                        searchUser(editable.toString())
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        searchList.setOnItemClickListener { parent, view, position, id ->
            val selectedText = parent.getItemAtPosition(position) as String
            val username = selectedText.substringBefore("\n")
            val i: Intent = Intent(this, UsersProfile::class.java)
            i.putExtra("user", username)
            startActivity(i)
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
                    val username = document.getString("username")
                    if (nome != null && cognome != null && username != null) {
                        if (username.lowercase().contains(user.trim().lowercase()) || nome.lowercase().contains(user.trim().lowercase()) || cognome.lowercase().contains(user.trim().lowercase())) {
                            val searchitemlist = "$username\n$nome $cognome"
                            searchList.add(searchitemlist)
                        }
                    }
                }
                adapterSearch.clear()
                adapterSearch.addAll(searchList)
            }
    }

    private fun searchUserFilter(user: String, filter: String) {
        val db = FirebaseFirestore.getInstance()


        db.collection("utenti")
            .get()
            .addOnSuccessListener { result ->
                val searchList = ArrayList<String>()
                for (document in result) {
                    val nome = document.getString("nome")
                    val cognome = document.getString("cognome")
                    val username = document.getString("username")
                    val facoltà = document.getString("facoltà")
                    if (nome != null && cognome != null && username != null && facoltà != null) {
                        if ((username.lowercase().contains(user.trim().lowercase()) || nome.lowercase().contains(user.trim().lowercase()) || cognome.lowercase().contains(user.trim().lowercase())) && filter == facoltà) {
                            val searchitemlist = "$username\n$nome $cognome"
                            searchList.add(searchitemlist)
                        }
                    }
                }
                adapterSearch.clear()
                adapterSearch.addAll(searchList)
            }
    }
}