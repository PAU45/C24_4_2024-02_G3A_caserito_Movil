package com.melendez.paulo.frontend_proyecto.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("caserito_api/authentication/log-in")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("caserito_api/authentication/sign-up")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<LoginResponse>
}
