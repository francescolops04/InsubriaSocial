package com.social.insubriasocial

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

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
    }
}