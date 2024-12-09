package com.melendez.paulo.frontend_proyecto

data class Item(
    val id: Int, // Manteniendo como Int
    val title: String,
    val description: String,
    val type: String,
    val location: String,
    val imageUrl: String,
    var isFavorite: Boolean = false // Añadido como variable mutable
)