package com.melendez.paulo.frontend_proyecto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.melendez.paulo.frontend_proyecto.databinding.ActivityRestaurantDetailBinding
import com.melendez.paulo.frontend_proyecto.network.Restaurant

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val restaurant = intent.getParcelableExtra<Restaurant>("restaurant")
        restaurant?.let {
            binding.tvRestaurantName.text = it.nombre
            binding.tvRestaurantLocation.text = it.ubicacion
            binding.tvRestaurantDescription.text = it.descripcion
            Glide.with(this).load(it.img).into(binding.ivRestaurantImage)
        }
    }
}