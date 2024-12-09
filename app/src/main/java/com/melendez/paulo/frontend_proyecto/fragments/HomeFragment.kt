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
import com.melendez.paulo.frontend_proyecto.Item
import com.melendez.paulo.frontend_proyecto.RecyclerViewAdapter
import com.melendez.paulo.frontend_proyecto.network.ApiClient
import com.melendez.paulo.frontend_proyecto.network.ApiService
import com.melendez.paulo.frontend_proyecto.network.FavoriteRequest
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
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
            return
        }

        val username = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getString("username", "Usuario")
        tvWelcome.text = "Bienvenido, $username"

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString()
            Toast.makeText(requireActivity(), "Buscando: $query", Toast.LENGTH_SHORT).show()
        }

        setupImageSlider()
        loadItems(token)
    }

    private fun setupImageSlider() {
        val sliderModels = ArrayList<SlideModel>().apply {
            add(SlideModel(imageUrl = "https://picsum.photos/200/300?grayscale", scaleType = ScaleTypes.CENTER_CROP))
            add(SlideModel(imageUrl = "https://picsum.photos/200/300?blur", scaleType = ScaleTypes.CENTER_CROP))
            add(SlideModel(imageUrl = "https://picsum.photos/200/300?grayscale", scaleType = ScaleTypes.CENTER_CROP))
            add(SlideModel(imageUrl = "https://picsum.photos/200/300?blur", scaleType = ScaleTypes.CENTER_CROP))
        }
        imageSlider.setImageList(sliderModels)
    }

    private fun getTokenFromPreferences(): String? {
        return requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getString("token", null)
    }

    private fun loadItems(token: String) {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        apiService.getAllRestaurants("Bearer $token").enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>) {
                if (response.isSuccessful) {
                    val items = response.body()?.map { restaurant ->
                        Item(
                            id = restaurant.restaurantId, // Usar restaurantId directamente
                            title = restaurant.nombre,
                            description = restaurant.descripcion,
                            type = restaurant.tipo ?: "",
                            location = restaurant.ubicacion,
                            imageUrl = restaurant.img ?: "",
                            isFavorite = loadFavoriteState(restaurant.restaurantId) // Cargar estado de favorito
                        )
                    } ?: emptyList()

                    setupAdapters(items, token)
                } else {
                    Toast.makeText(requireContext(), "Error al cargar los items", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupAdapters(items: List<Item>, token: String) {
        adapter = RecyclerViewAdapter(items, token, ::addFavorite, ::saveFavoriteState, ::loadFavoriteState)
        viewPager.adapter = adapter

        anotherViewPager.adapter = RecyclerViewAdapter(items, token, ::addFavorite, ::saveFavoriteState, ::loadFavoriteState)
        categoriesViewPager.adapter = RecyclerViewAdapter(items, token, ::addFavorite, ::saveFavoriteState, ::loadFavoriteState)
        ratingsViewPager.adapter = RecyclerViewAdapter(items, token, ::addFavorite, ::saveFavoriteState, ::loadFavoriteState)
    }

    private fun addFavorite(token: String, restauranteId: Int) { // Cambiado a Int
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        val favoriteRequest = FavoriteRequest(restauranteId.toLong()) // Asegúrate de que el ID sea Long
        Log.d("HomeFragment", "Adding favorite with token: $token and restauranteId: $restauranteId")

        apiService.addFavorite("Bearer $token", favoriteRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("HomeFragment", "Favorite added successfully")
                    Toast.makeText(requireContext(), "Favorito agregado con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("HomeFragment", "Error adding favorite: $errorMessage")
                    Toast.makeText(requireContext(), "Error al agregar favorito: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("HomeFragment", "Failure adding favorite", t)
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun saveFavoriteState(restauranteId: Int, isFavorite: Boolean) {
        val sharedPreferences = requireActivity().getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("favorite_$restauranteId", isFavorite)
        editor.apply()
    }

    private fun loadFavoriteState(restauranteId: Int): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("favorite_$restauranteId", false)
    }
}