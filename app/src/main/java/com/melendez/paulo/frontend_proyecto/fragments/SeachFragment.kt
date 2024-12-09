package com.melendez.paulo.frontend_proyecto.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melendez.paulo.frontend_proyecto.FavoritesAdapter
import com.melendez.paulo.frontend_proyecto.R
import com.melendez.paulo.frontend_proyecto.network.ApiClient
import com.melendez.paulo.frontend_proyecto.network.ApiService
import com.melendez.paulo.frontend_proyecto.network.Restaurant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button
    private lateinit var tvAlert: TextView
    private lateinit var rvFavorites: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchInput = view.findViewById(R.id.etSearch)
        searchButton = view.findViewById(R.id.btnSearch)
        tvAlert = view.findViewById(R.id.tvAlert)
        rvFavorites = view.findViewById(R.id.rvFavorites)

        searchButton.setOnClickListener {
            val location = searchInput.text.toString()
            if (location.isNotEmpty()) {
                searchLocation(location)
            } else {
                Toast.makeText(requireContext(), "Please enter a location", Toast.LENGTH_SHORT).show()
            }
        }

        // Load all restaurants initially
        loadAllRestaurants()
    }

    private fun loadAllRestaurants() {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        val token = getTokenFromPreferences()

        apiService.getAllRestaurants("Bearer $token").enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>) {
                if (response.isSuccessful) {
                    val restaurants = response.body() ?: emptyList()
                    if (restaurants.isEmpty()) {
                        tvAlert.text = "No hay restaurantes disponibles."
                        tvAlert.visibility = View.VISIBLE
                    } else {
                        tvAlert.visibility = View.GONE
                        favoritesAdapter = FavoritesAdapter(restaurants)
                        rvFavorites.layoutManager = LinearLayoutManager(requireContext())
                        rvFavorites.adapter = favoritesAdapter
                    }
                } else {
                    tvAlert.text = "Error al cargar los restaurantes."
                    tvAlert.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                tvAlert.text = "Error de red: ${t.message}"
                tvAlert.visibility = View.VISIBLE
            }
        })
    }

    private fun searchLocation(location: String) {
        // Implement your search logic here
        Toast.makeText(requireContext(), "Searching for: $location", Toast.LENGTH_SHORT).show()
    }

    private fun getTokenFromPreferences(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }
}