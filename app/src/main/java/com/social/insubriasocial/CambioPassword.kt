package com.social.insubriasocial

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

lateinit var btnBackCP: Button
lateinit var btnConfirmCP: Button
lateinit var passwordChanged: EditText
lateinit var oldpassword: EditText
lateinit var passwordChangedConfirm: EditText

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

        btnConfirmCP.setOnClickListener{
            if(passwordChanged.text.toString() != passwordChangedConfirm.text.toString()){
                Toast.makeText(this, "Le password non coincidono", Toast.LENGTH_SHORT).show()
            } else if (!isValidPassword(passwordChanged.text.toString())){
                Toast.makeText(this, "La password deve essere lunga almeno 8 caratteri", Toast.LENGTH_SHORT).show()
            } else {
                confrontaPasswordFirebase(oldpassword){ CorrectPassword ->
                    if(CorrectPassword){
                        Toast.makeText(this, "La password è stata cambiata correttamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "La password è sbagliata", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun confrontaPasswordFirebase(passwordEditText: EditText, onComplete: (Boolean) -> Unit) {
        val auth = FirebaseAuth.getInstance()

        val email = auth.currentUser?.email

        val passwordInserita = passwordEditText.text.toString()

        if (email != null && passwordInserita.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, passwordInserita)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete(true)
                    } else {
                        onComplete(false)
                    }
                }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
}