package com.melendez.paulo.frontend_proyecto

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.melendez.paulo.frontend_proyecto.network.FavoriteRestaurant
import com.melendez.paulo.frontend_proyecto.network.Restaurant

class RestaurantAdapter(
    private val restaurants: MutableList<Restaurant>,
    private val token: String,
    private val addFavorite: (String, Int) -> Unit,
    private val removeFavorite: (String, FavoriteRestaurant) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleCard)
        val description: TextView = view.findViewById(R.id.textView3)
        val image: ImageView = view.findViewById(R.id.image)
        val favorite: ImageView = view.findViewById(R.id.ivFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.title.text = restaurant.nombre ?: "N/A"
        holder.description.text = restaurant.descripcion ?: "N/A"
        Glide.with(holder.itemView.context).load(restaurant.img).into(holder.image)

        val favoriteRestaurant = FavoriteRestaurant(
            favoriteId = restaurant.restaurantId,
            restaurantId = restaurant.restaurantId,
            nombre = restaurant.nombre ?: "N/A",
            descripcion = restaurant.descripcion ?: "N/A",
            ubicacion = restaurant.ubicacion ?: "N/A",
            tipo = restaurant.tipo ?: "N/A",
            img = restaurant.img ?: "",
            horaApertura = restaurant.horaApertura ?: "N/A",
            horaCierre = restaurant.horaCierre ?: "N/A",
            distancia = restaurant.distancia ?: "N/A",
            tiempo = restaurant.tiempo ?: "N/A",
            calificacion = restaurant.calificacion.toDouble() // Convert Float to Double
        )

        val isFavorite = loadFavoriteState(restaurant.restaurantId)
        holder.favorite.setImageResource(if (isFavorite) R.drawable.ic_favorite else R.drawable.heart)

        holder.favorite.setOnClickListener {
            if (isFavorite) {
                removeFavorite(token, favoriteRestaurant)
                saveFavoriteState(restaurant.restaurantId, false)
                holder.favorite.setImageResource(R.drawable.heart)
            } else {
                addFavorite(token, restaurant.restaurantId)
                saveFavoriteState(restaurant.restaurantId, true)
                holder.favorite.setImageResource(R.drawable.ic_favorite)
            }
        }
    }

    override fun getItemCount() = restaurants.size

    fun removeRestaurantById(restaurantId: Int) {
        val position = restaurants.indexOfFirst { it.restaurantId == restaurantId }
        if (position != -1) {
            restaurants.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun saveFavoriteState(favoriteId: Int, isFavorite: Boolean) {
        val sharedPreferences = context.getSharedPreferences("favorite_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("favorite_$favoriteId", isFavorite)
        editor.apply()
    }

    private fun loadFavoriteState(favoriteId: Int): Boolean {
        val sharedPreferences = context.getSharedPreferences("favorite_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("favorite_$favoriteId", false)
    }
}
