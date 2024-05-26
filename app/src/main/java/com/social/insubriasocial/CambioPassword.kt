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

private lateinit var btnBackCP: Button
private lateinit var btnConfirmCP: Button
private lateinit var passwordChanged: EditText
private lateinit var oldpassword: EditText
private lateinit var passwordChangedConfirm: EditText
private lateinit var linkRecEP: TextView

class CambioPassword : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambio_password)

        btnBackCP = findViewById<Button>(R.id.buttonBackCambioPassword)
        btnConfirmCP = findViewById<Button>(R.id.buttonConfirmCambioPassword)
        oldpassword = findViewById<EditText>(R.id.PasswordAttualeCP)
        passwordChanged = findViewById<EditText>(R.id.NuovaPasswordCP)
        passwordChangedConfirm = findViewById<EditText>(R.id.ConfermaPasswordCP)
        linkRecEP = findViewById<TextView>(R.id.linkRecEP)

        btnBackCP.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }

        btnConfirmCP.setOnClickListener{
            //Controlla se le due password non corrispondono e in questo caso mostra un Toast con un messaggio di errore
            if(passwordChanged.text.toString() != passwordChangedConfirm.text.toString()){
                Toast.makeText(this, "Le password non coincidono", Toast.LENGTH_SHORT).show()
            } else if (!isValidPassword(passwordChanged.text.toString())){
                //Altrimenti se non è lunga almeno 8 caratteri mostra un Toast con un messaggio di avviso
                Toast.makeText(this, "La password deve essere lunga almeno 8 caratteri", Toast.LENGTH_SHORT).show()
            } else {
                // Se le password coincidono e la nuova password è valida, procede con la verifica della password attuale
                confrontaPasswordFirebase(oldpassword){ CorrectPassword ->
                    if(CorrectPassword && (passwordChanged.text.toString() != oldpassword.text.toString())){
                        changePassword(passwordChanged.text.toString())
                    } else if (CorrectPassword && (passwordChanged.text.toString() == oldpassword.text.toString())){
                        Toast.makeText(this, "La password non può essere uguale a quella precedente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "La password è errata", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        linkRecEP.setOnClickListener{
            val intent = Intent(this, RecoveryPasswordEP::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Confronta la password fornita dall'utente con quella memorizzata nel database Firebase
    private fun confrontaPasswordFirebase(passwordEditText: EditText, onComplete: (Boolean) -> Unit) {
        val auth = FirebaseAuth.getInstance()

        val email = auth.currentUser?.email

        val passwordInserita = passwordEditText.text.toString()

        // Verifica se l'email è valida e la password inserita non è vuota
        if (email != null && passwordInserita.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, passwordInserita)
                .addOnCompleteListener { task ->
                    // Se l'autenticazione è riuscita, chiama la callback con il valore true
                    if (task.isSuccessful) {
                        onComplete(true)
                    } else {
                        onComplete(false)
                    }
                }
        }
    }

    // Verifica se la password soddisfa i requisiti minimi di lunghezza
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    // Cambia la password dell'utente nel database Firebase
    private fun changePassword (newpassword: String){
        val user = FirebaseAuth.getInstance().currentUser
        user?.updatePassword(newpassword)
            ?.addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "La password è stata modificata correttamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }
}