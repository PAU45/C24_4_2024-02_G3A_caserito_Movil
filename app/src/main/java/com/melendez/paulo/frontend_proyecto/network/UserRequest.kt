package com.melendez.paulo.frontend_proyecto.network

data class UserRequest(
    val usuario: String,
    val email: String,
    val direccion: String,
    val telefono: String,
    val avatar: String? // New field for avatar
)