package com.melendez.paulo.frontend_proyecto.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.melendez.paulo.frontend_proyecto.R
import com.melendez.paulo.frontend_proyecto.network.Menu

class MenusAdapter(private val menus: List<Menu>) : RecyclerView.Adapter<MenusAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.menu_name)
        val description: TextView = view.findViewById(R.id.menu_description)
        val image: ImageView = view.findViewById(R.id.menu_image)
        val price: TextView = view.findViewById(R.id.menu_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = menus[position]
        holder.name.text = menu.nombre
        holder.description.text = menu.descripcion
        holder.price.text = menu.precio.toString()
        Glide.with(holder.itemView.context).load(menu.img).into(holder.image)
    }

    override fun getItemCount() = menus.size
}