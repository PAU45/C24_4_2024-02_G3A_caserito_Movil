package com.melendez.paulo.frontend_proyecto.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Recuperar el token desde SharedPreferences o almacenamiento seguro
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token.isNullOrEmpty()) {
            throw UnauthorizedException("Token no disponible.")
        }

        // Añadir el token a las cabeceras de la solicitud
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        // Continuar con la solicitud
        return chain.proceed(request)
    }
}

// Excepción personalizada para manejo de error de autenticación
class UnauthorizedException(message: String) : Exception(message)
