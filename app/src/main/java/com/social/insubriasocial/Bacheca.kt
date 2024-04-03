package com.social.insubriasocial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AlertDialog
import com.social.insubriasocial.Announcement

class Bacheca : AppCompatActivity() {

    private var selectedAnnouncement: Announcement? = null
    private lateinit var buttonAggiuntaAnnuncio: Button
    private lateinit var intent: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bacheca)


        buttonAggiuntaAnnuncio = findViewById<Button>(R.id.buttonAggiuntaAnnuncio)

        buttonAggiuntaAnnuncio.setOnClickListener {
            intent = Intent(this, AddAnnouncementActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setMessage("Sei sicuro di voler eliminare questo annuncio?")
            .setPositiveButton("Sì") { _, _ ->
                removeSelectedAnnouncement()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun removeSelectedAnnouncement() {
        selectedAnnouncement = null
        Toast.makeText(this, "Annuncio rimosso con successo", Toast.LENGTH_SHORT).show()

    }
}