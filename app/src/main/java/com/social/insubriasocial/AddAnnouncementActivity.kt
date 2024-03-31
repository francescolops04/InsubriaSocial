package com.social.insubriasocial

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddAnnouncementActivity : AppCompatActivity() {

    private lateinit var systemBars: WindowInsetsCompat

    init {
        systemBars = WindowInsetsCompat.Builder().build()
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_announcement)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            systemBars = insets
            v.setPadding(systemBars.systemWindowInsetLeft, systemBars.systemWindowInsetTop, systemBars.systemWindowInsetRight, systemBars.systemWindowInsetBottom)
            insets
        }
    }
}