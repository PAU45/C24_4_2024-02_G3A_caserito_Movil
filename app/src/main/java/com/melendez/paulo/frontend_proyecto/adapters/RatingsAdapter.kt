package com.melendez.paulo.frontend_proyecto.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.melendez.paulo.frontend_proyecto.R
import com.melendez.paulo.frontend_proyecto.network.Rating

class RatingsAdapter(private val ratings: List<Rating>) : RecyclerView.Adapter<RatingsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.username)
        val rating: TextView = view.findViewById(R.id.rating)
        val avatar: ImageView = view.findViewById(R.id.avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rating, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rating = ratings[position]
        holder.username.text = rating.username
        holder.rating.text = rating.calificacion.toString()
        Glide.with(holder.itemView.context).load(rating.avatarUser).into(holder.avatar)
    }

    override fun getItemCount() = ratings.size
}