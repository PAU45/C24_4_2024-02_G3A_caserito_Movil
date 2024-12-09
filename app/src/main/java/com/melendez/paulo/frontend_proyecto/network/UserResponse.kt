package com.melendez.paulo.frontend_proyecto.network


data class UserResponse(
    val usuario: String?,
    val email: String?,
    val direccion: String?,
    val telefono: String?,
    val roles: List<String>? // Ensure roles is a list
)

