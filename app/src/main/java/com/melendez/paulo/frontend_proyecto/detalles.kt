package com.melendez.paulo.frontend_proyecto.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.melendez.paulo.frontend_proyecto.R
import com.melendez.paulo.frontend_proyecto.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.melendez.paulo.frontend_proyecto.adapters.CommentsAdapter
import com.melendez.paulo.frontend_proyecto.adapters.MenusAdapter
import com.melendez.paulo.frontend_proyecto.adapters.RatingsAdapter

class detalles : Fragment(R.layout.fragment_restaurant_detail) {

    private var restaurant: Restaurant? = null
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var ratingsRecyclerView: RecyclerView
    private lateinit var menusRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurant = arguments?.getParcelable("restaurant")

        // Use restaurant data to populate the UI
        restaurant?.let {
            view.findViewById<TextView>(R.id.title).text = it.nombre
            view.findViewById<TextView>(R.id.description).text = it.descripcion
            view.findViewById<TextView>(R.id.location).text = it.ubicacion
            view.findViewById<TextView>(R.id.type).text = it.tipo
            view.findViewById<TextView>(R.id.opening_hours).text = it.horaApertura
            view.findViewById<TextView>(R.id.closing_hours).text = it.horaCierre
            view.findViewById<TextView>(R.id.distance).text = it.distancia
            view.findViewById<TextView>(R.id.time).text = it.tiempo
            view.findViewById<TextView>(R.id.rating).text = it.calificacion.toString()

            val imageView = view.findViewById<ImageView>(R.id.image)
            Glide.with(this).load(it.img).into(imageView)
        }

        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView)
        ratingsRecyclerView = view.findViewById(R.id.ratingsRecyclerView)
        menusRecyclerView = view.findViewById(R.id.menusRecyclerView)

        setupRecyclerViews()
        loadComments()
        loadRatings()
        loadMenus()

        val commentInput = view.findViewById<EditText>(R.id.commentInput)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val submitCommentButton = view.findViewById<Button>(R.id.submitCommentButton)

        submitCommentButton.setOnClickListener {
            val comment = commentInput.text.toString().trim()
            val rating = ratingBar.rating

            if (comment.isNotEmpty() && rating > 0) {
                submitCommentAndRating(comment, rating)
            } else {
                Toast.makeText(requireContext(), "Please enter a comment and rating", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerViews() {
        commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        ratingsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        menusRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadComments() {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        apiService.getCommentsByRestaurant(3).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    val comments = response.body() ?: emptyList()
                    if (comments.isEmpty()) {
                        Toast.makeText(requireContext(), "No comments available", Toast.LENGTH_SHORT).show()
                    } else {
                        commentsRecyclerView.adapter = CommentsAdapter(comments)
                        Log.d("Comments", "Comments loaded successfully: $comments")
                    }
                } else {
                    Log.e("Comments", "Failed to load comments: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to load comments", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Log.e("Comments", "Network error: ${t.message}")
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadRatings() {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        apiService.getRatingsByRestaurant(restaurant?.restaurantId ?: 0).enqueue(object : Callback<List<Rating>> {
            override fun onResponse(call: Call<List<Rating>>, response: Response<List<Rating>>) {
                if (response.isSuccessful) {
                    val ratings = response.body() ?: emptyList()
                    ratingsRecyclerView.adapter = RatingsAdapter(ratings)
                    Log.d("Ratings", "Ratings loaded successfully: $ratings")
                } else {
                    Log.e("Ratings", "Failed to load ratings: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to load ratings", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Rating>>, t: Throwable) {
                Log.e("Ratings", "Network error: ${t.message}")
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadMenus() {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        apiService.getMenusByRestaurant(restaurant?.restaurantId ?: 0).enqueue(object : Callback<List<Menu>> {
            override fun onResponse(call: Call<List<Menu>>, response: Response<List<Menu>>) {
                if (response.isSuccessful) {
                    val menus = response.body() ?: emptyList()
                    menusRecyclerView.adapter = MenusAdapter(menus)
                    Log.d("Menus", "Menus loaded successfully: $menus")
                } else {
                    Log.e("Menus", "Failed to load menus: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to load menus", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Menu>>, t: Throwable) {
                Log.e("Menus", "Network error: ${t.message}")
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun submitCommentAndRating(comment: String, rating: Float) {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        val token = getTokenFromPreferences()

        val commentRequest = CommentRequest(
            restauranteId = restaurant?.restaurantId ?: 0,
            comentario = comment
        )

        apiService.addComment("Bearer $token", commentRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Comment and rating submitted", Toast.LENGTH_SHORT).show()
                    loadComments()
                    loadRatings()
                } else {
                    Log.e("Submit", "Failed to submit comment and rating: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to submit comment and rating", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Submit", "Network error: ${t.message}")
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getTokenFromPreferences(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }
}