package com.melendez.paulo.frontend_proyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    private val items: List<Item>,
    private val token: String,
    private val addFavorite: (String, Int) -> Unit // Cambiar a Int
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleCard: TextView = itemView.findViewById(R.id.titleCard)
        val textView3: TextView = itemView.findViewById(R.id.textView3)
        val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.titleCard.text = item.title
        holder.textView3.text = item.description

        // Cambiar el icono según el estado de favorito
        holder.ivFavorite.setImageResource(
            if (item.isFavorite) R.drawable.relleno else R.drawable.heart
        )

        holder.ivFavorite.setOnClickListener {
            if (item.isFavorite) {
                // Lógica para eliminar de favoritos
                holder.ivFavorite.setImageResource(R.drawable.heart) // Cambiar a corazón vacío
            } else {
                addFavorite(token, item.id) // Aquí se usa Int
                holder.ivFavorite.setImageResource(R.drawable.relleno) // Cambiar a corazón lleno
            }
            item.isFavorite = !item.isFavorite // Alternar estado
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}