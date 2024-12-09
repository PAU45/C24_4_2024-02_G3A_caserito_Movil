package com.melendez.paulo.frontend_proyecto.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header

interface ApiService {
    @POST("caserito_api/authentication/log-in")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("caserito_api/authentication/sign-up")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<LoginResponse>

    @GET("caserito_api/user/me")
    fun getUserProfile(@Header("Authorization") token: String): Call<UserResponse>

    @POST("caserito_api/user/update-user")
    fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body userRequest: UserRequest
    ): Call<UserResponse>

    @POST("caserito_api/user/update-role")
    fun updateUserRole(
        @Header("Authorization") token: String,
        @Body roleRequest: RoleRequest
    ): Call<Void>
    @GET("caserito_api/restaurante/all")
    fun getAllRestaurants(@Header("Authorization") token: String): Call<List<Restaurant>>

    @POST("caserito_api/calificacion/agregar")
    fun addRating(
        @Header("Authorization") token: String,
        @Body ratingRequest: RatingRequest
    ): Call<Void>

    @POST("caserito_api/favorito/agregar")
    fun addFavorite(
        @Header("Authorization") token: String,
        @Body favoriteRequest: FavoriteRequest
    ): Call<Void>

    @GET("caserito_api/favorito")
    fun getFavoriteRestaurants(
        @Header("Authorization") token: String
    ): Call<List<Restaurant>>
}