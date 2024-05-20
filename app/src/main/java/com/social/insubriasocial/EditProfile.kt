package com.social.insubriasocial

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var btnBackEP: Button
private lateinit var btnConfirm: Button
private lateinit var nameChanged: EditText
private lateinit var lastnameChanged: EditText
private lateinit var userChanged: EditText
private lateinit var FacultyChanged: Spinner


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

        // Carica il profilo dell'utente
        userProfile()

        // Configura il controllo della visibilità del pulsante di conferma in base alle modifiche
        updateButtonVisible()




        btnBackEP.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }
        btnConfirm.setOnClickListener {
            // Verifica se lo username inserito è valido prima di procedere
            if(!isValidUsername(userChanged.text.toString())){
                Toast.makeText(this, "Lo username deve contenere almeno 6 caratteri", Toast.LENGTH_SHORT).show()
            }else{
                // Raccoglie i nuovi dati inseriti e li aggiorna nel database
                collectNewData(nameChanged, lastnameChanged, userChanged, FacultyChanged)
            }
        }

    }

    // Funzione per controllare e aggiornare la visibilità del pulsante in base alle modifiche nel campo del nome, cognome e username
    private fun updateButtonVisible(){
        nameChanged.doAfterTextChanged { editable ->
            checkNameAndUpdateButton(editable.toString())
        }

        lastnameChanged.doAfterTextChanged { editable ->
            checkLastNameAndUpdateButton(editable.toString())
        }

        userChanged.doAfterTextChanged { editable ->
            checkUserNameAndUpdateButton(editable.toString())
        }

        FacultyChanged.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                checkFacultyAndUpdateButton(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    //Questo metodo controlla se ci sono stati cambiamenti nel campo della facoltà e aggiorna
    //la visibilità del pulsante di conferma delle modifiche nel profilo
    private fun checkFacultyAndUpdateButton(currentFaculty: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (currentUserID != null) {
            db.collection("utenti")
                .document(currentUserID)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val nameDb = document.getString("nome") ?: ""
                        val lastnameDb = document.getString("cognome") ?: ""
                        val facultyDb = document.getString("facoltà") ?: ""
                        val usernameDb = document.getString("username") ?: ""
                        btnConfirm.visibility = if (facultyDb != currentFaculty || lastnameDb != lastnameChanged.text.toString() || usernameDb != userChanged.text.toString() || nameDb != nameChanged.text.toString()) View.VISIBLE else View.GONE
                    } else {
                        Toast.makeText(this, "Nessun documento trovato", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Errore durante il recupero dei dati: $exception", Toast.LENGTH_SHORT).show()
                }
        }
    }

    //Questo metodo controlla se ci sono stati cambiamenti nel campo del nome e aggiorna
    //la visibilità del pulsante di conferma delle modifiche nel profilo
    private fun checkNameAndUpdateButton(currentName: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        // Verifica se l'ID dell'utente non è nullo.
        if (currentUserID != null) {
            db.collection("utenti")
                .document(currentUserID)
                .get()
                .addOnSuccessListener { document ->
                    // Se il documento esiste, estrae il nome, cognome, facoltà e username dall'oggetto documento
                    if (document != null) {
                        val nameDb = document.getString("nome") ?: ""
                        val lastnameDb = document.getString("cognome") ?: ""
                        val facultyDb = document.getString("facoltà") ?: ""
                        val usernameDb = document.getString("username") ?: ""

                        btnConfirm.visibility = if (nameDb != currentName || lastnameDb != lastnameChanged.text.toString() || facultyDb != FacultyChanged.selectedItem.toString() || usernameDb != userChanged.text.toString()) View.VISIBLE else View.GONE
                    } else {
                        // Se il documento non esiste, mostra un Toast dicendo che nessun documento è stato trovato
                        Toast.makeText(this, "Nessun documento trovato", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Errore durante il recupero dei dati: $exception", Toast.LENGTH_SHORT).show()
                }
        }
    }

    //Questo metodo controlla se ci sono stati cambiamenti nel campo del cognome e aggiorna
    //la visibilità del pulsante di conferma delle modifiche nel profilo
    private fun checkLastNameAndUpdateButton(currentLastName: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        // Verifica se l'ID dell'utente non è nullo
        if (currentUserID != null) {
            db.collection("utenti")
                .document(currentUserID)
                .get()
                .addOnSuccessListener { document ->
                    // Se il documento esiste, estrae il nome, cognome, facoltà e lo username dall'oggetto documento
                    if (document != null) {
                        val nameDb = document.getString("nome") ?: ""
                        val lastnameDb = document.getString("cognome") ?: ""
                        val facultyDb = document.getString("facoltà") ?: ""
                        val usernameDb = document.getString("username") ?: ""


                        btnConfirm.visibility = if (lastnameDb != currentLastName || nameDb != nameChanged.text.toString() || facultyDb != FacultyChanged.selectedItem.toString() || usernameDb != userChanged.text.toString()) View.VISIBLE else View.GONE
                    } else {
                        Toast.makeText(this, "Nessun documento trovato", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Errore durante il recupero dei dati: $exception", Toast.LENGTH_SHORT).show()
                }
        }
    }

    //Questo metodo controlla se ci sono stati cambiamenti nel campo dello username e aggiorna
    //la visibilità del pulsante di conferma delle modifiche nel profilo.
    private fun checkUserNameAndUpdateButton(currentUsername: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (currentUserID != null) {
            db.collection("utenti")
                .document(currentUserID)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val nameDb = document.getString("nome") ?: ""
                        val lastnameDb = document.getString("cognome") ?: ""
                        val facultyDb = document.getString("facoltà") ?: ""
                        val usernameDb = document.getString("username") ?: ""
                        btnConfirm.visibility = if (usernameDb != currentUsername || lastnameDb != lastnameChanged.text.toString() || facultyDb != FacultyChanged.selectedItem.toString() || nameDb != nameChanged.text.toString()) View.VISIBLE else View.GONE
                    } else {
                        Toast.makeText(this, "Nessun documento trovato", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Errore durante il recupero dei dati: $exception", Toast.LENGTH_SHORT).show()
                }
        }
    }





    //Questo metodo recupera e visualizza i dettagli del profilo dell'utente corrente
    private fun userProfile() {
            val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
            val db = FirebaseFirestore.getInstance()
            if (currentUserID != null) {
                // Recupera i dati del profilo dell'utente corrente dal database Firestore
            db.collection("utenti")
                .document(currentUserID)
                .get()
                .addOnSuccessListener { document ->
                    // Se il documento esiste, estrae i valori relativi a username, nome, cognome e facoltà
                    if (document != null) {

                        val user = document.getString("username")
                        val name = document.getString("nome")
                        val lastname = document.getString("cognome")
                        val faculty = document.getString("facoltà")

                        // Imposta il testo negli elementi EditText dell'interfaccia utente
                        // corrispondenti al nome, al cognome e all'username utilizzando i valori estratti dal database
                        val editableUser = SpannableStringBuilder(user ?: "")
                        userChanged.text = editableUser

                        val editableName = SpannableStringBuilder(name ?: "")
                        nameChanged.text = editableName

                        val editableLastname = SpannableStringBuilder(lastname ?: "")
                        lastnameChanged.text = editableLastname

                        // Ottiene l'array delle opzioni per la facoltà dalle risorse e seleziona l'opzione corrispondente
                        // nella Spinner relativa alla facoltà utilizzando l'indice della facoltà estratta dal database
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

    //Questo metodo raccoglie i nuovi dati inseriti dall'utente per il proprio profilo e li aggiorna nel database Firestore
    private fun collectNewData(name: EditText, lastname: EditText, username: EditText, faculty: Spinner) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val user = auth.currentUser
        val nameText = name.text.toString()
        val lastnameText = lastname.text.toString()
        val usernameText = username.text.toString()
        val facultyText = faculty.selectedItem.toString()

        // Verifica se l'utente è autenticato
        if (user != null) {
            // Recupera i dati del profilo dell'utente dal database Firestore
            db.collection("utenti")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    val oldUsername = document.getString("username")

                    // Verifica se il nuovo username è diverso dal vecchio username.
                    if (oldUsername != usernameText) {
                        db.collection("utenti")
                            .whereEqualTo("username", usernameText)
                            .get()
                            .addOnSuccessListener { documents ->
                                // Se ci sono documenti restituiti dalla query, significa che il nuovo username è già in uso
                                if (!documents.isEmpty) {
                                    Toast.makeText(this, "Username già in uso", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Se il nuovo username è uguale al vecchio, aggiorna direttamente i dati del profilo senza eseguire la verifica
                                    val desc = hashMapOf<String, Any>(
                                        "username" to usernameText,
                                        "nome" to nameText,
                                        "cognome" to lastnameText,
                                        "facoltà" to facultyText
                                    )

                                    db.collection("utenti").document(user.uid)
                                        .update(desc)
                                        .addOnSuccessListener {
                                            val intent = Intent(this, Profilo::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                this,
                                                "Impossibile salvare i dati dell'utente: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            }
                    } else {
                        // Se il nuovo username è uguale al vecchio
                        // aggiorna direttamente i dati del profilo senza eseguire la verifica
                        val desc = hashMapOf<String,Any>(
                            "username" to usernameText,
                            "nome" to nameText,
                            "cognome" to lastnameText,
                            "facoltà" to facultyText
                        )

                        db.collection("utenti").document(user.uid)
                            .update(desc)
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

    //Verifica se lo username fornito ha almeno 6 caratteri.
    private fun isValidUsername(user: String): Boolean {
        return user.length >= 6
    }


}