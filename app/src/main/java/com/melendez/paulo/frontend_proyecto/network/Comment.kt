package com.melendez.paulo.frontend_proyecto.network

data class Comment(
    val restauranteId: Int,
    val comentario: String,
    val username: String,
    val avatarUser: String?
)