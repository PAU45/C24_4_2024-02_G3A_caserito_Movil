package com.melendez.paulo.frontend_proyecto.network

data class Restaurant(
    val restaurantId: Int, // Cambiado a Int para coincidir con el JSON
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    val tipo: String?,
    val img: String?
)