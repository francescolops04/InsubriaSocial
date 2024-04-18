package com.social.insubriasocial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

lateinit var btnBack: Button
lateinit var btnAccept: Button

class AddAnnouncement : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_announcement)


        btnBack = findViewById<Button>(R.id.buttonBackA)
        btnAccept = findViewById<Button>(R.id.buttonAcceptA)

        btnBack.setOnClickListener {
            val intent = Intent(this, Bacheca::class.java)
            startActivity(intent)
            finish()
        }

        btnAccept.setOnClickListener {
            val intent = Intent(this, Bacheca::class.java)
            startActivity(intent)
            finish()
        }
    }
}