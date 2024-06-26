package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


private lateinit var btnProfile: Button
private lateinit var btnPassword: Button
private lateinit var btnLogout: Button
private lateinit var btnChatS: ImageView
private lateinit var btnProfileBack: ImageView
private lateinit var btnBachecaS: ImageView
private lateinit var btnSearchS: ImageView

class Settings : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        btnProfile= findViewById<Button>(R.id.buttonProfileSetting)
        btnPassword = findViewById<Button>(R.id.buttonPasswordSettings)
        btnLogout = findViewById<Button>(R.id.buttonLogoutSettings)
        btnProfileBack = findViewById<ImageView>(R.id.ProfileS)
        btnBachecaS = findViewById<ImageView>(R.id.BachecaS)
        btnSearchS = findViewById<ImageView>(R.id.SearchS)
        btnChatS = findViewById<ImageView>(R.id.ChatS)


        btnProfile.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
            finish()
        }

        btnPassword.setOnClickListener {
            val intent = Intent(this, CambioPassword::class.java)
            startActivity(intent)
            finish()
        }

        btnLogout.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth!!.signOut() // Chiama il metodo signOut() sull'istanza di FirebaseAuth per disconnettere l'utente corrente
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnProfileBack.setOnClickListener{
            val intent = Intent(this, Profilo::class.java)
            startActivity(intent)
            finish()
        }

        btnBachecaS.setOnClickListener {
            val intent = Intent(this, Bacheca::class.java)
            startActivity(intent)
            finish()
        }

        btnSearchS.setOnClickListener {
            val intent = Intent(this, SistemaDiRicerca::class.java)
            startActivity(intent)
            finish()
        }

        btnChatS.setOnClickListener {
            val intent = Intent(this, ChatList::class.java)
            startActivity(intent)
            finish()
        }







    }
}