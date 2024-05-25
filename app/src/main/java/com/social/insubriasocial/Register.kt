package com.social.insubriasocial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Spinner

private lateinit var auth: FirebaseAuth
private lateinit var firestore: FirebaseFirestore
private lateinit var btnback: Button
private lateinit var btnRegister: Button
private lateinit var Email: EditText
private lateinit var Password: EditText
private lateinit var User: EditText
private lateinit var PasswordC: EditText
private lateinit var Nome: EditText
private lateinit var Cognome: EditText
private lateinit var Faculty: Spinner

class Register : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
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
        Nome = findViewById<EditText>(R.id.nomeR)
        Cognome = findViewById<EditText>(R.id.cognomeR)
        Faculty = findViewById<Spinner>(R.id.spinnerR)

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
            val selectedFaculty = Faculty.selectedItem.toString()
            val name = Nome.text.toString()
            val lastName = Cognome.text.toString()

            // Controlla se qualche campo non è vuoto
            if (!mail.isNotEmpty() && !pass.isNotEmpty()) {
                Toast.makeText(this, "Inserire email e password", Toast.LENGTH_SHORT).show()
            } else if (samePassword(pass, pass2)){
                Toast.makeText(this, "Le password non coincidono", Toast.LENGTH_SHORT).show()
            }else if(!user.isNotEmpty()){
                Toast.makeText(this, "Inserire username", Toast.LENGTH_SHORT).show()
            } else if(selectedFaculty.equals("Seleziona la facoltà")){
                Toast.makeText(this, "Non hai selezionato nessuna facoltà", Toast.LENGTH_SHORT).show()
            } else if(!name.isNotEmpty() && !lastName.isNotEmpty()){
                Toast.makeText(this, "Inserire nome e cognome", Toast.LENGTH_SHORT).show()
            }else{
                // Se tutti i campi sono stati compilati, registra l'utente
                registerUser(mail, pass, user, name, lastName, selectedFaculty)
            }
        }
    }

    // Metodo per registrare l'utente su Firebase
    private fun registerUser(email: String, password: String, username: String, name: String, lastName: String, selectedFaculty: String) {
        if (!isValidEmail(email)) {
            // Verifica se l'email è valida
            Toast.makeText(this, "Inserire mail valida", Toast.LENGTH_SHORT).show()
            return
        }

        if(!isValidPassword(password)){
            //Verifica se la password è valida
            Toast.makeText(this, "La password deve essere lunga almeno 8 caratteri", Toast.LENGTH_SHORT).show()
        }

        if (!isValidUsername(username)) {
            //Verifica se lo username è valido
            Toast.makeText(this, "Lo username deve essere lungo almeno 6 caratteri", Toast.LENGTH_SHORT).show()
            return
        }

        // Controlla se l'username è già in uso nel Firestore
        firestore.collection("utenti")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    Toast.makeText(this, "Username già in uso", Toast.LENGTH_SHORT).show()
                } else {
                    // Crea un nuovo utente con Firebase Authentication
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Registrazione completata", Toast.LENGTH_SHORT).show()

                                // Salva i dati dell'utente nel Firestore
                                val user = auth.currentUser
                                if (user != null) {
                                    val userData = hashMapOf(
                                        "email" to email,
                                        "username" to username,
                                        "nome" to name,
                                        "cognome" to lastName,
                                        "facoltà" to selectedFaculty
                                    )

                                    firestore.collection("utenti").document(user.uid)
                                        .set(userData)
                                        .addOnSuccessListener {
                                            val intent = Intent(this, Login::class.java)
                                            this.startActivity(intent)
                                            this.finish()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(this, "Impossibile salvare i dati dell'utente: ${e.message}", Toast.LENGTH_SHORT).show()
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

    //Metodo per verificare se è valida l'email
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //Metodo per verificare se la passowrd contiene almeno 8 caratteri
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    //Metodo per verificare se l'username ha almeno 6 caratteri
    private fun isValidUsername(user: String): Boolean {
        return user.length >= 6
    }

    //Metodo per vedere se le due password coincidono
    private fun samePassword(pass1: String, pass2: String):Boolean{
        return pass1 != pass2
    }

}