package com.melendez.paulo.frontend_proyecto

data class Item(
    val id: Int,
    val title: String,
    val description: String,
    val type: String,
    val location: String,
    val imageUrl: String,
    val isFavorite: Boolean,
    val favoriteId: Int
)