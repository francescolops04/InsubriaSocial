package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class RecoveryPasswordEP : AppCompatActivity() {

    private lateinit var btnbackEP: Button
    private lateinit var resetbtnEP: Button
    private lateinit var mailRecEP: EditText
    private lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery_password_ep)

        btnbackEP = findViewById<Button>(R.id.backRecEP)
        resetbtnEP = findViewById<Button>(R.id.buttonRecEP)
        mailRecEP = findViewById<EditText>(R.id.mailRecEP)
        auth = FirebaseAuth.getInstance()

        btnbackEP.setOnClickListener {
            val intent = Intent(this, CambioPassword::class.java)
            startActivity(intent)
            finish()
        }

        resetbtnEP.setOnClickListener {
            val mailRecText = mailRecEP.text.toString()
            recPassword(mailRecText)
        }
    }

    // Metodo per il recupero della password
    private fun recPassword(email:String){
        // Verifica se l'email è valida
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Il formato dell'e-mail non è valido", Toast.LENGTH_SHORT).show()
            return
        }
        // Invio dell'email per il recupero della password
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Abbiamo mandato una mail di recupero password", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Metodo per la validazione dell'email
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}