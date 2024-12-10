package com.melendez.paulo.frontend_proyecto.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.melendez.paulo.frontend_proyecto.R
import com.melendez.paulo.frontend_proyecto.network.Restaurant

class SearchAdapter(
    private var restaurants: MutableList<Restaurant>,
    private val context: Context,
    private val onItemClick: (Restaurant) -> Unit
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRestaurantName: TextView = itemView.findViewById(R.id.tvRestaurantName)
        val tvRestaurantLocation: TextView = itemView.findViewById(R.id.tvRestaurantLocation)
        val tvRestaurantDescription: TextView = itemView.findViewById(R.id.tvRestaurantDescription)

        fun bind(item: Restaurant) {
            tvRestaurantName.text = item.nombre
            tvRestaurantLocation.text = item.ubicacion
            tvRestaurantDescription.text = item.descripcion

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount(): Int = restaurants.size

    fun updateData(newRestaurants: List<Restaurant>) {
        restaurants.clear()
        restaurants.addAll(newRestaurants)
        notifyDataSetChanged()
    }
}