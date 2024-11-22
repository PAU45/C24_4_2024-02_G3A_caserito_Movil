package com.melendez.paulo.frontend_proyecto.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(context: Context) : Interceptor {
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString("access_token", "") ?: ""
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
