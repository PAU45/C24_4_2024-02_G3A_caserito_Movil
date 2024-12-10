package com.melendez.paulo.frontend_proyecto.network

data class Rating(
    val restauranteId: Int,
    val calificacion: Int,
    val username: String,
    val avatarUser: String?
)