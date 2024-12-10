package com.melendez.paulo.frontend_proyecto.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://apiprueba-womd.onrender.com/"

    private fun getDefaultClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    // Método para obtener Retrofit con un cliente personalizado (OkHttpClient)
    fun getClientWithCustomClient(context: Context, customClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(customClient)  // Usamos el OkHttpClient personalizado
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Método para obtener Retrofit con el cliente predeterminado (con logging)
    fun getClient(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getDefaultClient())  // Usamos el OkHttpClient con logging
            .build()
    }
}
