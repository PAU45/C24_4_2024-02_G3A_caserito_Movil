package com.melendez.paulo.frontend_proyecto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class SliderFragment : Fragment() {

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_IMAGE_URL = "image_url"

        fun newInstance(title: String, imageUrl: String): SliderFragment {
            return SliderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_IMAGE_URL, imageUrl)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_slider, container, false)
        val titleTextView = view.findViewById<TextView>(R.id.textView)
        val foodImageView = view.findViewById<ImageView>(R.id.imageView)

        titleTextView.text = arguments?.getString(ARG_TITLE)
        val imageUrl = arguments?.getString(ARG_IMAGE_URL)

        // Cargar la imagen usando Glide
        Glide.with(this)
            .load(imageUrl)
            .into(foodImageView)

        return view
    }
}