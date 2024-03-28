package com.social.insubriasocial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AlertDialog
import com.social.insubriasocial.Announcement

class Bacheca : AppCompatActivity() {

    private var selectedAnnouncement: Announcement? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bacheca)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonAggiuntaAnnuncio = findViewById<Button>(R.id.buttonAggiuntaAnnuncio)
        buttonAggiuntaAnnuncio.setOnClickListener {
            selectedAnnouncement?.let {
                showConfirmationDialog()
            } ?: run {
                Toast.makeText(this, "Seleziona un annuncio da rimuovere", Toast.LENGTH_SHORT)
                    .show()
            }

            val intent = Intent(this, AddAnnouncementActivity::class.java)
            startActivity(intent)
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