package com.melendez.paulo.frontend_proyecto.network

data class FavoriteRestaurant(
    val favoriteId: Int,
    val restaurantId: Int,
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    val tipo: String?,
    val img: String?,
    val horaApertura: String?,
    val horaCierre: String?,
    val distancia: String?,
    val tiempo: String?,
    val calificacion: Double
)