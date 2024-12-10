package com.melendez.paulo.frontend_proyecto

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.melendez.paulo.frontend_proyecto.network.Restaurant

import com.melendez.paulo.frontend_proyecto.fragments.detalles

class RecyclerViewAdapter(
    private val context: Context,
    private val items: List<Restaurant>,
    private val token: String,
    private val addFavorite: (String, Int) -> Unit,
    private val removeFavorite: (String, Int) -> Unit,
    private val saveFavoriteState: (Int, Boolean) -> Unit,
    private val loadFavoriteState: (Int) -> Boolean
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleCard: TextView = itemView.findViewById(R.id.titleCard)
        val textView3: TextView = itemView.findViewById(R.id.textView3)
        val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)
        val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(item: Restaurant) {
            titleCard.text = item.nombre
            textView3.text = item.descripcion

            // Load the image using Glide
            Glide.with(context)
                .load(item.img)
                .placeholder(R.color.black)
                .into(image)

            val isFavorite = loadFavoriteState(item.restaurantId)
            updateFavoriteIcon(isFavorite)

            ivFavorite.setOnClickListener {
                if (isFavorite) {
                    removeFavorite(token, item.restaurantId)
                    saveFavoriteState(item.restaurantId, false)
                } else {
                    addFavorite(token, item.restaurantId)
                    saveFavoriteState(item.restaurantId, true)
                }
                updateFavoriteIcon(!isFavorite)
            }

            itemView.setOnClickListener {
                val intent = Intent(context, detalles::class.java)
                intent.putExtra("restaurant", item)
                context.startActivity(intent)
            }
        }

        private fun updateFavoriteIcon(isFavorite: Boolean) {
            if (isFavorite) {
                ivFavorite.setImageResource(R.drawable.relleno)
            } else {
                ivFavorite.setImageResource(R.drawable.heart)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}