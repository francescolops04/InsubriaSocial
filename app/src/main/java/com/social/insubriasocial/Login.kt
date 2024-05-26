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
    private lateinit var btnLogin: Button
    private lateinit var btnback:Button
    private lateinit var Email:EditText
    private lateinit var Password:EditText
    private lateinit var linkRec:TextView
    private lateinit var auth: FirebaseAuth

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

        btnLogin.setOnClickListener {
            val email = Email.text.toString()
            val password = Password.text.toString()

            // Verifica che l'email e la password non siano vuote
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Inserire email e password correttamente", Toast.LENGTH_SHORT).show()
            }
        }

        btnback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        linkRec.setOnClickListener{
            val intent = Intent(this, RecoveryPassword::class.java)
            startActivity(intent)
            finish()
        }
    }


    // Funzione per eseguire il login dell'utente con l'email e la password fornite
    private fun loginUser(email: String, password: String) {
        // Verifica che l'email sia valida
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Formato email non valido", Toast.LENGTH_SHORT).show()
            return
        }

        //Verifica che la password abbia almeno 8 caratteri
        if (!isValidPassword(password)) {
            Toast.makeText(this, "La password deve essere lunga almeno 8 caratteri", Toast.LENGTH_SHORT).show()
            return
        }

        //Login dell'utente con e-mail e password
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Se il login ha successo, avvia l'activity del profilo e chiudi questa activity
                    Toast.makeText(this, "Login avvenuto con successo", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Profilo::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Email e/o password errate", Toast.LENGTH_SHORT).show()
                }
            }
    }



    // Funzione per verificare se un'email è valida
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Funzione per verificare se una password è lunga almeno 8 caratteri
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
}