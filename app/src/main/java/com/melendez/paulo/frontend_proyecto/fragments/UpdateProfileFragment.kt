package com.melendez.paulo.frontend_proyecto.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.melendez.paulo.frontend_proyecto.R
import com.melendez.paulo.frontend_proyecto.network.ApiClient
import com.melendez.paulo.frontend_proyecto.network.ApiService
import com.melendez.paulo.frontend_proyecto.network.UserRequest
import com.melendez.paulo.frontend_proyecto.network.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileFragment : Fragment() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etAvatar: EditText
    private lateinit var btnSaveProfile: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etUsername = view.findViewById(R.id.etUsername)
        etEmail = view.findViewById(R.id.etEmail)
        etDireccion = view.findViewById(R.id.etDireccion)
        etTelefono = view.findViewById(R.id.etTelefono)
        etAvatar = view.findViewById(R.id.etAvatar)
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile)

        val token = getTokenFromPreferences()

        btnSaveProfile.setOnClickListener {
            if (token != null) {
                updateUserProfile(token)
            } else {
                Toast.makeText(requireContext(), "No se encontró token.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTokenFromPreferences(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }

    private fun updateUserProfile(token: String) {
        val apiService = ApiClient.getClient(requireContext()).create(ApiService::class.java)
        val userRequest = UserRequest(
            usuario = etUsername.text.toString(),
            email = etEmail.text.toString(),
            direccion = etDireccion.text.toString(),
            telefono = etTelefono.text.toString(),
            avatar = etAvatar.text.toString()
        )

        val call = apiService.updateUserProfile("Bearer $token", userRequest)

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar el perfil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}