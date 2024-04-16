package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth


lateinit var btnBack: Button
lateinit var btnProfile: Button
lateinit var btnPassword: Button
lateinit var btnLogout: Button

class Settings : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        btnBack = findViewById<Button>(R.id.buttonBackSetting)
        btnProfile= findViewById<Button>(R.id.buttonProfileSetting)
        btnPassword = findViewById<Button>(R.id.buttonPasswordSettings)
        btnLogout = findViewById<Button>(R.id.buttonLogoutSettings)

        btnBack.setOnClickListener {
            val intent = Intent(this, Profilo::class.java)
            startActivity(intent)
            finish()
        }

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
            auth!!.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }




    }
}