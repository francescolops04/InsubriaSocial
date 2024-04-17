package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth


lateinit var btnProfile: Button
lateinit var btnPassword: Button
lateinit var btnLogout: Button
lateinit var btnProfileBack: ImageView

class Settings : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        btnProfile= findViewById<Button>(R.id.buttonProfileSetting)
        btnPassword = findViewById<Button>(R.id.buttonPasswordSettings)
        btnLogout = findViewById<Button>(R.id.buttonLogoutSettings)
        btnProfileBack = findViewById<ImageView>(R.id.ProfileS)


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

        btnProfileBack.setOnClickListener{
            val intent = Intent(this, Profilo::class.java)
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