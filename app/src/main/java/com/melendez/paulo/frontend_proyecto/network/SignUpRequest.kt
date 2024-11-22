package com.melendez.paulo.frontend_proyecto.network

data class SignUpRequest(
    val username: String,
    val password: String,
    val email: String,
    val telefono: String,
    val direccion: String
)
