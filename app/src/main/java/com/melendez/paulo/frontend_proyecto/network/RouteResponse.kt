
package com.melendez.paulo.frontend_proyecto.network

data class RouteResponse(
    val apiKey: String,
    val origin: Coordinate,
    val destination: Coordinate
)

data class Coordinate(
    val lng: Double,
    val lat: Double
)