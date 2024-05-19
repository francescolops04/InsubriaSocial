package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class RecoveryPassword : AppCompatActivity() {

    private lateinit var btnback: Button
    private lateinit var resetbtn: Button
    private lateinit var mailRec: EditText
    private lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery_password)

        btnback = findViewById<Button>(R.id.backRec)
        resetbtn = findViewById<Button>(R.id.buttonRec)
        mailRec = findViewById<EditText>(R.id.mailRec)
        auth = FirebaseAuth.getInstance()

        btnback.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        resetbtn.setOnClickListener {
            val mailRecText = mailRec.text.toString()
            recPassword(mailRecText)
        }
    }

    // Metodo per il recupero della password
    private fun recPassword(email:String){
        // Verifica se l'email è valida
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Il formato dell'email non è valido", Toast.LENGTH_SHORT).show()
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