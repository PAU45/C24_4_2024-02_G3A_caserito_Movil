package com.melendez.paulo.frontend_proyecto.network

data class RatingRequest(
    val restauranteId: String,
    val calificacion: Int
)