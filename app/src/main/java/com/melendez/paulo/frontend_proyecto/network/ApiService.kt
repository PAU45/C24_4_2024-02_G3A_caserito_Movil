package com.melendez.paulo.frontend_proyecto.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

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
    fun getAllRestaurants(
        @Header("Authorization") token: String
    ): Call<List<Restaurant>>

    @POST("caserito_api/calificacion/agregar")
    fun addRating(
        @Header("Authorization") token: String,
        @Body ratingRequest: RatingRequest
    ): Call<Void>

    @GET("caserito_api/favorito")
    fun getFavoriteRestaurants(@Header("Authorization") token: String): Call<List<FavoriteRestaurant>>

    @POST("caserito_api/favorito/agregar")
    fun addFavorite(
        @Header("Authorization") token: String,
        @Body body: Map<String, Int>
    ): Call<Void>

    @POST("caserito_api/favorito/eliminar")
    fun removeFavorite(
        @Header("Authorization") token: String,
        @Body favoritoRequest: FavoritoRequest
    ): Call<Void>

    @GET("caserito_api/restaurante/buscar")
    fun searchRestaurants(
        @Header("Authorization") token: String,
        @Query("nombre") nombre: String
    ): Call<List<Restaurant>>

    @POST("caserito_api/comentarios/agregar")
    fun addComment(
        @Header("Authorization") token: String,
        @Body commentRequest: CommentRequest
    ): Call<Void>

    @GET("caserito_api/comentarios/restaurante/{id}")
    fun getCommentsByRestaurant(
        @Path("id") restaurantId: Int
    ): Call<List<Comment>>

    @GET("caserito_api/calificacion/restaurante/{id}")
    fun getRatingsByRestaurant(
        @Path("id") restaurantId: Int
    ): Call<List<Rating>>

    @GET("caserito_api/menu/{id_restaurante}")
    fun getMenusByRestaurant(
        @Path("id_restaurante") restaurantId: Int
    ): Call<List<Menu>>

    @GET("caserito_api/restaurante/{id}/ruta")
    fun getRoute(@Path("id") id: String): Call<RouteResponse>
}