package com.melendez.paulo.frontend_proyecto.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.melendez.paulo.frontend_proyecto.R
import com.melendez.paulo.frontend_proyecto.LoginActivity
import com.melendez.paulo.frontend_proyecto.RestaurantAdapter
import com.melendez.paulo.frontend_proyecto.network.ApiClient
import com.melendez.paulo.frontend_proyecto.network.ApiService
import com.melendez.paulo.frontend_proyecto.network.FavoriteRestaurant
import com.melendez.paulo.frontend_proyecto.network.FavoritoRequest
import com.melendez.paulo.frontend_proyecto.network.Restaurant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnSearch: Button
    private lateinit var etSearch: EditText
    private lateinit var imageSlider: ImageSlider
    private lateinit var viewPager: ViewPager2
    private lateinit var anotherViewPager: ViewPager2
    private lateinit var categoriesViewPager: ViewPager2
    private lateinit var ratingsViewPager: ViewPager2
    private lateinit var restaurantAdapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvWelcome = view.findViewById(R.id.tvWelcome)
        btnSearch = view.findViewById(R.id.btnSearch)
        etSearch = view.findViewById(R.id.etSearch)
        imageSlider = view.findViewById(R.id.slider)
        viewPager = view.findViewById(R.id.viewPager)
        anotherViewPager = view.findViewById(R.id.anotherViewPager)
        categoriesViewPager = view.findViewById(R.id.categoriesViewPager)
        ratingsViewPager = view.findViewById(R.id.ratingsViewPager)

        val token = getTokenFromPreferences()

        if (token == null) {
            Log.e("HomeFragment", "Token no encontrado en SharedPreferences")
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
            return
        }

        val username = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getString("username", "Usuario")
        tvWelcome.text = "Bienvenido, $username"
        Log.d("HomeFragment", "Usuario cargado: $username")

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString()
            if (query.isNotEmpty()) {
                Log.d("HomeFragment", "Buscando: $query")
                Toast.makeText(requireActivity(), "Buscando: $query", Toast.LENGTH_SHORT).show()
            } else {
                Log.w("HomeFragment", "El campo de búsqueda está vacío")
                Toast.makeText(requireActivity(), "Por favor, ingresa un término de búsqueda", Toast.LENGTH_SHORT).show()
            }
        }

        setupImageSlider()
        loadRestaurants(token)
    }

    private fun setupImageSlider() {
        val sliderModels = ArrayList<SlideModel>().apply {
            add(SlideModel(imageUrl = "https://pbs.twimg.com/media/F-HHT-dWQAAtaI7.jpg", scaleType = ScaleTypes.CENTER_CROP))
            add(SlideModel(imageUrl = "https://www.tarjetacencosud.pe/wp-content/uploads/2024/10/Caja-web-Rokys-786x420-1.png", scaleType = ScaleTypes.CENTER_CROP))
        }
        imageSlider.setImageList(sliderModels)
    }

    private fun getTokenFromPreferences(): String? {
        val token = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getString("token", null)
        Log.d("HomeFragment", "Token obtenido: $token")
        return token
    }

    private fun loadRestaurants(token: String) {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        apiService.getAllRestaurants("Bearer $token").enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>) {
                if (response.isSuccessful) {
                    val restaurants = response.body()?.toMutableList() ?: mutableListOf()
                    Log.d("HomeFragment", "Restaurantes obtenidos: ${restaurants.size}")
                    if (restaurants.isEmpty()) {
                        Toast.makeText(requireContext(), "No hay restaurantes disponibles.", Toast.LENGTH_SHORT).show()
                    } else {
                        restaurantAdapter = RestaurantAdapter(
                            restaurants,
                            token,
                            { token, id -> addFavorite(token, id) },
                            { token, favoriteRestaurant -> removeFavorite(token, favoriteRestaurant) },
                            requireContext()
                        )
                        viewPager.adapter = restaurantAdapter
                        anotherViewPager.adapter = restaurantAdapter
                        categoriesViewPager.adapter = restaurantAdapter
                        ratingsViewPager.adapter = restaurantAdapter
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("HomeFragment", "Error al cargar los restaurantes: $errorBody")
                    Toast.makeText(requireContext(), "Error al cargar los restaurantes.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                Log.e("HomeFragment", "Error de red al cargar restaurantes: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addFavorite(token: String, restauranteId: Int) {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        val body = mapOf("restauranteId" to restauranteId)
        Log.d("HomeFragment", "Añadiendo a favoritos: Restaurante ID = $restauranteId")

        apiService.addFavorite("Bearer $token", body).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("HomeFragment", "Añadido a favoritos con éxito: Restaurante ID = $restauranteId")
                    Toast.makeText(requireContext(), "Añadido a favoritos", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("HomeFragment", "Error al añadir a favoritos: $errorBody")
                    Toast.makeText(requireContext(), "Error al añadir a favoritos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("HomeFragment", "Error de red al añadir a favoritos: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeFavorite(token: String, favoriteRestaurant: FavoriteRestaurant) {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        val favoritoRequest = FavoritoRequest(favoriteRestaurant.favoriteId)
        Log.d("HomeFragment", "Intentando eliminar de favoritos: ${favoriteRestaurant.favoriteId}")

        apiService.removeFavorite("Bearer $token", favoritoRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("HomeFragment", "Eliminado de favoritos con éxito: ${favoriteRestaurant.favoriteId}")
                    restaurantAdapter.removeRestaurantById(favoriteRestaurant.restaurantId)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("HomeFragment", "Error al eliminar de favoritos: $errorBody")
                    Toast.makeText(requireContext(), "Error al eliminar de favoritos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("HomeFragment", "Error de red al eliminar de favoritos: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
