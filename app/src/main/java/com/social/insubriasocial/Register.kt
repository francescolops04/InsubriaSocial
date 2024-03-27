package com.social.insubriasocial

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
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
private lateinit var btnback: Button
private lateinit var btnRegister: Button
private lateinit var Email: EditText
private lateinit var Password: EditText
private lateinit var User: EditText
private lateinit var PasswordC: EditText

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        btnback = findViewById<Button>(R.id.buttonBackR)
        btnRegister = findViewById<Button>(R.id.RegButton)
        Email = findViewById<EditText>(R.id.mailR)
        Password = findViewById<EditText>(R.id.passwordR)
        User = findViewById<EditText>(R.id.usernameR)
        PasswordC = findViewById<EditText>(R.id.passwordR2)


        btnback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnRegister.setOnClickListener{
            val mail = Email.text.toString()
            val pass = Password.text.toString()
            val user = User.text.toString()
            val pass2 = PasswordC.text.toString()

            if (!mail.isNotEmpty() && !pass.isNotEmpty()) {
                Toast.makeText(this, "Inserire email e password", Toast.LENGTH_SHORT).show()
            } else if (samePassword(pass, pass2)){
                Toast.makeText(this, "Le password non coincidono", Toast.LENGTH_SHORT).show()
            }else if(!user.isNotEmpty()){
                Toast.makeText(this, "Inserire username", Toast.LENGTH_SHORT).show()
            }else{
                registerUser(mail, pass, user)
            }
        }

    }

    private fun registerUser(email: String, password: String, username: String) {

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Inserire mail valida", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "La password deve essere lunga almeno 8 caratteri", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("utenti")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    Toast.makeText(this, "Username già in uso", Toast.LENGTH_SHORT).show()
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Registrazione completata", Toast.LENGTH_SHORT).show()

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
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Errore durante il controllo dell'username: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    private fun samePassword(pass1: String, pass2: String):Boolean{
        return pass1 != pass2
    }

}