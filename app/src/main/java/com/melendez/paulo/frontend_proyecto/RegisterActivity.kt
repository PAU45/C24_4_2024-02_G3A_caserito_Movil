package com.melendez.paulo.frontend_proyecto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.melendez.paulo.frontend_proyecto.network.ApiClient
import com.melendez.paulo.frontend_proyecto.network.ApiService
import com.melendez.paulo.frontend_proyecto.network.SignUpRequest
import com.melendez.paulo.frontend_proyecto.network.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Configurar Retrofit usando getClient(context)
        apiService = ApiClient.getClient(this).create(ApiService::class.java)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etDireccion = findViewById<EditText>(R.id.etDireccion)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val tvLogin = findViewById<TextView>(R.id.textView2)

        btnSignUp.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()
            val email = etEmail.text.toString()
            val telefono = etTelefono.text.toString()
            val direccion = etDireccion.text.toString()

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val signUpRequest = SignUpRequest(username, password, email, telefono, direccion)

            apiService.signUp(signUpRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Registro exitoso. Redirigiendo a Promociones...", Toast.LENGTH_SHORT).show()

                        // Redirigir al usuario a la pantalla de promociones
                        val intent = Intent(this@RegisterActivity, FormularioActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("Register", "Fallo en el registro. Código: ${response.code()}")
                        Toast.makeText(this@RegisterActivity, "Fallo en el registro. Código: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("Register", "Error de red: ${t.message}")
                    Toast.makeText(this@RegisterActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)  // Asegúrate de usar el nombre correcto de la clase
            startActivity(intent)
            finish()
        }
    }
}

