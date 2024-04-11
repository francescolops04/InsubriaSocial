package com.social.insubriasocial

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

lateinit var btnBackEP: Button
lateinit var btnConfirm: Button
lateinit var nameChanged: EditText
lateinit var lastnameChanged: EditText
lateinit var userChanged: EditText
lateinit var FacultyChanged: Spinner


class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        btnBackEP = findViewById<Button>(R.id.buttonBackEditProfile)
        btnConfirm = findViewById<Button>(R.id.buttonConfirmEditProfile)
        nameChanged = findViewById<EditText>(R.id.nomeEP)
        lastnameChanged = findViewById<EditText>(R.id.cognomeEP)
        userChanged = findViewById<EditText>(R.id.userEP)
        FacultyChanged = findViewById<Spinner>(R.id.spinnerEP)

        userProfile()


        btnBackEP.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }
        btnConfirm.setOnClickListener {
                collectNewData(nameChanged, lastnameChanged, userChanged, FacultyChanged)
        }

    }

    private fun userProfile() {
            val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
            val db = FirebaseFirestore.getInstance()
            if (currentUserID != null) {
            db.collection("utenti")
                .document(currentUserID)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        val user = document.getString("username")
                        val name = document.getString("nome")
                        val lastname = document.getString("cognome")
                        val faculty = document.getString("facoltà")

                        val editableUser = SpannableStringBuilder(user ?: "")
                        userChanged.text = editableUser

                        val editableName = SpannableStringBuilder(name ?: "")
                        nameChanged.text = editableName

                        val editableLastname = SpannableStringBuilder(lastname ?: "")
                        lastnameChanged.text = editableLastname

                        val facultiesArray = resources.getStringArray(R.array.opzioni_spinner_edit)
                        val facultyIndex = facultiesArray.indexOf(faculty)
                        FacultyChanged.setSelection(facultyIndex)


                    } else {
                        Toast.makeText(this, "Nessun documento trovato", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Errore durante il recupero dei dati: $exception",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }

    }


    private fun collectNewData(name: EditText, lastname: EditText, username: EditText, faculty: Spinner) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val user = auth.currentUser
        val nameText = name.text.toString()
        val lastnameText = lastname.text.toString()
        val usernameText = username.text.toString()
        val facultyText = faculty.selectedItem.toString()

        if (user != null) {
            db.collection("utenti")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    val oldUsername = document.getString("username")

                    if (oldUsername != usernameText) {
                        db.collection("utenti")
                            .whereEqualTo("username", usernameText)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    Toast.makeText(this, "Username già in uso", Toast.LENGTH_SHORT).show()
                                } else {
                                    val desc = hashMapOf(
                                        "username" to usernameText,
                                        "nome" to nameText,
                                        "cognome" to lastnameText,
                                        "facoltà" to facultyText
                                    )

                                    db.collection("utenti").document(user.uid)
                                        .set(desc)
                                        .addOnSuccessListener {
                                            val intent = Intent(this, Profilo::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                this,
                                                "Failed to save user data: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            }
                    } else {
                        val desc = hashMapOf(
                            "username" to usernameText,
                            "nome" to nameText,
                            "cognome" to lastnameText,
                            "facoltà" to facultyText
                        )

                        db.collection("utenti").document(user.uid)
                            .set(desc)
                            .addOnSuccessListener {
                                val intent = Intent(this, Profilo::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Failed to save user data: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
        }
    }




}