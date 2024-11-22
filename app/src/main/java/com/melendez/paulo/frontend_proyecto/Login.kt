package com.melendez.paulo.frontend_proyecto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.melendez.paulo.frontend_proyecto.network.ApiClient
import com.melendez.paulo.frontend_proyecto.network.ApiService
import com.melendez.paulo.frontend_proyecto.network.LoginRequest
import com.melendez.paulo.frontend_proyecto.network.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Configurar Retrofit usando getClient(context)
        apiService = ApiClient.getClient(this).create(ApiService::class.java)

        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.login_button)

        btnLogin.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val password = etPassword.text.toString()

            if (usuario.isEmpty()) {
                Toast.makeText(this, "El usuario no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(usuario, password)

            apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        val token = loginResponse?.token
                        if (token != null) {
                            // Guardar el token en SharedPreferences
                            val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("access_token", token)
                            editor.apply()

                            Toast.makeText(this@Login, "Login exitoso. Token: $token", Toast.LENGTH_SHORT).show()

                            // Redirigir al usuario a la pantalla principal
                            val intent = Intent(this@Login, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@Login, "Token no encontrado en la respuesta", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("Login", "Credenciales inválidas. Código: ${response.code()}")
                        Toast.makeText(this@Login, "Credenciales inválidas. Código: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("Login", "Error de red: ${t.message}")
                    Toast.makeText(this@Login, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
