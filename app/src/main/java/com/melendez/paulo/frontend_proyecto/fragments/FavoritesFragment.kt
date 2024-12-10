package com.melendez.paulo.frontend_proyecto.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melendez.paulo.frontend_proyecto.R
import com.melendez.paulo.frontend_proyecto.FavoritesAdapter
import com.melendez.paulo.frontend_proyecto.network.ApiClient
import com.melendez.paulo.frontend_proyecto.network.ApiService
import com.melendez.paulo.frontend_proyecto.network.FavoriteRestaurant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesFragment : Fragment() {

    private lateinit var tvAlert: TextView
    private lateinit var rvFavorites: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvAlert = view.findViewById(R.id.tvAlert)
        rvFavorites = view.findViewById(R.id.rvFavorites)

        // Load favorite restaurants
        loadFavoriteRestaurants()
    }

    private fun loadFavoriteRestaurants() {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        val token = getTokenFromPreferences()

        apiService.getFavoriteRestaurants("Bearer $token").enqueue(object : Callback<List<FavoriteRestaurant>> {
            override fun onResponse(call: Call<List<FavoriteRestaurant>>, response: Response<List<FavoriteRestaurant>>) {
                if (response.isSuccessful) {
                    val favoriteRestaurants = response.body() ?: emptyList()
                    if (favoriteRestaurants.isEmpty()) {
                        tvAlert.text = "No tienes restaurantes favoritos."
                        tvAlert.visibility = View.VISIBLE
                    } else {
                        tvAlert.visibility = View.GONE
                        favoritesAdapter = FavoritesAdapter(favoriteRestaurants)
                        rvFavorites.layoutManager = LinearLayoutManager(requireContext())
                        rvFavorites.adapter = favoritesAdapter
                    }
                } else {
                    tvAlert.text = "Error al cargar los restaurantes favoritos."
                    tvAlert.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<FavoriteRestaurant>>, t: Throwable) {
                tvAlert.text = "Error de red: ${t.message}"
                tvAlert.visibility = View.VISIBLE
            }
        })
    }

    private fun getTokenFromPreferences(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }
}