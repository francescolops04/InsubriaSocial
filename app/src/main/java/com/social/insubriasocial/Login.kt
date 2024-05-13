package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogin: Button
    private lateinit var btnback:Button
    private lateinit var Email:EditText
    private lateinit var Password:EditText
    private lateinit var linkRec:TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        btnLogin = findViewById<Button>(R.id.LogButton)
        btnback = findViewById<Button>(R.id.buttonBackL)
        Email = findViewById<EditText>(R.id.mailL)
        Password = findViewById<EditText>(R.id.passwordL)
        linkRec = findViewById<TextView>(R.id.linkRec)

        linkRec.setOnClickListener{
            val intent = Intent(this, RecoveryPassword::class.java)
            startActivity(intent)
            finish()
        }

        btnback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            val email = Email.text.toString()
            val password = Password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Inserire email e password correttamente", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun loginUser(email: String, password: String) {
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Formato e-mail non valido", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "La password deve essere lunga almeno 8 caratteri", Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login avvenuto con successo", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Profilo::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Autenticazione fallita", Toast.LENGTH_SHORT).show()
                }
            }
    }




    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
}