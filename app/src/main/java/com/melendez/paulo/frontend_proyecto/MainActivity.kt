package com.melendez.paulo.frontend_proyecto

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch: Button = findViewById(R.id.btnSearch)
        val etSearch: EditText = findViewById(R.id.etSearch)

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString()
            Toast.makeText(this, "Buscando: $query", Toast.LENGTH_SHORT).show()
        }
    }
}
