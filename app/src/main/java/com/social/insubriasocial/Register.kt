package com.social.insubriasocial

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
import com.google.firebase.firestore.FirebaseFirestore


private lateinit var auth: FirebaseAuth
private lateinit var firestore: FirebaseFirestore


class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val btnback = findViewById<Button>(R.id.buttonBackR)
        val btnRegister = findViewById<Button>(R.id.RegButton)
        val Email = findViewById<EditText>(R.id.mailR)
        val Password = findViewById<EditText>(R.id.passwordR)
        val User = findViewById<EditText>(R.id.usernameR)



        btnback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnRegister.setOnClickListener{
            val mail = Email.text.toString()
            val pass = Password.text.toString()
            val user = User.text.toString()

            if (mail.isNotEmpty() && pass.isNotEmpty()) {
                registerUser(mail, pass, user)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun registerUser(email: String, password: String, username: String) {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Inserire mail valida", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "La password deve essere lunga almeno 8 caratteri", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    Toast.makeText(this, "Registrazione completata", Toast.LENGTH_SHORT).show()

                    // Save user data to Firestore
                    val user = auth.currentUser
                    if (user != null) {
                        val userData = hashMapOf(
                            "email" to email,
                            "username" to username,
                            "password" to password
                        )
                        firestore.collection("utenti").document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                val intent = Intent(this, CreazioneProfilo::class.java)
                                this.startActivity(intent)
                                this.finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }

                } else {
                    Toast.makeText(this, "Email già in uso", Toast.LENGTH_SHORT).show()
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