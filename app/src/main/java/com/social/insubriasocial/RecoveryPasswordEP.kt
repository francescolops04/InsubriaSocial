package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException


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

    private fun recPassword(email:String){
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            return
        }
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Abbiamo mandato una mail di recupero password", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}