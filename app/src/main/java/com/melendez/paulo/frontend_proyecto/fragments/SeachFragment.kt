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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melendez.paulo.frontend_proyecto.R
import com.melendez.paulo.frontend_proyecto.adapters.SearchAdapter
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
    private lateinit var rvResults: RecyclerView
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchInput = view.findViewById(R.id.etSearch)
        searchButton = view.findViewById(R.id.btnSearch)
        tvAlert = view.findViewById(R.id.tvAlert)
        rvResults = view.findViewById(R.id.rvResults)

        setupRecyclerView()
        loadAllRestaurants()

        searchButton.setOnClickListener {
            val nombre = searchInput.text.toString().trim()
            if (nombre.isNotEmpty()) {
                searchRestaurantsByName(nombre)
            } else {
                loadAllRestaurants()
            }
        }
    }

    private fun setupRecyclerView() {
        rvResults.layoutManager = LinearLayoutManager(requireContext())
        searchAdapter = SearchAdapter(mutableListOf(), requireContext()) { restaurant ->
            if (restaurant != null) {
                val bundle = Bundle().apply {
                    putParcelable("restaurant", restaurant)
                }
                findNavController().navigate(R.id.action_searchFragment_to_detalles, bundle)
            } else {
                Toast.makeText(requireContext(), "Restaurant data is missing", Toast.LENGTH_SHORT).show()
            }
        }
        rvResults.adapter = searchAdapter
    }

    private fun loadAllRestaurants() {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        val token = getTokenFromPreferences()

        apiService.getAllRestaurants("Bearer $token").enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>) {
                if (response.isSuccessful) {
                    val restaurants = response.body() ?: emptyList()
                    if (restaurants.isNotEmpty()) {
                        updateRestaurantList(restaurants)
                    } else {
                        showNoResultsMessage("No hay restaurantes disponibles.")
                    }
                } else {
                    showNoResultsMessage("Error al cargar los restaurantes.")
                }
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                showNoResultsMessage("Error de red: ${t.message}")
            }
        })
    }

    private fun searchRestaurantsByName(nombre: String) {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        val token = getTokenFromPreferences()

        apiService.searchRestaurants("Bearer $token", nombre).enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>) {
                if (response.isSuccessful) {
                    val restaurants = response.body() ?: emptyList()
                    if (restaurants.isNotEmpty()) {
                        updateRestaurantList(restaurants)
                    } else {
                        showNoResultsMessage("No se encontraron restaurantes con ese nombre.")
                    }
                } else {
                    showNoResultsMessage("Error al buscar restaurantes.")
                }
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                showNoResultsMessage("Error de red: ${t.message}")
            }
        })
    }

    private fun updateRestaurantList(restaurants: List<Restaurant>) {
        tvAlert.visibility = View.GONE
        searchAdapter.updateData(restaurants)
    }

    private fun showNoResultsMessage(message: String) {
        tvAlert.text = message
        tvAlert.visibility = View.VISIBLE
    }

    private fun getTokenFromPreferences(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }
}