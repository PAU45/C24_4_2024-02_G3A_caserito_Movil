package com.melendez.paulo.frontend_proyecto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Verificar si el token está presente en SharedPreferences
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token == null) {
            // Si no hay token, redirigir al login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Para que el usuario no pueda regresar a la MainActivity sin estar logueado
            return  // Asegurarse de que no se ejecute más código
        }

        // Configuración del Bottom Navigation
        try {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            bottomNavigationView = findViewById(R.id.bottom_navigation)
            bottomNavigationView.setupWithNavController(navController)

            // Manejo de clics en el botón de navegación
            bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home -> {
                        navController.navigate(R.id.homeFragment)
                        true
                    }
                    R.id.search -> {
                        navController.navigate(R.id.searchFragment)
                        true
                    }
                    R.id.favorites -> {
                        navController.navigate(R.id.favoritesFragment)
                        true
                    }
                    R.id.profile -> {
                        navController.navigate(R.id.profileFragment)
                        true
                    }
                    else -> false
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error setting up navigation", e)
            Toast.makeText(this, "Error setting up navigation", Toast.LENGTH_SHORT).show()
        }
    }
}