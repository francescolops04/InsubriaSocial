package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

lateinit var btnBachecaSe: ImageView
lateinit var btnChatSe: ImageView
lateinit var btnSearchSe: ImageView
lateinit var btnProfileSe: ImageView
lateinit var btnSettingsSe: ImageView

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
}