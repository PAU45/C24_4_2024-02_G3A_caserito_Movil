package com.melendez.paulo.frontend_proyecto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
        val username = sharedPreferences.getString("username", "Usuario")

        if (!isLoggedIn) {
            // Si no hay una sesión activa, redirigir al LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return  // Asegúrate de que la ejecución se detenga aquí si el usuario no está logueado
        }

        setContentView(R.layout.activity_main)

        val tvWelcome: TextView = findViewById(R.id.tvWelcome)
        val btnLogout: Button = findViewById(R.id.btnLogout)
        val btnSearch: Button = findViewById(R.id.btnSearch)
        val etSearch: EditText = findViewById(R.id.etSearch)

        tvWelcome.text = "Bienvenido, $username"

        btnLogout.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // Redirigir al LoginActivity después de cerrar sesión
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString()
            Toast.makeText(this, "Buscando: $query", Toast.LENGTH_SHORT).show()
        }
    }
}
