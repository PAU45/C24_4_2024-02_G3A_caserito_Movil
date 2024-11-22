package com.melendez.paulo.frontend_proyecto.network

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val username: String,
    val msg: String,
    @SerializedName("jwt") val token: String,  // Mapeo de 'jwt' a 'token'
    val roles: List<String>,
    val status: Boolean
)
