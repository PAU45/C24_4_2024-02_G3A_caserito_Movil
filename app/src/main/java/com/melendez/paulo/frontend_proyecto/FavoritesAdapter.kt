package com.melendez.paulo.frontend_proyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.melendez.paulo.frontend_proyecto.network.FavoriteRestaurant
import com.melendez.paulo.frontend_proyecto.R

class FavoritesAdapter(private val favoriteRestaurants: List<FavoriteRestaurant>) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvRestaurantName)
        val location: TextView = view.findViewById(R.id.tvRestaurantLocation)
        val description: TextView = view.findViewById(R.id.tvRestaurantDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteRestaurant = favoriteRestaurants[position]
        holder.name.text = favoriteRestaurant.nombre
        holder.location.text = favoriteRestaurant.ubicacion
        holder.description.text = favoriteRestaurant.descripcion
    }

    override fun getItemCount() = favoriteRestaurants.size
}