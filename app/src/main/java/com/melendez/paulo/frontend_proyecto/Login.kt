package com.melendez.paulo.frontend_proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referenciar vistas del diseño
        val etUsername = findViewById<EditText>(R.id.etnombre)
        val etPassword = findViewById<EditText>(R.id.etcontrasena)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Credenciales simuladas
        val correctUsername = "admin"
        val correctPassword = "1234"

        // Manejar evento de clic en el botón Login
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim() // Remover espacios
            val password = etPassword.text.toString()

            when {
                username.isEmpty() -> {
                    Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show()
                }
                username == correctUsername && password == correctPassword -> {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    // Redirigir a otra actividad
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

}