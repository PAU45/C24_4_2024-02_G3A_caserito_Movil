package com.melendez.paulo.frontend_proyecto.network

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

object ApiClient {
    private const val BASE_URL = "https://apiprueba-womd.onrender.com/"

    fun getClient(context: Context): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
