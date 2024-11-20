package com.melendez.paulo.frontend_proyecto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SliderActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        val adapter = SliderAdapter(this) // Usa 'this' que es de tipo 'SliderActivity'
        viewPager.adapter = adapter

        // Mediador para los tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Slide ${position + 1}"
        }.attach()
    }

    private inner class SliderAdapter(fragmentActivity: SliderActivity) :
        FragmentStateAdapter(fragmentActivity) { // Cambia 'FragmentActivity' a 'SliderActivity'
        private val titles = listOf("Plato 1", "Plato 2", "Plato 3")
        private val imageUrls = listOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg"
        )

        override fun getItemCount(): Int {
            return titles.size
        }

        override fun createFragment(position: Int): Fragment {
            return SliderFragment.newInstance(titles[position], imageUrls[position])
        }
    }
}
