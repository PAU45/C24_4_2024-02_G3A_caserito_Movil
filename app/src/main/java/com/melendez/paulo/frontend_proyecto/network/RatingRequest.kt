package com.melendez.paulo.frontend_proyecto.network

data class RatingRequest(
    val restauranteId: Int,
    val calificacion: Int
)